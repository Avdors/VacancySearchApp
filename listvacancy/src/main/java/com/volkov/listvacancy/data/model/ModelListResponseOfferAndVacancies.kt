package com.volkov.listvacancy.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ModelListResponseOfferAndVacancies(
    @SerialName("offers")
    val offers: List<FeatListVacancyOffer?>? = null,
    @SerialName("vacancies")
    val vacancies: List<FeatListVacancy?>? = null
)


@Serializable
data class FeatListVacancyOffer(
    @SerialName("id")
    val id: String? = null,

    @SerialName("title")
    val title: String? = null,

    @SerialName("link")
    val link: String? = null,

    @SerialName("button")
    val button: Button? = null
)

@Serializable
data class Button(
    @SerialName("text")
    val text: String? = null
)

@Serializable
data class FeatListVacancy(
    @SerialName("id")
    val id: String? = null,

    @SerialName("lookingNumber")
    val lookingNumber: Int? = null,

    @SerialName("title")
    val title: String? = null,

    @SerialName("address")
    val address: FeatListAddress? = null,

    @SerialName("company")
    val company: String? = null,

    @SerialName("experience")
    val experience: FeatListExperience? = null,

    @SerialName("publishedDate")
    val publishedDate: String? = null,

    @SerialName("isFavorite")
    val isFavorite: Boolean? = null,

    @SerialName("salary")
    val salary: FeatListSalary? = null,

    @SerialName("schedules")
    val schedules: List<String?>? = null,

    @SerialName("appliedNumber")
    val appliedNumber: Int? = null,

    @SerialName("description")
    val description: String? = null,

    @SerialName("responsibilities")
    val responsibilities: String? = null,

    @SerialName("questions")
    val questions: List<String?>? = null
)

@Serializable
data class FeatListAddress(
    @SerialName("town")
    val town: String? = null,

    @SerialName("street")
    val street: String? = null,

    @SerialName("house")
    val house: String? = null
)

@Serializable
data class FeatListExperience(
    @SerialName("previewText")
    val previewText: String? = null,

    @SerialName("text")
    val text: String? = null
)

@Serializable
data class FeatListSalary(
    @SerialName("full")
    val full: String? = null,

    @SerialName("short")
    val short: String? = null
)