package ru.ragefalcon.sharedcode.viewmodels.MainViewModels

import com.soywiz.klock.DateTimeTz
import ru.ragefalcon.sharedcode.Database
import ru.ragefalcon.sharedcode.Time.SelectDenPlan
import ru.ragefalcon.sharedcode.Time.SelectStatikHourGoal
import ru.ragefalcon.sharedcode.avatar.*
import ru.ragefalcon.sharedcode.avatar.TreeSkills.*
import ru.ragefalcon.sharedcode.common.Mainparam
import ru.ragefalcon.sharedcode.extensions.*
import ru.ragefalcon.sharedcode.models.data.*
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.*
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers.SpisQueryForListener
import ru.ragefalcon.sharedcode.viewmodels.UniAdapters.*

class AvatarVMobjForSpis(private val mDB: Database, private val spisQueryListener: SpisQueryForListener) {

    inner class AvatarStatObj {
        private var updateSpisAvatarStat: ((List<ItemStat>) -> Unit)? = null
        private val stat = AvatarStat("avatar")

        val firstStart by lazy {
        }

        val statAvatar = UniQueryAdapter<SelectAvatarStat>()
            .apply {
                updateQuery(mDB.avatarStatQueries.selectAvatarStat())
                updateFunc {
                    it.firstOrNull()?.let {
                        stat.dreamInReal = "${it.dream_real}/${it.dream_count}"
                        stat.goalInReal = it.goal_real.toString()
                        stat.goalInWork = it.goal_work.toString()
                        stat.hourForDream = it.dream_hour.roundToString(1)
                        stat.hourForGoal = it.goal_hour.roundToString(1)
                        updateList()
                    }
                }
            }

        private fun updateList() {
            firstStart
            val listStat = listOf(
                ItemStat("1", "Капитал", stat.capital),
                ItemStat("2", "Целей достигнуто", stat.goalInReal),
                ItemStat("3", "Воплощенные мечты", stat.dreamInReal),
                ItemStat("4", "Целей в работе", stat.goalInWork),
                ItemStat("5", "Часов уделенных мечтам", stat.hourForDream),
                ItemStat("6", "Часов уделенных целям", stat.hourForGoal)
            )
            updateSpisAvatarStat?.invoke(listStat)
        }

        fun setCapital(cap: String) {
            stat.capital = cap
            updateList()
        }

        fun setListFun(ff: (List<ItemStat>) -> Unit) {
            updateSpisAvatarStat = ff
            updateList()
        }
    }

    val avatarStat = AvatarStatObj()

    var spisAvatarStat = MyObserveObj<List<ItemStat>> { ff ->
        avatarStat.setListFun(ff)
    }

    /**
     * Запрос для получения статистики по потраченным на цель часам (в неделю, в месяц, в год, всего, количество привязанных проектов)
     * */
    val selectHourForStatistikGoal = UniQueryAdapter<SelectHourGoalDream>().apply {
        updateQuery(
            mDB.spisGoalQueries.selectHourGoalDream(
                -1,
                (DateTimeTz.nowLocal().minusTime().unOffset()).withOffset().localUnix()
            )
        )
    }

