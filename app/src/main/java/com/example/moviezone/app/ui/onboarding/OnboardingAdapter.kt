package com.example.moviezone.app.ui.onboarding

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnboardingAdapter(fragment: OnboardingFragment) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OnboardingPage1Fragment()
            1 -> OnboardingPage2Fragment()
            2 -> OnboardingPage3Fragment()
            else -> OnboardingPage1Fragment()
        }
    }

    override fun getItemCount(): Int = 3

}