package com.iyakovlev.task2.utils

import android.util.Log

const val LOG_TAG = "LOG_TAG"

fun log(
    message: String,
    isDebug: Boolean = true
) {
    if (isDebug) Log.d(LOG_TAG, message)
}