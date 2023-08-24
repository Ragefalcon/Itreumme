package ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers

import com.soywiz.klock.DateTimeTz
import com.soywiz.klock.days
import com.soywiz.klock.years
import ru.ragefalcon.sharedcode.Database
import ru.ragefalcon.sharedcode.Time.SelectStatikHourGoal
import ru.ragefalcon.sharedcode.avatar.SelectHourGoalDream
import ru.ragefalcon.sharedcode.extensions.*
import ru.ragefalcon.sharedcode.models.data.*
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.AvatarVMobjForSpis
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.ComplexOpisVMobjForSpis
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.*
import ru.ragefalcon.sharedcode.viewmodels.UniAdapters.UniQueryAdapter
import kotlin.properties.Delegates

class AvatarVMfun(
    private val mDB: Database,
    private val spisVM: AvatarVMobjForSpis,
    private val spisCO: ComplexOpisVMobjForSpis
) {
    private var funDateUpd: (Long) -> Unit = {}
    private var dateOporTime: DateTimeTz by Delegates.observable(
        DateTimeTz.nowLocal().minusTime().unOffset() - 1.years
    ) { prop, old, new ->
        spisVM.spisDenPlanInBestDays.updateQuery(
            mDB.denPlanQueries.selectDenPlan(
                new.withOffset().localUnix(),
                (new + 1.days).withOffset().localUnix()
            )
        )
        spisCO.spisComplexOpisForDenPlanInBestDays.updateQuery(
            mDB.complexOpisTextQueries.selectComplexOpisTextCommonWithId("den_plan", new.withOffset().localUnix()),
            mDB.complexOpisCheckboxQueries.selectComplexOpisCheckboxCommonWithId(
                "den_plan",
                new.withOffset().localUnix()
            ),
            mDB.complexOpisImageGroupQueries.selectComplexOpisImageGroupCommonWithId(
                "den_plan",
                new.withOffset().localUnix()
            ),
            mDB.complexOpisLinkQueries.selectComplexOpisLinkCommonWithId("den_plan", new.withOffset().localUnix()),
        )

        funDateUpd(new.withOffset().localUnix())
    }

    fun getDateLong() = dateOporTime.localUnix()

    fun nextPlanBestDay() {
        dateOporTime += 1.days
    }

    fun prevPlanBestDay() {
        dateOporTime -= 1.days
    }

    fun setPlanBestDay(time: Long) {
        dateOporTime = DateTimeTz.Companion.fromUnixLocal(time)
    }

    fun setFunPlanBestDayDateOporTimeUpd(ff: (Long) -> Unit) {
        funDateUpd = ff
    }

    /**
     * Список дат(по месяцам) с затраченными на цель в этом месяце часами для диаграммы работы над целью.
     * */
    private val statikHourGoalForDiagram = UniQueryAdapter<SelectStatikHourGoal>().apply {
        setListner(mDB.statikHourGoalQueries.selectStatikHourGoal(4408L), {})
    }

    /**
     * Список дат(по месяцам) с затраченными на мечту в этом месяце часами для диаграммы работы над мечтой.
     * */
    private val statikHourDreamForDiagram = UniQueryAdapter<SelectStatikHourGoal>().apply {
        setListner(mDB.statikHourGoalQueries.selectStatikHourGoal(4408L), {})
    }

    /**
     * Запрос для получения статистики по потраченным на цель часам (в неделю, в месяц, в год, всего, количество привязанных проектов)
     * */
    private val selectHourForStatistikGoal = UniQueryAdapter<SelectHourGoalDream>().apply {
        updateQuery(
            mDB.spisGoalQueries.selectHourGoalDream(
                -1,
                (DateTimeTz.nowLocal().minusTime().unOffset()).withOffset().localUnix()
            )
        )
    }



    private fun setMapHourToUpdF(
        list: List<SelectHourGoalDream>,
        updF: (String, String, String, String, String) -> Unit
    ) {
        list.firstOrNull()?.let {
            updF(
                it.sum_week.roundToStringProb(1),
                it.sum_month.roundToStringProb(1),
                it.sum_year.roundToStringProb(1),
                it.sum_all.roundToStringProb(1),
                it.privscount.toString()
            )
        }
    }

    /**
     * Слушатель для обновления статистики по потраченным на цель часам (в неделю, в месяц, в год, всего, количество привязанных проектов)
     * */
    fun setListenerForStatistikHourGoal(updF: (String, String, String, String, String) -> Unit) {
        selectHourForStatistikGoal.updateFunc {
            setMapHourToUpdF(it, updF)
        }
    }

    /**
     * Запрос для получения статистики по потраченным на мечту часам (в неделю, в месяц, в год, всего, количество привязанных проектов)
     * */
    private val selectHourForStatistikDream = UniQueryAdapter<SelectHourGoalDream>().apply {
        updateQuery(
            mDB.spisGoalQueries.selectHourGoalDream(
                -1,
                (DateTimeTz.nowLocal().minusTime().unOffset()).withOffset().localUnix()
            )
        )
    }

    /**
     * Слушатель для обновления статистики по потраченным на мечту часам (в неделю, в месяц, в год, всего, количество привязанных проектов)
     * */
    fun setListenerHourForStatistikDream(updF: (String, String, String, String, String) -> Unit) {
        selectHourForStatistikDream.updateFunc {
            setMapHourToUpdF(it, updF)
        }
    }

    /**
     * Устанавливает цель для слушателя обновления статистики по потраченным на цель часам (в неделю, в месяц, в год, всего, количество привязанных проектов)
     * */
    fun setSelectedGoalListenerForStatistik(idGoal: Long) {
//        mDB.spisGoalQueries.updateTempHourGoal(idGoal)
        selectHourForStatistikGoal.updateQuery(
            mDB.spisGoalQueries.selectHourGoalDream(
                idGoal,
                (DateTimeTz.nowLocal().minusTime().unOffset()).withOffset().localUnix()
            )
        )
    }

    /**
     * Устанавливает цель для слушателя обновления статистики по потраченным на мечту часам (в неделю, в месяц, в год, всего, количество привязанных проектов)
     * */
    fun setSelectedDreamListenerForStatistik(idGoal: Long) {
//        mDB.spisGoalQueries.updateTempHourGoal(idGoal)
        selectHourForStatistikDream.updateQuery(
            mDB.spisGoalQueries.selectHourGoalDream(
                idGoal,
                (DateTimeTz.nowLocal().minusTime().unOffset()).withOffset().localUnix()
            )
        )
    }

    /**
     * Указывает цель для списка привязанных к цели проектов и этапов
     * */
    fun setSelectedIdForPrivsGoal(idGoal: Long) {
        spisVM.spisPrivsGoal.updateQuery(mDB.spis_plan_goalQueries.selectSpisPlanGoal(idGoal))
    }

    /**
     * Указывает характеристику для списка привязанных к характеристике планов и этапов
     * */
    fun setSelectedIdForPrivsCharacteristic(idCharacteristic: Long) {
        spisVM.spisPrivsCharacteristic.updateQuery(mDB.spisPlanCharacteristicQueries.selectSpisPlanForCharacteristic(idCharacteristic))
    }

    /**
     * Указывает цель для списка привязанных к мечте планов и этапов
     * */
    fun setSelectedIdForPrivsDream(idDream: Long) {
        spisVM.spisPrivsDream.updateQuery(mDB.spis_plan_goalQueries.selectSpisPlanDream(idDream))
    }

    /**
     * Устанавливает цель для списка дат(по месяцам) с затраченными на цель в этом месяце часами для диаграммы работы над целью.
     * */
    fun selectGoalForDiagram(idGoal: Long) {
        statikHourGoalForDiagram.updateQuery(mDB.statikHourGoalQueries.selectStatikHourGoal(idGoal))
    }

    /**
     * Устанавливает цель для списка дат(по месяцам) с затраченными на мечту в этом месяце часами для диаграммы работы над мечтой.
     * */
    fun selectDreamForDiagram(idGoal: Long) {
        statikHourDreamForDiagram.updateQuery(mDB.statikHourGoalQueries.selectStatikHourGoal(idGoal))
    }

    /**
     * Устанавливает слушатель для списка дат(по месяцам) с затраченными на цель в этом месяце часами для диаграммы работы над целью,
     * который предварительно конвертирует список в список годовых айтемов.
     * */
    fun setListenerStatikHourGoal(updF: (List<ItemYearGraf>) -> Unit) {
        statikHourGoalForDiagram.updateFunc { statik ->
            setMapStatikToItemYearGraf(statik, updF)
        }
    }

    /**
     * Устанавливает слушатель для списка дат(по месяцам) с затраченными на мечту в этом месяце часами для диаграммы работы над мечтой,
     * который предварительно конвертирует список в список годовых айтемов.
     * */
    fun setListenerStatikHourDream(updF: (List<ItemYearGraf>) -> Unit) {
        statikHourDreamForDiagram.updateFunc { statik ->
            setMapStatikToItemYearGraf(statik, updF)
        }
    }

    private fun setMapStatikToItemYearGraf(statik: List<SelectStatikHourGoal>, updF: (List<ItemYearGraf>) -> Unit) {
        if (statik.isNotEmpty()) {
            val dateStart: Int = DateTimeTz.fromUnixLocal(statik.firstOrNull()?.data1 ?: 0L).year.year //.unOffset()
            val dateEnd = DateTimeTz.fromUnixLocal(statik.lastOrNull()?.data1 ?: 0L).year.year //.unOffset()
            val max = statik.maxOf { it.hour ?: 0.0 }
            val listRez: MutableList<ItemYearGraf> = mutableListOf()
            var aa = 10.0
            for (year in dateStart..dateEnd) {
                listRez.add(
                    ItemYearGraf(
                        year,
                        statik.filter {
                            DateTimeTz.fromUnixLocal(it.data1).year.year == year //.unOffset()
                        }.map {
                            aa = statik.filter {
                                DateTimeTz.fromUnixLocal(it.data1).year.year == year //.unOffset()
                            }.sumOf { it.hour ?: 0.0 }
                            ItemRectDiag(
                                year.toString(),
                                DateTimeTz.fromUnixLocal(it.data1).month1.toString(), //.unOffset() //.toString( "dd.HH.mm"),//
                                it.hour ?: 0.0,
                                aa,
                                (it.hour ?: 0.0) / max
                            )
                        }.toMutableList().apply {
                            for (i in 1..12) {
                                if (this.find { it.month.toInt() == i } == null) {
                                    this.add(
                                        ItemRectDiag(
                                            year.toString(),
                                            i.toString(),
                                            0.0,
                                            aa,
                                            0.0
                                        )
                                    )
                                }
                            }
                        }.sortedByDescending { it.month.toInt() }
                    )
                )
            }
            updF(listRez)
        } else {
            val year = DateTimeTz.nowLocal().year.year.toString()
            updF(
                mutableListOf(
                    ItemYearGraf(
                        year.toInt(), listOf(
                            ItemRectDiag(year, "1", 0.0, 0.0, 0.0),
                            ItemRectDiag(year, "2", 0.0, 0.0, 0.0),
                            ItemRectDiag(year, "3", 0.0, 0.0, 0.0),
                            ItemRectDiag(year, "4", 0.0, 0.0, 0.0),
                            ItemRectDiag(year, "5", 0.0, 0.0, 0.0),
                            ItemRectDiag(year, "6", 0.0, 0.0, 0.0),
                            ItemRectDiag(year, "7", 0.0, 0.0, 0.0),
                            ItemRectDiag(year, "8", 0.0, 0.0, 0.0),
                            ItemRectDiag(year, "9", 0.0, 0.0, 0.0),
                            ItemRectDiag(year, "10", 0.0, 0.0, 0.0),
                            ItemRectDiag(year, "11", 0.0, 0.0, 0.0),
                            ItemRectDiag(year, "12", 0.0, 0.0, 0.0)
                        )
                    )
                )
            )
        }
    }


    fun setOpenspisGoals(open: Boolean) {
        if (open) {
            spisVM.spisGoals.updateQuery(mDB.spisGoalQueries.selectGoals(TypeBindElementForSchetPlan.GOAL.id,-1.0))
        } else {
            spisVM.spisGoals.updateQuery(mDB.spisGoalQueries.selectGoals(TypeBindElementForSchetPlan.GOAL.id,100.0))
        }
    }

    fun setOpenspisDreams(open: Boolean) {
        if (open) {
            spisVM.spisDreams.updateQuery(mDB.spisDreamQueries.selectDreams(-1))
        } else {
            spisVM.spisDreams.updateQuery(mDB.spisDreamQueries.selectDreams(10))
        }
    }

    fun setSelectTreeSkills(idTree: Long, typeTree: TypeTreeSkills) {
        spisVM.spisHandNodeTreeSkills.updateQuery(
            mDB.spisNodeTreeSkillsQueries.selectHandNodes(
                type_tree = typeTree.name,
                codComplete = TypeStatNodeTree.COMPLETE.codValue,
                id_tree = idTree,
                id_type_hand = TypeNodeTreeSkills.HAND.id
            )
        )
        spisVM.spisPlanNodeTreeSkills.updateQuery(
            mDB.propertyPlanNodeTSQueries.selectPlanNodeTreeSkills(
                typeTree.name,
                codNodeComplete = TypeStatNodeTree.COMPLETE.codValue,
                id_tree = idTree,
                id_type_plan =TypeNodeTreeSkills.PLAN.id
            )
        )
        spisVM.spisLevelTreeSkills.updateQuery(mDB.spisLevelTreeSkillsQueries.selectLevelTreeSkill(idTree, cod_node_complete = TypeStatNodeTree.COMPLETE.codValue))
//        spisVM.spisBindingNodeTreeSkills.updateQuery(mDB.spisBindingNodeTreeSkillsQueries.selectForTree(idTree))
        spisVM.spisWholeBranchParentNodeTreeSkills.updateQuery(
            mDB.spisBindingNodeTreeSkillsQueries.selectWholeBranchParent(
                idTree
            )
        )
        spisVM.spisWholeBranchChildNodeTreeSkills.updateQuery(
            mDB.spisBindingNodeTreeSkillsQueries.selectWholeBranchChild(
                idTree
            )
        )
    }


    /**
     * Получает список выделенных узлов из списка для выделения, возвращает максимальный уровень среди этих узлов,
     * массив id всех этих узлов, а также записывает все эти узлы в список для отображения их в один ряд.
     * */
    fun getSelectIdNodeTreeSkills(): Pair<Long, Array<Long>> {
        val rez = mutableListOf<Long>()
        val listNode = mutableListOf<ItemNodeTreeSkills>()
        var level = 0L
        spisVM.spisNodeTreeSkillsForSelection.getValue()?.toList()?.forEach {
            it.second.forEach { nodeTreeSkills ->
                if (nodeTreeSkills.check && nodeTreeSkills.marker != MarkerNodeTreeSkills.UNENABLE) {
                    rez.add(nodeTreeSkills.id)
                    listNode.add(nodeTreeSkills.copy())
                    if (nodeTreeSkills.level > level) level = nodeTreeSkills.level
                }
            }
        }
        spisVM.spisNodeTreeSkillsSelection.setValue(listNode) // spisVM.getListSelectNodeTreeSkills(rez.toTypedArray()))
        return Pair(level, rez.toTypedArray())
    }

    /**
     * Сбрасывает все выделение и параметры в списке узлов для выделения.
     * */
    fun resetSelectionNodeTreeSkills() {
        spisVM.spisNodeTreeSkillsSelection.setValue(listOf())
        setSelectNodeTreeSkillsForSelection(arrayOf())
        changeItemNodeId = null
        spisVM.spisNodeTreeSkillsForSelection.getValue()?.toList()?.forEach {
            it.second.forEach { nodeTreeSkills ->
                nodeTreeSkills.marker = MarkerNodeTreeSkills.NONE
            }
        }
    }


    fun changeCheckNode(node: ItemNodeTreeSkills) {
        node.check = node.check.not()
        if (node.check) {
            unenabelParentNodeForSelection(node.id)
        } else {
            updateCheckAndEnable()
        }
        unenableChangeItemNodeAndChildForSelection()
    }

    /**
     * Устанавливает маркер в значение -1, для всех предков узла с переданным id.
     * Маркер -1 предполагает отключение узла от возможности выбрать его.
     * */
    private fun unenabelParentNodeForSelection(id: Long) {
        spisVM.spisParentBranchNodeTreeSkills.getValue()?.find { it.id_node == id }?.let {
            setMarkerNodeTreeSkillsForSelection(it.getAllParent(), MarkerNodeTreeSkills.UNENABLE)
        }
    }

    /**
     * Обновляет маркеры, после изменения выделения. Т.к. после снятия выделения нужно убрать маркер -1 для предков
     * этого узла, но только в том случае если его предки не являются предками другого выделенного узла.
     * Для этого похоже прощее вначале убрать все маркеры, а потом пробежаться по выделенным узлам заново.
     * Не думаю, что деревья будут разрастаться до таких размеров, чтобы эта процедура выглядела слишком затратно
     * по ресурсам... но возможно в будущем это нужно будет оптимизировать.
     * */
    private fun updateCheckAndEnable() {
        spisVM.spisNodeTreeSkillsForSelection.getValue()?.toList()?.forEach {
            it.second.forEach { nodeTreeSkills ->
                nodeTreeSkills.marker = MarkerNodeTreeSkills.NONE
            }
        }
        spisVM.spisNodeTreeSkillsForSelection.getValue()?.toList()?.forEach {
            it.second.forEach { nodeTreeSkills ->
                if (nodeTreeSkills.check) {
                    unenabelParentNodeForSelection(nodeTreeSkills.id)
                }
            }
        }
    }

    fun updateInfoSpisNode(item: ItemNodeTreeSkills) {
        spisVM.updateInfoSpisNode(
            spisVM.spisParentBranchNodeTreeSkills.getValue()?.toList()?.find { it.id_node == item.id })
    }

    private fun setSelectNodeTreeSkillsForSelection(selected: Array<Long>) {
        spisVM.spisNodeTreeSkillsForSelection.getValue()?.toList()?.forEach {
            it.second.forEach {
                it.check = selected.contains(it.id)
            }
        }
    }

    /**
     * Вызывается когда создается панель редактирования/добавления ачивки, когда в эту панель передана ачивка из дерева
     * общего вида для редактирования.
     * */
    fun setSelectNodeTreeSkillsForSelectionForNode(idNode: Long): Pair<Long, Array<Long>> {
        spisVM.spisParentBranchNodeTreeSkills.getValue()?.toList()?.find { it.id_node == idNode }?.let {
            setSelectNodeTreeSkillsForSelection(it.direct)
            updateCheckAndEnable()
        }
        changeItemNodeId = idNode
        unenableChangeItemNodeAndChildForSelection()
        return getSelectIdNodeTreeSkills()
    }

    /**
     * В этой переменной хранится id ачивки которая в данный момент редактируется, чтобы исключать ее и ее детей
     * из возможности выбора в качестве родителя.
     * */
    private var changeItemNodeId: Long? = null

    /**
     * Эта функция отключает возможность выбрать саму ачивку и ее детей в качестве
     * своего же родителя.
     * */
    private fun unenableChangeItemNodeAndChildForSelection() {
        changeItemNodeId?.let { changeId ->
            spisVM.spisChildBranchNodeTreeSkills.getValue()?.find { it.id_node == changeId }?.let {
                setMarkerNodeTreeSkillsForSelection(it.getNodeAndAllParent(), MarkerNodeTreeSkills.UNENABLELVL2)
            } ?: setMarkerNodeTreeSkillsForSelection(arrayOf(changeId), MarkerNodeTreeSkills.UNENABLELVL2)
        }
    }

    private fun setMarkerNodeTreeSkillsForSelection(selected: Array<Long>, marker: MarkerNodeTreeSkills = MarkerNodeTreeSkills.NONE) {
        spisVM.spisNodeTreeSkillsForSelection.getValue()?.toList()?.forEach {
            it.second.forEach {
                if (selected.contains(it.id)) it.marker = marker
            }
        }
    }

    private fun setCheckNodeTreeSkills(selected: Array<Long>) {
        spisVM.spisNodeTreeSkills.getValue()?.toList()?.forEach {
            it.second.forEach {
                it.check = selected.contains(it.id)
            }
        }
    }

    fun setMarkerParentAndChildForNodeTreeSkills(itemNodeTreeSkills: ItemNodeTreeSkills) {
        val parentBranch =
            spisVM.spisParentBranchNodeTreeSkills.getValue()?.find { it.id_node == itemNodeTreeSkills.id }
                ?: ItemParentBranchNode(itemNodeTreeSkills.id, arrayOf(), arrayOf())
        val childBranch =
            spisVM.spisChildBranchNodeTreeSkills.getValue()?.find { it.id_node == itemNodeTreeSkills.id }
                ?: ItemParentBranchNode(itemNodeTreeSkills.id, arrayOf(), arrayOf())
        spisVM.spisNodeTreeSkills.getValue()?.toList()?.forEach {
            it.second.forEach { node ->
                if (parentBranch.direct.contains(node.id)) node.marker = MarkerNodeTreeSkills.DIRECTPARENT
                else if (parentBranch.indirect.contains(node.id)) node.marker = MarkerNodeTreeSkills.INDIRECTPARENT
                else if (childBranch.direct.contains(node.id)) node.marker = MarkerNodeTreeSkills.DIRECTCHILD
                else if (childBranch.indirect.contains(node.id)) node.marker = MarkerNodeTreeSkills.INDIRECTCHILD
                else node.marker = MarkerNodeTreeSkills.NONE
            }
        }
    }

    fun removeFromProgressCharacteristicsMessage(item: Pair<ItemCharacteristic,Long>){
        println( spisVM.mutableSpisProgressCharacteristic.remove(item))
        spisVM.spisProgressCharacteristicForMessage.setValue(spisVM.mutableSpisProgressCharacteristic.firstOrNull())
/*
        if (spisVM.mutableSpisProgressCharacteristic.isEmpty()) spisVM.spisProgressCharacteristicForMessage.setValue(null) else {
            println(spisVM.mutableSpisProgressCharacteristic.first().first.name)
            spisVM.spisProgressCharacteristicForMessage.setValue(listOf(spisVM.mutableSpisProgressCharacteristic.first()))
        }
*/
    }

    /** Минимальное значение характеристики в конце недели за все время */
    fun getMinSumWeekHourOfCharacteristic(): Double {
        return spisVM.minSumWeekHourOfCharacteristic
    }
    /** Максимальное значение характеристики в конце недели за все время */
    fun getMaxSumWeekHourOfCharacteristic(): Double {
        return spisVM.maxSumWeekHourOfCharacteristic
    }
    fun setCharacteristicForGrafProgress(idCharacteristic: Long){
            spisVM.sumWeekHourOfCharacteristic.updateQuery(mDB.spisCharacteristicQueries.selectGrafProgressCharacteristic(idCharacteristic))
    }

    init {
        spisVM.spisDenPlanInBestDays.updateQuery(
            mDB.denPlanQueries.selectDenPlan(
                dateOporTime.withOffset().localUnix(),
                (dateOporTime + 1.days).withOffset().localUnix()
            )
        )
        spisCO.spisComplexOpisForDenPlanInBestDays.updateQuery(
            mDB.complexOpisTextQueries.selectComplexOpisTextCommonWithId("den_plan", dateOporTime.withOffset().localUnix()),
            mDB.complexOpisCheckboxQueries.selectComplexOpisCheckboxCommonWithId("den_plan", dateOporTime.withOffset().localUnix()),
            mDB.complexOpisImageGroupQueries.selectComplexOpisImageGroupCommonWithId("den_plan", dateOporTime.withOffset().localUnix()),
            mDB.complexOpisLinkQueries.selectComplexOpisLinkCommonWithId("den_plan", dateOporTime.withOffset().localUnix()),
        )

    }
}