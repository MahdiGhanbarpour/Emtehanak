package ir.mahdighanbarpour.emtehanak.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.SimpleColorFilter
import com.airbnb.lottie.model.KeyPath
import com.airbnb.lottie.value.LottieValueCallback
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ir.mahdighanbarpour.emtehanak.R
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

// Displaying Toasts that have a short duration
fun makeShortToast(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

// Get parcelable data from intent
inline fun <reified T : Parcelable> Intent.getParcelable(key: String): T = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)!!
    else -> getParcelableExtra(key)!!
}

inline fun <reified T : Parcelable> Bundle.getParcelableCustom(key: String): T = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelable(key, T::class.java)!!
    else -> getParcelable(key)!!
}

// Get parcelable array data from intent
inline fun <reified T : Parcelable> Intent.getParcelableArray(key: String): Array<T>? {
    return when {
        Build.VERSION.SDK_INT >= 33 -> getParcelableArrayExtra(
            key, T::class.java
        )?.mapNotNull { it }?.toTypedArray()

        else -> getParcelableArrayExtra(key)?.mapNotNull { it as? T }?.toTypedArray()
    }
}

// Changing the box stroke color
fun changeBoxStrokeColor(context: Context, textInputLayout: TextInputLayout, color: Int) {
    textInputLayout.boxStrokeColor = ContextCompat.getColor(context, color)
}

// Dialing the number
fun makeCall(context: Context, number: String) {
    // Dial the number
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:$number")
    context.startActivity(intent)
}

// Checking the user's internet connection
fun isInternetAvailable(context: Context): Boolean {
    var result = false
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        connectivityManager.run {
            connectivityManager.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }

            }
        }
    }

    return result
}

// Async request to the server
fun <T : Any> Single<T>.asyncRequest(): Single<T> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

// Changing the status bar color
fun changeStatusBarColor(
    window: Window, statusBarColor: String, isAppearanceLightStatusBars: Boolean
) {
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = Color.parseColor(statusBarColor)
    WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars =
        isAppearanceLightStatusBars
}

// Creating the translate animation
fun translateAnimation(
    fromX: Float, toX: Float, fromY: Float, toY: Float, duration: Long
): TranslateAnimation {
    val anim = TranslateAnimation(fromX, toX, fromY, toY)
    anim.duration = duration

    return anim
}

// Creating the alpha animation
fun alphaAnimation(fromAlpha: Float, toAlpha: Float, duration: Long): AlphaAnimation {
    val anim = AlphaAnimation(fromAlpha, toAlpha)
    anim.duration = duration

    return anim
}

// Hiding the keyboard
fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

// Changing the LottieAnimationView layers color
fun LottieAnimationView.changeLayersColor(
    @ColorRes colorRes: Int
) {
    val color = ContextCompat.getColor(context, colorRes)
    val filter = SimpleColorFilter(color)
    val keyPath = KeyPath("**")
    val callback: LottieValueCallback<ColorFilter> = LottieValueCallback(filter)

    addValueCallback(keyPath, LottieProperty.COLOR_FILTER, callback)
}

// Getting the file from the uri
fun getFileFromUri(context: Context, uri: Uri): File? {
    val fileName = uri.lastPathSegment?.split("/")?.lastOrNull() ?: return null
    val tempFile = File(context.cacheDir, fileName)
    try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(tempFile)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
    } catch (e: Exception) {
        Log.v("testLog", e.message.toString())
    }
    return tempFile
}

// Load image with retry
fun loadImageWithRetry(root: View, imageView: ImageView, url: String, maxRetries: Int) {
    var retryCount = 0
    val handler = Handler(Looper.getMainLooper())

    // Load image
    fun loadImage() {
        // Set options
        val options = RequestOptions().centerInside().placeholder(R.drawable.img_loading)
            .error(R.drawable.img_error)

        // Check if the context is valid
        if (isValidContextForGlide(imageView.context)) {
            // Load image
            Glide.with(imageView.context).load(url).apply(options)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        // Retry if the image failed to load
                        if (retryCount < maxRetries) {
                            retryCount++
                            handler.postDelayed({ loadImage() }, 1000)  // Retry after 1 second
                        } else {
                            // Show error message
                            Snackbar.make(
                                root, "خطا در دریافت تصویر ضمیمه سوال", Snackbar.LENGTH_INDEFINITE
                            ).setAction("تلاش مجدد") {
                                loadImageWithRetry(root, imageView, url, maxRetries)
                            }.show()
                        }
                        return true
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        // Image loaded successfully
                        return false
                    }
                }).into(imageView)
        }
    }

    loadImage()
}

fun isValidContextForGlide(context: Context?): Boolean {
    // Check if the context is valid
    if (context == null) {
        return false
    }

    // Check if the context is an activity
    if (context is Activity) {
        if (context.isDestroyed || context.isFinishing) {
            return false
        }
    }
    return true
}