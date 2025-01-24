package com.serranoie.android.feature.saved

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.feature.saved.databinding.SavedRecipeItemBinding
import com.serranoie.android.feature.saved.utils.RecipeDeleteListener
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class SavedRecipesAdapter @Inject constructor(
    private val viewModel: SavedRecipesViewModel,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<Any>()
    private val compositeDisposable = CompositeDisposable()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedRecipeViewHolder {
        val binding =
            SavedRecipeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SavedRecipeViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SavedRecipeViewHolder -> {
                val recipe = items[position] as Recipe

                val listener = object : RecipeDeleteListener {
                    override fun delete(position: Int) {
                        compositeDisposable.add(
                            Completable.fromAction { viewModel.deleteRecipe(recipe.id!!) }
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe {
                                    notifyItemRemoved(position)
                                }
                        )
                    }
                }

                holder.bind(recipe, position, listener)
            }
        }
    }

    fun submitList(data: List<Recipe>) {
        items.addAll(data)
        notifyDataSetChanged()
    }

    fun updateAll(data: List<Recipe>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    inner class SavedRecipeViewHolder(private val binding: SavedRecipeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            data: Recipe,
            position: Int,
            listener: RecipeDeleteListener
        ) {
            binding.recipeTitleTextView.text = data.title
            binding.recipeImageView.load(data.image) {
                crossfade(true)
                crossfade(500)
                placeholder(R.drawable.placeholder_image)
                error(R.drawable.placeholder_image)
            }

            binding.deleteButton.setOnClickListener {
                listener.delete(position)
            }

            binding.root.setOnClickListener {
                val recipeId = data.id
                val request = NavDeepLinkRequest.Builder
                    .fromUri("cookoutines://instructions/${recipeId?.toString()}".toUri())
                    .build()
                findNavController(this.itemView).navigate(request)
            }
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        compositeDisposable.clear()
    }
}