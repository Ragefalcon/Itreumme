import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import ru.ragefalcon.sharedcode.source.disk.getPlatformName2

@Composable
fun App() {
    var text by remember { mutableStateOf("Hello, World 0 !") }

    Button(onClick = {
        text = "Hello, ${getPlatformName2()}" //
    }) {
        Text(text)
    }
}

expect fun getPlatformName(): String
