package com.dtrand.direct.chat

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.view.View
import android.widget.Toast
import com.jackandphantom.carouselrecyclerview.CarouselRecyclerview
import com.dtrand.direct.chat.DialogUtils.Companion.getDialog


class Utils {
    companion object {

        private val massages = ArrayList<String>()

        init {
            //
        //  saved massages here.
        }

        fun showMSGSDialog(context: Context) {
            getDialog(context, R.layout.select_msg_dialog).apply {

               val recy = findViewById<CarouselRecyclerview>(R.id.carouselRecyclerview)
               recy.adapter = ListAdapter(context, massages)
               recy.set3DItem(true)
               recy.setIsScrollingEnabled(true)

            }
        }

        fun  showToast(context: Context, text: String) {
            Toast.makeText(context,text , Toast.LENGTH_SHORT).show()
        }

        //Providing saved country code
        fun getCountryCode(context: Context): String {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences("user_preferences", MODE_PRIVATE)
            return sharedPreferences.getString("countryCode", "091")!!
        }
        //Providing saved country code
        fun saveCountryCode(context: Context, countryCode : String) {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences("user_preferences", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("countryCode", countryCode)
            editor.apply()
        }

        fun copyText(text : String, context: Context) {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(text, text)
            clipboard.setPrimaryClip(clip)
        }






        fun logDebug(e: Exception) {
            if (BuildConfig.DEBUG) e.printStackTrace()
        }

        //koi:whatsapp without no save

        fun initDialer(v : View, callBack: MainActivity.ClickCallBack) {

            v.findViewById<View>(R.id.paste_btn).setOnClickListener {
                callBack.onPasteBtn()
            }

            v.findViewById<View>(R.id.backspace_btn).setOnClickListener {
                callBack.onBackSpaceBtn()
            }
            v.findViewById<View>(R.id.backspace_btn).setOnLongClickListener {
                callBack.onBackSpaceLongPressed()
                true
            }



            v.findViewById<View>(R.id.dial0).setOnClickListener {
                callBack.onNumberSelect(0)
            }
            v.findViewById<View>(R.id.dial1).setOnClickListener {
                callBack.onNumberSelect(1)
            }
            v.findViewById<View>(R.id.dial2).setOnClickListener {
                callBack.onNumberSelect(2)
            }
            v.findViewById<View>(R.id.dial3).setOnClickListener {
                callBack.onNumberSelect(3)
            }
            v.findViewById<View>(R.id.dial4).setOnClickListener {
                callBack.onNumberSelect(4)
            }
            v.findViewById<View>(R.id.dial5).setOnClickListener {
                callBack.onNumberSelect(5)
            }
            v.findViewById<View>(R.id.dial6).setOnClickListener {
                callBack.onNumberSelect(6)
            }
            v.findViewById<View>(R.id.dial7).setOnClickListener {
                callBack.onNumberSelect(7)
            }
            v.findViewById<View>(R.id.dial8).setOnClickListener {
                callBack.onNumberSelect(8)
            }
            v.findViewById<View>(R.id.dial9).setOnClickListener {
                callBack.onNumberSelect(9)
            }
        }

    }
}