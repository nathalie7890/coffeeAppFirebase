package com.nathalie.coffeeapp.ui

import android.os.Bundle
import android.view.KeyEvent
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
import com.nathalie.coffeeapp.viewmodels.MainViewModel

class DrinksFragment : Fragment() {
    private lateinit var adapter: DrinkAdapter
    private lateinit var binding: FragmentDrinksBinding
    private val viewModel: DrinkViewModel by viewModels {
        DrinkViewModel.Provider((requireActivity().applicationContext as MyApplication).drinkRepo)
    }
    private val parentViewModel: MainViewModel by viewModels(ownerProducer = { requireParentFragment() })
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

        parentViewModel.refreshDrinks.observe(viewLifecycleOwner) {
            refresh("", "")
        }

        binding.run {
            search.etSearch.setOnKeyListener OnKeyListener@{ _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                    val search = search.etSearch.text.toString()
                   refresh(search, "")
                    return@OnKeyListener true
                }
                false
            }

            btnAll.setOnClickListener {
                refresh("", "")
            }
            btnHot.setOnClickListener {
                refresh("", "Hot")
            }
            btnCold.setOnClickListener {
                refresh("", "Cold")
            }
        }
    }

    //fetch words
    fun refresh(str: String, cat: String) {
        viewModel.getDrinks(str, cat)
    }

    //adapter for words
    fun setupAdapter() {
        val layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        adapter = DrinkAdapter(emptyList()) {
            val action = MainFragmentDirections.actionMainToDetail(it.id!!)
            NavHostFragment.findNavController(this).navigate(action)
        }

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