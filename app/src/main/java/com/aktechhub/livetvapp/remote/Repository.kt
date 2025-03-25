package com.aktechhub.livetvapp.remote

import com.aktechhub.livetvapp.model.Channel
import com.aktechhub.livetvapp.model.ChannelResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object ChannelRepository {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun getChannels(): List<Channel> {
        val apiUrl = "https://dooo.jollytv.site/live/api.php?username=testuser&password=testuser&action=live_tv"
        return try {
            val response: HttpResponse = client.get(apiUrl)

            if (response.status == HttpStatusCode.OK) {
                val channelResponse: ChannelResponse = response.body()
                channelResponse.channels ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // Return empty list if API fails
        }
    }
}
