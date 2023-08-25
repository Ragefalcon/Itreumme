package ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers

import com.soywiz.klock.DateTimeTz
import ru.ragefalcon.sharedcode.Database
import ru.ragefalcon.sharedcode.extensions.localUnix
import ru.ragefalcon.sharedcode.extensions.withOffset
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.*
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.InnerDialogs.InnerFinishTriggerEnum
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.LoadQuestVMobjForSpis

class PrivateCommonFun(private val mdb: Database, private val loadQSpis: LoadQuestVMobjForSpis) {
    fun runTriggerReact(quest_id: String, typeParentOfTrig: TypeParentOfTrig, elementId: Long, date: Long? = null) {
        mdb.questCommonTriggerQueries.selectForParent(quest_id, typeParentOfTrig.code, elementId).executeAsList()
            .let { findTrig ->
                findTrig.forEach { trig ->
                    when (TypeStartObjOfTrigger.getType(trig.type_trig_id)) {

                        TypeStartObjOfTrigger.INNERFINISH -> {
                            loadQSpis.innerFinishTriggerAction.setValue(InnerFinishTriggerEnum.getType(trig.child_id))
                        }

                        TypeStartObjOfTrigger.STARTPLAN -> {
                            mdb.spisPlanQueries.unlockQuestPlan(
                                TypeStatPlan.UNBLOCKNOW.codValue,
                                quest_id.toLong(),
                                trig.child_id,
                                listOf(TypeStatPlan.BLOCK.codValue, TypeStatPlan.INVIS.codValue)
                            )
                        }

                        TypeStartObjOfTrigger.STARTSTAP -> {
                            mdb.spisStapPlanQueries.unlockQuestPlanStap(
                                TypeStatPlan.UNBLOCKNOW.codValue,
                                quest_id.toLong(),
                                trig.child_id,
                                listOf(TypeStatPlan.BLOCK.codValue, TypeStatPlan.INVIS.codValue)
                            )
                        }

                        TypeStartObjOfTrigger.STARTDIALOG -> {
                            mdb.dialogLineQueries.insertOrReplace(
                                TypeDialogMessage.QUESTDIALOG.code,
                                trig.quest_id.toLong(),
                                trig.child_id,
                                trig.child_name,
                                date ?: DateTimeTz.nowLocal().withOffset().localUnix(),
                                0L
                            )
                        }

                        TypeStartObjOfTrigger.SUMTRIGGER -> {
                            loadQSpis.innerFinishTriggerAction.setValue(InnerFinishTriggerEnum.Cancel)
                        }

                        TypeStartObjOfTrigger.STARTTREE -> {
                            mdb.spisTreeSkillQueries.unlockTree(
                                stat = TypeStatTreeSkills.UNBLOCKNOW.codValue,
                                quest_id = quest_id.toLong(),
                                quest_key_id = trig.child_id,
                                statlock = listOf(TypeStatTreeSkills.BLOCK.codValue, TypeStatTreeSkills.INVIS.codValue)
                            )
                        }

                        TypeStartObjOfTrigger.STARTNODETREE -> {
                            mdb.spisNodeTreeSkillsQueries.unlockNode(
                                stat = TypeStatPlan.UNBLOCKNOW.codValue,
                                quest_id = quest_id.toLong(),
                                quest_key_id = trig.child_id,
                                statlock = listOf(TypeStatPlan.BLOCK.codValue, TypeStatPlan.INVIS.codValue)
                            )
                        }

                        TypeStartObjOfTrigger.STARTLEVELTREE -> {
                            loadQSpis.innerFinishTriggerAction.setValue(InnerFinishTriggerEnum.Cancel)
                        }

                        null -> {
                            loadQSpis.innerFinishTriggerAction.setValue(InnerFinishTriggerEnum.Cancel)
                        }
                    }
                }
                if (findTrig.isEmpty()) loadQSpis.innerFinishTriggerAction.setValue(InnerFinishTriggerEnum.Cancel)
            }
    }
}