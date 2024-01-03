package ir.mahdighanbarpour.khwarazmiapp.utils

import android.content.Context
import android.widget.Toast

// Displaying Toasts that have a short duration
fun makeShortToast(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}