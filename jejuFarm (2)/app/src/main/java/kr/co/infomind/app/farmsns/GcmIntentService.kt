package kr.co.infomind.app.farmsns

import android.app.IntentService
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.app.NotificationCompat
import android.util.Log

import com.google.android.gms.gcm.GoogleCloudMessaging

/**
 * Created by icelancer on 15. 2. 21..
 */
class GcmIntentService : IntentService("GcmIntentService") {
    private var mNotificationManager: NotificationManager? = null
    internal var builder: NotificationCompat.Builder? = null

    override fun onHandleIntent(intent: Intent?) {
        val extras = intent!!.extras
        val gcm = GoogleCloudMessaging.getInstance(this)
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        val messageType = gcm.getMessageType(intent)

        if (!extras.isEmpty) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE == messageType) {
                // This loop represents the service doing some work.
                for (i in 0..4) {
                    Log.i(TAG, "Working... " + (i + 1)
                            + "/5 @ " + SystemClock.elapsedRealtime())
                    try {
                        Thread.sleep(5000)
                    } catch (e: InterruptedException) {
                    }

                }
                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime())
                // Post notification of received message.
                sendNotification("Received-----: " + extras.toString())
                Log.i(TAG, "Received: " + extras.toString())
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        //C2DMReceiver.completeWakefulIntent(intent)
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private fun sendNotification(msg: String) {
        mNotificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val contentIntent = PendingIntent.getActivity(this, 0,
                Intent(this, ASNSActivity::class.java), 0)

        val mBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("GCM Notification")
                .setStyle(NotificationCompat.BigTextStyle()
                        .bigText(msg))
                .setContentText(msg)

        mBuilder.setContentIntent(contentIntent)
        mNotificationManager!!.notify(NOTIFICATION_ID, mBuilder.build())
    }

    companion object {
        val TAG = "icelancer"
        val NOTIFICATION_ID = 1
    }
}//        Used to name the worker thread, important only for debugging.
