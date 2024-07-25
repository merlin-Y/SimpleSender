package cn.merlin.ui.pages

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.merlin.bean.Device
import cn.merlin.bean.Message
import cn.merlin.bean.model.DeviceViewModel
import cn.merlin.bean.model.MessageVIewModel
import cn.merlin.database.SenderDB
import cn.merlin.ui.theme.TriangleLeftShape
import cn.merlin.ui.theme.TriangleRightShape
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

val currentDevice = mutableStateOf(DeviceViewModel(Device()))

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun message(
    width: Dp,
    height: Dp,
    senderDB: SenderDB,
    messageList: SnapshotStateList<MessageVIewModel>
) {
    var isDragging by remember { mutableStateOf(false) }
    val inputText = remember{ mutableStateOf("") }
    val acceptedFile = remember { mutableStateOf<File?>(null) }

    messageList.clear()

    val list = senderDB.selectAllMessage()
    list.forEach {
        messageList.add(MessageVIewModel(it))
    }

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
                    .onExternalDrag(
                        onDragStart = {
                            isDragging = true
                        },
                        onDragExit = {
                            isDragging = false
                        },
                        onDrag = {

                        },
                        onDrop = { state ->
                            val dragData = state.dragData
                            if (dragData is DragData.Image) {
                                println(dragData.readImage().toString())
                            } else if (dragData is DragData.FilesList && isDragging) {
                                dragData.readFiles().first().let {
                                    val message = Message(
                                        messageType = 1,
                                        messageSenderIdentifier = currentDevice.value.deviceIdentifier.value,
                                        messageReceiverIdentifier = currentDevice.value.deviceIdentifier.value,
                                        messageContent = it.substring(6),
                                        messageSenderIpAddress = cn.merlin.tools.currentDevice.value.deviceIpAddress.value,
                                        messageReceiverIpAddress = currentDevice.value.deviceIpAddress.value)
                                        messageList.add(MessageVIewModel(message))
                                        senderDB.insertMessage(MessageVIewModel(message))
                                    println(it)
                                }
                            }
                            isDragging = false
                        }
                    ),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(messageList) {
                    if(it.messageType.value == 0){
                        SentMessageRow(it)
                        ReceiveMessageRow(it)
                    }
                    else{

                    }
                }
            }
            inputField(messageList, inputText, senderDB)
        }
    }
}

@Composable
fun inputField(
    messageList: SnapshotStateList<MessageVIewModel>,
    inputText: MutableState<String>,
    senderDB: SenderDB
) {
    Spacer(modifier = Modifier.background(MaterialTheme.colorScheme.tertiary).height(2.dp).fillMaxWidth())
    TextField(
        modifier = Modifier
            .height(160.dp)
            .fillMaxWidth(),
        value = inputText.value,
        onValueChange = {
            inputText.value = it
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        if (inputText.value.isNotEmpty()) {
                            val message = Message(
                                messageSenderIdentifier = currentDevice.value.deviceIdentifier.value,
                                messageReceiverIdentifier = currentDevice.value.deviceIdentifier.value,
                                messageContent = inputText.value,
                                messageSenderIpAddress = currentDevice.value.deviceIpAddress.value,
                                messageReceiverIpAddress = currentDevice.value.deviceIpAddress.value
                            )
                            messageList.add(MessageVIewModel(message))
                            senderDB.insertMessage(MessageVIewModel(message))
                            inputText.value = ""
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
            selectionColors = TextSelectionColors(MaterialTheme.colorScheme.onSecondaryContainer,MaterialTheme.colorScheme.onSecondaryContainer)
        )
    )
}

@Composable
fun SentMessageRow(message: MessageVIewModel) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopEnd
    ){
        Row(
            Modifier.height(IntrinsicSize.Max),
        ) {
            Column(
                modifier = Modifier
                    .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(4.dp, 4.dp, 0.dp, 4.dp))
                    .padding(start = 5.dp, end = 5.dp)
            ) {
                Box(modifier = Modifier.widthIn(0.dp,400.dp)){
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
    ){
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
                    shape = RoundedCornerShape(4.dp, 4.dp, 0.dp, 4.dp))
                    .padding(start = 5.dp, end = 5.dp)
            ) {
                Box(modifier = Modifier.widthIn(0.dp,400.dp)){
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