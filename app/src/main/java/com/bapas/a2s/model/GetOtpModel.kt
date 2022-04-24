package com.bapas.a2s.model


data class GetOtpReq(
    var contact_number: String = "",
)

class GetOtpRespo {
    var status: Boolean = false
    var data: GetOtpData? = null
    var message: String  = ""
}

class GetOtpData {
    var Status: String = ""
    var Details: String = ""
}
