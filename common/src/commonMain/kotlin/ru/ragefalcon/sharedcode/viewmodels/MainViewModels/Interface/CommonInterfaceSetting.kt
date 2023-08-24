package ru.ragefalcon.sharedcode.viewmodels.MainViewModels.Interface

import com.soywiz.klock.DateTimeTz
import ru.ragefalcon.sharedcode.common.SelectLoadCommon
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.extensions.localUnix
import ru.ragefalcon.sharedcode.source.disk.ItrCommObserveMutableObj
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.BooleanItrComm
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.EnumObject2List
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.EnumObjectList
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.MyTypeCorner
import kotlin.reflect.KProperty

data class ItemInterfaceSetting(
    val code_name: String,
    val long: Long,
    val double: Double,
    val string: String
)

enum class TypeSaveStyleSet(val id: Long, val nameSpis: String) {
    NOTSAVE(0L, "Не сохраняемые?.."),
    FULL(1L, "Полные стили"),
    COMMONITEM(2L, "Шаблоны айтемов"),
    TEXTSTYLE(3L, "Стили текста"),
    TEXTBUTTON(4L, "Стили кнопок"),
    FULLPANEL(5L, "Стили целых панелей"),
    BACKANDBORDER(6L, "Градиенты"),
    BRUSH(7L, "Кисти"),
    SHAPE(8L, "Форма"),
    PADDING(9L, "Отступы"),
    SHADOW(10L, "Тени"),
    DROPDOWN(11L, "Выпадающее меню"),
    COMBOBOX(12L, "Комбобокс"),
    TEXTFIELD(13L, "Текстовое поле");

    companion object {
        fun getType(id: Long): TypeSaveStyleSet? = values().toList().find { it.id == id }
    }

}

interface CommonInterfaceSettingTable {
    fun clearFromDeprecated(list: List<String>)
    fun insert(code_name: String, defLong: Long, defDouble: Double, defString: String)
    fun update(code_name: String, long: Long)
    fun update(code_name: String, double: Double)
    fun update(code_name: String, string: String)
    fun transaction(ff: () -> Unit)
    fun getListFromBase(): List<ItemInterfaceSetting>
}

open class CommonInterfaceSetting(private val table: CommonInterfaceSettingTable) {
    sealed interface InterfaceSettingsType {
        val codeParent: String
        var codeName: String
        val nameSett: String
        val color: MyColorARGB
        val defLong: Long
        val defDouble: Double
        val defString: String
        fun updateValue(long: Long, double: Double, string: String)
        fun getCurrentItemInterSett(): ItemInterfaceSetting
        val itrObj: ItrCommObserveMutableObj<*>
        val code: String
            get() = if (codeParent != "") "${codeParent}_${codeName}" else codeName

    }

    /*
        inner class DelegateName2() {
            operator fun getValue(thisRef: InterfaceSettingsBoolean, property: KProperty<*>): InterfaceSettingsBoolean {
                aa.code_name = property.name
                return aa
            }

    //            operator fun getValue(brushStyleSetting: CommonInterfaceSetting.MySettings.BrushStyleSetting, property: KProperty<*>): Any {
    //
    //            }

            operator fun setValue(thisRef: InterfaceSettingsBoolean, property: KProperty<*>, value: String) {
                thisRef.code_name = property.name
                aa = thisRef
            }
        }
    */
    inner class InterfaceSettingsBoolean(
        override val codeParent: String,
        override var codeName: String,
        valueStart: Boolean,
        override val nameSett: String = "",
        override val color: MyColorARGB = MyColorARGB.colorMyMainTheme,
    ) : InterfaceSettingsType {
        override val defLong: Long = if (valueStart) 1L else 0L
        override val defDouble: Double = 0.0
        override val defString: String = ""


        private var currentValue = defLong
        override fun getCurrentItemInterSett() = ItemInterfaceSetting(code, currentValue, defDouble, defString)

        override val itrObj = ItrCommObserveMutableObj<Boolean>(valueStart) {
            currentValue = if (it) 1L else 0L
            table.update(this.code, currentValue)
        }

        override fun updateValue(long: Long, double: Double, string: String) {
            currentValue = long
            itrObj.innerUpdateValue(currentValue == 1L)
        }

    }

    inner class InterfaceSettingsLong(
        override val codeParent: String,
        override var codeName: String,
        valueStart: Long,
        override val nameSett: String = "",
        override val color: MyColorARGB = MyColorARGB.colorMyMainTheme,
    ) : InterfaceSettingsType {
        override val defLong: Long = valueStart
        override val defDouble: Double = 0.0
        override val defString: String = ""

        private var currentValue = defLong
        override fun getCurrentItemInterSett() = ItemInterfaceSetting(code, currentValue, defDouble, defString)

        override val itrObj = ItrCommObserveMutableObj<Long>(valueStart) {
            currentValue = it
            table.update(this.code, it)
        }

        override fun updateValue(long: Long, double: Double, string: String) {
            currentValue = long
            itrObj.innerUpdateValue(long)
        }
    }

    inner class InterfaceSettingsAngle(
        override val codeParent: String,
        override var codeName: String,
        valueStart: Long,
        override val nameSett: String = "",
        override val color: MyColorARGB = MyColorARGB.colorMyMainTheme,
    ) : InterfaceSettingsType {
        override val defLong: Long = valueStart
        override val defDouble: Double = 0.0
        override val defString: String = ""

        private var currentValue = defLong
        override fun getCurrentItemInterSett() = ItemInterfaceSetting(code, currentValue, defDouble, defString)

        override val itrObj = ItrCommObserveMutableObj<Long>(valueStart) {
            currentValue = it
            table.update(this.code, it)
        }

        override fun updateValue(long: Long, double: Double, string: String) {
            currentValue = long
            itrObj.innerUpdateValue(long)
        }
    }

    inner class InterfaceSettingsTypeCorner(
        override val codeParent: String,
        override var codeName: String,
        valueStart: MyTypeCorner,
        override val nameSett: String = "",
        override val color: MyColorARGB = MyColorARGB.colorMyMainTheme,
    ) : InterfaceSettingsType {
        override val defLong: Long = valueStart.id
        override val defDouble: Double = 0.0
        override val defString: String = ""

        private var currentValue = defLong
        override fun getCurrentItemInterSett() = ItemInterfaceSetting(code, currentValue, defDouble, defString)

        override val itrObj = ItrCommObserveMutableObj<MyTypeCorner>(valueStart) {
//            println("update table ItrComm Obser Corner")
            currentValue = it.id
            table.update(this.code, currentValue)
        }

        override fun updateValue(long: Long, double: Double, string: String) {
//            println("updateValue ItrComm Obser Corner")
            currentValue = long
            MyTypeCorner.getType(long)?.let {
                itrObj.innerUpdateValue(it)
            }
        }
    }

    inner class InterfaceSettingsFontWeight(
        override val codeParent: String,
        override var codeName: String,
        valueStart: Long,
        override val nameSett: String = "",
        override val color: MyColorARGB = MyColorARGB.colorMyMainTheme,
    ) : InterfaceSettingsType {
        override val defLong: Long = valueStart
        override val defDouble: Double = 0.0
        override val defString: String = ""

        private var currentValue = defLong
        override fun getCurrentItemInterSett() = ItemInterfaceSetting(code, currentValue, defDouble, defString)

        override val itrObj = ItrCommObserveMutableObj<Long>(valueStart) {
            currentValue = it
            table.update(this.code, it)
        }

        override fun updateValue(long: Long, double: Double, string: String) {
            currentValue = long
            itrObj.innerUpdateValue(long)
        }
    }

    inner class InterfaceSettingsDouble(
        override val codeParent: String,
        override var codeName: String,
        valueStart: Double,
        override val nameSett: String = "",
        override val color: MyColorARGB = MyColorARGB.colorMyMainTheme,
    ) : InterfaceSettingsType {
        override val defLong: Long = 1L
        override val defDouble: Double = valueStart
        override val defString: String = ""

        private var currentValue = defDouble
        override fun getCurrentItemInterSett() = ItemInterfaceSetting(code, defLong, currentValue, defString)

        override val itrObj = ItrCommObserveMutableObj<Double>(valueStart) {
            currentValue = it
            table.update(this.code, it)
        }

        override fun updateValue(long: Long, double: Double, string: String) {
            currentValue = double
            itrObj.innerUpdateValue(double)
        }
    }

    inner class InterfaceSettingsDoublePozitive(
        override val codeParent: String,
        override var codeName: String,
        valueStart: Double,
        override val nameSett: String = "",
        override val color: MyColorARGB = MyColorARGB.colorMyMainTheme,
    ) : InterfaceSettingsType {
        override val defLong: Long = 1L
        override val defDouble: Double = if (valueStart >= 0) valueStart else 0.0
        override val defString: String = ""

        private var currentValue = defDouble
        override fun getCurrentItemInterSett() = ItemInterfaceSetting(code, defLong, currentValue, defString)

        override val itrObj = ItrCommObserveMutableObj<Double>(valueStart) {
            if (it >= 0) {
                currentValue = it
                table.update(this.code, it)
            }
        }

        override fun updateValue(long: Long, double: Double, string: String) {
            if (double >= 0) {
                currentValue = double
                itrObj.innerUpdateValue(double)
            }
        }
    }

