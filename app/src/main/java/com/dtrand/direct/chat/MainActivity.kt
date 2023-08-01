package com.dtrand.direct.chat


import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipboardManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import java.net.URLEncoder


class MainActivity : Activity() {

    val maxLimit = 17

    /** @param massage Send the prefilled massage for whatsapp
     * */
    class Data(var number : Long, var displayNumber : String, var massage : String? = null, var countryCode : String = "091")

    private lateinit var dialer : View
    private lateinit var numberTxt : TextView
    private lateinit var selectCCBtn : TextView

    val data = Data(0, "")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        //Init views
        dialer = findViewById(R.id.dialerView)
        numberTxt = findViewById(R.id.display_no_text)
        selectCCBtn = findViewById(R.id.countryCodeBtn)


        initEvents()

    }

    override fun onResume() {
        super.onResume()
        initDisplays()
    }

    private fun openWhatsapp(number : String, massage : String?) {

        var textToShare = "https://wa.me/${data.countryCode.toInt()}${number}"

        massage?.let {
            textToShare += "?text=${URLEncoder.encode(massage, "UTF-8")}"
        }

        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(textToShare)
            startActivity(intent)
        } catch (e : PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun displayNumber() {
        if (data.number != 0L) {
            //If has an valid number
            data.displayNumber = data.number.toString()
            numberTxt.text = data.displayNumber
        } else numberTxt.text = getString(R.string.number_placeholder)
    }



    @SuppressLint("SetTextI18n")
    private fun initDisplays() {
        val cc = Utils.getCountryCode(this)
        selectCCBtn.text = "+${cc}".trim()
        numberTxt.text = data.displayNumber
        data.countryCode = cc


        if (BuildConfig.DEBUG) {
            // Do the debug stuff
            numberTxt.text = "9306300035"
        }
    }
    private fun initEvents() {

        selectCCBtn.setOnClickListener {
            DialogUtils.selectCountryCode(
                data.countryCode,
                this,
                object : DialogUtils.InputListener {
                    override fun onUserInput(string: String) {
                        Utils.saveCountryCode(this@MainActivity, string)
                        initDisplays()
                    }
                })
        }

        numberTxt.setOnLongClickListener {
            Utils.copyText(data.number.toString(), this)
            Toast.makeText(this@MainActivity, getString(R.string.copied), Toast.LENGTH_SHORT).show()
            true
        }


        findViewById<View>(R.id.whatsChatBtn).setOnClickListener {
            if (numberTxt.text.isNullOrBlank()) {
                //Is null number
                Utils.showToast(this@MainActivity, getString(R.string.enter_valid_no))
            }
            else { openWhatsapp(numberTxt.text.toString(), data.massage) }

        }

        findViewById<View>(R.id.msg_btn).setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("smsto:" + Uri.encode("${data.number}"))
                //   intent.putExtra("sms_body", " ")      // To send pre-filed  text msg
                startActivity(intent)
            }catch (e : Exception) {
                Utils.showToast(this, "No message app found!")
            }
        }

        //setup_ing the callback of dialer
        Utils.initDialer(dialer, object : ClickCallBack {
            override fun onNumberSelect(digit: Byte) {
                try {
                    val txt = "${data.number}$digit"

                    if (txt.length <= maxLimit) {
                        data.number = txt.toLong()
                        displayNumber()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            override fun onPasteBtn() {

                val clipboard = (getSystemService(CLIPBOARD_SERVICE)) as? ClipboardManager
                val textToPaste = clipboard?.primaryClip?.getItemAt(0)?.text ?: Toast.makeText(
                    this@MainActivity,
                    getString(R.string.copy_request),
                    Toast.LENGTH_SHORT
                ).show()

                textToPaste.let {
                    try {
                        //Removing the country code if its contains
                        val number = it.toString().trim()
                            .replace("-", "")
                            .replace(" ", "")
                            .replace("+${data.countryCode.toInt()}", "")
                            .replace("(", "")
                            .replace(")", "")


                        data.number = number.toLong()
                        displayNumber()

                    } catch (e: Exception) {
                        Utils.logDebug(e)

                        Utils.showToast(this@MainActivity, "${getString(R.string.paste_valid_number)} ''$textToPaste'' ")
                    }
                }

            }

            override fun onBackSpaceBtn() {
                data.number = data.number / 10
                displayNumber()
            }

            override fun onBackSpaceLongPressed() {
                data.number = 0
                displayNumber()
            }
        })

    }



    interface ClickCallBack{
        fun onNumberSelect(digit: Byte)
        fun onPasteBtn()
        fun onBackSpaceBtn()
        fun onBackSpaceLongPressed()
    }

}