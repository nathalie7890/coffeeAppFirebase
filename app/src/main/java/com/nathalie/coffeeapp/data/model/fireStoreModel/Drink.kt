package com.nathalie.coffeeapp.data.model.fireStoreModel

data class Drink(
    val id: String? = null,
    val title: String = "",
    val subtitle: String = "",
    val details: String = "",
    val ingredients: String = "",
    val category: Int = 0,
    var favorite: Boolean = false,
    val image: String? = "",
    val editable: Boolean = true,
    val uid: String = ""
)

//{
//    fun toHashMap(): HashMap<String, Any> {
//        return hashMapOf(
//            "title" to title,
//            "subtitle" to subtitle,
//            "details" to details,
//            "ingredients" to ingredients,
//            "category" to category,
//            "favorite" to favorite,
//            "image" to image,
//            "defaultImage" to defaultImage
//        )
//    }
//}