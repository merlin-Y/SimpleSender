import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okio.Utf8
import java.io.File
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

fun main() {
    val ipaddress = "127.0.0.1"
    val filePath = "C:/kotlin-compiler-2.0.10.zip"
    val port = 20001
    val packetSize = 4096
    var packetNumber = 1
    val list: MutableList<Socket> = mutableListOf()
    val socket = Socket()
    try {
        socket.connect(InetSocketAddress(ipaddress, port), 2000)
        list.add(socket)

        sendMessageByTCP(socket, "sendMessage")
        val message = receiveMessageByTCP(socket)
        if (message.contains("receive")) {
            val port = message.substring(message.indexOf(";") + 1, message.length)
            val file = File(filePath)
            CoroutineScope(Dispatchers.IO).launch {
//                sendMessageByUDP(ipaddress,port,)
            }

        }
        socket.close()
    } catch (_: Exception) {
        socket.close()
    }
}

fun sendMessageByTCP(socket: Socket, message: String) {
    val outputStream = socket.getOutputStream()
    outputStream.write(message.toByteArray())
    outputStream.flush()
    outputStream.close()
}

fun receiveMessageByTCP(socket: Socket): String {
    val inputStream = socket.getInputStream()
    var message = ""
    var length = 0
    val inMessage = ByteArray(128)
    length = inputStream.read(inMessage)
    message = String(inMessage, 0, length)
    return message
}

fun sendMessageByUDP(ipaddress: String, port: Int, message: ByteArray, packetNumber: Int) {
    val sendMessageArray = ByteArray(message.size + 8)
    packetNumber.toByteArray(sendMessageArray, 0)
    message.size.toByteArray(sendMessageArray, 4)
    message.copyInto(sendMessageArray, 8, 0)

    //datagramSocket
}

fun Int.toByteArray(byteArray: ByteArray, startIndex: Int) {
    byteArray[startIndex] = (this and 0xff).toByte()
    byteArray[startIndex + 1] = ((this shr 8) and 0xff).toByte()
    byteArray[startIndex + 2] = ((this shr 16) and 0xff).toByte()
    byteArray[startIndex + 3] = ((this shr 24) and 0xff).toByte()
}

