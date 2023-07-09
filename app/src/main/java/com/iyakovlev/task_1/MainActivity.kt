package com.iyakovlev.task_1

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.iyakovlev.task_1.databinding.MainLayoutBinding

class MainActivity : ComponentActivity() {
    lateinit var binding: MainLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        val name = preferences.getString(NAME, "Name")
        val surname = preferences.getString(SURNAME, "Surname")

        binding.nameText.text = "$name $surname"

    }
}