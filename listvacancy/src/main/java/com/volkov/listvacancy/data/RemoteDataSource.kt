package com.volkov.listvacancy.data


import com.example.core.api.UrlProvider
import com.volkov.listvacancy.data.model.ModelListResponseOfferAndVacancies
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import java.net.UnknownHostException

class RemoteDataSource(private val client: HttpClient) {

    suspend fun getData(): ModelListResponseOfferAndVacancies {
        val json = client.get {
            val url =
                UrlProvider.getUrl("vacancies")  // в core лежит список адресов для подключения, при работе с feature, их можно подменять вручную на любой тестовый адрес
            url(url)
        }.bodyAsText()

        return try {
            Json.decodeFromString<ModelListResponseOfferAndVacancies>(json)
        } catch (e: UnknownHostException) {
            // обработка отсутствия интернета
            throw Exception("Проблема с интернет соединением: ${e.message}")
        } catch (e: HttpRequestTimeoutException) {
            // обработка таймаутов
            throw Exception("Время ожидания истекло: ${e.message}")
        } catch (e: ClientRequestException) {
            // обработка таймаутов
            throw Exception("Ошибка клиента: ${e.message}")
        } catch (e: ServerResponseException) {
            // обработка таймаутов
            throw Exception("Ошибка клиента: ${e.message}")
        }
    }
}