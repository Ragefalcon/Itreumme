package ru.ragefalcon.tutatores.extensions

import androidx.transition.ChangeBounds
import androidx.transition.ChangeTransform
import androidx.transition.Transition
import androidx.transition.TransitionSet

fun getMyTransition(
    start: (Transition) -> Unit = {},
    end: (Transition) -> Unit = {},
    cancel: (Transition) -> Unit = {},
    pause: (Transition) -> Unit = {},
    resume: (Transition) -> Unit = {},
    duration: Long = 400
): Transition {
    val transition: Transition = TransitionSet()
        .addTransition(ChangeBounds())
        .addTransition(ChangeTransform())
        .setDuration(duration)
        .addListener(object : Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition) {
                start(transition)
            }

            override fun onTransitionEnd(transition: Transition) {
                end(transition)
            }

            override fun onTransitionCancel(transition: Transition) {
                cancel(transition)
            }

            override fun onTransitionPause(transition: Transition) {
                pause(transition)
            }

            override fun onTransitionResume(transition: Transition) {
                resume(transition)
            }

        })
    return transition
}