package com.citcall.citcallautoreaddemo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
//import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class MyBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val state: String = intent?.getStringExtra(TelephonyManager.EXTRA_STATE).toString()

        if (state == TelephonyManager.EXTRA_STATE_RINGING) {
            val caller: String = intent?.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER).toString()
            if (caller != "null" && caller.isNotEmpty()) {
                //Toast.makeText(context, "Incoming Call from $caller", Toast.LENGTH_LONG).show()
                val lastFourDigits = caller.substring(caller.length - 4)
                val intent1 = Intent("miscall").apply {
                    putExtra("last_four_digit",lastFourDigits)
                }
                if (context != null) {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent1)
                }
            }
        }
    }
}
