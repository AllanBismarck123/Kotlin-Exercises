package com.example.exercises
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.widget.Toast

class NetworkChecker(private val connectivityManager: ConnectivityManager?) {

     fun hasInternet(): Boolean {
        val network: Network? = connectivityManager?.activeNetwork ?: return false
        val capabilites: NetworkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilites.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
               capabilites.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
               capabilites.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
    }
}