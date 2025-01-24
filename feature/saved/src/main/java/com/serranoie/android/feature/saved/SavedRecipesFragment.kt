package com.serranoie.android.feature.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.serranoie.android.core.domain.result.DataResult
import com.serranoie.android.feature.saved.databinding.FragmentSavedRecipesBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

@AndroidEntryPoint
class SavedRecipesFragment : Fragment() {

    private var _binding: FragmentSavedRecipesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SavedRecipesViewModel by viewModels()
    private lateinit var adapter: SavedRecipesAdapter

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedRecipesBinding.inflate(inflater, container, false)

        setupUi()
        setupObservers()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.triggerRefresh()
    }

    private fun setupUi() {
        adapter = SavedRecipesAdapter(viewModel)
        binding.postsRecyclerView.adapter = adapter
        binding.postsRecyclerView.layoutManager =
            StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
    }

    private fun setupObservers() {
        compositeDisposable.add(
            viewModel.recipesState
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result ->
                    when (result) {
                        is DataResult.Success -> {
                            binding.progressBar.isVisible = false
                            adapter.submitList(result.data)
                            adapter.updateAll(result.data)
                        }

                        is DataResult.Loading -> {
                            binding.progressBar.isVisible = true
                        }

                        is DataResult.Error -> {
                            binding.progressBar.isVisible = false
                            binding.errorTextLabel.text = result.exception.message
                        }
                    }
                }
        )

        compositeDisposable.add(
            viewModel.refreshTrigger
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    viewModel.loadSavedRecipes()
                }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        compositeDisposable.clear()
    }
}