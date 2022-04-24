package com.bapas.agent

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.bapas.agent.activities.ActAgentBase

abstract class BaseFragment : Fragment() {
    var agentBaseContext: ActAgentBase? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        agentBaseContext = ActAgentBase()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.agentBaseContext = (context as? ActAgentBase)!!
    }

    fun createDialog(layout: Int, it: Context): Dialog? {
        val materialDialog = Dialog(it)
        materialDialog.setContentView(layout)
        materialDialog.setCancelable(false)
        SetWindowManagerCustom(materialDialog)
        materialDialog.show()
        return materialDialog
    }


    fun SetWindowManagerCustom(materialDialog: Dialog) {

        val layOutParams = WindowManager.LayoutParams()
        layOutParams.copyFrom(materialDialog.window!!.attributes)
    }



}