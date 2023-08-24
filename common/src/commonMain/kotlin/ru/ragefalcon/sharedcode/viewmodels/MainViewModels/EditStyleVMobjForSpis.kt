package ru.ragefalcon.sharedcode.viewmodels.MainViewModels

import ru.ragefalcon.sharedcode.Database
import ru.ragefalcon.sharedcode.common.Color_library
import ru.ragefalcon.sharedcode.common.Spis_save_set_style
import ru.ragefalcon.sharedcode.common.Spis_set_color_library
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.models.data.ItemColorLibrary
import ru.ragefalcon.sharedcode.models.data.ItemSaveSetStyle
import ru.ragefalcon.sharedcode.models.data.ItemSetColorLibrary
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.Interface.TypeSaveStyleSet
import ru.ragefalcon.sharedcode.viewmodels.UniAdapters.UniConvertQueryAdapter

class EditStyleVMobjForSpis(private val mDB: Database) {
    val spisColorLibrary = UniConvertQueryAdapter<Color_library, ItemColorLibrary>() {
        ItemColorLibrary(
            it._id,
            MyColorARGB(it.color)
        )
    }.apply {
        this.updateQuery(mDB.colorLibraryQueries.selectColorLibrary(-1L))
    }

    val spisSetColorLibrary = UniConvertQueryAdapter<Spis_set_color_library, ItemSetColorLibrary>() {
        ItemSetColorLibrary(it._id, it.name)
    }.apply {
        this.updateQuery(mDB.spisSetColorLibraryQueries.selectSetColorLibrary())
    }

    val spisSaveSetStyleFull = UniConvertQueryAdapter<Spis_save_set_style, ItemSaveSetStyle>() {
        ItemSaveSetStyle(it._id, it.name, TypeSaveStyleSet.getType(it._id) ?: TypeSaveStyleSet.NOTSAVE)
    }.apply {
        this.updateQuery(mDB.spisSaveSetStyleQueries.selectSaveSetStyleFull(TypeSaveStyleSet.FULL.id))
    }

    val spisSaveSetStyleTextStyle = UniConvertQueryAdapter<Spis_save_set_style, ItemSaveSetStyle>() {
        ItemSaveSetStyle(it._id, it.name, TypeSaveStyleSet.getType(it._id) ?: TypeSaveStyleSet.NOTSAVE)
    }.apply {
        this.updateQuery(mDB.spisSaveSetStyleQueries.selectSaveSetStyleTextStyle(TypeSaveStyleSet.TEXTSTYLE.id))
    }

    val spisSaveSetStyleCommonItem = UniConvertQueryAdapter<Spis_save_set_style, ItemSaveSetStyle>() {
        ItemSaveSetStyle(it._id, it.name, TypeSaveStyleSet.getType(it._id) ?: TypeSaveStyleSet.NOTSAVE)
    }.apply {
        this.updateQuery(mDB.spisSaveSetStyleQueries.selectSaveSetStyleCommonItem(TypeSaveStyleSet.COMMONITEM.id))
    }

    val spisSaveSetStyleTextButton = UniConvertQueryAdapter<Spis_save_set_style, ItemSaveSetStyle>() {
        ItemSaveSetStyle(it._id, it.name, TypeSaveStyleSet.getType(it._id) ?: TypeSaveStyleSet.NOTSAVE)
    }.apply {
        this.updateQuery(mDB.spisSaveSetStyleQueries.selectSaveSetStyleTextButton(TypeSaveStyleSet.TEXTBUTTON.id))
    }

    val spisSaveSetStyleFullPanel = UniConvertQueryAdapter<Spis_save_set_style, ItemSaveSetStyle>() {
        ItemSaveSetStyle(it._id, it.name, TypeSaveStyleSet.getType(it._id) ?: TypeSaveStyleSet.NOTSAVE)
    }.apply {
        this.updateQuery(mDB.spisSaveSetStyleQueries.selectSaveSetStyleFullPanel(TypeSaveStyleSet.FULLPANEL.id))
    }

