package com.example.core.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.net.UnknownHostException

object KtorKlienProvider {
    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO) {
            // Логирование запросов
            install(Logging) {
                level = LogLevel.ALL
            }

            // Настройка JSON сериализации
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }

            // Таймауты для запросов
            install(HttpTimeout) {
                requestTimeoutMillis = 15000 // Время ожидания всего запроса
                connectTimeoutMillis = 10000 // Время ожидания подключения
                socketTimeoutMillis = 15000  // Время ожидания ответа
            }

            // Обработка исключений
            HttpResponseValidator {
                handleResponseExceptionWithRequest { exception, _ ->
                    when (exception) {
                        // Обрабатываем отсутствие интернета или недоступность хоста
                        is UnknownHostException -> {
                            throw Exception("Нет подключения к интернету")
                        }
                        // Обработка таймаутов
                        is HttpRequestTimeoutException -> {
                            throw Exception("Превышено время ожидания запроса")
                        }
                        else -> {
                            throw Exception("Неизвестная ошибка: ${exception.message}")
                        }
                    }
                }
            }

            // Обработка статусов ответа
            expectSuccess = true // По умолчанию кидает исключение на любой код ошибки (4xx, 5xx)

            HttpResponseValidator {
                validateResponse { response: HttpResponse ->
                    val statusCode = response.status.value
                    if (statusCode in 500..599) {
                        throw ResponseException(response, "Ошибка сервера: $statusCode")
                    }
                }
            }
        }
    }
}