package com.serranoie.android.feature.instructions.directions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.serranoie.android.core.domain.model.instructions.Equipment
import com.serranoie.android.core.domain.model.instructions.Ingredient
import com.serranoie.android.core.domain.model.instructions.InstructionsItem
import com.serranoie.android.core.domain.model.instructions.Step
import com.serranoie.android.feature.instructions.R
import com.serranoie.android.feature.instructions.databinding.FragmentDirectionsBinding
import com.serranoie.android.feature.instructions.databinding.ItemDirectionEquipmentBinding
import com.serranoie.android.feature.instructions.databinding.ItemDirectionIngredientBinding
import com.serranoie.android.feature.instructions.databinding.ItemDirectionRecipeBinding

class DirectionsAdapter :
    ListAdapter<InstructionsItem, DirectionsAdapter.InstructionGroupViewHolder>(
        InstructionGroupDiffCallback()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstructionGroupViewHolder {
        val binding = FragmentDirectionsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return InstructionGroupViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: InstructionGroupViewHolder, position: Int) {
        val instructionGroup = getItem(position)
        holder.bind(instructionGroup)
    }

    inner class InstructionGroupViewHolder(
        private val binding: FragmentDirectionsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(instructionGroup: InstructionsItem) {
            binding.tvInstructionGroupName.text = instructionGroup.name

            val stepAdapter = InstructionStepAdapter()

            binding.directionsRecyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = stepAdapter
            }
            stepAdapter.submitList(instructionGroup.steps)
        }
    }

    // Adapter for the instruction steps
    inner class InstructionStepAdapter :
        ListAdapter<Step, InstructionStepAdapter.InstructionStepViewHolder>(
            InstructionStepDiffCallback()
        ) {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): InstructionStepViewHolder {
            val binding = ItemDirectionRecipeBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return InstructionStepViewHolder(binding)
        }

        override fun onBindViewHolder(holder: InstructionStepViewHolder, position: Int) {
            val step = getItem(position)
            holder.bind(step)
        }

        inner class InstructionStepViewHolder(
            private val binding: ItemDirectionRecipeBinding
        ) : RecyclerView.ViewHolder(binding.root) {

            fun bind(step: Step) {
                binding.tvStepNumber.text = "${step.number}."
                binding.tvStepDescription.text = step.step

                // Set up ingredients RecyclerView if there are ingredients
                if (step.ingredients.isNotEmpty()) {
                    val ingredientsAdapter = IngredientAdapter()
                    binding.rvIngredients.apply {
                        layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        adapter = ingredientsAdapter
                        visibility = View.VISIBLE
                    }
                    ingredientsAdapter.submitList(step.ingredients)
                }

                // Set up equipment RecyclerView if there is equipment
                if (step.equipment.isNotEmpty()) {
                    val equipmentAdapter = EquipmentAdapter()
                    binding.rvEquipment.apply {
                        layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        adapter = equipmentAdapter
                        visibility = View.VISIBLE
                    }
                    equipmentAdapter.submitList(step.equipment)
                }
            }
        }
    }

    // Adapter for ingredients
    inner class IngredientAdapter :
        ListAdapter<Ingredient, IngredientAdapter.IngredientViewHolder>(
            IngredientDiffCallback()
        ) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
            val binding = ItemDirectionIngredientBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return IngredientViewHolder(binding)
        }

        override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
            val ingredient = getItem(position)
            holder.bind(ingredient)
        }

        inner class IngredientViewHolder(
            private val binding: ItemDirectionIngredientBinding
        ) : RecyclerView.ViewHolder(binding.root) {

            fun bind(ingredient: Ingredient) {
                binding.tvIngredientName.text = ingredient.name
                binding.ivIngredientImage.load(ingredient.image) {
                    crossfade(true)
                    placeholder(R.drawable.placeholder_image)
                    error(R.drawable.placeholder_image)
                }
            }
        }
    }

    // Adapter for equipment
    inner class EquipmentAdapter :
        ListAdapter<Equipment, EquipmentAdapter.EquipmentViewHolder>(
            EquipmentDiffCallback()
        ) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipmentViewHolder {
            val binding = ItemDirectionEquipmentBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return EquipmentViewHolder(binding)
        }

        override fun onBindViewHolder(holder: EquipmentViewHolder, position: Int) {
            val equipment = getItem(position)
            holder.bind(equipment)
        }

        inner class EquipmentViewHolder(
            private val binding: ItemDirectionEquipmentBinding
        ) : RecyclerView.ViewHolder(binding.root) {

            fun bind(equipment: Equipment) {
                binding.tvEquipmentName.text = equipment.name
                binding.ivEquipmentImage.load(equipment.image) {
                    crossfade(true)
                    placeholder(R.drawable.placeholder_image)
                    error(R.drawable.placeholder_image)
                }
            }
        }
    }
}

// For InstructionsItem
class InstructionGroupDiffCallback : DiffUtil.ItemCallback<InstructionsItem>() {
    override fun areItemsTheSame(oldItem: InstructionsItem, newItem: InstructionsItem): Boolean {
        // Assuming each instruction group has a unique ID or name
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: InstructionsItem, newItem: InstructionsItem): Boolean {
        return oldItem == newItem
    }
}

// For InstructionsItem.Step
class InstructionStepDiffCallback : DiffUtil.ItemCallback<Step>() {
    override fun areItemsTheSame(oldItem: Step, newItem: Step): Boolean {
        // Assuming each step has a unique number within the group
        return oldItem.number == newItem.number
    }

    override fun areContentsTheSame(oldItem: Step, newItem: Step): Boolean {
        return oldItem == newItem
    }
}

// For InstructionsItem.Step.Ingredient
class IngredientDiffCallback : DiffUtil.ItemCallback<Ingredient>() {
    override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
        // Assuming each ingredient has a unique ID
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
        return oldItem == newItem
    }
}

// For InstructionsItem.Step.Equipment
class EquipmentDiffCallback : DiffUtil.ItemCallback<Equipment>() {
    override fun areItemsTheSame(oldItem: Equipment, newItem: Equipment): Boolean {
        // Assuming each piece of equipment has a unique ID
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Equipment, newItem: Equipment): Boolean {
        return oldItem == newItem
    }
}