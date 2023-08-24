package ru.ragefalcon.sharedcode.viewmodels.MainViewModels

import com.squareup.sqldelight.Query
import ru.ragefalcon.sharedcode.ComplexOpis.Complex_opis_shablon
import ru.ragefalcon.sharedcode.Database
import ru.ragefalcon.sharedcode.models.data.*
import ru.ragefalcon.sharedcode.viewmodels.UniAdapters.CommonComplexObserveObj
import ru.ragefalcon.sharedcode.viewmodels.UniAdapters.UniConvertQueryAdapter

class ComplexOpisVMobjForSpis(private val mDB: Database)  {
    val spisComplexOpisShablon = UniConvertQueryAdapter<Complex_opis_shablon, ItemComplexOpisShablon>{
        ItemComplexOpisShablon(
            id = it._id,
                    name = it.name,
                    color = it.colorNum,
                    fontSize = it.font_size,
                    cursiv = it.cursive == 1L,
                    bold = it.bold.toInt(),
                    many_type = it.many_type?.let { it == 1L },
                    link = it.link,
                    sizePreview = it.size_preview,
                    widthLimit = it.width_limit?.let { it == 1L },
                    enableText = it.enable_text?.let { it == 1L },
                    textBefore = it.text_before?.let { it == 1L },
        )
    }.apply {
        updateQuery(mDB.complexOpisShablonQueries.selectComplexOpisShablon())
    }

    val spisComplexOpisForVxod = ComplexOpisCommonSpis(
        mDB.complexOpisTextQueries.selectComplexOpisTextCommonTable("vxod"),
        mDB.complexOpisCheckboxQueries.selectComplexOpisCheckboxCommonTable("vxod"),
        mDB.complexOpisImageGroupQueries.selectComplexOpisImageGroupCommonTable("vxod"),
        mDB.complexOpisLinkQueries.selectComplexOpisLinkCommonTable("vxod"),
        ::ItemComplexOpisText,
        ::ItemComplexOpisCheckbox,
        ::ItemComplexOpisImageGroup,
        ::ItemComplexOpisLink
    )

    val spisComplexOpisForNapom = ComplexOpisCommonSpis(
        mDB.complexOpisTextQueries.selectComplexOpisTextCommonWithId("napom", -1L),
        mDB.complexOpisCheckboxQueries.selectComplexOpisCheckboxCommonWithId("napom", -1L),
        mDB.complexOpisImageGroupQueries.selectComplexOpisImageGroupCommonWithId("napom", -1L),
        mDB.complexOpisLinkQueries.selectComplexOpisLinkCommonWithId("napom", -1L),
        ::ItemComplexOpisText,
        ::ItemComplexOpisCheckbox,
        ::ItemComplexOpisImageGroup,
        ::ItemComplexOpisLink
    )

    val spisComplexOpisForDenPlan = ComplexOpisCommonSpis(
        mDB.complexOpisTextQueries.selectComplexOpisTextCommonWithId("den_plan", -1L),
        mDB.complexOpisCheckboxQueries.selectComplexOpisCheckboxCommonWithId("den_plan", -1L),
        mDB.complexOpisImageGroupQueries.selectComplexOpisImageGroupCommonWithId("den_plan", -1L),
        mDB.complexOpisLinkQueries.selectComplexOpisLinkCommonWithId("den_plan", -1L),
        ::ItemComplexOpisText,
        ::ItemComplexOpisCheckbox,
        ::ItemComplexOpisImageGroup,
        ::ItemComplexOpisLink
    )

