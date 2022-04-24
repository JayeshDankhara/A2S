package com.bapas.agent.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.bapas.agent.R
import com.bapas.agent.model.LoginData
import com.bapas.agent.utility.Logger
import com.bapas.agent.utility.Utils
import com.google.gson.Gson

class ActAgentSplash : ActAgentBase() {
    private var userData: LoginData? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_splash)
        userData = Utils.getUserData()
        Logger.e(Gson().toJson(userData ))
        Handler().postDelayed({
            if (userData != null && userData?.id != null) {
                startActivity(Intent(this, ActAgentMain::class.java))
            } else {
                startActivity(Intent(this, ActAgentLogin::class.java))
            }
            this.finishAffinity()
        }, 2500)
    }
}