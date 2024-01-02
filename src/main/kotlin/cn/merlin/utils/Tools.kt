package cn.merlin.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import java.util.prefs.Preferences

val Settings : SnapshotStateMap<String, MutableState<Boolean>> =  mutableStateMapOf()
fun checkIfContain(data: Preferences, key: String) : Boolean{
    return data.get(key,null) == null
}