    /**
     * Значение countCalendarWeek должно быть продублировано во всех запросах ниже с "calendar"
     * (SelectComplexOpisTextCommonWithId в ComplexOpisText.sq и для остальных соответственно)
     * вот в этом месте:
     *     WHEN 'calendar' THEN  complex_opis.item_id IN (SELECT _id FROM den_plan WHERE den_plan.data >= :id AND
     *          den_plan.data < strftime('%s000', :id / 1000, 'unixepoch', '43 days'))
     * число 43 получено произведением количества дней в неделе на countCalendarWeek плюс 1 день,
     * т.е. если countCalendarWeek = 6, то в запросе 43 (= 7*6 + 1).
     * */
    val countCalendarWeek = 6
    val spisComplexOpisForCalendar = ComplexOpisCommonSpis(
        mDB.complexOpisTextQueries.selectComplexOpisTextCommonWithId("calendar", -1L),
        mDB.complexOpisCheckboxQueries.selectComplexOpisCheckboxCommonWithId("calendar", -1L),
        mDB.complexOpisImageGroupQueries.selectComplexOpisImageGroupCommonWithId("calendar", -1L),
        mDB.complexOpisLinkQueries.selectComplexOpisLinkCommonWithId("calendar", -1L),
        ::ItemComplexOpisText,
        ::ItemComplexOpisCheckbox,
        ::ItemComplexOpisImageGroup,
        ::ItemComplexOpisLink
    )
    val spisComplexOpisForCalendarNapom = ComplexOpisCommonSpis(
        mDB.complexOpisTextQueries.selectComplexOpisTextCommonWithId("calendar_napom", -1L),
        mDB.complexOpisCheckboxQueries.selectComplexOpisCheckboxCommonWithId("calendar_napom", -1L),
        mDB.complexOpisImageGroupQueries.selectComplexOpisImageGroupCommonWithId("calendar_napom", -1L),
        mDB.complexOpisLinkQueries.selectComplexOpisLinkCommonWithId("calendar_napom", -1L),
        ::ItemComplexOpisText,
        ::ItemComplexOpisCheckbox,
        ::ItemComplexOpisImageGroup,
        ::ItemComplexOpisLink
    )

    val spisComplexOpisForDenPlanInBestDays = ComplexOpisCommonSpis(
        mDB.complexOpisTextQueries.selectComplexOpisTextCommonWithId("den_plan", -1L),
        mDB.complexOpisCheckboxQueries.selectComplexOpisCheckboxCommonWithId("den_plan", -1L),
        mDB.complexOpisImageGroupQueries.selectComplexOpisImageGroupCommonWithId("den_plan", -1L),
        mDB.complexOpisLinkQueries.selectComplexOpisLinkCommonWithId("den_plan", -1L),
        ::ItemComplexOpisText,
        ::ItemComplexOpisCheckbox,
        ::ItemComplexOpisImageGroup,
        ::ItemComplexOpisLink
    )

    val spisComplexOpisForShabDenPlan = ComplexOpisCommonSpis(
        mDB.complexOpisTextQueries.selectComplexOpisTextCommonTable("shab_den_plan"),
        mDB.complexOpisCheckboxQueries.selectComplexOpisCheckboxCommonTable("shab_den_plan"),
        mDB.complexOpisImageGroupQueries.selectComplexOpisImageGroupCommonTable("shab_den_plan"),
        mDB.complexOpisLinkQueries.selectComplexOpisLinkCommonTable("shab_den_plan"),
        ::ItemComplexOpisText,
        ::ItemComplexOpisCheckbox,
        ::ItemComplexOpisImageGroup,
        ::ItemComplexOpisLink
    )

    val spisComplexOpisForBloknot = ComplexOpisCommonSpis(
        mDB.complexOpisTextQueries.selectComplexOpisTextCommonTable("spis_bloknot"),
        mDB.complexOpisCheckboxQueries.selectComplexOpisCheckboxCommonTable("spis_bloknot"),
        mDB.complexOpisImageGroupQueries.selectComplexOpisImageGroupCommonTable("spis_bloknot"),
        mDB.complexOpisLinkQueries.selectComplexOpisLinkCommonTable("spis_bloknot"),
        ::ItemComplexOpisText,
        ::ItemComplexOpisCheckbox,
        ::ItemComplexOpisImageGroup,
        ::ItemComplexOpisLink
    )

