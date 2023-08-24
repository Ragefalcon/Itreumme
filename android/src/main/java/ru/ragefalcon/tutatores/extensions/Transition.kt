package ru.ragefalcon.tutatores.extensions

import android.util.Log
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
//            .addTransition(ChangeClipBounds()) //.addTarget(ImageView::class.java) // Only for ImageViews
        .addTransition(ChangeBounds()) // For both
        .addTransition(ChangeTransform())//.addTarget(ConstraintLayout::class.java) // Only for TextViews
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
//            .setInterpolator {
//                it*it
//            }

    return transition
}

fun Transition.addMyListener(
    start: (Transition) -> Unit = {},
    end: (Transition) -> Unit = {},
    cancel: (Transition) -> Unit = {},
    pause: (Transition) -> Unit = {},
    resume: (Transition) -> Unit = {},
): Transition {
    return addListener(object : Transition.TransitionListener {
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
}
fun Transition.addMyEndListener(
    end: (Transition) -> Unit = {},
): Transition {
    return addListener(object : Transition.TransitionListener {
        override fun onTransitionStart(transition: Transition) {
        }

        override fun onTransitionEnd(transition: Transition) {
            end(transition)
        }

        override fun onTransitionCancel(transition: Transition) {
        }

        override fun onTransitionPause(transition: Transition) {
        }

        override fun onTransitionResume(transition: Transition) {
        }

    })
}