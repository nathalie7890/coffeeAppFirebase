package com.nathalie.coffeeapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

// Contains functions and information for the different Tab Layout fragments (Drinks, Beans, Roast)
// used in MainFragment.kt
class ViewPagerAdapter(
    val fragments: List<Fragment>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    // returns the number of fragments in the list
    override fun getItemCount() = fragments.size

    // creates the fragment UI depending on the position selected in the TabLayout
    override fun createFragment(position: Int) = fragments[position]
}