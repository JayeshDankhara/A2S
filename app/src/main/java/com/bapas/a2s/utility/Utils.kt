package com.bapas.a2s.utility

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.*
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.database.Cursor
import android.graphics.*
import android.graphics.BitmapFactory.decodeFile
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.media.MediaScannerConnection
import android.net.ConnectivityManager
import android.net.Uri
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.provider.Settings
import android.text.InputFilter
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import android.util.Patterns
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import com.bapas.a2s.BuildConfig
import com.bapas.a2s.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.GsonBuilder
import java.io.*
import java.lang.reflect.Type
import java.net.HttpURLConnection
import java.net.URL
import java.security.NoSuchAlgorithmException
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


object Utils {


    fun addFragment(
        @IdRes containerViewId: Int, fragment: androidx.fragment.app.Fragment,
        fragmentTag: String, supportFragmentManager: androidx.fragment.app.FragmentManager
    ) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_left,
                R.anim.slide_out_left,
                R.anim.slide_in_right,
                R.anim.slide_out_right
            )
            .addToBackStack(fragmentTag)
            .add(containerViewId, fragment, fragmentTag)
            .commit()
    }

    fun drawableToBitmap(drawable: Drawable): Bitmap? {
        var bitmap: Bitmap? = null
        if (drawable is BitmapDrawable) {
            val bitmapDrawable = drawable
            if (bitmapDrawable.bitmap != null) {
                return bitmapDrawable.bitmap
            }
        }
        bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            Bitmap.createBitmap(
                1,
                1,
                Bitmap.Config.ARGB_8888
            ) // Single color bitmap will be created of 1x1 pixel
        } else {
            Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
        }
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    fun withoutBackStack(
        manager: androidx.fragment.app.FragmentManager,
        fragment: androidx.fragment.app.Fragment, frameId: Int = 0
    ) {
        val transaction = manager.beginTransaction()
//        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_right)
        transaction.replace(frameId, fragment, fragment.javaClass.simpleName)
        transaction.commit()
    }

    var filter = InputFilter { source, start, end, _, _, _ ->
        for (i in start until end) {
            if (!Character.isLetterOrDigit(source[i]) &&
                source[i].toString() != "_" &&
                source[i].toString() != "-"
            ) {
                return@InputFilter ""
            }
        }
        null
    }


    fun loadImgGlide(ctx: Context, load: String, into: ImageView) {
        Glide
            .with(ctx)
            .load(load)
            .placeholder(R.drawable.progress_animation).timeout(60000)
            .into(into)
    }

    fun loadImgGlide2(ctx: Context, load: String, into: ImageView) {
        Glide
            .with(ctx)
            .load(load)
            .into(into)
    }

    fun getPath(uri: Uri?, act: Activity): String? {
        // just some safety built in
        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor = act.managedQuery(uri, projection, null, null, null)
        if (cursor != null) {
            val column_index: Int = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            val path: String = cursor.getString(column_index)
            cursor.close()
            return path
        }
        // this is our fallback here
        return uri.path
    }

    fun replaceFragmentToActivity(
        manager: androidx.fragment.app.FragmentManager,
        fragment: androidx.fragment.app.Fragment, frameId: Int = 0
    ) {

        val transaction = manager.beginTransaction()
//        Debug.e("name", fragment.javaClass.simpleName)
        transaction.addToBackStack(fragment.javaClass.simpleName)
        transaction.replace(frameId, fragment, fragment.javaClass.simpleName)
        transaction.commit()

    }

    @Throws(IOException::class)
    fun getThumbnail(uri: Uri, context: Context): Bitmap? {
        val openInputStream: InputStream = context.contentResolver.openInputStream(uri)!!
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        options.inDither = true
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        BitmapFactory.decodeStream(openInputStream, null as Rect?, options)
        if (openInputStream != null) {
            openInputStream.close()
        }
        if (options.outWidth == -1 || options.outHeight == -1) {
            return null
        }
        val max = Math.max(options.outHeight, options.outWidth).toDouble()
        val d =
            if (max > 3510.0) max / 3510.0 else 1.0
        val options2 = BitmapFactory.Options()
        options2.inSampleSize = getPowerOfTwoForSampleRatio(d)

        options2.inDither = true
        options2.inPreferredConfig = Bitmap.Config.ARGB_8888
        val openInputStream2: InputStream = context.contentResolver.openInputStream(uri)!!
        val decodeStream = BitmapFactory.decodeStream(openInputStream2, null as Rect?, options2)
        if (openInputStream2 != null) {
            openInputStream2.close()
        }
        return decodeStream
    }

    fun getPowerOfTwoForSampleRatio(d: Double): Int {
        val highestOneBit = Integer.highestOneBit(Math.floor(d).toInt())
        return if (highestOneBit == 0) {
            1
        } else highestOneBit
    }

    fun bitmapToUrl(bitmap: Bitmap, c: Context): Uri {
        val wrapper = ContextWrapper(c)
        var file = wrapper.getDir("profile", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            // Compress the bitmap and save in jpg format
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        // Return the saved bitmap uri
        return FileProvider.getUriForFile(c, BuildConfig.APPLICATION_ID + ".provider", file)
    }


    fun createDialog(layout: Int, c: Context): Dialog {

        val materialDialog = Dialog(c)
        materialDialog.setContentView(layout)
        materialDialog.setCancelable(false)
        setWindowManagerCustom(materialDialog)
        materialDialog.show()
        return materialDialog
    }


    private fun setWindowManagerCustom(materialDialog: Dialog) {
        val layOutParams = WindowManager.LayoutParams()
        layOutParams.copyFrom(materialDialog.window!!.attributes)
    }

    fun alertDialog(context: Context, title: String, msg: String, cancel: Boolean, ok: Boolean) {

        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setMessage(msg)
            .setCancelable(false)

        if (ok) {
            dialogBuilder.setPositiveButton("Ok") { dialog, _ ->

                dialog.dismiss()
            }
        }
        if (cancel) {
            dialogBuilder.setNegativeButton("cancel") { dialog, _ ->
                dialog.cancel()
            }
        }

        val alert = dialogBuilder.create()
        alert.setTitle(title)
        alert.show()
    }

    fun updateResources(context: Context, language: String): Boolean {

        val locale: Locale = Locale(language)
//                if (Prefs.getString(PreferenceField.LANGUAGE, Constant.ENGLISH) == Constant.SPANISH_LATIN) {
//                    Locale("es", "419")
//                } else {
//                    Locale(Prefs.getString(PreferenceField.LANGUAGE, Constant.ENGLISH))
//                }
        Locale.setDefault(Locale(language))

        val resources = context.resources


        val configuration = resources.configuration
        configuration.locale = locale
        if (Build.VERSION.SDK_INT >= 21) {
            configuration.setLayoutDirection(locale)
        }
        resources.updateConfiguration(configuration, resources.displayMetrics)

        return true
    }

    fun setTimeFormat(time: String): String {
        val df = SimpleDateFormat("HH:mm")
        val date: Date = df.parse(time)
        return SimpleDateFormat("hh:mm a").format(date)
    }

    fun getHoursAndMinutes(totalMinutes: Int): String {
        var minutes = Integer.toString(totalMinutes % 60)
        minutes = if (minutes.length == 1) "0$minutes" else minutes
        return (totalMinutes / 60).toString() + " : " + minutes
    }

    fun IsValidEmailAddress(hex: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        pattern = Pattern.compile(EMAIL_PATTERN)
        matcher = pattern.matcher(hex)
        return matcher.matches()
    }

    fun isInternetConnected(activity: Context?): Boolean {
        activity?.let {
            return isInternetConnectedCheck(activity)
        }
        return true
    }

    fun isInternetConnectedForBackgroundService(activity: Context?): Boolean {
        activity?.let {
            return if (isInternetConnectedCheck(activity)) {
                true
            } else {
                Handler(Looper.getMainLooper()).post {
                    showToast(
                        activity,
                        activity.getString(R.string.internet_string)
                    )
                }
                false
            }
        }
        return true
    }

    fun isInternetConnectedCheck(mContext: Context?): Boolean {
        var outcome = false

        try {
            if (mContext != null) {
                val cm = mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

                val networkInfos = cm.allNetworkInfo

                for (tempNetworkInfo in networkInfos) {

                    if (tempNetworkInfo.isConnected) {
                        outcome = true
                        break
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return outcome
    }

    fun isWifiConnected(context: Context): Boolean {
        val connManager: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)

        return mWifi!!.isConnected
    }

    fun isLayoutRight(context: Context): Boolean {
        return context.getString(R.string.isLayoutRight) == "true"
    }

    fun changeEditTextGravity(context: Context, editText: EditText) {
        if (Utils.isLayoutRight(context)) {
            editText.gravity = Gravity.END
        } else {
            editText.gravity = Gravity.START
        }
    }

    fun chnageLanguage(context: Activity, language: String) {
        val res = context.resources
// Change locale settings in the app.
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.setLocale(Locale(language.lowercase(Locale.getDefault()))) // API 17+ only.
// Use conf.locale = new Locale(...) if targeting lower versions
        res.updateConfiguration(conf, dm)
    }

    fun writeToFile(data: String, context: Context) {

        val outputStreamWriter: OutputStreamWriter =
            OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE))
        outputStreamWriter.write(data)
        outputStreamWriter.close()
    }


    fun getOutputMediaFileUri(activity: Activity): Uri {

        return FileProvider.getUriForFile(
            activity,
            BuildConfig.APPLICATION_ID + ".provider",
            getOutputMediaFile(activity)!!
        )

    }

    fun getOutputMediaFile(activity: Activity): File? {

        // External sdcard location
        val mediaStorageDir = File(
            Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "." + activity.getString(R.string.app_name)
        )

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null
            }
        }

        // Create a media file name
        val timeStamp = SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale("en")
        ).format(Date())
        val mediaFile: File
        mediaFile = File(
            mediaStorageDir.path + File.separator
                    + "IMG_" + timeStamp + ".jpg"
        )

        return mediaFile
    }

    fun initHomeFrag(manager: FragmentManager, fragment: Fragment, frameId: Int = 0) {

        val transaction = manager.beginTransaction()
        transaction.replace(frameId, fragment, fragment::class.java.simpleName)
        transaction.commit()

    }

    fun checkForThetaConnectionDialogue(context: Context): Boolean {
        val wifiManager =
            context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        val ssid = wifiInfo.ssid
//        ssid.toLowerCase().indexOf("theta") != -1
        return ssid.lowercase(Locale.getDefault()).contains("theta")

    }

    fun cancleAlarm(context: Context, ALARM_ACTION: String) {
        val am = context.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val i = Intent(ALARM_ACTION)
        val pi = PendingIntent.getBroadcast(context.applicationContext, 0, i, 0)
        am.cancel(pi)
    }

    fun showToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    fun getDeviceWidth(context: Activity): Int {
        val displayMetrics = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels

        return width
    }

    fun getDeviceHeight(context: Activity): Int {
        val displayMetrics = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels

        return height
    }

    fun SetWindowManagerCustom(materialDialog: Dialog) {
        val layOutParams = WindowManager.LayoutParams()
        layOutParams.copyFrom(materialDialog.window!!.attributes)
    }

    fun isLocationEnabled(context: Context): Boolean {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gps_enabled = false
        var network_enabled = false
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
            Logger.e("Exception"+ex.message.toString())
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {
            Logger.e("Exception"+ex.message.toString())
        }
        return gps_enabled || network_enabled
    }

    fun convertDpToPixel(dp: Float): Int {
        val metrics = Resources.getSystem().displayMetrics
        val px = dp * (metrics.densityDpi / 160f)
        return Math.round(px)
    }

    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

    fun getSecret(email: String?, password: String?): String {

        val text = email?.reversed() + password?.reversed()

        return md5(text)
    }

    fun isEmailValid(email: String): Boolean {

        val isEmailValid =
            !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()

        Logger.e("isEmailValid"+isEmailValid.toString())

        return isEmailValid

    }

    fun galleryAddPicBroadCast(activity: Activity, photoFile: File) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val contentUri = Uri.fromFile(photoFile)
        mediaScanIntent.data = contentUri
        activity.sendBroadcast(mediaScanIntent)
    }

    fun md5(s: String): String {
        val MD5 = "MD5"
        try {
            // Create MD5 Hash
            val digest = java.security.MessageDigest
                .getInstance(MD5)
            digest.update(s.toByteArray())
            val messageDigest = digest.digest()

            // Create Hex String
            val hexString = StringBuilder()
            for (aMessageDigest in messageDigest) {
                var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
                while (h.length < 2)
                    h = "0" + h
                hexString.append(h)
            }
            return hexString.toString()

        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        return ""
    }

    fun isStringValid(text: String?): Boolean {

        return !(text == null || text.isEmpty() || text == "null" || text.isBlank())
    }

    @Throws(IOException::class)
    fun getSavedImagePicturePath(context: Context, bitmap: Bitmap): String {

        val root = Environment.getExternalStorageDirectory().toString()
        val mydir = File(root + "/" + context.getString(R.string.app_name))
        mydir.mkdirs()

        val fname = "Image-" + Calendar.getInstance().timeInMillis + ".png"
        val filepath = root + "/" + context.getString(R.string.app_name) + "/" + fname


        val file = File(mydir, fname)
        if (file.exists()) file.delete()


        val out = FileOutputStream(file)

        bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
        out.flush()
        out.close()

        return filepath

    }

//    this function will create dynamic temUri

    @Throws(Exception::class)
    fun createTemporaryFile(context: Context): File {
        var tempDir = Environment.getExternalStorageDirectory()


        tempDir = File(tempDir.absolutePath + "/." + context.getString(R.string.app_name))
        if (!tempDir.exists()) {
            tempDir.mkdir()
        }
        return File.createTempFile("Image-", ".png", tempDir)
    }

    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    fun createImageFile(c: Context): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = c.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    fun showAlert(c: Context, msString: String) {
        AlertDialog.Builder(c)
            .setMessage(msString)
            .setTitle(R.string.app_name)
            .setPositiveButton(android.R.string.ok) { dialog, which ->
                dialog.dismiss()

            }
            .show()
    }

    fun sendCamaraIntent(baseContext: Activity, imageCaptureCode: Int): Uri {
        var photo: File? = null
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            // place where to store camera taken picture
            photo = createImageFile(baseContext)
            photo.delete()
        } catch (e: Exception) {
            showToast(baseContext, "alert_check_sd_card")
        }
        val mImageUri = Uri.fromFile(photo)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri)
        baseContext.startActivityForResult(intent, imageCaptureCode)
        return mImageUri
    }

    fun getThumbnil(file: File): Bitmap {
        val bitmap = decodeFile(file.absolutePath, null)
        val bitmapOptions = BitmapFactory.Options()
        bitmapOptions.inJustDecodeBounds =
            true // obtain the size of the image, without loading it in memory
        decodeFile(file.absolutePath, bitmapOptions)
        val desiredWidth = bitmap.width / 2
        val desiredHeight = bitmap.height / 2
        val widthScale = bitmapOptions.outWidth.toFloat() / desiredWidth
        val heightScale = bitmapOptions.outHeight.toFloat() / desiredHeight
        val scale = Math.min(widthScale, heightScale)
        var sampleSize = 1
        while (sampleSize < scale) {
            sampleSize *= 2
        }
        bitmapOptions.inSampleSize = sampleSize // this value must be a power of 2,
        bitmapOptions.inJustDecodeBounds = false // now we want to load the image
        bitmap.recycle()
        return decodeFile(file.absolutePath, bitmapOptions)
    }

    fun getFormettedDate(
        context: Context, strDate: String,
        defaultDateFormet: String, newDateFormet: String
    ): String {
        try {
            val locale = Locale("en")
            Locale.setDefault(locale)
            val config = Configuration()
            config.locale = locale
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            val originalFormat = SimpleDateFormat(defaultDateFormet, Locale.getDefault())
            val targetFormat = SimpleDateFormat(newDateFormet)
            val date = originalFormat.parse(strDate)
            return targetFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return strDate
    }

    inline fun <reified T> parseArray(json: String, typeToken: Type): T {
        val gson = GsonBuilder().create()
        return gson.fromJson(json, typeToken)
    }

    fun getFormatedNumber(number: String): String {
        try {
            return if (number.isNotEmpty()) {
                val `val` = java.lang.Double.parseDouble(number)
                NumberFormat.getNumberInstance(Locale.US).format(`val`)
            } else {
                ""
            }
        } catch (e: NumberFormatException) {
            Logger.e("NumberFormatExp"+e.message.toString())
        }
        return ""
    }

    fun getFormettedDateSpanish(
        context: Context, strDate: String?,
        defaultDateFormet: String, newDateFormet: String
    ): String {

        var date_obj: Date? = null
        val fmt = SimpleDateFormat(defaultDateFormet)

        var finaldate = ""
        try {
            date_obj = fmt.parse(strDate)
            val destDf = SimpleDateFormat(newDateFormet, Locale("es"))
            finaldate = destDf.format(date_obj)
//            finaldate = finaldate.replace("AM", "am").replace("PM", "pm")
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return finaldate
    }

    fun convertDateFirstLaterCaps(
        context: Context, date: String,
        defaultDateFormat: String, newDateFormat: String
    ): CharSequence? {
        var allCaps = ""

        val date = getFormettedDateSpanish(
            context, date, defaultDateFormat,
            newDateFormat
        )
        if (date.isNotEmpty()) {
            allCaps = date.uppercase(Locale.getDefault())[0] + date.substring(1, date.length)
        }


        return allCaps
    }


    fun hideSoftKeyboard(activity: Activity) {
        try {
            val view = activity.currentFocus
            if (view != null) {
                val imm =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        } catch (e: Exception) {
        }
    }

    fun hideKeyboardonWhitespace(viewgroup: ViewGroup, activity: Activity) {
        var view: View
        if (viewgroup is ViewGroup) {
            for (i in 0..viewgroup.childCount) {
                if (viewgroup.getChildAt(i) != null) {
                    view = viewgroup.getChildAt(i)
                    if (view !is EditText) {
                        view.setOnTouchListener { v, m ->
                            hideSoftKeyboard(activity)
                            false
                        }
                    }
                }
            }
        }
    }

    var neededPermission = android.Manifest.permission.CAMERA

    fun checkPermission(context: Context): Boolean {
        if (ContextCompat.checkSelfPermission(
                context,
                neededPermission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    fun requestPermission(context: Activity, reqCode: Int) {
        ActivityCompat.requestPermissions(context, arrayOf(neededPermission), reqCode)
    }

    fun isPermissionOfCamera(activity: FragmentActivity): Boolean {
        val cam = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
        return cam == PackageManager.PERMISSION_GRANTED
    }

//    fun checkPermissionCamera(activity: FragmentActivity) {
//        PermissionHandler.Builder().setContext(activity)
//                .setAllPermission(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE))
//                .setPermissionCallback(object : PermissionInterface {
//                    override fun permissionGranted() {
//                    }
//
//                    override fun permissionDenied() {
//                        Toast.makeText(activity, "Provide Permission", Toast.LENGTH_SHORT).show()
//
//                        activity.supportFragmentManager.findFragmentById(R.id.main_container)?.let {
//                            // the fragment exists
//
//                        }
//                    }
//
//                    override fun permissionShowDialog() {
//
//
//                    }
//                }).build()
//    }

    fun showKeyboard(activity: Activity, editText: EditText?) {
        val imm: InputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInputFromWindow(
            editText?.applicationWindowToken,
            InputMethodManager.SHOW_FORCED, 0
        )
    }

    fun showKeyboardforce(ctx: Context, view: View) {
        val r = Runnable {
            if (view.requestFocus()) {
                val imm =
                    ctx.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(
                    view,
                    InputMethodManager.SHOW_IMPLICIT
                )
            }
        }
        view.post(r)
    }

    fun loadImageUsingGlide(
        context: Context,
        imgUrl: Any?,
        imageView: ImageView,
        placeholder: Int
    ) {
//        val requestOptions =  RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).
        Glide.with(context)
            .load(imgUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(placeholder)
            .placeholder(placeholder)
            .dontTransform()
            .override(Constants.PREFERRED_IMAGE_SIZE_PREVIEW, Constants.PREFERRED_IMAGE_SIZE_FULL)
            .into(imageView)

    }

    fun loadDataInWebView(newsDescription: String, webView: WebView) {

        val style = "<head><style type=\"text/css\">\n" +
                "@font-face {\n" +
                "    font-family: MyFont;\n" +
                "    src: url(\"file:///android_asset/fonts/Muli-Light.ttf\")\n" +
                "}\n" +
                "body {\n" +
                "    font-family: MyFont;\n" +
                "    font-size: medium;\n" +
                "    text-align: justify;\n" +
                "}\n" +
                "img { width : 100% ;}" +
                "</style></head>"


        val detailWebViewText = "<html>$style<body>" + newsDescription
            .replace("width=", "width = \"100%\"  dummy1 =").replace(
                "height=",
                "dummy2="
            ) + "</body></Html>"

        val detailWebViewText1 = removeSquareBrackets(detailWebViewText)


        webView.setBackgroundColor(Color.TRANSPARENT)
        webView.loadDataWithBaseURL(
            "file:///android_asset/",
            detailWebViewText1,
            "text/html",
            "UTF-8",
            ""
        )

    }

    fun removeAllFragment(f: FragmentActivity) {
        for (fragment in f.supportFragmentManager.fragments) {
            f.supportFragmentManager.beginTransaction().remove(fragment).commit()
        }
    }

    private fun removeSquareBrackets(s: String): String {

        val startSquareBracketIndex: Int = s.indexOf("[")

        return if (startSquareBracketIndex == -1) {
            s
        } else {
            val text = s.substring(s.indexOf("["), s.indexOf("]") + 1)
            removeSquareBrackets(s.replace("" + text, ""))
        }
    }

    fun appInstalledOrNot(context: Context, uri: String): Boolean {
        val pm = context.packageManager
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
        }

        return false
    }

    fun setDecimalPointLimit(price: Double): String {
        val formatter = NumberFormat.getNumberInstance()

        formatter.minimumFractionDigits = 2
        formatter.maximumFractionDigits = 2
        return formatter.format(price)
    }

    fun buttonAnimation(c: Context, animation: Int, textView: View) {
        val a: Animation = AnimationUtils.loadAnimation(c, animation)
        a.reset()
        textView.clearAnimation()
        textView.startAnimation(a)
    }

    fun isWifiOrMobileDataConnected(context: Context): Boolean {
        if (isInternetConnectedCheck(context)) {
            val connMgr =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            val wifi = connMgr?.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            val mobile = connMgr?.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)

            val wifiManager: WifiManager =
                context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val info: WifiInfo = wifiManager.connectionInfo
            val ssid = info.ssid

            if (wifi?.isConnected == true && (!ssid.lowercase(Locale.getDefault())
                    .contains("theta") && wifi.extraInfo.lowercase(Locale.getDefault())?.contains(
                    "theta"
                ) == false)
            ) {
                return true
            } else if (mobile?.isConnected == true) {
                return true
            }
        }
        return false
    }

    fun isServiceRunningInBackground(context: Context, serviceClass: Class<*>): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    fun isServiceRunningInForeground(context: Context, serviceClass: Class<*>): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                if (service.foreground) {
                    return true
                }
            }
        }
        return false
    }

    fun saveImageInApp(activity: Activity, bitmap: Bitmap) {
        val cw = ContextWrapper(activity)
        val directory = cw.getDir("profile", Context.MODE_PRIVATE)
        if (!directory.exists()) {
            directory.mkdir()
        }
        val mypath = File(directory, "thumbnail.jpg")

        var fos: FileOutputStream? = null
        fos = FileOutputStream(mypath)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos.close()
        fos.flush()
    }

    fun saveImage(activity: Activity, finalBitmap: Bitmap): String {
        val root = Environment.getExternalStorageDirectory().toString()
        val myDir = File("$root/" + activity.getString(R.string.app_name))
        myDir.mkdirs()
        val fname = "Image-" + Calendar.getInstance().timeInMillis + ".jpeg"

        val file = File(myDir, fname)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
            Utils.showToast(activity, "Download Successful")
            galleryAddPicBroadCast(activity, file)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return file.absolutePath
    }

    @Throws(IOException::class)
    fun saveImage1(bitmap: Bitmap, name: String, context: Context) {
        val fos: OutputStream? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver: ContentResolver = context.contentResolver
            val contentValues = ContentValues()
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "$name.jpg")
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + context.getString(R.string.app_name))
            val imageUri =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            resolver.openOutputStream(Objects.requireNonNull(imageUri)!!)
        } else {
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES+ File.separator + context.getString(R.string.app_name))
                    .toString()
            val image = File(imagesDir, "$name.jpg")
            FileOutputStream(image)
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        Objects.requireNonNull(fos)?.close()
        showToast(context, "Download Image Successfully")
    }


    fun saveImage11(myBitmap: Bitmap, context: Context): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
