package com.nathalie.coffeeapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// -> Entity data class for use by the RoastRepository and CoffeeDatabase(Roast is an entity and in RoomDatabase will appear as a Table with columns and rows)
@Entity
data class Roast(

    // -> PrimaryKey is a unique ID used in RoomDatabase to differentiate all the Roasts(assigned to the ID)
    @PrimaryKey
    val id: Long? = null,
    val title: String,
    val details: String,
    val image: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Roast

        if (id != other.id) return false
        if (title != other.title) return false
        if (image != null) {
            if (other.image == null) return false
            if (!image.contentEquals(other.image)) return false
        } else if (other.image != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + title.hashCode()
        result = 31 * result + (image?.contentHashCode() ?: 0)
        return result
    }
}
