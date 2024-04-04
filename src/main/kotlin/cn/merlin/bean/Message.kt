package cn.merlin.bean


class Message (
    val messageId: Int = 0,
    val messageType: Int = 0,
    val messageContent: String = "test",
    val messageSenderIpAddress: String = "",
    val messageReceiverIpAddress: String = "",
    val messageSenderMacAddress: String = "",
    val messageReceiverMacAddress: String = ""
)