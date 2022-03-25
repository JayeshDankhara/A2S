package com.bapas.a2s.utility

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.bapas.a2s.R
import com.bapas.a2s.activities.ActBase
import com.google.android.material.snackbar.Snackbar

/**
 * Extension functions for set visibility of any view by calling
 * yourView.visible()
 * yourView.gone()
 */
fun View.gone() {
    this.visibility = View.GONE
}

fun ImageView.setLocalImage(uri: Uri, applyCircle: Boolean = false) {
    /*val glide = Glide.with(this).load(uri).placeholder(R.drawable.progress_animation)
    if (applyCircle) {
        glide.apply(RequestOptions.circleCropTransform()).into(this)
    } else {
        glide.into(this)
    }*/
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

/**
 * Extension function for show message like toast
 * example :
 * in Activity = snackbar("test")
 * in Fragment = activity.snackbar("test")
 */
fun View.snackbar(@StringRes message: Int) = Snackbar
    .make(this, message, Snackbar.LENGTH_SHORT)
    .apply { show() }

fun View.snackbar(message: CharSequence) = Snackbar
    .make(this, message, Snackbar.LENGTH_SHORT)
    .apply { show() }
/*
fun loadCircleImage(
    context: Context,
    imageUri: String,
    @DrawableRes placeholder: Int,
    imageView: ImageView
) {
    val requestOption = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .error(placeholder)
        .placeholder(placeholder)
        .circleCrop()

    Glide.with(context).load(imageUri).apply(requestOption).into(imageView)
}*/

/**
 * For show dialog
 *
 * @param title - title which shown in dialog (application name)
 * @param msg - message which shown in dialog
 * @param positiveText - positive button text
 * @param listener - positive button listener
 * @param negativeText - negative button text
 * @param negativeListener - negative button listener
 * @param icon - drawable icon which shown is dialog
 */
fun Context.showDialog(
    msg: String,
    title: String? = getString(R.string.app_name),
    positiveText: String? = "OK",
    listener: DialogInterface.OnClickListener? = null,
    negativeText: String? = "Cancel",
    negativeListener: DialogInterface.OnClickListener? = null,
    icon: Int? = null
) {
    if (ActBase.dialogShowing) {
        return
    }
    val builder = AlertDialog.Builder(this)
    builder.setTitle(title)
    builder.setMessage(msg)
    builder.setCancelable(false)
    builder.setPositiveButton(positiveText) { dialog, which ->
        ActBase.dialogShowing = false
        listener?.onClick(dialog, which)
    }
    if (negativeListener != null) {
        builder.setNegativeButton(negativeText) { dialog, which ->
            ActBase.dialogShowing = false
            negativeListener.onClick(dialog, which)
        }
    }
    if (icon != null) {
        builder.setIcon(icon)
    }
    val dialog = builder.create()
    dialog.show()
    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED)
    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
    ActBase.dialogShowing = true
}

/**
 * For validate email-id
 *
 * @return email-id is valid or not
 */
fun String.isValidEmail(): Boolean {
    return !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

//fun isGooglePlayServicesAvailable(context: Context): Boolean {
//    val googleApiAvailability = GoogleApiAvailability.getInstance()
//    val status = googleApiAvailability.isGooglePlayServicesAvailable(context)
//    if (ConnectionResult.SUCCESS == status)
//        return true
//    else {
//        if (googleApiAvailability.isUserResolvableError(status))
//            Toast.makeText(
//                context,
//                "Please Install google play services to use this application",
//                Toast.LENGTH_LONG
//            ).show()
//    }
//    return false
//}

/**
 * Extensions for simpler launching of Activities
 * Kotlin DSL function
 */
inline fun <reified T : Any> Activity.launchActivity(
    requestCode: Int = -1,
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {

    val intent = newIntent<T>(this)
    intent.init()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        startActivityForResult(intent, requestCode, options)
    } else {
        startActivityForResult(intent, requestCode)
    }
}

inline fun <reified T : Any> Context.launchActivity(
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {

    val intent = newIntent<T>(this)
    intent.init()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        startActivity(intent, options)
    } else {
        startActivity(intent)
    }
}

inline fun <reified T : Any> newIntent(context: Context): Intent =
    Intent(context, T::class.java)


/**
 * Extensions for stop multiple click
 */
fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}

/**
 * convert object to string
 */
/*fun convertObjectToString(value: Any): String {
    return Gson().toJson(value)
}*/

/**
 * create RequestPart to string
 */
/*fun createPartFromString(partString: String): RequestBody {
    return RequestBody.create(MultipartBody.FORM, partString)
}*/

/**
 * check string null or not with return value
 */
fun checkStringValue(value: String?, blank: String = ""): String {
    if (value.isNullOrEmpty()) {
        return blank
    }
    return value
}

fun checkStringValue(value: String?): Boolean {
    if (value.isNullOrEmpty()) {
        return false
    }
    return true
}

fun amountWithCurrency(amount: String?): String {
    return "${checkStringValue(amount, "0.0")} INR"
}