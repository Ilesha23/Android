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
        val email = preferences.getString(EMAIL, "name.surname@example.com")
        println(email)

        val leftPart = email!!.split("@")[0]
        var name = "Name"
        var surname = "Surname"
        if (leftPart.contains(".")) {
            name = leftPart.split(".")[0].replaceFirstChar { it.uppercaseChar() }
            surname = leftPart.split(".")[1].replaceFirstChar { it.uppercaseChar() }
        }

        binding.nameText.text = "$name $surname"

    }
}