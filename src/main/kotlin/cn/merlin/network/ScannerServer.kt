package cn.merlin.network

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.net.ServerSocket

class ScannerServer {
    val port = 19999

    suspend fun startServer(){
        coroutineScope {
            launch {
                try {
                    val serverSocket = ServerSocket(port)
                    println("Server is listening on port $port")
                    while (true){
                        val socket = serverSocket.accept()
                        println("New connection established")
                        /* TODO 处理连接响应返回设备信息 */
                    }
                }catch (_: Exception){

                }
            }
        }
    }
}