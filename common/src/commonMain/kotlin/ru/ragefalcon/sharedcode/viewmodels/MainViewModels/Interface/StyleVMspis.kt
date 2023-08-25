package ru.ragefalcon.sharedcode.viewmodels.MainViewModels.Interface

import ru.ragefalcon.sharedcode.Database
import ru.ragefalcon.sharedcode.extensions.MyColorARGB

class InterfaceStyleTable(private val mDB: Database) : CommonInterfaceSettingTable {
    override fun clearFromDeprecated(list: List<String>) {
        mDB.styleSettingsQueries.clearFromDeprecated(list)
    }

    override fun insert(code_name: String, defLong: Long, defDouble: Double, defString: String) {
        mDB.styleSettingsQueries.insert(code_name, defLong, defDouble, defString)
    }

    override fun update(code_name: String, long: Long) {
        mDB.styleSettingsQueries.updateLong(long, code_name)
    }

    override fun update(code_name: String, double: Double) {
        mDB.styleSettingsQueries.updateDouble(double, code_name)
    }

    override fun update(code_name: String, string: String) {
        mDB.styleSettingsQueries.updateString(string, code_name)
    }

    override fun transaction(ff: () -> Unit) {
        mDB.styleSettingsQueries.transaction {
            ff()
        }
    }

    override fun getListFromBase(): List<ItemInterfaceSetting> =
        mDB.styleSettingsQueries.selectInterfaceSettings().executeAsList().map {
            ItemInterfaceSetting(it.codename, it.intparam, it.doubleparam, it.stringparam)
        }

}

class StyleVMspis(private val table: InterfaceStyleTable) : CommonInterfaceSetting(table) {

