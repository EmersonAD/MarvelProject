package com.souzaemerson.marvelproject.util

import android.content.Context
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.setError(context: Context, stringResId: Int?) {
    error = if (stringResId != null) {
        context.getString(stringResId)
    } else null
}

fun Context.toast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}