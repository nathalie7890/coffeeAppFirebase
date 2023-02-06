package com.nathalie.coffeeapp.ui.drink

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.nathalie.coffeeapp.MyApplication
import com.nathalie.coffeeapp.adapters.DrinkAdapter
import com.nathalie.coffeeapp.data.CoffeeDatabase
import com.nathalie.coffeeapp.databinding.FragmentDrinksBinding
import com.nathalie.coffeeapp.ui.MainFragmentDirections
import com.nathalie.coffeeapp.utils.Utils
import com.nathalie.coffeeapp.utils.Utils.hideKeyboard
import com.nathalie.coffeeapp.viewmodels.MainViewModel
import com.nathalie.coffeeapp.viewmodels.drink.DrinkViewModel
import kotlinx.coroutines.launch

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

        lifecycleScope.launchWhenResumed {
            viewModel.getDrinks("", 0, false)
        }
        setupAdapter()

        viewModel.drinks.observe(viewLifecycleOwner) {
            adapter.setDrinks(it)
            //if no item, display this
            binding.emptyDrink.llEmpty.isVisible = adapter.itemCount <= 0
        }

        parentViewModel.refreshDrinks.observe(viewLifecycleOwner) {
            refresh("", 0, false)
        }

        //once refresh in done, hides the refresh icon
        viewModel.swipeRefreshLayoutFinished.asLiveData()
            .observe(viewLifecycleOwner) {
                binding.srlRefresh.isRefreshing = false
            }

        binding.run {
            //when swipe down to refresh, refresh the fragment
            // change btnAdd bg color to be darker than the rest to indicate it is selected
            srlRefresh.setOnRefreshListener {
                viewModel.onRefresh()
                binding.search.etSearch.setText("")
                Utils.updateColors(requireContext(), btnAll, btnClassic, btnCraft, btnFav)
            }

            //when user press enter while focus on the search bar/typing in the search bar
            //hide keyboard
            search.etSearch.setOnKeyListener OnKeyListener@{ _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                    val search = search.etSearch.text.toString()
                    refresh(search, 0, false)
                    hideKeyboard()
                    return@OnKeyListener true
                }
                false
            }


            //when btn add is clicked, take user to add drink fragment
            btnAdd.setOnClickListener {
                val action = MainFragmentDirections.actionMainToAddDrink()
                NavHostFragment.findNavController(requireParentFragment()).navigate(action)
            }

            //only one of btnAll, btnClassic, btnCraft has darker than the rest
            //darker btn indicates to user it is selected
            btnAll.setOnClickListener {
                refresh("", 0, false)
                Utils.updateColors(requireContext(), btnAll, btnClassic, btnCraft, btnFav)
            }
            btnClassic.setOnClickListener {
                refresh("", 1, false)
                Utils.updateColors(requireContext(), btnClassic, btnAll, btnCraft, btnFav)
            }
            btnCraft.setOnClickListener {
                refresh("", 2, false)
                Utils.updateColors(requireContext(), btnCraft, btnAll, btnClassic, btnFav)
            }

            //when btnFav is clicked, fetch drinks that favorite == true
            //change btnAll's bg color to be darker
            btnFav.setOnClickListener {
                refresh("", 0, true)
                Utils.updateColors(requireContext(), btnFav, btnAll, btnClassic, btnCraft)
            }
        }
    }

    // fetch words
    fun refresh(str: String, cat: Int, fav: Boolean) {
        viewModel.onRefresh(str, cat, fav)
    }

    // adapter for words
    fun setupAdapter() {
        val layoutManager = GridLayoutManager(requireContext(), 2)
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

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}