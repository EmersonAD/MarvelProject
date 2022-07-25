package com.souzaemerson.marvelproject.util

import android.text.Editable
import android.text.TextWatcher

class Watcher(val onTextChanged: (String) -> Unit): TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        onTextChanged(s.toString())
    }

    override fun afterTextChanged(s: Editable?) {}
}