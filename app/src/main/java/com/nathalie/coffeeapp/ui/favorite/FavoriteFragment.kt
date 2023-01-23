package com.nathalie.coffeeapp.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.ui.drink.DrinksFragment

class FavoriteFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    companion object {
        private var favoriteFragmentInstance: FavoriteFragment? = null
        fun getInstance(): FavoriteFragment {
            if (favoriteFragmentInstance == null) {
                favoriteFragmentInstance = FavoriteFragment()
            }

            return favoriteFragmentInstance!!
        }
    }
}