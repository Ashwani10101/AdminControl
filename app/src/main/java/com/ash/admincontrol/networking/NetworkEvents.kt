package com.ash.admincontrol.networking

import androidx.lifecycle.LiveData
import com.ash.admincontrol.networking.Event

/**
 * This liveData enabling network connectivity monitoring
 * @see ConnectivityStateHolder to get current connection state
 */
object NetworkEvents : LiveData<Event>() {

    internal fun notify(event: Event) {
        postValue(event)
    }

}