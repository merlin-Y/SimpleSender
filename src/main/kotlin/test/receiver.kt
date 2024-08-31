import java.net.ServerSocket
import java.net.Socket

fun main() {
    val port = 20001
    val socket = ServerSocket(port)
    try {
        socket.accept()

    }catch (_:Exception){}
}