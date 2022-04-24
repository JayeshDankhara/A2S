package com.bapas.agent.utility;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

/**
 * Created by JayeshDankhara on 12/16/2020.
 */
public class InternetConnection {

    public static boolean checkConnection(Context context) {
        return  ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }
    //Show snack bar
    public static void showSnack(boolean isConnected, Activity activity) {
        String message;
        Snackbar snackbar = null;
        int color;
        if (!isConnected) {
            message = "Sorry! Not connected to internet";
            color = Color.parseColor("#E12035");

            View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
            Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show();
            View sbView = snackbar.getView();
            TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);

            textView.setTextColor(color);
            snackbar.show();
        }
        else {
            snackbar.dismiss();
        }
    }
}