    val spisComplexOpisForIdea = ComplexOpisCommonSpis(
        mDB.complexOpisTextQueries.selectComplexOpisTextCommonWithId("spis_idea", -1L),
        mDB.complexOpisCheckboxQueries.selectComplexOpisCheckboxCommonWithId("spis_idea", -1L),
        mDB.complexOpisImageGroupQueries.selectComplexOpisImageGroupCommonWithId("spis_idea", -1L),
        mDB.complexOpisLinkQueries.selectComplexOpisLinkCommonWithId("spis_idea", -1L),
        ::ItemComplexOpisText,
        ::ItemComplexOpisCheckbox,
        ::ItemComplexOpisImageGroup,
        ::ItemComplexOpisLink
    )

    val spisComplexOpisForIdeaStap = ComplexOpisCommonSpis(
        mDB.complexOpisTextQueries.selectComplexOpisTextCommonWithId("spis_stap_idea", -1L),
        mDB.complexOpisCheckboxQueries.selectComplexOpisCheckboxCommonWithId("spis_stap_idea", -1L),
        mDB.complexOpisImageGroupQueries.selectComplexOpisImageGroupCommonWithId("spis_stap_idea", -1L),
        mDB.complexOpisLinkQueries.selectComplexOpisLinkCommonWithId("spis_stap_idea", -1L),
        ::ItemComplexOpisText,
        ::ItemComplexOpisCheckbox,
        ::ItemComplexOpisImageGroup,
        ::ItemComplexOpisLink
    )

    val spisComplexOpisForNextActionCommon = ComplexOpisCommonSpis(
        mDB.complexOpisTextQueries.selectComplexOpisTextCommonTable("spis_next_action"),
        mDB.complexOpisCheckboxQueries.selectComplexOpisCheckboxCommonTable("spis_next_action"),
        mDB.complexOpisImageGroupQueries.selectComplexOpisImageGroupCommonTable("spis_next_action"),
        mDB.complexOpisLinkQueries.selectComplexOpisLinkCommonTable("spis_next_action"),
        ::ItemComplexOpisText,
        ::ItemComplexOpisCheckbox,
        ::ItemComplexOpisImageGroup,
        ::ItemComplexOpisLink
    )

    val spisComplexOpisForNextActionStap = ComplexOpisCommonSpis(
        mDB.complexOpisTextQueries.selectComplexOpisTextCommonTable("spis_next_action_stap"),
        mDB.complexOpisCheckboxQueries.selectComplexOpisCheckboxCommonTable("spis_next_action_stap"),
        mDB.complexOpisImageGroupQueries.selectComplexOpisImageGroupCommonTable("spis_next_action_stap"),
        mDB.complexOpisLinkQueries.selectComplexOpisLinkCommonTable("spis_next_action_stap"),
        ::ItemComplexOpisText,
        ::ItemComplexOpisCheckbox,
        ::ItemComplexOpisImageGroup,
        ::ItemComplexOpisLink
    )

    val spisComplexOpisForPlan = ComplexOpisCommonSpis(
        mDB.complexOpisTextQueries.selectComplexOpisTextCommonTable("spis_plan"),
        mDB.complexOpisCheckboxQueries.selectComplexOpisCheckboxCommonTable("spis_plan"),
        mDB.complexOpisImageGroupQueries.selectComplexOpisImageGroupCommonTable("spis_plan"),
        mDB.complexOpisLinkQueries.selectComplexOpisLinkCommonTable("spis_plan"),
        ::ItemComplexOpisText,
        ::ItemComplexOpisCheckbox,
        ::ItemComplexOpisImageGroup,
        ::ItemComplexOpisLink
    )

    val spisComplexOpisForStapPlan = ComplexOpisCommonSpis(
        mDB.complexOpisTextQueries.selectComplexOpisTextCommonWithId("spis_stap_plan", -1L),
        mDB.complexOpisCheckboxQueries.selectComplexOpisCheckboxCommonWithId("spis_stap_plan", -1L),
        mDB.complexOpisImageGroupQueries.selectComplexOpisImageGroupCommonWithId("spis_stap_plan", -1L),
        mDB.complexOpisLinkQueries.selectComplexOpisLinkCommonWithId("spis_stap_plan", -1L),
        ::ItemComplexOpisText,
        ::ItemComplexOpisCheckbox,
        ::ItemComplexOpisImageGroup,
        ::ItemComplexOpisLink
    )

