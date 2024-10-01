package mx.com.atriz.core.ext

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import mx.com.atriz.core.service.Tracker

@Suppress("DEPRECATION")
fun Context.isLocationServiceRunning(): Boolean {
    val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val runningServices = activityManager.getRunningServices(Integer.MAX_VALUE)
    return runningServices.any {
        it.service.className == Tracker::class.java.name && it.foreground
    }
}

fun Context.startLocationService(
    settings: Tracker.Settings
) {
    val intent = Intent(this, Tracker::class.java)
    intent.putExtra("settings", settings)
    if (!isLocationServiceRunning()) {
        startForegroundService(intent)
    }
}

fun Context.stopLocationService() {
    if (isLocationServiceRunning()) {
        stopService(Intent(this, Tracker::class.java))
    }
}
