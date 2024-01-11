package ir.mahdighanbarpour.khwarazmiapp.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Parcelable
import android.widget.Toast

// Displaying Toasts that have a short duration
fun makeShortToast(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

inline fun <reified T : Parcelable> Intent.getParcelable(key: String): T = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)!!
    else -> getParcelableExtra(key)!!
}

fun makeCall(context: Context,number:String) {
    // Dial the number
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:$number")
    context.startActivity(intent)
}