package com.aktechhub.livetvapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Channel(
    @SerialName("lcn_id") val channelNumber: Int,
    @SerialName("name") val channelName: String,
    @SerialName("source") val streamUrl: String,
    @SerialName("source_type") val sourceType: String,
    @SerialName("genre_id") val genreId: Int,
    @SerialName("language_id") val languageId: Int,
    @SerialName("image") val logoUrl: String,
)

@Serializable
data class Genre(
    @SerialName("genre_id") val genreId: Int,
    @SerialName("genre_name") val genreName: String,
   // @SerialName("image") val imageUrl: String,
)

@Serializable
data class Language(
    @SerialName("language_id") val languageId: Int,
    @SerialName("language_name") val languageName: String,
   // @SerialName("image") val imageUrl: String,
)