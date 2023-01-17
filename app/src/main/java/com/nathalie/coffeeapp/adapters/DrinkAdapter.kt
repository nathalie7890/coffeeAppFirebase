package com.nathalie.coffeeapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nathalie.coffeeapp.data.Model.Drink
import com.nathalie.coffeeapp.databinding.ItemLayoutDrinkBinding

class DrinkAdapter(
    private var items: List<Drink>
) : RecyclerView.Adapter<DrinkAdapter.ItemDrinkHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemDrinkHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemLayoutDrinkBinding.inflate(layoutInflater, parent, false)
        return ItemDrinkHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemDrinkHolder, position: Int) {
        val item = items[position]

        holder.binding.run {
            tvTitle.text = item.title
            tvSubtitle.text = item.subtitle
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setDrinks(items: List<Drink>) {
        this.items = items
        notifyDataSetChanged()
    }

    class ItemDrinkHolder(val binding: ItemLayoutDrinkBinding) :
        RecyclerView.ViewHolder(binding.root)
}