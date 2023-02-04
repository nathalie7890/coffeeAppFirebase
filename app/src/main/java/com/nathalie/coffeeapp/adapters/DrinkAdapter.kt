package com.nathalie.coffeeapp.adapters

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.nathalie.coffeeapp.R
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

            //if image is not null, decode using decodeByteArray
            //else if defaultImage is not null, decode using decodeResources
            //else if both are null, default image set in xml will be displayed
            if (item.image != null) {
                item.image.let { bytes ->
                    val bitmap = BitmapFactory.decodeByteArray(item.image, 0, bytes.size)
                    ivDrinkImage.setImageBitmap(bitmap)
                }
            } else if (item.defaultImage != null) {
                val img = holder.itemView.context.resources.getIdentifier(
                    item.defaultImage,
                    "drawable",
                    holder.itemView.context.packageName
                )
                ivDrinkImage.setImageResource(img)
            } else ivDrinkImage.setImageResource(R.drawable.mocha)

            //is item.favorite is true, reveal heart icon else hide heart icon
            ivFav.isVisible = item.favorite == true

            //when item is clicked, take user to drink detail fragment
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