package com.bapas.agent.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.bapas.agent.R
import com.bapas.agent.databinding.ActAgentMainBinding
import com.bapas.agent.fragment.FragAgentCardDetails
import com.bapas.agent.fragment.FragAgentHome
import com.bapas.agent.fragment.FragAgentProfile
import com.bapas.agent.utility.Utils

class ActAgentMain : ActAgentBase() {
    private lateinit var mBind: ActAgentMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBind = DataBindingUtil.setContentView(this, R.layout.act_agent_main)
        bottomNavigation()
        Utils.withoutBackStack(supportFragmentManager, FragAgentHome(), mBind.mainContainer.id)
    }

    private fun bottomNavigation() {
        var fragName = "FragMain"
        mBind.apply {
            navigation.setOnNavigationItemSelectedListener { item: MenuItem ->
                when (item.itemId) {
                    R.id.navigation_home -> {
                        if (isConnected) {
                            val fragment = FragAgentHome()
                            if (fragName == "FragAgentHome") {
                                return@setOnNavigationItemSelectedListener false
                            } else {
                                OpenFrag(fragment)
                                fragName = "FragAgentHome"
                                return@setOnNavigationItemSelectedListener true
                            }
                        } else {
                            showMessage(resources.getString(R.string.internet_string))
                            return@setOnNavigationItemSelectedListener false
                        }
                    }
                    R.id.navigation_profile -> {
                        val fragment = FragAgentProfile()
                        if (isConnected) {
                            fragName = "FragAgentProfile"
                            OpenFrag(fragment)
//                            showMessage("Coming Soon Videos...")
                            return@setOnNavigationItemSelectedListener true
                        } else {
                            showMessage(resources.getString(R.string.internet_string))
                            return@setOnNavigationItemSelectedListener false
                        }
                    }
                    R.id.navigation_download -> {
                        val fragment = FragAgentCardDetails()
                        if (isConnected) {
                            fragName = "FragAgentCardDetails"
                            OpenFrag(fragment)
                            return@setOnNavigationItemSelectedListener true
                        } else {
                            showMessage(resources.getString(R.string.internet_string))
                            return@setOnNavigationItemSelectedListener false
                        }
                    }
                }
                false
            }
        }
    }
}