package cn.merlin.layout.mainWindow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.merlin.utils.changeTheme
import cn.merlin.utils.updateSettings
import moe.tlaster.precompose.navigation.Navigator

@Composable
fun settings(
    width: Dp,
    height: Dp,
    navigator: Navigator
) {
    val expend = mutableStateOf(false)
    Surface(
        modifier = Modifier.size(width, height),
        color = MaterialTheme.colorScheme.secondary
    ) {
        Column {
            Text(
                modifier = Modifier.size(width, 40.dp).padding(start = 20.dp),
                text = "系统设置",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontSize = 16.sp,
                maxLines = 1,
                lineHeight = 32.sp
            )
            Spacer(modifier = Modifier.background(MaterialTheme.colorScheme.tertiary).height(2.dp).width(width))
            LazyColumn(
                modifier = Modifier.padding(top = 10.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.padding(end = 60.dp)
                    ) {
                        Text(
                            text = "主题",
                            modifier = Modifier.padding(start = 40.dp).weight(5f)
                        )
                        Surface(
                            modifier = Modifier.weight(1f)
                        ) {
                            Button(
                                modifier = Modifier.width(40.dp),
                                shape = MaterialTheme.shapes.extraSmall,
                                onClick = {
                                    expend.value = !expend.value
                                }
                            ) {
                                Row{
                                    Text(
                                        text =
                                        when (changeTheme.value) {
                                            0 -> "跟随系统"
                                            1 -> "浅色主题"
                                            2 -> "深色主题"
                                            else -> "跟随系统"
                                        }
                                    )
                                    Icon(if(expend.value) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,"")
                                }
                            }
                            DropdownMenu(
                                modifier = Modifier.padding(0.dp),
                                expanded = expend.value,
                                onDismissRequest = { expend.value = false }
                            ) {
                                DropdownMenuItem(
                                    modifier = Modifier.padding(0.dp),
                                    text = {
                                        Text(text = "跟随系统")
                                    },
                                    colors = MenuDefaults.itemColors(textColor = MaterialTheme.colorScheme.onSecondary),
                                    onClick = {
                                        changeTheme.value = 0
                                        updateSettings("changeTheme",0)
                                    }
                                )
                                DropdownMenuItem(
                                    modifier = Modifier.padding(0.dp),
                                    text = {
                                        Text(text = "浅色主题")
                                    },
                                    colors = MenuDefaults.itemColors(textColor = MaterialTheme.colorScheme.onSecondary),
                                    onClick = {
                                        changeTheme.value = 1
                                        updateSettings("changeTheme",1)
                                    }
                                )
                                DropdownMenuItem(
                                    modifier = Modifier.padding(0.dp),
                                    text = {
                                        Text(text = "深色主题")
                                    },
                                    colors = MenuDefaults.itemColors(textColor = MaterialTheme.colorScheme.onSecondary),
                                    onClick = {
                                        changeTheme.value = 2
                                        updateSettings("changeTheme",2)
                                    }
                                )
                            }
                        }
                    }
                }

            }
        }
    }
}