package com.volkov.listvacancy.presentation.model

class ListOfferModel(
    val id: String? = null,  // id может отсутствовать в некоторых объектах, поэтому он nullable
    val title: String,
    val link: String,
    val button: ButtonModel? = null // button присутствует не во всех объектах offers
) {
}

class ButtonModel(
    val text: String
) {}
