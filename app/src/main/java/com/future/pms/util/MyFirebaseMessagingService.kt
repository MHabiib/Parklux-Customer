package com.future.pms.util

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.future.pms.util.Constants.Companion.FCM_PARKING_ZONE
import com.future.pms.util.Constants.Companion.FCM_TOTAL_PRICE
import com.future.pms.util.Constants.Companion.MY_FIREBASE_MESSAGING
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        if (remoteMessage?.data != null) {
            val intent = Intent(MY_FIREBASE_MESSAGING)
            intent.putExtra(FCM_PARKING_ZONE, remoteMessage.data[FCM_PARKING_ZONE])
            intent.putExtra(FCM_TOTAL_PRICE, remoteMessage.data[FCM_TOTAL_PRICE])

            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        }
    }
}
