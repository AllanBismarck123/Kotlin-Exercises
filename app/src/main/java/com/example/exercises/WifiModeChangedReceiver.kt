package com.example.exercises

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.widget.Toast


class WifiModeChangedReceiver: BroadcastReceiver() {
   override fun onReceive(context: Context?, intent: Intent?) {

       val wifiStateExtra =
           intent?.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN)
               ?: return
       when (wifiStateExtra) {
           WifiManager.WIFI_STATE_ENABLED -> Toast.makeText(
               context,
               "Wifi Mode Enabled",
               Toast.LENGTH_SHORT
           ).show()
           WifiManager.WIFI_STATE_DISABLED -> Toast.makeText(
               context,
               "Wifi Mode Disabled",
               Toast.LENGTH_SHORT
           ).show()
       }
   }
}