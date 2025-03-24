package com.aktechhub.livetvapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Channel(
    @SerialName("lcn_id") val number: Int,          // Map lcn_id -> number
    @SerialName("name") val name: String,          // No change needed
    @SerialName("source") val streamUrl: String,   // Map source -> streamUrl
    @SerialName("image") val logoUrl: String       // Map image -> logoUrl
)
