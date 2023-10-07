package ru.ragefalcon.tutatores.story

import androidx.fragment.app.Fragment
import ru.ragefalcon.tutatores.commonfragments.BodyTutDialog
import ru.ragefalcon.tutatores.commonfragments.FragmentTutDialog
import ru.ragefalcon.tutatores.extensions.*
import java.lang.ref.WeakReference

class VoiceOver(val fragment: WeakReference<Fragment>) {

    fun showDialog(body: SpisVODialog) {
        fragment.get()?.showMyFragDial(FragmentTutDialog(body.ordinal), cancelable = false)
    }

    companion object {
        fun getFunBody(key: Int): BodyTutDialog.() -> Unit {
            return SpisVODialog.values()[key].body
        }

        enum class SpisVODialog(val body: BodyTutDialog.() -> Unit) {
            VO_FIRST({
                addText(
                    "Привет путник, мы с тобой еще не знакомы, поэтому я постараюсь аккуратно и постепенно ввести тебя в курс дела.\n" +
                            "Возможно, с чем то ты уже знаком, но думаю у меня найдется, что тебе предложить и чем тебе помочь на твоем нелегком пути. " +
                            "Если дальше мы пойдем вместе, то нам еще многим предстоит поделиться друг с другом, но я верю что мы оба от этого только выиграем.\n" +
                            "Итак, начнем потихоньку."
                )
                addButton("Далее") {
                    this.clear()
                    VO_BIRTHDAY.body(this)
                }
            }),
            VO_BIRTHDAY({
                addText(
                    "Расскажи когда ты пришел в этот мир путник? Нам всем важно об этом помнить, " +
                            "т.к. это один из самых мощных факторов, который одновременно и ограничивает наш путь и заставляет нас по нему усиленно идти." +
                            "Если забыть про него, то можно увязнуть в потоке времени и рутине, теряя драгоценное время...\n" +
                            "Впрочем если пока еще не доверяешь мне, можешь указать что угодно, после сможешь поправить дату нажав на свое время жизни...\n(Укажи дату своего рождения):"
                )
                addDateEdit(java.util.Date().time.toString()) {
                    date = it
                }
                addButton("Далее") {
                    frag.get()?.getVM()?.addAvatar?.addBirthday(date)
                    frag.get()?.dismissDial {
                        frag.get()?.getSFM()?.setFragmentResult("FinalStartDialog", androidx.core.os.bundleOf())
                    }
                }
            }),
            VO_BIRTHDAY_UPDATE({
                frag.get()?.let { frag ->
                    addText(
                        "Решил, что готов указать реальную дату или понял, что ошибся? Можешь ввести новую дату.\n(Укажи дату своего рождения):"
                    )
                    addDateEdit(
                        frag.getVM().avatarSpis.spisMainParam.getLiveData().value?.let { it.find { it.name == "Birthday" }?.stringparam }
                            ?: java.util.Date().time.toString()) {
                        date = it
                    }
                    addButton("Далее") {
                        frag.getVM().addAvatar.addBirthday(date)
                        frag.dismissDial {
                            frag.getSFM().setFragmentResult("FinalStartDialog", androidx.core.os.bundleOf())
                        }
                    }
                }
            }),
            VO_SELECT_RAZDEL({
                frag.get()?.let { frag ->
                    addText("Куда дальше?")
                    addButton("Аватар") {
                        frag.dismissDial {
                            frag.getSFM().setFragmentResult(
                                "actionStartToAvatar",
                                androidx.core.os.bundleOf()
                            )
                        }
                    }
                    addButton("Журнал") {
                        frag.dismissDial {
                            frag.getSFM().setFragmentResult(
                                "actionStartToJournal",
                                androidx.core.os.bundleOf()
                            )
                        }
                    }
                    addButton("Время") {
                        frag.dismissDial {
                            frag.getSFM().setFragmentResult(
                                "actionStartToTime",
                                androidx.core.os.bundleOf()
                            )
                        }
                    }
                    addButton("Финансы") {
                        frag.dismissDial {
                            frag.getSFM().setFragmentResult(
                                "actionStartToFinance",
                                androidx.core.os.bundleOf()
                            )
                        }
                    }
                }
            });
        }
    }

}

