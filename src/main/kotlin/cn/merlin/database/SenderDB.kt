package cn.merlin.database
import cn.merlin.bean.Device
import cn.merlin.bean.Message
import cn.merlin.database.model.DeviceModel
import cn.merlin.database.model.MessageModel
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import javax.naming.Context

class SenderDB {
    val url = "src/main/resources/database/sender.db"
//    val url = "jdbc:sqlite:resources/database/sender.db"
    val database = Database.connect("jdbc:sqlite:${url}", driver = "org.sqlite.JDBC")

    fun createTables(){
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(DeviceModel)
            SchemaUtils.create(MessageModel)
        }
    }

    fun insertDevice(device: Device): Int{
        var deviceId: Int = -1
        transaction {
            deviceId = DeviceModel.insert {
                it[deviceName] = device.deviceName
                it[deviceIpAddress] = device.deviceIpAddress
                it[deviceMacAddress] = device.deviceMacAddress
                it[deviceNickName] = device.deviceName
            } get DeviceModel.deviceId
        }
        return deviceId
    }

    fun insertMessage(message: Message): Int{
        var messageId: Int = -1
        transaction {
            messageId = MessageModel.insert{
                it[messageSenderId] = message.messageSenderId
                it[messageReceiverId] = message.messageReceiverId
                it[messageSenderIpAddress] = message.messageSenderIpAddress
                it[messageReceiverIpAddress] = message.messageReceiverIpAddress
            } get MessageModel.messageId
        }
        return messageId
    }

    fun selectAllDevice(): Query?{
        var query: Query? = null
        transaction {
            query = DeviceModel.selectAll()
        }
        return query
    }

    fun selectAllMessage(): Query?{
        var query: Query? = null
        transaction {
            query = MessageModel.selectAll()
        }
        return query
    }

    fun selectDeviceById(deviceId: Int): Query?{
        var query: Query? = null
        transaction {
            query = DeviceModel.select { DeviceModel.deviceId eq deviceId }
        }
        return query
    }

    fun selectMessageById(messageId: Int): Query?{
        var query: Query? = null
        transaction {
            query = MessageModel.select { MessageModel.messageId eq messageId }
        }
        return query
    }

    fun deleteDeviceById(deviceId: Int): Int{
        var id: Int = -1
        transaction {
            id = DeviceModel.deleteWhere { DeviceModel.deviceId eq deviceId }
        }
        return id
    }

    fun deleteMessageById(messageId: Int): Int{
        var id: Int = -1
        transaction {
            id = MessageModel.deleteWhere { MessageModel.messageId eq messageId }
        }
        return id
    }
}

