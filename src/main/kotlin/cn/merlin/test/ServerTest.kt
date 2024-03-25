package cn.merlin.test

import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.ServerSocket

class ServerTest {
    private val receiveRequestPort = 19999
    private val receivedPort = mutableSetOf<Int>(19999)

    fun startServer() {
        try {
            val serverSocket = ServerSocket(receiveRequestPort)
            println("Server is listening on port $receiveRequestPort")
            while (true) {
                val socket = serverSocket.accept()
                println("New connection established")
                val objectOutputStream = ObjectOutputStream(socket.getOutputStream())
                val objectInputStream = ObjectInputStream(socket.getInputStream())
                val request = objectInputStream.readInt()
                println(request)
                if (request == 2) {
                    objectOutputStream.writeInt(1)
                    objectOutputStream.flush()
                    val totalPackets = objectInputStream.readInt()
                    println(totalPackets)
                    val port = getFreePort()
                    receiveFile(
                        File("C:\\Users\\merlin\\Documents\\SimpleSender\\src\\main\\resources\\file.zip"),
                        port,
                        totalPackets
                    )
                }
                socket.close()
            }
        } catch (_: Exception) {

        }
    }

    private fun receiveFile(file: File, port: Int, totalPackets: Int) {
        val packetSize = 10240
        try {
            val socket = DatagramSocket(port)
            val receiveData = ByteArray(totalPackets * packetSize)
            val receivedPackets = mutableSetOf<Int>()
            while (receivedPackets.size < totalPackets) {
                val buffer = ByteArray(packetSize)
                val packet = DatagramPacket(buffer, buffer.size)
                socket.receive(packet)

                val packetNumber = packet.data[0].toInt()
                receivedPackets.add(packetNumber)
                val offset = packetNumber * packetSize
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