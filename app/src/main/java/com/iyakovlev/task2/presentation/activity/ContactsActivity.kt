package com.iyakovlev.task2.presentation.activity

import android.os.Bundle
import androidx.fragment.app.commit
import com.iyakovlev.task2.R
import com.iyakovlev.task2.common.constants.TestingConstants.isUsingTransactions
import com.iyakovlev.task2.databinding.ActivityContactsBinding
import com.iyakovlev.task2.presentation.base.BaseActivity
import com.iyakovlev.task2.presentation.fragments.contacts.ContactsFragment
import com.iyakovlev.task2.utils.log


class ContactsActivity : BaseActivity<ActivityContactsBinding>(ActivityContactsBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        log("activity created")

        if (savedInstanceState == null) {
            if (isUsingTransactions) {
                val fragment = ContactsFragment()
                supportFragmentManager.commit {
                    add(R.id.nav_host_fragment, fragment)
                }
            }
        }
    }

}