package com.example.kotlinpushnotificationpoc

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var CHANNEL_ID = "Standard";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Create notification channel and register it
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

        //Should probably be moved to the Alarm Receiver class so that it always has the channel generated. For purposes
        //of demo this is fine.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun triggerPush(view: View){
        generatePush("Instant Message",
            txt_notification.text.toString())
    }

    fun generatePush(pPushTitle: String, pNotificationText: String){
        var generalTapIntent = Intent(this, ActivityDetail::class.java)
        var pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, generalTapIntent, 0);

        var notificationId = 0;
        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(pPushTitle)
            .setContentText(pNotificationText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent) //sets the event to fire when notification is clicked
            .setAutoCancel(true) //Removes notification when user taps it

        with(NotificationManagerCompat.from(this)){
            notify(notificationId, builder.build());
        }
    }

    fun startDetails(view: View){
        with(Intent(this, ActivityDetail::class.java)){
            startActivity(this);
        }
    }

    fun triggerAlarm(view: View){
        var alarmMgr: AlarmManager? = null
        lateinit var pendingIntent: PendingIntent

        var intent = Intent(getApplicationContext(), AlarmReceiver::class.java)
        intent.putExtra("EXTRA_TITLE",txt_notification.text.toString());
        intent.putExtra("EXTRA_DESC","Alarm Push: ${SystemClock.elapsedRealtime()}");
        //Fire a broadcast which is picked up by the alarmReceiver class which catches the broadcast and triggers the notification.
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0)
        alarmMgr = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmMgr?.set(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + 9000, //9 seconds from now
            pendingIntent
        )
    }
}
