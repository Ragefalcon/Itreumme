package MainTabs.Avatar.Element

import MainTabs.OpenFileFilter
import MainTabs.imageFromFile
import MyDialog.MyDialogLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.dp
import common.BackgroungPanelStyle1
import common.MyTextButtStyle1
import extensions.ColumnVA
import extensions.RowVA
import ru.ragefalcon.sharedcode.models.data.ItemIconNodeTree
import ru.ragefalcon.sharedcode.source.disk.CommonName
import viewmodel.QuestDB
import viewmodel.StateVM
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import javax.swing.JFileChooser

fun PanViewAvatar(
    dialLay: MyDialogLayout,
    avatar: MutableState<ImageBitmap>
) {
    val innerDialLay = MyDialogLayout()

    dialLay.dial = @Composable {
        BackgroungPanelStyle1() {
            val size = 350.dp
            val shape = RoundedCornerShape(15.dp) //CircleShape
/*
            val avatarFile = File(StateVM.dirAvatar, CommonName.nameAvatarFile)

            val avatarF = remember {
                mutableStateOf(
                    if (avatarFile.exists()) imageFromFile(avatarFile) else useResource("iv_barash.jpg", ::loadImageBitmap)
                )
            }
*/
            ColumnVA {
                Box(
                    Modifier.padding(20.dp)
//                        .height(size)
//                        .width(size)
                    ,
                    contentAlignment = Alignment.TopCenter
                ) {
                    Image(
                        bitmap = avatar.value, //useResource("iv_avatar.png", ::loadImageBitmap), //BitmapPainter(
                        "defaultAvatar",
                        Modifier.wrapContentSize()
                            .height(size)
                            .width(size)
                            .clip(shape)
                            .border(1.dp, Color.White, shape)
                            .padding(1.dp)
                            .border(3.dp, Color(0x7FFFF7D9), RoundedCornerShape(14.dp))
                            .shadow(2.dp, shape),
                        contentScale = ContentScale.Crop,// Fit,
                    )
                }
                RowVA(Modifier.padding(bottom = 20.dp)) {
                    MyTextButtStyle1("Скрыть",Modifier.padding(end = 20.dp)) {
                        dialLay.close()
                    }
                    MyTextButtStyle1("Изменить") {
                        PanSelectAvatar(innerDialLay,avatar)
                    }

                }
            }

        }
        innerDialLay.getLay()
    }

    dialLay.show()
}