    inner class InterfaceSettingsString(
        override val codeParent: String,
        override var codeName: String,
        valueStart: String,
        override val nameSett: String = "",
        override val color: MyColorARGB = MyColorARGB.colorMyMainTheme,
    ) : InterfaceSettingsType {
        override val defLong: Long = 1L
        override val defDouble: Double = 0.0
        override val defString: String = valueStart

        private var currentValue = defString
        override fun getCurrentItemInterSett() = ItemInterfaceSetting(code, defLong, defDouble, currentValue)

        override val itrObj = ItrCommObserveMutableObj<String>(valueStart) {
            currentValue = it
            table.update(this.code, it)
        }

        override fun updateValue(long: Long, double: Double, string: String) {
            currentValue = string
            itrObj.innerUpdateValue(string)
        }
    }

    inner class InterfaceSettingsMyColor(
        override val codeParent: String,
        override var codeName: String,
        valueStart: MyColorARGB,
        override val nameSett: String = "",
        override val color: MyColorARGB = MyColorARGB.colorMyMainTheme,
    ) : InterfaceSettingsType {
        override val defLong: Long = 1L
        override val defDouble: Double = 0.0
        override val defString: String = valueStart.toHexString()

        private var currentValue = defString
        override fun getCurrentItemInterSett() = ItemInterfaceSetting(code, defLong, defDouble, currentValue)

        fun getCurrentColor() = MyColorARGB(currentValue)

        override val itrObj = ItrCommObserveMutableObj<MyColorARGB>(valueStart) {
            currentValue = it.toHexString()
            table.update(this.code, currentValue)
        }

        override fun updateValue(long: Long, double: Double, string: String) {
            currentValue = string
            itrObj.innerUpdateValue(MyColorARGB(string))
        }
    }

    inner class InterfaceSettingsMyColorGradient(
        override val codeParent: String,
        override var codeName: String,
        valueStart: List<MyColorARGB>,
        override val nameSett: String = "",
        override val color: MyColorARGB = MyColorARGB.colorMyMainTheme,
    ) : InterfaceSettingsType {
        override val defLong: Long = 1L
        override val defDouble: Double = 0.0
        override val defString: String = run {
            var tmp = ""
            valueStart.forEach {
                if (tmp != "") tmp += ";"
                tmp += it.toHexString()
            }
            tmp
        }

        private var currentValue = defString
        override fun getCurrentItemInterSett() = ItemInterfaceSetting(code, defLong, defDouble, currentValue)

        override val itrObj = ItrCommObserveMutableObj<List<MyColorARGB>>(valueStart) { list ->
            var tmp = ""
            list.forEach {
                if (tmp != "") tmp += ";"
                tmp += it.toHexString()
            }
            currentValue = tmp
            table.update(this.code, currentValue)
        }

        override fun updateValue(long: Long, double: Double, string: String) {
            currentValue = string
            itrObj.innerUpdateValue(string.split(";").map { MyColorARGB(it) })
        }
    }

    open class RazdelSetting(
        val nameRazdel: String,
        val color: MyColorARGB = MyColorARGB.colorBackGr2,
        val code_name_razdel: String,
        val type_razdel: TypeSaveStyleSet,
        sver: Boolean = false
    ) : EnumObject2List<InterfaceSettingsType, RazdelSetting>() {

        fun getListInterfaceSettings(): List<ItemInterfaceSetting> {
            val listSett = mutableListOf<ItemInterfaceSetting>()
            getSpisSett(this, listSett)
            return listSett
        }

        private fun getSpisSett(razdel: RazdelSetting, listSett: MutableList<ItemInterfaceSetting>) {
            listSett.addAll(razdel.map {
                it.getCurrentItemInterSett().copy(code_name = it.code.removePrefix(code_name_razdel))
            })
            razdel.list2.forEach {
                getSpisSett(it, listSett)
            }
        }

        val svernut = BooleanItrComm(sver)
    }

