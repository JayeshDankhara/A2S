package com.bapas.a2s.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.bapas.a2s.R
import com.bapas.a2s.databinding.ActMainBinding
import com.bapas.a2s.fragment.FragCardDetails
import com.bapas.a2s.fragment.FragHome
import com.bapas.a2s.fragment.FragProfile
import com.bapas.a2s.utility.Utils

class ActMain : ActBase() {
    private lateinit var mBind: ActMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBind = DataBindingUtil.setContentView(this, R.layout.act_main)
        bottomNavigation()
        Utils.withoutBackStack(supportFragmentManager, FragHome(), mBind.mainContainer.id)
    }

    private fun bottomNavigation() {
        var fragName = "FragMain"
        mBind.apply {
            navigation.setOnNavigationItemSelectedListener { item: MenuItem ->
                when (item.itemId) {
                    R.id.navigation_home -> {
                        if (isConnected) {
                            val fragment = FragHome()
                            if (fragName == "FragMain") {
                                return@setOnNavigationItemSelectedListener false
                            } else {
                                OpenFrag(fragment)
                                fragName = "FragMain"
                                return@setOnNavigationItemSelectedListener true
                            }
                        } else {
                            showMessage(resources.getString(R.string.internet_string))
                            return@setOnNavigationItemSelectedListener false
                        }
                    }
                    R.id.navigation_profile -> {
                        val fragment = FragProfile()
                        if (isConnected) {
                            fragName = "FragProfile"
                            OpenFrag(fragment)
//                            showMessage("Coming Soon Videos...")
                            return@setOnNavigationItemSelectedListener true
                        } else {
                            showMessage(resources.getString(R.string.internet_string))
                            return@setOnNavigationItemSelectedListener false
                        }
                    }
                    R.id.navigation_download -> {
                        val fragment = FragCardDetails()
                        if (isConnected) {
                            fragName = "FragCardDetails"
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