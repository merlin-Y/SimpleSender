package cn.merlin.bean.model

import androidx.compose.runtime.mutableStateOf
import cn.merlin.bean.Message

class MessageModel(message: Message){
    var messageId = mutableStateOf(message.messageId)
    var messageType = mutableStateOf(message.messageType)
    var messageContent = mutableStateOf(message.messageContent)
    var messageSenderIpAddress = mutableStateOf(message.messageSenderIpAddress)
    var messageReceiverIpAddress = mutableStateOf(message.messageReceiverIpAddress)
    var messageSenderMacAddress = mutableStateOf(message.messageSenderMacAddress)
    var messageReceiverMacAddress = mutableStateOf(message.messageReceiverMacAddress)
}