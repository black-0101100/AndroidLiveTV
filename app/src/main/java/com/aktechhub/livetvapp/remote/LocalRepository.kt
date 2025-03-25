package com.aktechhub.livetvapp.remote


import androidx.compose.runtime.Immutable

@Immutable
data class Language(val id: Int, val name: String)

@Immutable
data class Category(val id: Int, val name: String)

@Immutable
data class Channel(val id: Int, val name: String, val number: Int)

object LocalRepository {
    val languages = listOf(
        Language(1, "Tamil"),
        Language(2, "English"),
        Language(3, "Malayalam"),
        Language(4, "Telugu"),
        Language(5, "Hindi")

    )

    val categories = listOf(
        Category(1, "Entertainment"),
        Category(2, "Movies"),
        Category(3, "Music"),
        Category(4, "News"),
        Category(5, "Sports")
    )

    val channels = listOf(
        Channel(1, "Sun TV", 501),
        Channel(2, "Star Vijay", 502),
        Channel(3, "Zee Tamil", 503),
        Channel(4, "K TV", 504),
        Channel(5, "Colors Tamil", 505),
        Channel(6, "Sun Life", 506),
        Channel(7, "Adithya TV", 507)
    )
}
