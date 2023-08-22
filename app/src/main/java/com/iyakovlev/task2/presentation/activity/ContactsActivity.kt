package com.iyakovlev.task2.presentation.activity

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.commit
import androidx.navigation.fragment.NavHostFragment
import com.iyakovlev.task2.R
import com.iyakovlev.task2.databinding.ActivityContactsBinding
import com.iyakovlev.task2.presentation.common.BaseActivity
import com.iyakovlev.task2.presentation.fragments.ContactsFragment
import com.iyakovlev.task2.utils.Constants.LOG_TAG
import com.iyakovlev.task2.utils.TestingConstants.isUsingTransactions


class ContactsActivity : BaseActivity<ActivityContactsBinding>(ActivityContactsBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LOG_TAG","work?")
        Log.e(LOG_TAG, "activity created")

        if (savedInstanceState == null) {
            if (isUsingTransactions) {
                val fragment = ContactsFragment.newInstance()
                supportFragmentManager.commit {
                    add(R.id.nav_host_fragment, fragment)
                }
            }
        }
    }

}