    val spisComplexOpisForHistoryPlan = ComplexOpisCommonSpis(
        mDB.complexOpisTextQueries.selectComplexOpisTextCommonWithId("history_plan", -1L),
        mDB.complexOpisCheckboxQueries.selectComplexOpisCheckboxCommonWithId("history_plan", -1L),
        mDB.complexOpisImageGroupQueries.selectComplexOpisImageGroupCommonWithId("history_plan", -1L),
        mDB.complexOpisLinkQueries.selectComplexOpisLinkCommonWithId("history_plan", -1L),
        ::ItemComplexOpisText,
        ::ItemComplexOpisCheckbox,
        ::ItemComplexOpisImageGroup,
        ::ItemComplexOpisLink
    )

    val spisComplexOpisForHistoryStapPlan = ComplexOpisCommonSpis(
        mDB.complexOpisTextQueries.selectComplexOpisTextCommonWithId("history_stap_plan", -1L),
        mDB.complexOpisCheckboxQueries.selectComplexOpisCheckboxCommonWithId("history_stap_plan", -1L),
        mDB.complexOpisImageGroupQueries.selectComplexOpisImageGroupCommonWithId("history_stap_plan", -1L),
        mDB.complexOpisLinkQueries.selectComplexOpisLinkCommonWithId("history_stap_plan", -1L),
        ::ItemComplexOpisText,
        ::ItemComplexOpisCheckbox,
        ::ItemComplexOpisImageGroup,
        ::ItemComplexOpisLink
    )

}
class ComplexOpisCommonSpis<T: Any, K: Any, F: Any, Q: Any>(
    private var queryText: Query<T>,
    private var queryCheckbox: Query<K>,
    private var queryImageGroup: Query<F>,
    private var queryLink: Query<Q>,
    private val adaptText:(T)->ItemComplexOpisText,
    private val adaptCheckbox: (K)->ItemComplexOpisCheckbox,
    private val adaptImageGroup: (F)->ItemComplexOpisImageGroup,
    private val adaptLink: (Q)->ItemComplexOpisLink
)  {

    private var listComplexOpisText = listOf<ItemComplexOpisText>()
    private var listComplexOpisCheckbox = listOf<ItemComplexOpisCheckbox>()
    private var listComplexOpisImageGroup = listOf<ItemComplexOpisImageGroup>()
    private var listComplexOpisLink = listOf<ItemComplexOpisLink>()

/*
    private fun updateSpisComplexOpis() {
        val commonListNode = mutableListOf<ItemComplexOpis>()
            .apply {
                addAll(listComplexOpisText)
                addAll(listComplexOpisCheckbox)
            }
            .sortedBy { it.sort }
        spisComplexOpis.setValue(commonListNode.copy().groupBy { it.item_id })
        println("spisComplexOpis.setValue")
    }
*/
    var spisComplexOpis = CommonComplexObserveObj<Map<Long, List<ItemComplexOpis>>>().apply {
        setValueFun {
            mutableListOf<ItemComplexOpis>()
                .apply {
                    addAll(listComplexOpisText)
                    addAll(listComplexOpisCheckbox)
                    addAll(listComplexOpisImageGroup)
                    addAll(listComplexOpisLink)
                }
                .sortedBy { it.sort }.copy().groupBy { it.item_id }
        }
    }

    val spisComplexOpisText = UniConvertQueryAdapter<T, ItemComplexOpisText>(adaptF = adaptText).apply {
        updateFunc {
            listComplexOpisText = it
            spisComplexOpis.update()
        }
        updateQuery(queryText)
    }

    val spisComplexOpisCheckbox = UniConvertQueryAdapter<K, ItemComplexOpisCheckbox>(adaptF = adaptCheckbox).apply {
        updateFunc {
            listComplexOpisCheckbox = it
            spisComplexOpis.update()
        }
        updateQuery(queryCheckbox)
    }

    val spisComplexOpisImageGroup = UniConvertQueryAdapter<F, ItemComplexOpisImageGroup>(adaptF = adaptImageGroup).apply {
        updateFunc {
            listComplexOpisImageGroup = it
            spisComplexOpis.update()
        }
        updateQuery(queryImageGroup)
    }

    val spisComplexOpisLink = UniConvertQueryAdapter<Q, ItemComplexOpisLink>(adaptF = adaptLink).apply {
        updateFunc {
            listComplexOpisLink = it
            spisComplexOpis.update()
        }
        updateQuery(queryLink)
    }

    fun updateQuery(
        newQueryText: Query<T>,
        newQueryCheckbox: Query<K>,
        newQueryImageGroup: Query<F>,
        newQueryLink: Query<Q>
    ){
        spisComplexOpisText.updateQuery(newQueryText)
        spisComplexOpisCheckbox.updateQuery(newQueryCheckbox)
        spisComplexOpisImageGroup.updateQuery(newQueryImageGroup)
        spisComplexOpisLink.updateQuery(newQueryLink)
    }

}
/*

class PairQueryConvertor<T: Any, K: Any> (val query: Query<T>, val convertor: (T)->K)

class ComplexOpisCommonSpis2<F: Any>(
    private var queryList: List<PairQueryConvertor<*,F>>
)  {

    private var listComplexOpis = mutableListOf<Pair<List<F>,UniConvertQueryAdapter<*,F>>>().apply {
        queryList.forEach {
            add(listOf<F>() to UniConvertQueryAdapter(adaptF = it.convertor).apply{
                updateQuery(it.query)
            })
        }
    }
    private var listComplexOpisCheckbox = listOf<ItemComplexOpisCheckbox>()
    private var listComplexOpisImageGroup = listOf<ItemComplexOpisImageGroup>()
    private var listComplexOpisLink = listOf<ItemComplexOpisLink>()

    */
