package com.example.exercises
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    private lateinit var airPlaneReceiver: AirplaneModeChangedReceiver
    private lateinit var wifiReceiver: WifiModeChangedReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Coroutines.main()
        //Coroutines.main2()
        //Coroutines.main3()
        //Coroutines.main4()
        //Coroutines.main5()
        //Coroutines.main6()
        //Coroutines.main7()
        //Coroutines.main8()
        //Coroutines.main9()
        //Coroutines.main10()
        //Coroutines.main11()
        //Coroutines.main12()
        //Coroutines.main13()
        //Coroutines.main14()
        //Coroutines.main15()
        //Coroutines.main16()
        //Coroutines.main17()
        //Coroutines.main18()
        //Coroutines.main19()
        //Coroutines.main20()
        //Coroutines.main21()

        initAirPlaneReceiver()
        initWifiReceiver()
    }

    override fun onStart() {
        super.onStart()
        initWifiReceiver()
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(airPlaneReceiver)
        unregisterReceiver(wifiReceiver)
    }

    private fun initAirPlaneReceiver() {
        airPlaneReceiver = AirplaneModeChangedReceiver()

        IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also {
            registerReceiver(airPlaneReceiver, it)
        }
    }

    private fun initWifiReceiver() {
        wifiReceiver = WifiModeChangedReceiver()

        IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION).also {
            registerReceiver(wifiReceiver, it)
        }
    }

}