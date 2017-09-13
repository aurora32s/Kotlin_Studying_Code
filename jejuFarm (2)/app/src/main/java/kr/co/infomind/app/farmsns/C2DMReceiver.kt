package kr.co.infomind.app.farmsns


import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.PowerManager
import android.os.Vibrator
import android.preference.PreferenceManager
import android.support.v4.content.WakefulBroadcastReceiver
import android.util.Log

class C2DMReceiver : WakefulBroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // TODO Auto-generated method stub

        if (intent.action == "com.google.android.c2dm.intent.REGISTRATION") {
            Log.e("REGISTRATION", "REGISTRATION")
            handleRegistration(context, intent)
        } else if (intent.action == "com.google.android.c2dm.intent.RECEIVE") {


            //			String key =  nullToZero(intent.getStringExtra("key" ));
            //			String msg = nullToZero (intent.getStringExtra("message"));


            c2dm_msg = intent.getStringExtra("msg")
            val parameter = c2dm_msg.split("#".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            var subject = ""
            var content = ""

            if (parameter[0] == "1") {
                subject = "알림"
                content = "게시글에 신규댓글이 추가되었습니다."
            } else if (parameter[0] == "2") {
                subject = "알림"
                content = "친구요청이 들어왔습니다."
            } else if (parameter[0] == "3") {
                subject = "[공지사항]"
                content = "공지사항 확인바랍니다."
            } else if (parameter[0] == "4") {
                subject = "[서리]"
                content = parameter[1]
            } else if (parameter[0] == "5") {
                subject = "[특보]"
                content = parameter[1]
            } else if (parameter[0] == "6") {
                subject = "알림"
                content = "신규게시글이 등록되었습니다."
            } else if (parameter[0] == "7") {
                subject = "알림"
                content = "상담내용이 등록되었습니다."
            } else if (parameter[0] == "8") {
                subject = "알림"
                content = "상담댓글이 등록되었습니다."
            } else {
                subject = "알림"
                content = "새로운 소식을 확인하십시오."
            }


            Log.e("C2DM", "get message")
            val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            val wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP, "TEST_1")
            wakeLock.acquire(10000)

            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(1000)

            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notification = Notification(R.drawable.icon, subject, System.currentTimeMillis())
            notification.flags = Notification.FLAG_AUTO_CANCEL
            val pendingIntent = PendingIntent.getActivity(context, 0, Intent(context, ASNSActivity::class.java), 0)
            notification.setLatestEventInfo(context, subject, content, pendingIntent)
            nm.cancel(1)
            nm.notify(1, notification)
            //Toast.makeText( context, content, 100000 ).show();
            //Toast.makeText( context, content, 400000 ).show();

        }

    }

    private fun handleRegistration(context: Context, intent: Intent) {
        // TODO Auto-generated method stub
        registration_id = intent.getStringExtra("registration_id")

        Log.e("C2DM", "Get the Registration ID From C2DM")
        Log.e("C2DM", "Registration ID : " + registration_id!!)

        if (intent.getStringExtra("error") != null) {
            Log.v("C2DM", "C2DM REGISTRATION : Registration failed," + "should try again later")

        } else if (intent.getStringExtra("unregistered") != null) {
            // 받은 메세지가 unregistered일 경우
            Log.v("C2DM", "C2DM REGISTRATION : unregistration done, " + "new messages from the authorized " + "sender will be rejected")
        } else if (registration_id != null) {
            // 받은 메세지가 unregistered일 경우
            Log.v("C2DM", "Registration ID complete!")
            // Registration ID 저장
            val shrdPref = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = shrdPref.edit()
            editor.putString("registration_id", registration_id)
            editor.commit()
        }
    }

    companion object {

        internal var registration_id: String? = null
        internal var c2dm_msg = ""

        fun nullToZero(value: String?): String {
            var value = value

            if (value == null || value == "") {
                value = "XXX"
            }
            return value
        }
    }

}
