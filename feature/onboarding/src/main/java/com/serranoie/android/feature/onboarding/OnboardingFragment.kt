package com.serranoie.android.feature.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.serranoie.android.feature.onboarding.databinding.FragmentOnboardingBinding

class OnboardingFragment : Fragment() {

    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OnboardingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnStart.setOnClickListener {
            val request = NavDeepLinkRequest.Builder
                .fromUri("cookoutines://recipes".toUri())
                .build()
            findNavController().navigate(request)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}