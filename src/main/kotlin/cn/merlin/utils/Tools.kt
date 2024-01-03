package cn.merlin.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import cn.merlin.layout.theme.isReversedTheme
import java.util.prefs.Preferences

val Settings : SnapshotStateMap<String, MutableState<Boolean>> =  mutableStateMapOf()
fun checkIfContain(data: Preferences, key: String) : Boolean{
    return data.get(key,null) == null
}

fun reverseThemeColorse(data: Preferences){
    Settings.set("useDarkTheme" , mutableStateOf(!Settings.getValue("useDarkTheme").value))
    data.putBoolean("useDarkTheme", Settings.getValue("useDarkTheme").value)
    isReversedTheme.value = !isReversedTheme.value
}