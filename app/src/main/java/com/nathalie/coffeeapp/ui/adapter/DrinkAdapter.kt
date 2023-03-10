package com.nathalie.coffeeapp.ui.adapter

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.data.model.fireStoreModel.Drink
import com.nathalie.coffeeapp.data.service.StorageService
import com.nathalie.coffeeapp.databinding.ItemLayoutDrinkBinding
import com.nathalie.coffeeapp.ui.utils.Utils.update

class DrinkAdapter(private var items: MutableList<Drink>) :
    RecyclerView.Adapter<DrinkAdapter.ItemDrinkHolder>() {

    var listener: Listener? = null

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


            item.image?.let {
                StorageService.getImageUri(it) { uri ->
                    Glide.with(holder.binding.root)
                        .load(uri)
                        .placeholder(R.color.chocolate)
                        .into(ivDrinkImage)
                }
            }


            cvDrinkItem.setOnClickListener {
                listener?.onClick(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size

    }

    fun setDrinks(items: MutableList<Drink>) {
        val oldItems = this.items
        this.items = items.toMutableList()
        update(oldItems, items) { product1, product2 ->
            product1.id == product2.id
        }
    }


    class ItemDrinkHolder(val binding: ItemLayoutDrinkBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface Listener {
        fun onClick(item: Drink)
    }
}