package ru.ragefalcon.sharedcode.models.data

import ru.ragefalcon.sharedcode.ComplexOpis.*
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeOpisBlock

sealed class ItemComplexOpis {
    abstract val id: Long
    abstract val table_name: String
    abstract val item_id: Long
    abstract val type_block: TypeOpisBlock
    abstract val sort: Long
}

sealed class ItemComplexOpisTextCommon : ItemComplexOpis() {
    abstract val text: String
    abstract val color: Long
    abstract val fontSize: Long
    abstract val cursiv: Boolean
    abstract val bold: Int

    abstract fun myCopy(
        id: Long = this.id,
        table_name: String = this.table_name,
        item_id: Long = this.item_id,
        type_block: TypeOpisBlock = this.type_block,
        sort: Long = this.sort,
        text: String = this.text,
        color: Long = this.color,
        fontSize: Long = this.fontSize,
        cursiv: Boolean = this.cursiv,
        bold: Int = this.bold
    ): ItemComplexOpisTextCommon
}

data class ItemComplexOpisText(
    override val id: Long,
    override val table_name: String,
    override val item_id: Long,
    override val type_block: TypeOpisBlock,
    override val sort: Long,
    override val text: String,
    override val color: Long,
    override val fontSize: Long,
    override val cursiv: Boolean,
    override val bold: Int
) : ItemComplexOpisTextCommon() {
    override fun myCopy(
        id: Long,
        table_name: String,
        item_id: Long,
        type_block: TypeOpisBlock,
        sort: Long,
        text: String,
        color: Long,
        fontSize: Long,
        cursiv: Boolean,
        bold: Int
    ): ItemComplexOpisTextCommon = this.copy(
        id = id,
        table_name = table_name,
        item_id = item_id,
        type_block = type_block,
        sort = sort,
        text = text,
        color = color,
        fontSize = fontSize,
        cursiv = cursiv,
        bold = bold
    )

    constructor(from: SelectComplexOpisTextCommonTable) : this(
        id = from._id,
        table_name = from.table_name,
        item_id = from.item_id,
        type_block = TypeOpisBlock.simpleText,
        sort = from.sort,
        text = from.text,
        color = from.colorNum,
        fontSize = from.font_size,
        cursiv = from.cursive == 1L,
        bold = from.bold.toInt()
    )

    constructor(from: SelectComplexOpisTextCommonWithId) : this(
        id = from._id,
        table_name = from.table_name,
        item_id = from.item_id,
        type_block = TypeOpisBlock.simpleText,
        sort = from.sort,
        text = from.text,
        color = from.colorNum,
        fontSize = from.font_size,
        cursiv = from.cursive == 1L,
        bold = from.bold.toInt()
    )
}


data class ItemComplexOpisCheckbox(
    override val id: Long,
    override val table_name: String,
    override val item_id: Long,
    override val type_block: TypeOpisBlock,
    override val sort: Long,
    override val text: String,
    override val color: Long,
    override val fontSize: Long,
    override val cursiv: Boolean,
    override val bold: Int,
    val checked: Long,
    val many_type: Boolean
) : ItemComplexOpisTextCommon() {

    override fun myCopy(
        id: Long,
        table_name: String,
        item_id: Long,
        type_block: TypeOpisBlock,
        sort: Long,
        text: String,
        color: Long,
        fontSize: Long,
        cursiv: Boolean,
        bold: Int
    ): ItemComplexOpisTextCommon = this.copy(
        id = id,
        table_name = table_name,
        item_id = item_id,
        type_block = type_block,
        sort = sort,
        text = text,
        color = color,
        fontSize = fontSize,
        cursiv = cursiv,
        bold = bold
    )

    /**
     * 0 - пустой чекбокс
     * 1 - галочка в чекбоксе
     * 2 - крестик/провал
     * 3 - вопрос
     * 4 - внимание/воклицательный знак
     * */
    fun nextCheck(): ItemComplexOpisCheckbox = this.copy(
        checked = if (many_type) {
            if (checked + 1 > 4) 0L else checked + 1L
        } else {
            if (checked == 0L) 1L else 0L
        }
    )

    constructor(from: SelectComplexOpisCheckboxCommonTable) : this(
        id = from._id,
        table_name = from.table_name,
        item_id = from.item_id,
        type_block = TypeOpisBlock.checkbox,
        sort = from.sort,
        text = from.text,
        color = from.colorNum,
        fontSize = from.font_size,
        cursiv = from.cursive == 1L,
        bold = from.bold.toInt(),
        checked = from.checked,
        many_type = from.many_type == 1L
    )

    constructor(from: SelectComplexOpisCheckboxCommonWithId) : this(
        id = from._id,
        table_name = from.table_name,
        item_id = from.item_id,
        type_block = TypeOpisBlock.checkbox,
        sort = from.sort,
        text = from.text,
        color = from.colorNum,
        fontSize = from.font_size,
        cursiv = from.cursive == 1L,
        bold = from.bold.toInt(),
        checked = from.checked,
        many_type = from.many_type == 1L
    )
}

