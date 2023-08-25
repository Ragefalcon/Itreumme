package ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData

enum class TypeOpisBlock(val id: Long, val nameBlock: String) {
    simpleText(1L, "Текст"),
    checkbox(2L, "Чек-бокс"),
    link(3L, "Ссылка"),
    image(4L, "Изображения")
    ;
}

enum class TableNameForComplexOpis(val nameTable: String) {
    spisNapom("napom"),
    spisDenPlan("den_plan"),
    spisNextAction("spis_next_action"),
    spisShabDenPlan("shab_den_plan"),
    spisVxod("vxod"),
    spisBloknot("spis_bloknot"),
    spisIdea("spis_idea"),
    spisIdeaStap("spis_stap_idea"),
    spisPlan("spis_plan"),
    spisPlanStap("spis_stap_plan")
    ;
}

enum class FilterSchetOper(val title: String) {
    Rasxod("Расход"),
    Doxod("Доход"),
    Perevod("Перевод");
}

enum class FilterSchetPlanOper(val title: String) {
    Rasxod("Расход"),
    Doxod("Доход"),
    Popravka("Поправка на курс"),
    Perevod("Перевод");
}

interface tabElement {
    val nameTab: String
}

enum class MyTypeCorner(val id: Long, override val nameTab: String) : tabElement {
    Round(1L, "круглый"),
    Cut(2L, "срез"),
    Ticket(3L, "вогнутый");

    companion object {
        fun getType(id: Long) = values().toList().find { it.id == id }
    }
}

enum class TypeIconBorder(val id: Long, val type: String) {
    NONE(1L, "Без рамок"),
    ROUND(2L, "Круглая рамка"),
    SQUARE(3L, "Квадратная рамка"),
    ROUNDCORNER(4L, "Круглые углы"),
    CUTCORNER(5L, "Срезаные углы"),
    TICKETCORNER(6L, "Вогнутые углы");

    companion object {
        fun getType(id: Long): TypeIconBorder? = values().toList().find { it.id == id }
    }
}

enum class TypeBindElementForSchetPlan(val id: Long) {
    GOAL(1L),
    PLAN(2L),
    PLANSTAP(3L);

    companion object {
        fun getType(id: Long): TypeBindElementForSchetPlan? = values().toList().find { it.id == id }
    }
}

enum class MarkerNodeTreeSkills(val id: Long) {
    DIRECTPARENT(1L),
    INDIRECTPARENT(2L),
    DIRECTCHILD(3L),
    INDIRECTCHILD(4L),
    NONE(5L),
    UNENABLE(6L),
    UNENABLELVL2(7L);

    companion object {
        fun getType(id: Long): MarkerNodeTreeSkills? = values().toList().find { it.id == id }
    }
}

enum class TypeTreeSkills(val id: Long, val nameType: String) {
    KIT(1L, "Набор"),
    LEVELS(2L, "Уровни"),
    TREE(3L, "Дерево");

    companion object {
        fun getType(nameType: String): TypeTreeSkills? = values().toList().find { it.nameType == nameType }
        fun getType(id: Long): TypeTreeSkills? = values().toList().find { it.id == id }
    }
}

enum class TypeNodeTreeSkills(val id: Long, val nameType: String) {
    HAND(1L, "Ручное выполнение"),
    PLAN(2L, "Выполнение с проектом"),
    COUNTER_END(3L, "Конечный счетчик"),
    COUNTER_ENDLESS(4L, "Бесконечный счетчик"),
    HOUR_END(5L, "По количеству часов"),
    HOUR_ENDLESS(6L, "Подсчет часов");

    companion object {
        fun getType(nameType: String): TypeNodeTreeSkills? = values().toList().find { it.nameType == nameType }
        fun getType(id: Long): TypeNodeTreeSkills? = values().toList().find { it.id == id }
    }
}

