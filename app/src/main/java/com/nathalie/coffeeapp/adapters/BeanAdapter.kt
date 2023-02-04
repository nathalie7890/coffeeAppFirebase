package com.nathalie.coffeeapp.adapters

import android.graphics.BitmapFactory
import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.data.model.Bean
import com.nathalie.coffeeapp.databinding.ItemLayoutBeanBinding

// Contains functions and information for the RecyclerView(list that holds all the Beans)
// items(list of Beans), onClick(function to get details of a specific Bean to navigate to that Bean's details)
class BeanAdapter(
    private var items: List<Bean>,
    val onClick: (item: Bean) -> Unit
) : RecyclerView.Adapter<BeanAdapter.ItemBeanHolder>() {

    // create UI that will hold and display the data
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemBeanHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemLayoutBeanBinding.inflate(layoutInflater, parent, false)
        return ItemBeanHolder(binding)
    }

    // bind Beans data that will be displayed by the above created UI
    override fun onBindViewHolder(holder: ItemBeanHolder, position: Int) {
        val item = items[position]

        holder.binding.run {
            sliderBody.value = item.body.toFloat()
            sliderAroma.value = item.aroma.toFloat()
            sliderCaffeine.value = item.caffeine.toFloat()

            sliderBody.isEnabled = false
            sliderAroma.isEnabled = false
            sliderCaffeine.isEnabled = false


            //if image is not null, decode using decodeByteArray
            //else if defaultImage is not null, decode using decodeResources
            //else if both are null, default image set in xml will be displayed
            if (item.image != null) {
                item.image.let { bytes ->
                    val bitmap = BitmapFactory.decodeByteArray(item.image, 0, bytes.size)
                    ivBeanImage.setImageBitmap(bitmap)
                }
            } else if (item.defaultImage != null) {
                val img = holder.itemView.context.resources.getIdentifier(
                    item.defaultImage,
                    "drawable",
                    holder.itemView.context.packageName
                )
                ivBeanImage.setImageResource(img)
            } else ivBeanImage.setImageResource(R.drawable.coffee_bean)

            tvTitle.text = item.title
            tvSubtitle.text = item.subtitle
            tvTaste.text = item.taste

            beanDetail.setOnClickListener {
                onClick(item)
            }

        }
    }

    // returns the number of Beans data in the list
    override fun getItemCount() = items.size

    // fetches list of Beans and "refreshes" the cache if there are any changes to the list
    fun setBeans(items: List<Bean>) {
        this.items = items
        notifyDataSetChanged()
    }

    // child class that is binding to the UI, used by the above class
    class ItemBeanHolder(val binding: ItemLayoutBeanBinding) :
        RecyclerView.ViewHolder(binding.root)
}