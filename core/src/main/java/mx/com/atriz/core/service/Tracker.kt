package mx.com.atriz.core.service

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.Looper
import androidx.annotation.DrawableRes
import androidx.core.app.ServiceCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import mx.com.atriz.core.ext.getLocationServiceNotification
import mx.com.atriz.core.ext.l
import mx.com.atriz.core.ext.sendLocation
import mx.com.atriz.core.service.Tracker.Settings.ServiceType.FUSED
import mx.com.atriz.core.service.Tracker.Settings.ServiceType.LEGACY
import java.io.Serializable

/**
 * A service that tracks the device's location and sends updates.
 *
 * This service should be used with dependency injection, specifically Koin.
 */
class Tracker(private val settings: Settings) : Service() {

    private val binder = LocationBinder()

    private val fusedClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            try {
                sendLocation(locationResult.locations[0])
            } catch (_: Exception) {
            }
        }
    }

    override fun onBind(p0: Intent?) = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ServiceCompat.startForeground(
                    this,
                    ID,
                    getLocationServiceNotification(
                        settings.title,
                        settings.description,
                        settings.icon
                    ),
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
                )
            } else {
                startForeground(
                    ID,
                    getLocationServiceNotification(
                        settings.title,
                        settings.description,
                        settings.icon
                    )
                )
            }
            startLocationUpdates()
        } catch (e: Exception) {
            l("LocationService Start failed: ${e.message}")
        }

        return START_STICKY
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        when (settings.serviceType) {
            LEGACY -> TODO()
            FUSED -> {
                val locationRequest = LocationRequest
                    .Builder(PRIORITY_HIGH_ACCURACY, settings.updateEvery)
                    .setWaitForAccurateLocation(true)
                    .build()

                fusedClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        l("LocationService removed by force... Restarting")
        val service = PendingIntent.getService(
            applicationContext,
            ID,
            Intent(applicationContext, Tracker::class.java),
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager[AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000] = service
    }

    override fun onDestroy() {
        super.onDestroy()
        l("LocationService destroyed")
        fusedClient.removeLocationUpdates(locationCallback)
        fusedClient.flushLocations()
    }

    /**
     * Binder class for the Tracker service.
     */
    inner class LocationBinder : Binder() {
        /**
         * Gets the Tracker service instance.
         *
         * @return The Tracker service instance.
         */
        fun getService(): Tracker = this@Tracker
    }

    /**
     * Data class containing settings for the Tracker service.
     *
     * @param title The title of the service.
     * @param description The description of the service.
     * @param icon The icon resource ID for the service.
     * @param updateEvery The location update interval in milliseconds.
     * @param serviceType The type of location service to use.
     */
    data class Settings(
        val title: String,
        val description: String,
        @DrawableRes val icon: Int,
        val updateEvery: Long = 5000,
        val serviceType: ServiceType = FUSED
    ) : Serializable {

        /**
         * Enum representing the type of location service.
         */
        enum class ServiceType(val value: Int) {
            LEGACY(0),
            FUSED(1);

            companion object {
                /**
                 * Gets the ServiceType enum value for the given integer value.
                 *
                 * @param value The integer value.
                 * @return The corresponding ServiceType enum value.
                 */
                fun get(value: Int) = values().first { it.value == value }
            }
        }
    }

    companion object {
        const val CHANNEL_ID = "LocationService"
        const val ID = 1010
    }
}
