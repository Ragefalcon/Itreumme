package ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers

import ru.ragefalcon.sharedcode.Database
import ru.ragefalcon.sharedcode.models.data.*

data class PairId(val item: ItemComplexOpisImage, val newId: Long)

class AddComplexOpisHandler(private var mdb: Database) {

    fun addComplexOpisAfterOtherItem(
        list: List<ItemComplexOpis>,
        withId: ((Long) -> Unit)? = null,
        addItem: () -> Unit
    ): List<PairId> {
        var listNewId = listOf<PairId>()
        mdb.complexOpisQueries.transactionWithResult<Long> {
            addItem()
            return@transactionWithResult mdb.complexOpisQueries.getLastIndex().executeAsOne()
        }.let { plan_id ->
            if (plan_id > 0) {
                withId?.invoke(plan_id)
                listNewId = addComplexOpis(plan_id, list)
            }
        }
        return listNewId
    }

    fun addComplexOpis(
        item_id: Long,
        list: List<ItemComplexOpis>
    ): List<PairId> {
        val listNewId = mutableListOf<PairId>()
        list.forEach { opis ->
            mdb.complexOpisQueries.transactionWithResult<Long> {
                mdb.complexOpisQueries.insertOrReplace(
                    table_name = opis.table_name,
                    item_id = item_id,
                    sort = opis.sort
                )
                return@transactionWithResult mdb.complexOpisQueries.getLastIndex().executeAsOne()
            }.let { opis_id ->
                if (opis_id > 0) {
                    val check: Boolean = when (opis) {
                        is ItemComplexOpisTextCommon -> {
                            mdb.complexOpisTextQueries.insertOrReplace(
                                opis_id = opis_id,
                                text = opis.text,
                                text_fts = opis.text.uppercase(),
                                colorNum = opis.color,
                                font_size = opis.fontSize,
                                cursive = if (opis.cursiv) 1L else 0L,
                                bold = opis.bold.toLong()
                            )
                            when (opis) {
                                is ItemComplexOpisText -> {
                                    true
                                }

                                is ItemComplexOpisCheckbox -> {
                                    mdb.complexOpisCheckboxQueries.insertOrReplace(
                                        opis_id,
                                        opis.checked,
                                        if (opis.many_type) 1L else 0L
                                    )
                                    true
                                }

                                is ItemComplexOpisLink -> {
                                    mdb.complexOpisLinkQueries.insertOrReplace(
                                        opis_id = opis_id,
                                        link = opis.link
                                    )
                                    true
                                }

                                is ItemComplexOpisImageGroup -> {
                                    mdb.complexOpisImageGroupQueries.insertOrReplace(
                                        opis_id = opis_id,
                                        size_preview = opis.sizePreview,
                                        width_limit = if (opis.widthLimit) 1L else 0L,
                                        enable_text = if (opis.enableText) 1L else 0L,
                                        text_before = if (opis.textBefore) 1L else 0L
                                    )
                                    opis.spisImages.forEach { itemImg ->
                                        if (itemImg.opis_id > 0) {
                                            mdb.complexOpisImageQueries.update(
                                                opis_id = opis_id,
                                                sort = itemImg.sort,
                                                id = itemImg.id
                                            )
                                        } else {
                                            mdb.complexOpisImageQueries.transactionWithResult<Long> {
                                                mdb.complexOpisImageQueries.insertOrReplace(
                                                    opis_id = opis_id,
                                                    sort = itemImg.sort
                                                )
                                                return@transactionWithResult mdb.complexOpisQueries.getLastIndex()
                                                    .executeAsOne()
                                            }.let { newId ->
                                                if (newId > 0) listNewId.add(PairId(itemImg, newId))
                                            }
                                        }
                                    }
                                    true
                                }
                            }
                        }
                    }
                }
            }
        }
        return listNewId
    }

