package com.serranoie.android.feature.instructions

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import com.google.android.material.tabs.TabLayoutMediator
import com.serranoie.android.core.domain.result.DataResult
import com.serranoie.android.feature.instructions.databinding.FragmentInstructionsRecipeBinding
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.recipeState.collect { state ->
                when (state) {
                    is DataResult.Loading -> {
                        binding.circularLoader.visibility = View.VISIBLE
                    }

                    is DataResult.Success -> {
                        binding.circularLoader.visibility = View.GONE

                        Log.d("InstructionsRecipeFragment", "Success: ${state.data.title}")
                        binding.collapsingToolbarLayout.title = state.data.title

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

        val tabTitles = listOf("Ingredient", "Direction")
        val pagerAdapter = RecipePagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val customView = LayoutInflater.from(requireContext())
                .inflate(R.layout.tab_item_buttom, null) as TextView
            customView.text = tabTitles[position]
            tab.customView = customView
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}