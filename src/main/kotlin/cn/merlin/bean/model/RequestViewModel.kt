package cn.merlin.bean.model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import cn.merlin.bean.Request

class RequestViewModel(request: Request) {
    val requestSenderIpAddress = mutableStateOf(request.requestSenderIpAddress)
    val requestSenderPort = mutableStateOf(request.requestSenderPort)
}