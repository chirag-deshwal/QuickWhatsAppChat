package com.dtrand.direct.chat

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

class DialogUtils {


    interface InputListener {
        fun onUserInput(string: String)
    }

    companion object {


        fun selectCountryCode(defaultCC : String, context: Context, callback : InputListener) {
            getDialog(context, R.layout.select_contry_code_dialog).apply {

                //Set the default values
                findViewById<EditText>(R.id.cc_edittext).apply {
                    setText(defaultCC)
                    requestFocus()
                }

                findViewById<View>(R.id.selectedCC_doneBtn).setOnClickListener {
                    findViewById<EditText>(R.id.cc_edittext).let {
                        callback.onUserInput(it.text.toString())
                        dismiss()
                    }
                }

                show()
            }
        }

        fun getDialog(context: Context, layoutID : Int) : Dialog {
            val dialog = Dialog(context, R.style.bottomDialogStyle)
            dialog.setContentView(layoutID)

            dialog.window?.let {
                it.setGravity(Gravity.BOTTOM)
                it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }

            return dialog
        }

    }

}