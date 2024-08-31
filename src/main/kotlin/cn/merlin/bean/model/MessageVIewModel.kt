package cn.merlin.bean.model

import androidx.compose.runtime.mutableStateOf
import cn.merlin.bean.Message

class MessageVIewModel(message: Message) {
    val messageId = mutableStateOf(message.messageId)
    val messageContent = mutableStateOf(message.messageContent)
    val messageSenderIdentifier = mutableStateOf(message.messageSenderIdentifier)
    val messageReceiverIdentifier = mutableStateOf(message.messageReceiverIdentifier)
    val messageReceived = mutableStateOf(false)
}