data class ItemComplexOpisLink(
    override val id: Long,
    override val table_name: String,
    override val item_id: Long,
    override val type_block: TypeOpisBlock,
    override val sort: Long,
    override val text: String,
    override val color: Long,
    override val fontSize: Long,
    override val cursiv: Boolean,
    override val bold: Int,
    val link: String
) : ItemComplexOpisTextCommon() {

    override fun myCopy(
        id: Long,
        table_name: String,
        item_id: Long,
        type_block: TypeOpisBlock,
        sort: Long,
        text: String,
        color: Long,
        fontSize: Long,
        cursiv: Boolean,
        bold: Int
    ): ItemComplexOpisTextCommon = this.copy(
        id = id,
        table_name = table_name,
        item_id = item_id,
        type_block = type_block,
        sort = sort,
        text = text,
        color = color,
        fontSize = fontSize,
        cursiv = cursiv,
        bold = bold
    )

    constructor(from: SelectComplexOpisLinkCommonTable) : this(
        id = from._id,
        table_name = from.table_name,
        item_id = from.item_id,
        type_block = TypeOpisBlock.link,
        sort = from.sort,
        text = from.text,
        color = from.colorNum,
        fontSize = from.font_size,
        cursiv = from.cursive == 1L,
        bold = from.bold.toInt(),
        link = from.link
    )

    constructor(from: SelectComplexOpisLinkCommonWithId) : this(
        id = from._id,
        table_name = from.table_name,
        item_id = from.item_id,
        type_block = TypeOpisBlock.link,
        sort = from.sort,
        text = from.text,
        color = from.colorNum,
        fontSize = from.font_size,
        cursiv = from.cursive == 1L,
        bold = from.bold.toInt(),
        link = from.link
    )
}

data class ItemComplexOpisImage(
    val id: Long,
    val opis_id: Long,
    val sort: Long,
    val fileTmp: Boolean
)

data class ItemComplexOpisImageGroup(
    override val id: Long,
    override val table_name: String,
    override val item_id: Long,
    override val type_block: TypeOpisBlock,
    override val sort: Long,
    override val text: String,
    override val color: Long,
    override val fontSize: Long,
    override val cursiv: Boolean,
    override val bold: Int,
    val sizePreview: Long,
    val widthLimit: Boolean,
    val enableText: Boolean,
    val textBefore: Boolean,
    val spisImages: List<ItemComplexOpisImage>
) : ItemComplexOpisTextCommon() {

    override fun myCopy(
        id: Long,
        table_name: String,
        item_id: Long,
        type_block: TypeOpisBlock,
        sort: Long,
        text: String,
        color: Long,
        fontSize: Long,
        cursiv: Boolean,
        bold: Int
    ): ItemComplexOpisTextCommon = this.copy(
        id = id,
        table_name = table_name,
        item_id = item_id,
        type_block = type_block,
        sort = sort,
        text = text,
        color = color,
        fontSize = fontSize,
        cursiv = cursiv,
        bold = bold
    )

    constructor(from: SelectComplexOpisImageGroupCommonTable) : this(
        id = from._id,
        table_name = from.table_name,
        item_id = from.item_id,
        type_block = TypeOpisBlock.image,
        sort = from.sort,
        text = from.text,
        color = from.colorNum,
        fontSize = from.font_size,
        cursiv = from.cursive == 1L,
        bold = from.bold.toInt(),
        sizePreview = from.size_preview,
        widthLimit = from.width_limit == 1L,
        enableText = from.enable_text == 1L,
        textBefore = from.text_before == 1L,
        spisImages = from.spis_image.split(',').map { strImg ->
            strImg.split("-").let {
                if (it.size == 2) ItemComplexOpisImage(it[0].toLong(), from._id, it[1].toLong(), false)
                else ItemComplexOpisImage(-1L, -1L, -1L, true)
            }
        }
    )

    constructor(from: SelectComplexOpisImageGroupCommonWithId) : this(
        id = from._id,
        table_name = from.table_name,
        item_id = from.item_id,
        type_block = TypeOpisBlock.image,
        sort = from.sort,
        text = from.text,
        color = from.colorNum,
        fontSize = from.font_size,
        cursiv = from.cursive == 1L,
        bold = from.bold.toInt(),
        sizePreview = from.size_preview,
        widthLimit = from.width_limit == 1L,
        enableText = from.enable_text == 1L,
        textBefore = from.text_before == 1L,
        spisImages = from.spis_image.split(',').map { strImg ->
            strImg.split("-").let {
                if (it.size == 2) ItemComplexOpisImage(it[0].toLong(), from._id, it[1].toLong(), false)
                else ItemComplexOpisImage(-1L, -1L, -1L, true)
            }
        }
    )
}

fun ItemComplexOpis.myCommonCopy( sort: Long? = null, id: Long? = null,  table_name: String? = null, item_id: Long? = null): ItemComplexOpis = when (this) {
    is ItemComplexOpisTextCommon -> when (this) {
        is ItemComplexOpisText -> this.copy(sort = sort ?: this.sort, id = id ?: this.id, item_id = item_id ?: this.item_id, table_name = table_name ?: this.table_name)
        is ItemComplexOpisCheckbox -> this.copy(sort = sort ?: this.sort, id = id ?: this.id, item_id = item_id ?: this.item_id, table_name = table_name ?: this.table_name)
        is ItemComplexOpisImageGroup -> this.copy(sort = sort ?: this.sort, id = id ?: this.id, item_id = item_id ?: this.item_id, table_name = table_name ?: this.table_name)
        is ItemComplexOpisLink -> this.copy(sort = sort ?: this.sort, id = id ?: this.id, item_id = item_id ?: this.item_id, table_name = table_name ?: this.table_name)
    }
}

fun List<ItemComplexOpis>.copy(): List<ItemComplexOpis> = mutableListOf<ItemComplexOpis>()
    .apply {
        this@copy.forEach {
            add(
                when (it) {
                    is ItemComplexOpisTextCommon -> when (it) {
                        is ItemComplexOpisText -> it.copy()
                        is ItemComplexOpisCheckbox -> it.copy()
                        is ItemComplexOpisImageGroup -> it.copy()
                        is ItemComplexOpisLink -> it.copy()
                    }
                }
            )

        }
    }

