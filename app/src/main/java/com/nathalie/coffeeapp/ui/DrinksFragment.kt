package com.nathalie.coffeeapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.nathalie.coffeeapp.MyApplication
import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.adapters.DrinkAdapter
import com.nathalie.coffeeapp.data.Model.Drink
import com.nathalie.coffeeapp.databinding.FragmentDrinksBinding
import com.nathalie.coffeeapp.viewmodels.DrinkViewModel

class DrinksFragment : Fragment() {
    private lateinit var adapter: DrinkAdapter
    private lateinit var binding: FragmentDrinksBinding
    private val viewModel: DrinkViewModel by viewModels {
        DrinkViewModel.Provider((requireActivity().applicationContext as MyApplication).drinkRepo)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDrinksBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()

        viewModel.drinks.observe(viewLifecycleOwner) {
            adapter.setDrinks(it)
        }

    }

    //fetch words
    fun refresh(str: String) {
        viewModel.getDrinks(str)
    }

    //adapter for words
    fun setupAdapter() {
        val layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        adapter = DrinkAdapter(emptyList())

        binding.rvDrinks.adapter = adapter
        binding.rvDrinks.layoutManager = layoutManager
    }


    companion object {
        private var drinksFragmentInstance: DrinksFragment? = null
        fun getInstance(): DrinksFragment {
            if (drinksFragmentInstance == null) {
                drinksFragmentInstance = DrinksFragment()
            }

            return drinksFragmentInstance!!
        }
    }
}