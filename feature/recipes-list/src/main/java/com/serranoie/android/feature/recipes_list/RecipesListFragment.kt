package com.serranoie.android.feature.recipes_list

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
import com.serranoie.android.feature.recipes_list.databinding.FragmentRecipesListBinding
import com.serranoie.android.feature.recipes_list.trending.TrendingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesListFragment : Fragment() {

    private var _binding: FragmentRecipesListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecipesListViewModel by viewModels()
    private lateinit var recipesAdapter: RecipesAdapter
    private lateinit var trendingAdapter: TrendingAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesListBinding.inflate(inflater, container, false)

        setupUi()
        setupObservers()
        setupPopularAdapter()
        setupTrendingAdapter()

        return binding.root
    }

    private fun setupUi() {
        recipesAdapter = RecipesAdapter(viewModel)
        binding.recipesRecyclerView.apply {
            adapter = recipesAdapter
            layoutManager = LinearLayoutManager(context)
        }

        trendingAdapter = TrendingAdapter()
        binding.trendingRecyclerView.apply {
            adapter = trendingAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

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
                    viewModel.resetToRandomRecipes()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

    }

    private fun setupTrendingAdapter() {
        lifecycleScope.launch {
            viewModel.trendingRecipesState.collect { result ->
                when (result) {
                    is DataResult.Success -> trendingAdapter.submitList(result.data)
                    is DataResult.Error -> binding.errorTextView.text =
                        result.exception.message ?: "Unknown error"

                    is DataResult.Loading -> {}
                }
            }
        }
    }

    private fun setupPopularAdapter() {
        lifecycleScope.launch {
            viewModel.trendingRecipesState.collect { result ->
                when (result) {
                    is DataResult.Success -> {
                        trendingAdapter.submitList(result.data)
                    }

                    is DataResult.Error -> {
                        binding.errorTextView.text = result.exception.message ?: "Unknown error"
                    }

                    is DataResult.Loading -> {
                    }
                }
            }
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.recipesState.collect { result ->
                when (result) {
                    is DataResult.Success -> {
                        binding.progressBar.isVisible = false
                        binding.errorTextView.isVisible = false
                        recipesAdapter.submitList(result.data)
                    }

                    is DataResult.Error -> {
                        binding.progressBar.isVisible = false
                        binding.errorTextView.isVisible = true
                        binding.errorTextView.text = result.exception.message ?: "Unknown error"
                    }

                    is DataResult.Loading -> {
                        binding.progressBar.isVisible = true
                        binding.errorTextView.isVisible = false
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.searchResultsState.collect { result ->
                when (result) {
                    is DataResult.Success -> {
                        binding.progressBar.isVisible = false
                        binding.errorTextView.isVisible = false
                        recipesAdapter.submitList(result.data)
                    }

                    is DataResult.Error -> {
                        binding.progressBar.isVisible = false
                        binding.errorTextView.isVisible = true
                        binding.errorTextView.text = result.exception.message ?: "Unknown error"
                    }

                    is DataResult.Loading -> {
                        binding.progressBar.isVisible = true
                        binding.errorTextView.isVisible = false
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
