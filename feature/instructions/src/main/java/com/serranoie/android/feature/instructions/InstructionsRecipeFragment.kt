package com.serranoie.android.feature.instructions

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.serranoie.android.core.domain.result.DataResult
import com.serranoie.android.feature.instructions.databinding.FragmentInstructionsRecipeBinding
import com.serranoie.android.feature.instructions.directions.DirectionsAdapter
import com.serranoie.android.feature.instructions.directions.DirectionsViewModel
import com.serranoie.android.feature.instructions.ingredients.IngredientsAdapter
import com.serranoie.android.feature.instructions.utils.CuisineTypeAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

@AndroidEntryPoint
class InstructionsRecipeFragment : Fragment() {

    private val viewModel: InstructionsRecipeViewModel by viewModels()
    private val directionsViewModel: DirectionsViewModel by viewModels()

    private var _binding: FragmentInstructionsRecipeBinding? = null
    private val binding get() = _binding!!

    private lateinit var directionsAdapter: DirectionsAdapter

    private val compositeDisposable = CompositeDisposable()

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

        compositeDisposable.add(
            directionsViewModel.instructions
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result ->
                    when (result) {
                        is DataResult.Loading -> {
                            binding.circularLoader.visibility = View.VISIBLE
                        }

                        is DataResult.Success -> {
                            directionsAdapter.submitList(result.data)
                            binding.circularLoader.visibility = View.GONE
                        }

                        is DataResult.Error -> {
                            Log.e(
                                "InstructionsRecipeFragment",
                                "Error loading instructions: ${result.exception}"
                            )
                        }
                    }
                }
        )

        compositeDisposable.add(
            viewModel.recipeState
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { state ->
                    when (state) {
                        is DataResult.Loading -> {
                            binding.circularLoader.visibility = View.VISIBLE
                        }

                        is DataResult.Success -> {
                            compositeDisposable.add(
                                viewModel.isRecipeSaved(state.data.id!!)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe { isSaved ->
                                        updateFabState(isSaved)

                                        binding.extendedFab.setOnClickListener {
                                            if (isSaved) {
                                                viewModel.deleteRecipe(state.data.id!!)
                                            } else {
                                                viewModel.saveRecipe(state.data.copy(isSaved = true))
                                            }
                                        }
                                    }
                            )

                            binding.circularLoader.visibility = View.GONE

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
                            binding.stepsRecyclerView.layoutManager =
                                LinearLayoutManager(requireContext())
                            binding.stepsRecyclerView.adapter = directionsAdapter
                            directionsViewModel.getRecipeInstructions(state.data.id!!)

                            // * INGREDIENTS
                            val ingredientsAdapter =
                                IngredientsAdapter(state.data.extendedIngredients!!)
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
        )

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ingredientsRecyclerView.visibility = View.GONE

        binding.cardIngredients.setOnClickListener {
            if (binding.ingredientsRecyclerView.visibility == View.GONE) {
                binding.ingredientsRecyclerView.visibility = View.VISIBLE
            } else {
                binding.ingredientsRecyclerView.visibility = View.GONE
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.extendedFab.setOnClickListener {
            // TODO: save current recipe entity here...
        }
    }

    private fun updateFabState(isSaved: Boolean) {
        binding.extendedFab.text = if (isSaved) {
            "Saved"
        } else {
            "Save Recipe"
        }
        binding.extendedFab.icon = if (isSaved) {
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_bookmarked)
        } else {
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_bookmark)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}