    fun updComplexOpis(
        item_id: Long,
        list: List<ItemComplexOpis>
    ): List<PairId> {
        val listNewId = mutableListOf<PairId>()
        mdb.complexOpisQueries.transaction {
            list.filter { it.id > 0 }.forEach { opis ->
                mdb.complexOpisQueries.updateSort(opis.sort, opis.id)
                val check: Boolean = when (opis) {
                    is ItemComplexOpisTextCommon -> {
                        mdb.complexOpisTextQueries.update(
                            text = opis.text,
                            text_fts = opis.text.uppercase(),
                            colorNum = opis.color,
                            font_size = opis.fontSize,
                            cursive = if (opis.cursiv) 1L else 0L,
                            bold = opis.bold.toLong(),
                            opis_id = opis.id
                        )
                        when (opis) {
                            is ItemComplexOpisText -> {
                                true
                            }

                            is ItemComplexOpisCheckbox -> {
                                mdb.complexOpisCheckboxQueries.update(
                                    opis_id = opis.id,
                                    checked = opis.checked,
                                    many_type = if (opis.many_type) 1L else 0L
                                )
                                true
                            }

                            is ItemComplexOpisLink -> {
                                mdb.complexOpisLinkQueries.updateLink(
                                    opis_id = opis.id,
                                    link = opis.link
                                )
                                true
                            }

                            is ItemComplexOpisImageGroup -> {
                                mdb.complexOpisImageGroupQueries.update(
                                    opis_id = opis.id,
                                    size_preview = opis.sizePreview,
                                    width_limit = if (opis.widthLimit) 1L else 0L,
                                    enable_text = if (opis.enableText) 1L else 0L,
                                    text_before = if (opis.textBefore) 1L else 0L
                                )
                                opis.spisImages.forEach { itemImg ->
                                    mdb.complexOpisImageQueries.transactionWithResult<Long> {
                                        if (itemImg.opis_id > 0 && itemImg.sort > 0) mdb.complexOpisImageQueries.updateSort(
                                            id = itemImg.id,
                                            sort = itemImg.sort
                                        )
                                        if (itemImg.opis_id > 0 && itemImg.sort < 0) mdb.complexOpisImageQueries.delete(
                                            itemImg.id
                                        )

                                        /**
                                         * Вариант (itemImg.opis_id < 0 && itemImg.sort < 0) рассматривать не актуально,
                                         * т.к. таких айтемов еще нет в базе и добавлять их туда не надо, а временные
                                         * картинки удалятся сами при закрытии программы.
                                         * */
                                        if (itemImg.opis_id < 0 && itemImg.sort > 0) {
                                            mdb.complexOpisImageQueries.insertOrReplace(
                                                opis_id = opis.id,
                                                sort = itemImg.sort
                                            )
                                            return@transactionWithResult mdb.complexOpisQueries.getLastIndex()
                                                .executeAsOne()
                                        }
                                        return@transactionWithResult -1L
                                    }.let { newId ->
                                        if (newId > 0) listNewId.add(PairId(itemImg, newId))
                                    }
                                }
                                true
                            }
                        }
                    }
                }
            }

            list.filter { it.sort < 0 && it.id > 0L }.forEach { opis ->
                mdb.complexOpisQueries.delete(opis.id)
            }
        }
        list.filter { it.sort > 0 && it.id == -1L }.forEach { opis ->
            mdb.complexOpisQueries.transactionWithResult<Long> {
                mdb.complexOpisQueries.insertOrReplace(
                    table_name = opis.table_name,
                    item_id = item_id,
                    sort = opis.sort
                )
                return@transactionWithResult mdb.complexOpisQueries.getLastIndex().executeAsOne()
            }.let { opis_id ->
                if (opis_id > 0) {
                    val check: Boolean = when (opis) {
                        is ItemComplexOpisTextCommon -> {
                            mdb.complexOpisTextQueries.insertOrReplace(
                                opis_id = opis_id,
                                text = opis.text,
                                text_fts = opis.text.uppercase(),
                                colorNum = opis.color,
                                font_size = opis.fontSize,
                                cursive = if (opis.cursiv) 1L else 0L,
                                bold = opis.bold.toLong()
                            )
                            when (opis) {
                                is ItemComplexOpisText -> {

                                    true
                                }

                                is ItemComplexOpisCheckbox -> {
                                    mdb.complexOpisCheckboxQueries.insertOrReplace(
                                        opis_id,
                                        opis.checked,
                                        if (opis.many_type) 1L else 0L
                                    )
                                    true
                                }

                                is ItemComplexOpisLink -> {
                                    mdb.complexOpisLinkQueries.insertOrReplace(
                                        opis_id = opis_id,
                                        link = opis.link
                                    )
                                    true
                                }

                                is ItemComplexOpisImageGroup -> {
                                    mdb.complexOpisImageGroupQueries.insertOrReplace(
                                        opis_id = opis_id,
                                        size_preview = opis.sizePreview,
                                        width_limit = if (opis.widthLimit) 1L else 0L,
                                        enable_text = if (opis.enableText) 1L else 0L,
                                        text_before = if (opis.textBefore) 1L else 0L
                                    )
                                    opis.spisImages.forEach { itemImg ->
                                        if (itemImg.opis_id > 0) {
                                            mdb.complexOpisImageQueries.update(
                                                opis_id = opis_id,
                                                sort = itemImg.sort,
                                                id = itemImg.id
                                            )
                                        } else {
                                            mdb.complexOpisImageQueries.transactionWithResult<Long> {
                                                mdb.complexOpisImageQueries.insertOrReplace(
                                                    opis_id = opis_id,
                                                    sort = itemImg.sort
                                                )
                                                return@transactionWithResult mdb.complexOpisQueries.getLastIndex()
                                                    .executeAsOne()
                                            }.let { newId ->
                                                if (newId > 0) listNewId.add(PairId(itemImg, newId))
                                            }
                                        }
                                    }
                                    true
                                }
                            }
                        }
                    }
                }
            }
        }
        return listNewId
    }

    fun updComplexOpisCheckbox(
        opis_id: Long,
        check: Long
    ) {
        mdb.complexOpisCheckboxQueries.updateChecked(checked = check, opis_id = opis_id)
    }

    fun addComplexOpisShablon(
        item: ItemComplexOpisShablon
    ) {
        mdb.complexOpisShablonQueries.insertOrReplace(
            name = item.name,
            colorNum = item.color,
            font_size = item.fontSize,
            cursive = if (item.cursiv) 1L else 0L,
            bold = item.bold.toLong(),
            size_preview = item.sizePreview,
            width_limit = item.widthLimit?.let { if (it) 1L else 0L },
            enable_text = item.enableText?.let { if (it) 1L else 0L },
            text_before = item.textBefore?.let { if (it) 1L else 0L },
            many_type = item.many_type?.let { if (it) 1L else 0L },
            link = item.link
        )
    }

    fun deleteShablon(
        item: ItemComplexOpisShablon
    ) {
        mdb.complexOpisShablonQueries.delete(item.id)
    }
}