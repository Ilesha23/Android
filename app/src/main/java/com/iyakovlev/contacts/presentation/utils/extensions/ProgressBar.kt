package com.iyakovlev.contacts.presentation.utils.extensions

import android.view.View
import android.widget.ProgressBar

fun ProgressBar.toggleLoading(
    isLoading: Boolean
) {
    if (isLoading) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}