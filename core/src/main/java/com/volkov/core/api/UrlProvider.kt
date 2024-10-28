package com.example.core.api

object UrlProvider {
    private val urlMap = mapOf(
        //"vacancies" to "https://drive.usercontent.google.com/u/0/uc?id=1z4TbeDkbfXkvgpoJprXbN85uCcD7f00r&export=download"
        "vacancies" to "https://drive.google.com/uc?export=download&id=1vMIv-cQR6dV-oEo2htuOhXMx5lp-FC-s"
        //"vacancies" to "https://drive.google.com/file/d/1vMIv-cQR6dV-oEo2htuOhXMx5lp-FC-s/view?usp=sharing"
        //"vacancies" to "https://drive.google.com/uc?export=download&id=1vMIv-cQR6dV-oEo2htuOhXMx5lp-FC-s"
        //"vacancies" to "https://drive.google.com/file/d/1vMIv-cQR6dV-oEo2htuOhXMx5lp-FC-s"
    )

    fun getUrl(key: String): String? {
        return urlMap[key]
    }
}