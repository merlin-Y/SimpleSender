package cn.merlin.database.model

import org.jetbrains.exposed.sql.Table

object MessageModel : Table() {
    val messageId = integer("id").autoIncrement()
    val messageType = integer("messageType")
    val messageContent = varchar("messageContent",50)
    val messageSenderIpAddress = varchar("senderIpAddress",50)
    val messageReceiverIpAddress = varchar("receiverIpAddress",50)
    val messageSenderIdentifier = varchar("messageSenderIdentifier",50)
    val messageReceiverIdentifier = varchar("messageReceiverIdentifier",50)

    override val primaryKey = PrimaryKey(messageId,name = "messageId")
}