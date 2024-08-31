package cn.merlin.database.model

import org.jetbrains.exposed.sql.Table

object MessageModel : Table() {
    val messageId = integer("id").autoIncrement()
    val messageContent = varchar("messageContent",60000)
    val messageSenderIdentifier = varchar("messageSenderIdentifier",50)
    val messageReceiverIdentifier = varchar("messageReceiverIdentifier",50)

    override val primaryKey = PrimaryKey(messageId,name = "messageId")
}