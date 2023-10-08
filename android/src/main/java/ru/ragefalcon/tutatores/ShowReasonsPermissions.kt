package ru.ragefalcon.tutatores

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private const val PERMISSIONS_REQUEST_CODE = 10

class ShowReasonsPermissions : ComponentActivity() {

    companion object {
        val keyExtraPermissions = "permission"
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("MyTag", "Разрешение предоставлено")
            // Разрешение предоставлено
            // Вы можете выполнить необходимые действия
        } else {
            Log.d("MyTag", "Разрешение не предоставлено")
            // Разрешение не предоставлено
            // Можете предоставить пользователю дополнительную информацию или попросить разрешение снова
        }
    }

    var permission: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.extras?.let {
            it.getString(keyExtraPermissions).let {
                if (it == null) onDestroy()
                else permission = it
            }
        }

        setContent {
            Column(Modifier.fillMaxSize().background(color = colorResource(R.color.colorPrimary))) {
                Column(
                    Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = when (permission) {
                            Manifest.permission.POST_NOTIFICATIONS -> {
                                "Оповещения нужны чтобы отображать процесс загрузки файла на гугл диск."
                            }
                            else -> "Нет подходящих объяснений."
                        },
                        modifier = Modifier.fillMaxWidth().padding(30.dp),
                        color = colorResource(R.color.colorSchetItem),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
                Row (Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center){
                    Button(onClick = {
                        onBackPressed()
                    }) {
                        Text("Назад")
                    }
                    Button(onClick = {
                        requestPermissionLauncher.launch(permission)
//                        requestPermissions(arrayOf(permission),PERMISSIONS_REQUEST_CODE)
                    }) {
                        Text("Разрешить")
                    }
                }
            }
        }
    }

}