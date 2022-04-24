package com.bapas.a2s.model

data class DistrictRespo(
    var status: Boolean = false,
    var data: ArrayList<DistrictData> = arrayListOf(),
    var message: String = "",
)

data class DistrictData(
    var id: String = "",
    var uuid: String = "",
    var name: String = "",
    var created_at: String = "",
    var updated_at: String = "",
)

