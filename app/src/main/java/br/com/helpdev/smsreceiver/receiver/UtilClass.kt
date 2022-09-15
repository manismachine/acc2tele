package br.com.helpdev.smsreceiver.receiver

import android.util.Log
import java.net.HttpURLConnection
import java.net.URL


class UtilClass  {

    companion object {
        private val TAG by lazy { UtilClass::class.java.simpleName }

    }

     fun sendGet(result: URL) {

         Log.e("lalala", result.toString())
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