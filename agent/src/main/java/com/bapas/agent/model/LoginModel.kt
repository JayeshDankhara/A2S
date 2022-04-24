package com.bapas.agent.model

data class LoginReq(
    var email: String = "",
    var password: String = ""
)

data class LoginRespo(
    var status: Boolean = false,
    var data: LoginData? = null,
    var message: String = "",
)

data class LoginData(
    var id: String = "",
    var uuid: String = "",
    var name: String = "",
    var email: String = "",
    var email_verified_at: String = "",
    var plain_password: String = "",
    var api_token: String = "",
    var contact_number: String = "",
    var otp_session_id: String = "",
    var otp: String = "",
    var card_limit: String = "",
    var district_id: String = "",
    var taluka_id: String = "",
    var bank_name: String = "",
    var account_number: String = "",
    var ifsc_code: String = "",
    var location: String = "",
    var adhar_card: String = "",
    var pan_card: String = "",
    var address: String = "",
    var qualification: String = "",
    var cast: String = "",
    var card_rang_start: String = "",
    var card_rang_end: String = "",
    var created_at: String = "",
    var updated_at: String = "",
)