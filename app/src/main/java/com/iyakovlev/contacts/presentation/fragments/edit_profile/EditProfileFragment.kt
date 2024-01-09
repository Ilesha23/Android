package com.iyakovlev.contacts.presentation.fragments.edit_profile

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.iyakovlev.contacts.R
import com.iyakovlev.contacts.domain.states.Resource
import com.iyakovlev.contacts.databinding.FragmentEditProfileBinding
import com.iyakovlev.contacts.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@AndroidEntryPoint
class EditProfileFragment :
    BaseFragment<FragmentEditProfileBinding>(FragmentEditProfileBinding::inflate) {

    private val viewModel: EditProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setData()

        setListeners()
        setObservers()

    }

    private fun setListeners() {
        with(binding) {
            ibBack.setOnClickListener {
                navController.navigateUp()
            }
            btnSave.setOnClickListener {
                save()
            }
            etBirth.setOnClickListener {
                showDatePicker()
            }
        }
    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {
                    when (it) {
                        is Resource.Error -> {
                            toggleLoading(false)
                            Toast.makeText(context, "error editing profile", Toast.LENGTH_SHORT)
                                .show()
                        }

                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            toggleLoading(false)
                            navController.navigateUp()
                        }
                    }
                }
            }
        }
    }

    private fun setData() {
        with(binding) {
            val bd = viewModel.user.birthday
            etUsername.setText(viewModel.user.name)
            etCareer.setText(viewModel.user.career)
            etPhone.setText(viewModel.user.phone)
            etAddress.setText(viewModel.user.address)
            etBirth.setText(
                bd?.substring(8, 10) + "-" + bd?.substring(5, 7) + "-" + bd?.substring(0, 4)
            ) // ?
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                updateEditText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = 0
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun updateEditText(calendar: Calendar) {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(calendar.time)
        binding.etBirth.setText(formattedDate)
    }

    private fun save() {
        with(binding) {
            viewModel.editUser(
                etUsername.text.toString(),
                etCareer.text.toString(),
                etPhone.text.toString(),
                etAddress.text.toString(),
                etBirth.text.toString()
            )
        }
    }

    private fun toggleLoading(isLoading: Boolean) {
        with(binding) {
            if (isLoading) {
                pbEdit.visibility = View.VISIBLE
                btnSave.text = ""
            } else {
                pbEdit.visibility = View.GONE
                btnSave.text = getString(R.string.save)
            }
        }
    }

}