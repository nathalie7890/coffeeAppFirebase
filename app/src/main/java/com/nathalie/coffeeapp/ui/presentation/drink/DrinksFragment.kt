package com.nathalie.coffeeapp.ui.presentation.drink


import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
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

@AndroidEntryPoint
class DrinksFragment : BaseFragment<FragmentDrinksBinding>() {
    private lateinit var adapter: DrinkAdapter
    private val parentViewModel: MainViewModel by viewModels(ownerProducer = { requireParentFragment() })
    override val viewModel: DrinkViewModel by viewModels()

    override fun getLayoutResource() = R.layout.fragment_drinks

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getDrinks(0)
    }

    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)
        setupAdapter()

        viewModel.swipeRefreshLayoutFinished.asLiveData()
            .observe(viewLifecycleOwner) {
                binding?.srlRefresh?.isRefreshing = false
            }

        binding?.run {
            srlRefresh.setOnRefreshListener {
                viewModel.onRefresh(0)
                search.etSearch.setText("")
                Utils.updateColors(requireContext(), btnAll, btnClassic, btnCraft, btnFav)
            }

            search.etSearch.setOnKeyListener OnKeyListener@{ _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                    val search = search.etSearch.text.toString()
                    refresh(2)
                    hideKeyboard()
                    return@OnKeyListener true
                }
                false
            }

            btnLogout.setOnClickListener {
                viewModel.logout()
                val action = MainFragmentDirections.toLoginFragment()
                navController.navigate(action)
            }

            btnAdd.setOnClickListener {
                val action = MainFragmentDirections.actionMainToAddDrink()
                navController.navigate(action)
            }

            btnAll.setOnClickListener {
                refresh(0)
                Utils.updateColors(requireContext(), btnAll, btnClassic, btnCraft, btnFav)
            }

            btnClassic.setOnClickListener {
                refresh(1)
                Utils.updateColors(requireContext(), btnClassic, btnAll, btnCraft, btnFav)
            }

            btnCraft.setOnClickListener {
                refresh(2)
                Utils.updateColors(requireContext(), btnCraft, btnAll, btnClassic, btnFav)
            }


        }
    }

    override fun onBindData(view: View) {
        super.onBindData(view)
        viewModel.drinks.observe(viewLifecycleOwner) {
            adapter.setDrinks(it.toMutableList())
        }

        parentViewModel.refreshDrinks.observe(viewLifecycleOwner) {
            if (it.first) {
                refresh(0)
                parentViewModel.shouldRefreshDrinks(false)
            }
        }
    }

    fun refresh(cat: Int) {
        viewModel.onRefresh(cat)
    }

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