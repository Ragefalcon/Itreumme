package common


import MyDialog.MyDialogLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import extensions.BoxOpisStyleState
import extensions.ComplexOpisStyleState
import ru.ragefalcon.sharedcode.models.data.ItemComplexOpis
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.Interface.CommonInterfaceSetting

@OptIn(androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun MyBoxOpisStyle(
    expandedOpis: MutableState<Boolean>,
    opis: List<ItemComplexOpis>,
    dialLay: MyDialogLayout? = null,
    style: CommonInterfaceSetting.MySettings.ComplexOpisStyleSetting,
) {
    MyBoxOpisStyle(expandedOpis, opis, dialLay, ComplexOpisStyleState(style))
}

@OptIn(androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun MyBoxOpisStyle(
    expandedOpis: MutableState<Boolean>,
    opis: List<ItemComplexOpis>,
    dialLay: MyDialogLayout? = null,
    style: ComplexOpisStyleState,
) {
    with(style) {
        MyShadowBox(
            shadow = plateView.shadow
        ) {
            if (expandedOpis.value)
                SelectionContainer {
                    MyComplexOpisView(
                        (if (expandedOpis.value) outer_padding else Modifier)
                            .fillMaxWidth()
                            .background(plateView.BACKGROUND, plateView.shape)
                            .clip(plateView.shape)
                            .border(
                                width = plateView.BORDER_WIDTH,
                                brush = plateView.BORDER,
                                shape = plateView.shape
                            )
                            .run {
                                if (expandedOpis.value) this.then(inner_padding)
                                else this
                            }, opis, dialLay, style
                    )
                }
        }
    }
}

@OptIn(androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun MyBoxOpisStyle(
    expandedOpis: MutableState<Boolean>,
    text: String,
    style: BoxOpisStyleState
) {
    with(style) {
        MyShadowBox(
            shadow = shadow

        ) {
            if (expandedOpis.value)
                SelectionContainer {
                    Text(
                        modifier = (if (expandedOpis.value) padingOuter
                        else Modifier
                                )
                            .fillMaxWidth()
                            .background(background, shapeCard)
                            .border(
                                width = borderWidth,
                                brush = border,
                                shape = shapeCard
                            )
                            .run {
                                if (expandedOpis.value) this.then(padingInner)
                                else this
                            },
                        text = text,
                        style = textStyle
                    )
                }
        }
    }
}
