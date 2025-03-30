package com.aktechhub.livetvapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Channel(
    @SerialName("stream_id") val channelNumber: Int,
    @SerialName("stream_name") val channelName: String,
    @SerialName("stream_source") val streamUrl: String,
    @SerialName("stream_source_type") val sourceType: String,
    @SerialName("stream_genre") val genreId: Int,
    @SerialName("stream_languages") val languageId: Int,
    @SerialName("stream_image") val logoUrl: String,
    @SerialName("stream_epg_id") val epgId: String?,
    @SerialName("stream_price") val price: String
)

@Serializable
data class Genre(
    @SerialName("genre_id") val genreId: Int,
    @SerialName("genre_name") val genreName: String,
    @SerialName("language_id") val languageId: Int,
   //@SerialName("image") val imageUrl: String,
)

@Serializable
data class Language(
    @SerialName("lang_id") val languageId: Int,
    @SerialName("lang_name") val languageName: String,
)