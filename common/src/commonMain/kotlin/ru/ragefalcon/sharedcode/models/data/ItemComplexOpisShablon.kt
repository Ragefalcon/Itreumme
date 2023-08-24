package ru.ragefalcon.sharedcode.models.data

import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeOpisBlock

data class ItemComplexOpisShablon(
    val id: Long,
    val name: String,
    val color: Long,
    val fontSize: Long,
    val cursiv: Boolean,
    val bold: Int,
    val many_type: Boolean?,
    val link: String?,
    val sizePreview: Long?,
    val widthLimit: Boolean?,
    val enableText: Boolean?,
    val textBefore: Boolean?,
    val typeOpisBlock: TypeOpisBlock = if (link != null) TypeOpisBlock.link
    else if (many_type != null) TypeOpisBlock.checkbox
    else if (sizePreview != null) TypeOpisBlock.image
    else TypeOpisBlock.simpleText
) {

    constructor(name: String, parent: ItemComplexOpisTextCommon) : this(
        -1L,
        name,
        parent.color,
        parent.fontSize,
        parent.cursiv,
        parent.bold,
        if (parent is ItemComplexOpisCheckbox) parent.many_type else null,
        if (parent is ItemComplexOpisLink) "" else null,
        if (parent is ItemComplexOpisImageGroup) parent.sizePreview else null,
        if (parent is ItemComplexOpisImageGroup) parent.widthLimit else null,
        if (parent is ItemComplexOpisImageGroup) parent.enableText else null,
        if (parent is ItemComplexOpisImageGroup) parent.textBefore else null
    )


    fun getItemOpis(tableName: String, item_id: Long, sort: Long): ItemComplexOpis = when (typeOpisBlock){
        TypeOpisBlock.link -> ItemComplexOpisLink(-1L, tableName, item_id, TypeOpisBlock.link, sort, "", this.color, this.fontSize, this.cursiv, this.bold, "")
        TypeOpisBlock.checkbox ->  ItemComplexOpisCheckbox(-1L, tableName, item_id, TypeOpisBlock.checkbox, sort, "", this.color, this.fontSize, this.cursiv, this.bold, 0L , this.many_type ?: false)
        TypeOpisBlock.image ->  ItemComplexOpisImageGroup(-1L, tableName, item_id, TypeOpisBlock.image, sort, "", this.color, this.fontSize, this.cursiv, this.bold, sizePreview ?: 1L, widthLimit ?: false,enableText ?: false,textBefore ?: false, listOf())
        TypeOpisBlock.simpleText ->  ItemComplexOpisText(-1L, tableName, item_id, TypeOpisBlock.simpleText, sort, "", this.color, this.fontSize, this.cursiv, this.bold)
    }


}
