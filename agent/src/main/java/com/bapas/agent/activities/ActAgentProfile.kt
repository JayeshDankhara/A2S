package com.bapas.agent.activities

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bapas.agent.R
import com.bapas.agent.databinding.ActAgentProfileBinding
import com.bapas.agent.model.LoginData
import com.bapas.agent.utility.Utils
import com.bapas.agent.utility.setSafeOnClickListener

class ActAgentProfile : ActAgentBase() {
    private lateinit var mBind: ActAgentProfileBinding
    private var agentData: LoginData? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBind = DataBindingUtil.setContentView(this, R.layout.act_agent_profile)
        agentData = Utils.getUserData()
        setAgentData()
        onClickItem()
    }

    private fun setAgentData() {
        mBind.edtName.setText(agentData?.name)
        mBind.edtMobile.setText(agentData?.contact_number)
        mBind.edtEmail.setText(agentData?.email)
        mBind.edtAadhar.setText(agentData?.adhar_card)
        mBind.edtPanCard.setText(agentData?.pan_card)
    }

    private fun onClickItem() {
        mBind.btnBack.setSafeOnClickListener {
            this.onBackPressed()
        }
    }
}