    open inner class MySettings() : EnumObjectList<InterfaceSettingsType>() {

        inner class nameRazd<T : RazdelSetting>(private var funn: (String) -> T) {

            operator fun provideDelegate(
                thisRef: Any?,
                property: KProperty<*>
            ): settNameRazdDelegat<T> {
                return settNameRazdDelegat(funn(property.name))
            }
        }

        inner class settName<T : InterfaceSettingsType>(private var sett: T) {

            operator fun provideDelegate(
                thisRef: Any?,
                property: KProperty<*>
            ): settNameDelegat<T> {
                sett.codeName = property.name
                return settNameDelegat(sett)
            }
        }

        inner class settNameDelegat<T : InterfaceSettingsType>(private var sett: T) {

            operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
                return sett
            }

            operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
                value.codeName = property.name
                sett = value
            }
        }

        inner class settNameRazdDelegat<T : RazdelSetting>(private var razd: T) {

            operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
                return razd
            }

            operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
                razd = value
            }
        }

        open inner class RazdelSettingInner(
            nameRazdel: String,
            color: MyColorARGB = MyColorARGB.colorBackGr2,
            code_name_razdel: String,
            type_razdel: TypeSaveStyleSet,
            sver: Boolean = true
        ) :
            RazdelSetting(nameRazdel, color, code_name_razdel, type_razdel, sver) {
            protected fun <T : InterfaceSettingsType> addSett(item: T, ind: Int? = null): T =
                this@MySettings.add(this@RazdelSettingInner.add(item, ind))

            protected fun addBoolean(
                nameSett: String,
                defBoolean: Boolean = false,
                codeSett: String = ""
            ): InterfaceSettingsBoolean = addSett(
                InterfaceSettingsBoolean(
                    code_name_razdel,
                    codeSett,
                    defBoolean,
                    nameSett,
                    MyColorARGB("FF40432E")
                )
            )

            protected fun addDoublePoz(
                nameSett: String,
                defDouble: Double = 2.0,
                codeSett: String = ""
            ): InterfaceSettingsDoublePozitive = addSett(
                InterfaceSettingsDoublePozitive(
                    code_name_razdel,
                    codeSett,
                    defDouble,
                    nameSett,
                    MyColorARGB("FF40432E")
                )
            )

            protected fun addDouble(
                nameSett: String,
                defDouble: Double = 2.0,
                codeSett: String = ""
            ): InterfaceSettingsDouble = addSett(
                InterfaceSettingsDouble(
                    code_name_razdel,
                    codeSett,
                    defDouble,
                    nameSett,
                    MyColorARGB("FF40432E")
                )
            )

            protected fun addAngle(
                nameSett: String,
                defAngle: Long = 0L,
                codeSett: String = ""
            ): InterfaceSettingsAngle = addSett(
                InterfaceSettingsAngle(
                    code_name_razdel,
                    codeSett,
                    defAngle,
                    nameSett,
                    MyColorARGB("FF40432E")
                )
            )

            protected fun addColorGradient(
                nameSett: String,
                defValue: List<MyColorARGB>,
                codeSett: String = ""
            ): InterfaceSettingsMyColorGradient = addSett(
                InterfaceSettingsMyColorGradient(
                    code_name_razdel,
                    codeSett,
                    defValue,
                    nameSett,
                    MyColorARGB("FF40432E")
                )
            )

            protected fun addColor(
                nameSett: String,
                defColorARGB: MyColorARGB = MyColorARGB.colorMyMainTheme,
                codeSett: String = ""
            ): InterfaceSettingsMyColor = addSett(
                InterfaceSettingsMyColor(
                    code_name_razdel,
                    codeSett,
                    defColorARGB,
                    nameSett,
                    MyColorARGB("FF40432E")
                )
            )

            protected fun addCorner(
                nameSett: String,
                defCorner: MyTypeCorner,
                codeSett: String = ""
            ): InterfaceSettingsTypeCorner = addSett(
                InterfaceSettingsTypeCorner(
                    code_name_razdel,
                    codeSett,
                    defCorner,
                    nameSett,
                    MyColorARGB("FF40432E")
                )
            )

            protected fun r_addBrush(
                codeSett: String,
                nameSett: String,
                mainColor: MyColorARGB = MyColorARGB.colorMyBorderStroke,
                gradientEnable: Boolean = false,
                gradientAngle: Long = 0L,
                gradientColor: List<MyColorARGB> = listOf(MyColorARGB.colorRasxodTheme, MyColorARGB.colorRasxodTheme),
            ): BrushStyleSetting = add2(
                BrushStyleSetting(
                    code_name_razdel,
                    codeSett,
                    nameSett,
                    type_razdel = TypeSaveStyleSet.BRUSH,
                    mainColor = mainColor,
                    gradientEnable = gradientEnable,
                    gradientAngle = gradientAngle,
                    gradientColor = gradientColor
                )
            )

            protected fun r_addShape(
                codeSett: String,
                nameSett: String
            ): CornerStyleItemSetting = add2(
                CornerStyleItemSetting(
                    code_name_parent_razdel = code_name_razdel,
                    name_podrazdel = codeSett,
                    nameItem = nameSett,
                    type_razdel = TypeSaveStyleSet.SHAPE
                )
            )

            protected fun r_addTextField(
                codeSett: String,
                nameSett: String
            ): MyTextFieldStyle = add2(
                MyTextFieldStyle(
                    code_name_parent_razdel = code_name_razdel,
                    name_podrazdel = codeSett,
                    nameSett = nameSett
                )
            )

            protected fun r_addPadding(
                codeSett: String,
                nameSett: String = "Внутренние отступы"
            ): PaddingStyleItemSetting = add2(
                PaddingStyleItemSetting(
                    code_name_razdel,
                    nameItem = nameSett,
                    name_podrazdel = codeSett,
                    type_razdel = TypeSaveStyleSet.PADDING
                )
            )

            protected fun r_addTextStyle(
                codeSett: String,
                nameSett: String,
                defColorARGB: MyColorARGB = MyColorARGB.colorMyBorderStroke,
                defFontSize: Double = 13.0
            ): TextStyleSetting = add2(
                TextStyleSetting(
                    code_name_razdel,
                    name_podrazdel = codeSett,
                    nameItem = nameSett,
                    type_razdel = TypeSaveStyleSet.TEXTSTYLE,
                    textColor = defColorARGB,
                    fontSize = defFontSize,
                )
            )

            protected fun r_addShadowStyle(
                codeSett: String,
                nameSett: String,
            ): ShadowStyleSetting = add2(
                ShadowStyleSetting(
                    code_name_razdel,
                    name_podrazdel = codeSett,
                    nameItem = nameSett,
                )
            )

            protected fun r_addSimplePlateWithShadow(
                codeSett: String,
                nameSett: String,
            ): SimplePlateWithShadowStyleItemSetting = add2(
                SimplePlateWithShadowStyleItemSetting(
                    nameSett,
                    code_name_razdel,
                    codeSett,
                    type_razdel = TypeSaveStyleSet.TEXTBUTTON
                )
            )

            protected fun r_addSimplePlate(
                codeSett: String,
                nameSett: String,
            ): SimplePlateStyleItemSetting = add2(
                SimplePlateStyleItemSetting(
                    nameSett,
                    code_name_razdel,
                    codeSett,
                    type_razdel = TypeSaveStyleSet.TEXTBUTTON
                )
            )

            protected fun r_addDropDownMenu(
                codeSett: String,
                nameSett: String,
            ): DropDownMenuStyleSetting = add2(
                DropDownMenuStyleSetting(
                    nameSett,
                    code_name_razdel,
                    codeSett,
                    type_razdel = TypeSaveStyleSet.DROPDOWN
                )
            )

            protected fun r_addComboBox(
                codeSett: String,
                nameSett: String,
            ): ComboBoxStyleSetting = add2(
                ComboBoxStyleSetting(
                    nameSett,
                    code_name_razdel,
                    codeSett,
                    type_razdel = TypeSaveStyleSet.COMBOBOX
                )
            )

            protected fun r_addRectDiagramColor(
                codeSett: String,
                nameSett: String,
            ): RectDiagramColorStyleSetting = add2(
                RectDiagramColorStyleSetting(
                    nameSett,
                    code_name_razdel,
                    codeSett,
                    type_razdel = TypeSaveStyleSet.FULLPANEL
                )
            )

            protected fun r_addTwoRectDiagramColor(
                codeSett: String,
                nameSett: String,
            ): TwoRectDiagramColorStyleSetting = add2(
                TwoRectDiagramColorStyleSetting(
                    nameSett,
                    code_name_razdel,
                    codeSett,
                    type_razdel = TypeSaveStyleSet.FULLPANEL
                )
            )

            protected fun r_addGrafikColor(
                codeSett: String,
                nameSett: String,
            ): GrafikColorStyleSetting = add2(
                GrafikColorStyleSetting(
                    nameSett,
                    code_name_razdel,
                    codeSett,
                    type_razdel = TypeSaveStyleSet.FULLPANEL
                )
            )

            protected fun r_addSeekBar(
                codeSett: String,
                nameSett: String,
            ): ButtonSeekBarStyleItemSetting = add2(
                ButtonSeekBarStyleItemSetting(
                    nameSett,
                    code_name_razdel,
                    codeSett,
                    type_razdel = TypeSaveStyleSet.TEXTBUTTON
                )
            )

            protected fun r_addTextButton(
                codeSett: String,
                nameSett: String,
            ): TextButtonStyleItemSetting = add2(
                TextButtonStyleItemSetting(
                    nameSett,
                    code_name_razdel,
                    codeSett,
                    type_razdel = TypeSaveStyleSet.TEXTBUTTON
                )
            )

            protected fun r_addCheckbox(
                codeSett: String,
                nameSett: String,
            ): MyChekboxStyleSetting = add2(
                MyChekboxStyleSetting(
                    nameSett,
                    code_name_razdel,
                    codeSett,
                )
            )

            protected fun r_addToggleButton(
                codeSett: String,
                nameSett: String,
            ): ToggleButtonStyleItemSetting = add2(
                ToggleButtonStyleItemSetting(
                    nameSett,
                    code_name_razdel,
                    codeSett,
                    type_razdel = TypeSaveStyleSet.TEXTBUTTON
                )
            )

            protected fun r_addIconButton(
                codeSett: String,
                nameSett: String,
            ): IconButtonStyleItemSetting = add2(
                IconButtonStyleItemSetting(
                    nameSett,
                    code_name_razdel,
                    codeSett,
                    type_razdel = TypeSaveStyleSet.TEXTBUTTON
                )
            )

            protected fun r_addIconButtonWithoutBorder(
                codeSett: String,
                nameSett: String,
            ): IconButtonWithoutBorderStyleSetting = add2(
                IconButtonWithoutBorderStyleSetting(
                    nameSett,
                    code_name_razdel,
                    codeSett,
                    type_razdel = TypeSaveStyleSet.TEXTBUTTON
                )
            )

            protected fun addToggleTextButton(
                codeSett: String,
                nameSett: String,
            ): ToggleTextButtonStyleItemSetting = add2(
                ToggleTextButtonStyleItemSetting(
                    nameSett,
                    code_name_razdel,
                    codeSett,
                    type_razdel = TypeSaveStyleSet.TEXTBUTTON
                )
            )
        }


        open inner class BrushStyleSetting(
            code_name_item: String,
            name_podrazdel: String,
            nameItem: String = "Кисть",
            color: MyColorARGB = MyColorARGB("FF657629"),
            type_razdel: TypeSaveStyleSet,

            mainColor: MyColorARGB = MyColorARGB.colorMyBorderStroke,
            gradientEnable: Boolean = false,
            gradientAngle: Long = 0L,
            gradientColor: List<MyColorARGB> = listOf(MyColorARGB.colorRasxodTheme, MyColorARGB.colorRasxodTheme),

            sver: Boolean = true,
        ) : RazdelSettingInner(nameItem, color, "${code_name_item}_${name_podrazdel}", type_razdel, sver) {
            val BRUSH_COLOR by settName(addColor("Цвет", mainColor))
            val GRADIENT_ENABLE by settName(addBoolean("Градиент", gradientEnable))
            val GRADIENT_ANGLE by settName(addAngle("Угол градиента", gradientAngle))
            val GRADIENT_COLORS by settName(addColorGradient("Цвета:", gradientColor))

        }

        open inner class ShadowStyleSetting(
            code_name_parent_razdel: String,
            name_podrazdel: String,
            nameItem: String = "Стиль тени",
            color: MyColorARGB = MyColorARGB("FF9FA361"),
            shadowColor: MyColorARGB = MyColorARGB("66000000"),
            shadowOffsetX: Double = 2.0,
            shadowOffsetY: Double = 2.0,
            shadowBlurRadius: Double = 4.0,
            sver: Boolean = true,
        ) : RazdelSettingInner(
            nameItem,
            color,
            "${code_name_parent_razdel}_${name_podrazdel}",
            TypeSaveStyleSet.SHADOW,
            sver
        ) {
            val SHADOW_COLOR by settName(addColor("Цвет тени", shadowColor))
            val SHADOW_OFFSET_X by settName(addDouble("Тень OffsetX", shadowOffsetX))
            val SHADOW_OFFSET_Y by settName(addDouble("Тень OffsetY", shadowOffsetY))
            val SHADOW_BLUR_RADIUS by settName(addDoublePoz("Тень радиус размытия", shadowBlurRadius))
        }

        open inner class TextStyleSetting(
            code_name_parent_razdel: String,
            name_podrazdel: String,
            nameItem: String = "Стиль текста",
            color: MyColorARGB = MyColorARGB("FF9FA311"),
            type_razdel: TypeSaveStyleSet,
            textColor: MyColorARGB = MyColorARGB.colorMyBorderStroke,
            fontSize: Double = 20.0,
            fontWeight: Long = 4L,
            fontStyleItalic: Boolean = false,
//            letterSpacing: Double = TextUnit.Unspecified,
            shadowColor: MyColorARGB = MyColorARGB("AF000000"),
            shadowOffsetX: Double = 2.0,
            shadowOffsetY: Double = 2.0,
            shadowBlurRadius: Double = 4.0,
//            textAlign: TextAlign? = null,

            sver: Boolean = true,
        ) : RazdelSettingInner(nameItem, color, "${code_name_parent_razdel}_${name_podrazdel}", type_razdel, sver) {
            val TEXT_COLOR by settName(addColor("Цвет текста", textColor))
            val FONT_SIZE by settName(addDoublePoz("Размер шрифта", fontSize))
            val FONT_WEIGHT = addSett(
                InterfaceSettingsFontWeight(
                    code_name_razdel,
                    "FONT_WEIGHT",
                    fontWeight,
                    "Толщина",
                    MyColorARGB("FF40432E")
                )
            )
            val ITALIC by settName(addBoolean("Курсив", fontStyleItalic))
            val SHADOW_COLOR by settName(addColor("Цвет тени", shadowColor))
            val SHADOW_OFFSET_X by settName(addDouble("Тень OffsetX", shadowOffsetX))
            val SHADOW_OFFSET_Y by settName(addDouble("Тень OffsetY", shadowOffsetY))
            val SHADOW_BLUR_RADIUS by settName(addDoublePoz("Тень радиус размытия", shadowBlurRadius))


        }

        open inner class PaddingStyleItemSetting(
            code_name_parent_razdel: String,
            name_podrazdel: String,
            nameItem: String = "Внутренние отступы",
            color: MyColorARGB = MyColorARGB("FF8AAF3E"),
            type_razdel: TypeSaveStyleSet,
            padding_horizontal: Double = 8.0,
            padding_vertical: Double = 1.0,
            enableIndiv: Boolean = false,
            start: Double = 0.0,
            end: Double = 0.0,
            top: Double = 0.0,
            bottom: Double = 0.0,
            sver: Boolean = true,
        ) : RazdelSettingInner(nameItem, color, "${code_name_parent_razdel}_${name_podrazdel}", type_razdel, sver) {
            val PADDING_HORIZONTAL by settName(addDoublePoz("Горизонтальные отступы", padding_horizontal))
            val PADDING_VERTICAL by settName(addDoublePoz("Вертикальные отступы", padding_vertical))
            val PADDING_INDIVIDUALLY by settName(addBoolean("Отступы по отдельности", enableIndiv))
            val START by settName(addDoublePoz("Слева", start))
            val END by settName(addDoublePoz("Справа", end))
            val TOP by settName(addDoublePoz("Сверху", top))
            val BOTTOM by settName(addDoublePoz("Снизу", bottom))
        }

        open inner class MyTextFieldStyle(
            code_name_parent_razdel: String,
            name_podrazdel: String,
            nameSett: String,
            color: MyColorARGB = MyColorARGB("FF6D34B7"),
            sver: Boolean = true,
        ) : RazdelSettingInner(
            nameSett,
            color,
            "${code_name_parent_razdel}_${name_podrazdel}",
            TypeSaveStyleSet.TEXTFIELD,
            sver
        ) {
            val panelFocus by nameRazd { name -> r_addSimplePlateWithShadow(name, "Панель в фокусе") }
            val panelUnfocus by nameRazd { name -> r_addSimplePlateWithShadow(name, "Панель без фокуса") }
            val textMain by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст основной",
                    MyColorARGB.colorMyBorderStroke,
                    16.0
                )
            }
            val textHint by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст подсказки",
                    MyColorARGB.colorMyBorderStrokeCommon,
                    16.0
                )
            }
            val textNamePole by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст названия поля",
                    MyColorARGB.colorMyBorderStroke,
                    12.0
                )
            }
            val textNamePoleFocus by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст названия поля в фокусе",
                    MyColorARGB.colorMyBorderStroke,
                    12.0
                )
            }
            val inner_padding by nameRazd { name -> r_addPadding(name) }
            val START_NAME by settName(addDoublePoz("Отступ названия", 15.0))
            val cursorBrush by nameRazd { name -> r_addBrush(name, "Цвет курсора") }
        }

        open inner class CornerStyleItemSetting(
            nameItem: String,
            code_name_parent_razdel: String,
            name_podrazdel: String,
            color: MyColorARGB = MyColorARGB("FF4D6467"),
            type_razdel: TypeSaveStyleSet,
            enable: Boolean = false,
            commonShape: MyTypeCorner = MyTypeCorner.Round,
            border_radius: Double = 15.0,
            topstartShape: MyTypeCorner = MyTypeCorner.Round,
            topendShape: MyTypeCorner = MyTypeCorner.Round,
            bottomstartShape: MyTypeCorner = MyTypeCorner.Round,
            bottomendShape: MyTypeCorner = MyTypeCorner.Round,
            topstart: Double = 0.0,
            topend: Double = 0.0,
            bottomstart: Double = 0.0,
            bottomend: Double = 0.0,
            sver: Boolean = true,
        ) : RazdelSettingInner(nameItem, color, "${code_name_parent_razdel}_${name_podrazdel}", type_razdel, sver) {
            val BORDER_CORNER_TYPE_COMMON by settName(addCorner("Тип углов", commonShape))
            val BORDER_RADIUS by settName(addDoublePoz("Радиус скругления границы", border_radius))
            val BORDER_CORNER_INDIVIDUALLY by settName(addBoolean("Углы по отдельности", enable))
            val BORDER_CORNER_TYPE_TOPSTART by settName(addCorner("Верхний левый угол", topstartShape))
            val BORDER_CORNER_TOPSTART by settName(addDoublePoz("Верхний левый угол", topstart))
            val BORDER_CORNER_TYPE_TOPEND by settName(addCorner("Верхний правый угол", topendShape))
            val BORDER_CORNER_TOPEND by settName(addDoublePoz("Верхний правый угол", topend))
            val BORDER_CORNER_TYPE_BOTTOMSTART by settName(addCorner("Нижний левый угол", bottomstartShape))
            val BORDER_CORNER_BOTTOMSTART by settName(addDoublePoz("Нижний левый угол", bottomstart))
            val BORDER_CORNER_TYPE_BOTTOMEND by settName(addCorner("Нижний правый угол", bottomendShape))
            val BORDER_CORNER_BOTTOMEND by settName(addDoublePoz("Нижний правый угол", bottomend))
        }

        open inner class BackAndBorderStyleItemSetting(
            nameItem: String,
            code_name_parent_razdel: String,
            name_podrazdel: String,
            color: MyColorARGB = MyColorARGB("FF09465F"),
            type_razdel: TypeSaveStyleSet,
            border_width: Double = 0.5,
            border_width_active: Double = 0.5,
            background_color: MyColorARGB = MyColorARGB.colorMyMainTheme,
            border_color: MyColorARGB = MyColorARGB.colorMyBorderStrokeCommon,
            border_active_color: MyColorARGB = MyColorARGB("AFFF0000"),
            enableGradientBackgr: Boolean = false,
            enableGradientBorder: Boolean = false,
            enableGradientBorderActive: Boolean = false,
            background_angle: Long = 0L,
            border_angle: Long = 0L,
            border_angle_active: Long = 0L,
            background: List<MyColorARGB> = listOf(MyColorARGB.colorBackGr2, MyColorARGB.colorBackGr2),
            border: List<MyColorARGB> = listOf(
                MyColorARGB.colorMyBorderStrokeCommon,
                MyColorARGB.colorMyBorderStrokeCommon
            ),
            borderActive: List<MyColorARGB> = listOf(MyColorARGB.colorRasxodTheme, MyColorARGB.colorRasxodTheme),
            sver: Boolean = true,
        ) : RazdelSettingInner(nameItem, color, "${code_name_parent_razdel}_${name_podrazdel}", type_razdel, sver) {
            val BORDER_WIDTH by settName(addDoublePoz("Ширина границы", border_width))
            val BORDER_WIDTH_ACTIVE by settName(addDoublePoz("Ширина границы(выд.)", border_width_active))
            val BACKGROUND = r_addBrush(
                "BACKGROUND", "Фон",
                mainColor = background_color,
                gradientEnable = enableGradientBackgr,
                gradientAngle = background_angle,
                gradientColor = background,
            )
            val BORDER = r_addBrush(
                "BORDER", "Граница",
                mainColor = border_color,
                gradientEnable = enableGradientBorder,
                gradientAngle = border_angle,
                gradientColor = border,
            )
            val BORDER_ACTIVE = r_addBrush(
                "BORDER_ACTIVE", "Граница(выд.)",
                mainColor = border_active_color,
                gradientEnable = enableGradientBorderActive,
                gradientAngle = border_angle_active,
                gradientColor = borderActive,
            )
        }

        open inner class TextButtonStyleItemSetting(
            nameItem: String,
            code_name_parent_razdel: String,
            name_podrazdel: String,
            color: MyColorARGB = MyColorARGB("FF8F66B0"),
            type_razdel: TypeSaveStyleSet,
            border_width: Double = 0.5,
            elevation_default: Double = 2.0,
            elevation_pressed: Double = 8.0,
            elevation_hovered: Double = 4.0,
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
            val BORDER_WIDTH by settName(addDoublePoz("Ширина границы", border_width))
            val ELEVATION_DEFAULT by settName(addDoublePoz("Высота(обычная)", elevation_default))
            val ELEVATION_PRESSED by settName(addDoublePoz("Высота(при нажатии)", elevation_pressed))
            val ELEVATION_HOVERED by settName(addDoublePoz("Высота(при наведении)", elevation_hovered))
            val shadow by nameRazd { name -> r_addShadowStyle(name, "Стиль тени") }
            val BACKGROUND = r_addBrush(
                "BACKGROUND", "Фон",
                mainColor = background_color,
                gradientEnable = enableGradientBackgr,
                gradientAngle = background_angle,
                gradientColor = background
            )
            val BORDER = r_addBrush(
                "BORDER", "Граница",
                mainColor = border_color,
                gradientEnable = enableGradientBorder,
                gradientAngle = border_angle,
                gradientColor = border
            )
            val CORNER_BUTTON = r_addShape("shape_button", "Форма кнопки")
            val TEXT_BUTTON = r_addTextStyle("text_button", "Стиль текста")
        }

        open inner class MyChekboxStyleSetting(
            nameItem: String,
            code_name_parent_razdel: String,
            name_podrazdel: String,
            color: MyColorARGB = MyColorARGB("FF8F96F0"),
            sver: Boolean = true,
        ) : RazdelSettingInner(
            nameItem,
            color,
            "${code_name_parent_razdel}_${name_podrazdel}",
            TypeSaveStyleSet.NOTSAVE,
            sver
        ) {
            val color_false by settName(addColor("Цвет(false)", MyColorARGB("FFFFFFFF")))
            val color_true by settName(addColor("Цвет(true)", MyColorARGB("FF66FF66")))
            val TEXT_BUTTON by nameRazd { name -> r_addTextStyle(name, "Стиль текста") }
        }

        open inner class RectDiagramColorStyleSetting(
            nameItem: String,
            code_name_parent_razdel: String,
            name_podrazdel: String,
            color: MyColorARGB = MyColorARGB("FFBE8367"),
            type_razdel: TypeSaveStyleSet,
            sver: Boolean = true,
        ) : RazdelSettingInner(nameItem, color, "${code_name_parent_razdel}_${name_podrazdel}", type_razdel, sver) {
            val COLOR_YEAR by settName(addColor("Год", MyColorARGB("FF000000")))
            val COLOR_YEAR_BORDER by settName(addColor("Год (обводка)", MyColorARGB("FFFFFFFF")))
            val COLOR_HOUR by settName(addColor("Часы", MyColorARGB.colorSchetItemText))
            val COLOR_HOUR_BORDER by settName(addColor("Часы (обводка)", MyColorARGB("FFFFFFFF")))
            val COLOR_HOUR_2 by settName(addColor("Часы на оси", MyColorARGB.colorSchetItemText))
            val COLOR_MONTH by settName(addColor("Месяцы", MyColorARGB("FF000000")))
            val COLOR_RAZDELIT by settName(addColor("Полоска между годами", MyColorARGB.colorSchetItemText))
            val COLOR_RECT by settName(addColor("Столбик", MyColorARGB.colorSchetItemText.plusWhite(1.3f)))
            val COLOR_RECT_SELECT by settName(addColor("Столбик (курсор)", MyColorARGB("FFFFFFFF")))
            val COLOR_RECT_BORDER by settName(addColor("Столбик (обводка)", MyColorARGB.colorSchetItemText))
        }

        open inner class TwoRectDiagramColorStyleSetting(
            nameItem: String,
            code_name_parent_razdel: String,
            name_podrazdel: String,
            color: MyColorARGB = MyColorARGB("FFBE8367"),
            type_razdel: TypeSaveStyleSet,
            sver: Boolean = true,
        ) : RazdelSettingInner(nameItem, color, "${code_name_parent_razdel}_${name_podrazdel}", type_razdel, sver) {
            val COLOR_YEAR by settName(addColor("Год", MyColorARGB("FF000000")))
            val COLOR_YEAR_BORDER by settName(addColor("Год (обводка)", MyColorARGB("FFFFFFFF")))
            val COLOR_SUMMA by settName(addColor("Сумма 1", MyColorARGB.colorSchetItemText))
            val COLOR_SUMMA_2 by settName(addColor("Сумма 2", MyColorARGB.colorSchetItemText))
            val COLOR_SUMMA_BORDER by settName(addColor("Сумма 1 (обводка)", MyColorARGB("FFFFFFFF")))
            val COLOR_SUMMA_BORDER_2 by settName(addColor("Сумма 2 (обводка)", MyColorARGB("FFFFFFFF")))
            val COLOR_SUMMA_OS by settName(addColor("Сумма 1 (подпись)", MyColorARGB.colorSchetItemText))
            val COLOR_SUMMA_OS_2 by settName(addColor("Сумма 2 (подпись)", MyColorARGB.colorSchetItemText))
            val COLOR_MONTH by settName(addColor("Месяцы", MyColorARGB("FF000000")))
            val COLOR_RAZDELIT by settName(addColor("Полоска между годами", MyColorARGB.colorSchetItemText))
            val COLOR_RECT by settName(addColor("Столбик", MyColorARGB.colorSchetItemText.plusWhite(1.3f)))
            val COLOR_RECT_SELECT by settName(addColor("Столбик (курсор)", MyColorARGB("FFFFFFFF")))
            val COLOR_RECT_BORDER by settName(addColor("Столбик (обводка)", MyColorARGB.colorSchetItemText))
            val COLOR_RECT_2 by settName(addColor("Столбик2", MyColorARGB.colorSchetItemText.plusWhite(1.3f)))
            val COLOR_RECT_SELECT_2 by settName(addColor("Столбик2 (курсор)", MyColorARGB("FFFFFFFF")))
            val COLOR_RECT_BORDER_2 by settName(addColor("Столбик2 (обводка)", MyColorARGB.colorSchetItemText))
        }

        open inner class GrafikColorStyleSetting(
            nameItem: String,
            code_name_parent_razdel: String,
            name_podrazdel: String,
            color: MyColorARGB = MyColorARGB("FFBE8367"),
            type_razdel: TypeSaveStyleSet,
            sver: Boolean = true,
        ) : RazdelSettingInner(nameItem, color, "${code_name_parent_razdel}_${name_podrazdel}", type_razdel, sver) {
            val COLOR_YEAR by settName(addColor("Год", MyColorARGB("FF000000")))
            val COLOR_YEAR_STROKE by settName(addColor("Год (обводка)", MyColorARGB("FFFFFFFF")))
            val COLOR_SUMMA by settName(addColor("Сумма", MyColorARGB.colorSchetItemText))
            val COLOR_RAZDELIT by settName(addColor("Полоска между годами", MyColorARGB.colorSchetItemText))
            val COLOR_OS by settName(addColor("Ось", MyColorARGB.colorSchetItemText))
            val COLOR_OS_CURSOR by settName(addColor("Ось курсора", MyColorARGB.colorSchetItemText))
            val COLOR_SUMMA_CURSOR by settName(addColor("Сумма курсор", MyColorARGB.colorSchetItemText))
            val COLOR_CURSOR by settName(addColor("Курсор", MyColorARGB("FFFFFFFF")))
            val COLOR_CURSOR_STROKE by settName(addColor("Курсор (обводка)", MyColorARGB("FFFFFFFF")))
            val COLOR_GRAF by settName(addColor("График", MyColorARGB("FF000000")))
            val COLOR_GRAF_STROKE by settName(
                addColor(
                    "График (обводка)",
                    MyColorARGB.colorSchetItemText.plusWhite(1.3f)
                )
            )
        }

        open inner class SimplePlateWithShadowStyleItemSetting(
            nameItem: String,
            code_name_parent_razdel: String,
            name_podrazdel: String,
            color: MyColorARGB = MyColorARGB("FFBE8367"),
            type_razdel: TypeSaveStyleSet,
            border_width: Double = 0.5,
            elevation_default: Double = 0.5,
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
            //            val testDeleg by DelegateName(addBoolean("GRADIENT_ENAB1232LE", "Градиент true", true)) // =
            val BORDER_WIDTH by settName(addDoublePoz("Ширина границы", border_width))
            val shadow by nameRazd { name -> r_addShadowStyle(name, "Стиль тени") }
            val BACKGROUND = r_addBrush(
                "BACKGROUND", "Фон",
                mainColor = background_color,
                gradientEnable = enableGradientBackgr,
                gradientAngle = background_angle,
                gradientColor = background
            )
            val BORDER = r_addBrush(
                "BORDER", "Граница",
                mainColor = border_color,
                gradientEnable = enableGradientBorder,
                gradientAngle = border_angle,
                gradientColor = border
            )
            val SHAPE by nameRazd { name -> r_addShape(name, "Форма") }
        }

        open inner class SimplePlateStyleItemSetting(
            nameItem: String,
            code_name_parent_razdel: String,
            name_podrazdel: String,
            color: MyColorARGB = MyColorARGB("FFBE8367"),
            type_razdel: TypeSaveStyleSet,
            border_width: Double = 0.5,
            elevation_default: Double = 0.5,
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
            //            val testDeleg by DelegateName(addBoolean("GRADIENT_ENAB1232LE", "Градиент true", true)) // =
            val BORDER_WIDTH by settName(addDoublePoz("Ширина границы", border_width))
            val BACKGROUND = r_addBrush(
                "BACKGROUND", "Фон",
                mainColor = background_color,
                gradientEnable = enableGradientBackgr,
                gradientAngle = background_angle,
                gradientColor = background
            )
            val BORDER = r_addBrush(
                "BORDER", "Граница",
                mainColor = border_color,
                gradientEnable = enableGradientBorder,
                gradientAngle = border_angle,
                gradientColor = border
            )
            val SHAPE by nameRazd { name -> r_addShape(name, "Форма") }
        }

        open inner class DropDownMenuStyleSetting(
            nameItem: String,
            code_name_parent_razdel: String,
            name_podrazdel: String,
            color: MyColorARGB = MyColorARGB("FF9EC347"),
            type_razdel: TypeSaveStyleSet,
            border_width: Double = 0.5,
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
            val textStyle by nameRazd { name -> r_addTextStyle(name, "Стиль текста") }
            val inner_padding by nameRazd { name -> r_addPadding(name) }

            //            val testDeleg by DelegateName(addBoolean("GRADIENT_ENAB1232LE", "Градиент true", true)) // =
            val BORDER_WIDTH by settName(addDoublePoz("Ширина границы", border_width))
            val verticalPadding by settName(addDoublePoz("Вертикальные отступы", 5.0))
            val BACKGROUND by nameRazd { name ->
                r_addBrush(
                    name, "Фон",
                    mainColor = background_color,
                    gradientEnable = enableGradientBackgr,
                    gradientAngle = background_angle,
                    gradientColor = background
                )
            }
            val BACKGROUND_HOVERED by nameRazd { name ->
                r_addBrush(
                    name,
                    "Фон при наведении",
                    mainColor = background_color,
                    gradientEnable = enableGradientBackgr,
                    gradientAngle = background_angle,
                    gradientColor = background
                )
            }
            val BORDER by nameRazd { name ->
                r_addBrush(
                    name, "Граница",
                    mainColor = border_color,
                    gradientEnable = enableGradientBorder,
                    gradientAngle = border_angle,
                    gradientColor = border
                )
            }
            val SHAPE by nameRazd { name -> r_addShape(name, "Форма") }
        }

        open inner class ComboBoxStyleSetting(
            nameItem: String,
            code_name_parent_razdel: String,
            name_podrazdel: String,
            color: MyColorARGB = MyColorARGB("FF5E8387"),
            type_razdel: TypeSaveStyleSet,
            sver: Boolean = true,
        ) : RazdelSettingInner(nameItem, color, "${code_name_parent_razdel}_${name_podrazdel}", type_razdel, sver) {
            val textStyle by nameRazd { name -> r_addTextStyle(name, "Стиль текста") }
            val panel by nameRazd { name -> r_addSimplePlateWithShadow(name, "Основной элемент - панель") }
            val dropdownMenu by nameRazd { name -> r_addDropDownMenu(name, "Выпадающий список") }
            val color_open_butt by settName(addColor("Цвет кнопки открытия", MyColorARGB.colorMyBorderStroke))
        }

        open inner class ToggleButtonStyleItemSetting(
            nameItem: String,
            code_name_parent_razdel: String,
            name_podrazdel: String,
            color: MyColorARGB = MyColorARGB("FFBE8367"),
            type_razdel: TypeSaveStyleSet,
            border_width: Double = 0.5,
            elevation_default: Double = 0.5,
            elevation_pressed: Double = 0.5,
            elevation_hovered: Double = 0.5,
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
            val COLOR_FALSE by settName(addColor("Цвет false", MyColorARGB("FFFFFFFF")))
            val COLOR_TRUE by settName(addColor("Цвет true", MyColorARGB("FFFF0000").plusDark(0.9f)))
            val inner_padding = r_addPadding("inner_padding")
            val BORDER_WIDTH by settName(addDoublePoz("Ширина границы", border_width))
            val ELEVATION_DEFAULT by settName(addDoublePoz("Высота(обычная)", elevation_default))
            val ELEVATION_PRESSED by settName(addDoublePoz("Высота(при нажатии)", elevation_pressed))
            val ELEVATION_HOVERED by settName(addDoublePoz("Высота(при наведении)", elevation_hovered))
            val shadow by nameRazd { name -> r_addShadowStyle(name, "Стиль тени") }
            val BACKGROUND = r_addBrush(
                "BACKGROUND", "Фон",
                mainColor = background_color,
                gradientEnable = enableGradientBackgr,
                gradientAngle = background_angle,
                gradientColor = background
            )
            val BACKGROUND_TRUE by nameRazd { name ->
                r_addBrush(
                    name, "Фон (true)",
                    mainColor = background_color,
                    gradientEnable = enableGradientBackgr,
                    gradientAngle = background_angle,
                    gradientColor = background
                )
            }
            val BORDER = r_addBrush(
                "BORDER", "Граница",
                mainColor = border_color,
                gradientEnable = enableGradientBorder,
                gradientAngle = border_angle,
                gradientColor = border
            )
            val BORDER_TRUE by nameRazd { name ->
                r_addBrush(
                    name, "Граница (true)",
                    mainColor = background_color,
                    gradientEnable = enableGradientBackgr,
                    gradientAngle = background_angle,
                    gradientColor = background
                )
            }
            val CORNER_BUTTON = r_addShape("shape_button", "Форма кнопки")
        }

        open inner class IconButtonStyleItemSetting(
            nameItem: String,
            code_name_parent_razdel: String,
            name_podrazdel: String,
            color: MyColorARGB = MyColorARGB("FFBE8367"),
            type_razdel: TypeSaveStyleSet,
            border_width: Double = 0.5,
            elevation_default: Double = 2.0,
            elevation_pressed: Double = 6.0,
            elevation_hovered: Double = 4.0,
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
            val COLOR by settName(addColor("Цвет иконки", MyColorARGB("FFFFFFFF")))
            val inner_padding = r_addPadding("inner_padding")
            val BORDER_WIDTH by settName(addDoublePoz("Ширина границы", border_width))
            val ELEVATION_DEFAULT by settName(addDoublePoz("Высота(обычная)", elevation_default))
            val ELEVATION_PRESSED by settName(addDoublePoz("Высота(при нажатии)", elevation_pressed))
            val ELEVATION_HOVERED by settName(addDoublePoz("Высота(при наведении)", elevation_hovered))
            val shadow by nameRazd { name -> r_addShadowStyle(name, "Стиль тени") }
            val BACKGROUND = r_addBrush(
                "BACKGROUND", "Фон",
                mainColor = background_color,
                gradientEnable = enableGradientBackgr,
                gradientAngle = background_angle,
                gradientColor = background
            )
            val BORDER = r_addBrush(
                "BORDER", "Граница",
                mainColor = border_color,
                gradientEnable = enableGradientBorder,
                gradientAngle = border_angle,
                gradientColor = border
            )
            val CORNER_BUTTON = r_addShape("shape_button", "Форма кнопки")
        }

        open inner class IconButtonWithoutBorderStyleSetting(
            nameItem: String,
            code_name_parent_razdel: String,
            name_podrazdel: String,
            color: MyColorARGB = MyColorARGB("FFBE8367"),
            type_razdel: TypeSaveStyleSet,
            elevation_default: Double = 2.0,
            elevation_pressed: Double = 6.0,
            elevation_hovered: Double = 4.0,
            sver: Boolean = true,
        ) : RazdelSettingInner(nameItem, color, "${code_name_parent_razdel}_${name_podrazdel}", type_razdel, sver) {
            val COLOR by settName(addColor("Цвет иконки", MyColorARGB("FFFFFFFF")))
            val ELEVATION_DEFAULT by settName(addDoublePoz("Высота(обычная)", elevation_default))
            val ELEVATION_PRESSED by settName(addDoublePoz("Высота(при нажатии)", elevation_pressed))
            val ELEVATION_HOVERED by settName(addDoublePoz("Высота(при наведении)", elevation_hovered))
            val shadow by nameRazd { name -> r_addShadowStyle(name, "Стиль тени") }
        }

        open inner class ToggleTextButtonStyleItemSetting(
            nameItem: String,
            code_name_parent_razdel: String,
            name_podrazdel: String,
            color: MyColorARGB = MyColorARGB("FFBE8367"),
            type_razdel: TypeSaveStyleSet,
            border_width: Double = 0.5,
            elevation_default: Double = 0.5,
            elevation_pressed: Double = 0.5,
            elevation_hovered: Double = 0.5,
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
        ) : ToggleButtonStyleItemSetting(
            nameItem,
            code_name_parent_razdel,
            name_podrazdel,
            color,
            type_razdel,
            border_width,
            elevation_default,
            elevation_pressed,
            elevation_hovered,
            background_color,
            border_color,
            enableGradientBackgr,
            enableGradientBorder,
            background_angle,
            border_angle,
            background,
            border,
            sver
        ) {
            val styleText by nameRazd { name ->
                r_addTextStyle(
                    name,
                    "Текст кнопки",
                    MyColorARGB.colorMyBorderStroke,
                    20.0
                )
            }
        }

        open inner class ButtonSeekBarStyleItemSetting(
            nameItem: String,
            code_name_parent_razdel: String,
            name_podrazdel: String,
            color: MyColorARGB = MyColorARGB("FF885982"),
            type_razdel: TypeSaveStyleSet,
            border_width: Double = 0.5,
            elevation_default: Double = 0.5,
            elevation_pressed: Double = 0.5,
            elevation_hovered: Double = 0.5,
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
            cornerInd: Boolean = false,
            sver: Boolean = true,
        ) : TextButtonStyleItemSetting(
            nameItem,
            code_name_parent_razdel,
            name_podrazdel,
            color,
            type_razdel,
            border_width,
            elevation_default,
            elevation_pressed,
            elevation_hovered,
            background_color,
            border_color,
            enableGradientBackgr,
            enableGradientBorder,
            background_angle,
            border_angle,
            background,
            border,
            sver
        ) {
            val CORNER_IND_ENABLE by settName(addBoolean("Углы на каждой кнопке", cornerInd))
            val BACKGROUND_ACTIVE = r_addBrush(
                "BACKGROUND_ACTIVE", "Фон(выд.)",
                mainColor = background_color,
                gradientEnable = enableGradientBackgr,
                gradientAngle = background_angle,
                gradientColor = background
            )
            val BORDER_ACTIVE = r_addBrush(
                "BORDER_ACTIVER", "Граница(выд.)",
                mainColor = border_color,
                gradientEnable = enableGradientBorder,
                gradientAngle = border_angle,
                gradientColor = border
            )
        }

        open inner class CommonBoxOpisStyleItemSetting(
            nameItem: String,
            code_name_parent_razdel: String,
            name_podrazdel: String,
            color: MyColorARGB = MyColorARGB("FF468F45"),
            type_razdel: TypeSaveStyleSet,
            elevation_card: Double = 8.0,
            sver: Boolean = true,
        ) : RazdelSettingInner(nameItem, color, "${code_name_parent_razdel}_${name_podrazdel}", type_razdel, sver) {

            val shadow by nameRazd { name -> r_addShadowStyle(name, "Стиль тени") }
            val outer_padding = r_addPadding("outer_padding", "Внешние отступы")
            val inner_padding = r_addPadding("inner_padding")
            val corner = r_addShape("corner", "Форма")
            val back_and_border = add2(
                BackAndBorderStyleItemSetting(
                    "Фон и границы",
                    code_name_razdel,
                    name_podrazdel = "back_and_border",
                    type_razdel = TypeSaveStyleSet.BACKANDBORDER
                )
            )
            val mainText = r_addTextStyle("Main_text", "Стиль заголовка")
        }

        open inner class ComplexOpisFontSizeSetSetting(
            nameItem: String,
            code_name_parent_razdel: String,
            name_podrazdel: String,
        ) : RazdelSettingInner(
            nameItem,
            MyColorARGB("FF96AFA5"),
            "${code_name_parent_razdel}_${name_podrazdel}",
            TypeSaveStyleSet.FULLPANEL,
            true
        ) {
            val fontSize1 by settName(addDoublePoz("Размер шрифта - самый маленький", 10.0))
            val fontSize2 by settName(addDoublePoz("Размер шрифта - маленький", 13.0))
            val fontSize3 by settName(addDoublePoz("Размер шрифта - средний", 16.0))
            val fontSize4 by settName(addDoublePoz("Размер шрифта - большой", 20.0))
            val fontSize5 by settName(addDoublePoz("Размер шрифта - самый большой", 24.0))
        }

        open inner class ComplexOpisColorSetSetting(
            nameItem: String,
            code_name_parent_razdel: String,
            name_podrazdel: String,
        ) : RazdelSettingInner(
            nameItem,
            MyColorARGB("FF96AF65"),
            "${code_name_parent_razdel}_${name_podrazdel}",
            TypeSaveStyleSet.FULLPANEL,
            true
        ) {
            val color1 by settName(addColor("Цвет 1", MyColorARGB.colorMyBorderStroke))
            val color2 by settName(addColor("Цвет 2", MyColorARGB.colorMyBorderStrokeCommon))
            val color3 by settName(addColor("Цвет 3", MyColorARGB.colorSchetItem))
            val color4 by settName(addColor("Цвет 4", MyColorARGB.colorSchetTheme))
            val color5 by settName(addColor("Цвет 5", MyColorARGB.colorDoxodItem))
            val color6 by settName(addColor("Цвет 6", MyColorARGB.colorDoxodTheme))
            val color7 by settName(addColor("Цвет 7", MyColorARGB.colorStatTimeSquareTint_00))
            val color8 by settName(addColor("Цвет 8", MyColorARGB.colorStatTimeSquareTint_01))
            val color9 by settName(addColor("Цвет 9", MyColorARGB.colorStatTimeSquareTint_02))
            val color10 by settName(addColor("Цвет 10", MyColorARGB.colorStatTimeSquareTint_03))
            val color11 by settName(addColor("Цвет 11", MyColorARGB.colorStatTint_05))
            val color12 by settName(addColor("Цвет 12", MyColorARGB.colorStatTint_04))
        }

        open inner class ComplexOpisStyleSetting(
            nameItem: String,
            code_name_parent_razdel: String,
            name_podrazdel: String,
        ) : RazdelSettingInner(
            nameItem,
            MyColorARGB("FF66AF65"),
            "${code_name_parent_razdel}_${name_podrazdel}",
            TypeSaveStyleSet.FULLPANEL,
            true
        ) {
            val plateView by nameRazd { name -> r_addSimplePlateWithShadow(name, "Панель отображения") }
            val plateEdit by nameRazd { name -> r_addSimplePlateWithShadow(name, "Панель редактирования") }
            val plateEditButt by nameRazd { name ->
                r_addSimplePlateWithShadow(
                    name,
                    "Панель с кнопками стиля текста "
                )
            }
            val plateLink by nameRazd { name -> r_addSimplePlateWithShadow(name, "Панель ссылки") }

            val plateCheckboxFalse by nameRazd { name -> r_addSimplePlateWithShadow(name, "Плитка чекбокса (false)") }
            val plateCheckboxTrue by nameRazd { name -> r_addSimplePlateWithShadow(name, "Плитка чекбокса (true)") }
            val plateCheckboxFail by nameRazd { name ->
                r_addSimplePlateWithShadow(
                    name,
                    "Плитка чекбокса (Провалено)"
                )
            }
            val plateCheckboxQuestion by nameRazd { name ->
                r_addSimplePlateWithShadow(
                    name,
                    "Плитка чекбокса (Вопрос)"
                )
            }
            val plateCheckboxPriority by nameRazd { name ->
                r_addSimplePlateWithShadow(
                    name,
                    "Плитка чекбокса (Внимание)"
                )
            }

            val outer_padding_text by nameRazd { name -> r_addPadding(name, "Внешние отступы текста") }
            val outer_padding by nameRazd { name -> r_addPadding(name, "Внешние отступы") }
            val inner_padding by nameRazd { name -> r_addPadding(name) }
            val inner_padding_edit by nameRazd { name -> r_addPadding(name, "Внутренние отступы поля редактирования") }
            val inner_padding_link by nameRazd { name -> r_addPadding(name, "Внутренние отступы плитки ссылки") }

            val baseText by nameRazd { name -> r_addTextStyle(name, "Базовый стиль текста") }
            val colorCheckboxFalse by settName(addColor("Цвет чекбокса (false)", MyColorARGB.colorMyBorderStrokeCommon))
            val colorCheckboxTrue by settName(addColor("Цвет чекбокса (true)", MyColorARGB("FF40FF40")))
            val colorCheckboxFail by settName(addColor("Цвет чекбокса (Провалено)", MyColorARGB("FF40FF40")))
            val colorCheckboxQuestion by settName(addColor("Цвет чекбокса (Вопрос)", MyColorARGB("FF40FF40")))
            val colorCheckboxPriority by settName(addColor("Цвет чекбокса (Внимание)", MyColorARGB("FF40FF40")))
            val colorArrow by settName(addColor("Цвет стрелок сортировки", MyColorARGB.colorMyBorderStroke))
            val colorToggleTextFalse by settName(
                addColor(
                    "Цвет кнопок КЖ (false)",
                    MyColorARGB.colorMyBorderStrokeCommon
                )
            )
            val colorToggleTextTrue by settName(addColor("Цвет кнопок КЖ (true)", MyColorARGB.colorMyBorderStroke))
            val colorIconShablon by settName(addColor("Цвет иконок шаблонов", MyColorARGB.colorMyBorderStroke))

            val brushBoundImage by nameRazd { name ->
                r_addBrush(
                    name,
                    "Граница изображения",
                    MyColorARGB.colorMyBorderStrokeCommon
                )
            }
            val brushBoundImageActive by nameRazd { name ->
                r_addBrush(
                    name,
                    "Граница изображения (выд.)",
                    MyColorARGB.colorMyBorderStroke
                )
            }

            val textFieldOpis by nameRazd { name -> r_addTextField(name, "Поле редактирования текста") }
            val textFieldLink by nameRazd { name -> r_addTextField(name, "Поле редактирования ссылки") }
            val buttAddFieldOpis by nameRazd { name -> r_addTextButton(name, "Текстовая кнопка добавления") }
            val buttIconAddFieldOpis by nameRazd { name -> r_addIconButton(name, "Кнопка добавления с иконкой") }

            val buttIconDelItem by nameRazd { name -> r_addIconButtonWithoutBorder(name, "Кнопка удаления айтема") }
            val buttIconDelImage by nameRazd { name ->
                r_addIconButtonWithoutBorder(
                    name,
                    "Кнопка удаления изображения"
                )
            }
            val buttIconDelShablon by nameRazd { name -> r_addIconButtonWithoutBorder(name, "Кнопка удаления шаблона") }
            val buttIconSaveShablon by nameRazd { name ->
                r_addIconButtonWithoutBorder(
                    name,
                    "Кнопка добавления шаблонов"
                )
            }
            val buttIconLink by nameRazd { name -> r_addIconButtonWithoutBorder(name, "Кнопка ссылки") }
            val shadowImage by nameRazd { name -> r_addShadowStyle(name, "Стиль тени изображений") }

            val comboBoxShablon by nameRazd { name -> r_addComboBox(name, "Список шаблонов") }
            val comboBoxColor by nameRazd { name -> r_addComboBox(name, "Список цветов") }
            val comboBoxFont by nameRazd { name -> r_addComboBox(name, "Список размеров шрифтов") }
            val comboBoxSizeImage by nameRazd { name -> r_addComboBox(name, "Список размеров изображений") }

            val fontSet by nameRazd { name ->
                add2(
                    ComplexOpisFontSizeSetSetting(
                        "Набор размеров шрифтов",
                        code_name_razdel,
                        name
                    )
                )
            }
            val colorSet by nameRazd { name ->
                add2(
                    ComplexOpisColorSetSetting(
                        "Набор цветов",
                        code_name_razdel,
                        name
                    )
                )
            }
        }

        open inner class CommonBoxOpisWithButtStyleItemSetting(
            nameItem: String,
            code_name_parent_razdel: String,
            name_podrazdel: String,
            color: MyColorARGB = MyColorARGB("FF468F45"),
            colorButton: MyColorARGB = MyColorARGB.colorMyBorderStroke,
            type_razdel: TypeSaveStyleSet,
            elevation_button: Double = 8.0,
            elevation_card: Double = 8.0,
            sver: Boolean = true,
        ) : CommonBoxOpisStyleItemSetting(
            nameItem,
            code_name_parent_razdel,
            name_podrazdel,
            color,
            type_razdel,
            elevation_card,
            sver
        ) {
            val ELEVATION_BUTTON by settName(addDoublePoz("Высота кнопки(-> тень)", elevation_button))
            val COLOR_TEXT_BUTTON by settName(addColor("Цвет кнопки", colorButton))
        }

        open inner class CommonStyleItemSetting(
            nameItem: String,
            code_name_razdel: String,
            color: MyColorARGB = MyColorARGB("FF8E8367"),
            type_razdel: TypeSaveStyleSet,
            elevation_card: Double = 8.0,
            sver: Boolean = true,
        ) : RazdelSettingInner(nameItem, color, code_name_razdel, type_razdel, sver) {

            val ELEVATION_CARD by settName(addDoublePoz("Высота(-> тень)", elevation_card))
            val outer_padding = r_addPadding("outer_padding", "Внешние отступы")
            val inner_padding = r_addPadding("inner_padding")
            val corner = r_addShape("corner", "Форма")
            val back_and_border = add2(
                BackAndBorderStyleItemSetting(
                    "Фон и границы",
                    code_name_razdel,
                    name_podrazdel = "back_and_border",
                    type_razdel = TypeSaveStyleSet.BACKANDBORDER
                )
            )
            val mainText = r_addTextStyle("Main_text", "Стиль заголовка")
            val menuButt = r_addTextButton("menuButton", "Кнопка меню \"⋮\"")
            val shadow by nameRazd { name -> r_addShadowStyle(name, "Стиль тени") }
            val dropdown by nameRazd { name -> r_addDropDownMenu(name, "Контекстное меню") }
        }

        open inner class StyleItemSetting(
            nameItem: String,
            code_name_item: String,
            color: MyColorARGB = MyColorARGB("FF8E8367"),
            type_razdel: TypeSaveStyleSet,
            elevation_card: Double = 8.0,
            /**
             * Определяет, устанавливается ли этот стиль при создании базы или по умолчанию он отключен.
             * */
            active: Boolean = false,
            sver: Boolean = true
        ) : CommonStyleItemSetting(
            nameItem,
            code_name_item,
            color,
            type_razdel,
            elevation_card,
            sver
        ) {
            val ACTIVETED_ITEM_STYLE = addSett(
                InterfaceSettingsBoolean(
                    code_name_item,
                    "ACTIVETED_ITEM_STYLE",
                    active,
                    "Использовать этот стиль",
                    MyColorARGB("FF695F48")
                ),
                0
            )

        }

        open inner class ItemStyleWithOpisSetting(
            nameItem: String,
            code_name_item: String,
            color: MyColorARGB = MyColorARGB("FF8E8367"),
            type_razdel: TypeSaveStyleSet,
            elevation_card: Double = 8.0,
            /**
             * Определяет, устанавливается ли этот стиль при создании базы или по умолчанию он отключен.
             * */
            active: Boolean = false,
            sver: Boolean = true
        ) : StyleItemSetting(
            nameItem,
            code_name_item,
            color,
            type_razdel,
            elevation_card,
            active,
            sver
        ) {
            val OPISANIE = add2(
                CommonBoxOpisWithButtStyleItemSetting(
                    "Стиль описания",
                    code_name_item,
                    "opis_style",
                    type_razdel = TypeSaveStyleSet.COMMONITEM
                )
            )

        }