    /**
     * Запрос для получения статистики по потраченным на мечту часам (в неделю, в месяц, в год, всего, количество привязанных проектов)
     * */
    val selectHourForStatistikDream = UniQueryAdapter<SelectHourGoalDream>().apply {
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

    var dreamStat = MyObserveObj<DreamStat> { ff ->
        selectHourForStatistikDream.updateFunc {
            setMapHourToUpdF(it){ week, month, year, all, count ->
                ff(DreamStat("DreamStat",week, month, year, all, count))
            }
        }
    }

    var goalStat = MyObserveObj<DreamStat> { ff ->
        selectHourForStatistikGoal.updateFunc {
            setMapHourToUpdF(it){ week, month, year, all, count ->
                ff(DreamStat("DreamStat",week, month, year, all, count))
            }
        }
    }

    /**
     * Список дат(по месяцам) с затраченными на цель в этом месяце часами для диаграммы работы над целью.
     * */
    val statikHourGoalForDiagram = UniQueryAdapter<SelectStatikHourGoal>().apply {
        setListner(mDB.statikHourGoalQueries.selectStatikHourGoal(4408L), {})
    }

    /**
     * Список дат(по месяцам) с затраченными на мечту в этом месяце часами для диаграммы работы над мечтой.
     * */
    val statikHourDreamForDiagram = UniQueryAdapter<SelectStatikHourGoal>().apply {
        setListner(mDB.statikHourGoalQueries.selectStatikHourGoal(4408L), {})
    }

    var diagramStatikHourDream = MyObserveObj<List<ItemYearGraf>> { ff ->
        statikHourDreamForDiagram.updateFunc { statik ->
            setMapStatikToItemYearGraf(statik){ list ->
                ff(list)
            }
        }
    }

    var diagramStatikHourGoal = MyObserveObj<List<ItemYearGraf>> { ff ->
        statikHourGoalForDiagram.updateFunc { statik ->
            setMapStatikToItemYearGraf(statik){ list ->
                ff(list)
            }
        }
    }

    private fun setMapStatikToItemYearGraf(statik: List<SelectStatikHourGoal>, updF: (List<ItemYearGraf>) -> Unit) {
        if (statik.isNotEmpty()) {
            val dateStart: Int = DateTimeTz.fromUnixLocal(statik.firstOrNull()?.data1 ?: 0L).year.year
            val dateEnd = DateTimeTz.fromUnixLocal(statik.lastOrNull()?.data1 ?: 0L).year.year
            val max = statik.maxOf { it.hour ?: 0.0 }
            val listRez: MutableList<ItemYearGraf> = mutableListOf()
            var aa = 10.0
            for (year in dateStart..dateEnd) {
                listRez.add(
                    ItemYearGraf(
                        year,
                        statik.filter {
                            DateTimeTz.fromUnixLocal(it.data1).year.year == year
                        }.map {
                            aa = statik.filter {
                                DateTimeTz.fromUnixLocal(it.data1).year.year == year
                            }.sumOf { it.hour ?: 0.0 }
                            ItemRectDiag(
                                year.toString(),
                                DateTimeTz.fromUnixLocal(it.data1).month1.toString(),
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


    val spisTreeSkills = UniConvertQueryAdapter<SelectTreeSkill, ItemTreeSkill>() {
        ItemTreeSkill(
            id = it._id.toString(),
            id_area = it.id_area,
            name = it.name,
            id_type_tree = it.id_type_tree,
            opis = it.opis,
            stat = TypeStatTreeSkills.getType(it.open_edit),
            icon = it.icon,
            completeCountNode = it.complete_count_node,
            countNode = it.count_node,
            namequest = it.namequest,
            quest_id = it.quest_id
        )
    }.apply {
        mDB.spisTreeSkillQueries.selectTreeSkill(TypeStatNodeTree.COMPLETE.codValue).let {
            spisQueryListener.spisTreeSkills = it
            this.updateQuery(it)
        }
    }

    val spisBestDays = UniConvertQueryAdapter<Bestdays, ItemBestDays>() {
        ItemBestDays(
            id = it._id.toString(),
            name = it.name,
            data = it.data_.unOffset(),
            enableIcon = it.icon_enable == 1L
        )
    }.apply {
        this.updateQuery(mDB.bestDaysQueries.selectBestDay())
    }

    val spisPrivsGoal = UniConvertQueryAdapter<SelectSpisPlanGoal, ItemPrivsGoal>() {
        ItemPrivsGoal(
            id = it._id.toString(),
            name = it.name,
            namePlan = it.plan_name,
            vajn = it.vajn,
            stap = it.stap.toString(),
            id_plan = it.id_plan.toString(),
            gotov = it.gotov,
            hour = it.hour,
            opis = it.opis
        )
    }

    val spisPrivsDream = UniConvertQueryAdapter<SelectSpisPlanDream, ItemPrivsGoal>() {
        ItemPrivsGoal(
            id = it._id.toString(),
            name = it.name,
            namePlan = it.plan_name,
            vajn = it.vajn,
            stap = it.stap.toString(),
            id_plan = it.id_plan.toString(),
            gotov = it.gotov,
            hour = it.hour,
            opis = it.opis
        )
    }

    val spisPrivsCharacteristic = UniConvertQueryAdapter<SelectSpisPlanForCharacteristic, ItemPrivsGoal>() {
        ItemPrivsGoal(
            id = it._id.toString(),
            name = it.name ?: "",
            namePlan = it.plan_name,
            vajn = it.vajn,
            stap = it.stap.toString(),
            id_plan = it.id_plan.toString(),
            gotov = it.gotov ?: 0.0,
            hour = it.hour ?: 0.0,
            opis = it.opis ?: ""
        )
    }

    var lastValuesCharacteristic = mutableMapOf<Long, Long>()

    var mutableSpisProgressCharacteristic: MutableList<Pair<ItemCharacteristic, Long>> = mutableListOf()
    var spisProgressCharacteristicForMessage = CommonObserveObj<Pair<ItemCharacteristic, Long>?>()
    val spisCharacteristics = UniConvertQueryAdapter<SelectCharacteristic, ItemCharacteristic>() { characteristic ->
        val item = ItemCharacteristic(
            id = characteristic._id,
            sort = characteristic.sort ?: 0L,
            name = characteristic.name,
            opis = characteristic.opis,
            stat = (characteristic.hour / 10).toLong(),
            startStat = characteristic.start_value,
            progress = (characteristic.hour % 10) / 10,
            hour = characteristic.hour,
        )
        lastValuesCharacteristic.get(characteristic._id)?.let {
            if (it != (characteristic.hour / 10).toLong()) {
                mutableSpisProgressCharacteristic.add(item to (characteristic.hour / 10).toLong() - it)
                mutableSpisProgressCharacteristic.firstOrNull().let {
                    spisProgressCharacteristicForMessage.setValue(it)
                }
                lastValuesCharacteristic.remove(characteristic._id)
                lastValuesCharacteristic.put(characteristic._id, (characteristic.hour / 10).toLong())
            }
        } ?: run { lastValuesCharacteristic.put(characteristic._id, (characteristic.hour / 10).toLong()) }
        item
    }.apply {
        this.updateQuery(mDB.spisCharacteristicQueries.selectCharacteristic())
    }

    var maxSumWeekHourOfCharacteristic = 0.0
        private set
    var minSumWeekHourOfCharacteristic = 0.0
        private set

    val sumWeekHourOfCharacteristic = UniQueryAdapter<SelectGrafProgressCharacteristic>(
        mDB.spisCharacteristicQueries.selectGrafProgressCharacteristic(-1L)
    )
    var spisSumWeekHourOfCharacteristic = MyObserveObj<List<ItemOperWeek>> { ff ->
        sumWeekHourOfCharacteristic.updateFunc {
            val rez: MutableList<ItemOperWeek> = mutableListOf()
            maxSumWeekHourOfCharacteristic = 0.0
            minSumWeekHourOfCharacteristic = 0.0
            it.fold(initial = 0.0, operation = { foldValue, item ->
                val sumCap = foldValue + item.hourweek
                if (sumCap > maxSumWeekHourOfCharacteristic) maxSumWeekHourOfCharacteristic = sumCap
                if (sumCap < minSumWeekHourOfCharacteristic) minSumWeekHourOfCharacteristic = sumCap
                rez.add(ItemOperWeek(item.weekdate.toLong(), item.hourweek, sumCap))
                sumCap
            })
            ff(rez)
        }
    }

    val spisGoals = UniConvertQueryAdapter<SelectGoals, ItemGoal>() {
        ItemGoal(
            id = it._id.toString(),
            lvl = it.lvl,
            name = it.name,
            data1 = it.data1.unOffset(),
            data2 = it.data2.unOffset(),
            opis = it.opis,
            gotov = it.gotov,
            hour = it.hour,

            foto = it.foto,
            min_aim = it.min_aim,
            max_aim = it.max_aim,
            summa = it.summa,
            summaRasxod = it.sumRasxod,
            schplOpen = it.open_ == 1L
        )
    }.apply {

        this.updateQuery(mDB.spisGoalQueries.selectGoals(TypeBindElementForSchetPlan.GOAL.id, 100.0))
    }

    val spisDreams = UniConvertQueryAdapter<SelectDreams, ItemDream>() {
        ItemDream(
            id = it._id.toString(),
            lvl = it.lvl,
            name = it.name,
            data1 = it.data1.unOffset(),
            opis = it.opis,
            stat = it.stat,
            hour = it.hour.toDouble(),
            foto = it.foto
        )
    }.apply {

        this.updateQuery(mDB.spisDreamQueries.selectDreams(10))
    }

    val spisDenPlanInBestDays = UniConvertQueryAdapter<SelectDenPlan, ItemDenPlan>() {
        ItemDenPlan(
            id = it._id.toString(),
            name = it.name,
            time1 = it.time1,
            time2 = it.time2,
            sum_hour = it.sum_hour,
            vajn = it.vajn,
            gotov = it.gotov,
            data = it.data_.unOffset(),
            opis = it.opis,
            privplan = it.privplan,
            stap_prpl = it.stap_prpl,
            nameprpl = it.nameprpl,
            namestap = it.namestap
        )
    }

    val spisMainParam = UniConvertQueryAdapter<Mainparam, ItemMainParam>() {
        ItemMainParam(
            id = it._id.toString(),
            name = it.name,
            intparam = it.intparam,
            stringparam = it.stringparam
        )
    }.apply {
        updateQuery(mDB.mainParamQueries.selectMainParam())
    }

    private var listHandNodeTreeSkills = listOf<ItemHandNodeTreeSkills>()
    private var listPlanNodeTreeSkills = listOf<ItemPlanNodeTreeSkills>()

    private fun updateSpisNode(): Map<Long, List<ItemNodeTreeSkills>> = mutableListOf<ItemNodeTreeSkills>()
        .apply {
            addAll(listHandNodeTreeSkills)
            addAll(listPlanNodeTreeSkills)
        }
        .sortedBy { it.id }.groupBy { it.level }

    fun updateInfoSpisNode(branchParent: ItemParentBranchNode?) {
        branchParent?.let {
            val directNode = mutableListOf<ItemNodeTreeSkills>()
                .apply {
                    addAll(listHandNodeTreeSkills)
                    addAll(listPlanNodeTreeSkills)
                }
                .filterCopy { branchParent.direct.contains(it.id) }
                .onEach { it.marker = MarkerNodeTreeSkills.DIRECTPARENT }
            val indirectNode = mutableListOf<ItemNodeTreeSkills>()
                .apply {
                    addAll(listHandNodeTreeSkills)
                    addAll(listPlanNodeTreeSkills)
                }
                .filterCopy { branchParent.indirect.contains(it.id) }
                .onEach { it.marker = MarkerNodeTreeSkills.INDIRECTPARENT }
            val commonListNode = mutableListOf<ItemNodeTreeSkills>()
                .apply {
                    addAll(directNode)
                    addAll(indirectNode)
                }
            spisNodeTreeSkillsForInfo.setValue(commonListNode)
        } ?: let {
            spisNodeTreeSkillsForInfo.setValue(listOf())
        }
    }

    val spisLevelTreeSkills = UniConvertQueryAdapter<SelectLevelTreeSkill, ItemLevelTreeSkills>() {
        ItemLevelTreeSkills(
            id = it._id,
            id_tree = it.id_tree,
            name = it.name,
            opis = it.opis,
            num_level = it.num_level,
            proc_porog = it.proc_porog,
            mustCompleteCountNode = it.count_complete_node_must,
            mustCountNode = it.count_node_must,
            completeCountNode = it.count_complete_node,
            countNode = it.count_node,
            open = it.open_level,
            quest_id = it.quest_id,
            quest_key_id = it.quest_key_id
        )
    }.apply {
        updateQuery(
            mDB.spisLevelTreeSkillsQueries.selectLevelTreeSkill(
                -1L,
                cod_node_complete = TypeStatNodeTree.COMPLETE.codValue
            )
        )
    }

    val spisHandNodeTreeSkills = UniConvertQueryAdapter<SelectHandNodes, ItemHandNodeTreeSkills>() {
        ItemHandNodeTreeSkills(
            id = it._id,
            id_tree = it.id_tree,
            name = it.name,
            opis = it.opis,
            id_type_node = it.id_type_node,
            stat = TypeStatNodeTree.getType(it.complete),
            level = it.level,
            icon = if (it.ext1 != null && it.type1 != null) ItemIconNodeTree(it.icon, it.ext1, it.type1) else null,
            icon_complete = if (it.ext2 != null && it.type2 != null) ItemIconNodeTree(
                it.icon_complete,
                it.ext2,
                it.type2
            ) else null,
            open = it.open_node == 1L,
            must_node = it.must_node == 1L,
            quest_id = it.quest_id,
            quest_key_id = it.quest_key_id
        )
    }.apply {
        updateFunc {
            listHandNodeTreeSkills = it
            spisNodeTreeSkills.update()
            spisNodeTreeSkillsForSelection.update()
        }
        updateQuery(mDB.spisNodeTreeSkillsQueries.selectHandNodes("KIT", -1L, -1L, TypeNodeTreeSkills.HAND.id))
    }

    val spisPlanNodeTreeSkills = UniConvertQueryAdapter<SelectPlanNodeTreeSkills, ItemPlanNodeTreeSkills>() {
        ItemPlanNodeTreeSkills(
            id = it._id,
            id_tree = it.id_tree,
            name = it.name,
            opis = it.opis,
            id_type_node = it.id_type_node,
            stat = TypeStatNodeTree.getType(it.complete),
            level = it.level,
            icon = if (it.ext1 != null && it.type1 != null) ItemIconNodeTree(it.icon, it.ext1, it.type1) else null,
            icon_complete = if (it.ext2 != null && it.type2 != null) ItemIconNodeTree(
                it.icon_complete,
                it.ext2,
                it.type2
            ) else null,
            open = it.open_node == 1L,
            must_node = it.must_node == 1L,
            privplan = it.privplan,
            stap_prpl = it.stap_prpl,
            porog_hour = it.porog_hour,
            quest_id = it.quest_id,
            quest_key_id = it.quest_key_id
        )
    }.apply {
        updateFunc {
            listPlanNodeTreeSkills = it
            spisNodeTreeSkills.update()
            spisNodeTreeSkillsForSelection.update()
        }
        updateQuery(
            mDB.propertyPlanNodeTSQueries.selectPlanNodeTreeSkills(
                "KIT",
                codNodeComplete = TypeStatNodeTree.COMPLETE.codValue,
                id_tree = -1L,
                id_type_plan = TypeNodeTreeSkills.PLAN.id
            )
        )
    }

    val spisWholeBranchParentNodeTreeSkills = UniQueryAdapter<SelectWholeBranchParent>().apply {
        updateFunc { spisBinding ->
            spisParentBranchNodeTreeSkills.setValue(spisBinding.groupBy { it.id_childW }.map {
                ItemParentBranchNode(
                    it.key,
                    direct = it.value.filter { it.level == 0L }.map { it.id_parentW }.toTypedArray(),
                    indirect = it.value.filter { it.level > 0L }.map { it.id_parentW }.toTypedArray()
                )
            })
        }
        updateQuery(mDB.spisBindingNodeTreeSkillsQueries.selectWholeBranchParent(-1L))
    }

    val spisWholeBranchChildNodeTreeSkills = UniQueryAdapter<SelectWholeBranchChild>().apply {
        updateFunc { spisBinding ->
            spisChildBranchNodeTreeSkills.setValue(spisBinding.groupBy { it.id_parentW }.map {
                ItemParentBranchNode(
                    it.key,
                    direct = it.value.filter { it.level == 0L }.map { it.id_childW }.toTypedArray(),
                    indirect = it.value.filter { it.level > 0L }.map { it.id_childW }.toTypedArray()
                )
            })
        }
        updateQuery(mDB.spisBindingNodeTreeSkillsQueries.selectWholeBranchChild(-1L))
    }

    var spisNodeTreeSkills = CommonComplexObserveObj<Map<Long, List<ItemNodeTreeSkills>>>().apply {
        setValueFun { updateSpisNode() }
    }

    var spisNodeTreeSkillsForSelection = CommonComplexObserveObj<Map<Long, List<ItemNodeTreeSkills>>>().apply {
        setValueFun { updateSpisNode() }
    }

    var spisNodeTreeSkillsSelection = CommonObserveObj<List<ItemNodeTreeSkills>>()
    var spisNodeTreeSkillsForInfo = CommonObserveObj<List<ItemNodeTreeSkills>>()
    var spisParentBranchNodeTreeSkills = CommonObserveObj<List<ItemParentBranchNode>>()
    var spisChildBranchNodeTreeSkills = CommonObserveObj<List<ItemParentBranchNode>>()

    val spisIconNodeTree = UniConvertQueryAdapter<Spis_icon_node_tree_skills, ItemIconNodeTree>() {
        ItemIconNodeTree(
            id = it._id,
            extension = it.extension,
            type_ramk = it.type_ramk
        )
    }.apply {
        this.updateQuery(mDB.spisIconNodeTreeSkillsQueries.select())
    }
}


