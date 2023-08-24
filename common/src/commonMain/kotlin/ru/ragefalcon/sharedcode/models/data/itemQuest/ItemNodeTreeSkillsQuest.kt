package ru.ragefalcon.sharedcode.models.data.itemQuest

import ru.ragefalcon.sharedcode.models.data.Id_class
import ru.ragefalcon.sharedcode.models.data.ItemIconNodeTree
import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.MarkerNodeTreeSkills

//@Parcelize
sealed class ItemNodeTreeSkillsQuest(
    val id: Long,
    val id_tree: Long,
    val name: String,
    val opis: String,
    val id_type_node: Long,
    val visible_stat: Long,
    val level: Long,
    val icon: ItemIconNodeTree?,
    val icon_complete: ItemIconNodeTree?,
    val must_node: Boolean,
    var marker: MarkerNodeTreeSkills = MarkerNodeTreeSkills.NONE,
    var check: Boolean = false,
    var sver: Boolean = true
) : Id_class(id_main = id.toString()), Parcelable {
    abstract fun copy(): ItemNodeTreeSkillsQuest
}

//@Parcelize
class ItemHandNodeTreeSkillsQuest(
    id: Long,
    id_tree: Long,
    name: String,
    opis: String,
    id_type_node: Long,
    visible_stat: Long,
    level: Long,
    icon: ItemIconNodeTree?,
    icon_complete: ItemIconNodeTree?,
    must_node: Boolean,
//    var sver: Boolean = true
) : ItemNodeTreeSkillsQuest(
    id,
    id_tree,
    name,
    opis,
    id_type_node,
    visible_stat,
    level,
    icon,
    icon_complete,
    must_node
) {
    override fun copy(): ItemNodeTreeSkillsQuest = ItemHandNodeTreeSkillsQuest(
        id,
        id_tree,
        name,
        opis,
        id_type_node,
        visible_stat,
        level,
        icon,
        icon_complete,
        must_node
    )
}

//@Parcelize
class ItemPlanNodeTreeSkillsQuest(
    id: Long,
    id_tree: Long,
    name: String,
    opis: String,
    id_type_node: Long,
    visible_stat: Long,
    level: Long,
    icon: ItemIconNodeTree?,
    icon_complete: ItemIconNodeTree?,
    must_node: Boolean,
    val privplan: Long,
    val stap_prpl: Long,
    val porog_hour: Double,
//    var sver: Boolean = true
) : ItemNodeTreeSkillsQuest(
    id,
    id_tree,
    name,
    opis,
    id_type_node,
    visible_stat,
    level,
    icon,
    icon_complete,
    must_node
) {
    override fun copy(): ItemNodeTreeSkillsQuest = ItemPlanNodeTreeSkillsQuest(
        id,
        id_tree,
        name,
        opis,
        id_type_node,
        visible_stat,
        level,
        icon,
        icon_complete,
        must_node,
        privplan,
        stap_prpl,
        porog_hour
    )
}

//@Parcelize
class ItemCountNodeTreeSkillsQuest(
    id: Long,
    id_tree: Long,
    name: String,
    opis: String,
    id_type_node: Long,
    visible_stat: Long,
    level: Long,
    icon: ItemIconNodeTree?,
    icon_complete: ItemIconNodeTree?,
    must_node: Boolean,
    val priv_counter: Long,
    val count_value: Long,
    val max_value: Long,
    val porog_value: Long,
//    var sver: Boolean = true
) : ItemNodeTreeSkillsQuest(
    id,
    id_tree,
    name,
    opis,
    id_type_node,
    visible_stat,
    level,
    icon,
    icon_complete,
    must_node
) {
    override fun copy(): ItemNodeTreeSkillsQuest = ItemCountNodeTreeSkillsQuest(
        id,
        id_tree,
        name,
        opis,
        id_type_node,
        visible_stat,
        level,
        icon,
        icon_complete,
        must_node,
        priv_counter,
        count_value,
        max_value,
        porog_value
    )
}

fun List<ItemNodeTreeSkillsQuest>.copy():List<ItemNodeTreeSkillsQuest> = mutableListOf<ItemNodeTreeSkillsQuest>()
    .apply {
        this@copy.forEach {
            add(it.copy())
        }
    }

fun List<ItemNodeTreeSkillsQuest>.filterCopy(filter: (ItemNodeTreeSkillsQuest)->Boolean):List<ItemNodeTreeSkillsQuest> = mutableListOf<ItemNodeTreeSkillsQuest>()
    .apply {
        this@filterCopy.filter(filter).forEach {
            add(it.copy())
        }
    }
