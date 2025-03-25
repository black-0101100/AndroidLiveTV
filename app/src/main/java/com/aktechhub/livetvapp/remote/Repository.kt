package com.aktechhub.livetvapp.remote

import com.aktechhub.livetvapp.model.Channel
import com.aktechhub.livetvapp.model.Genre
import com.aktechhub.livetvapp.model.Language
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

    private const val BASE_URL = "https://dooo.jollytv.site/live/api.php"
    private const val USERNAME = "testuser"
    private const val PASSWORD = "testuser"

    suspend fun getChannels(): List<Channel> {
        val apiUrl = "$BASE_URL?username=$USERNAME&password=$PASSWORD&action=live_tv"
        return fetchData<ChannelResponse>(apiUrl)?.channels ?: emptyList()
    }

    suspend fun getGenres(): List<Genre> {
        val apiUrl = "$BASE_URL?username=$USERNAME&password=$PASSWORD&action=live_tv_genre"
        return fetchData<GenreResponse>(apiUrl)?.genres ?: emptyList()
    }

    suspend fun getLanguages(): List<Language> {
        val apiUrl = "$BASE_URL?username=$USERNAME&password=$PASSWORD&action=live_tv_languages"
        return fetchData<LanguageResponse>(apiUrl)?.languages ?: emptyList()
    }

    private suspend inline fun <reified T> fetchData(url: String): T? {
        return try {
            val response: HttpResponse = client.get(url)
            if (response.status == HttpStatusCode.OK) {
                response.body()
            } else {
                println("Error: HTTP ${response.status.value} - Failed to fetch data from $url")
                null
            }
        } catch (e: Exception) {
            println("Exception: ${e.message}")
            null
        }
    }
}