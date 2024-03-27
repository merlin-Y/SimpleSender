package cn.merlin.test

import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.*
import kotlin.math.ceil

class SenderTest {
    fun sendFileToSelectedDevice(file: File) {
        val packetSize = 10240
        try {
            val socket = Socket()
            socket.connect(InetSocketAddress("127.0.0.1", 19999), 100)
            val datagramSocket = DatagramSocket()
            val data = file.readBytes()
            val totalPackets = ceil(data.size.toDouble() / packetSize).toInt()
            val objectInputStream = ObjectInputStream(socket.getInputStream())
            val objectOutputStream = ObjectOutputStream(socket.getOutputStream())
            objectOutputStream.writeInt(2)
            objectOutputStream.flush()
            val requestCode = objectInputStream.readInt()
            if (requestCode == 1) {
                objectOutputStream.writeInt(totalPackets)
                objectOutputStream.flush()
            }
            for (i in 0 until totalPackets) {
                val offset = i * packetSize
                val length = if (offset + packetSize < data.size) packetSize else data.size - offset
                val packetData = data.copyOfRange(offset, offset + length)
                val packet = DatagramPacket(
                    packetData,
                    length,
                    InetAddress.getByName("127.0.0.1"),
                    20000
                )
                datagramSocket.send(packet)
            }
            datagramSocket.close()
            socket.close()
        } catch (e: Exception) {
            println(e)
        }
    }
}