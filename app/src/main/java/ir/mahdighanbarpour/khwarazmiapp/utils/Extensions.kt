package ir.mahdighanbarpour.khwarazmiapp.utils

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Parcelable
import android.view.Window
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.TranslateAnimation
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.textfield.TextInputLayout
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

// Displaying Toasts that have a short duration
fun makeShortToast(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

inline fun <reified T : Parcelable> Intent.getParcelable(key: String): T = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)!!
    else -> getParcelableExtra(key)!!
}

inline fun <reified T : Parcelable> Intent.getParcelableArray(key: String): Array<T>? {
    return when {
        Build.VERSION.SDK_INT >= 33 -> getParcelableArrayExtra(
            key, T::class.java
        )?.mapNotNull { it }?.toTypedArray()

        else -> getParcelableArrayExtra(key)?.mapNotNull { it as? T }?.toTypedArray()
    }
}

fun changeBoxStrokeColor(context: Context, textInputLayout: TextInputLayout, color: Int) {
    textInputLayout.boxStrokeColor = ContextCompat.getColor(context, color)
}

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

fun <T : Any> Single<T>.asyncRequest(): Single<T> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun changeStatusBarColor(
    window: Window, statusBarColor: String, isAppearanceLightStatusBars: Boolean
) {
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = Color.parseColor(statusBarColor)
    WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars =
        isAppearanceLightStatusBars
}

fun translateAnimation(
    fromX: Float, toX: Float, fromY: Float, toY: Float, duration: Long
): TranslateAnimation {
    val anim = TranslateAnimation(fromX, toX, fromY, toY)
    anim.duration = duration

    return anim
}

fun alphaAnimation(fromAlpha: Float, toAlpha: Float, duration: Long): AlphaAnimation {
    val anim = AlphaAnimation(fromAlpha, toAlpha)
    anim.duration = duration

    return anim
}