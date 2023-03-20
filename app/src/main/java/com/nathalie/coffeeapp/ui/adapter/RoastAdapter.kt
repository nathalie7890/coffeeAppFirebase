package com.nathalie.coffeeapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.data.model.fireStoreModel.Roast
import com.nathalie.coffeeapp.data.service.StorageService
import com.nathalie.coffeeapp.databinding.ItemLayoutRoastBinding
import com.nathalie.coffeeapp.ui.utils.Utils.update

// To attach data to the RecyclerView
class RoastAdapter(private var items: MutableList<Roast>) :
    RecyclerView.Adapter<RoastAdapter.ItemRoastHolder>() {

    var listener: Listener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemRoastHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemLayoutRoastBinding.inflate(layoutInflater, parent, false)
        return ItemRoastHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemRoastHolder, position: Int) {
        val item = items[position]

        // Attach data to the single item layout
        holder.binding.run {
            tvTitle.text = item.title
            tvTitle2.text = item.title
            tvDetails.text = item.details

            // attach image for the item
            item.image?.let {
                StorageService.getImageUri(it) { uri ->
                    Glide.with(holder.binding.root)
                        .load(uri)
                        .placeholder(R.color.chocolate)
                        .into(ivRoastImage)
                }
            }

            // listener to show or hide the roast item details
            val components = arrayOf(ivDarkOverlay, tvTitle, tvTitle2, tvDetails)
            cvRoast.setOnClickListener {
                components.forEach {
                    if (it.visibility == View.GONE) it.visibility = View.VISIBLE
                    else it.visibility = View.GONE
                }
                // is item's editable = false, hide edit btn
                if (!item.editable) btnEdit.visibility = View.INVISIBLE
            }

            // listener function, if click on item
            btnEdit.setOnClickListener {
                item.id?.let { it1 -> listener?.onClick(it1) }
            }
        }
    }

    override fun getItemCount() = items.size

    // fetches and updates the items
    fun setRoasts(items: MutableList<Roast>) {
        val oldItems = this.items
        this.items = items.toMutableList()
        update(oldItems, items) { product1, product2 ->
            product1.id == product2.id
        }
    }

    class ItemRoastHolder(val binding: ItemLayoutRoastBinding) :
        RecyclerView.ViewHolder(binding.root)

    // same listener used in code above
    interface Listener {
        fun onClick(id: String)
    }
}