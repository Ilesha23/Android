package com.iyakovlev.contacts.presentation.utils.extensions

import android.os.CountDownTimer
import com.google.android.material.snackbar.Snackbar
import com.iyakovlev.contacts.common.constants.Constants

fun Snackbar.showSnackBarWithTimer(
    actionString: String,
    action: () -> Unit
) {

    val timer = object : CountDownTimer(Constants.SNACK_BAR_LENGTH, Constants.SNACK_BAR_TICK_RATE) {
        override fun onTick(millisUntilFinished: Long) {
            this@showSnackBarWithTimer.setAction("$actionString ${millisUntilFinished / Constants.SNACK_BAR_TICK_RATE}") {
                action()
            }
        }

        override fun onFinish() {
            this@showSnackBarWithTimer.dismiss()
        }
    }

    timer.start()
    this@showSnackBarWithTimer.show()
}