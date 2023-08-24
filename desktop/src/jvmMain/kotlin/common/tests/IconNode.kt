package common.tests

import MainTabs.imageFromByteArray
import MainTabs.imageFromFile
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import extensions.CustomShape
import ru.ragefalcon.sharedcode.models.data.ItemIconNodeTree
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.MyTypeCorner
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeIconBorder
import viewmodel.StateVM
import java.io.File

@Composable
fun IconNode(icon: ItemIconNodeTree?, defaultResource: String, dirQuest: String? = null, complete:Boolean = false, size: Dp = 50.dp, modifier: Modifier = Modifier, pair: Pair<Dp,Brush>? = null){
    if (icon == null){
        IconNode(
            file = null,
            defaultResource,
            complete,
            size,
            null,
            modifier,
            TypeIconBorder.ROUNDCORNER, pair
        )
    }   else {
        val iconFile = File(dirQuest ?: StateVM.dirIconNodeTree, icon.name())
        IconNode(
            if (iconFile.exists()) imageFromFile(iconFile) else null,
            defaultResource,
            complete,
            size,
            null,
            modifier,
            TypeIconBorder.getType(icon.type_ramk) ?: TypeIconBorder.ROUNDCORNER, pair
        )
    }
}

@Composable
fun IconNode(folder: String, fileName: String, defaultResource: String, complete:Boolean = false, size: Dp = 50.dp, shape: Shape? = null, modifier: Modifier = Modifier, type: TypeIconBorder = TypeIconBorder.ROUNDCORNER, pair: Pair<Dp,Brush>? = null){
    val iconFile = File(
        File(System.getProperty("user.dir"), folder).path,fileName)
    IconNode(if (iconFile.exists()) imageFromFile(iconFile) else null,defaultResource, complete, size, shape, modifier, type, pair)
}

@Composable
fun IconNode(iconName: String, defaultResource: String, complete:Boolean = false, size: Dp = 50.dp, shape: Shape? = null, modifier: Modifier = Modifier, type: TypeIconBorder = TypeIconBorder.ROUNDCORNER, pair: Pair<Dp,Brush>? = null){
    val iconFile = File(StateVM.dirIconNodeTree,iconName)
    IconNode(if (iconFile.exists()) imageFromFile(iconFile) else null,defaultResource, complete, size, shape, modifier, type, pair)
}

@Composable
fun IconNode(byteArray: ByteArray?, defaultResource: String, complete:Boolean = false, size: Dp = 50.dp, shape: Shape? = null, modifier: Modifier = Modifier, type: TypeIconBorder = TypeIconBorder.ROUNDCORNER, pair: Pair<Dp,Brush>? = null){
//    imageFromByteArray(byteArray!!).asSkiaBitmap().asComposeImageBitmap().
    IconNode(if (byteArray != null) imageFromByteArray(byteArray) else null,defaultResource, complete, size, shape, modifier, type, pair)
}
@Composable
fun IconNode(file: ImageBitmap?, defaultResource: String, complete:Boolean = false, size: Dp = 50.dp, shape: Shape? = null, modifier: Modifier = Modifier, type: TypeIconBorder = TypeIconBorder.ROUNDCORNER, pair: Pair<Dp,Brush>? = null){
    val avatarF =
        if (file!=null) file else useResource(defaultResource,::loadImageBitmap)
    val padd = 10.dp
    val shapeBorder = shape ?: when (type){
            TypeIconBorder.NONE -> RectangleShape
            TypeIconBorder.ROUND -> CircleShape
            TypeIconBorder.SQUARE -> RectangleShape
            TypeIconBorder.ROUNDCORNER -> RoundedCornerShape(15.dp)
        TypeIconBorder.CUTCORNER -> CutCornerShape(15.dp)
        TypeIconBorder.TICKETCORNER -> CustomShape(MyTypeCorner.Ticket, CornerSize(15.dp))
    }
    Image(
        bitmap = avatarF,
        "defaultAvatar",
        modifier
            .padding(padd)
            .height(padd*2+size)
            .width(padd*2+size)
            .clip(shapeBorder)
            .run {
                if(type != TypeIconBorder.NONE || complete) {
                    if (pair != null) {
                        this.border(pair.first, pair.second, shapeBorder)
                    } else this.border(2.dp, if (complete) Color.Green else Color.White, shapeBorder)
                } else this
            }
//            .padding(4.dp)
            .wrapContentSize(),
        contentScale = ContentScale.Crop,// Fit,
//                            colorFilter = ColorFilter.tint(Color.White)
    )
}

