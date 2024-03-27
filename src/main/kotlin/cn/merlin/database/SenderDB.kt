package cn.merlin.database

import cn.merlin.bean.Device
import cn.merlin.bean.Message
import cn.merlin.database.model.DeviceModel
import cn.merlin.database.model.DeviceModel.deviceIpAddress
import cn.merlin.database.model.DeviceModel.deviceMacAddress
import cn.merlin.database.model.DeviceModel.deviceName
import cn.merlin.database.model.DeviceModel.deviceNickName
import cn.merlin.database.model.MessageModel
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class SenderDB {
    val url = "src/main/resources/database/sender.db"
    val database = Database.connect("jdbc:sqlite:${url}", driver = "org.sqlite.JDBC")

    fun createTables() {
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(DeviceModel)
            SchemaUtils.create(MessageModel)
        }
    }

    fun insertDevice(device: Device): Int {
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

    fun selectAllDevice(): MutableList<Device> {
        val devices: MutableList<Device> = mutableListOf()
        transaction {
            val query = DeviceModel.selectAll()
            query?.forEach {
                val device = Device()
                device.deviceIpAddress = it[deviceIpAddress]
                device.deviceName = it[deviceName]
                device.deviceMacAddress = it[deviceMacAddress]
                device.deviceNickName = it[deviceNickName]
                devices.add(device)
            }
        }
        return devices
    }

    fun selectDeviceById(deviceId: Int): Device {
        val device = Device()
        transaction {
            val query = DeviceModel.select { DeviceModel.deviceId eq deviceId }
            query.forEach {
                device.deviceIpAddress = it[deviceIpAddress]
                device.deviceName = it[deviceName]
                device.deviceMacAddress = it[deviceMacAddress]
                device.deviceNickName = it[deviceNickName]
            }
        }
        return device
    }

    fun deleteDeviceById(deviceId: Int): Int {
        var result: Int = -1
        transaction {
            result = DeviceModel.deleteWhere { DeviceModel.deviceId eq deviceId }
        }
        return result
    }

    fun insertMessage(message: Message): Int {
        var messageId: Int = -1
        transaction {
            messageId = MessageModel.insert {
                it[messageSenderId] = message.messageSenderId
                it[messageReceiverId] = message.messageReceiverId
                it[messageSenderIpAddress] = message.messageSenderIpAddress
                it[messageReceiverIpAddress] = message.messageReceiverIpAddress
            } get MessageModel.messageId
        }
        return messageId
    }

    fun selectAllMessage(): MutableList<Message>{
        val messages: MutableList<Message> = mutableListOf()
        transaction {
            val query = MessageModel.selectAll()
            query.forEach {
                val message = Message(
                    it[MessageModel.messageSenderId],
                    it[MessageModel.messageReceiverId],
                    it[MessageModel.messageSenderIpAddress],
                    it[MessageModel.messageReceiverIpAddress]
                )
                messages.add(message)
            }
        }
        return messages
    }

    fun selectMessageById(messageId: Int): Query? {
        var query: Query? = null
        transaction {
            query = MessageModel.select { MessageModel.messageId eq messageId }
        }
        return query
    }

    fun deleteMessageById(messageId: Int): Int {
        var result: Int = -1
        transaction {
            result = MessageModel.deleteWhere { MessageModel.messageId eq messageId }
        }
        return result
    }
}

