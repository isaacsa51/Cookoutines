package com.serranoie.android.feature.recipes_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        setupSwipeToRefresh()

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
    }

    private fun setupSwipeToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshRecipes()
            binding.swipeRefreshLayout.isRefreshing = false
        }
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.recipesState.collect { result ->
                binding.let { safeBinding ->
                    when (result) {
                        is DataResult.Success -> {
                            safeBinding.progressBar.isVisible = false
                            safeBinding.errorTextView.isVisible = false
                            recipesAdapter.submitList(result.data)
                        }

                        is DataResult.Error -> {
                            safeBinding.progressBar.isVisible = false
                            safeBinding.errorTextView.isVisible = true
                            safeBinding.errorTextView.text =
                                result.exception.message ?: "Unknown error"
                        }

                        is DataResult.Loading -> {
                            safeBinding.progressBar.isVisible = true
                            safeBinding.errorTextView.isVisible = false
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
