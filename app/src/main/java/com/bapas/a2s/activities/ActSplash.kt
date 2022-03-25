package com.bapas.a2s.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.bapas.a2s.R

class ActSplash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_splash)

        Handler().postDelayed({
            startActivity(Intent(this, ActLogin::class.java))
            this.finishAffinity()
        }, 2500)
    }
}