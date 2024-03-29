package com.nathalie.coffeeapp.data.model.fireStoreModel

data class Roast(
    val id: String? = null,
    val title: String = "",
    val details: String = "",
    val image: String? = null,
    val editable: Boolean = true,
    val uid: String = ""
)
//{
//    fun toHashMap(): HashMap<String, Any> {
//        return hashMapOf(
//            "title" to title,
//            "details" to details,
//            "image" to image,
//            "defaultImage" to defaultImage
//        )
//    }
//}