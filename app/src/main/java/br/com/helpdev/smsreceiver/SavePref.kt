package br.com.helpdev.smsreceiver

import android.content.Context
import android.content.SharedPreferences

class SavePref() {

    var savepref: SharedPreferences? = null
    var mContext: Context? = null

    constructor (context: Context?) : this() {
        mContext = context
        savepref = mContext!!.getSharedPreferences("savepref", 0)
    }


    fun setChatid(encIMEIString: String?) {
        val editor = savepref!!.edit()
        editor.putString("Chatid", encIMEIString)
        editor.apply()
    }

    fun getChatid(): String? {
        return savepref!!.getString("Chatid", "nochatid")
    }


}