    val spisSaveSetStyleItemGradient = UniConvertQueryAdapter<Spis_save_set_style, ItemSaveSetStyle>() {
        ItemSaveSetStyle(it._id, it.name, TypeSaveStyleSet.getType(it._id) ?: TypeSaveStyleSet.NOTSAVE)
    }.apply {
        this.updateQuery(mDB.spisSaveSetStyleQueries.selectSaveSetStyleBackAndBorder(TypeSaveStyleSet.BACKANDBORDER.id))
    }

    val spisSaveSetStyleBrush = UniConvertQueryAdapter<Spis_save_set_style, ItemSaveSetStyle>() {
        ItemSaveSetStyle(it._id, it.name, TypeSaveStyleSet.getType(it._id) ?: TypeSaveStyleSet.NOTSAVE)
    }.apply {
        this.updateQuery(mDB.spisSaveSetStyleQueries.selectSaveSetStyleBrush(TypeSaveStyleSet.BRUSH.id))
    }

    val spisSaveSetStyleShape = UniConvertQueryAdapter<Spis_save_set_style, ItemSaveSetStyle>() {
        ItemSaveSetStyle(it._id, it.name, TypeSaveStyleSet.getType(it._id) ?: TypeSaveStyleSet.NOTSAVE)
    }.apply {
        this.updateQuery(mDB.spisSaveSetStyleQueries.selectSaveSetStyleShape(TypeSaveStyleSet.SHAPE.id))
    }

    val spisSaveSetStylePadding = UniConvertQueryAdapter<Spis_save_set_style, ItemSaveSetStyle>() {
        ItemSaveSetStyle(it._id, it.name, TypeSaveStyleSet.getType(it._id) ?: TypeSaveStyleSet.NOTSAVE)
    }.apply {
        this.updateQuery(mDB.spisSaveSetStyleQueries.selectSaveSetStylePadding(TypeSaveStyleSet.PADDING.id))
    }

    val spisSaveSetStyleShadow = UniConvertQueryAdapter<Spis_save_set_style, ItemSaveSetStyle>() {
        ItemSaveSetStyle(it._id, it.name, TypeSaveStyleSet.getType(it._id) ?: TypeSaveStyleSet.NOTSAVE)
    }.apply {
        this.updateQuery(mDB.spisSaveSetStyleQueries.selectSaveSetStyleShadow(TypeSaveStyleSet.SHADOW.id))
    }

    val spisSaveSetStyleDropdownMenu = UniConvertQueryAdapter<Spis_save_set_style, ItemSaveSetStyle>() {
        ItemSaveSetStyle(it._id, it.name, TypeSaveStyleSet.getType(it._id) ?: TypeSaveStyleSet.NOTSAVE)
    }.apply {
        this.updateQuery(mDB.spisSaveSetStyleQueries.selectSaveSetStyleDropdown(TypeSaveStyleSet.DROPDOWN.id))
    }

    val spisSaveSetStyleComboBox = UniConvertQueryAdapter<Spis_save_set_style, ItemSaveSetStyle>() {
        ItemSaveSetStyle(it._id, it.name, TypeSaveStyleSet.getType(it._id) ?: TypeSaveStyleSet.NOTSAVE)
    }.apply {
        this.updateQuery(mDB.spisSaveSetStyleQueries.selectSaveSetStyleComboBox(TypeSaveStyleSet.COMBOBOX.id))
    }

    val spisSaveSetStyleTextField = UniConvertQueryAdapter<Spis_save_set_style, ItemSaveSetStyle>() {
        ItemSaveSetStyle(it._id, it.name, TypeSaveStyleSet.getType(it._id) ?: TypeSaveStyleSet.NOTSAVE)
    }.apply {
        this.updateQuery(mDB.spisSaveSetStyleQueries.selectSaveSetStyleTextField(TypeSaveStyleSet.TEXTFIELD.id))
    }

}