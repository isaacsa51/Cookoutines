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
import androidx.recyclerview.widget.ListAdapter
import com.facebook.shimmer.ShimmerFrameLayout
import com.serranoie.android.core.domain.result.DataResult
import com.serranoie.android.feature.recipes_list.databinding.FragmentRecipesListBinding
import com.serranoie.android.feature.recipes_list.trending.TrendingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
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

    private fun setupObservers() {
        observeData(
            viewModel.trendingRecipesState,
            trendingAdapter,
            binding.shimmerTrending
        )
        observeData(
            viewModel.recipesState,
            recipesAdapter,
            binding.shimmerRandom
        )
    }

    private fun <T> observeData(
        dataState: Flow<DataResult<List<T>>>,
        adapter: ListAdapter<T, *>,
        shimmerView: ShimmerFrameLayout
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            dataState.collect { result ->
                binding.apply {
                    when (result) {
                        is DataResult.Success -> {
                            shimmerView.isVisible = false
                            errorTextView.isVisible = false
                            adapter.submitList(result.data)
                            recipesRecyclerView.isVisible = result.data.isNotEmpty()
                        }

                        is DataResult.Error -> {
                            shimmerView.isVisible = false
                            errorTextView.isVisible = true
                            errorTextView.text = result.exception.message ?: "Unknown error"
                            recipesRecyclerView.isVisible = false
                        }

                        is DataResult.Loading -> {
                            shimmerView.isVisible = true
                            errorTextView.isVisible = false
                            recipesRecyclerView.isVisible = false
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
