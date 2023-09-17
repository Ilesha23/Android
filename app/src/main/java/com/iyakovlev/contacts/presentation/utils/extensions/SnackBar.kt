package com.iyakovlev.contacts.presentation.utils.extensions

import android.os.CountDownTimer
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.iyakovlev.contacts.R
import com.iyakovlev.contacts.common.constants.Constants

fun Snackbar.showSnackBar(
    view: View,
    message: String,
    actionString: String,
    action: () -> Unit
) {
//    val message = getString(R.string.contact_deleted_snackbar)
    val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)

    val timer = object : CountDownTimer(Constants.SNACK_BAR_LENGTH, Constants.SNACK_BAR_TICK_RATE) {
        override fun onTick(millisUntilFinished: Long) {
            snackBar.setAction("$actionString ${millisUntilFinished / Constants.SNACK_BAR_TICK_RATE}") {
                action()
            }
        }

        override fun onFinish() {
            snackBar.dismiss()
        }
    }

    timer.start()
    snackBar.show()
}