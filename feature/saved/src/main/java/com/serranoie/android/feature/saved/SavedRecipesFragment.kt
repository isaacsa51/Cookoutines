package com.serranoie.android.feature.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.serranoie.android.core.domain.result.DataResult
import com.serranoie.android.feature.saved.databinding.FragmentSavedRecipesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SavedRecipesFragment : Fragment() {

    private var _binding: FragmentSavedRecipesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SavedRecipesViewModel by viewModels()
    private lateinit var adapter: SavedRecipesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedRecipesBinding.inflate(inflater, container, false)

        setupUi()
        setupObservers()

        return binding.root
    }

    private fun setupUi() {
        adapter = SavedRecipesAdapter(viewModel, viewLifecycleOwner.lifecycleScope)
        binding.postsRecyclerView.adapter = adapter
        binding.postsRecyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.recipesState.collect { result ->
                when (result) {
                    is DataResult.Success -> {
                        binding.progressBar.isVisible = false
                        // binding.errorTextView.isVisible = false
                        adapter.submitList(result.data)
                    }

                    is DataResult.Loading -> {
                        binding.progressBar.isVisible = true
                        // binding.errorTextView.isVisible = false
                    }

                    is DataResult.Error -> {
                        binding.progressBar.isVisible = false
                        // binding.errorTextView.isVisible = true
                        binding.errorTextLabel.text = result.exception.message
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