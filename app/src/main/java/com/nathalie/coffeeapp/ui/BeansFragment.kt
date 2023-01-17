package com.nathalie.coffeeapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nathalie.coffeeapp.R

class BeansFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_beans, container, false)
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