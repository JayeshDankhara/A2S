package com.bapas.a2s.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bapas.a2s.R
import com.bapas.a2s.databinding.ActLoginBinding

class ActLogin : AppCompatActivity() {
    private lateinit var mBind: ActLoginBinding
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
    }
}