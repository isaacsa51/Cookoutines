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
import com.serranoie.android.feature.recipes_list.databinding.FragmentRecipesListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipesListFragment : Fragment() {

    private var _binding: FragmentRecipesListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecipesListViewModel by viewModels()
    private lateinit var recipesAdapter: RecipesAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesListBinding.inflate(inflater, container, false)

        setupUi()
        setupObservers()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupUi() {
        recipesAdapter = RecipesAdapter()
        binding.recipesRecyclerView.apply {
            adapter = recipesAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.recipesState.collect { result ->
                when (result) {
                    is com.serranoie.android.core.domain.result.DataResult.Success -> {
                        // Hide loading/error views and show data
                        binding.progressBar.isVisible = false
                        binding.errorTextView.isVisible = false
                        recipesAdapter.submitList(result.data)
                    }

                    is com.serranoie.android.core.domain.result.DataResult.Error -> {
                        // Show error message and hide RecyclerView
                        binding.progressBar.isVisible = false
                        binding.errorTextView.isVisible = true
                        binding.errorTextView.text = result.exception.message ?: "Unknown error"
                    }

                    is com.serranoie.android.core.domain.result.DataResult.Loading -> {
                        // Show loading view and hide data/error
                        binding.progressBar.isVisible = true
                        binding.errorTextView.isVisible = false
                    }
                }
            }
        }
    }
}