enum class TypeStatPlan(val codValue: Long, val nameType: String) {
    COMPLETE(10L, "Выполнен"),
    CLOSE(4L, "Закрыт/провален"),
    FREEZE(3L, "Заморожен"),
    UNBLOCKNOW(1L, "Только что разблокирован"),
    VISIB(0L, "Сразу виден"),
    BLOCK(-2L, "Виден, но заблокирован"),
    INVIS(-3L, "Не виден сразу");

    companion object {
        fun getBlockList(): List<TypeStatPlan> = listOf(BLOCK, INVIS)
        fun getIsklSelectList(): List<TypeStatPlan> = listOf(COMPLETE, UNBLOCKNOW, FREEZE, CLOSE)
        fun getCloseSelectList(): List<TypeStatPlan> = listOf(COMPLETE, CLOSE)
        fun getSelectList(): List<TypeStatPlan> = values().filter { getIsklSelectList().contains(it).not() }
        fun getOpenList(): List<TypeStatPlan> = values().filter { getCloseSelectList().contains(it).not() }
        fun getType(nameType: String): TypeStatPlan = values().toList().find { it.nameType == nameType } ?: VISIB
        fun getType(code: Long): TypeStatPlan = values().toList().find { it.codValue == code } ?: VISIB
    }
}

enum class TypeStatPlanStap(val codValue: Long, val nameType: String) {
    COMPLETE(10L, "Выполнен"),
    CLOSE(4L, "Закрыт/провален"),
    FREEZE(3L, "Заморожен"),
    NEXTACTION(2L, "Следующее действие"),
    UNBLOCKNOW(1L, "Только что разблокирован"),
    VISIB(0L, "Сразу виден"),
    BLOCK(-2L, "Виден, но заблокирован"),
    INVIS(-3L, "Не виден сразу");

    companion object {
        fun getBlockList(): List<TypeStatPlanStap> = listOf(BLOCK, INVIS)
        fun getIsklSelectList(): List<TypeStatPlanStap> = listOf(COMPLETE, UNBLOCKNOW, FREEZE, CLOSE)
        fun getCloseSelectList(): List<TypeStatPlanStap> = listOf(COMPLETE, CLOSE)
        fun getSelectList(): List<TypeStatPlanStap> = values().filter { getIsklSelectList().contains(it).not() }
        fun getOpenList(): List<TypeStatPlanStap> = values().filter { getCloseSelectList().contains(it).not() }
        fun getType(nameType: String): TypeStatPlanStap = values().toList().find { it.nameType == nameType } ?: VISIB
        fun getType(code: Long): TypeStatPlanStap = values().toList().find { it.codValue == code } ?: VISIB
    }
}

enum class TypeStatNodeTree(val codValue: Long, val nameType: String) {
    COMPLETE(10L, "Выполнен"),
    UNBLOCKNOW(1L, "Только что разблокирован"),
    VISIB(0L, "Сразу виден"),
    BLOCK(-2L, "Виден, но заблокирован"),
    INVIS(-3L, "Не виден сразу");

    companion object {
        fun getBlockList(): List<TypeStatNodeTree> = listOf(BLOCK, INVIS)
        fun getIsklSelectList(): List<TypeStatNodeTree> = listOf(COMPLETE, UNBLOCKNOW)
        fun getSelectList(): List<TypeStatNodeTree> = values().filter { it != COMPLETE && it != UNBLOCKNOW }
        fun getType(nameType: String): TypeStatNodeTree = values().toList().find { it.nameType == nameType } ?: VISIB
        fun getType(code: Long): TypeStatNodeTree = values().toList().find { it.codValue == code } ?: VISIB
    }
}

enum class TypeStatTreeSkills(val codValue: Long, val nameType: String) {
    COMPLETE(10L, "Выполнено"),
    UNBLOCKNOW(1L, "Только что разблокировано"),
    VISIB(0L, "Сразу видно"),
    OPEN_EDIT(-1L, "Открыто для редактирования"),
    BLOCK(-2L, "Видно, но заблокировано"),
    INVIS(-3L, "Не видно сразу");

