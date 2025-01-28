package com.serranoie.android.feature.recipes_list.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface RecipeAdapterDelegate {
    fun getViewType(): Int
    fun isForViewType(item: RecipeListItem): Boolean
    fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    fun bindViewHolder(holder: RecyclerView.ViewHolder, item: RecipeListItem)
}