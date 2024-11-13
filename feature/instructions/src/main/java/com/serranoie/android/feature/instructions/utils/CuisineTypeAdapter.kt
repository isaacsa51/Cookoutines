package com.serranoie.android.feature.instructions.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.serranoie.android.feature.instructions.R

class CuisineTypeAdapter(private val cuisines: List<String>) :
    RecyclerView.Adapter<CuisineTypeAdapter.CuisineViewHolder>() {

    class CuisineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cuisineTextView: TextView = itemView.findViewById(R.id.cuisineType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CuisineViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.cuisine_item, parent, false)
        return CuisineViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CuisineViewHolder, position: Int) {
        holder.cuisineTextView.text = cuisines[position]
    }

    override fun getItemCount(): Int {
        return cuisines.size
    }

}