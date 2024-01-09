package cn.merlin.database.model

import cn.merlin.database.model.DeviceModel.autoIncrement
import org.jetbrains.exposed.sql.Table

object MessageModel : Table() {
    val messageId = integer("id").autoIncrement()
    val messageSenderId = integer("senderId")
    val messageReceiverId = integer("receiverId")
    val messageSenderIpAddress = varchar("senderIpAddress",50)
    val messageReceiverIpAddress = varchar("receiverIpAddress",50)

    override val primaryKey = PrimaryKey(messageId,name = "messageId")
}