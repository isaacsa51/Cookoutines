package com.serranoie.android.feature.instructions

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.serranoie.android.core.domain.result.DataResult
import com.serranoie.android.feature.instructions.databinding.FragmentInstructionsRecipeBinding
import com.serranoie.android.feature.instructions.directions.DirectionsAdapter
import com.serranoie.android.feature.instructions.directions.DirectionsViewModel
import com.serranoie.android.feature.instructions.ingredients.IngredientsAdapter
import com.serranoie.android.feature.instructions.utils.CuisineTypeAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InstructionsRecipeFragment : Fragment() {

    private val viewModel: InstructionsRecipeViewModel by viewModels()
    private val directionsViewModel: DirectionsViewModel by viewModels()

    private var _binding: FragmentInstructionsRecipeBinding? = null
    private val binding get() = _binding!!

    private lateinit var directionsAdapter: DirectionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val recipeIdString = arguments?.getString("recipeId")
        val recipeId = recipeIdString?.toIntOrNull()

        if (recipeId != null) {
            viewModel.getCurrentRecipe(recipeId)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInstructionsRecipeBinding.inflate(inflater, container, false)
        val view = binding.root

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewLifecycleOwner.lifecycleScope.launch {
            directionsViewModel.instructions.collect { result ->
                when (result) {
                    is DataResult.Loading -> {
                        // Show a loading indicator in your UI
                    }
                    is DataResult.Success -> {
                        directionsAdapter.submitList(result.data) // Submit the list here
                    }
                    is DataResult.Error -> {
                        // Handle the error, e.g., show an error message
                        Log.e("InstructionsRecipeFragment", "Error loading instructions: ${result.exception}")
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.recipeState.collect { state ->
                when (state) {
                    is DataResult.Loading -> {
                        binding.circularLoader.visibility = View.VISIBLE
                    }

                    is DataResult.Success -> {
                        binding.circularLoader.visibility = View.GONE

                        Log.e("InstructionsRecipeFragment", "DATA: ${state.data}")

                        binding.collapsingToolbarLayout.title = state.data.title

                        binding.readyTimeInfo.text =
                            state.data.readyInMinutes.toString() + " minutes"
                        binding.servingsInfo.text = state.data.servings.toString() + " servings"


                        binding.summaryInfo.text = HtmlCompat.fromHtml(
                            state.data.summary.toString(),
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        )


                        // * CUISINES
                        if (state.data.cuisines.toString() == "[]") {
                            binding.cuisineRecyclerView.visibility = View.GONE
                        } else {
                            val cuisineList = state.data.cuisines.toString()
                                .removeSurrounding("[", "]")
                                .split(", ")
                            val cuisineAdapter = CuisineTypeAdapter(cuisineList)
                            binding.cuisineRecyclerView.adapter = cuisineAdapter
                        }

                        // * DIRECTIONS
                        directionsAdapter = DirectionsAdapter()
                        binding.directionsRecyclerView.adapter = directionsAdapter
                        directionsViewModel.getRecipeInstructions(state.data.id!!)

                        // * INGREDIENTS
                        val ingredientsAdapter = IngredientsAdapter(state.data.extendedIngredients!!)
                        binding.ingredientsRecyclerView.adapter = ingredientsAdapter
                        binding.totalIngredientsLabel.text =
                            "Total ingredients: ${state.data.extendedIngredients?.size}"

                        binding.recipeImageView.load(state.data.image) {
                            crossfade(true)
                            crossfade(500)
                            placeholder(R.drawable.placeholder_image)
                            error(R.drawable.placeholder_image)
                        }
                    }

                    is DataResult.Error -> {
                        binding.circularLoader.visibility = View.VISIBLE
                        Log.e("InstructionsRecipeFragment", "Error: ${state.exception}")
                    }
                }
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.ingredientsRecyclerView.visibility == View.GONE
//
//        binding.cardIngredients.setOnClickListener {
//            if (binding.ingredientsRecyclerView.visibility == View.GONE) {
//                binding.ingredientsRecyclerView.visibility = View.VISIBLE
//            } else {
//                binding.ingredientsRecyclerView.visibility = View.GONE
//            }
//        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}