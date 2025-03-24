package com.aktechhub.livetvapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChannelResponse(
    @SerialName("channels") val channels: List<Channel>? // Make it nullable to avoid errors
)
