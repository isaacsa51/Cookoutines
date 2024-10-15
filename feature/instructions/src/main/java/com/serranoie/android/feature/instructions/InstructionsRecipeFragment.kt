package com.serranoie.android.feature.instructions

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.serranoie.android.core.domain.result.DataResult
import com.serranoie.android.feature.instructions.databinding.FragmentInstructionsRecipeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InstructionsRecipeFragment : Fragment() {

    companion object {
        fun newInstance() = InstructionsRecipeFragment()
    }

    private val viewModel: InstructionsRecipeViewModel by viewModels()

    private var _binding: FragmentInstructionsRecipeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val recipeIdString = arguments?.getString("recipeId")
        val recipeId = recipeIdString?.toIntOrNull()

        if (recipeId != null) {
            Log.d("InstructionsRecipeFragment", "Recipe ID: $recipeId")
            viewModel.getCurrentRecipe(recipeId) // Trigger data fetching
        } else {
            // Handle case where recipeId is not available (e.g., show error)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInstructionsRecipeBinding.inflate(inflater, container, false)
        val view = binding.root

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.recipeState.collect { state ->
                when (state) {
                    is DataResult.Loading -> {
                        binding.circularLoader.visibility = View.VISIBLE
                    }

                    is DataResult.Success -> {
                        binding.circularLoader.visibility = View.GONE
                        Log.d("InstructionsRecipeFragment", "Recipe Data: ${state.data}")
                    }

                    is DataResult.Error -> {
                        binding.circularLoader.visibility = View.GONE
                        Log.e("InstructionsRecipeFragment", "Error: ${state.exception}")
                    }
                }
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}