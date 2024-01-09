package cn.merlin.bean


class Message (
    val messageSenderId: Int,
    val messageReceiverId: Int,
    val messageSenderIpAddress: String,
    val messageReceiverIpAddress: String
)
//    val messageId = MessageModel.integer("id").autoIncrement()
//    val messageSenderId = MessageModel.integer("senderId")
//    val messageReceiverId = MessageModel.integer("receiverId")
//    val messageSenderIpAddress = MessageModel.varchar("senderIpAddress", 50)
//    val messageReceiverIpAddress = MessageModel.varchar("receiverIpAddress", 50)