package cn.merlin.bean.model

import androidx.compose.runtime.mutableStateOf
import cn.merlin.bean.File

class FileViewModel(file: File) {
    var fileId = mutableStateOf(file.fileId)
    var fileName = mutableStateOf(file.fileName)
    var filePAth = mutableStateOf(file.filePath)
}