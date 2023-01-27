package com.nathalie.coffeeapp.adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.nathalie.coffeeapp.data.model.Drink
import com.nathalie.coffeeapp.databinding.ItemLayoutDrinkBinding

// Contains functions and information for the RecyclerView(list that holds all the Drinks)
// items(list of Drinks), onClick(function to get details of a specific Drink to navigate to that Drink's details)
class DrinkAdapter(
    private var items: List<Drink>,
    val onClick: (item: Drink) -> Unit
) : RecyclerView.Adapter<DrinkAdapter.ItemDrinkHolder>() {

    // create UI that will hold and display the data
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemDrinkHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemLayoutDrinkBinding.inflate(layoutInflater, parent, false)
        return ItemDrinkHolder(binding)
    }

    // bind Drinks data that will be displayed by the above created UI
    override fun onBindViewHolder(holder: ItemDrinkHolder, position: Int) {
        val item = items[position]

        holder.binding.run {
            tvTitle.text = item.title
            tvSubtitle.text = item.subtitle

            item.image?.let { bytes ->
                val bitmap = BitmapFactory.decodeByteArray(item.image, 0, bytes.size)
                ivDrinkImage.setImageBitmap(bitmap)
            }

            ivFav.isVisible = item.favorite == true
            cvDrinkItem.setOnClickListener {
                onClick(item)
            }
        }

    }

    // returns the number of Drinks data in the list
    override fun getItemCount() = items.size

    // fetches list of Drinks and "refreshes" the cache if there are any changes to the list
    fun setDrinks(items: List<Drink>) {
        this.items = items
        notifyDataSetChanged()
    }

    // child class that is binding to the UI, used by the above class
    class ItemDrinkHolder(val binding: ItemLayoutDrinkBinding) :
        RecyclerView.ViewHolder(binding.root)
}