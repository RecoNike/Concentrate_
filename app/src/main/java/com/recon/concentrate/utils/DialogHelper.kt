package com.recon.concentrate.utils

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

class DialogHelper(private val context: Context) {

    fun showNotificationDialog(
        title: String,
        message: String,
        positiveButtonLabel: String,
        positiveButtonAction: DialogInterface.OnClickListener? = null,
        negativeButtonLabel: String? = null,
        negativeButtonAction: DialogInterface.OnClickListener? = null
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonLabel, positiveButtonAction)
            .setCancelable(false)

        if (negativeButtonLabel != null) {
            builder.setNegativeButton(negativeButtonLabel, negativeButtonAction)
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }
}
