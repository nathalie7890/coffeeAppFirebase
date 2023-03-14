package com.nathalie.coffeeapp.ui.presentation

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.nathalie.coffeeapp.R
import android.util.Log
import com.nathalie.coffeeapp.adapters.ViewPagerAdapter
import com.nathalie.coffeeapp.data.service.AuthService
import com.nathalie.coffeeapp.databinding.FragmentMainBinding
import com.nathalie.coffeeapp.ui.presentation.bean.BeansFragment
import com.nathalie.coffeeapp.ui.presentation.drink.DrinksFragment
import com.nathalie.coffeeapp.ui.presentation.roast.RoastFragment
import com.nathalie.coffeeapp.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private val drinksFragment = DrinksFragment.getInstance()
    private val beansFragment = BeansFragment.getInstance()
    private val roastFragment = RoastFragment.getInstance()
    private val viewModel: MainViewModel by viewModels()

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

        //list of fragments in viewPager
        val adapter = ViewPagerAdapter(
            listOf(drinksFragment, beansFragment, roastFragment),
            childFragmentManager,
            lifecycle
        )

        binding.vpCoffee.adapter = adapter
        binding.vpCoffee.isUserInputEnabled = false
        binding.vpCoffee.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPage = position
            }
        })

        setFragmentResultListener("finish_login") { _, result ->
            val refresh = result.getBoolean("refresh")
            viewModel.shouldRefreshDrinks(refresh)
            viewModel.shouldRefreshBeans(refresh)
        }

        setFragmentResultListener("finish_add_drink") { _, result ->
            val refresh = result.getBoolean("refresh")
            viewModel.shouldRefreshDrinks(refresh)
        }

        setFragmentResultListener("finish_delete_drink") { _, result ->
            val refresh = result.getBoolean("refresh")
            viewModel.shouldRefreshDrinks(refresh)
        }

        setFragmentResultListener("finish_edit_drink") { _, result ->
            val refresh = result.getBoolean("refresh")
            viewModel.shouldRefreshDrinks(refresh)
        }

//        setFragmentResultListener("finish_fav_drink") { _, result ->
//            val refresh = result.getBoolean("refresh")
//            viewModel.shouldRefreshDrinks(refresh, viewModel.fav)
//        }

        setFragmentResultListener("finish_add_bean") { _, result ->
            val refresh = result.getBoolean("refresh")
            viewModel.shouldRefreshBeans(refresh)
        }

        setFragmentResultListener("finish_delete_bean") { _, result ->
            val refresh = result.getBoolean("refresh")
            viewModel.shouldRefreshBeans(refresh)
        }

        setFragmentResultListener("from_add_roast") { _, result ->
            val refresh = result.getBoolean("refresh")
            viewModel.shouldRefreshRoast(refresh)
        }

        setFragmentResultListener("from_edit_roast") { _, result ->
            val refresh = result.getBoolean("refresh")
            viewModel.shouldRefreshRoast(refresh)
        }
        setFragmentResultListener("from_delete_roast") { _, result ->
            val refresh = result.getBoolean("refresh")
            viewModel.shouldRefreshRoast(refresh)
        }


        //set tab names and icons
        TabLayoutMediator(binding.tlCoffee, binding.vpCoffee) { tab, pos ->
            when (pos) {
                0 -> {
                    tab.text = "Drinks"
                    tab.setIcon(R.drawable.ic_drink)
                }
                1 -> {
                    tab.text = "Beans"
                    tab.setIcon(R.drawable.ic_bean)
                }
                else -> {
                    tab.text = "Roast"
                    tab.setIcon(R.drawable.ic_roast)
                }
            }
        }.attach()
    }

    fun sayHello() {
        Log.d("debug", "hello from main")
    }
}