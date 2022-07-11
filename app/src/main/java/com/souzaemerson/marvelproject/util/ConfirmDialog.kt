package com.souzaemerson.marvelproject.util

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.souzaemerson.marvelproject.R

class ConfirmDialog(
    private val title: String,
    private val message: String,
    private val textYes: String,
    private val textNo: String
) : DialogFragment() {

    private var yesListener: (() -> Unit)? = null
    fun setListener(listener: () -> Unit ){ yesListener = listener }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(textYes){ _, _ ->
                yesListener?.let {
                    it()
                }
            }
            .setNegativeButton(textNo){ dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .create()

}