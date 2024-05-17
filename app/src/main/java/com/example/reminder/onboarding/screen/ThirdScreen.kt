
package com.example.reminder.onboarding.screen

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.reminder.R
import com.example.reminder.databinding.FragmentThirdScreenBinding

class ThirdScreen : Fragment() {
    private var _binding: FragmentThirdScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_third_screen, container, false)

        binding.skipBtn.setOnClickListener {
            navigateToSignIn()
        }

        binding.finishBtn.setOnClickListener {
            navigateToSignIn()
        }

        return binding.root
    }

    private fun navigateToSignIn() {
        findNavController().navigate(R.id.action_viewPagerFragment_to_signIn)
        onBoardingFinished()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onBoardingFinished() {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("Finished", true)
            apply()
        }
    }
}