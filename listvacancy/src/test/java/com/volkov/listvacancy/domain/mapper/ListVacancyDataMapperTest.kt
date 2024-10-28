package com.volkov.listvacancy.domain.mapper

import com.google.common.truth.Truth.assertThat
import com.volkov.listvacancy.data.model.DataModelListVacancy
import com.volkov.listvacancy.domain.model.ListAddressDomainModel
import com.volkov.listvacancy.domain.model.ListExperienceDomainModel
import com.volkov.listvacancy.domain.model.ListSalaryDomainModel
import kotlin.test.Test

class ListVacancyDataMapperTest {
    private val mapper = ListVacancyDataMapper()

    @Test
    fun `mapToDomain handles null values correctly`() {
        // Создаем объект DataModelListVacancy с null значениями
        val dataModel = DataModelListVacancy(
            id = null,
            lookingNumber = null,
            title = null,
            address = null,
            company = null,
            experience = null,
            publishedDate = null,
            isFavorite = null,
            salary = null,
            schedules = null,
            appliedNumber = null,
            description = null,
            responsibilities = null,
            questions = null
        )

        // Вызываем метод mapToDomain
        val result = mapper.mapToDomain(dataModel)

        // Проверяем, что результат содержит значения по умолчанию вместо null
        assertThat(result.id).isEqualTo("")
        assertThat(result.lookingNumber).isEqualTo(0)
        assertThat(result.title).isEqualTo("")
        assertThat(result.address).isEqualTo(ListAddressDomainModel("", "", ""))
        assertThat(result.company).isEqualTo("")
        assertThat(result.experience).isEqualTo(ListExperienceDomainModel("", ""))
        assertThat(result.publishedDate).isEqualTo("")
        assertThat(result.isFavorite).isFalse()
        assertThat(result.salary).isEqualTo(ListSalaryDomainModel("", ""))
        assertThat(result.schedules).isEmpty()
        assertThat(result.appliedNumber).isEqualTo(0)
        assertThat(result.description).isEqualTo("")
        assertThat(result.responsibilities).isEqualTo("")
        assertThat(result.questions).isEmpty()
    }

    @Test
    fun `mapToDomain handles partially null values correctly`() {
        // Создаем объект DataModelListVacancy с частично null значениями
        val dataModel = DataModelListVacancy(
            id = "1",
            lookingNumber = 100,
            title = "Vacancy Title",
            address = null,
            company = "Company Name",
            experience = null,
            publishedDate = "2024-10-05",
            isFavorite = true,
            salary = null,
            schedules = listOf("Full-time", null),
            appliedNumber = 5,
            description = null,
            responsibilities = "Some responsibilities",
            questions = listOf("Question 1", null)
        )

        // Вызываем метод mapToDomain
        val result = mapper.mapToDomain(dataModel)

        // Проверяем значения
        assertThat(result.id).isEqualTo("1")
        assertThat(result.lookingNumber).isEqualTo(100)
        assertThat(result.title).isEqualTo("Vacancy Title")
        assertThat(result.address).isEqualTo(ListAddressDomainModel("", "", "")) // По умолчанию
        assertThat(result.company).isEqualTo("Company Name")
        assertThat(result.experience).isEqualTo(ListExperienceDomainModel("", "")) // По умолчанию
        assertThat(result.publishedDate).isEqualTo("2024-10-05")
        assertThat(result.isFavorite).isTrue()
        assertThat(result.salary).isEqualTo(ListSalaryDomainModel("", "")) // По умолчанию
        assertThat(result.schedules).containsExactly("Full-time") // Фильтрация null значений
        assertThat(result.appliedNumber).isEqualTo(5)
        assertThat(result.description).isEqualTo("") // По умолчанию
        assertThat(result.responsibilities).isEqualTo("Some responsibilities")
        assertThat(result.questions).containsExactly("Question 1") // Фильтрация null значений
    }
}