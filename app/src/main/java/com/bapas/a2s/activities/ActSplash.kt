package com.bapas.a2s.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.bapas.a2s.R
import com.bapas.a2s.model.VerifyOtpData
import com.bapas.a2s.utility.Utils

class ActSplash : AppCompatActivity() {
    var userData: VerifyOtpData? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_splash)
        userData = Utils.getUserData()
        Handler().postDelayed({
            if (userData?.otp_session_id == null || userData?.otp_session_id.toString().isEmpty()) {
                startActivity(Intent(this, ActLogin::class.java))
            } else {
                startActivity(Intent(this, ActMain::class.java))
            }
            this.finishAffinity()
        }, 2500)
    }
}