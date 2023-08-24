package MainTabs.Setting

import MyDialog.MyDialogLayout
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import common.DiskretSeekBar

class SettFinanceTabs(val dialLay: MyDialogLayout) {

    val seekFinanceTabs =
        DiskretSeekBar(
            listOf(
                "Типы расходов/доходов" to "typerd",
                "Счета и валюты" to "scheta",
                "Планирование" to "plans",
            ), "plans"
        )

    val typerTab = SettFinTypeRasxTab(dialLay)
    val typedTab = SettFinTypeDoxTab(dialLay)
    val schetTab = SettFinSchetTab(dialLay)
    val schetPlanTab = SettFinSchetPlanTab(dialLay)
    val typeRasxForSchetPlanTab = SettFinTypeRasxodForPlans(dialLay)
    val valutTab = SettFinValutTab(dialLay)

    @Composable
    fun show() {
        Column(Modifier.fillMaxSize()) {
            seekFinanceTabs.active?.let {
                when (it.cod) {
                    "scheta" -> Row(Modifier.weight(1f)) {
                        schetTab.show(Modifier.weight(1f))
                        valutTab.show(Modifier.weight(1f))
                    }
                    "plans" -> Row(Modifier.weight(1f)) {
                        schetPlanTab.show(Modifier.weight(1f))
                        typeRasxForSchetPlanTab.show(Modifier.weight(1f))
                    }
                    "typerd" -> Row(Modifier.weight(1f)) {
                        typerTab.show(Modifier.weight(1f))
                        typedTab.show(Modifier.weight(1f))
                    }
                    else -> {
                    }
                }
            }
            seekFinanceTabs.show()
        }
    }

}