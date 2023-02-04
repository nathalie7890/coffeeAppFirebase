package com.nathalie.coffeeapp.ui.bean

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nathalie.coffeeapp.MyApplication
import com.nathalie.coffeeapp.adapters.BeanAdapter
import com.nathalie.coffeeapp.databinding.FragmentBeansBinding
import com.nathalie.coffeeapp.ui.MainFragmentDirections
import com.nathalie.coffeeapp.viewmodels.bean.BeanViewModel
import com.nathalie.coffeeapp.viewmodels.MainViewModel

class BeansFragment : Fragment() {
    private lateinit var binding: FragmentBeansBinding
    private lateinit var adapter: BeanAdapter
    private val viewModel: BeanViewModel by viewModels {
        BeanViewModel.Provider((requireActivity().applicationContext as MyApplication).beanRepo)
    }
    private val parentViewModel: MainViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBeansBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()

        //show the second item in recycle view
        binding.rvBeans.post {
            binding.rvBeans.scrollToPosition(1)
        }

        viewModel.beans.observe(viewLifecycleOwner) {
            adapter.setBeans(it)
            //if no item, display this
            binding.emptyBeans.llEmpty.isVisible = adapter.itemCount <= 0
        }

        parentViewModel.refreshBeans.observe(viewLifecycleOwner) {
            refresh()
        }

        //when add bean btn is clicked, take user to add bean fragment
        binding.btnAddBean.setOnClickListener {
            val action = MainFragmentDirections.actionMainToAddBean()
            NavHostFragment.findNavController(requireParentFragment()).navigate(action)
        }
    }

    //fetch beans
    fun refresh() {
        viewModel.getBeans()
    }

    fun setupAdapter() {
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter = BeanAdapter(emptyList()) {
            val action = MainFragmentDirections.actionMainToDetailBean(it.id!!)
            NavHostFragment.findNavController(this).navigate(action)
        }
        binding.rvBeans.adapter = adapter
        binding.rvBeans.layoutManager = layoutManager
    }

    companion object {
        private var beansFragmentInstance: BeansFragment? = null
        fun getInstance(): BeansFragment {
            if (beansFragmentInstance == null) {
                beansFragmentInstance = BeansFragment()
            }

            return beansFragmentInstance!!
        }
    }
}