package com.iyakovlev.task4.presentation.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.iyakovlev.task4.presentation.fragments.ContactsFragment
import com.iyakovlev.task4.presentation.fragments.MainFragment

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MainFragment()
            1 -> ContactsFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

}