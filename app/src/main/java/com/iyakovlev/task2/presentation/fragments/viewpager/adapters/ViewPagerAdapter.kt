package com.iyakovlev.task2.presentation.fragments.viewpager.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.iyakovlev.task2.presentation.fragments.contacts.ContactsFragment
import com.iyakovlev.task2.presentation.fragments.main.MainFragment

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> MainFragment()
            0 -> ContactsFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

}