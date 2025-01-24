package com.serranoie.android.feature.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.serranoie.android.core.domain.result.DataResult
import com.serranoie.android.feature.search.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: SearchAdapter

    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        setupUi()
        setupObservers()

        return binding.root
    }

    private fun setupUi() {
        adapter = SearchAdapter()

        binding.recipesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recipesRecyclerView.adapter = adapter


        binding.searchTextInputLayout.editText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                submitSearchQuery()

                // Hide the keyboard
                val imm =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.searchTextInputLayout.windowToken, 0)

                true
            } else {
                false
            }
        }

        binding.searchTextInputLayout.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    adapter.submitList(emptyList())
                    binding.recipesRecyclerView.isVisible = false
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchResultsState.collect { result ->
                when (result) {
                    is DataResult.Success -> {
                        binding.shimmerLayout.isVisible = false
                        binding.errorTextView.isVisible = false
                        binding.recipesRecyclerView.isVisible = true
                        adapter.submitList(result.data)
                    }

                    is DataResult.Error -> {
                        binding.shimmerLayout.isVisible = false
                        binding.errorTextView.isVisible = true
                        binding.recipesRecyclerView.isVisible = false
                        binding.errorTextView.text = result.exception.message ?: "There was an error searching with that query..."
                    }

                    is DataResult.Loading -> {
                        binding.shimmerLayout.isVisible = true
                        binding.errorTextView.isVisible = false
                        binding.recipesRecyclerView.isVisible = false
                    }
                }
            }
        }
    }

    private fun submitSearchQuery() {
        val query = binding.searchTextInputLayout.editText?.text.toString()
        if (query.isNotBlank()) {
            viewModel.searchRecipes(query)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}