package com.serranoie.android.feature.instructions.directions

import android.os.Bundle
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DirectionsFragment : Fragment() {

    private var recipeId: Int? = null

    private var _binding: FragmentDirectionsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DirectionsViewModel by viewModels()
    private lateinit var directionsAdapter: DirectionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDirectionsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipeId = arguments?.getInt("recipeId") ?: -1

        directionsAdapter = DirectionsAdapter()
        binding.directionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = directionsAdapter
        }

        viewModel.getRecipeInstructions(recipeId)

        // Collect the instructions flow from the ViewModel
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.instructions.collectLatest { result ->
                    when (result) {
                        is DataResult.Loading -> {
                            // Show loading state
//                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is DataResult.Success -> {
                            // Hide loading state
//                            binding.progressBar.visibility = View.GONE
                            // Update the adapter with the instructions data
                            directionsAdapter.submitList(result.data)
                        }

                        is DataResult.Error -> {
                            // Hide loading state
                            //binding.progressBar.visibility = View.GONE
                            // Handle the error
                            // ...
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(recipeId: Int): DirectionsFragment {
            val fragment = DirectionsFragment()
            val args = Bundle()
            args.putSerializable("recipeId", recipeId)
            fragment.arguments = args
            return fragment
        }
    }
}