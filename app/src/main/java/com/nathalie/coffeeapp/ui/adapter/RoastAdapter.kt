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

        holder.binding.run {
            tvTitle.text = item.title
            tvTitle2.text = item.title
            tvDetails.text = item.details

            item.image?.let {
                StorageService.getImageUri(it) { uri ->
                    Glide.with(holder.binding.root)
                        .load(uri)
                        .placeholder(R.color.chocolate)
                        .into(ivRoastImage)
                }
            }

            val components = arrayOf(ivDarkOverlay, tvTitle, tvTitle2, tvDetails, btnEdit)
            cvRoast.setOnClickListener {
                components.forEach {
                    if (it.visibility == View.GONE) it.visibility = View.VISIBLE
                    else it.visibility = View.GONE
                }
            }

            btnEdit.setOnClickListener {
                item.id?.let { it1 -> listener?.onClick(it1) }
            }
        }
    }

    override fun getItemCount() = items.size

    fun setRoasts(items: MutableList<Roast>) {
        val oldItems = this.items
        this.items = items.toMutableList()
        update(oldItems, items) { product1, product2 ->
            product1.id == product2.id
        }
    }

    class ItemRoastHolder(val binding: ItemLayoutRoastBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface Listener {
        fun onClick(id: String)
    }
}