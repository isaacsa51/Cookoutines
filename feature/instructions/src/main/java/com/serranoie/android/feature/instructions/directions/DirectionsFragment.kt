package com.serranoie.android.feature.instructions.directions

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.serranoie.android.feature.instructions.R

class DirectionsFragment : Fragment() {

    companion object {
        fun newInstance() = DirectionsFragment()
    }

    private val viewModel: DirectionsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_directions, container, false)
    }
}