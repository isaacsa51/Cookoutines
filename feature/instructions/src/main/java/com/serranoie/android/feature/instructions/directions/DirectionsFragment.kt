package com.serranoie.android.feature.instructions.directions

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.serranoie.android.core.domain.result.DataResult
import com.serranoie.android.feature.instructions.databinding.FragmentDirectionsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DirectionsFragment : Fragment() {

    private var recipeId: Int? = null

    private val viewModel: DirectionsViewModel by viewModels()

    private var _binding: FragmentDirectionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            @Suppress("UNCHECKED_CAST")
            recipeId = (it.getSerializable("recipeId") as? Int)!!
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recipeId?.let { viewModel.getRecipeInstructions(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDirectionsBinding.inflate(inflater, container, false)

        binding.directionsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.instructions.collect { instructions ->
                    when (instructions) {
                        is DataResult.Success -> {
                            binding.circularLoader.visibility = View.GONE

                            val adapter = DirectionsAdapter(instructions.data)

                            binding.directionsRecyclerView.adapter = adapter
                        }

                        is DataResult.Error -> {
                            binding.circularLoader.visibility = View.GONE
                            Log.e("DirectionsFragment", "Error: ${instructions.exception.message}")
                        }

                        is DataResult.Loading -> {
                            binding.circularLoader.visibility = View.GONE
                        }
                    }
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(id: Int): DirectionsFragment {
            val fragment = DirectionsFragment()
            val args = Bundle()
            args.putSerializable("recipeId", id)
            fragment.arguments = args
            return fragment
        }
    }
}