package cn.merlin.tools

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import cn.merlin.bean.model.DeviceViewModel

object DeviceConfiguration {
    private var detectedDeviceList: SnapshotStateList<DeviceViewModel> = mutableStateListOf()
    private var savedDeviceList: SnapshotStateList<DeviceViewModel> = mutableStateListOf()
    val savedDeviceIdentifierSet: MutableSet<String> = mutableSetOf()
    val detectedDeviceIdentifierSet: MutableSet<String> = mutableSetOf()

    fun getSavedList(): SnapshotStateList<DeviceViewModel> {
        return savedDeviceList
    }

    fun getDetectedList(): SnapshotStateList<DeviceViewModel>{
        return detectedDeviceList
    }
}