package com.iyakovlev.contacts.presentation.fragments.search

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iyakovlev.contacts.R
import com.iyakovlev.contacts.common.constants.Constants.ISDEBUG
import com.iyakovlev.contacts.common.resource.Resource
import com.iyakovlev.contacts.databinding.FragmentSearchBinding
import com.iyakovlev.contacts.presentation.base.BaseFragment
import com.iyakovlev.contacts.presentation.fragments.search.adapters.SearchAdapter
import com.iyakovlev.contacts.presentation.utils.ItemSpacingDecoration
import com.iyakovlev.contacts.presentation.utils.extensions.toggleLoading
import com.iyakovlev.contacts.utils.log
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {

    private val viewModel: SearchViewModel by viewModels()

    private val searchAdapter = SearchAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pbContacts.toggleLoading(true)

        setupRecyclerView()
        setListeners()
        setObservers()

    }

    private fun makeUpButton() {
        binding.fabUp.icon =
            ContextCompat.getDrawable(requireContext(), R.drawable.floating_action_button_up)
        val layoutManager = binding.rvContacts.layoutManager as LinearLayoutManager
        val firstItem = layoutManager.findFirstCompletelyVisibleItemPosition()
        if (firstItem == 0) {
            binding.fabUp.hide()
        } else {
            binding.fabUp.show()
        }
        binding.rvContacts.apply {
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val fi = layoutManager.findFirstCompletelyVisibleItemPosition()
                    if (fi == 0) {
                        binding.fabUp.hide()
                    } else if (dy > 0) {
                        binding.fabUp.show()
                    }
                }
            })
        }
    }

    private fun setupRecyclerView() {
        binding.rvContacts.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchAdapter
            val spacing = resources.getDimensionPixelSize(R.dimen.contacts_item_spacing)
            val lastSpacing = resources.getDimensionPixelSize(R.dimen.last_item_bottom_spacing)
            if (itemDecorationCount == 0) {
                addItemDecoration(ItemSpacingDecoration(spacing, lastSpacing))
            }
        }
    }

    private fun setObservers() {
        viewModel.updateContacts()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.cachedList.collect {
                        log("cached list submitted: ${viewModel.cachedList.value}", ISDEBUG)
                        searchAdapter.submitList(it)
                        toggleSearchInfo(it)
                    }
                }
                launch {
                    viewModel.state.collect { list ->
                        log("contacts list submit", ISDEBUG)
                        searchAdapter.submitList(list.data)
                        if (viewModel.state.value is Resource.Error) {
                            binding.pbContacts.toggleLoading(false)
                            Toast.makeText(
                                context,
                                getString(viewModel.state.value.message ?: R.string.error),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (viewModel.state.value is Resource.Success) {
                            binding.pbContacts.toggleLoading(false)
                        }
                        list.data?.let { toggleSearchInfo(it) }
                    }
                }

            }
        }
    }

    private fun setListeners() {
        with(binding) {
            fabUp.setOnClickListener {
                rvContacts.smoothScrollToPosition(0)
                binding.fabUp.hide()
            }
            ibBack.setOnClickListener {// TODO: ???
                navController.navigateUp()
//                navController.navigate(Uri.parse("myapp://com.iyakovlev.contacts/contacts"))
            }
            svContacts.setOnSearchClickListener {
                tvHeader.visibility = View.INVISIBLE
                ibBack.visibility = View.INVISIBLE
                it.layoutParams.width = MATCH_PARENT
            }
            svContacts.setOnCloseListener {
                tvHeader.visibility = View.VISIBLE
                ibBack.visibility = View.VISIBLE
                svContacts.layoutParams.width = WRAP_CONTENT
                viewModel.setFilter(null)
                searchAdapter.submitList(viewModel.state.value.data)
                viewModel.state.value.data?.let { toggleSearchInfo(it) }
                false
            }
            svContacts.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    viewModel.setFilter(p0)
                    log("setted filter: $p0", ISDEBUG)
                    return true
                }

            })
        }
    }

    private fun toggleSearchInfo(list: List<*>) {
        with(binding) {
            if (list.isEmpty()) {
                tvSearchNotFound.visibility = View.VISIBLE
                tvSearchRecommendation.visibility = View.VISIBLE
            } else {
                tvSearchNotFound.visibility = View.INVISIBLE
                tvSearchRecommendation.visibility = View.INVISIBLE
            }
        }
    }

}