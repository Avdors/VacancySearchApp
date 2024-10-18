package com.example.core.api

object UrlProvider {
    private val urlMap = mapOf(
        "vacancies" to "https://drive.usercontent.google.com/u/0/uc?id=1z4TbeDkbfXkvgpoJprXbN85uCcD7f00r&export=download"
    )

    fun getUrl(key: String): String? {
        return urlMap[key]
    }
}