//        !!!!!!!!!!!!! Не использовать поиск из стоки ниже, т.к. список настроек очень большой это очень ресурсо затратно,
//        при этом тот же поиск по ключу в Map происходит почти моментально. Скорее всего уже основной класс нужно было
//        наследовать не от List, а от Map
//        fun getSetting(code: String): InterfaceSettingsType? = this.find { it.code == code }

        fun startInit() {
            val timeNow = DateTimeTz.nowLocal().localUnix()
//            println("timeNow = ${timeNow}")
            table.getListFromBase().let {
//                println("timeNow 1 = ${DateTimeTz.nowLocal().localUnix() - timeNow}")
                it.let {
                    val checkList = this.map { it.code }.toMutableList()
//                    println("timeNow 1.5 = ${DateTimeTz.nowLocal().localUnix() - timeNow}")
                    table.clearFromDeprecated(checkList)
//                    println("timeNow 1.6 = ${DateTimeTz.nowLocal().localUnix() - timeNow}")
                    val tmpMap: Map<String, InterfaceSettingsType> =
                        mutableMapOf<String, InterfaceSettingsType>().apply {
                            this@MySettings.forEach {
                                this.put(it.code, it)
                            }
                        }
//                    println("timeNow 1.7 = ${DateTimeTz.nowLocal().localUnix() - timeNow}")
                    it.forEach { conv ->
//                        this.getSetting
                        tmpMap.get(conv.code_name)?.let {
                            it.updateValue(conv.long, conv.double, conv.string)
                            checkList.remove(it.code)
                        }
                    }
//                    println("timeNow 2 = ${DateTimeTz.nowLocal().localUnix() - timeNow}")
                    table.transaction {
                        checkList.forEach {
                            tmpMap.get(it)?.let {
                                table.insert(it.code, it.defLong, it.defDouble, it.defString)
                            }
                        }
                    }
                }
//                println("timeNow 3 = ${DateTimeTz.nowLocal().localUnix() - timeNow}")
//                this.clearList()
            }
/*
            table.getListFromBase().map {
                ConvertInterfaceSetting(this.getSetting(it.code_name), it)
            }.let {
                println("timeNow 1 = ${DateTimeTz.nowLocal().localUnix() - timeNow}")
                it.let {
                    val checkList = this.map { it.code }.toMutableList()
                    table.clearFromDeprecated(checkList)
                    it.forEach { conv ->
                        conv.obj?.let {
                            it.updateValue(conv.value.long, conv.value.double, conv.value.string)
                            checkList.remove(it.code)
                        }
                    }
                    println("timeNow 2 = ${DateTimeTz.nowLocal().localUnix() - timeNow}")
                    table.transaction {
                        checkList.forEach {
                            this.getSetting(it)?.let {
                                table.insert(it.code, it.defLong, it.defDouble, it.defString)
                            }
                        }
                    }
                }
                println("timeNow 3 = ${DateTimeTz.nowLocal().localUnix() - timeNow}")
//                this.clearList()
            }
*/
        }

        fun refreshValueFromBase(list: List<SelectLoadCommon>) {
            val timeNow2 = DateTimeTz.nowLocal().localUnix()
//            println("timeNow2 = ${timeNow2}")
            val tmpMap: Map<String, InterfaceSettingsType> =
                mutableMapOf<String, InterfaceSettingsType>().apply {
                    this@MySettings.forEach {
                        this.put(it.code, it)
                    }
                }
//            println("timeNow2 1 = ${DateTimeTz.nowLocal().localUnix() - timeNow2}")
            list.forEach { conv ->
                tmpMap.get(conv.codename)?.updateValue(conv.intparam, conv.doubleparam, conv.stringparam)
//                tmpMap.get(conv.code_name)?.updateValue(conv.long, conv.double, conv.string)
            }
//            println("timeNow2 1 = ${DateTimeTz.nowLocal().localUnix() - timeNow2}")
        }

        fun toDefault() {
            val tmpMap: Map<String, InterfaceSettingsType> =
                mutableMapOf<String, InterfaceSettingsType>().apply {
                    this@MySettings.forEach {
                        this.put(it.code, it)
                    }
                }
            table.getListFromBase().map {
                ConvertInterfaceSetting(tmpMap.get(it.code_name), it)
            }.let {
                it.let {
                    val checkList = this.map { it.code }.toMutableList()
                    table.clearFromDeprecated(listOf())
                    table.transaction {
                        checkList.forEach {
                            tmpMap.get(it)?.let {
                                table.insert(it.code, it.defLong, it.defDouble, it.defString)
                            }
                        }
                    }
                }
            }
            startInit()
        }
    }

    private data class ConvertInterfaceSetting(val obj: InterfaceSettingsType?, val value: ItemInterfaceSetting)

}

