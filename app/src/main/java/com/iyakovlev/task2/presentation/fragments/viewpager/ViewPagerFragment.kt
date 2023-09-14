package com.iyakovlev.task2.presentation.fragments.viewpager

import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import com.iyakovlev.task2.databinding.FragmentViewPagerBinding
import com.iyakovlev.task2.presentation.base.BaseFragment
import com.iyakovlev.task2.presentation.fragments.viewpager.adapters.ViewPagerAdapter

class ViewPagerFragment :
    BaseFragment<FragmentViewPagerBinding>(FragmentViewPagerBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pagerAdapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (ViewPagerAdapter.FragmentType.values()[position]) {
                ViewPagerAdapter.FragmentType.MAIN_FRAGMENT -> tab.text = "Profile"
                ViewPagerAdapter.FragmentType.CONTACTS_FRAGMENT -> tab.text = "Contacts"
            }
        }.attach()
    }

}