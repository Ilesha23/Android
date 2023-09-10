package com.iyakovlev.task2.presentation.activity

import android.os.Bundle
import androidx.fragment.app.commit
import androidx.navigation.fragment.NavHostFragment
import com.iyakovlev.task2.R
import com.iyakovlev.task2.common.constants.TestingConstants.isUsingTransactions
import com.iyakovlev.task2.databinding.ActivityContactsBinding
import com.iyakovlev.task2.presentation.base.BaseActivity
import com.iyakovlev.task2.presentation.fragments.contacts.ContactsFragment
import com.iyakovlev.task2.utils.log
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ContactsActivity : BaseActivity<ActivityContactsBinding>(ActivityContactsBinding::inflate) {

    private val isDebug = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        log("activity created", isDebug)



        if (savedInstanceState == null) {
            if (isUsingTransactions) {
                val fragment = ContactsFragment()
                supportFragmentManager.commit {
                    add(R.id.nav_host_fragment, fragment)
                }
            } else {
                val navHostFragment =
                    NavHostFragment.create(R.navigation.nav_graph)
                supportFragmentManager.commit {
                    replace(R.id.nav_host_fragment, navHostFragment)
                    setPrimaryNavigationFragment(navHostFragment)
                }
            }
        }
    }

}