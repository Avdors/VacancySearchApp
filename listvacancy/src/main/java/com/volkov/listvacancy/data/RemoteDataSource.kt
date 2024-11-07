package com.volkov.listvacancy.data

import android.util.Log
import com.example.core.api.UrlProvider
import com.volkov.listvacancy.data.model.ModelListResponseOfferAndVacancies
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Url
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json
import java.net.UnknownHostException

class RemoteDataSource(private val client: HttpClient) {

    suspend fun getData(): ModelListResponseOfferAndVacancies {
        val url = Url(UrlProvider.getUrl("vacancies") ?: throw Exception("URL not found"))
        val response = client.get {
//            val url =
//                UrlProvider.getUrl("vacancies")  // вот здесь Ktor может интерпретировать переданный URL как относительный, и добавляет к нему //localhost
            url(url)
        }
        val test = response
        if (response.status.isSuccess()) {

            val json = response.bodyAsText()

            return try {
                Json.decodeFromString<ModelListResponseOfferAndVacancies>(json)
            } catch (e: UnknownHostException) {
                // Обработка отсутствия интернета
                throw Exception("Проблема с интернет-соединением: ${e.message}")
            } catch (e: HttpRequestTimeoutException) {
                // Обработка таймаутов
                throw Exception("Время ожидания истекло: ${e.message}")
            } catch (e: ClientRequestException) {
                // Обработка ошибок 4xx (например, 404 Not Found)
                throw Exception("Ошибка клиента: ${e.message}")
            } catch (e: ServerResponseException) {
                // Обработка ошибок сервера 5xx
                throw Exception("Ошибка сервера: ${e.message}")
            } catch (e: ResponseException) {
                // Обработка прочих ошибок HTTP
                throw Exception("Ошибка HTTP: ${e.message}")
            } catch (e: Exception) {
                // Обработка всех остальных исключений
                throw Exception("Неизвестная ошибка: ${e.message}")
            }
        } else {
            Log.d("RemoteDataSource", "Requesting data from URL respons: $response")
            return throw Exception("Ошибка сервера: ${response.status.value}")
        }


    }
}