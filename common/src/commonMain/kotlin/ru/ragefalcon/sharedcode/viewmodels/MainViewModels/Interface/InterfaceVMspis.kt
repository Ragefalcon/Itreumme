package ru.ragefalcon.sharedcode.viewmodels.MainViewModels.Interface

import ru.ragefalcon.sharedcode.Database

class InterfaceVMspis(private val table: InterfaceStateTable) : CommonInterfaceSetting(table) {

    inner class InterfaceState() : MySettings() {
        inner class TimeServiceParam(
            code_name_razdel: String = "TimeServiceParam"
        ) : MySettings.RazdelSettingInner(
            "Служебные параметры для панели времени",
            code_name_razdel = code_name_razdel,
            type_razdel = TypeSaveStyleSet.NOTSAVE
        ) {
            val alarmSrokStart by settName(
                addBoolean(
                    "Сигнал о том что сегодня начинаются сроки каких то проектов",
                    false
                )
            )
            val dateAlarmSrokUpdate by settName(
                addSett(
                    InterfaceSettingsLong(
                        code_name_razdel,
                        "dateAlarmSrokUpdate",
                        0L
                    )
                )
            )
            val shablonCheckRepeat by settName(addBoolean("Включение повторов из шаблонов по умолчанию", true))
            val shablonCheckTime by settName(addBoolean("Включение времени из шаблонов по умолчанию", true))
            val shablonCheckStapName by settName(
                addBoolean(
                    "Включение имени этапа из следующего действия по умолчанию",
                    false
                )
            )
            val shablonCheckStapOpis by settName(
                addBoolean(
                    "Включение описания этапа из следующего действия по умолчанию",
                    false
                )
            )
        }

        val timeServiceParam = TimeServiceParam()

        val ADD_DEN_PLAN_WITH_100_PERCENT =
            add(InterfaceSettingsBoolean("", "add_den_plan_with_100_percent", false))

        val ADD_DEN_PLAN_WITH_100_PERCENT_FROM_TIME =
            add(InterfaceSettingsBoolean("", "add_den_plan_with_100_percent_from_time", false))

        val DefaultPercentForPlan = add(InterfaceSettingsBoolean("", "DefaultPercentForPlan", false))

    }

    val intSett = InterfaceState()

}

class InterfaceStateTable(private val mDB: Database) : CommonInterfaceSettingTable {
    override fun clearFromDeprecated(list: List<String>) {
        mDB.intrerfaceSettingsQueries.clearFromDeprecated(list)
    }

    override fun insert(code_name: String, defLong: Long, defDouble: Double, defString: String) {
        mDB.intrerfaceSettingsQueries.insert(code_name, defLong, defDouble, defString)
    }

    override fun update(code_name: String, long: Long) {
        mDB.intrerfaceSettingsQueries.updateLong(long, code_name)
    }

    override fun update(code_name: String, double: Double) {
        mDB.intrerfaceSettingsQueries.updateDouble(double, code_name)
    }

    override fun update(code_name: String, string: String) {
        mDB.intrerfaceSettingsQueries.updateString(string, code_name)
    }

    override fun transaction(ff: () -> Unit) {
        mDB.intrerfaceSettingsQueries.transaction {
            ff()
        }
    }

    override fun getListFromBase(): List<ItemInterfaceSetting> =
        mDB.intrerfaceSettingsQueries.selectInterfaceSettings().executeAsList().map {
            ItemInterfaceSetting(it.codename, it.intparam, it.doubleparam, it.stringparam)
        }

}