    inner class InterfaceState() : MySettings() {
        inner class CommonItemStyle(
        ) : CommonStyleItemSetting(
            "Стандартный айтем",
            "common_item_style2",
            type_razdel = TypeSaveStyleSet.COMMONITEM
        ) {
        }


        inner class ItemBloknotStyle(
        ) : ItemStyleWithOpisSetting(
            "Айтем блокнота",
            "item_bloknot_style",
            type_razdel = TypeSaveStyleSet.COMMONITEM
        ) {
            val COLOR_BUTT_OPEN by settName(addColor("Цвет кнопки \uD83D\uDD6E", MyColorARGB.colorMyBorderStroke))
            val countText = r_addTextStyle("count_text", "Текст количества", MyColorARGB("AFFFF7D9"), 10.0)
        }

        inner class ItemIdeaStyle(
        ) : ItemStyleWithOpisSetting(
            "Айтем раздела блокнота",
            "item_idea_style",
            type_razdel = TypeSaveStyleSet.COMMONITEM
        ) {


        }

        inner class ItemIdeaStapStyle(
        ) : ItemStyleWithOpisSetting(
            "Айтем заметки в блокноте",
            "item_idea_stap_style",
            type_razdel = TypeSaveStyleSet.COMMONITEM
        ) {
            val COLOR_BUTT_OPEN by settName(addColor("Цвет кнопки \uD83D\uDD6E", MyColorARGB.colorMyBorderStroke))
            val opisOpenText by nameRazd { name ->
                add2(
                    CommonBoxOpisWithButtStyleItemSetting(
                        "Стиль описания (откр.)",
                        name,
                        "opis_style",
                        type_razdel = TypeSaveStyleSet.COMMONITEM
                    )
                )
            }
        }

        inner class ItemDenPlanStyle(
        ) : ItemStyleWithOpisSetting(
            "Айтем плана ежедневника",
            "item_den_plan_style",
            type_razdel = TypeSaveStyleSet.COMMONITEM
        ) {
            val SLIDER_COLOR1 by settName(addColor("Слайдер цвет 1", MyColorARGB("6FFF8888")))
            val SLIDER_COLOR2 by settName(addColor("Слайдер цвет 2", MyColorARGB.colorEffektShkal_Nedel))
            val countText = r_addTextStyle("count_text", "Текст количества часов", MyColorARGB("FFFFF7F9"), 13.0)
            val timeText = r_addTextStyle("time_text", "Текст времени", MyColorARGB("9900E6E6"), 13.0)
            val privPlanText = r_addTextStyle("privPlanText", "Текст привязки", MyColorARGB("9900E6E6"), 13.0)
            val BORDER_WIDTH_PRIV_PLAN by settName(addDoublePoz("Ширина границы привязки", 1.0))
            val shadow_priv_plan by nameRazd { name -> r_addShadowStyle(name, "Стиль тени") }
            val BACKGROUND_PRIV_PLAN = r_addBrush("BACKGROUND_PRIV_PLAN", "Фон привязки")
            val BORDER_PRIV_PLAN = r_addBrush("BORDER_PRIV_PLAN", "Граница привязки")
            val CORNER_PRIV_PLAN = r_addShape("shape_PRIV_PLAN", "Форма привязки")
        }

        inner class ItemPlanStyle(
        ) : ItemStyleWithOpisSetting(
            "Айтем проекта",
            "item_plan_style",
            type_razdel = TypeSaveStyleSet.COMMONITEM
        ) {
            val SLIDER_COLOR1 by settName(addColor("Слайдер цвет 1", MyColorARGB("6FFF8888")))
            val SLIDER_COLOR2 by settName(addColor("Слайдер цвет 2", MyColorARGB.colorEffektShkal_Nedel))
            val hourText by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст количества часов",
                    MyColorARGB("FFFFF7F9"),
                    13.0
                )
            }
            val countStapText by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст количества этапов",
                    MyColorARGB.colorEffektShkal_Nedel,
                    15.0
                )
            }
            val questNameText by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Название квеста",
                    MyColorARGB("9900E6E6"),
                    13.0
                )
            }
            val BORDER_WIDTH_QUEST by settName(addDoublePoz("Ширина квестовой рамки", 1.0))
            val border_quest by nameRazd { name -> r_addBrush(name, "Квестовая рамка") }

            val quest_plate by nameRazd { name -> r_addSimplePlateWithShadow(name, "Табличка квеста") }

            val background_brush_gotov by nameRazd { name -> r_addBrush(name, "Фон выполненного") }
            val border_brush_gotov by nameRazd { name -> r_addBrush(name, "Граница выполненного") }
            val background_brush_unblock by nameRazd { name -> r_addBrush(name, "Фон разблокированного") }
            val border_brush_unblock by nameRazd { name -> r_addBrush(name, "Граница разблокированного") }
            val background_brush_freeze by nameRazd { name -> r_addBrush(name, "Фон замороженного") }
            val border_brush_freeze by nameRazd { name -> r_addBrush(name, "Граница замороженного") }
            val background_brush_close by nameRazd { name -> r_addBrush(name, "Фон закрытого") }
            val border_brush_close by nameRazd { name -> r_addBrush(name, "Граница закрытого") }
            val background_brush_direction by nameRazd { name -> r_addBrush(name, "Фон направления") }
            val border_brush_direction by nameRazd { name -> r_addBrush(name, "Граница направления") }
            val background_brush_direction_freeze by nameRazd { name ->
                r_addBrush(
                    name,
                    "Фон направления замороженного"
                )
            }
            val border_brush_direction_freeze by nameRazd { name ->
                r_addBrush(
                    name,
                    "Граница направления замороженного"
                )
            }
            val background_brush_direction_close by nameRazd { name -> r_addBrush(name, "Фон направления закрытого") }
            val border_brush_direction_close by nameRazd { name -> r_addBrush(name, "Граница направления закрытого") }
            val dataText by nameRazd { name -> r_addTextStyle(name, "Текст сроков", MyColorARGB("AFFFF7D9"), 10.0) }
            val plateSrok by nameRazd { name -> r_addSimplePlate(name, "Плитка срока 1") }
            val plateSrokIn by nameRazd { name -> r_addSimplePlate(name, "Плитка срока 2") }
        }

        inner class ItemSkillsTreeStyle(
        ) : ItemStyleWithOpisSetting(
            "Айтем древа ачивок",
            "item_skills_tree_style",
            type_razdel = TypeSaveStyleSet.COMMONITEM
        ) {
            val topPadding by settName(addDoublePoz("Отступ от назв. квеста", 5.0))
            val OPEN_COLOR by settName(addColor("Цвет кнопки открытия", MyColorARGB("FFFFFFFF")))
            val ICON_TREE_COLOR by settName(addColor("Цвет иконки дерева", MyColorARGB("FFFFFFFF")))
            val INFO_ICON_COLOR by settName(addColor("Цвет иконок статуса", MyColorARGB("FFFFFFFF")))
            val countStapText by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст количества ачивок",
                    MyColorARGB.colorSchetTheme,
                    12.0
                )
            }
            val questNameText by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Название квеста",
                    MyColorARGB("9900E6E6"),
                    13.0
                )
            }
            val BORDER_WIDTH_QUEST by settName(addDoublePoz("Ширина квестовой рамки", 1.0))
            val border_quest by nameRazd { name -> r_addBrush(name, "Квестовая рамка") }

            val icon_plate by nameRazd { name -> r_addSimplePlateWithShadow(name, "Плитка иконки ") }
            val quest_plate by nameRazd { name -> r_addSimplePlateWithShadow(name, "Табличка квеста") }

            val background_brush_no_edit by nameRazd { name -> r_addBrush(name, "Фон закрытого редактирования") }
            val border_brush_no_edit by nameRazd { name -> r_addBrush(name, "Граница закрытого редактирования") }
            val background_brush_unblock by nameRazd { name -> r_addBrush(name, "Фон разблокированного") }
            val border_brush_unblock by nameRazd { name -> r_addBrush(name, "Граница разблокированного") }
            val background_brush_block by nameRazd { name -> r_addBrush(name, "Фон заблокированного") }
            val border_brush_block by nameRazd { name -> r_addBrush(name, "Граница заблокированного") }
            val background_brush_complete by nameRazd { name -> r_addBrush(name, "Фон выполненного") }
            val border_brush_complete by nameRazd { name -> r_addBrush(name, "Граница выполненного") }
        }

        inner class ItemNodeTreeSkillsStyle(
        ) : ItemStyleWithOpisSetting(
            "Айтем узла древа ачивок",
            "item_node_tree_skills_style",
            type_razdel = TypeSaveStyleSet.COMMONITEM
        ) {
            val border_width_must by settName(addDoublePoz("Ширина гр-цы обязательного узла", 3.0))
            val border_width_parent_child by settName(addDoublePoz("Ширина гр-цы \"родственников\"", 3.0))
            val border_width_icon by settName(addDoublePoz("Ширина гр-цы иконки", 2.0))
            val BORDER_ICON_COLOR by nameRazd { name -> r_addBrush(name, "Граница рамки иконки") }
            val INFO_ICON_COLOR by settName(addColor("Цвет иконок статуса", MyColorARGB("FFFFFFFF")))
            val noQuestText by nameRazd { name -> r_addTextStyle(name, "Текст *", MyColorARGB.colorDoxodItem0, 15.0) }
            val background_brush_complete by nameRazd { name -> r_addBrush(name, "Фон выполненного") }
            val border_brush_complete by nameRazd { name -> r_addBrush(name, "Граница выполненного") }
            val background_brush_unblock by nameRazd { name -> r_addBrush(name, "Фон разблокированного") }
            val border_brush_unblock by nameRazd { name -> r_addBrush(name, "Граница разблокированного") }
            val background_brush_block by nameRazd { name -> r_addBrush(name, "Фон заблокированного") }
            val border_brush_block by nameRazd { name -> r_addBrush(name, "Граница заблокированного") }
            val border_brush_DIRECTPARENT by nameRazd { name -> r_addBrush(name, "Граница прямого родителя") }
            val border_brush_INDIRECTPARENT by nameRazd { name -> r_addBrush(name, "Граница непрямого родителя") }
            val border_brush_DIRECTCHILD by nameRazd { name -> r_addBrush(name, "Граница прямого потомка") }
            val border_brush_INDIRECTCHILD by nameRazd { name -> r_addBrush(name, "Граница непрямого потомка") }
        }

        inner class ItemSkillsTreeLevelStyle(
        ) : ItemStyleWithOpisSetting(
            "Айтем уровня древа ачивок",
            "item_skills_tree_level_style",
            type_razdel = TypeSaveStyleSet.COMMONITEM
        ) {
            val INFO_COLOR_1 by settName(addColor("Цвет инфор-и 1", MyColorARGB("FFFFFFFF")))
            val INFO_COLOR_2 by settName(addColor("Цвет инфор-и 2", MyColorARGB("FFFFFFFF")))
            val infoText by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст информации",
                    MyColorARGB.colorSchetTheme,
                    12.0
                )
            }
            val noQuestText by nameRazd { name -> r_addTextStyle(name, "Текст *", MyColorARGB.colorDoxodItem0, 15.0) }

            val background_brush_no_edit by nameRazd { name -> r_addBrush(name, "Фон закрытого редактирования") }
            val border_brush_no_edit by nameRazd { name -> r_addBrush(name, "Граница закрытого редактирования") }
            val background_brush_no_edit_block by nameRazd { name -> r_addBrush(name, "Фон закр. ред. и заблок-го") }
            val border_brush_no_edit_block by nameRazd { name -> r_addBrush(name, "Граница закр. ред. и заблок-го") }
            val background_brush_block by nameRazd { name -> r_addBrush(name, "Фон заблокированного") }
            val border_brush_block by nameRazd { name -> r_addBrush(name, "Граница заблокированного") }


        }

        inner class ItemGoalStyle(
        ) : ItemStyleWithOpisSetting(
            "Айтем цели",
            "item_goal_style",
            type_razdel = TypeSaveStyleSet.COMMONITEM
        ) {
            val ARROW_COLOR by settName(addColor("Кнопки упорядочивания", MyColorARGB("6FFF8888")))
            val hourText by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст количества часов",
                    MyColorARGB("FFFFF7F9"),
                    13.0
                )
            }
            val background_brush_gotov by nameRazd { name -> r_addBrush(name, "Фон достигнутой") }
            val border_brush_gotov by nameRazd { name -> r_addBrush(name, "Граница достигнутой") }
        }

        inner class ItemDreamStyle(
        ) : ItemStyleWithOpisSetting(
            "Айтем мечты",
            "item_dream_style",
            type_razdel = TypeSaveStyleSet.COMMONITEM
        ) {
            val ARROW_COLOR by settName(addColor("Кнопки упорядочивания", MyColorARGB("6FFF8888")))
            val background_brush_gotov by nameRazd { name -> r_addBrush(name, "Фон достигнутой") }
            val border_brush_gotov by nameRazd { name -> r_addBrush(name, "Граница достигнутой") }
        }

        inner class ItemBestDayStyle(
        ) : ItemStyleWithOpisSetting(
            "Айтем памятного дня",
            "item_best_day_style",
            type_razdel = TypeSaveStyleSet.COMMONITEM
        ) {
            val BLACKandWHITE by settName(addBoolean("Черно белое", true))
            val TINT by settName(addColor("Тонирование", MyColorARGB.colorMyBorderStroke))
            val image_border_width by settName(addDoublePoz("Ширина граница изоб-я", 2.0))
            val OPEN_BUTT_COLOR by settName(addColor("Кнопка открытия", MyColorARGB("6FFF8888")))
            val image_border_brush by nameRazd { name -> r_addBrush(name, "Граница изоб-я") }
            val image_shape by nameRazd { name -> r_addShape(name, "Форма изоб-я") }
            val image_shadow by nameRazd { name -> r_addShadowStyle(name, "Стиль тени изоб-я") }
            val dateText by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст даты",
                    MyColorARGB("FFFFF7F9"),
                    13.0
                )
            }
        }

        inner class ItemCharacteristicStyle(
        ) : ItemStyleWithOpisSetting(
            "Айтем характеристики",
            "item_characteristic_style",
            type_razdel = TypeSaveStyleSet.COMMONITEM
        ) {
            val plateNonEdit by nameRazd { name -> r_addSimplePlateWithShadow(name, "Панель не ред. айтема") }
            val outerPaddingNonEdit by nameRazd { name -> r_addPadding(name, "Внеш. отступы не ред. айтема") }
            val innerPaddingNonEdit by nameRazd { name -> r_addPadding(name, "Внут. отступы не ред. айтема") }
            val ARROW_COLOR by settName(addColor("Кнопки упорядочивания", MyColorARGB("6FFF8888")))
            val COLOR_INDIK_BACK by settName(addColor("Фон индикатора", MyColorARGB("FF444444")))
            val COLOR_INDIK_COMPLETE by settName(addColor("Заполнение индикатора", MyColorARGB.colorEffektShkal_Month))
            val COLOR_INDIK_BORDER by settName(addColor("Граница индикатора", MyColorARGB.colorMyBorderStroke.copy(51)))
            val valueText by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст значения",
                    MyColorARGB("FFFFFFFF"),
                    22.0
                )
            }
            val startValueText by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст стартового значения",
                    MyColorARGB("BFFFFFFF"),
                    16.0
                )
            }
        }

        inner class ItemPlanStapStyle(
        ) : ItemStyleWithOpisSetting(
            "Айтем этапа проекта",
            "item_plan_stap_style",
            type_razdel = TypeSaveStyleSet.COMMONITEM
        ) {
            val levelValue by settName(addDoublePoz("Отступ подэтапов", 15.0))
            val SLIDER_COLOR1 by settName(addColor("Слайдер цвет 1", MyColorARGB("6FFF8888")))
            val SLIDER_COLOR2 by settName(addColor("Слайдер цвет 2", MyColorARGB.colorEffektShkal_Nedel))
            val PLUS_COLOR by settName(addColor("Цвет +", MyColorARGB("FFFFFFFF")))
            val MINUS_COLOR by settName(addColor("Цвет -", MyColorARGB("FFFFFFFF")))
            val hourText by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст количества часов",
                    MyColorARGB("FFFFF7F9"),
                    13.0
                )
            }
            val countStapText by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст количества подэтапов",
                    MyColorARGB.colorEffektShkal_Nedel,
                    15.0
                )
            }
            val noQuestText by nameRazd { name -> r_addTextStyle(name, "Текст *", MyColorARGB.colorDoxodItem0, 15.0) }
            val background_brush_gotov by nameRazd { name -> r_addBrush(name, "Фон выполненного") }
            val border_brush_gotov by nameRazd { name -> r_addBrush(name, "Граница выполненного") }
            val background_brush_unblock by nameRazd { name -> r_addBrush(name, "Фон разблокированного") }
            val border_brush_unblock by nameRazd { name -> r_addBrush(name, "Граница разблокированного") }
            val background_brush_freeze by nameRazd { name -> r_addBrush(name, "Фон замороженного") }
            val border_brush_freeze by nameRazd { name -> r_addBrush(name, "Граница замороженного") }
            val background_brush_close by nameRazd { name -> r_addBrush(name, "Фон закрытого") }
            val border_brush_close by nameRazd { name -> r_addBrush(name, "Граница закрытого") }
            val dataText by nameRazd { name -> r_addTextStyle(name, "Текст сроков", MyColorARGB("AFFFF7D9"), 10.0) }
            val plateSrok by nameRazd { name -> r_addSimplePlate(name, "Плитка срока 1") }
            val plateSrokIn by nameRazd { name -> r_addSimplePlate(name, "Плитка срока 2") }
        }

        inner class ItemSchetOperStyle(
            name_podrazdel: String, code_name_item: String, nameSett: String
        ) : StyleItemSetting(
            nameSett,
            "${code_name_item}_${name_podrazdel}",
            type_razdel = TypeSaveStyleSet.COMMONITEM
        ) {
            val SLIDER_COLOR1 by settName(addColor("Слайдер цвет 1", MyColorARGB("6FFF8888")))
            val SLIDER_COLOR2 by settName(addColor("Слайдер цвет 2", MyColorARGB.colorEffektShkal_Nedel))
            val PLUS_COLOR by settName(addColor("Цвет +", MyColorARGB("FFFFFFFF")))
            val MINUS_COLOR by settName(addColor("Цвет -", MyColorARGB("FFFFFFFF")))

            val textType by nameRazd { name -> r_addTextStyle(name, "Текст типа", MyColorARGB("FFFFFFFF"), 13.0) }
            val textSchet by nameRazd { name -> r_addTextStyle(name, "Текст счета", MyColorARGB("9900E6E6"), 14.0) }
            val textSumm by nameRazd { name -> r_addTextStyle(name, "Текст суммы", MyColorARGB("FFFFFFFF"), 25.0) }
            val textDate by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст даты",
                    MyColorARGB.colorMyBorderStrokeCommon,
                    12.0
                )
            }

            val background_brush_gotov by nameRazd { name -> r_addBrush(name, "Фон выполненного") }
            val border_brush_gotov by nameRazd { name -> r_addBrush(name, "Граница выполненного") }
            val background_brush_unblock by nameRazd { name -> r_addBrush(name, "Фон разблокированного") }
            val border_brush_unblock by nameRazd { name -> r_addBrush(name, "Граница разблокированного") }
        }

        inner class ItemRasxDoxOperStyle(
            name_podrazdel: String, code_name_item: String, nameSett: String
        ) : StyleItemSetting(
            nameSett,
            "${code_name_item}_${name_podrazdel}",
            type_razdel = TypeSaveStyleSet.COMMONITEM
        ) {
            val textType by nameRazd { name -> r_addTextStyle(name, "Текст типа", MyColorARGB("FFFFFFFF"), 15.0) }
            val textSchet by nameRazd { name -> r_addTextStyle(name, "Текст счета", MyColorARGB("9900E6E6"), 13.0) }
            val textSumm by nameRazd { name -> r_addTextStyle(name, "Текст суммы", MyColorARGB("FFFFF7F9"), 17.0) }
            val textDate by nameRazd { name -> r_addTextStyle(name, "Текст даты", MyColorARGB("BFFFF7D9"), 11.0) }
        }

        inner class ItemSchetGraf(
            code_parent: String,
            code_razdel: String
        ) : RazdelSettingInner(
            "Айтем счета с полоской",
            code_name_razdel = "${code_parent}_${code_razdel}",
            type_razdel = TypeSaveStyleSet.FULLPANEL,
            sver = true, color = MyColorARGB("7A76A4A1")
        ) {
            val plateMain by nameRazd { name -> r_addSimplePlateWithShadow(name, "Основная панель") }
            val platePolos by nameRazd { name -> r_addSimplePlateWithShadow(name, "Полоска") }
            val platePolosMinus by nameRazd { name -> r_addSimplePlateWithShadow(name, "Полоска минус") }
            val textName by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст названия счета",
                    MyColorARGB("9900E6E6"),
                    13.0
                )
            }
            val textSumm by nameRazd { name -> r_addTextStyle(name, "Текст суммы", MyColorARGB("FFFFF7F9"), 17.0) }
            val textValut by nameRazd { name -> r_addTextStyle(name, "Текст валюты", MyColorARGB("BFFFF7D9"), 11.0) }
            val outer_padding by nameRazd { name -> r_addPadding(name, "Внешние отступы") }
            val inner_padding by nameRazd { name -> r_addPadding(name) }
            val BORDER_BRUSH_ACTIVE by nameRazd { name -> r_addBrush(name, "Граница выбранного") }
            val border_width_active by settName(addDoublePoz("Ширина граница выбранного", 2.0))
        }

        inner class ItemSchetPlanGraf(
            code_parent: String,
            code_razdel: String
        ) : RazdelSettingInner(
            "Айтем счета плана с полоской",
            code_name_razdel = "${code_parent}_${code_razdel}",
            type_razdel = TypeSaveStyleSet.FULLPANEL,
            sver = true, color = MyColorARGB("7A76A4A1")
        ) {
            val plateMain by nameRazd { name -> r_addSimplePlateWithShadow(name, "Основная панель") }
            val plateFinGoal by nameRazd { name -> r_addSimplePlateWithShadow(name, "Панель цели") }
            val platePolos by nameRazd { name -> r_addSimplePlateWithShadow(name, "Полоска") }
            val platePolosMinus by nameRazd { name -> r_addSimplePlateWithShadow(name, "Полоска минус") }
            val textName by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст названия счета",
                    MyColorARGB("9900E6E6"),
                    13.0
                )
            }
            val textSumm by nameRazd { name -> r_addTextStyle(name, "Текст суммы", MyColorARGB("FFFFF7F9"), 17.0) }
            val textValut by nameRazd { name -> r_addTextStyle(name, "Текст валюты", MyColorARGB("BFFFF7D9"), 11.0) }
            val outer_padding by nameRazd { name -> r_addPadding(name, "Внешние отступы") }
            val inner_padding by nameRazd { name -> r_addPadding(name) }
            val outer_padding_goal by nameRazd { name -> r_addPadding(name, "Внешние отступы (цель)") }
            val inner_padding_goal by nameRazd { name -> r_addPadding(name, "Внутренние отступы (цель)") }
            val BORDER_BRUSH_ACTIVE by nameRazd { name -> r_addBrush(name, "Граница выбранного") }
            val border_width_active by settName(addDoublePoz("Ширина граница выбранного", 2.0))

            val privPlGotBrush by nameRazd { name -> r_addBrush(name, "Фон выпол-й привязки плана") }
            val privGoalGotBrush by nameRazd { name -> r_addBrush(name, "Фон выпол-й привязки цели") }
            val privPlanText by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст привязки плана",
                    MyColorARGB("9900E6E6"),
                    13.0
                )
            }
            val platePrivPlan by nameRazd { name -> r_addSimplePlateWithShadow(name, "Плитка привязанного плана") }
            val privGoalText by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст привязки цели",
                    MyColorARGB("9900E6E6"),
                    13.0
                )
            }
            val platePrivGoal by nameRazd { name -> r_addSimplePlateWithShadow(name, "Плитка привязанной цели") }
        }

        inner class ItemEffectStyle(
        ) : StyleItemSetting(
            "Айтем эффективности",
            "item_effect_style",
            type_razdel = TypeSaveStyleSet.COMMONITEM,
            active = true
        ) {
            val hourText = r_addTextStyle("hour_text", "Текст часов", MyColorARGB("AFFFF7D9"), 10.0)
        }

        inner class ItemNapomStyle(
        ) : ItemStyleWithOpisSetting(
            "Айтем напоминания",
            "item_napom_style",
            type_razdel = TypeSaveStyleSet.COMMONITEM,
            active = true
        ) {
            val COLOR_BUTT_GOTOV by settName(addColor("Цвет кнопки выполнения", MyColorARGB.colorMyBorderStroke))
            val timeText = r_addTextStyle("time_text", "Текст времени", MyColorARGB("9900E6E6"), 13.0)
            val BACKGROUND_BRUSH_GOTOV = r_addBrush("BACKGROUND_GOTOV_NAPOM", "Фон выполненного")
            val BORDER_BRUSH_GOTOV = r_addBrush("BORDER_BRUSH_GOTOV", "Граница выполненного")
        }

        inner class ItemVxodStyle(
        ) : ItemStyleWithOpisSetting(
            "Айтем входящего",
            "item_vxod_style",
            type_razdel = TypeSaveStyleSet.COMMONITEM,
            active = true
        ) {
            val dateText = r_addTextStyle("date_text", "Текст даты", MyColorARGB.colorMyBorderStrokeCommon, 13.0)
            val BORDER_WIDTH_2 by settName(addDoublePoz("Ширина границы 2", 2.0))
            val BORDER_WIDTH_3 by settName(addDoublePoz("Ширина границы 3", 2.0))
            val BORDER_WIDTH_4 by settName(addDoublePoz("Ширина границы 4", 2.0))
            val BORDER_BRUSH_2 = r_addBrush("BORDER_BRUSH_2", "Граница 2")
            val BORDER_BRUSH_3 = r_addBrush("BORDER_BRUSH_3", "Граница 3")
            val BORDER_BRUSH_4 = r_addBrush("BORDER_BRUSH_4", "Граница 4")
        }

        inner class PanAddRasxodParam(
            name_podrazdel: String, code_name_item: String
        ) : RazdelSettingInner(
            "Панель добавления расхода",
            code_name_razdel = "${code_name_item}_${name_podrazdel}",
            type_razdel = TypeSaveStyleSet.FULLPANEL
        ) {
            val VIGNETTE by settName(addBoolean("Виньетирование", true))
            val platePanel by nameRazd { name -> r_addSimplePlate(name, "Панель") }
            val plateShablon by nameRazd { name -> r_addSimplePlateWithShadow(name, "Панель шаблонов") }
            val ARROW_SORT_COLOR by settName(
                addColor(
                    "Цвет стрелок сортировки шаблонов",
                    MyColorARGB.colorMyBorderStrokeCommon
                )
            )
            val itemRasxodShablon by nameRazd { name ->
                add2(
                    ItemRasxDoxOperStyle(
                        name,
                        code_name_razdel,
                        "Айтем шаблона расхода"
                    )
                )
            }
            val textName by nameRazd { name -> r_addTextField(name, "Текст названия") }
            val textSumm by nameRazd { name -> r_addTextField(name, "Текст суммы") }
            val checkBoxPlan by nameRazd { name -> r_addCheckbox(name, "Чекбокс для счета плана") }
            val shablSaveButt by nameRazd { name -> r_addIconButton(name, "Кнопка сохранения шаблонов") }
            val shablLoadButt by nameRazd { name -> r_addIconButton(name, "Кнопка загрузки шаблонов") }
            val buttAdd by nameRazd { name -> r_addTextButton(name, "Кнопка добавления") }
            val buttCancel by nameRazd { name -> r_addTextButton(name, "Кнопка отмены") }
        }

        inner class PanMessage(
            name_podrazdel: String, code_name_item: String, nameRazdel: String
        ) : RazdelSettingInner(
            nameRazdel,
            code_name_razdel = "${code_name_item}_${name_podrazdel}",
            type_razdel = TypeSaveStyleSet.FULLPANEL
        ) {
            val VIGNETTE by settName(addBoolean("Виньетирование", true))
            val platePanel by nameRazd { name -> r_addSimplePlate(name, "Панель") }
            val textName by nameRazd { name -> r_addTextStyle(name, "Текст", MyColorARGB("FFFFF7D9"), 24.0) }
            val butt by nameRazd { name -> r_addTextButton(name, "Кнопка") }
        }

        inner class PanAddDoxod(
            name_podrazdel: String, code_name_item: String
        ) : RazdelSettingInner(
            "Панель добавления дохода",
            code_name_razdel = "${code_name_item}_${name_podrazdel}",
            type_razdel = TypeSaveStyleSet.FULLPANEL
        ) {
            val VIGNETTE by settName(addBoolean("Виньетирование", true))
            val platePanel by nameRazd { name -> r_addSimplePlate(name, "Панель") }
            val plateShablon by nameRazd { name -> r_addSimplePlateWithShadow(name, "Панель шаблонов") }
            val ARROW_SORT_COLOR by settName(
                addColor(
                    "Цвет стрелок сортировки шаблонов",
                    MyColorARGB.colorMyBorderStrokeCommon
                )
            )
            val itemDoxodShablon by nameRazd { name ->
                add2(
                    ItemRasxDoxOperStyle(
                        name,
                        code_name_razdel,
                        "Айтем шаблона дохода"
                    )
                )
            }
            val textName by nameRazd { name -> r_addTextField(name, "Текст названия") }
            val textSumm by nameRazd { name -> r_addTextField(name, "Текст суммы") }
            val shablSaveButt by nameRazd { name -> r_addIconButton(name, "Кнопка сохранения шаблонов") }
            val shablLoadButt by nameRazd { name -> r_addIconButton(name, "Кнопка загрузки шаблонов") }
            val buttAdd by nameRazd { name -> r_addTextButton(name, "Кнопка добавления") }
            val buttCancel by nameRazd { name -> r_addTextButton(name, "Кнопка отмены") }
        }

        inner class PanAddPerevod(
            name_podrazdel: String, code_name_item: String
        ) : RazdelSettingInner(
            "Панель добавления перевода",
            code_name_razdel = "${code_name_item}_${name_podrazdel}",
            type_razdel = TypeSaveStyleSet.FULLPANEL
        ) {
            val VIGNETTE by settName(addBoolean("Виньетирование", true))
            val platePanel by nameRazd { name -> r_addSimplePlate(name, "Панель") }
            val textName by nameRazd { name -> r_addTextField(name, "Текст названия") }
            val textSumm by nameRazd { name -> r_addTextField(name, "Текст суммы") }
            val textSummPerevod by nameRazd { name -> r_addTextField(name, "Текст суммы перевода") }
            val buttAdd by nameRazd { name -> r_addTextButton(name, "Кнопка добавления") }
            val buttCancel by nameRazd { name -> r_addTextButton(name, "Кнопка отмены") }
            val textArrowOut by nameRazd { name -> r_addTextStyle(name, "Текст << >>", MyColorARGB("FFFFF7D9"), 24.0) }
            val textArrowIn by nameRazd { name -> r_addTextStyle(name, "Текст >> <<", MyColorARGB("FFFFF7D9"), 24.0) }
        }

        inner class PanAddPerevodPlan(
            name_podrazdel: String, code_name_item: String
        ) : RazdelSettingInner(
            "Панель добавления перевода плана",
            code_name_razdel = "${code_name_item}_${name_podrazdel}",
            type_razdel = TypeSaveStyleSet.FULLPANEL
        ) {
            val VIGNETTE by settName(addBoolean("Виньетирование", true))
            val platePanel by nameRazd { name -> r_addSimplePlate(name, "Панель") }
            val textName by nameRazd { name -> r_addTextField(name, "Текст названия") }
            val textSumm by nameRazd { name -> r_addTextField(name, "Текст суммы") }
            val buttAdd by nameRazd { name -> r_addTextButton(name, "Кнопка добавления") }
            val buttCancel by nameRazd { name -> r_addTextButton(name, "Кнопка отмены") }
            val textArrowOut by nameRazd { name -> r_addTextStyle(name, "Текст << >>", MyColorARGB("FFFFF7D9"), 24.0) }
            val textArrowIn by nameRazd { name -> r_addTextStyle(name, "Текст >> <<", MyColorARGB("FFFFF7D9"), 24.0) }
        }

        inner class FinRasxodParam(
            name_podrazdel: String, code_name_item: String
        ) : RazdelSettingInner(
            "Расходы",
            code_name_razdel = "${code_name_item}_${name_podrazdel}",
            type_razdel = TypeSaveStyleSet.FULLPANEL
        ) {
            val textTire by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст тире в диаграмме",
                    MyColorARGB("FFFFF7D9"),
                    24.0
                )
            }
            val textRasxDiag by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст в диаграмме",
                    MyColorARGB("FFFFF7D9"),
                    24.0
                )
            }
            val rectDiagColor by nameRazd { name -> r_addRectDiagramColor(name, "Цвета диаграммы по типу") }

            val textRezSumm by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст суммы итого",
                    MyColorARGB("FFFFF7D9"),
                    24.0
                )
            }
            val itemRasxod by nameRazd { name -> add2(ItemRasxDoxOperStyle(name, code_name_razdel, "Айтем расхода")) }
            val panAddRasxod by nameRazd { name -> add2(PanAddRasxodParam(name, code_name_razdel)) }
            val seekBarTypeGrafStyle by nameRazd { name -> r_addSeekBar(name, "Кнопки выбора графика") }
            val cb_typeRasx by nameRazd { name -> r_addComboBox(name, "Список типа расходов") }
        }

        inner class FinDoxodParam(
            name_podrazdel: String, code_name_item: String
        ) : RazdelSettingInner(
            "Доходы",
            code_name_razdel = "${code_name_item}_${name_podrazdel}",
            type_razdel = TypeSaveStyleSet.FULLPANEL
        ) {
            val textTire by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст тире в диаграмме",
                    MyColorARGB("FFFFF7D9"),
                    24.0
                )
            }
            val textDoxDiag by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст в диаграмме",
                    MyColorARGB("FFFFF7D9"),
                    24.0
                )
            }
            val twoRectDiagColor by nameRazd { name -> r_addTwoRectDiagramColor(name, "Цвета диаграммы расх./дох.") }
            val GrafColor by nameRazd { name -> r_addGrafikColor(name, "Цвета графика капитала") }

            val textRezSumm by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст суммы итого",
                    MyColorARGB("FFFFF7D9"),
                    24.0
                )
            }

            val itemDoxod by nameRazd { name -> add2(ItemRasxDoxOperStyle(name, code_name_razdel, "Айтем дохода")) }
            val panAddDoxod by nameRazd { name -> add2(PanAddDoxod(name, code_name_razdel)) }
            val seekBarTypeGrafStyle by nameRazd { name -> r_addSeekBar(name, "Кнопки выбора графика") }
            val cb_typeDox by nameRazd { name -> r_addComboBox(name, "Список типа доходов") }
        }

        inner class FinSchetParam(
            name_podrazdel: String, code_name_item: String
        ) : RazdelSettingInner(
            "Счета",
            code_name_razdel = "${code_name_item}_${name_podrazdel}",
            type_razdel = TypeSaveStyleSet.FULLPANEL
        ) {
            val textRezSumm by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст суммы итого",
                    MyColorARGB("FFFFF7D9"),
                    24.0
                )
            }
            val textStat by nameRazd { name -> r_addTextStyle(name, "Текст статистики", MyColorARGB("FFFFF7D9"), 17.0) }
            val COLOR_STAT_RASX by settName(addColor("Цвет текста расходов", MyColorARGB.colorRasxodItem))
            val COLOR_STAT_DOX by settName(addColor("Цвет текста доходов", MyColorARGB.colorDoxodItem))
            val COLOR_STAT_PER by settName(addColor("Цвет текста переводов", MyColorARGB.colorSchetItem))
            val COLOR_STAT_POPRAV by settName(addColor("Цвет текста поправки", MyColorARGB.colorMyMainTheme))

            val privSchetPlanInfoForSchetGraf by nameRazd { name -> add2(PrivSchetPlanInfo(name)) }
            val itemPerevod by nameRazd { name -> add2(ItemRasxDoxOperStyle(name, code_name_razdel, "Айтем перевода")) }
            val itemPopravka by nameRazd { name ->
                add2(
                    ItemRasxDoxOperStyle(
                        name,
                        code_name_razdel,
                        "Айтем поправки на курс"
                    )
                )
            }
            val panAddPerevod by nameRazd { name -> add2(PanAddPerevod(name, code_name_razdel)) }
            val panAddPerevodPlan by nameRazd { name -> add2(PanAddPerevodPlan(name, code_name_razdel)) }
            val itemSchetGraf by nameRazd { name -> add2(ItemSchetGraf(name, code_name_razdel)) }
            val itemSchetPlanGraf by nameRazd { name -> add2(ItemSchetPlanGraf(name, code_name_razdel)) }


            val cb_spisSchet by nameRazd { name -> r_addComboBox(name, "Список счетов") }
            val cb_spisSchetPlan by nameRazd { name -> r_addComboBox(name, "Список счетов планов") }
            val buttAddInnerRasxod by nameRazd { name -> r_addTextButton(name, "Кнопка доб. расхода") }
            val buttAddInnerDoxod by nameRazd { name -> r_addTextButton(name, "Кнопка доб. дохода") }
            val cb_typeFilter by nameRazd { name -> r_addComboBox(name, "Список типов операции") }
            val panelStat by nameRazd { name -> r_addSimplePlateWithShadow(name, "Плитка статистики") }
            val panelStatInnerPadding by nameRazd { name -> r_addPadding(name, "Отступы статистики") }
        }

        inner class FinParam(
            name_podrazdel: String, code_name_item: String
        ) : RazdelSettingInner(
            "Финансы",
            code_name_razdel = "${code_name_item}_${name_podrazdel}",
            type_razdel = TypeSaveStyleSet.FULLPANEL
        ) {

            val colorButtFullScreenGraf by settName(
                addColor(
                    "Цвет кнопки разворач-я графиков",
                    MyColorARGB("FFFFFFFF")
                )
            )

            val buttAddRasxod by nameRazd { name -> r_addTextButton(name, "Кнопка доб. расхода") }
            val buttAddDoxod by nameRazd { name -> r_addTextButton(name, "Кнопка доб. дохода") }
            val buttAddPerevod by nameRazd { name -> r_addTextButton(name, "Кнопка доб. перевода") }
            val buttAddPerevodPlan by nameRazd { name -> r_addTextButton(name, "Кнопка доб. перевода плана") }

            val seekBarStyle by nameRazd { name -> r_addSeekBar(name, "Кнопки выбора панелей") }
            val seekBarPeriodStyle by nameRazd { name -> r_addSeekBar(name, "Кнопки выбора периода") }
            val buttFilter by nameRazd { name -> r_addToggleButton(name, "Кнопка фильтра") }
            val buttNextDate by nameRazd { name -> r_addTextButton(name, "Кнопка листания даты") }
            val buttNextPeriod by nameRazd { name -> r_addTextButton(name, "Кнопка листания периодов") }
            val buttDate by nameRazd { name -> r_addTextButton(name, "Кнопка даты") }

            val rasxodParam by nameRazd { name -> add2(FinRasxodParam(name, code_name_razdel)) }
            val doxodParam by nameRazd { name -> add2(FinDoxodParam(name, code_name_razdel)) }
            val schetParam by nameRazd { name -> add2(FinSchetParam(name, code_name_razdel)) }
        }

        inner class JournalParam(
        ) : RazdelSettingInner("Журнал", code_name_razdel = "JOURNAL_PANEL", type_razdel = TypeSaveStyleSet.FULLPANEL) {
            val itemBloknot = add2(ItemBloknotStyle())
            val complexOpisForBloknot by nameRazd { name ->
                add2(
                    ComplexOpisStyleSetting(
                        "Сложное описание для блокнотов",
                        code_name_razdel,
                        name
                    )
                )
            }
            val itemIdea = add2(ItemIdeaStyle())
            val complexOpisForIdea by nameRazd { name ->
                add2(
                    ComplexOpisStyleSetting(
                        "Сложное описание для разделов",
                        code_name_razdel,
                        name
                    )
                )
            }
            val itemIdeaStap = add2(ItemIdeaStapStyle())
            val complexOpisForIdeaStap by nameRazd { name ->
                add2(
                    ComplexOpisStyleSetting(
                        "Сложное описание для заметок",
                        code_name_razdel,
                        name
                    )
                )
            }
            val idea_stap_plate by nameRazd { name -> r_addSimplePlateWithShadow(name, "Плитка заметок") }
            val addBlokButt by nameRazd { name -> r_addTextButton(name, "Кнопка доб. блокнота") }
            val addIdeaButt by nameRazd { name -> r_addTextButton(name, "Кнопка доб. раздела") }
            val addIdeaStapButt by nameRazd { name -> r_addTextButton(name, "Кнопка доб. заметки") }
            val nextIdeaStapButt by nameRazd { name -> r_addTextButton(name, "Кнопка листания заметок") }
            val sortIdeaButt by nameRazd { name -> r_addTextButton(name, "Кнопка сортировки разделов") }
            val sortIdeaStapButt by nameRazd { name -> r_addTextButton(name, "Кнопка сортировки заметок") }
            val findIdeaStapButt by nameRazd { name -> r_addToggleButton(name, "Кнопка поиска заметок") }
        }

        inner class PrivSchetPlanInfo(
            code_name_razd: String
        ) : RazdelSettingInner(
            "Информация о привязанном счете",
            code_name_razdel = code_name_razd,
            type_razdel = TypeSaveStyleSet.FULLPANEL,
            sver = true
        ) {
            val textStyle by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст сумм",
                    MyColorARGB.colorMyBorderStrokeCommon,
                    12.0
                )
            }
            val textStyleInBox by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст в полоске",
                    MyColorARGB.colorMyBorderStroke,
                    12.0
                )
            }
            val textStyleInBox2 by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст в полоске(2)",
                    MyColorARGB.colorMyBorderStroke,
                    12.0
                )
            }
            val COLOR_T_GOAL by settName(addColor("Цвет текста цели", MyColorARGB(255, 127, 255, 127)))
            val COLOR_T_OSTAT by settName(addColor("Цвет текста остатка", MyColorARGB(255, 196, 255, 196)))
            val COLOR_T_PERER by settName(addColor("Цвет текста остатка/перерасхода", MyColorARGB(255, 255, 196, 196)))
            val COLOR_T_IZ by settName(addColor("Цвет текста из", MyColorARGB(255, 255, 255, 127)))
            val shapeBox by nameRazd { name -> r_addShape(name, "Форма") }
            val shadow by nameRazd { name -> r_addShadowStyle(name, "Стиль тени") }

            val COLOR_B_MAX_BACK by settName(addColor("Полоска фон max", MyColorARGB("FF444444")))
            val COLOR_B_MIN_BACK by settName(addColor("Полоска фон min", MyColorARGB("FF888888")))
            val COLOR_B_RASXOD by settName(addColor("Полоска расход", MyColorARGB("7FFFFF00")))
            val COLOR_B_RASXOD2 by settName(addColor("Полоска расход(-)", MyColorARGB("7FFF0000")))
            val COLOR_B_DOXOD by settName(addColor("Полоска доход", MyColorARGB("7F00FF00")))

            val BORDER_WIDTH by settName(addDoublePoz("Ширина границы", 2.0))
            val BORDER_BRUSH = r_addBrush("BORDER_BRUSH", "Граница")
        }

        inner class TodayPlateParam(
        ) : RazdelSettingInner(
            "Сегодня",
            code_name_razdel = "TIME_TODAY",
            type_razdel = TypeSaveStyleSet.NOTSAVE,
            sver = true
        ) {
            val COLOR_TODAY by settName(addColor("Цвет сегодня", MyColorARGB.colorEffektShkal_Month))
            val COLOR_TOMORROW by settName(addColor("Цвет завтра", MyColorARGB(255, 0, 166, 214)))
            val COLOR_YESTERDAY by settName(addColor("Цвет вчера", MyColorARGB(255, 255, 165, 0)))
            val COLOR_TOMORROW_END by settName(addColor("Цвет завтра 2", MyColorARGB(255, 0, 166, 214).plusDark(0.3f)))
            val COLOR_YESTERDAY_END by settName(addColor("Цвет вчера 2", MyColorARGB(255, 255, 165, 0).plusDark(0.3f)))
            val textStyle = r_addTextStyle("text_style", "Текст времени", MyColorARGB("9900E6E6"), 13.0)
            val shadow by nameRazd { name -> r_addShadowStyle(name, "Стиль тени") }
            val BORDER_WIDTH by settName(addDoublePoz("Ширина границы", 2.0))
            val shape = r_addShape("corner", "Форма")
            val inner_padding = r_addPadding("inner_padding")
            val BORDER_BRUSH = r_addBrush("BORDER_BRUSH", "Граница")
        }

        inner class VxodTabParam(
            code_name_razdel: String = "TIME_PANEL"
        ) : RazdelSettingInner(
            "Входящие",
            code_name_razdel = code_name_razdel,
            type_razdel = TypeSaveStyleSet.NOTSAVE,
            sver = true
        ) {
            val buttAddVxod = r_addTextButton("buttAddVxod", "Кнопка доб. входящих")
            val textCountVxod = r_addTextStyle(
                "textCountVxod",
                "Текст количества входящих",
                MyColorARGB.colorMyBorderStrokeCommon,
                20.0
            )

            val itemVxod = add2(ItemVxodStyle())
            val complexOpisForVxod by nameRazd { name ->
                add2(
                    ComplexOpisStyleSetting(
                        "Сложное описание для входящих",
                        code_name_razdel,
                        name
                    )
                )
            }
        }

        inner class CalendarPanel(
            name_podrazdel: String, code_name_parent: String, nameInMenu: String
        ) : RazdelSettingInner(
            nameInMenu,
            code_name_razdel = "${code_name_parent}_${name_podrazdel}",
            type_razdel = TypeSaveStyleSet.FULLPANEL
        ) {
            val textDate by nameRazd { name -> r_addTextStyle(name, "Текст даты") }
            val padding_date by nameRazd { name -> r_addPadding(name, "Отступы даты") }
            val itemCalendarNapom by nameRazd { name ->
                add2(
                    ItemCalendarNapom(
                        name,
                        code_name_razdel,
                        "Айтем напоминания"
                    )
                )
            }
            val itemCalendarDenPlan by nameRazd { name ->
                add2(
                    ItemCalendarDenPlan(
                        name,
                        code_name_razdel,
                        "Айтем плана на день"
                    )
                )
            }
        }

        open inner class ItemBasePlate(
            name_podrazdel: String, code_name_parent: String, nameInMenu: String
        ) : RazdelSettingInner(
            nameInMenu,
            code_name_razdel = "${code_name_parent}_${name_podrazdel}",
            type_razdel = TypeSaveStyleSet.FULLPANEL
        ) {
            val outer_padding by nameRazd { name -> r_addPadding(name, "Внешние отступы") }
            val inner_padding by nameRazd { name -> r_addPadding(name) }
            val plateItem by nameRazd { name -> r_addSimplePlateWithShadow(name, "Плитка айтема") }
            val mainText by nameRazd { name -> r_addTextStyle(name, "Стиль заголовка") }
        }

        inner class ItemCalendarNapom(
            name_podrazdel: String, code_name_parent: String, nameInMenu: String
        ) : ItemBasePlate(name_podrazdel, code_name_parent, nameInMenu) {
            val BACKGROUND_BRUSH_GOTOV by nameRazd { name -> r_addBrush(name, "Фон выполненного") }
            val BORDER_BRUSH_GOTOV by nameRazd { name -> r_addBrush(name, "Граница выполненного") }
            val BORDER_BRUSH_SELECT by nameRazd { name -> r_addBrush(name, "Граница выделенного") }
        }

        inner class ItemCalendarDenPlan(
            name_podrazdel: String, code_name_parent: String, nameInMenu: String
        ) : ItemBasePlate(name_podrazdel, code_name_parent, nameInMenu) {
            val COLOR_INDIK_BACK by settName(addColor("Фон индикатора", MyColorARGB("FF444444")))
            val COLOR_INDIK_COMPLETE by settName(addColor("Заполнение индикатора", MyColorARGB.colorEffektShkal_Month))
            val COLOR_INDIK_BORDER by settName(addColor("Граница индикатора", MyColorARGB.colorMyBorderStroke.copy(51)))
            val BORDER_BRUSH_SELECT by nameRazd { name -> r_addBrush(name, "Граница выделенного") }
            val textHour by nameRazd { name -> r_addTextStyle(name, "Текст часов") }
        }

        open inner class PanSelectShablon(
            name_podrazdel: String, code_name_item: String, nameInMenu: String
        ) : PanAddBlank(name_podrazdel, code_name_item, nameInMenu) {
            val ARROW_SORT_COLOR_next_stap by settName(
                addColor(
                    "Цвет стрелок сортировок для этапов",
                    MyColorARGB.colorMyBorderStrokeCommon
                )
            )
            val ARROW_SORT_COLOR_next_denplan by settName(
                addColor(
                    "Цвет стрелок сортировок для планов",
                    MyColorARGB.colorMyBorderStrokeCommon
                )
            )
            val BORDER_WIDTH_next_stap by settName(addDoublePoz("Ширина границы для этапов", 1.0))
            val BORDER_WIDTH_next_denplan by settName(addDoublePoz("Ширина границы для планов", 1.0))
            val buttSort by nameRazd { name -> r_addToggleButton(name, "Кнопка сортировки") }
            val buttCancel by nameRazd { name -> r_addTextButton(name, "Кнопка отмены") }
            val buttLoadShablon by nameRazd { name -> r_addTextButton(name, "Кнопка загрузки шаблона или след. д.") }
            val plateShablon by nameRazd { name -> r_addSimplePlateWithShadow(name, "Плитка шаблонов") }
            val plateNextAction by nameRazd { name -> r_addSimplePlateWithShadow(name, "Плитка следующих действий") }
            val textTitleShablon by nameRazd { name -> r_addTextStyle(name, "Текст заголовка шаблонов") }
            val textTitleNextAction by nameRazd { name -> r_addTextStyle(name, "Текст заголовка сдедующих действий") }


            val checkRepeat by nameRazd { name -> r_addCheckbox(name, "Чекбокс \"повторы\"") }
            val checkTime by nameRazd { name -> r_addCheckbox(name, "Чекбокс \"время\"") }
            val checkNameFromStap by nameRazd { name -> r_addCheckbox(name, "Чекбокс \"название из плана\"") }
            val checkOpis by nameRazd { name -> r_addCheckbox(name, "Чекбокс \"описание из этапа\"") }
            val buttAddNextAction by nameRazd { name -> r_addTextButton(name, "Кнопка добавления следующего действия") }
            val cornerDenPlan by nameRazd { name -> r_addShape(name, "Форма айтема плана") }
            val cornerStap by nameRazd { name -> r_addShape(name, "Форма айтема этапа") }
            val paddingInnerDenPlan by nameRazd { name -> r_addPadding(name, "Внутренние отступы айтема плана") }
            val paddingOuterDenPlan by nameRazd { name -> r_addPadding(name, "Внешние отступы айтема плана") }
            val paddingInnerStap by nameRazd { name -> r_addPadding(name, "Внутренние отступы айтема этапа") }
            val paddingOuterStap by nameRazd { name -> r_addPadding(name, "Внешние отступы айтема этапа") }
            val cornerOpisDenPlan by nameRazd { name -> r_addShape(name, "Форма описания айтема плана") }
            val cornerOpisStap by nameRazd { name -> r_addShape(name, "Форма описания айтема этапа") }
            val paddingOpisInnerDenPlan by nameRazd { name ->
                r_addPadding(
                    name,
                    "Внутренние отступы описания айтема плана"
                )
            }
            val paddingOpisOuterDenPlan by nameRazd { name ->
                r_addPadding(
                    name,
                    "Внешние отступы описания айтема плана"
                )
            }
            val paddingOpisInnerStap by nameRazd { name ->
                r_addPadding(
                    name,
                    "Внутренние отступы описания айтема этапа"
                )
            }
            val paddingOpisOuterStap by nameRazd { name -> r_addPadding(name, "Внешние отступы описания айтема этапа") }
        }

        inner class DenPlanTabParam(
            code_name_razdel: String = "TIME_PANEL"
        ) : RazdelSettingInner(
            "Ежедневник",
            code_name_razdel = code_name_razdel,
            type_razdel = TypeSaveStyleSet.NOTSAVE,
            sver = true
        ) {
            val buttBestDays = r_addToggleButton("buttBestDays", "Кнопка дня")
            val buttAddNapom = r_addTextButton("buttAddNapom", "Кнопка добавления напоминания")
            val buttAddDenPlan = r_addTextButton("buttAddDenPlan", "Кнопка добавления плана на день")
            val buttIconCalendar by nameRazd { name -> r_addIconButton(name, "Кнопка календаря") }
            val buttIconShablon by nameRazd { name -> r_addIconButton(name, "Кнопка шаблонов") }
            val buttDate = r_addTextButton("buttDate", "Кнопка даты")
            val buttDateArrow = r_addTextButton("buttDateArrow", "Кнопки листания даты")
            val buttSverDenPlan = r_addTextButton("buttSverDenPlan", "Кнопка сворачивания/разворачивания")
            val today = add2(TodayPlateParam())
            val itemNapom = add2(ItemNapomStyle())
            val complexOpisForNapom by nameRazd { name ->
                add2(
                    ComplexOpisStyleSetting(
                        "Сложное описание для напоминаний",
                        code_name_razdel,
                        name
                    )
                )
            }
            val itemDenPlan = add2(ItemDenPlanStyle())
            val complexOpisForDenPlan by nameRazd { name ->
                add2(
                    ComplexOpisStyleSetting(
                        "Сложное описание для планов на день",
                        code_name_razdel,
                        name
                    )
                )
            }
            val calendarPanel by nameRazd { name -> add2(CalendarPanel(name, code_name_razdel, "Календарь")) }
            val panSelectShablon by nameRazd { name ->
                add2(
                    PanSelectShablon(
                        name,
                        code_name_razdel,
                        "Панель выбора шаблона"
                    )
                )
            }
            val timeSelector by nameRazd { name ->
                add2(
                    TimeSelectorStyle(
                        "Стиль выбора времени",
                        code_name_razdel,
                        name,
                        type_razdel = TypeSaveStyleSet.FULLPANEL
                    )
                )
            }
        }

        inner class TimeSelectorStyle(
            nameItem: String,
            code_name_parent_razdel: String,
            name_podrazdel: String,
            color: MyColorARGB = MyColorARGB("FFBE8367"),
            type_razdel: TypeSaveStyleSet,
            sver: Boolean = true,
        ) : RazdelSettingInner(nameItem, color, "${code_name_parent_razdel}_${name_podrazdel}", type_razdel, sver) {
            val COLOR_BACK_NUMBER by settName(addColor("Фон цифр", MyColorARGB.colorBackGr1))
            val COLOR_NUMBER by settName(addColor("Цвет цифр", MyColorARGB.colorBackGr1))
            val COLOR_NUMBER_BORDER by settName(addColor("Цвет обводки цифр", MyColorARGB.colorBackGr1))
            val COLOR_TIME_PERIOD_1 by settName(addColor("Период времени 0-4", MyColorARGB.colorMyBorderStroke))
            val COLOR_TIME_PERIOD_2 by settName(addColor("Период времени 5-8", MyColorARGB.colorMyBorderStroke))
            val COLOR_TIME_PERIOD_3 by settName(addColor("Период времени 9-12", MyColorARGB.colorMyBorderStroke))
            val COLOR_TIME_PERIOD_4 by settName(addColor("Период времени 13-16", MyColorARGB.colorMyBorderStroke))
            val COLOR_TIME_PERIOD_5 by settName(addColor("Период времени 17-20", MyColorARGB.colorMyBorderStroke))
            val COLOR_TIME_PERIOD_6 by settName(addColor("Период времени 21-24", MyColorARGB.colorMyBorderStroke))
        }

        inner class TimelineDiagramColors(
            nameItem: String,
            code_name_parent_razdel: String,
            name_podrazdel: String,
            color: MyColorARGB = MyColorARGB("FFBE8367"),
            type_razdel: TypeSaveStyleSet,
            sver: Boolean = true,
        ) : RazdelSettingInner(nameItem, color, "${code_name_parent_razdel}_${name_podrazdel}", type_razdel, sver) {
            val COLOR_RAMK by settName(addColor("Рамка", MyColorARGB.colorMyBorderStroke))
            val COLOR_SHKALA by settName(addColor("Шкала", MyColorARGB("FFFFFFFF")))
            val COLOR_BETWEEN_DAYS by settName(addColor("Между днями", MyColorARGB("FF000000")))
            val COLOR_YEAR by settName(addColor("Год", MyColorARGB.colorMyBorderStroke))
            val COLOR_MONTHS_DAYS by settName(addColor("Дни/Месяцы", MyColorARGB("FFFFFFFF")))
            val COLOR_BACKGROUND by settName(addColor("Фон", MyColorARGB.colorMyBorderStroke.copy(A = 50)))
            val COLOR_BACKGROUND_COMPLETE by settName(addColor("Фон (завершенного)", MyColorARGB("FFFFFF00")))
            val COLOR_BACKGROUND_CLOSE by settName(addColor("Фон (закрытого)", MyColorARGB("FF888888")))
            val COLOR_BACKGROUND_NEAR by settName(addColor("Фон (подходящего)", MyColorARGB("FF0000FF").copy(A = 50)))
            val COLOR_BACKGROUND_END by settName(addColor("Фон (закончившегося)", MyColorARGB("FFFF0000").copy(A = 50)))
            val COLOR_VIGNETTE by settName(addColor("Виньетирование", MyColorARGB("E6000000")))
            val COLOR_WEEKEND by settName(addColor("Выходные", MyColorARGB("56FF0000")))
            val COLOR_CURRENT by settName(addColor("Сегодня", MyColorARGB("FF00FFFF")))
            val COLOR_RANGE_PAST by settName(addColor("Срок (прошедший)", MyColorARGB("FFFF0000")))
            val COLOR_RANGE_PRESENT by settName(addColor("Срок (текущий)", MyColorARGB("FF00FF00")))
            val COLOR_RANGE_FUTURE by settName(addColor("Срок (будущий)", MyColorARGB("FF0000FF")))
            val COLOR_POINT_WORK by settName(addColor("Отметка даты работы над проектом", MyColorARGB("FFFFFF00")))
        }

        inner class BoxSelectParentPlanParam(
            code_name_razdel: String = "BoxSelectParentPlan_COMMON"
        ) : RazdelSettingInner(
            "Дизайн выбора проектов/этапов для привязки",
            code_name_razdel = code_name_razdel,
            type_razdel = TypeSaveStyleSet.FULLPANEL,
            sver = true, color = MyColorARGB("9A86C4B1")
        ) {
            val mainPlate by nameRazd { name -> r_addSimplePlateWithShadow(name, "Общая плитка") }
            val buttGetPlan by nameRazd { name -> r_addTextButton(name, "Кнопка выбрать проект") }
            val buttGetStap by nameRazd { name -> r_addTextButton(name, "Кнопка выбрать этап") }
            val buttBack by nameRazd { name -> r_addTextButton(name, "Кнопка назад") }
            val buttSelect by nameRazd { name -> r_addTextButton(name, "Кнопка выбрать") }
            val buttUnselect by nameRazd { name -> r_addTextButton(name, "Кнопка отменить выбор") }
            val cornerPlan by nameRazd { name -> r_addShape(name, "Форма айтема проекта") }
            val cornerStap by nameRazd { name -> r_addShape(name, "Форма айтема этапа") }
            val textParentPlan by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст родительского плана для этапа",
                    MyColorARGB.colorMyBorderStroke,
                    20.0
                )
            }
            val textLabelOn by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст названия (наведеенный)",
                    MyColorARGB.colorMyBorderStroke,
                    10.0
                )
            }
            val textLabelOff by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст названия",
                    MyColorARGB.colorMyBorderStrokeCommon,
                    10.0
                )
            }
            val textLabelPadding by settName(addDoublePoz("Отступ названия", 10.0))
        }

        inner class TimelineCommonParam(
            code_name_razdel: String = "TIMELINE_COMMON"
        ) : RazdelSettingInner(
            "Дизайн панели таймлайна",
            code_name_razdel = code_name_razdel,
            type_razdel = TypeSaveStyleSet.FULLPANEL,
            sver = true, color = MyColorARGB("7AC6A4A1")
        ) {
            val timelineSeekBarStyle by nameRazd { name -> r_addSeekBar(name, "Кнопки выбора панелей") }
            val timelineColors by nameRazd { name ->
                add2(
                    TimelineDiagramColors(
                        "Цвета диаграммы",
                        code_name_razdel,
                        name,
                        type_razdel = TypeSaveStyleSet.FULLPANEL
                    )
                )
            }
            val buttYear by nameRazd { name -> r_addToggleButton(name, "Кнопка диапазона") }
            val plateSrokPanel by nameRazd { name -> r_addSimplePlate(name, "Панель") }
            val outer_padding by nameRazd { name -> r_addPadding(name, "Внешние отступы айтемов") }
            val inner_padding by nameRazd { name -> r_addPadding(name, "Внутренние отступы айтемов") }
            val cornerPlan by nameRazd { name -> r_addShape(name, "Форма айтема проекта") }
            val cornerStap by nameRazd { name -> r_addShape(name, "Форма айтема этапа") }
            val buttDate by nameRazd { name -> r_addTextButton(name, "Кнопка даты") }
            val buttArrow1 by nameRazd { name -> r_addTextButton(name, "Кнопка >") }
            val buttArrow2 by nameRazd { name -> r_addTextButton(name, "Кнопка >>") }
            val buttArrow3 by nameRazd { name -> r_addTextButton(name, "Кнопка >>>") }
            val buttHide by nameRazd { name -> r_addTextButton(name, "Кнопка Скрыть") }
            val buttTimeline by nameRazd { name -> r_addTextButton(name, "Кнопка Таймлайн") }
            val backgroundButtTimeline by nameRazd { name -> r_addBrush(name, "Цвет кнопки Таймлайн (внимание)") }
            val borderButtTimeline by nameRazd { name -> r_addBrush(name, "Цвет границы кнопки Таймлайн (внимание)") }
            val textTimelineInfo by nameRazd { name -> r_addTextStyle(name, "Текст инфо таймлайна") }
            val color_info_present by settName(addColor("Цвет оповещений (активные)", MyColorARGB("FF0000FF")))
            val color_info_future by settName(addColor("Цвет оповещений (будущие)", MyColorARGB("FF0000FF")))
            val color_info_past by settName(addColor("Цвет оповещений (прошедшие)", MyColorARGB("FF0000FF")))
        }

        open inner class PanAddBlank(
            name_podrazdel: String, code_name_item: String, nameInMenu: String
        ) : RazdelSettingInner(
            nameInMenu,
            code_name_razdel = "${code_name_item}_${name_podrazdel}",
            type_razdel = TypeSaveStyleSet.FULLPANEL
        ) {
            val VIGNETTE by settName(addBoolean("Виньетирование", true))
            val platePanel by nameRazd { name -> r_addSimplePlate(name, "Панель") }
        }

        open inner class PanAddCommon(
            name_podrazdel: String, code_name_item: String, nameInMenu: String
        ) : PanAddBlank(name_podrazdel, code_name_item, nameInMenu) {
            val textName by nameRazd { name -> r_addTextField(name, "Текст названия") }
            val buttAdd by nameRazd { name -> r_addTextButton(name, "Кнопка добавления") }
            val buttCancel by nameRazd { name -> r_addTextButton(name, "Кнопка отмены") }
        }

        open inner class PanAddCommonWithMarker(
            name_podrazdel: String, code_name_item: String, nameInMenu: String
        ) : PanAddCommon(name_podrazdel, code_name_item, nameInMenu) {
            val dropdownMenuStyle by nameRazd { name -> r_addDropDownMenu(name, "Выпадающее меню маркера") }
        }

        open inner class PanAddPlanStap(
            name_podrazdel: String, code_name_item: String, nameInMenu: String = "Панель добавления этапов"
        ) : PanAddCommonWithMarker(name_podrazdel, code_name_item, nameInMenu) {
            val buttGotov by nameRazd { name -> r_addToggleButton(name, "Кнопка готовности") }
            val buttSrok by nameRazd { name -> r_addToggleButton(name, "Кнопка сроков") }
            val buttDateArrowStart by nameRazd { name -> r_addTextButton(name, "Кнопка листания даты (начало)") }
            val buttDateStart by nameRazd { name -> r_addTextButton(name, "Кнопка даты (начало)") }
            val buttArrowDateEnd by nameRazd { name -> r_addTextButton(name, "Кнопка листания даты (конец)") }
            val buttDateEnd by nameRazd { name -> r_addTextButton(name, "Кнопка даты (конец)") }
        }

        open inner class PanAddPlan(
            name_podrazdel: String, code_name_item: String, nameInMenu: String = "Панель добавления проектов"
        ) : PanAddPlanStap(name_podrazdel, code_name_item, nameInMenu) {
            val buttNaprav by nameRazd { name -> r_addToggleButton(name, "Кнопка направления") }
        }

        open inner class PanHistory(
            name_podrazdel: String, code_name_item: String, nameInMenu: String
        ) : PanAddBlank(name_podrazdel, code_name_item, nameInMenu) {
            val textNameElem by nameRazd { name -> r_addTextStyle(name, "Текст заголовка") }
            val seekBarHistory by nameRazd { name -> r_addSeekBar(name, "Кнопки выбора панелей") }
            val checkBoxYear by nameRazd { name -> r_addCheckbox(name, "Чекбокс \"по годам\"") }
            val cb_years by nameRazd { name -> r_addComboBox(name, "Список годов") }
            val buttHide by nameRazd { name -> r_addTextButton(name, "Кнопка добавления") }
            val itemHistory by nameRazd { name -> add2(ItemHistoryPlan(name, code_name_razdel, "Айтем истории")) }
            val rectDiagColor by nameRazd { name -> r_addRectDiagramColor(name, "Цвета диаграммы") }
        }

        open inner class ColorShkal(
            name_podrazdel: String, code_name_item: String, nameInMenu: String
        ) : RazdelSettingInner(
            nameInMenu,
            code_name_razdel = "${code_name_item}_${name_podrazdel}",
            type_razdel = TypeSaveStyleSet.FULLPANEL
        ) {
            val colorRamk by settName(addColor("Рамка шкалы", MyColorARGB("FF000000")))
            val colorBack by settName(addColor("Фон шкалы", MyColorARGB("6F000000")))
            val colorTime by settName(addColor("Задействованное время", MyColorARGB.colorStatTint_01))
            val colorTommorow by settName(addColor("Время от завтрашнего дня", MyColorARGB.colorStatTint_02))
        }

        open inner class ItemHistoryPlan(
            name_podrazdel: String, code_name_item: String, nameInMenu: String
        ) : RazdelSettingInner(
            nameInMenu,
            code_name_razdel = "${code_name_item}_${name_podrazdel}",
            type_razdel = TypeSaveStyleSet.FULLPANEL
        ) {
            val colorRazdelit by settName(addColor("Разделитель", MyColorARGB.colorMyBorderStroke))
            val colorShkal by nameRazd { name -> add2(ColorShkal(name, code_name_razdel, "Цвета шкалы времени ")) }
            val textMonth by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст месяца",
                    MyColorARGB.colorStatTint_02,
                    25.0
                )
            }
            val textDate by nameRazd { name -> r_addTextStyle(name, "Текст даты", MyColorARGB("FF00FFFF"), 13.0) }
            val textPlanName by nameRazd { name -> r_addTextStyle(name, "Текст имени проекта") }
            val textStapName by nameRazd { name -> r_addTextStyle(name, "Текст имени этапа") }
            val textHour by nameRazd { name -> r_addTextStyle(name, "Текст часов", MyColorARGB("FFFFF7F9")) }
            val plateStapName by nameRazd { name -> r_addSimplePlateWithShadow(name, "Плитка названия этапа") }
        }

        inner class PlanTabParam(
            code_name_razdel: String = "TIME_PANEL"
        ) : RazdelSettingInner(
            "Проекты",
            code_name_razdel = code_name_razdel,
            type_razdel = TypeSaveStyleSet.FULLPANEL,
            sver = true, color = MyColorARGB("7AC6A4A1")
        ) {
            val buttAddPlan = r_addTextButton("buttAddPlan", "Кнопка доб. проекта")
            val buttAddPlanStap = r_addTextButton("buttAddPlanStap", "Кнопка доб. этапа проекта")
            val buttVypPlan by nameRazd { name -> r_addToggleButton(name, "Кнопка вып. проекта") }
            val buttVypPlanStap by nameRazd { name -> r_addToggleButton(name, "Кнопка вып. этапов проекта") }
            val buttSortPlan by nameRazd { name -> r_addToggleButton(name, "Кнопка сорт-ки проекта") }
            val buttSortPlanStap by nameRazd { name -> r_addToggleButton(name, "Кнопка сорт-ки этапов проекта") }
            val buttTextToggleTMP by nameRazd { name -> addToggleTextButton(name, "Временная общая") }
            val textCountPlan = r_addTextStyle(
                "textCountPlan",
                "Текст количества проектов",
                MyColorARGB.colorMyBorderStrokeCommon,
                20.0
            )
            val textCountPlanStap = r_addTextStyle(
                "textCountPlanStap",
                "Текст количества этапов проекта",
                MyColorARGB.colorMyBorderStrokeCommon,
                20.0
            )
            val panelPlanStap by nameRazd { name -> r_addSimplePlateWithShadow(name, "Панель этапов проекта") }
            val itemPlan = add2(ItemPlanStyle())
            val itemPlanStap = add2(ItemPlanStapStyle())

            val panAddPlan by nameRazd { name -> add2(PanAddPlan(name, code_name_razdel)) }
            val complexOpisForPlan by nameRazd { name ->
                add2(
                    ComplexOpisStyleSetting(
                        "Сложное описание для проектов",
                        code_name_razdel,
                        name
                    )
                )
            }
            val panAddPlanStap by nameRazd { name -> add2(PanAddPlanStap(name, code_name_razdel)) }
            val complexOpisForPlanStap by nameRazd { name ->
                add2(
                    ComplexOpisStyleSetting(
                        "Сложное описание для этапов",
                        code_name_razdel,
                        name
                    )
                )
            }
            val panHistory by nameRazd { name -> add2(PanHistory(name, code_name_razdel, "Панель хроник")) }
        }

        inner class GoalTabParam(
            code_parent: String,
            code_razdel: String
        ) : RazdelSettingInner(
            "Цели",
            code_name_razdel = "${code_parent}_${code_razdel}",
            type_razdel = TypeSaveStyleSet.FULLPANEL,
            sver = true, color = MyColorARGB("7AC6A4A1")
        ) {
            val buttPrivPlan by nameRazd { name -> r_addTextButton(name, "Кнопка привязанных планов") }
            val buttArrowGoal by nameRazd { name -> r_addTextButton(name, "Кнопки листания целей") }
            val buttAddGoal by nameRazd { name -> r_addTextButton(name, "Кнопка доб. цели") }
            val buttVypGoal by nameRazd { name -> r_addToggleButton(name, "Кнопка вып. цели") }
            val buttViewSpisGoal by nameRazd { name -> r_addToggleButton(name, "Кнопка списка целей") }
            val textCountGoal by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст количества целей",
                    MyColorARGB.colorMyBorderStroke,
                    20.0
                )
            }

            val textHourGoal1 by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст кол. часов (диаг. 1)",
                    MyColorARGB.colorMyMainTheme,
                    16.0
                )
            }
            val textHourGoal2 by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст кол. часов (диаг. 2)",
                    MyColorARGB.colorMyMainTheme,
                    16.0
                )
            }
            val TEXT_ALL_HOUR_COLOR by settName(addColor("Кол. часов диаг. (всего)", MyColorARGB.colorStatTint_01))
            val TEXT_REST_HOUR_COLOR by settName(addColor("Кол. часов диаг. (остальные)", MyColorARGB.colorStatTint_04))

            val rectDiagColor by nameRazd { name -> r_addRectDiagramColor(name, "Цвета диаграммы") }

            val panelDiagram by nameRazd { name -> r_addSimplePlateWithShadow(name, "Панель диаграмм") }

            val itemGoal = add2(ItemGoalStyle())
        }

        inner class DreamTabParam(
            code_parent: String,
            code_razdel: String
        ) : RazdelSettingInner(
            "Мечты",
            code_name_razdel = "${code_parent}_${code_razdel}",
            type_razdel = TypeSaveStyleSet.FULLPANEL,
            sver = true, color = MyColorARGB("7A86A4A1")
        ) {
            val buttArrowDream by nameRazd { name -> r_addTextButton(name, "Кнопки листания мечт") }
            val buttAddDream by nameRazd { name -> r_addTextButton(name, "Кнопка доб. мечты") }

            val buttVypDream by nameRazd { name -> r_addToggleButton(name, "Кнопка вып. мечты") }
            val buttViewSpisDream by nameRazd { name -> r_addToggleButton(name, "Кнопка списка мечт") }

            val textCountDream by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст количества мечт",
                    MyColorARGB.colorMyBorderStroke,
                    20.0
                )
            }
            val textAdd by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст \"Добавить фото\"",
                    MyColorARGB.colorMyBorderStroke,
                    16.0
                )
            }
            val textOpis by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст описания",
                    MyColorARGB.colorMyBorderStroke,
                    16.0
                )
            }

            val panelImage by nameRazd { name -> r_addSimplePlateWithShadow(name, "Панель изображения") }
            val panelOpis by nameRazd { name -> r_addSimplePlateWithShadow(name, "Панель описания") }
            val innerPaddingOpis by nameRazd { name -> r_addPadding(name, "Внутренние отступы описания") }

            val VIGNETTE by settName(addBoolean("Виньетирование описания", true))

            val itemDream = add2(ItemDreamStyle())
        }

        inner class CharacteristicsPanelView(
            code_parent: String,
            code_razdel: String
        ) : RazdelSettingInner(
            "Панель информации",
            code_name_razdel = "${code_parent}_${code_razdel}",
            type_razdel = TypeSaveStyleSet.FULLPANEL,
            sver = true, color = MyColorARGB("7AC6C4A1")
        ) {
            val grafColor by nameRazd { name -> r_addGrafikColor(name, "Цвета графика роста") }
        }

        inner class CharacteristicsParam(
            code_parent: String,
            code_razdel: String
        ) : RazdelSettingInner(
            "Характеристики",
            code_name_razdel = "${code_parent}_${code_razdel}",
            type_razdel = TypeSaveStyleSet.FULLPANEL,
            sver = true, color = MyColorARGB("7AC6C4A1")
        ) {
            val plate by nameRazd { name -> r_addSimplePlateWithShadow(name, "Панель") }
            val BORDER2_WIDTH by settName(addDoublePoz("Ширина 2-й границы", 2.0))
            val BORDER2_BRUSH by nameRazd { name -> r_addBrush(name, "2-я граница") }
            val textCount by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст количества характеристик",
                    MyColorARGB.colorMyBorderStroke,
                    20.0
                )
            }

            val buttEdit by nameRazd { name -> r_addToggleButton(name, "Кнопка редактирования характеристик") }
            val buttAdd by nameRazd { name -> r_addTextButton(name, "Кнопка добавления характеристики") }

            val VIGNETTE1 by settName(addBoolean("Виньетирование1", true))
            val VIGNETTE2 by settName(addBoolean("Виньетирование2", false))

            val itemCharacteristic = add2(ItemCharacteristicStyle())

            val characteristicsPanelView by nameRazd { name -> add2(CharacteristicsPanelView(name, code_name_razdel)) }

        }

        inner class SkillsParam(
            code_parent: String,
            code_razdel: String
        ) : RazdelSettingInner(
            "Ачивки",
            code_name_razdel = "${code_parent}_${code_razdel}",
            type_razdel = TypeSaveStyleSet.FULLPANEL,
            sver = true, color = MyColorARGB("7A46C481")
        ) {
            val plate by nameRazd { name -> r_addSimplePlateWithShadow(name, "Панель") }

            val textCount by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст количества деревьев",
                    MyColorARGB.colorMyBorderStroke,
                    20.0
                )
            }
            val buttEdit by nameRazd { name -> r_addToggleButton(name, "Кнопка редактирования деревьев") }
            val buttAdd by nameRazd { name -> r_addTextButton(name, "Кнопка добавления деревьев") }
            val buttAddLevel by nameRazd { name -> r_addTextButton(name, "Кнопка доб. уровня деревьев") }
            val buttAddNode by nameRazd { name -> r_addTextButton(name, "Кнопка доб. узла деревьев") }

            val buttVisibleBinding by nameRazd { name ->
                r_addToggleButton(
                    name,
                    "Кнопка видимости связей узлов дерева"
                )
            }

            val itemSkillNode = add2(ItemNodeTreeSkillsStyle())
            val itemSkillLevel = add2(ItemSkillsTreeLevelStyle())
            val itemSkill = add2(ItemSkillsTreeStyle())
        }

        inner class ChronicleParam(
            code_parent: String,
            code_razdel: String
        ) : RazdelSettingInner(
            "Хроника времен",
            code_name_razdel = "${code_parent}_${code_razdel}",
            type_razdel = TypeSaveStyleSet.FULLPANEL,
            sver = true, color = MyColorARGB("7AC6A4A1")
        ) {
            val plateChronicle by nameRazd { name -> r_addSimplePlateWithShadow(name, "Панель") }
            val textChronicle by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст названия панели",
                    MyColorARGB.colorMyBorderStroke,
                    20.0
                )
            }
            val buttSelectDate by nameRazd { name -> r_addTextButton(name, "Кнопка выбора даты") }
            val innerPadding by nameRazd { name -> r_addPadding(name, "Внутренние отступы") }
            val itemBestDay = add2(ItemBestDayStyle())
        }

        open inner class ItemStatusStyle(
            nameItem: String,
            code_name_parent_razdel: String,
            name_podrazdel: String,
            color: MyColorARGB = MyColorARGB("FFBE8367"),
            type_razdel: TypeSaveStyleSet,
            border_width: Double = 2.0,
            elevation_default: Double = 2.0,
            background_color: MyColorARGB = MyColorARGB.colorMyMainTheme,
            border_color: MyColorARGB = MyColorARGB.colorMyBorderStrokeCommon,
            enableGradientBackgr: Boolean = false,
            enableGradientBorder: Boolean = false,
            background_angle: Long = 0L,
            border_angle: Long = 0L,
            background: List<MyColorARGB> = listOf(MyColorARGB.colorBackGr2, MyColorARGB.colorBackGr2),
            border: List<MyColorARGB> = listOf(
                MyColorARGB.colorMyBorderStrokeCommon,
                MyColorARGB.colorMyBorderStrokeCommon
            ),
            sver: Boolean = true,
        ) : RazdelSettingInner(nameItem, color, "${code_name_parent_razdel}_${name_podrazdel}", type_razdel, sver) {
            val outer_padding by nameRazd { name -> r_addPadding(name, "Внешние отступы") }
            val inner_padding by nameRazd { name -> r_addPadding(name) }
            val shape by nameRazd { name -> r_addShape(name, "Форма") }
            val shadow by nameRazd { name -> r_addShadowStyle(name, "Стиль тени") }
            val BORDER_WIDTH by settName(addDoublePoz("Ширина границы", border_width))
            val BACKGROUND by nameRazd { name ->
                r_addBrush(
                    name, "Фон",
                    mainColor = background_color,
                    gradientEnable = enableGradientBackgr,
                    gradientAngle = background_angle,
                    gradientColor = background,
                )
            }
            val BORDER by nameRazd { name ->
                r_addBrush(
                    name, "Граница",
                    mainColor = border_color,
                    gradientEnable = enableGradientBorder,
                    gradientAngle = border_angle,
                    gradientColor = border,
                )
            }
            val mainText by nameRazd { name -> r_addTextStyle(name, "Стиль названия") }
            val valueText by nameRazd { name -> r_addTextStyle(name, "Стиль значения") }

        }

        inner class AppBarStyle(
        ) : RazdelSettingInner(
            "Заголовок окна и окно",
            code_name_razdel = "APP_BAR",
            type_razdel = TypeSaveStyleSet.FULLPANEL,
            sver = true,
            color = MyColorARGB("FFAA6500")
        ) {
            val shape_window by nameRazd { name -> r_addShape(name, "Форма окна") }
            val plateAppBar by nameRazd { name -> r_addSimplePlateWithShadow(name, "Панель") }
            val textItreumme by nameRazd { name -> r_addTextStyle(name, "Стиль названия") }
            val buttIcon by nameRazd { name -> r_addToggleButton(name, "Стиль кнопок") }
            val outerIconPadding by nameRazd { name -> r_addPadding(name, "Отступы кнопок") }
        }

        inner class StatusParam(
        ) : RazdelSettingInner(
            "Панель статуса",
            code_name_razdel = "STATUS_PANEL",
            type_razdel = TypeSaveStyleSet.FULLPANEL,
            sver = true,
            color = MyColorARGB("FFAA6500")
        ) {
            val plateStatus by nameRazd { name -> r_addSimplePlateWithShadow(name, "Панель") }
            val inner_padding by nameRazd { name -> r_addPadding(name) }
            val outerPadding by nameRazd { name -> r_addPadding(name, "Внешние отступы") }
            val outerPaddingAvatar by nameRazd { name -> r_addPadding(name, "Внешние отступы аватара") }

            val BORDER_WIDTH_AVATAR by settName(addDoublePoz("Ширина границы аватара", 1.0))
            val BORDER_BRUSH_AVATAR by nameRazd { name -> r_addBrush(name, "Граница аватара") }
            val shape_avatar by nameRazd { name -> r_addShape(name, "Форма аватара") }
            val shadow_avatar by nameRazd { name -> r_addShadowStyle(name, "Стиль тени аватара") }
            val shadow_panel by nameRazd { name -> r_addShadowStyle(name, "Стиль тени панели") }

            val itemTimeLife by nameRazd { name ->
                add2(
                    ItemStatusStyle(
                        "Айтем времени жизни",
                        code_name_razdel,
                        name,
                        type_razdel = TypeSaveStyleSet.NOTSAVE
                    )
                )
            }
            val itemStatus by nameRazd { name ->
                add2(
                    ItemStatusStyle(
                        "Айтем статуса",
                        code_name_razdel,
                        name,
                        type_razdel = TypeSaveStyleSet.NOTSAVE
                    )
                )
            }
        }


        inner class AvatarParam(
        ) : RazdelSettingInner(
            "Аватар",
            code_name_razdel = "AVATAR_PANEL",
            type_razdel = TypeSaveStyleSet.FULLPANEL,
            sver = false,
            color = MyColorARGB("FFAA6500")
        ) {

            val seekBarStyle by nameRazd { name -> r_addSeekBar(name, "Кнопки выбора панелей") }

            val chronicleTab by nameRazd { name -> add2(ChronicleParam(code_name_razdel, name)) }
            val characteristicsTab by nameRazd { name -> add2(CharacteristicsParam(code_name_razdel, name)) }
            val goalTab by nameRazd { name -> add2(GoalTabParam(code_name_razdel, name)) }
            val skillTab by nameRazd { name -> add2(SkillsParam(code_name_razdel, name)) }
            val dreamTab by nameRazd { name -> add2(DreamTabParam(code_name_razdel, name)) }
        }

        inner class TimeParam(
        ) : RazdelSettingInner(
            "Время",
            code_name_razdel = "TIME_PANEL",
            type_razdel = TypeSaveStyleSet.FULLPANEL,
            sver = false,
            color = MyColorARGB("4509465F")
        ) {

            val seekBarStyle = r_addSeekBar("time_seek_bar", "Кнопки выбора панелей")

            val boxSelectParentPlanParam = add2(BoxSelectParentPlanParam())
            val timelineCommonParam = add2(TimelineCommonParam())
            val planTab = add2(PlanTabParam())
            val denPlanTab = add2(DenPlanTabParam())
            val vxodTab = add2(VxodTabParam())

            val itemEffect = add2(ItemEffectStyle())
        }


        inner class PanSelectDateStyle(
            name_podrazdel: String, code_name_item: String
        ) : RazdelSettingInner(
            "Панель выбора даты",
            code_name_razdel = "${code_name_item}_${name_podrazdel}",
            type_razdel = TypeSaveStyleSet.FULLPANEL
        ) {
            val VIGNETTE by settName(addBoolean("Виньетирование", true))
            val platePanel by nameRazd { name -> r_addSimplePlate(name, "Панель") }
            val panelPadding by nameRazd { name -> r_addPadding(name) }
            val plateDayWeek by nameRazd { name -> r_addSimplePlateWithShadow(name, "Панель дня недели") }
            val plateWeekend by nameRazd { name -> r_addSimplePlateWithShadow(name, "Панель выходных") }
            val textDayWeek by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст дня недели",
                    MyColorARGB("FFFFF7D9"),
                    24.0
                )
            }
            val textWeekend by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст выходных",
                    MyColorARGB("FFFFF7D9"),
                    24.0
                )
            }
            val textDate by nameRazd { name -> r_addTextStyle(name, "Текст даты", MyColorARGB("FFFFF7D9"), 24.0) }
            val textMonth by nameRazd { name -> r_addTextStyle(name, "Текст месяца", MyColorARGB("FFFFF7D9"), 24.0) }
            val buttArrow by nameRazd { name -> r_addTextButton(name, "Кнопка листания месяцев") }
            val buttYear by nameRazd { name -> r_addTextButton(name, "Кнопка выбора года") }
            val buttYearActive by nameRazd { name -> r_addTextButton(name, "Кнопка выбранного года") }
            val buttNumber by nameRazd { name -> r_addTextButton(name, "Кнопка выбора числа") }
            val buttNumberActive by nameRazd { name -> r_addTextButton(name, "Кнопка выбранного числа") }
            val buttNumberWeekend by nameRazd { name -> r_addTextButton(name, "Кнопка выбора числа (вых.)") }
            val buttNumberWeekendActive by nameRazd { name -> r_addTextButton(name, "Кнопка выбранного числа (вых.)") }
            val buttSelect by nameRazd { name -> r_addTextButton(name, "Кнопка Ок") }
            val buttCancel by nameRazd { name -> r_addTextButton(name, "Кнопка отмены") }
        }


        inner class CommStyleParam(
        ) : RazdelSettingInner("Общие параметры", code_name_razdel = "", type_razdel = TypeSaveStyleSet.FULLPANEL) {
            val OLD_PAPER by settName(addBoolean("Старая бумага", true))
            val HARD_BORDER by settName(addBoolean("Декоративные рамки окна", false))
            val BORDER_WIDTH by settName(addDoublePoz("Ширина границы", 1.0))
            val BORDER_BRUSH = r_addBrush("main_BORDER_BRUSH", "Граница окна")
            val COLOR_BACKGROUND = r_addBrush("main_background", "Цвет общего фона")
            val mainSeekBarStyle = r_addSeekBar("main_seek_bar", "Кнопки выбора панелей")
            val panSelectDate by nameRazd { name -> add2(PanSelectDateStyle(name, code_name_razdel)) }
            val commonPanel by nameRazd { name -> add2(PanMessage(name, code_name_razdel, "Стандартный вид панели")) }
            val commonTextField by nameRazd { name -> r_addTextField(name, "Текстовое поле для ввода") }
            val commonItemStyle = add2(CommonItemStyle())
            val commonOpisStyle = add2(
                CommonBoxOpisWithButtStyleItemSetting(
                    "Стандартный вид описания",
                    code_name_razdel,
                    "opis_style",
                    type_razdel = TypeSaveStyleSet.COMMONITEM
                )
            )
            val commonDropdownMenuStyle by nameRazd { name -> r_addDropDownMenu(name, "Выпадающее меню") }
            val commonComboBoxStyle by nameRazd { name -> r_addComboBox(name, "Выпадающее список") }
            val privSchetPlanInfo = add2(PrivSchetPlanInfo("PrivSchetPlanInfo"))
            val commonTextButt = r_addTextButton("commonTextButton", "Стандартные кнопки")
        }

        inner class StyleParam(
        ) : RazdelSettingInner(
            "Все параметры",
            code_name_razdel = "",
            type_razdel = TypeSaveStyleSet.FULL,
            sver = false
        ) {

            val appBarStyle = add2(AppBarStyle())
            val commonParam = add2(CommStyleParam())
            val statusParam = add2(StatusParam())
            val avatarParam = add2(AvatarParam())
            val timeParam = add2(TimeParam())
            val journalParam = add2(JournalParam())
            val finParam by nameRazd { name -> add2(FinParam(name, code_name_razdel)) }

        }

        val styleParam = StyleParam()
    }

    val styleSett = InterfaceState()

}