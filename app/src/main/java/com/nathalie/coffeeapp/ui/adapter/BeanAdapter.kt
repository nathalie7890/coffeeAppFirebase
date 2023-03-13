package com.nathalie.coffeeapp.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.data.model.fireStoreModel.Bean
import com.nathalie.coffeeapp.data.service.StorageService
import com.nathalie.coffeeapp.databinding.ItemLayoutBeanBinding
import com.nathalie.coffeeapp.ui.utils.Utils.update

class BeanAdapter(private var items: MutableList<Bean>) :
    RecyclerView.Adapter<BeanAdapter.ItemBeanHolder>() {

    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemBeanHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemLayoutBeanBinding.inflate(layoutInflater, parent, false)
        return ItemBeanHolder(binding)
    }


    override fun onBindViewHolder(holder: ItemBeanHolder, position: Int) {
        val item = items[position]

        holder.binding.run {
            sliderBody.value = item.body.toFloat()
            sliderAroma.value = item.aroma.toFloat()
            sliderCaffeine.value = item.caffeine.toFloat()

            sliderBody.isEnabled = false
            sliderAroma.isEnabled = false
            sliderCaffeine.isEnabled = false

            tvTitle.text = item.title
            tvSubtitle.text = item.subtitle
            tvTaste.text = item.taste


            item.image?.let {
                StorageService.getImageUri(it) { uri ->
                    Glide.with(holder.binding.root)
                        .load(uri)
                        .placeholder(R.color.chocolate)
                        .into(ivBeanImage)
                }
            }


            beanDetail.setOnClickListener {
                listener?.onClick(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setBeans(items: MutableList<Bean>) {
        val oldItems = this.items
        this.items = items.toMutableList()
        update(oldItems, items) { product1, product2 ->
            product1.id == product2.id
        }
    }

    class ItemBeanHolder(val binding: ItemLayoutBeanBinding) : RecyclerView.ViewHolder(binding.root)

    interface Listener {
        fun onClick(item: Bean)
    }
}