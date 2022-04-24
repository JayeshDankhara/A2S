package com.bapas.a2s.model

data class TalukasReq(
    var district_id: String
)

data class TalukasRespo(
    var status: Boolean = false,
    var data: ArrayList<TalukasData> = arrayListOf(),
    var message: String = "",
)

data class TalukasData(
    var id: String = "",
    var uuid: String = "",
    var name: String = "",
    var created_at: String = "",
    var updated_at: String = "",
)

