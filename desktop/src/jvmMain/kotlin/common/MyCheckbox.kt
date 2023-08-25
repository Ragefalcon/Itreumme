package common

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import extensions.CheckboxStyleState
import extensions.RowVA
import viewmodel.MainDB

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyCheckbox(
    boolVal: MutableState<Boolean>,
    label: String,
    modifier: Modifier = Modifier,
    style: CheckboxStyleState = CheckboxStyleState(MainDB.styleParam.finParam.rasxodParam.panAddRasxod.checkBoxPlan),
    onCheckedChange: (Boolean) -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    with(style) {
        RowVA(modifier
            .mouseClickable {
                boolVal.value = boolVal.value.not()
                onCheckedChange(boolVal.value)
            }
            .hoverable(interactionSource = interactionSource)
        ) {

            Image(
                painterResource(if (boolVal.value) "ic_baseline_check_box_24.xml" else "ic_round_check_box_outline_blank_24.xml"),
                "MyCheckBox",
                Modifier
                    .height(25.dp)
                    .width(25.dp),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(if (boolVal.value) colorTrue else colorFalse)
            )
            Text(
                label,
                modifier = Modifier
                    .offset(
                        if (isHovered) offsetTextHover.x.dp else 0.dp,
                        if (isHovered) offsetTextHover.y.dp else 0.dp
                    )
                    .padding(start = 10.dp),
                style = (if (isHovered) textStyleShadowHover else textStyle)
            )
        }

    }
}

@Composable
fun MyCheckbox(boolVal: MutableState<Boolean>, modifier: Modifier = Modifier, onCheckedChange: (Boolean) -> Unit = {}) {
    Image(
        painterResource(if (boolVal.value) "ic_baseline_check_box_24.xml" else "ic_round_check_box_outline_blank_24.xml"),
        "MyCheckBox",
        modifier
            .height(25.dp)
            .width(25.dp)
            .clickable {
                boolVal.value = !boolVal.value
                onCheckedChange(boolVal.value)
            },
        contentScale = ContentScale.Fit,
    )
}


