package com.nathalie.coffeeapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// -> Entity data class for use by the BeanRepository and CoffeeDatabase (Bean is an entity and in RoomDatabase will appear as a Table with columns and rows)
@Entity
data class Bean(

    // -> PrimaryKey is a unique ID used in RoomDatabase to differentiate all the Beans(assigned to the ID)
    @PrimaryKey
    val id: Long? = null,
    val title: String,
    val subtitle: String,
    val taste: String,
    val details: String,
    val body: Int,
    val aroma: Int,
    val caffeine: Int,
    val image: ByteArray? = null,
    val defaultImage: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Bean

        if (id != other.id) return false
        if (title != other.title) return false
        if (details != other.details) return false
        if (body != other.body) return false
        if (aroma != other.aroma) return false
        if (caffeine != other.caffeine) return false
        if (image != null) {
            if (other.image == null) return false
            if (!image.contentEquals(other.image)) return false
        } else if (other.image != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + title.hashCode()
        result = 31 * result + details.hashCode()
        result = 31 * result + body
        result = 31 * result + aroma
        result = 31 * result + caffeine
        result = 31 * result + (image?.contentHashCode() ?: 0)
        return result
    }
}
