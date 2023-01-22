package com.nathalie.coffeeapp.adapters

import android.graphics.BitmapFactory
import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nathalie.coffeeapp.data.model.Bean
import com.nathalie.coffeeapp.databinding.ItemLayoutBeanBinding

class BeanAdapter(
    private var items: List<Bean>,
    val onClick: (item: Bean) -> Unit
) : RecyclerView.Adapter<BeanAdapter.ItemBeanHolder>() {

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


            item.image?.let { bytes ->
                val bitmap = BitmapFactory.decodeByteArray(item.image, 0, bytes.size)
                ivBeanImage.setImageBitmap(bitmap)
            }

            tvTitle.text = item.title
            tvSubtitle.text = item.subtitle
            tvTaste.text = item.taste

            beanDetail.setOnClickListener {
                onClick(item)
            }

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setBeans(items: List<Bean>) {
        this.items = items
        notifyDataSetChanged()
    }

    class ItemBeanHolder(val binding: ItemLayoutBeanBinding) :
        RecyclerView.ViewHolder(binding.root)
}