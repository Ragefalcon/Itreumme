package common

import MainTabs.Setting.openInBrowser
import MainTabs.imageFromFile
import MyDialog.MyDialogLayout
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import extensions.*
import ru.ragefalcon.sharedcode.models.data.*
import viewmodel.MainDB
import viewmodel.StateVM
import java.io.File
import java.net.URI

@Composable
fun MyComplexOpisView(
    modifier: Modifier,
    listOpis: List<ItemComplexOpis>,
    dialLay: MyDialogLayout? = null,
    styleState: ComplexOpisStyleState
) {

    with(styleState) {
        Column(modifier) {
            listOpis.forEach { itemOpis ->
                with(LocalDensity.current) {
                    when (itemOpis) {
                        is ItemComplexOpisTextCommon -> { //Common
                            MyShadowBox(
                                if (itemOpis is ItemComplexOpisCheckbox) {
                                    when (itemOpis.checked) {
                                        0L -> plateCheckboxFalse.shadow
                                        1L -> plateCheckboxTrue.shadow
                                        2L -> plateCheckboxFail.shadow
                                        3L -> plateCheckboxQuestion.shadow
                                        4L -> plateCheckboxPriority.shadow
                                        else -> plateCheckboxFalse.shadow
                                    }
                                } else Shadow(Color.Transparent)
                            ) {
                                RowVA(Modifier.padding(vertical = 1.dp).fillMaxWidth()
                                    .run {
                                        if (itemOpis.sort < 0) this.alpha(0.3f) else this
                                    }
                                    .run {
                                        if (itemOpis is ItemComplexOpisCheckbox) {
                                            this.padding(vertical = 2.dp)
                                                .withSimplePlate(
                                                    when (itemOpis.checked) {
                                                        0L -> plateCheckboxFalse
                                                        1L -> plateCheckboxTrue
                                                        2L -> plateCheckboxFail
                                                        3L -> plateCheckboxQuestion
                                                        4L -> plateCheckboxPriority
                                                        else -> plateCheckboxFalse
                                                    }
                                                )
                                        } else this
                                    }
//                                .border(2.dp, MyColorARGB.colorMyBorderStroke.toColor(), Cust tCornerShape(8.dp))
//                                .clip(CutCornerShape(8.dp))
                                ) {
                                    @Composable
                                    fun mainText() {
                                        Text(
                                            itemOpis.text,
                                            modifier = Modifier.weight(1f, false),
                                            style = baseText.copy(
                                                color = colorSet.getColor(itemOpis.color),
                                                fontSize = fontSet.getFontSize(itemOpis.fontSize),
                                                fontStyle = if (itemOpis.cursiv) FontStyle.Italic else FontStyle.Normal,
                                                fontWeight = FontWeight(if (itemOpis.bold == 1) 600 else 400)
                                            )
                                        )
                                    }
                                    if (itemOpis is ItemComplexOpisCheckbox) {
                                        Image(
                                            painterResource(
                                                when (itemOpis.checked) {
                                                    0L -> "ic_round_check_box_outline_blank_24.xml"
                                                    1L -> "ic_baseline_check_box_24.xml"
                                                    2L -> "ic_baseline_close_24.xml"
                                                    3L -> "ic_baseline_question_mark_24.xml"
                                                    4L -> "ic_baseline_priority_high_24.xml"
                                                    else -> "ic_round_check_box_outline_blank_24.xml"
                                                }
                                            ),
                                            "MyCheckBox",
                                            Modifier.padding(end = 10.dp)
                                                .height(25.dp)
                                                .width(25.dp)
                                                .clickable {
                                                    if (itemOpis.id != -1L) MainDB.addComplexOpis.updComplexOpisCheckbox(
                                                        itemOpis.id,
                                                        itemOpis.nextCheck().checked
                                                    )
                                                },
                                            contentScale = ContentScale.Fit,
                                            colorFilter = ColorFilter.tint(
                                                when (itemOpis.checked) {
                                                    0L -> colorCheckboxFalse
                                                    1L -> colorCheckboxTrue
                                                    2L -> colorCheckboxFail
                                                    3L -> colorCheckboxQuestion
                                                    4L -> colorCheckboxPriority
                                                    else -> colorCheckboxFalse
                                                }
                                            )
                                        )
                                    }
                                    if (itemOpis is ItemComplexOpisImageGroup) {
                                        BoxWithConstraints {
                                            RowVA {
                                                if (itemOpis.enableText && itemOpis.textBefore) mainText()
                                                MyShadowBox(shadowImage) {
                                                    MeasureUnconstrainedViewWidth({ mainText() }) { widthText ->
                                                        println("widthText = ${widthText}")
                                                        val smallText =
                                                            (widthText + 10.dp) / this@BoxWithConstraints.maxWidth < 1f / 3f
                                                        PlateOrderLayout(
                                                            modifier = Modifier.padding(vertical = 5.dp)
                                                                .padding(
                                                                    start = if (itemOpis.enableText && itemOpis.textBefore) 10.dp else 0.dp,
                                                                    end = if (itemOpis.enableText && itemOpis.textBefore.not()) 10.dp else 0.dp
                                                                )
                                                                .run {
                                                                    if (itemOpis.enableText) {
                                                                        if (smallText) this.widthIn(
                                                                            0.dp,
                                                                            this@BoxWithConstraints.maxWidth - widthText - 10.dp
                                                                        ) else this.widthIn(
                                                                            0.dp,
                                                                            this@BoxWithConstraints.maxWidth * 2f / 3f - 10.dp
                                                                        )
                                                                    } else this
                                                                },
//                                                koefMaxWidth = if (itemOpis.enableText && smallText.not()) 2f / 3f else 1f,
                                                            fillWidth = false,
                                                            spaceBetween = 10.dp.toPx().toInt()
                                                        ) {
                                                            itemOpis.spisImages.sortedBy { it.sort }.forEach { itImg ->
                                                                val interactionSource =
                                                                    remember { MutableInteractionSource() }
                                                                val isHovered by interactionSource.collectIsHoveredAsState()

                                                                val imageIB: MutableState<ImageBitmap?> =
                                                                    mutableStateOf<ImageBitmap?>(null).apply {
                                                                        val ff = File(
                                                                            StateVM.dirComplexOpisImages,
                                                                            "complexOpisImage_${itImg.id}.jpg"
                                                                        )
                                                                        if (ff.exists()) {
                                                                            value = imageFromFile(ff)
                                                                        }
                                                                    }

                                                                imageIB.value?.let { imgBtm ->
                                                                    Image(
                                                                        bitmap = imgBtm, //BitmapPainter(
                                                                        "defaultAvatar",
                                                                        Modifier
//                                                        .padding(vertical = 5.dp)
//                                                        .padding(end = 10.dp)
                                                                            .wrapContentSize()
                                                                            .heightIn(
                                                                                0.dp, when (itemOpis.sizePreview) {
                                                                                    1L -> 50.dp
                                                                                    2L -> 100.dp
                                                                                    3L -> 170.dp
                                                                                    else -> 100.dp
                                                                                }
                                                                            )
                                                                            .run {
                                                                                if (itemOpis.widthLimit) this.widthIn(
                                                                                    0.dp, when (itemOpis.sizePreview) {
                                                                                        1L -> 50.dp
                                                                                        2L -> 100.dp
                                                                                        3L -> 170.dp
                                                                                        else -> 100.dp
                                                                                    }
                                                                                ) else this
                                                                            }
                                                                            .border(
                                                                                BorderStroke(
                                                                                    1.dp,
                                                                                    if (isHovered) brushBoundImageActive else brushBoundImage
                                                                                )
                                                                            )
                                                                            .hoverable(interactionSource)
                                                                            .clickable(interactionSource, null) {
                                                                                PanViewImageList(
                                                                                    itemOpis.spisImages.map {
                                                                                        File(
                                                                                            StateVM.dirComplexOpisImages,
                                                                                            "complexOpisImage_${it.id}.jpg"
                                                                                        )
                                                                                    },
                                                                                    File(
                                                                                        StateVM.dirComplexOpisImages,
                                                                                        "complexOpisImage_${itImg.id}.jpg"
                                                                                    )
                                                                                )
                                                                            },
                                                                        contentScale = ContentScale.Fit,
                                                                    )
                                                                }
                                                                if (imageIB.value == null) Image(
                                                                    painterResource("ic_round_delete_forever_24.xml"),
                                                                    "not_exist_img",
                                                                    Modifier//.padding(end = 10.dp)
                                                                        .height(
                                                                            when (itemOpis.sizePreview) {
                                                                                1L -> 50.dp
                                                                                2L -> 100.dp
                                                                                3L -> 170.dp
                                                                                else -> 100.dp
                                                                            }
                                                                        )
                                                                        .width(
                                                                            when (itemOpis.sizePreview) {
                                                                                1L -> 50.dp
                                                                                2L -> 100.dp
                                                                                3L -> 170.dp
                                                                                else -> 100.dp
                                                                            }
                                                                        ),
                                                                    contentScale = ContentScale.Fit,
                                                                    colorFilter = ColorFilter.tint(Color.Red.copy(0.5f))
                                                                )
                                                            }
                                                        }
                                                    }

                                                }
                                                if (itemOpis.enableText && itemOpis.textBefore.not()) mainText()
                                            }
                                        }
                                    }
                                    if (itemOpis !is ItemComplexOpisImageGroup && itemOpis !is ItemComplexOpisLink) mainText()
                                    if (itemOpis is ItemComplexOpisLink) {
                                        MyShadowBox(plateLink.shadow) {
                                            RowVA(
                                                Modifier.fillMaxWidth().withSimplePlate(plateLink)
                                                    .then(inner_padding_link)
                                            ) {
                                                MyIconButtWithoutBorder(
                                                    "ic_round_keyboard_double_arrow_up_24.xml",
                                                    sizeIcon = 30.dp,
                                                    myStyleButton = buttIconLink
                                                ) {
                                                    if ("^https://|^http://|^www\\.".toRegex()
                                                            .containsMatchIn(itemOpis.link)
                                                    )
                                                        openInBrowser(URI(itemOpis.link))
                                                    else openInBrowser(URI("www.${itemOpis.link}"))
                                                }
                                                mainText()
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

