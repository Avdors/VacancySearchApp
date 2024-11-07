package com.example.core.api

object UrlProvider {
    private val urlMap = mapOf(
        "vacancies" to "https://drive.google.com/uc?export=download&id=1vMIv-cQR6dV-oEo2htuOhXMx5lp-FC-s"
    )

    fun getUrl(key: String): String? {
        return urlMap[key]
    }
}