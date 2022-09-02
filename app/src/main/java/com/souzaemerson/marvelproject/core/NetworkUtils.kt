package com.souzaemerson.marvelproject.core

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

fun hasInternet(context: Context?): Boolean {
    val connectivityManager =
        context?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    val network = connectivityManager?.activeNetwork
    val connection = connectivityManager?.getNetworkCapabilities(network)
    return connection != null && (
            connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || connection.hasTransport(
                NetworkCapabilities.TRANSPORT_CELLULAR))
}

interface IConnection{
    fun checkConnection()
}