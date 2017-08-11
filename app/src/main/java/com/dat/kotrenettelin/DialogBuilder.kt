package com.dat.kotrenettelin

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.LinearLayout

/**
 * Created by Dat on 8/11/2017.
 */
class DialogBuilder(context: Context) {
    private var builder: AlertDialog.Builder
    private lateinit var body: LinearLayout

    init {
        builder = AlertDialog.Builder(context)
        initBody(context)
    }

    private fun initBody(context: Context) {
        body = LinearLayout(context)
        body.orientation = LinearLayout.VERTICAL
        val padding = context.resources.getDimension(R.dimen.dialog_padding).toInt()
        body.setPadding(padding, padding, padding, padding)
        body.isFocusable = true
        body.isFocusableInTouchMode = true
        body.isClickable = true
        body.requestFocus()
    }

    fun setCancelable(cancelable: Boolean): DialogBuilder {
        builder.setCancelable(cancelable)
        return this
    }

    fun addCustomView(view: View): DialogBuilder {
        body.addView(view)
        return this
    }

    fun setNegativeButton(name: String?, listener: DialogInterface.OnClickListener?): DialogBuilder {
        builder.setNegativeButton(name, listener)
        return this
    }

    fun setPositiveButton(name: String?, listener: DialogInterface.OnClickListener?): DialogBuilder {
        builder.setPositiveButton(name, listener)
        return this
    }

    fun build(): AlertDialog {
        if (body.childCount > 0) {
            builder.setView(body)
        }

        val builderDialog = builder.create()
        builderDialog.setOnDismissListener { dialog ->
            body.requestFocus()
        }
        return builderDialog
    }

}