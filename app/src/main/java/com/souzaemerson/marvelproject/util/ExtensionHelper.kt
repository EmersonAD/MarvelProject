package com.souzaemerson.marvelproject.util

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import timber.log.Timber

fun TextInputLayout.setError(context: Context, stringResId: Int?) {
    error = if (stringResId != null) {
        context.getString(stringResId)
    } else null
}

fun Fragment.toast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.timberInfo(tag: String?, message: String) {
    Timber.tag(tag).i(message)
}

fun Fragment.timberError(tag: String?, message: String) {
    Timber.tag(tag).e(message)
}
