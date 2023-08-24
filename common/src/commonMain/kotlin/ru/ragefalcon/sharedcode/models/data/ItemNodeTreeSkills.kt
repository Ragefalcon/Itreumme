package ru.ragefalcon.sharedcode.models.data


import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.MarkerNodeTreeSkills
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatNodeTree
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatPlan

//@Parcelize
sealed class ItemNodeTreeSkills(
    val id: Long,
    val id_tree: Long,
    val name: String,
    val opis: String,
    val id_type_node: Long,
    val complete: TypeStatNodeTree,
    val level: Long,
    val icon: ItemIconNodeTree?,
    val icon_complete: ItemIconNodeTree?,
    val open: Boolean,
    val must_node: Boolean,
    var marker: MarkerNodeTreeSkills = MarkerNodeTreeSkills.NONE,
    val quest_id: Long,
    val quest_key_id: Long,
    var check: Boolean = false,
    var sver: Boolean = true
) : Id_class(id_main = id.toString()), Parcelable {
    abstract fun copy(): ItemNodeTreeSkills
}

//@Parcelize
class ItemHandNodeTreeSkills(
    id: Long,
    id_tree: Long,
    name: String,
    opis: String,
    id_type_node: Long,
    stat: TypeStatNodeTree,
    level: Long,
    icon: ItemIconNodeTree?,
    icon_complete: ItemIconNodeTree?,
    open: Boolean,
    must_node: Boolean,
    quest_id: Long,
    quest_key_id: Long,
//    var sver: Boolean = true
) : ItemNodeTreeSkills(
    id,
    id_tree,
    name,
    opis,
    id_type_node,
    stat,
    level,
    icon,
    icon_complete,
    open,
    must_node,
    quest_id = quest_id,
    quest_key_id = quest_key_id
    ) {
    override fun copy(): ItemNodeTreeSkills = ItemHandNodeTreeSkills(
        id,
        id_tree,
        name,
        opis,
        id_type_node,
        complete,
        level,
        icon,
        icon_complete,
        open,
        must_node,
        quest_id,
        quest_key_id
    )
}

//@Parcelize
class ItemPlanNodeTreeSkills(
    id: Long,
    id_tree: Long,
    name: String,
    opis: String,
    id_type_node: Long,
    stat: TypeStatNodeTree,
    level: Long,
    icon: ItemIconNodeTree?,
    icon_complete: ItemIconNodeTree?,
    open: Boolean,
    must_node: Boolean,
    quest_id: Long,
    quest_key_id: Long,
    val privplan: Long,
    val stap_prpl: Long,
    val porog_hour: Double,
//    var sver: Boolean = true
) : ItemNodeTreeSkills(
    id,
    id_tree,
    name,
    opis,
    id_type_node,
    stat,
    level,
    icon,
    icon_complete,
    open,
    must_node,
    quest_id = quest_id,
    quest_key_id = quest_key_id,
) {
    override fun copy(): ItemNodeTreeSkills = ItemPlanNodeTreeSkills(
        id,
        id_tree,
        name,
        opis,
        id_type_node,
        complete,
        level,
        icon,
        icon_complete,
        open,
        must_node,
        quest_id,
        quest_key_id,
        privplan,
        stap_prpl,
        porog_hour
    )
}

//@Parcelize
class ItemCountNodeTreeSkills(
    id: Long,
    id_tree: Long,
    name: String,
    opis: String,
    id_type_node: Long,
    complete: TypeStatNodeTree,
    level: Long,
    icon: ItemIconNodeTree?,
    icon_complete: ItemIconNodeTree?,
    open: Boolean,
    must_node: Boolean,
    quest_id: Long,
    quest_key_id: Long,
    val priv_counter: Long,
    val count_value: Long,
    val max_value: Long,
    val porog_value: Long,
//    var sver: Boolean = true
) : ItemNodeTreeSkills(
    id,
    id_tree,
    name,
    opis,
    id_type_node,
    complete,
    level,
    icon,
    icon_complete,
    open,
    must_node,
    quest_id = quest_id,
    quest_key_id = quest_key_id,
) {
    override fun copy(): ItemNodeTreeSkills = ItemCountNodeTreeSkills(
        id,
        id_tree,
        name,
        opis,
        id_type_node,
        complete,
        level,
        icon,
        icon_complete,
        open,
        must_node,
        quest_id,
        quest_key_id,
        priv_counter,
        count_value,
        max_value,
        porog_value
    )
}

fun List<ItemNodeTreeSkills>.copy(): List<ItemNodeTreeSkills> = mutableListOf<ItemNodeTreeSkills>()
    .apply {
        this@copy.forEach {
            add(it.copy())
        }
    }

fun List<ItemNodeTreeSkills>.filterCopy(filter: (ItemNodeTreeSkills) -> Boolean): List<ItemNodeTreeSkills> =
    mutableListOf<ItemNodeTreeSkills>()
        .apply {
            this@filterCopy.filter(filter).forEach {
                add(it.copy())
            }
        }
