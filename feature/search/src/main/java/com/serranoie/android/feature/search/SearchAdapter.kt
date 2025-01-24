package com.serranoie.android.feature.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.serranoie.android.core.domain.model.search.Result
import com.serranoie.android.feature.search.databinding.ItemEmptySearchBinding
import com.serranoie.android.feature.search.databinding.ItemSearchBinding

class SearchAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<Any>()
    private var isSearchAdapter = false

    override fun getItemViewType(position: Int): Int {
        return if (items.isEmpty() && isSearchAdapter) {
            TYPE_EMPTY_SEARCH
        } else {
            TYPE_RECIPE_SEARCH
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_RECIPE_SEARCH -> {
                val binding =
                    ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                SearchViewHolder(binding)
            }

            TYPE_EMPTY_SEARCH -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_empty_search, parent, false)
                EmptySearchViewHolder(view)
            }

            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun getItemCount(): Int {
        return if (isSearchAdapter && items.isEmpty()) {
            1
        } else {
            items.size
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SearchViewHolder -> {
                val recipeSearch = items[position] as Result
                holder.bind(recipeSearch)
            }
        }
    }

    fun submitList(data: List<Result>) {
        isSearchAdapter = true
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
        notifyItemChanged(0)
        notifyItemRangeChanged(0, items.size)
        notifyItemRangeChanged(0, items.size)
    }

    inner class SearchViewHolder(private val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Result) {
            binding.recipeTitleTextView.text = data.title
            binding.authorTextView.isVisible = false
            binding.labelCredits.isVisible = false

            binding.recipeImageView.load(data.image) {
                crossfade(true)
                crossfade(500)
                placeholder(R.drawable.placeholder_image)
                error(R.drawable.placeholder_image)
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

    inner class EmptySearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemEmptySearchBinding.bind(itemView)
        init {
            binding.label.text = "No recipes found with this query..."
        }
    }

    companion object {
        private const val TYPE_RECIPE_SEARCH = 0
        private const val TYPE_EMPTY_SEARCH = 1
    }
}