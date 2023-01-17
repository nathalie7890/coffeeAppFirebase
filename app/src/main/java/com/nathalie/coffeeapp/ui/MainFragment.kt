package com.nathalie.coffeeapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.adapters.ViewPagerAdapter
import com.nathalie.coffeeapp.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private val drinksFragment = DrinksFragment.getInstance()
    private val beansFragment = BeansFragment.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var currentPage = 0

        val adapter = ViewPagerAdapter(
            listOf(drinksFragment, beansFragment),
            childFragmentManager,
            lifecycle
        )

        binding.vpCoffee.adapter = adapter

        binding.vpCoffee.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPage = position
            }
        })

        TabLayoutMediator(binding.tlCoffee, binding.vpCoffee) { tab, pos ->
            tab.text = when (pos) {
                0 -> "Drinks"
                else -> "Beans"
            }
        }.attach()
    }
}