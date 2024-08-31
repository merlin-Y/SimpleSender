package cn.merlin.network

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import cn.merlin.bean.Device
import cn.merlin.bean.model.DeviceViewModel
import cn.merlin.bean.model.FileViewModel
import cn.merlin.bean.model.MessageVIewModel
import cn.merlin.tools.DeviceConfiguration.getDetectedList
import cn.merlin.tools.RequestCode
import cn.merlin.tools.freePort
import cn.merlin.tools.getFreePort
import cn.merlin.tools.isDeviceFlushed
import kotlinx.coroutines.*
import java.net.*
import kotlin.reflect.jvm.internal.impl.load.java.JavaClassFinder.Request


class Sender {
    private val sendCommandCodeRequestPort = 19999
    private val keepCodeTimeout = 3000L
    private val sendRequestCodeTimeout = 20000L
    private val sendJobs: MutableList<Job> = mutableListOf()

    suspend fun sendCodeRequest(currentDevice: Device) {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                val commandCode = "detectCode"
                val broadcastAddress = currentDevice.deviceIpAddress.substring(
                    0,
                    currentDevice.deviceIpAddress.lastIndexOf('.') + 1
                ) + "255"
                val sendMessage = "$commandCode;${currentDevice.deviceIpAddress}"
                sendCommandCode(sendMessage, broadcastAddress)
                delay(1500)
            }
        }
    }

    fun deviceConnectionKeeper(currentDevice: Device) {
        CoroutineScope(Dispatchers.Default).launch {
            val commandCode = "keepCode"
            while (true) {
                if (!getDetectedList().isEmpty()) {
                    getDetectedList().forEach {
                        CoroutineScope(Dispatchers.IO).launch {
                            if (it.isConnected.value) {
                                val freePort = getFreePort()
                                val sendMessage = "$commandCode;${currentDevice.deviceIpAddress}:${freePort}"

                                sendCommandCode(sendMessage, it.deviceIpAddress.value)

                                val receiveSocket = DatagramSocket(freePort)
                                val receiveBuffer = ByteArray(64)
                                val receivePacket = DatagramPacket(receiveBuffer, receiveBuffer.size)

                                try {
                                    receiveSocket.soTimeout = keepCodeTimeout.toInt()
                                    receiveSocket.receive(receivePacket)
                                    val length =
                                        (receivePacket.data[0].toInt() shl 8) or (receivePacket.data[1].toInt() and 0xFF)
                                    val buffer = ByteArray(length)
                                    System.arraycopy(receivePacket.data, 2, buffer, 0, length)
                                    val cnb = buffer.toString(Charsets.UTF_8)
                                    if (cnb.contains("keepCode")) {
                                        receiveSocket.close()
                                        freePort(freePort)
                                    }
                                    receiveSocket.close()
                                    freePort(freePort)
                                } catch (_: Exception) {
                                    it.isConnected.value = false
                                    isDeviceFlushed.value = !isDeviceFlushed.value
                                    receiveSocket.close()
                                    freePort(freePort)
                                }
                            }
                        }
                    }
                }
                delay(1000)
            }
        }
    }

    suspend fun sendRequestCodeToSelectedDevice(
        currentDevice: Device,
        device: DeviceViewModel,
    ): RequestCode = withContext(Dispatchers.IO) {
        val requestCode = RequestCode()
        val commandCode = "sendMessageRequest"
        val freePort = getFreePort()
        val sendMessage = "$commandCode;${currentDevice.deviceIpAddress}:${freePort}"

        sendCommandCode(sendMessage, device.deviceIpAddress.value)

        val receiveSocket = DatagramSocket(freePort)
        val receiveBuffer = ByteArray(64)
        val receivePacket = DatagramPacket(receiveBuffer, receiveBuffer.size)

        try {
            receiveSocket.soTimeout = sendRequestCodeTimeout.toInt()
            receiveSocket.receive(receivePacket)
            receiveSocket.close()
        } catch (_: Exception) {
            freePort(freePort)
            requestCode.requestCode = 3
            receiveSocket.close()
            return@withContext requestCode
        }
        val length =
            (receivePacket.data[0].toInt() shl 8) or (receivePacket.data[1].toInt() and 0xFF)
        val buffer = ByteArray(length)
        System.arraycopy(receivePacket.data, 2, buffer, 0, length)
        val cnb = buffer.toString(Charsets.UTF_8)
        if (cnb.contains("sendMessage")) {
            val request = cnb.substring(cnb.lastIndexOf(';') + 1, cnb.lastIndexOf(':'))
            requestCode.requestPort = cnb.substring(cnb.lastIndexOf(':') + 1, cnb.length).toInt()
            when (request) {
                //reject
                "rejectRequest" -> {
                    requestCode.requestCode = 0
                    freePort(freePort)
                    return@withContext requestCode
                }
                //access
                "accessRequest" -> {
                    requestCode.requestCode = 1
                    receiveSocket.close()
                    return@withContext requestCode
                }

                else -> {
                    requestCode.requestCode = 2
                    freePort(freePort)
                    return@withContext requestCode
                }
            }
        }
        requestCode.requestCode = 3
        return@withContext requestCode
    }

    fun sendMessageToSelectedDevice(
        currentDevice: Device,
        device: DeviceViewModel,
        messageVIewModel: MessageVIewModel,
        fileList: MutableList<FileViewModel>
    ) {
        val commandCode = "sendMessage"
        val freePort = getFreePort()
        val sendMessage = "$commandCode;${currentDevice.deviceIpAddress}:${freePort}"
        sendCommandCode(sendMessage, device.deviceIpAddress.value)
//                socket.connect(InetSocketAddress(device.deviceIpAddress.value, sendMessageCodePort), 2000)
//                val objectOutputStream = ObjectOutputStream(socket.getOutputStream())
//                val objectInputStream = ObjectInputStream(socket.getInputStream())
//                objectOutputStream.writeInt(2)
//                objectOutputStream.flush()
//                val requestCode = objectInputStream.readInt()
//                if (requestCode == 1) {
//                    objectOutputStream.writeObject(
//                        cn.merlin.database.model.FileModel(
//                            file.name,
//                            data.size,
//                            totalPackets
//                        )
//                    )
//                    objectOutputStream.flush()
//                } else this.cancel()
//                val port = objectInputStream.readInt()
//                socket.close()
//                for (i in 0 until totalPackets) {
//                    val offset = i * packetSize
//                    val length = if (offset + packetSize < data.size) packetSize else data.size - offset
//                    val packetData = ByteArray(packetSize + 4)
//                    packetData[0] = (i shr 8).toByte()
//                    packetData[1] = i.toByte()
//                    packetData[2] = (length shr 8).toByte()
//                    packetData[3] = length.toByte()
//                    System.arraycopy(data, offset, packetData, 4, length)
////                        data.copyOfRange(offset, offset + length )
//                    val packet = DatagramPacket(
//                        packetData,
//                        length + 4,
//                        InetAddress.getByName(device.deviceIpAddress.value),
//                        port
//                    )
//                    datagramSocket.send(packet)
//                }
//
//                datagramSocket.close()
//        }
    }

    private fun sendCommandCode(cnb: String, ipAddress: String) {
        val sendMessageArray = ByteArray(cnb.length + 2)
        sendMessageArray[0] = (cnb.length shr 8).toByte()
        sendMessageArray[1] = cnb.length.toByte()
        System.arraycopy(
            cnb.toByteArray(Charsets.UTF_8),
            0,
            sendMessageArray,
            2,
            cnb.length
        )
        val packet =
            DatagramPacket(
                sendMessageArray,
                cnb.length + 2,
                InetAddress.getByName(ipAddress),
                sendCommandCodeRequestPort
            )
        val datagramSocket = DatagramSocket()
        try {
            datagramSocket.send(packet)
        } catch (_: Exception) {
            datagramSocket.close()
        }
        datagramSocket.close()
    }
}
