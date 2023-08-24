package ru.ragefalcon.sharedcode.viewmodels.MainViewModels

import ru.ragefalcon.sharedcode.models.data.ItemSaveSetStyle
import ru.ragefalcon.sharedcode.source.disk.ItrCommObserveObj
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.Interface.TypeSaveStyleSet

class EditStyleVMspis( private val spisVM: EditStyleVMobjForSpis) {
    val spisColorLibrary = ItrCommObserveObj(spisVM.spisColorLibrary.getMyObserveObj())
    val spisSetColorLibrary = ItrCommObserveObj(spisVM.spisSetColorLibrary.getMyObserveObj())
    val spisSaveSetStyleFull = ItrCommObserveObj(spisVM.spisSaveSetStyleFull.getMyObserveObj())
    val spisSaveSetStyleCommonItem = ItrCommObserveObj(spisVM.spisSaveSetStyleCommonItem.getMyObserveObj())
    val spisSaveSetStyleTextStyle = ItrCommObserveObj(spisVM.spisSaveSetStyleTextStyle.getMyObserveObj())
    val spisSaveSetStyleTextButton = ItrCommObserveObj(spisVM.spisSaveSetStyleTextButton.getMyObserveObj())
    val spisSaveSetStyleFullPanel = ItrCommObserveObj(spisVM.spisSaveSetStyleFullPanel.getMyObserveObj())
    val spisSaveSetStyleItemGradient = ItrCommObserveObj(spisVM.spisSaveSetStyleItemGradient.getMyObserveObj())
    val spisSaveSetStyleBrush = ItrCommObserveObj(spisVM.spisSaveSetStyleBrush.getMyObserveObj())
    val spisSaveSetStyleShape = ItrCommObserveObj(spisVM.spisSaveSetStyleShape.getMyObserveObj())
    val spisSaveSetStylePadding = ItrCommObserveObj(spisVM.spisSaveSetStylePadding.getMyObserveObj())
    val spisSaveSetStyleShadow = ItrCommObserveObj(spisVM.spisSaveSetStyleShadow.getMyObserveObj())
    val spisSaveSetStyleDropdownMenu = ItrCommObserveObj(spisVM.spisSaveSetStyleDropdownMenu.getMyObserveObj())
    val spisSaveSetStyleComboBox = ItrCommObserveObj(spisVM.spisSaveSetStyleComboBox.getMyObserveObj())
    val spisSaveSetStyleTextField = ItrCommObserveObj(spisVM.spisSaveSetStyleTextField.getMyObserveObj())
    fun getSpisSaveSetStyle(type: TypeSaveStyleSet): ItrCommObserveObj<List<ItemSaveSetStyle>>? = when (type) {
        TypeSaveStyleSet.NOTSAVE -> null
        TypeSaveStyleSet.FULL -> spisSaveSetStyleFull
        TypeSaveStyleSet.COMMONITEM -> spisSaveSetStyleCommonItem
        TypeSaveStyleSet.TEXTSTYLE -> spisSaveSetStyleTextStyle
        TypeSaveStyleSet.TEXTBUTTON -> spisSaveSetStyleTextButton
        TypeSaveStyleSet.FULLPANEL -> spisSaveSetStyleFullPanel
        TypeSaveStyleSet.BACKANDBORDER -> spisSaveSetStyleItemGradient
        TypeSaveStyleSet.BRUSH -> spisSaveSetStyleBrush
        TypeSaveStyleSet.SHAPE -> spisSaveSetStyleShape
        TypeSaveStyleSet.PADDING -> spisSaveSetStylePadding
        TypeSaveStyleSet.SHADOW -> spisSaveSetStyleShadow
        TypeSaveStyleSet.DROPDOWN -> spisSaveSetStyleDropdownMenu
        TypeSaveStyleSet.COMBOBOX -> spisSaveSetStyleComboBox
        TypeSaveStyleSet.TEXTFIELD -> spisSaveSetStyleTextField
    }
}