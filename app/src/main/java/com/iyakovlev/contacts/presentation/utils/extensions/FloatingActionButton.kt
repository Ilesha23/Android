package com.iyakovlev.contacts.presentation.utils.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

fun ExtendedFloatingActionButton.toggleFabVisibility(duration: Long, show: Boolean) {
    if (show) {
        this.visibility = View.VISIBLE
        this.animate()
            .alpha(1f)
            .setDuration(duration)
            .setListener(null)
            .start()
    } else {
        this.animate()
            .alpha(0f)
            .setDuration(duration)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    this@toggleFabVisibility.visibility = View.INVISIBLE
                }
            })
            .start()
    }
}