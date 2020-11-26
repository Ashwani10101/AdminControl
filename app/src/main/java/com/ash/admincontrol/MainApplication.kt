package com.ash.admincontrol

import android.app.Application
import com.ash.admincontrol.networking.ConnectivityStateHolder.registerConnectivityBroadcaster

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        registerConnectivityBroadcaster()
    }
}