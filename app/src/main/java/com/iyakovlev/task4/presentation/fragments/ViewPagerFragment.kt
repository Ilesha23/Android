package com.iyakovlev.task4.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import com.iyakovlev.task4.databinding.FragmentViewPagerBinding
import com.iyakovlev.task4.presentation.adapters.ViewPagerAdapter
import com.iyakovlev.task4.presentation.common.BaseFragment

class ViewPagerFragment :
    BaseFragment<FragmentViewPagerBinding>(FragmentViewPagerBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pagerAdapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Your Profile"
                1 -> tab.text = "Contacts"
            }
        }.attach()
        Log.e("Log", "vp created")
    }

    override fun setObservers() {
        TODO("Not yet implemented")
    }

    override fun setListeners() {
        TODO("Not yet implemented")
    }
}