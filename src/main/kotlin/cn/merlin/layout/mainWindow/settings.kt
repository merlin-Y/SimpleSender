import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.merlin.utils.Settings
import cn.merlin.utils.followSystemTheme

@Composable
fun setting(
    width: Dp
) {
    val expend = mutableStateOf(false)
    Surface(
        modifier = Modifier.size(width, 700.dp),
        color = MaterialTheme.colorScheme.secondary
    ) {
        Column {
            Text(
                modifier = Modifier.size(width, 40.dp).padding(start = 20.dp),
                text = "Settings",
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
                    Row {
                        Text(
                            text = "改变主题",
                            modifier = Modifier.padding(start = 20.dp)
                        )
                        Text(
                            text =
                            if (followSystemTheme.value) "跟随系统"
                            else {
                                if (Settings["userTheme"]?.value == 0) "浅色模式" else "深色模式"
                            },
                        )
                    }
                }
            }
        }
    }
}