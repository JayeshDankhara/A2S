package com.bapas.a2s.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bapas.a2s.R
import com.bapas.a2s.utility.InternetConnection
import com.bapas.a2s.utility.Utils
import com.bapas.a2s.utility.snackbar

open class ActBase  : AppCompatActivity()  {
    val isConnected: Boolean
        get() = InternetConnection.checkConnection(this@ActBase)
    companion object {
        var dialogShowing = false
    }
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

    }
    fun showMessage(message: Any) {
        val stMsg = when (message) {
            is String -> message.toString()
            is Int -> resources.getString(message)
            else -> ""
        }

        findViewById<View>(android.R.id.content).apply {
            snackbar(stMsg)
        }
    }

    fun OpenFrag(fragment: Fragment) {
        Utils.removeAllFragment(this)
        supportFragmentManager.beginTransaction()
            .add(R.id.main_container, fragment, fragment.javaClass.simpleName).commit()
//        binding.executePendingBindings()
    }

}