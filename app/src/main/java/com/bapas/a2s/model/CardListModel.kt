package com.bapas.a2s.model


data class CardListReq(
    var user_type: String = "",
)
data class CardListRespo(
    var status: Boolean = false,
    var data: ArrayList<CardListData> = arrayListOf(),
    var message: String = "",
)

data class CardListData(
    var id: String = "",
    var uuid: String = "",
    var name: String = "",
    var agent_id: String = "",
    var adhar_number: String = "",
    var adhar: String = "",
    var photo: String = "",
    var serial_number: String = "",
    var family: String = "",
    var cast: String = "",
    var contact_number: String = "",
    var gender: String = "",
    var date_of_born: String = "",
    var location: String = "",
    var type: String = "",
    var created_at: String = "",
    var updated_at: String = "",
    var agent_name: String = "",
)
