package ru.ragefalcon.sharedcode.viewmodels.MainViewModels.InnerDialogs

import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeParentOfTrig
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStartObjOfTrigger
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers.ParentOfTrigger
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers.StartObjOfTrigger


val QUEST_ID_INNER_DIALOG = -87L

/**
 * Триггеры к которым будет создана таблица в квесте внутренних диалогов. И к каждому из которых можно будет привязать свой диалог.
 * И ссылаясь на этот триггер будет запускаться привязанный к нему диалог.
 * */
enum class InnerStartTriggerEnum(
    val id: Long,
    val trigArea: InnerTriggerAreaEnum,
    val nameTrig: String,
    val opisTrig: String
) {
    StartTrigger(
        1L,
        InnerTriggerAreaEnum.HelloDialog,
        "Первый старт",
        "Данный триггер вызывается когда программа стартует первый раз с пустой базой данных. " +
                "Если пользователь скажет, что у него уже есть база которую можжно скачать из сети, то нужно перевести на страничку авторизации, иначе начать " +
                "диалоги знакомства и первые вопросы об имени и дате рождения."
    ),
    VxodKonvert(
        2L,
        InnerTriggerAreaEnum.VxodHepler,
        "Конвертация входящих",
        "Триггер стартует когда пользователь просит памятку для того чтобы понять, что делать " +
                "с входящим планом. Ему задаются наводящие вопросы которые позволяют в том числе выйти на конвертацию входящего в план/этап/заметку/др., либо, например, на его удаление за ненадобностью."
    ),
    ChangeBirthday(
        3L,
        InnerTriggerAreaEnum.HelloDialog,
        "Смена даты прибытия",
        "Триггер стартует когда пользователь хочет сменить данные о своей дате рождения."
    ),
    TestTrigger(
        4L,
        InnerTriggerAreaEnum.HelloDialog,
        "Тестовый триггер",
        "Триггер для старта участка диалога который нужно протестировать в данный момент."
    ),
    HelpOpis(
        5L,
        InnerTriggerAreaEnum.HelloDialog,
        "Описание программы",
        "Стартует диалог с оглавлением описания программы."
    )
    ;

    fun parentTrig() = ParentOfTrigger(TypeParentOfTrig.INNERSTART, this.id)

    companion object {
        fun getType(id: Long): InnerStartTriggerEnum? = values().toList().find { it.id == id }
    }
}


/**
 * Триггеры, на которые будут ссылаться ответы диалогов. В программе на каждый такой триггер будет функция,
 * которая соответственно будет выполняться после ответа. Если функция содержит входной параметр, то этот параметр
 * должен быть получен либо из ответа с вводом данных, либо сохранен перед стартом диалога.
 * И перед стартом функции нужно будет проверять не равны ли эти параметры null.
 * */
enum class InnerFinishTriggerEnum(
    val id: Long,
    val trigArea: InnerTriggerAreaEnum,
    val nameTrig: String,
    val opisTrig: String
) {
    Cancel(
        0L,
        InnerTriggerAreaEnum.HelloDialog,
        "Триггер отмены",
        "Отменяет ожидание какого либо действия."
    ),
    AddBirthday(
        1L,
        InnerTriggerAreaEnum.HelloDialog,
        "День рождения",
        "Добавляет дату рождения пользователя."
    ),
    DeleteVxod(
        2L,
        InnerTriggerAreaEnum.VxodHepler,
        "Удалить входящее",
        "Просто удаляет входящее за ненадобностью."
    ),
    VxodToPlan(
        3L,
        InnerTriggerAreaEnum.VxodHepler,
        "Входящее -> Проект",
        "Конвертирует запись входящего в проект."
    ),
    VxodToIdea(
        4L,
        InnerTriggerAreaEnum.VxodHepler,
        "Входящее -> Заметка",
        "Конвертирует запись входящего в заметку."
    ),
    VxodToDenPlan(
        5L,
        InnerTriggerAreaEnum.VxodHepler,
        "Входящее -> Ежедневник",
        "Конвертирует запись входящего в план ежедневника."
    ),
    VxodToStapPlan(
        6L,
        InnerTriggerAreaEnum.VxodHepler,
        "Входящее -> Этап проекта",
        "Конвертирует запись входящего в этап проекта."
    ),
    ScipAddBirthday(
        7L,
        InnerTriggerAreaEnum.HelloDialog,
        "Пропустить ввод дня рождения",
        "Добавляет текущую дату вместо даты рождения пользователя."
    )
    ;

    fun startObjTrig() = StartObjOfTrigger(TypeStartObjOfTrigger.INNERFINISH, this.id.toLong(), this.nameTrig)

    companion object {
        fun getType(id: Long): InnerFinishTriggerEnum? = values().toList().find { it.id == id }
    }
}

enum class InnerTriggerAreaEnum(val id: Long, val code: String, val opisArea: String) {
    HelloDialog(
        1L,
        "HelloDialog",
        "Диалоги и ответы возникающие на старте пользования программой, когда пользователь только осваивается."
    ),
    VxodHepler(
        2L,
        "VxodHepler",
        "Диалоги и ответы возникающие при работе с памяткой для распределения входящих."
    );

    companion object {
        fun getType(id: Long): InnerTriggerAreaEnum? = when (id) {
            HelloDialog.id -> HelloDialog
            VxodHepler.id -> VxodHepler
            else -> null
        }
    }
}
