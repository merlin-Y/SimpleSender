package cn.merlin.bean.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class InputModel(type: Int,content: String){
    var content = mutableStateOf(content)
    val contentType = mutableStateOf(type)
}