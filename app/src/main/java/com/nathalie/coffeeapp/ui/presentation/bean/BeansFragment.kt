package com.nathalie.coffeeapp.ui.presentation.bean

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.data.model.fireStoreModel.Bean
import com.nathalie.coffeeapp.databinding.FragmentBeansBinding
import com.nathalie.coffeeapp.ui.presentation.BaseFragment
import com.nathalie.coffeeapp.viewmodels.MainViewModel
import com.nathalie.coffeeapp.ui.adapter.BeanAdapter
import com.nathalie.coffeeapp.ui.presentation.MainFragmentDirections
import com.nathalie.coffeeapp.ui.viewmodels.bean.BeanViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BeansFragment : BaseFragment<FragmentBeansBinding>() {
    private lateinit var adapter: BeanAdapter
    private val parentViewModel: MainViewModel by viewModels(ownerProducer = { requireParentFragment() })
    override val viewModel: BeanViewModel by viewModels()
    override fun getLayoutResource() = R.layout.fragment_beans

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getBeans()
    }

    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)
        setupAdapter()
        Log.d("debugging", "beans observe")
        viewModel.beans.observe(viewLifecycleOwner) {
            adapter.setBeans(it.toMutableList())
        }
    }

    override fun onBindData(view: View) {
        super.onBindData(view)

    }

    fun setupAdapter() {
        Log.d("debugging", "Beans fragment")
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter = BeanAdapter(mutableListOf())
        adapter.listener = object : BeanAdapter.Listener {
            override fun onClick(item: Bean) {
                val action = item.id?.let { MainFragmentDirections.actionMainToDetailBean(it) }
                if (action != null) {
                    NavHostFragment.findNavController(this@BeansFragment).navigate(action)
                }
            }
        }

        binding?.rvBeans?.adapter = adapter
        binding?.rvBeans?.layoutManager = layoutManager
    }

    companion object {
        private var beansFragmentInstance: BeansFragment? = null
        fun getInstance(): BeansFragment {
            if (beansFragmentInstance == null) {
                beansFragmentInstance = BeansFragment()
            }

            Log.d("debugging", "beans get instance")
            return beansFragmentInstance!!
        }
    }
}