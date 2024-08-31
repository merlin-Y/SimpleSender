import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.net.DatagramSocket
import java.net.InetSocketAddress
import java.net.Socket

fun main() {
    val ipaddress = "127.0.0.1"
    val filePath = "C:\\kotlin-compiler-2.0.10.zip"
    val port = 20001
    val packetSize = 10240
    var packetNumber = 1
    val list: MutableList<Socket> = mutableListOf()
    val socket = Socket()
    try{
        socket.connect(InetSocketAddress(ipaddress,port),2000)
        list.add(socket)
        socket.close()
    }catch (_:Exception){
        socket.close()
    }

    //send files
    val datagramSocket = DatagramSocket()
    val file = File(filePath)


        CoroutineScope(Dispatchers.IO).launch {
            try {
                val outputStream = socket.getOutputStream()
                val inputStream = socket.getInputStream()
                val randoms = (200..300).random()
                if(packetNumber == randoms){
                    outputStream.write("stopFileReceive".toByteArray())
                    outputStream.flush()
                    var message = ""
                    val inMessage = ByteArray(128)
                    val length = inputStream.read(inMessage)
                    message = String(inMessage,0,length)
                    if(message == ""){

                    }
                }
            }catch (_:Exception){}
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val fileInputStream = FileInputStream(file)
            }catch (_:Exception){}

        }


}