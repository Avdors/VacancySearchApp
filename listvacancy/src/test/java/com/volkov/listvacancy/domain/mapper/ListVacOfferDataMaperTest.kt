package com.volkov.listvacancy.domain.mapper

import com.example.core.data.database.model.OfferModelDataBase
import com.google.common.truth.Truth.assertThat
import com.volkov.listvacancy.data.model.Button
import com.volkov.listvacancy.data.model.DataModelListVacancyOffer
import com.volkov.listvacancy.domain.model.ListOfferDomainModel
import kotlin.test.Test

class ListVacOfferDataMaperTest {
    private val mapper = ListVacOfferDataMaper()

    @Test
    fun `mapOfferToDomain handles null values correctly`() {
        // Создаем объект DataModelListVacancyOffer с null значениями
        val dataModel = DataModelListVacancyOffer(
            id = null,
            title = null,
            link = null,
            button = null
        )

        // Вызываем метод mapOfferToDomain
        val result = mapper.mapOfferToDomain(dataModel)

        // Проверяем, что результат содержит значения по умолчанию вместо null
        assertThat(result.id).isEqualTo("")
        assertThat(result.title).isEqualTo("")
        assertThat(result.link).isEqualTo("")
        assertThat(result.button).isNull() // button должен быть null, если он отсутствует в модели
    }

    @Test
    fun `mapOfferToDomainFromDB handles null button correctly`() {
        // Создаем объект OfferModelDataBase с null для button
        val offerDbModel = OfferModelDataBase(
            id = "123",
            title = "Special Offer",
            link = "http://example.com",
            button = null // button отсутствует
        )

        // Вызываем метод mapOfferToDomainFromDB
        val result = mapper.mapOfferToDomainFromDB(offerDbModel)

        // Проверяем, что значения корректно маппятся и button остаётся null
        assertThat(result.id).isEqualTo("123")
        assertThat(result.title).isEqualTo("Special Offer")
        assertThat(result.link).isEqualTo("http://example.com")
        assertThat(result.button).isNull() // Убеждаемся, что button остался null
    }

    @Test
    fun `mapOfferToDatabase handles null button correctly`() {
        // Создаем объект ListOfferDomainModel с null для button
        val domainModel = ListOfferDomainModel(
            id = "123",
            title = "Special Offer",
            link = "http://example.com",
            button = null // button отсутствует
        )

        // Вызываем метод mapOfferToDatabase
        val result = mapper.mapOfferToDatabase(domainModel)

        // Проверяем, что значения корректно маппятся и button остаётся null
        assertThat(result.id).isEqualTo("123")
        assertThat(result.title).isEqualTo("Special Offer")
        assertThat(result.link).isEqualTo("http://example.com")
        assertThat(result.button).isNull() // Убеждаемся, что button остался null
    }

    @Test
    fun `mapOfferToDomain handles non-null values correctly`() {
        // Создаем объект DataModelListVacancyOffer с заданными значениями
        val dataModel = DataModelListVacancyOffer(
            id = "1",
            title = "Discount Offer",
            link = "http://offerlink.com",
            button = Button("Click Here")
        )

        // Вызываем метод mapOfferToDomain
        val result = mapper.mapOfferToDomain(dataModel)

        // Проверяем, что значения корректно маппятся
        assertThat(result.id).isEqualTo("1")
        assertThat(result.title).isEqualTo("Discount Offer")
        assertThat(result.link).isEqualTo("http://offerlink.com")
        assertThat(result.button?.text).isEqualTo("Click Here")
    }
}