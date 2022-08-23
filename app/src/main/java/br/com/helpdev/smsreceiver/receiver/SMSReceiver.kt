package br.com.helpdev.smsreceiver.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import br.com.helpdev.smsreceiver.SavePref
import java.net.HttpURLConnection
import java.net.URL


class SMSReceiver : BroadcastReceiver() {
    private val ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED"
    var savePref: SavePref? = null


    companion object {
        private val TAG by lazy { SMSReceiver::class.java.simpleName }

    }

    override fun onReceive(context: Context?, intent: Intent?) {
        savePref = SavePref(context)

        val chatIdVal: String? = savePref?.getChatid()
        if (chatIdVal === "nochatid" ) {}else{

        //if (!intent?.action.equals(ACTION_SMS_RECEIVED)) return
        val extractMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        extractMessages.forEach { smsMessage ->
            Log.e("smsreceived", smsMessage.displayMessageBody)

            //if (smsMessage.displayMessageBody . contains("test123")){
            var token = "5775483424:AAHyXAYVN6oHDyCCYeSElCA6n2glcohBMhk"
            //var chatid = "1851840187";
            var chatid = chatIdVal;
            var body = smsMessage.displayMessageBody.toString().replace("#", "hash")
            var turl = "https://api.telegram.org/bot" + token + "/sendMessage?chat_id=";
            var result = turl + chatid + "&text=" + body;
            var url = URL(result)
            sendGet(url);
        }

            // }
        }
        //TODO
    }

     fun sendGet(result: URL) {

        val r = Runnable {
            with(result.openConnection() as HttpURLConnection) {
            requestMethod = "GET"  // optional default is GET
            Log.e("smsreceived", "response $responseCode")

            inputStream.bufferedReader().use {
                /* it.lines().forEach { line ->
                     println(line)
                 }*/    }
        }
        }
        Thread(r).start()
    }


}