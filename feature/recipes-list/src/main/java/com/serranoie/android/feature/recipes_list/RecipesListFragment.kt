package com.serranoie.android.feature.recipes_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.serranoie.android.feature.recipes_list.databinding.FragmentRecipesListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipesListFragment : Fragment() {

    private var _binding: FragmentRecipesListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecipesListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}