package cn.merlin.network

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import cn.merlin.bean.Device
import cn.merlin.bean.model.DeviceModel
import kotlinx.coroutines.*
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.*
import kotlin.math.ceil

val DetectedDevices: SnapshotStateList<DeviceModel> = mutableStateListOf()

class Sender {

    private var currentDevice = Device()

    fun detectDevices() {
        CoroutineScope(Dispatchers.IO).launch {
            currentDevice = CurrentDeviceInformation.getInformation()
            val ipAddressIndex =
                currentDevice.deviceIpAddress.substring(0, currentDevice.deviceIpAddress.lastIndexOf("."))
            println("Scanning from $ipAddressIndex.1~$ipAddressIndex.255")
            for (i in 1..255) {
                CoroutineScope(Dispatchers.IO).launch {
                    val ipAddress = "$ipAddressIndex.$i"
                    if (ipAddress == currentDevice.deviceIpAddress) cancel()
                    try {
                        val socket = Socket()
                        socket.connect(InetSocketAddress(ipAddress, 19999), 100)
                        val objectOutputStream = ObjectOutputStream(socket.getOutputStream())
                        val objectInputStream = ObjectInputStream(socket.getInputStream())
                        objectOutputStream.writeInt(1)
                        objectOutputStream.flush()
                        val device = objectInputStream.readObject() as Device
                        DetectedDevices.add(DeviceModel(device))
                        socket.close()
                    } catch (e: Exception) {
//                            print(e)
                    }
                }
            }
            println("${currentDevice.deviceName} ${currentDevice.deviceIpAddress} ${currentDevice.deviceMacAddress}")
            println("Scanning accomplished")
        }
    }

    fun sendFileToSelectedDevice(device: DeviceModel, file: File) {
        CoroutineScope(Dispatchers.IO).launch {
            val packetSize = 10240
            try {
                val socket = Socket()
                socket.connect(InetSocketAddress(device.deviceIpAddress.value, 19999), 2000)
                val datagramSocket = DatagramSocket()
                val data = file.readBytes()
                val totalPackets = ceil(data.size.toDouble() / packetSize).toInt()
                val objectOutputStream = ObjectOutputStream(socket.getOutputStream())
                val objectInputStream = ObjectInputStream(socket.getInputStream())
                objectOutputStream.writeInt(2)
                objectOutputStream.flush()
                val requestCode = objectInputStream.readInt()
                if (requestCode == 1) {
                    objectOutputStream.writeObject(cn.merlin.bean.File(file.name, data.size,totalPackets))
                    objectOutputStream.flush()
                } else this.cancel()
                val port = objectInputStream.readInt()
                for (i in 0 until totalPackets) {
                    val offset = i * packetSize
                    val length = if (offset + packetSize < data.size) packetSize else data.size - offset
                    val packetData = ByteArray(packetSize + 4)
                    packetData[0] = (i shr 8).toByte()
                    packetData[1] = i.toByte()
                    packetData[2] = (length shr 8).toByte()
                    packetData[3] = length.toByte()
                    System.arraycopy(data,offset,packetData,4,length)
//                        data.copyOfRange(offset, offset + length )
                    val packet = DatagramPacket(
                        packetData,
                        length + 4,
                        InetAddress.getByName(device.deviceIpAddress.value),
                        port
                    )
                    datagramSocket.send(packet)
                }
                socket.close()
                datagramSocket.close()
            } catch (e: Exception) {
                println(e)
            }
        }
    }
}