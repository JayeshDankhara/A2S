package com.bapas.a2s.model

data class HospitalReq(
    var district_id: String,
    var taluka_id: String
)

data class HospitalRespo(
    var status: Boolean = false,
    var data: ArrayList<HospitalData> = arrayListOf(),
    var message: String = "",
)

data class HospitalData(
    var id: String = "",
    var uuid: String = "",
    var district_id: String = "",
    var taluka_id: String = "",
    var name: String = "",
    var address: String = "",
    var cell: String = "",
    var phone: String = "",
    var email: String = "",
    var password: String = "",
    var created_at: String = "",
    var updated_at: String = "",
)
