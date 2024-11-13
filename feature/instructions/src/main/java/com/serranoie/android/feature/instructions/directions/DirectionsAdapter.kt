package com.serranoie.android.feature.instructions.directions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.serranoie.android.core.domain.model.instructions.InstructionsItem
import com.serranoie.android.feature.instructions.databinding.ItemDirectionRecipeBinding

class DirectionsAdapter(
    private val directions: List<InstructionsItem>
) : RecyclerView.Adapter<DirectionsAdapter.DirectionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DirectionViewHolder {
        val binding = ItemDirectionRecipeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return DirectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DirectionViewHolder, position: Int) {
        holder.bind(directions)
    }

    override fun getItemCount(): Int = directions.size

    inner class DirectionViewHolder(private val binding: ItemDirectionRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(direction: List<InstructionsItem>) {
            // Assuming direction has a single InstructionsItem
            val instructionItem = direction[0]

            binding.stepNumberLabel.text =
                "Step number: ${instructionItem.steps[adapterPosition]?.number}"
            binding.stepInfo.text = instructionItem.steps[adapterPosition]?.step

            // Handle equipment
            val equipmentNames =
                instructionItem.steps[adapterPosition]?.equipment
            binding.equipmentName.text = equipmentNames.toString()
            binding.equipmentLabel.isVisible = equipmentNames?.isNotEmpty() == true
            binding.equipmentName.isVisible = equipmentNames?.isNotEmpty() == true

            // Handle ingredients
            val ingredientNames =
                instructionItem.steps[adapterPosition]?.ingredients
            binding.ingredientNeededLabel.text = "Ingredient needed: $ingredientNames"
            binding.ingredientNeededLabel.isVisible = ingredientNames?.isNotEmpty() == true
        }
    }
}