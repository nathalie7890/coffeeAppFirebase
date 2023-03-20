package com.nathalie.coffeeapp.data.model.fireStoreModel

data class Bean(
    val id: String? = null,
    val title: String = "",
    val subtitle: String = "",
    val taste: String = "",
    val details: String = "",
    val body: Int = 1,
    val aroma: Int = 1,
    val caffeine: Int = 1,
    val image: String? = null,
    val editable: Boolean = true,
    val uid: String = ""
)
//{
//    fun toHashMap(): HashMap<String, Any> {
//        return hashMapOf(
//            "title" to title,
//            "subtitle" to subtitle,
//            "taste" to taste,
//            "details" to details,
//            "body" to body,
//            "aroma" to aroma,
//            "caffeine" to caffeine,
//            "image" to image,
//            "defaultImage" to defaultImage
//        )
//    }
//}

