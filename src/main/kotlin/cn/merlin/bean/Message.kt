package cn.merlin.bean

import kotlinx.serialization.Serializable

@Serializable
class Message (
    var messageId: Int = 0,
    var messageContent: String = "test",
    val messageSenderIdentifier: String = "",
    val messageReceiverIdentifier: String = ""
)