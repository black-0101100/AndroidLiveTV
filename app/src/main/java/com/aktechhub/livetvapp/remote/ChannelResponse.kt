package com.aktechhub.livetvapp.remote

import com.aktechhub.livetvapp.model.Channel
import com.aktechhub.livetvapp.model.Genre
import com.aktechhub.livetvapp.model.Language
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChannelResponse(
    @SerialName("channels") val channels: List<Channel>?
)

@Serializable
data class GenreResponse(
    @SerialName("genres") val genres: List<Genre>?
)

@Serializable
data class LanguageResponse(
    @SerialName("languages") val languages: List<Language>?
)
