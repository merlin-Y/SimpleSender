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
                    val objectInputStream = ObjectInputStream(socket.getInputStream())
                    val objectOutputStream = ObjectOutputStream(socket.getOutputStream())
                    val request = objectInputStream.readInt()
                    if (request == 1) {
                        objectOutputStream.writeObject(CurrentDeviceInformation.getInformation())
                    } else if (request == 2) {
                        objectOutputStream.writeInt(1)
                        val totalPackets = objectInputStream.readObject() as cn.merlin.bean.File
                        launch(Dispatchers.IO) {
                            val port = getFreePort()
                            receiveFile(File(totalPackets.fileName), port, totalPackets.totalPackets)
                        }
                    }

                }
            } catch (_: Exception) {

            }
        }
    }

    private fun receiveFile(file: File, port: Int, totalPackets: Int) {
        val packetSize = 1024
        try {
            val socket = DatagramSocket(port)
            val receiveData = ByteArray(totalPackets * 1024)
            val receivedPackets = mutableSetOf<Int>()
            while (receivedPackets.size < totalPackets) {
                val buffer = ByteArray(1024)
                val packet = DatagramPacket(buffer, buffer.size)
                socket.receive(packet)

                val packetNumber = packet.data[0].toInt()
                receivedPackets.add(packetNumber)
                val offset = packetNumber * 1024
                System.arraycopy(packet.data, 1, receiveData, offset, packet.length - 1)
            }
            file.writeBytes(receiveData)
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