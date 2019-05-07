package com.example.kotlinpushnotificationpoc

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent){

        var title = "";
        var desc = "";
        intent.extras?.also{
            title = it.getString("EXTRA_TITLE", "")
            desc = it.getString("EXTRA_DESC","")
        }

        var generalTapIntent = Intent(context, ActivityDetail::class.java)
        var pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, generalTapIntent, 0);

        var notificationId = 0;
        var builder = NotificationCompat.Builder(context, "Standard")
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle(title)
            .setContentText(desc)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent) //sets the event to fire when notification is clicked
            .setAutoCancel(true) //Removes notification when user taps it

        with(NotificationManagerCompat.from(context)){
            notify(notificationId, builder.build());
        }
    }
}