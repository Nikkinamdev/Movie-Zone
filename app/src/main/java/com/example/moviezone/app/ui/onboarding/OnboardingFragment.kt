package com.example.moviezone.app.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.moviezone.R
import com.example.moviezone.databinding.FragmentOnboardingBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OnboardingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class OnboardingFragment : Fragment(R.layout.fragment_onboarding) {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var binding: FragmentOnboardingBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOnboardingBinding.bind(view)
        binding.getStarted.setOnClickListener {
            it.findNavController().navigate(R.id.action_onboardingFragment_to_loginFragment)
        }
        viewPager = view.findViewById(R.id.viewPager)
        tabLayout = view.findViewById(R.id.tabLayout)

        viewPager.adapter = OnboardingAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()

        setupDots()

        viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateDots(position)
            }
        })
    }

    private fun setupDots() {
        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
            tab?.setCustomView(R.layout.dot_layout)
        }

        updateDots(0)
    }

    private fun updateDots(position: Int) {
        for (i in 0 until tabLayout.tabCount) {

            val tab = tabLayout.getTabAt(i)
            val dot = tab?.customView

            dot?.background = ContextCompat.getDrawable(
                requireContext(),
                if (i == position)
                    R.drawable.dot_selected
                else
                    R.drawable.dot_unselected
            )
        }
    }
}