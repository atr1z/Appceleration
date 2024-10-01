package mx.com.atriz.core.ext

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import mx.com.atriz.core.service.Tracker

fun Context.getLocationServiceNotification(
    title: String,
    description: String,
    @DrawableRes icon: Int
): Notification {
    val notificationBuilder = NotificationCompat.Builder(this, Tracker.CHANNEL_ID)
        .setSmallIcon(icon)
        .setContentTitle(title)
        .setContentText(description)
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .setCategory(NotificationCompat.CATEGORY_SERVICE)
        .setStyle(NotificationCompat.DecoratedCustomViewStyle())
        .setOngoing(true)
        .setOnlyAlertOnce(true)
        .setDefaults(NotificationCompat.DEFAULT_ALL)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        notificationBuilder.setForegroundServiceBehavior(Notification.FOREGROUND_SERVICE_IMMEDIATE)
    }

    return notificationBuilder.build()
}

fun Context.createNotificationsChannel() {
    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val mChannel = NotificationChannel(Tracker.CHANNEL_ID, "Location Service", importance)
    val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(mChannel)
}
