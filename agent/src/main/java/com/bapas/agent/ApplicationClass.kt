package com.bapas.agent

import android.app.Activity
import android.app.Application
import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatDelegate
import com.pixplicity.easyprefs.library.Prefs
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump

class ApplicationClass : Application() {
    private val activity: Activity? = null
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("font/Regular.ttf")
                            .setFontAttrId(io.github.inflationx.calligraphy3.R.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )
        Prefs.Builder().setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()
    }
}