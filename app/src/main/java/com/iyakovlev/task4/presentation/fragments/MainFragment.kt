package com.iyakovlev.task4.presentation.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.iyakovlev.task4.presentation.common.BaseFragment
import com.iyakovlev.task4.R
import com.iyakovlev.task4.common.Constants
import com.iyakovlev.task4.databinding.FragmentMainBinding
import com.iyakovlev.task4.presentation.adapters.ViewPagerAdapter
import com.iyakovlev.task4.utils.Constants.LOG_TAG
import com.iyakovlev.task4.utils.extensions.capitalizeFirstChar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {
    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null


    private lateinit var preferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.e(LOG_TAG, "main fragment")

        preferences = requireContext().getSharedPreferences(
            Constants.APP_PREFERENCES,
            AppCompatActivity.MODE_PRIVATE
        )
        parseEmailToPrefs()
        setNameInTextView()


//        val pagerAdapter = ViewPagerAdapter(this)
//        binding.viewPager.adapter = pagerAdapter
//
//        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
//            // Set tab titles if needed
//            when (position) {
//                0 -> tab.text = "Fragment 1"
//                1 -> tab.text = "Fragment 2"
//            }
//        }.attach()

//        val viewPagerAdapter = ViewPagerAdapter(this)
//        binding.viewPager.adapter = viewPagerAdapter
//
//        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
//            tab.text = "OBJECT ${(position + 1)}"
//        }.attach()
    }

    private fun parseEmailToPrefs() {
        val email = preferences.getString(Constants.EMAIL, null)
        if (email != null) {
            val leftPart = email.split(Constants.DELIMITER_AT)[0]
            if (leftPart.contains(Constants.DELIMITER_DOT)) {
                val name = leftPart.split(Constants.DELIMITER_DOT)[0].capitalizeFirstChar()
                val surname = leftPart.split(Constants.DELIMITER_DOT)[1].capitalizeFirstChar()
                preferences.edit()
                    .putString(Constants.NAME, name)
                    .putString(Constants.SURNAME, surname)
                    .apply()
            }

        } else {
            preferences.edit()
                .putString(Constants.NAME, getString(R.string.default_name))
                .putString(Constants.SURNAME, getString(R.string.default_lastname))
                .apply()
        }
    }

    private fun setNameInTextView() {
        val name = preferences.getString(Constants.NAME, getString(R.string.default_name))
        val lastname = preferences.getString(Constants.SURNAME, getString(R.string.default_lastname))
        val fullName = "$name $lastname"
        binding.tvFullName.text = fullName
    }

    override fun setObservers() {
        TODO("Not yet implemented")
    }

    override fun setListeners() {
        TODO("Not yet implemented")
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
            }
    }
}