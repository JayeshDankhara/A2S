package com.bapas.a2s.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bapas.a2s.R
import com.bapas.a2s.databinding.ActLoginBinding
import com.bapas.a2s.model.GetOtpReq
import com.bapas.a2s.model.GetOtpRespo
import com.bapas.a2s.model.VerifyOtpReq
import com.bapas.a2s.model.VerifyOtpRespo
import com.bapas.a2s.rest.ApisManager
import com.bapas.a2s.utility.Constants
import com.bapas.a2s.utility.Logger
import com.bapas.a2s.utility.Utils
import com.bapas.a2s.utility.setSafeOnClickListener
import com.google.gson.Gson
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActLogin : AppCompatActivity() {
    private lateinit var mBind: ActLoginBinding
    private var otp = ""
    private var otpSessionId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBind = DataBindingUtil.setContentView(this, R.layout.act_login)
        onClickItem()
    }

    private fun onClickItem() {
        mBind.btnVerify.setOnClickListener {
            startActivity(Intent(this, ActMain::class.java))
            this.finish()
        }
        mBind.btnGetOtp.setSafeOnClickListener {
            val mobile = mBind.edtMobile.text.toString()
            if (mobile.isEmpty()) {
                Utils.showToast(this, "Enter Mobile Number")
            } else if (mobile.length < 10) {
                Utils.showToast(this, "Enter Valid Mobile Number")
            } else {
                callApiGetOtp(mobile)
            }
        }
        mBind.btnVerify.setSafeOnClickListener {
            otp = mBind.edtOTP.text.toString()
            if (otp.isEmpty()) {
                Utils.showToast(this, "Enter OTP")
            } else if (otp.length < 6) {
                Utils.showToast(this, "Enter Valid OTP")
            } else {
                callApiVerifyOtp(otp)
            }
        }
    }

    private fun callApiVerifyOtp(otp1: String) {
        val req = VerifyOtpReq()
        req.otp = otp1
        req.otp_session_id = otpSessionId
        Utils.showAndroidevLoader(this)
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                ApisManager().otpVerify(req, object : Callback<VerifyOtpRespo> {
                    override fun onResponse(
                        call: Call<VerifyOtpRespo>,
                        response: Response<VerifyOtpRespo>
                    ) {
                        if (response.isSuccessful) {
                            Utils.closeAndroidevLoader()
                            val body = response.body()
                            Logger.e(Gson().toJson(body))
                            if (body != null) {
                                val data = body.data
                                if (body.status == true) {
                                    if (data != null) {
                                        Prefs.putString(
                                            Constants.USER_DATA,
                                            Gson().toJson(body.data)
                                        )
                                        startActivity(
                                            Intent(
                                                this@ActLogin,
                                                ActMain::class.java
                                            )
                                        )
                                    }
                                } else {
                                    Utils.showToast(this@ActLogin, body?.message.toString())
                                }
                            } else {
                                Utils.showToast(this@ActLogin, body?.message.toString())
                            }
                        } else {
                            Utils.showToast(this@ActLogin, response.message().toString())
                            Utils.closeAndroidevLoader()
                        }
                    }

                    override fun onFailure(call: Call<VerifyOtpRespo>, t: Throwable) {
                        Utils.closeAndroidevLoader()
                        Utils.showToast(this@ActLogin, t.message.toString())
                    }
                })
            }
        }


    }

    private fun callApiGetOtp(ctNumber: String) {
        this.let { ctx ->
            val req = GetOtpReq()
            req.contact_number = ctNumber
            Utils.showAndroidevLoader(ctx)
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                    ApisManager().getOTP(req, object : Callback<GetOtpRespo> {
                        override fun onResponse(
                            call: Call<GetOtpRespo>,
                            response: Response<GetOtpRespo>
                        ) {
                            if (response.isSuccessful) {
                                Utils.closeAndroidevLoader()
                                val body = response.body()
                                if (body!!.status) {
                                    Logger.e(Gson().toJson(body))
                                    otpSessionId = body.data?.Details.toString()
                                    Utils.showToast(ctx, body.message)
                                } else {
                                    Utils.showToast(ctx, body?.message.toString())
                                }
                            } else {
                                Utils.showToast(ctx, response.message().toString())
                                Utils.closeAndroidevLoader()
                            }
                        }

                        override fun onFailure(call: Call<GetOtpRespo>, t: Throwable) {
                            Utils.closeAndroidevLoader()
                            Utils.showToast(ctx, t.message.toString())
                        }
                    })
                }
            }
        }
    }
}