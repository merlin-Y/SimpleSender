package cn.merlin.layout.mainWindow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.merlin.bean.Message
import cn.merlin.bean.model.DeviceModel
import cn.merlin.bean.model.MessageModel
import cn.merlin.database.SenderDB
import cn.merlin.layout.mainWindow.bubble.ArrowAlignment
import cn.merlin.layout.mainWindow.bubble.BubbleLayout
import cn.merlin.layout.mainWindow.bubble.BubbleShadow
import cn.merlin.layout.mainWindow.bubble.rememberBubbleState
import cn.merlin.layout.mainWindow.chat.ChatFlexBoxLayout
import cn.merlin.layout.mainWindow.chat.MessageTimeText
import cn.merlin.network.CurrentDeviceInformation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

val currentDevice: MutableState<DeviceModel> = mutableStateOf(DeviceModel(CurrentDeviceInformation.getInformation()))

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun message(
    width: Dp,
    height: Dp,
    senderDB: SenderDB
) {
    var isDragging by remember { mutableStateOf(false) }
    val contentList: SnapshotStateList<MessageModel> = remember{ mutableStateListOf() }
    val inputText = mutableStateOf("")

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
                            } else if (dragData is DragData.FilesList) {
                                dragData.readFiles().first().let {

                                    println(it)
                                }
                            }
                            isDragging = false
                        }
                    )
            ) {
                items(contentList) {
                    val messageStatus = remember { MessageStatus.entries[Random.nextInt(3)] }
                    if (it.messageSenderMacAddress.value == CurrentDeviceInformation.getInformation().deviceMacAddress) {
                        SentMessageRow(
                            drawArrow = true,
                            text = it.messageContent.value,
                            messageTime = "",
                            messageStatus =messageStatus
                        )
//                        Text(
//                            text = it.messageContent.value,
//                            modifier = Modifier.wrapContentWidth(Alignment.End),
//                            color = MaterialTheme.colorScheme.onPrimary
//                        )
                    } else {
                        SentMessageRow(
                            drawArrow = true,
                            text = it.messageContent.value,
                            messageTime = "",
                            messageStatus =messageStatus
                        )
                    }
                }
            }
            inputField(contentList,inputText)
        }
    }
}

@Composable
private fun SentMessageRow(
    drawArrow: Boolean = true,
    text: String,
    messageTime: String,
    messageStatus: MessageStatus
) {
    Column(
        modifier = Modifier
            .padding(start = 60.dp, end = 8.dp, top = if (drawArrow) 2.dp else 0.dp, bottom = 2.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.End
    ) {

        BubbleLayout(
            bubbleState = rememberBubbleState(
                alignment = ArrowAlignment.RightTop,
                cornerRadius = 8.dp,
                drawArrow = drawArrow,
            ),
            shadow = BubbleShadow(elevation = 1.dp),
            backgroundColor = MaterialTheme.colorScheme.primary
        ) {
            ChatFlexBoxLayout(
                modifier = Modifier
                    .padding(start = 2.dp, top = 2.dp, end = 4.dp, bottom = 2.dp),
                text = text,
                color = MaterialTheme.colorScheme.onPrimary,
                messageStat = {
                    MessageTimeText(
                        modifier = Modifier.wrapContentSize(),
                        messageTime = messageTime,
                        messageStatus = messageStatus
                    )
                }
            )
        }
    }
}

@Composable
private fun ReceivedMessageRow(
    drawArrow: Boolean = true,
    text: String,
    messageTime: String
) {
    Column(
        modifier = Modifier
            .padding(start = 8.dp, end = 60.dp, top = if (drawArrow) 2.dp else 0.dp, bottom = 2.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.Start
    ) {
        BubbleLayout(
            bubbleState = rememberBubbleState(
                alignment = ArrowAlignment.LeftTop,
                drawArrow = drawArrow,
                cornerRadius = 8.dp,
            ),
            shadow = BubbleShadow(elevation = 1.dp)
        ) {
            ChatFlexBoxLayout(
                modifier = Modifier
                    .padding(start = 2.dp, top = 2.dp, end = 4.dp, bottom = 2.dp),
                text = text,
                color = MaterialTheme.colorScheme.onPrimary,
                messageStat = {
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                        Text(
                            modifier = Modifier.padding(top = 1.dp, bottom = 1.dp, end = 4.dp),
                            text = messageTime,
                            fontSize = 12.sp
                        )
                    }
                }
            )
        }
    }
}

enum class MessageStatus {
    PENDING, RECEIVED, READ
}

data class ChatMessage(
    val id: Long,
    var message: String,
    var date: Long
)

@Composable
fun inputField(
    contentList: SnapshotStateList<MessageModel>,
    inputText: MutableState<String>
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
                               CoroutineScope(Dispatchers.IO).launch{
                                   if(inputText.value.isNotEmpty()){
                                       contentList.add(MessageModel(Message(messageContent = inputText.value)))
                                       inputText.value = ""
                                   }
                               }
                           }
                       ){
                           Icon(Icons.AutoMirrored.Filled.Send,"", tint = MaterialTheme.colorScheme.onSecondaryContainer)
                       }
        },
        colors = TextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
            cursorColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}