package mx.com.atriz.core.factory

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import mx.com.atriz.core.entities.Event

abstract class Application : Application() {

    private var internetConnection = false
    private var internetWifiConnection = false
    private var internetCellularConnection = false

    @SuppressLint("MissingPermission")
    fun addInternetCallbacks() {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(networkCallbacks)
    }

    private val networkCallbacks = object : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network) {
            super.onLost(network)
            internetConnection = false
            internetWifiConnection = false
            internetCellularConnection = false
        }

        @SuppressLint("InlinedApi")
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)

            val i = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                internetCellularConnection = i
            }
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                internetWifiConnection = i
            }

            if (internetConnection != (internetWifiConnection || internetCellularConnection)) {
                internetConnection = (internetWifiConnection || internetCellularConnection)
                val intent = Intent(Event.Network.value)
                intent.putExtra(
                    Event.Internet.value,
                    mx.com.atriz.core.entities.Network(
                        internetConnection,
                        internetWifiConnection,
                        internetCellularConnection
                    )
                )
                baseContext.sendBroadcast(intent)
            }
        }
    }
}
