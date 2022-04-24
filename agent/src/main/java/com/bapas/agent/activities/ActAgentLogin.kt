package com.bapas.agent.activities

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bapas.agent.R
import com.bapas.agent.databinding.ActAgentLoginBinding
import com.bapas.agent.model.LoginReq
import com.bapas.agent.model.LoginRespo
import com.bapas.agent.rest.ApisManager
import com.bapas.agent.utility.Constants
import com.bapas.agent.utility.Logger
import com.bapas.agent.utility.Utils
import com.bapas.agent.utility.setSafeOnClickListener
import com.google.gson.Gson
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActAgentLogin : ActAgentBase() {
    private lateinit var mBind: ActAgentLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBind = DataBindingUtil.setContentView(this, R.layout.act_agent_login)
        onClickItem()
    }

    private fun onClickItem() {
        mBind.btnLogin.setSafeOnClickListener {
            val mail = mBind.edtUserNAme.text.toString()
            val pass = mBind.edtPassword.text.toString()
            if (mail.isEmpty()) {
                Utils.showToast(this, "Enter Mail")
            } else if (!Utils.IsValidEmailAddress(mail)) {
                Utils.showToast(this, "Enter Valid Mail")
            } else if (pass.isEmpty()) {
                Utils.showToast(this, "Enter Password")
            } else if (pass.length < 6) {
                Utils.showToast(this, "Password must be 6 character")
            } else {
                val req = LoginReq()
                req.email = mail
                req.password = pass

                Utils.showAndroidevLoader(this)
                CoroutineScope(Dispatchers.IO).launch {
                    withContext(Dispatchers.Main) {
                        ApisManager().login(req, object : Callback<LoginRespo> {
                            override fun onResponse(
                                call: Call<LoginRespo>,
                                response: Response<LoginRespo>
                            ) {
                                if (response.isSuccessful) {
                                    Utils.closeAndroidevLoader()
                                    val body = response.body()
                                    Logger.e(Gson().toJson(body))
                                    if (body != null) {
                                        val data = body.data
                                        if (data != null) {
                                            Prefs.putString(
                                                Constants.USER_DATA,
                                                Gson().toJson(data)
                                            )
                                            startActivity(
                                                Intent(
                                                    this@ActAgentLogin,
                                                    ActAgentMain::class.java
                                                )
                                            )
                                            finishAffinity()
                                        } else Utils.showToast(this@ActAgentLogin, body.message)
                                    } else {
                                        Utils.showToast(this@ActAgentLogin, body?.message.toString())
                                    }
                                } else {
                                    Utils.showToast(this@ActAgentLogin, response.message().toString())
                                    Utils.closeAndroidevLoader()
                                }
                            }

                            override fun onFailure(call: Call<LoginRespo>, t: Throwable) {
                                Utils.closeAndroidevLoader()
                                Utils.showToast(this@ActAgentLogin, t.message.toString())
                            }
                        })
                    }
                }
            }
        }
    }
}