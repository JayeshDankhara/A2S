package com.bapas.agent.model

data class AddCardModel(
    var member: String = "",
    var name: String = "",
    var adhar_number: String = "",
    var adhar: String? = null,
    var adharName: String = "",
    var photo: String? = null,
    var photoName: String = "",
    var contact_number: String = "",
    var gender: String = "",
    var date_of_born: String = "",
    var location: String = "",
)
