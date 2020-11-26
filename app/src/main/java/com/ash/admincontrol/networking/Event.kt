package com.ash.admincontrol.networking

sealed class Event {

    class ConnectivityEvent(val isConnected: Boolean) : Event()
}