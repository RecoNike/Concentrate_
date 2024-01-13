package com.recon.concentrate.intro_fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

 class IntroViewPagerAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {

        override fun getItemCount(): Int = 3 // Количество ваших фрагментов

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> IntroFragment()
                1 -> IntroFragment1()
                2 -> IntroFragment2()
                else -> throw IllegalArgumentException("Invalid position: $position")
            }
        }

}