/*
        private fun updateSpisComplexOpis() {
            val commonListNode = mutableListOf<ItemComplexOpis>()
                .apply {
                    addAll(listComplexOpisText)
                    addAll(listComplexOpisCheckbox)
                }
                .sortedBy { it.sort }
            spisComplexOpis.setValue(commonListNode.copy().groupBy { it.item_id })
            println("spisComplexOpis.setValue")
        }
    *//*

    var spisComplexOpis = CommonComplexObserveObj<Map<Long, List<ItemComplexOpis>>>().apply {
        setValueFun {
            println("spisComplexOpis.setValueFun")

            mutableListOf<ItemComplexOpis>()
                .apply {
                    addAll(listComplexOpisText)
                    addAll(listComplexOpisCheckbox)
                    addAll(listComplexOpisImageGroup)
                    addAll(listComplexOpisLink)
                }
                .sortedBy { it.sort }.copy().groupBy { it.item_id }
        }
    }

    val spisComplexOpisText = UniConvertQueryAdapter<T, ItemComplexOpisText>(adaptF = adaptText).apply {
        updateFunc {
            listComplexOpisText = it
            spisComplexOpis.update()
//            updateSpisComplexOpis()
            println("update spisComplexOpisText")
        }
        updateQuery(queryList)
    }

    val spisComplexOpisCheckbox = UniConvertQueryAdapter<K, ItemComplexOpisCheckbox>(adaptF = adaptCheckbox).apply {
        updateFunc {
            listComplexOpisCheckbox = it
            spisComplexOpis.update()
//            updateSpisComplexOpis()
            println("update spisComplexOpisCheckbox")
        }
        updateQuery(queryCheckbox)
    }

    val spisComplexOpisImageGroup = UniConvertQueryAdapter<F, ItemComplexOpisImageGroup>(adaptF = adaptImageGroup).apply {
        updateFunc {
            listComplexOpisImageGroup = it
            spisComplexOpis.update()
            println("update spisComplexOpisImageGroup")
        }
        updateQuery(queryImageGroup)
    }

    val spisComplexOpisLink = UniConvertQueryAdapter<Q, ItemComplexOpisLink>(adaptF = adaptLink).apply {
        updateFunc {
            listComplexOpisLink = it
            spisComplexOpis.update()
            println("update spisComplexOpisLink")
        }
        updateQuery(queryLink)
    }

}*/
