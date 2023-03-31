package com.nathalie.coffeeapp.ui.presentation.drink


import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.data.model.fireStoreModel.Drink
import com.nathalie.coffeeapp.databinding.FragmentDrinksBinding
import com.nathalie.coffeeapp.ui.adapter.DrinkAdapter
import com.nathalie.coffeeapp.ui.presentation.BaseFragment
import com.nathalie.coffeeapp.ui.presentation.MainFragmentDirections
import com.nathalie.coffeeapp.ui.utils.Utils
import com.nathalie.coffeeapp.ui.utils.Utils.hideKeyboard
import com.nathalie.coffeeapp.ui.viewmodels.drink.DrinkViewModel
import com.nathalie.coffeeapp.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
// Fragment bound to the Drinks UI
class DrinksFragment : BaseFragment<FragmentDrinksBinding>() {
    private lateinit var adapter: DrinkAdapter
    private val parentViewModel: MainViewModel by viewModels(ownerProducer = { requireParentFragment() })
    override val viewModel: DrinkViewModel by viewModels()

    override fun getLayoutResource() = R.layout.fragment_drinks

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            // fetches all the beans
            viewModel.getDrinks("", 0, false)
        }
    }

    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)
        setupAdapter()

        lifecycleScope.launch {
            // refreshes and refetches when screen is swiped down
            viewModel.swipeRefreshLayoutFinished.collect {
                binding?.srlRefresh?.isRefreshing = false
            }
        }


        binding?.run {
            srlRefresh.setOnRefreshListener {
                // refreshes and refetches when screen is swiped down
                viewModel.onRefresh("", 0, false)
                search.etSearch.setText("")
                Utils.updateColors(requireContext(), btnAll, btnClassic, btnCraft, btnFav)
            }

            // Refetches drinks according to text in search field
            search.etSearch.setOnKeyListener OnKeyListener@{ _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                    val search = search.etSearch.text.toString()
                    refresh(search, 0, false)
                    hideKeyboard()
                    return@OnKeyListener true
                }
                false
            }

            // navigates to add drink fragment
            btnAdd.setOnClickListener {
                val action = MainFragmentDirections.actionMainToAddDrink()
                navController.navigate(action)
            }

            // refetches drinks where cat = 0
            btnAll.setOnClickListener {
                refresh("", 0, false)
                search.etSearch.setText("")
                Utils.updateColors(requireContext(), btnAll, btnClassic, btnCraft, btnFav)
            }

            // refetches drinks where cat = 1
            btnClassic.setOnClickListener {
                refresh("", 1, false)
                search.etSearch.setText("")
                Utils.updateColors(requireContext(), btnClassic, btnAll, btnCraft, btnFav)
            }

            // refetches drinks where cat = 2
            btnCraft.setOnClickListener {
                refresh("", 2, false)
                search.etSearch.setText("")
                Utils.updateColors(requireContext(), btnCraft, btnAll, btnClassic, btnFav)
            }

            // refetches drinks where fav = 2
            btnFav.setOnClickListener {
                refresh("", 0, true)
                search.etSearch.setText("")
                Utils.updateColors(requireContext(), btnFav, btnAll, btnClassic, btnCraft)
            }
        }
    }

    override fun onBindData(view: View) {
        super.onBindData(view)

        lifecycleScope.launch {
            viewModel.isLoading.collect() {
                if (!it) binding?.llLoader?.isVisible = false
            }
        }

        viewModel.drinks.observe(viewLifecycleOwner) {
            // adds the fetched list of drinks to the drinks adapter

            binding?.llEmpty?.isVisible = adapter.itemCount <= 0
            adapter.setDrinks(it.toMutableList())
        }

        // refresh function to refetch the drinks
        parentViewModel.refreshDrinks.observe(viewLifecycleOwner) {
            if (it.first) {
                refresh("", 0, false)
                parentViewModel.shouldRefreshDrinks(false)
            }
        }
    }

    // refresh function to refetch the drinks
    fun refresh(search: String, cat: Int, fav: Boolean) {
        viewModel.onRefresh(search, cat, fav)
    }

    // Binds the recycler view and the data, sets the click listener to navigate to details
    fun setupAdapter() {
        val layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = DrinkAdapter(mutableListOf())
        adapter.listener = object : DrinkAdapter.Listener {
            override fun onClick(item: Drink) {
                val action = item.id?.let { MainFragmentDirections.actionMainToDetail(it) }
                if (action != null) {
                    NavHostFragment.findNavController(this@DrinksFragment).navigate(action)
                }
            }
        }

        binding?.rvDrinks?.adapter = adapter
        binding?.rvDrinks?.layoutManager = layoutManager
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