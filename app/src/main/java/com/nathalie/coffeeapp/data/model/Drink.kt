package com.nathalie.coffeeapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Drink(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val title: String,
    val subtitle: String,
    val details: String,
    val ingredients: String,
    val category: Int,
    val image: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Drink

        if (id != other.id) return false
        if (title != other.title) return false
        if (details != other.details) return false
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
        result = 31 * result + (image?.contentHashCode() ?: 0)
        return result
    }
}
