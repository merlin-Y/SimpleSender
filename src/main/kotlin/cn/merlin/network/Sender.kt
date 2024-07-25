package cn.merlin.network

import cn.merlin.bean.Device
import cn.merlin.bean.File
import cn.merlin.bean.model.DeviceViewModel
import kotlinx.coroutines.*
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.*
import kotlin.math.ceil

val detectedDeviceIdentifierList: MutableList<String> = mutableListOf()

class Sender {
    private val sendRequestPort = 19999
    private var currentDevice = Device()

    suspend fun sendCodeRequest(currentDevice: Device) {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                val commandCode = "detectCode"
                val broadcastAddress = InetAddress.getByName(
                    currentDevice.deviceIpAddress.substring(
                        0,
                        currentDevice.deviceIpAddress.lastIndexOf('.') + 1
                    ) + "255"
                )
                val sendMessage = "$commandCode;${currentDevice.deviceIpAddress}"
                val sendMessageArray = ByteArray(sendMessage.length + 2)
                sendMessageArray[0] = (sendMessage.length shr 8).toByte()
                sendMessageArray[1] = sendMessage.length.toByte()
                System.arraycopy(sendMessage.toByteArray(Charsets.UTF_8), 0, sendMessageArray, 2, sendMessage.length)
                val socket = DatagramSocket()
                try {
                    socket.broadcast = true
                    val packet =
                        DatagramPacket(sendMessageArray, sendMessage.length + 2, broadcastAddress, sendRequestPort)
                    socket.send(packet)
                } catch (_: Exception) {
                    socket.close()
                }
                socket.close()
                delay(2000)
            }
        }
    }

    suspend fun sendMessageRequest(message: String,fileList: MutableList<File>) {

    }

    fun cancelScanningDevice(){

    }
    fun restartScanningDevice(){

    }

//    fun sendFileToSelectedDevice(device: DeviceViewModel, file: File) {
//        CoroutineScope(Dispatchers.IO).launch {
//            val packetSize = 10240
//            try {
//                val socket = Socket()
//                socket.connect(InetSocketAddress(device.deviceIpAddress.value, 19999), 2000)
//                val datagramSocket = DatagramSocket()
//                val data = file.readBytes()
//                val totalPackets = ceil(data.size.toDouble() / packetSize).toInt()
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
//            } catch (e: Exception) {
//                println(e)
//            }
//        }
//    }
}