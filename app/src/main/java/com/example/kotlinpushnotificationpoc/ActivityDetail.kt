package com.example.kotlinpushnotificationpoc

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class ActivityDetail : AppCompatActivity() {

    var CHANNEL_ID = "Standard"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
    }

    fun triggerPush(view: View){
        var generalTapIntent = Intent(this, MainActivity::class.java)
        var pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, generalTapIntent, 0);

        var notificationId = 0;
        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Instant Message")
            .setContentText("Back to Main")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent) //sets the event to fire when notification is clicked
            .setAutoCancel(true) //Removes notification when user taps it

        with(NotificationManagerCompat.from(this)){
            notify(notificationId, builder.build());
        }
    }
}
