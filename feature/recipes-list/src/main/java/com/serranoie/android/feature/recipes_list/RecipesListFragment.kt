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
import com.facebook.shimmer.ShimmerFrameLayout
import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.core.domain.model.search.Result
import com.serranoie.android.core.domain.result.DataResult
import com.serranoie.android.feature.recipes_list.adapter.RandomRecipeDelegate
import com.serranoie.android.feature.recipes_list.adapter.RecipeDelegateAdapter
import com.serranoie.android.feature.recipes_list.adapter.RecipeListItem
import com.serranoie.android.feature.recipes_list.adapter.TrendingRecipeDelegate
import com.serranoie.android.feature.recipes_list.databinding.FragmentRecipesListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesListFragment : Fragment() {

    private var _binding: FragmentRecipesListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecipesListViewModel by viewModels()

    private lateinit var randomRecipeAdapter: RecipeDelegateAdapter
    private lateinit var trendingRecipeAdapter: RecipeDelegateAdapter

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
        randomRecipeAdapter = RecipeDelegateAdapter(
            listOf(
                RandomRecipeDelegate(viewModel)
            )
        )
        trendingRecipeAdapter = RecipeDelegateAdapter(
            listOf(
                TrendingRecipeDelegate()
            )
        )
        binding.recipesRecyclerView.apply {
            adapter = randomRecipeAdapter
            layoutManager = LinearLayoutManager(context)
        }
        binding.trendingRecyclerView.apply {
            adapter = trendingRecipeAdapter
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
            binding.shimmerTrending,
            trendingRecipeAdapter
        )
        observeData(
            viewModel.recipesState,
            binding.shimmerRandom,
            randomRecipeAdapter
        )
    }

    private fun <T> observeData(
        dataState: Flow<DataResult<List<T>>>,
        shimmerView: ShimmerFrameLayout,
        adapter: RecipeDelegateAdapter
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            dataState.collect { result ->
                binding.apply {
                    when (result) {
                        is DataResult.Success -> {
                            shimmerView.isVisible = false
                            errorTextView.isVisible = false
                            val items = result.data.map {
                                when (adapter.delegates.first()) {
                                    is TrendingRecipeDelegate -> RecipeListItem.TrendingRecipeItem(
                                        it as Result
                                    )

                                    is RandomRecipeDelegate -> RecipeListItem.LatestRecipeItem(it as Recipe)
                                    else -> throw IllegalArgumentException("Unknown delegate type")
                                }
                            }
                            adapter.submitList(items)
                            if (adapter.delegates.first() is RandomRecipeDelegate) {
                                recipesRecyclerView.isVisible = result.data.isNotEmpty()
                            } else {
                                trendingRecyclerView.isVisible = result.data.isNotEmpty()
                            }
                        }

                        is DataResult.Error -> {
                            shimmerView.isVisible = false
                            errorTextView.isVisible = true
                            errorTextView.text = result.exception.message ?: "Unknown error"
                            if (adapter.delegates.first() is RandomRecipeDelegate) {
                                recipesRecyclerView.isVisible = false
                            } else {
                                trendingRecyclerView.isVisible = false
                            }
                        }

                        is DataResult.Loading -> {
                            shimmerView.isVisible = true
                            errorTextView.isVisible = false
                            if (adapter.delegates.first() is RandomRecipeDelegate) {
                                recipesRecyclerView.isVisible = false
                            } else {
                                trendingRecyclerView.isVisible = false
                            }
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