    companion object {
        fun getType(nameType: String): TypeStatTreeSkills = values().toList().find { it.nameType == nameType } ?: VISIB
        fun getType(code: Long): TypeStatTreeSkills = values().toList().find { it.codValue == code } ?: VISIB
    }
}

enum class TypeStatQuestElementVisible(val codValue: Long, val nameType: String) {
    VISIB(0L, "Сразу видно"),
    BLOCK(-2L, "Видно, но заблокировано"),
    INVIS(-3L, "Не видно сразу");

    companion object {
        fun getType(nameType: String): TypeStatQuestElementVisible? = values().toList().find { it.nameType == nameType }
        fun getType(codValue: Long): TypeStatQuestElementVisible? = values().toList().find { it.codValue == codValue }
    }
}

enum class TypeStartObjOfTrigger(val id: Long, val nameType: String) {
    STARTPLAN(1L, "Старт проекта"),
    STARTDIALOG(2L, "Старт диалога"),
    SUMTRIGGER(3L, "Группа условий"),
    STARTSTAP(4L, "Старт этапа"),
    STARTTREE(6L, "Старт дерева"),
    STARTNODETREE(7L, "Старт узла дерева"),
    STARTLEVELTREE(8L, "Старт уровня дерева"),

    /**
     * INNERFINISH - сигнализирует о том, что диалоги или что-то еще из внутреннего квеста привело к ситуации,
     * когда программа должна отреагировать соответствующим образом. Например, после диалога о входящих открыть
     * соответствующую панель или выполнить соответствующее действие.
     * */
    INNERFINISH(5L, "Внутренний триггер");

    companion object {
        fun getListIdDialogTrigger(): List<Long> = listOf(STARTDIALOG.id)
        fun getListIdPlanTrigger(): List<Long> = listOf(STARTPLAN.id)
        fun getListIdStapPlanTrigger(): List<Long> = listOf(STARTSTAP.id)

        fun getType(code: Long): TypeStartObjOfTrigger? = values().toList().find { it.id == code }
    }
}

enum class TypeQuestElement(val code: String) {
    PLAN("plan"),
    PLANSTAP("stap_plan"),
    TREESKILLS("TreeSkills"),
    NODETREESKILLS("NodeTree")
}

enum class TypeParentOfTrig(val code: String) {
    /**
     * [STARTQUESTDIALOG] - если бы он не распространялся на все квесты, то его можно было бы отнести к внутренним триггерам.
     * Но он может быть использован во всех квестах. В квесте устанавливается стартовый диалог и после добавления квеста
     * автоматически запускается этот триггер, который как раз приводит к запуску стартового диалога.
     * */
    STARTQUESTDIALOG("startQuestDialog"),

    /**
     * [OTVDIALOG] - свзязывает с событием выбора ответа в диалогах.
     * */
    OTVDIALOG("otvDialog"),

    /**
     * [PLAN] сигнализируют о выполнении проекта.
     * */
    PLAN(TypeQuestElement.PLAN.code),

    /**
     * [PLANSTAP] сигнализируют о выполнении этапа проекта.
     * */
    PLANSTAP(TypeQuestElement.PLANSTAP.code),

    /**
     * [INNERSTART] - говорит о старте внутреннего триггера, из тех что должны быть не доступны при составлении обычных квестов.
     * Они только для внутренних. Когда на действие изнутри программы стартует событие (в основном диалог) из [внутреннего]квеста.
     * Такие как: старт смены даты рождения, старт разбора входящих, диалоги при первом старте и т.д.
     * */
    INNERSTART("InnerStart"),

    /**
     * [NODETREESKILLS] - маркер триггера сигнализирующего о выполнении/достижении ачивки (узла дерева).
     * */
    NODETREESKILLS(TypeQuestElement.NODETREESKILLS.code)
}


enum class TypeDialogMessage(val code: String) {
    QUESTDIALOG("dialog"),
}
