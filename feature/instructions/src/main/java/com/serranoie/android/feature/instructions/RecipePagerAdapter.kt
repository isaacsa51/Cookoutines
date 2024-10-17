package com.serranoie.android.feature.instructions

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.serranoie.android.feature.instructions.directions.DirectionsFragment
import com.serranoie.android.feature.instructions.ingredients.IngredientsFragment

class RecipePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2 // Number of tabs

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> IngredientsFragment()
            1 -> DirectionsFragment()
            else -> throw IllegalStateException("Invalid tab position")
        }
    }
}