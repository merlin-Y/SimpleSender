package cn.merlin.network

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.ServerSocket

val GetRequests = mutableStateOf(false)

class SenderServer {
    private val receiveRequestPort = 19999
    private val receivedPort = mutableSetOf<Int>(19999)

    fun startServer() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val serverSocket = ServerSocket(receiveRequestPort)
                println("Server is listening on port $receiveRequestPort")
                while (true) {
                    val socket = serverSocket.accept()
                    println("New connection established")
                    val objectOutputStream = ObjectOutputStream(socket.getOutputStream())
                    val objectInputStream = ObjectInputStream(socket.getInputStream())
                    val request = objectInputStream.readInt()
                    if (request == 1) {
                        objectOutputStream.writeObject(CurrentDeviceInformation.getInformation())
                        objectOutputStream.flush()
                    } else if (request == 2) {
                        objectOutputStream.writeInt(1)
                        objectOutputStream.flush()
                        val receiveFile = objectInputStream.readObject() as cn.merlin.bean.File
                        receiveFile.fileName =
                            "C:\\Users\\merlin\\Documents\\SimpleSender\\src\\main\\resources\\files\\receivedFile.zip"
                        val port = getFreePort()
                        objectOutputStream.writeInt(port)
                        objectOutputStream.flush()
                        receiveFile(receiveFile, port)
                    }
                    socket.close()
                }
            } catch (_: Exception) {

            }
        }
    }

    private fun receiveFile(receivedFile: cn.merlin.bean.File, port: Int) {
        val packetSize = 10240
        var packetNumber = 0
        try {
            val socket = DatagramSocket(port)
            val receiveData = ByteArray(receivedFile.totalPackets * 1024)
            val receivedPackets = mutableSetOf<Int>()
            while (receivedPackets.size < receivedFile.totalPackets) {
                val buffer = ByteArray(packetSize)
                val packet = DatagramPacket(buffer, buffer.size)
                socket.receive(packet)
                val offset = packetNumber * packetSize
                System.arraycopy(packet.data, 1, receiveData, offset, packet.length - 1)
                receivedPackets.add(packetNumber)
                packetNumber++
            }
            File(receivedFile.fileName).writeBytes(receiveData)
            socket.close()
            receivedPort.remove(port)
        } catch (e: Exception) {
//
        }
    }

    private fun getFreePort(): Int {
        var port = 20000
        while (receivedPort.contains(port)) {
            port += 1
        }
        receivedPort.add(port)
        return port
    }

}