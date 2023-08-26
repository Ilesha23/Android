package com.iyakovlev.task4.presentation.activity

import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.iyakovlev.task4.databinding.MainLayoutBinding
import com.iyakovlev.task4.presentation.adapters.ViewPagerAdapter
import com.iyakovlev.task4.presentation.common.BaseActivity
import com.iyakovlev.task4.presentation.fragments.MainFragment

class MainActivity : BaseActivity<MainLayoutBinding>(MainLayoutBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pagerAdapter = ViewPagerAdapter(this)
        binding.viewPager?.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout!!, binding.viewPager!!) { tab, position ->
            when (position) {
                0 -> tab.text = "Main fragment"
                1 -> tab.text = "Contacts fragment"
            }
        }.attach()
    }

}