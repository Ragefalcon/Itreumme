package MainTabs

import MainTabs.Setting.GoogleSincTab
import MainTabs.Setting.SettFinanceTabs
import MainTabs.Setting.SettHelpTab
import MainTabs.Setting.SettTimeTabs
import MyDialog.MyDialogLayout
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import common.DiskretSeekBar

class MainSettTabs(val dialLay: MyDialogLayout) {

//    val dialLay = MyDialogLayout()


    val seekSettingTabs =
        DiskretSeekBar(listOf("Синхронизация" to "google", "Финансы" to "finance", "Время" to "time", "Помощь" to "help"), "help")

    val googleTab = GoogleSincTab(dialLay)
    val financeTab = SettFinanceTabs(dialLay)
    val timeTab = SettTimeTabs(dialLay)
    val helpTab = SettHelpTab(dialLay)

    @Composable
    fun show() {
        Box() {
            Column {
                seekSettingTabs.show()
                seekSettingTabs.active?.let {
                    when (it.cod) {
                        "google" -> googleTab.show()
                        "finance" -> financeTab.show()
                        "time" -> timeTab.show()
                        "help" -> helpTab.show()
                        else -> {}
                    }
                }
            }
//            dialLay.getLay()
        }

    }
}