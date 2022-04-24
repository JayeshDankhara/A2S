package com.bapas.agent.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bapas.agent.BaseFragment
import com.bapas.agent.R
import com.bapas.agent.activities.ActAgentLogin
import com.bapas.agent.activities.ActAgentProfile
import com.bapas.agent.databinding.FragAgentProfileBinding
import com.bapas.agent.utility.setSafeOnClickListener
import com.pixplicity.easyprefs.library.Prefs

class FragAgentProfile : BaseFragment() {
    lateinit var mBind: FragAgentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBind = DataBindingUtil.inflate(inflater, R.layout.frag_agent_profile, container, false)
        onClickItem()
        return mBind.root
    }

    private fun onClickItem() {

        mBind.lnrLogout.setSafeOnClickListener {
            callAPILogout()
        }
        mBind.lnrProfile.setSafeOnClickListener {
            val intent = Intent(this.requireActivity(), ActAgentProfile::class.java)
            startActivity(intent)
        }
    }

    private fun callAPILogout() {
        Prefs.clear()
        this.requireActivity().let {
            it.startActivity(Intent(it, ActAgentLogin::class.java))
            it.finishAffinity()
        }
    }
}