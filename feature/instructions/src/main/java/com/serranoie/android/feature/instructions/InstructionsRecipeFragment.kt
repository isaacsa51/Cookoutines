package com.serranoie.android.feature.instructions

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.serranoie.android.core.domain.model.recipe.ExtendedIngredient
import com.serranoie.android.core.domain.model.recipe.Recipe
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
        val recipeId = arguments?.getString("recipeId")?.toIntOrNull()
        recipeId?.let { viewModel.getCurrentRecipe(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInstructionsRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupAdapters()
        setupObservers()
        setupClickListeners()
    }

    private fun setupToolbar() {
        (requireActivity() as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
    }

    private fun setupAdapters() {
        directionsAdapter = DirectionsAdapter(::updateProgress)
        binding.stepsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = directionsAdapter
        }
    }

    private fun setupObservers() {
        observeRecipeState()
        observeInstructionsState()
    }

    private fun observeRecipeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.recipeState.collect { result ->
                when (result) {
                    is DataResult.Loading -> showLoading()
                    is DataResult.Success -> {
                        hideLoading()
                        bindRecipeData(result.data)
                    }

                    is DataResult.Error -> showError(result.exception)
                }
            }
        }
    }

    private fun observeInstructionsState() {
        viewLifecycleOwner.lifecycleScope.launch {
            directionsViewModel.instructions.collect { result ->
                when (result) {
                    is DataResult.Loading -> showLoading()
                    is DataResult.Success -> {
                        hideLoading()
                        directionsAdapter.submitList(result.data)
                    }

                    is DataResult.Error -> showError(result.exception)
                }
            }
        }
    }

    @SuppressLint("StringFormatInvalid")
    private suspend fun bindRecipeData(recipe: Recipe) {
        binding.apply {
            collapsingToolbarLayout.title = recipe.title
            readyTimeInfo.text = getString(R.string.ready_time_format, recipe.readyInMinutes)
            servingsInfo.text = getString(R.string.servings_format, recipe.servings)
            summaryInfo.text =
                HtmlCompat.fromHtml(recipe.summary.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)

            setupCuisineAdapter(recipe.cuisines as List<String>)

            directionsViewModel.getRecipeInstructions(recipe.id!!)

            setupIngredientsAdapter(recipe.extendedIngredients)

            recipeImageView.load(recipe.image) {
                crossfade(true)
                crossfade(500)
                placeholder(R.drawable.placeholder_image)
                error(R.drawable.placeholder_image)
            }

            updateFabState(viewModel.isRecipeSaved(recipe.id!!))

            extendedFab.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    toggleSaveRecipe(recipe)
                }
            }
        }
    }

    private fun setupCuisineAdapter(cuisines: List<String>) {
        binding.cuisineRecyclerView.isVisible = cuisines.isNotEmpty()
        if (cuisines.isNotEmpty()) {
            val cuisineAdapter = CuisineTypeAdapter(cuisines)
            binding.cuisineRecyclerView.adapter = cuisineAdapter
        }
    }

    @SuppressLint("StringFormatInvalid")
    private fun setupIngredientsAdapter(ingredients: List<ExtendedIngredient?>?) {
        binding.ingredientsRecyclerView.isVisible = false
        ingredients?.let {
            val ingredientsAdapter = IngredientsAdapter(it)
            binding.ingredientsRecyclerView.adapter = ingredientsAdapter
            binding.totalIngredientsLabel.text =
                getString(R.string.total_ingredients_format, it.size)
        }
    }

    private suspend fun toggleSaveRecipe(recipe: Recipe) {
        val isSaved = viewModel.isRecipeSaved(recipe.id!!)
        if (isSaved) {
            viewModel.deleteRecipe(recipe.id!!)
        } else {
            viewModel.saveRecipe(recipe.copy(isSaved = true))
        }
        updateFabState(!isSaved)
    }

    private fun updateFabState(isSaved: Boolean) {
        binding.extendedFab.apply {
            text = getString(if (isSaved) R.string.saved else R.string.save_recipe)
            icon = ContextCompat.getDrawable(
                requireContext(),
                if (isSaved) R.drawable.ic_bookmarked else R.drawable.ic_bookmark
            )
        }
    }

    private fun setupClickListeners() {
        binding.cardIngredients.setOnClickListener {
            binding.ingredientsRecyclerView.isVisible = !binding.ingredientsRecyclerView.isVisible
        }
    }

    private fun updateProgress(progress: Float) {
        binding.recipeProgress.progress = (progress * 100).toInt()
        binding.recipeProgress.isVisible = progress > 0
    }

    private fun showLoading() {
        binding.circularLoader.isVisible = true
    }

    private fun hideLoading() {
        binding.circularLoader.isVisible = false
    }

    private fun showError(exception: Throwable) {
        hideLoading()
        Log.e("InstructionsRecipeFragment", "Error: ${exception.message}", exception)
        // Optionally, show a user-friendly error message here
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}