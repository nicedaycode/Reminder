
package com.example.reminder.onboarding.screen

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.reminder.R
import com.example.reminder.databinding.FragmentFirstScreenBinding

class FirstScreen : Fragment() {
    private var _binding: FragmentFirstScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_first_screen, container, false)

        binding.skipBtn.setOnClickListener {
            findNavController().navigate(R.id.action_viewPagerFragment_to_signIn)
            onBoardingFinished()
        }

        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
        binding.nextBtn.setOnClickListener {
            viewPager?.currentItem = 1
        }

        return binding.root
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