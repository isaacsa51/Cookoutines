package com.serranoie.android.feature.instructions.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.serranoie.android.core.domain.model.recipe.ExtendedIngredient
import com.serranoie.android.feature.instructions.databinding.FragmentIngredientsBinding

class IngredientsFragment : Fragment() {

    private var ingredients: List<ExtendedIngredient>? = null

    private lateinit var binding: FragmentIngredientsBinding
    private lateinit var ingredientsAdapter: IngredientsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            @Suppress("UNCHECKED_CAST")
            ingredients = it.getSerializable("ingredientsList") as? List<ExtendedIngredient>
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIngredientsBinding.inflate(inflater, container, false)

        // Initialize the adapter with the ingredients list
        ingredientsAdapter = IngredientsAdapter(ingredients ?: emptyList())
        binding.ingredientsRecyclerView.adapter = ingredientsAdapter

        return binding.root
    }

    companion object {
        fun newInstance(ingredients: List<ExtendedIngredient?>): IngredientsFragment {
            val fragment = IngredientsFragment()
            val args = Bundle()
            args.putSerializable("ingredientsList", ArrayList(ingredients))
            fragment.arguments = args
            return fragment
        }
    }
}