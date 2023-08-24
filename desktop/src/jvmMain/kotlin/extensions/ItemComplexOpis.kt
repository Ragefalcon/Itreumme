package extensions

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import ru.ragefalcon.sharedcode.models.data.*
import ru.ragefalcon.sharedcode.source.disk.ItrCommObserveObj
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TableNameForComplexOpis
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeOpisBlock
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers.PairId
import viewmodel.StateVM
import java.io.File

//fun SnapshotStateList<ItemComplexOpis>.addUpdList(funAddUpd: (List<ItemComplexOpis>)->List<PairId>) {
fun List<ItemComplexOpis>.clearSourceList(newTable: TableNameForComplexOpis) = this.map {
    if (it !is ItemComplexOpisImageGroup) it.myCommonCopy(id = -1L, item_id = -1L, table_name = newTable.nameTable)
    else it.copy(
        id = -1L,
        item_id = -1L,
        table_name = newTable.nameTable,
        spisImages = it.spisImages.map { it.copy(opis_id = -1L) })
}

fun ItrCommObserveObj<Map<Long, List<ItemComplexOpis>>>.delAllImageForItem(itemId: Long) = this.getState().value?.let { mapOpis ->
    mapOpis[itemId]?.let { listOpis ->
        listOpis.forEach {
            if(it is ItemComplexOpisImageGroup) {
                it.spisImages.forEach { img ->
                    val tmp = File(StateVM.dirComplexOpisImages,"complexOpisImage_${img.id}.jpg")
                    if (tmp.exists()) tmp.delete()
                }
            }
        }
    }
}

fun List<ItemComplexOpis>.addUpdList(leaveTmpImage: Boolean = false, funAddUpd: (List<ItemComplexOpis>) -> List<PairId>) {
    (if (this.size == 1) {
        this.first().let {
            if (it is ItemComplexOpisTextCommon && it.text == "" && (it !is ItemComplexOpisImageGroup || (it is ItemComplexOpisLink && it.link == ""))) {
                if (it.id == -1L)
                    listOf()
                else listOf(it.myCopy(sort = -1))
            } else this
        }
    } else this).let { opis ->
        opis.filter { it is ItemComplexOpisImageGroup && it.id > 0 }.forEach { itemGroup ->
            if (itemGroup is ItemComplexOpisImageGroup) {
                itemGroup.spisImages.filter { itemGroup.sort < 0 || (it.opis_id > 0 && it.sort < 0) }.forEach { img ->
                    val tmp = File(StateVM.dirComplexOpisImages, "complexOpisImage_${img.id}.jpg")
                    if (tmp.exists()) tmp.delete()
                }
            }
        }
        funAddUpd(opis).let { listId ->
            listId.forEach { oldNewId ->
                if (oldNewId.item.fileTmp) File(
                    StateVM.dirTemp,
                    "complexOpisImage_tmp_${oldNewId.item.id}.jpg"
                ).run {
                    if (leaveTmpImage) copyTo(File(StateVM.dirComplexOpisImages, "complexOpisImage_${oldNewId.newId}.jpg"))
                    else renameTo(File(StateVM.dirComplexOpisImages, "complexOpisImage_${oldNewId.newId}.jpg"))
                }
                else File(
                    StateVM.dirComplexOpisImages,
                    "complexOpisImage_${oldNewId.item.id}.jpg"
                ).copyTo(File(StateVM.dirComplexOpisImages, "complexOpisImage_${oldNewId.newId}.jpg"))
            }
        }
    }
}

fun getStartListComplexOpis(type: TableNameForComplexOpis, item_id: Long? = null): SnapshotStateList<ItemComplexOpis> =
    mutableStateListOf<ItemComplexOpis>(
        ItemComplexOpisText(
            -1L,
            type.nameTable,
            item_id ?: -1,
            TypeOpisBlock.simpleText,
            1L,
            text = "",
            color = 1L,
            fontSize = 3,
            cursiv = false,
            bold = 4
        )
    )