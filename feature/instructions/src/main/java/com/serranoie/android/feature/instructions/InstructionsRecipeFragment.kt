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
import com.serranoie.android.feature.instructions.directions.DirectionsFragment
import com.serranoie.android.feature.instructions.ingredients.IngredientsFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InstructionsRecipeFragment : Fragment() {

    private val viewModel: InstructionsRecipeViewModel by viewModels()

    private var _binding: FragmentInstructionsRecipeBinding? = null
    private val binding get() = _binding!!

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
            viewModel.recipeState.collect { state ->
                when (state) {
                    is DataResult.Loading -> {
                        binding.circularLoader.visibility = View.VISIBLE
                    }

                    is DataResult.Success -> {
                        binding.circularLoader.visibility = View.GONE

                        binding.collapsingToolbarLayout.title = state.data.title

                        binding.readyTimeInfo.text =
                            state.data.readyInMinutes.toString() + " minutes"
                        binding.servingsInfo.text = state.data.servings.toString() + " servings"
                        binding.cuisineType.text = state.data.cuisines.toString()

                        binding.summaryInfo.text = HtmlCompat.fromHtml(
                            state.data.summary.toString(),
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        )
                        binding.recipeImageView.load(state.data.image) {
                            crossfade(true)
                            crossfade(500)
                            placeholder(R.drawable.placeholder_image)
                            error(R.drawable.placeholder_image)
                        }

                        val ingredientsFragment = IngredientsFragment.newInstance(state.data.extendedIngredients!!)
                        childFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, ingredientsFragment)
                            .commit()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        childFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, IngredientsFragment())
            .commit()

        binding.radioGroupTabs.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioIngredients -> {
                    val ingredients = (viewModel.recipeState.value as? DataResult.Success)?.data?.extendedIngredients ?: emptyList()
                    val ingredientsFragment = IngredientsFragment.newInstance(ingredients)
                    childFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, ingredientsFragment)
                        .commit()
                }

                R.id.radioDirections -> {
                    childFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, DirectionsFragment())
                        .commit()
                }
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}