//        val wallpaperDirectory = File(Environment.getExternalStorageDirectory().toString() + "IMAGE_DIRECTORY")
        val wallpaperDirectory = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()
        )
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            Log.d("dirrrrrr", "" + wallpaperDirectory.mkdirs())
            wallpaperDirectory.mkdirs()
        }


        MediaScannerConnection.scanFile(
            context, arrayOf(wallpaperDirectory.toString()), null
        ) { path, uri ->
            Logger.e("ExternalStorage Scanned $path:")
            Logger.e("ExternalStorage-> uri=$uri")
        }



        try {
            val f = File(
                wallpaperDirectory, Calendar.getInstance()
                    .timeInMillis.toString() + ".jpg"
            )
//            f.createNewFile() //give read write permission
            f.mkdirs() //give read write permission
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(context, arrayOf(f.path), arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.absolutePath)
            return f.absolutePath
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        return ""
    }


    fun isDataNotEmpty(editText: EditText? = null, textView: TextView? = null): Boolean {

        var isValidate: Boolean = false

        editText?.let {
            isValidate = editText.text.takeIf { isStringValid(it.toString()) }?.let { true }
                ?: let { false }
        } ?: let {
            isValidate = textView?.text.takeIf { isStringValid(it.toString()) }?.let { true }
                ?: let { false }
        }

        return isValidate

    }

    fun saveImage(activity: Activity, finalBitmap: Bitmap, fileId: String?): String {
        val cw = ContextWrapper(activity)
        val directory = cw.getDir("profile", Context.MODE_PRIVATE)

        if (!directory.exists()) {
            directory.mkdir()
        }

        directory.mkdirs()

        val uniqueFileId: String = if (fileId?.isNotEmpty() == true) {
            Calendar.getInstance().timeInMillis.toString() + "_" + fileId.substring(
                fileId.lastIndexOf("/") + 1,
                fileId.length
            )
        } else {
            "IMG_" + Calendar.getInstance().timeInMillis + ".JPG"
        }
        val file = File(directory, uniqueFileId)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()


        } catch (e: Exception) {
            e.printStackTrace()
        }

        return file.absolutePath
    }

    fun getBitmapFromPath(path: String): Bitmap? {
        val bmOptions = BitmapFactory.Options()
        return decodeFile(path, bmOptions)
    }

    fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            val url = URL(src)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connectTimeout = 3000
            connection.connect()
            val input: InputStream = connection.inputStream
            val bitmap = BitmapFactory.decodeStream(input)

            bitmap
        } catch (e: IOException) {
            Logger.e("getBitmapFromURL:"+e.message.toString())
            null
        }
    }


    fun checkIfImageFitPanorama(path: String): Boolean {
        val width = getBitmapFromPath(path)?.width
        val height = getBitmapFromPath(path)?.height

        return (width != null && height != null && width / height == 2 / 1)
    }

    fun getDatDirFolderPath(activity: Context): String? {
        val cw = ContextWrapper(activity)
        val directory = cw.getDir("profile", Context.MODE_PRIVATE)

        if (directory.exists()) {
            return directory.absolutePath
        }

        return null
    }

    @SuppressLint("HardwareIds")
    fun getAndroidId(activity: Context): String? {
        var aid: String? = ""
        try {
            aid = Settings.Secure.getString(
                activity.contentResolver,
                Settings.Secure.ANDROID_ID
            )

            if (aid == null) {
                aid = "No DeviceId"
            } else if (aid.isEmpty()) {
                aid = "No DeviceId"
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return aid
    }

    fun getAppVersion(context: Context): Int {
        val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        return pInfo.versionCode
    }

    fun getVersionName(context: Context): String {

        var versionName = context.packageManager
            .getPackageInfo(context.packageName, 0).versionName

        return versionName
    }

    fun convertPixelsToDp(px: Float, context: Context): Float {
        return px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun getFormattedDouble(value: String?): String {

        return if (value != null && value.isNotEmpty() && value != ".")
            String.format(
                "%.2f", value.toDouble()
            )
        else
            String.format("%.2f", 0.00)
    }

    fun getDecimalFormatValue(amt: String): String {
        return DecimalFormat("#,##,##0.00").format(amt.toDouble())

    }
}