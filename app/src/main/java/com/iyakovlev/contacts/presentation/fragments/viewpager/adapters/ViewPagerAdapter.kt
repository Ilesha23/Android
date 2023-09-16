package com.iyakovlev.contacts.presentation.fragments.viewpager.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.iyakovlev.contacts.presentation.fragments.contacts.ContactsFragment
import com.iyakovlev.contacts.presentation.fragments.main.MainFragment

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    enum class FragmentType {
        MAIN_FRAGMENT,
        CONTACTS_FRAGMENT
    }

    override fun getItemCount(): Int = FragmentType.values().size

    override fun createFragment(position: Int): Fragment {
        return when (FragmentType.values()[position]) {
            FragmentType.MAIN_FRAGMENT -> MainFragment()
            FragmentType.CONTACTS_FRAGMENT -> ContactsFragment()
        }
    }

}