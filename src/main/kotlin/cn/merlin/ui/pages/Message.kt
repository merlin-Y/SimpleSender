package cn.merlin.ui.pages

import AccessFileDialog
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.merlin.bean.Device
import cn.merlin.bean.File
import cn.merlin.bean.Message
import cn.merlin.bean.model.DeviceViewModel
import cn.merlin.bean.model.FileViewModel
import cn.merlin.bean.model.MessageVIewModel
import cn.merlin.database.SenderDB
import cn.merlin.network.NetworkController
import cn.merlin.tools.*
import cn.merlin.ui.theme.TWEEN_DURATION
import cn.merlin.ui.theme.TriangleLeftShape
import cn.merlin.ui.theme.TriangleRightShape
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

val currentDevice = mutableStateOf(DeviceViewModel(Device()))

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun message(
    width: Dp,
    height: Dp,
    senderDB: SenderDB,
    networkController: NetworkController
) {
    var isDragging by remember { mutableStateOf(false) }
    val showAccessFileDialog = remember{ mutableStateOf(false) }
    val isDraggingColore = animateColorAsState(
        if (isDragging) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.secondary,
        TweenSpec(TWEEN_DURATION)
    )
    val inputText = remember {
        mutableStateOf(
            TextFieldValue(if (messageListMap.contains(currentDevice.value.deviceIdentifier.value)) messageListMap[currentDevice.value.deviceIdentifier.value]
                ?: "" else "")
        )
    }
    val messageListView: SnapshotStateList<MessageVIewModel> = remember { mutableStateListOf() }
    val fileList: SnapshotStateList<FileViewModel> = remember { mutableStateListOf() }

    LaunchedEffect(isRequestListFlushed.value) {
        if(!receiveRequestList.isEmpty()){
            showAccessFileDialog.value = true
        }
    }

    AccessFileDialog(showAccessFileDialog)

    Surface(
        modifier = Modifier.size(width, height),
        color = MaterialTheme.colorScheme.secondary
    ) {
        Column {
            Row {
                Text(
                    modifier = Modifier.size(width, 40.dp).padding(start = 20.dp).weight(5f),
                    text = currentDevice.value.deviceName.value,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontSize = 16.sp,
                    maxLines = 1,
                    lineHeight = 32.sp
                )
                IconButton(
                    onClick = {
                        currentDevice.value.inListType.value = !currentDevice.value.inListType.value
                        if (currentDevice.value.inListType.value) {
                            currentDevice.value.deviceId.value = senderDB.insertDevice(currentDevice.value)
                        } else {
                            senderDB.deleteDeviceById(currentDevice.value.deviceId.value)
                        }
                    },
                    modifier = Modifier.height(40.dp)
                ) {
                    Icon(
                        Icons.Filled.Star,
                        "",
                        modifier = Modifier.weight(1f),
                        tint = if (currentDevice.value.inListType.value) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
            Spacer(modifier = Modifier.background(MaterialTheme.colorScheme.tertiary).height(2.dp).width(width))
            LazyColumn(
                modifier = Modifier.padding(0.dp).weight(5f).width(width).padding(top = 10.dp)
                    .border(1.dp, isDraggingColore.value),
//                    .dragAndDropTarget(
//                        onDragStart = {
//                            isDragging = true
//                        },
//                        onDragExit = {
//                            isDragging = false
//                        },
//                        onDrag = {
//
//                        },
//                        onDrop = { state ->
//                            val dragData = state.dragData
//                            if (dragData is DragData.Image) {
//                                println(dragData.readImage().toString())
//                            } else if (dragData is DragData.FilesList && isDragging) {
//                                dragData.readFiles().first().let {
//                                    val message = Message(
//                                        messageSenderIdentifier = currentDevice.value.deviceIdentifier.value,
//                                        messageReceiverIdentifier = currentDevice.value.deviceIdentifier.value,
//                                        messageContent = it.substring(6)
//                                    )
//                                    println(message.messageContent)
//                                    messageListView.add(MessageVIewModel(message))
//                                    senderDB.insertMessage(MessageVIewModel(message))
//                                    println(it)
//                                }
//                            }
//                            isDragging = false
//                        }
//                    ),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                reverseLayout = true
            ) {
                items(messageListView) {
                    if (it.messageSenderIdentifier.value == currentDevice.value.deviceIdentifier.value) {
                        SentMessageRow(it)
                    } else {
                        ReceiveMessageRow(it)
                    }
                }
            }
            Spacer(modifier = Modifier.background(MaterialTheme.colorScheme.tertiary).height(2.dp).fillMaxWidth())
            TextField(
                modifier = Modifier
                    .height(160.dp)
                    .fillMaxWidth()
                    .border(1.dp, isDraggingColore.value),
//                    .onExternalDrag(
//                        onDragStart = {
//                            isDragging = true
//                        },
//                        onDragExit = {
//                            isDragging = false
//                        },
//                        onDrag = {
//
//                        },
//                        onDrop = { state ->
//                            val dragData = state.dragData
//                            if (dragData is DragData.Image) {
//                                println(dragData.readImage().toString())
//                            } else if (dragData is DragData.FilesList && isDragging) {
//                                dragData.readFiles().first().let {
//                                    val file = it.substring(6)
//                                    if(!messageFileListMap.contains(currentDevice.value.deviceIdentifier.value)){
//                                        val list = mutableListOf(file)
//                                        messageFileListMap.put(currentDevice.value.deviceIdentifier.value,list)
//                                    }else{
//                                        messageFileListMap[currentDevice.value.deviceIdentifier.value]!!.add(file)
//                                    }
//                                }
//                            }
//                            isDragging = false
//                        }
//                    ),
                value = inputText.value,
                onValueChange = {
                    inputText.value = it
                    if (!messageListMap.contains(currentDevice.value.deviceIdentifier.value)) {
                        messageListMap.put(currentDevice.value.deviceIdentifier.value, inputText.value.text)
                    }
                    messageListMap[currentDevice.value.deviceIdentifier.value] = inputText.value.text
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            CoroutineScope(Dispatchers.Default).launch {
                                if (inputText.value.text.isNotEmpty()) {
                                    val message = Message(
                                        messageSenderIdentifier = cn.merlin.tools.currentDevice.value.deviceIdentifier.value,
                                        messageReceiverIdentifier = currentDevice.value.deviceIdentifier.value,
                                        messageContent = inputText.value.text
                                    )
//                                    messageListView.add(MessageVIewModel(message))
//                                    senderDB.insertMessage(MessageVIewModel(message))
                                    inputText.value = TextFieldValue("")
                                    if (!messageListMap.contains(currentDevice.value.deviceIdentifier.value)) {
                                        messageListMap.put(currentDevice.value.deviceIdentifier.value, inputText.value.text)
                                    }
                                    messageListMap[currentDevice.value.deviceIdentifier.value] = inputText.value.text

                                    val requestCode = networkController.sendRequestCodeToSelectedDevice(currentDevice.value)
                                    when(requestCode.requestCode){
                                        0 -> {
                                            TODO("SendMessage")
                                        }
                                        1 -> {
                                            TODO("RejectAccess")
                                        }
                                        2 -> {
                                            TODO("Send error")
                                        }
                                        3 -> {
                                            TODO("TimeOut")
                                        }
                                    }
                                }
                            }
                        }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, "", tint = MaterialTheme.colorScheme.onSecondaryContainer)
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    cursorColor = MaterialTheme.colorScheme.onPrimary,
                    focusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                    disabledIndicatorColor = MaterialTheme.colorScheme.tertiary,
                    selectionColors = TextSelectionColors(
                        MaterialTheme.colorScheme.onSecondaryContainer,
                        MaterialTheme.colorScheme.onSecondaryContainer
                    )
                )
            )
        }
    }
}

@Composable
fun SentMessageRow(message: MessageVIewModel) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopEnd
    ) {
        Row(
            Modifier.height(IntrinsicSize.Max),
        ) {
            Column(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(4.dp, 4.dp, 0.dp, 4.dp)
                    )
                    .padding(start = 5.dp, end = 5.dp)
            ) {
                Box(modifier = Modifier.widthIn(0.dp, 400.dp)) {
                    BasicTextField(
                        value = message.messageContent.value,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.padding(5.dp),
                        textStyle = LocalTextStyle.current.copy(
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 18.sp
                        )
                    )
                }
            }
            Column(
                modifier = Modifier.background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = TriangleRightShape(10)
                )
                    .width(8.dp)
                    .fillMaxHeight()
            ) {}
        }
    }
}

@Composable
fun ReceiveMessageRow(message: MessageVIewModel) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopStart
    ) {
        Row(
            Modifier.height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.Start
        ) {
            Column(
                modifier = Modifier.background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = TriangleLeftShape(10)
                )
                    .width(8.dp)
                    .fillMaxHeight()
            ) {}
            Column(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(4.dp, 4.dp, 0.dp, 4.dp)
                    )
                    .padding(start = 5.dp, end = 5.dp)
            ) {
                Box(modifier = Modifier.widthIn(0.dp, 400.dp)) {
                    BasicTextField(
                        value = message.messageContent.value,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.padding(5.dp),
                        textStyle = LocalTextStyle.current.copy(
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 18.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun FileBox(file: String) {
    val isFinished = remember { mutableStateOf(false) }
    if (isFinished.value) {
        TODO("FileBox")
    } else {
        TODO("ImageBox")
    }
}