package cn.merlin.bean.model

import androidx.compose.runtime.mutableStateOf
import moe.tlaster.precompose.viewmodel.ViewModel

class MenuItemModel: ViewModel() {
    val selectedMenuTag = mutableStateOf("DetectDevice")

}