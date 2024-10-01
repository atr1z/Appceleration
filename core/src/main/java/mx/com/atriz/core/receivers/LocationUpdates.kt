package mx.com.atriz.core.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import mx.com.atriz.core.entities.Event

class LocationUpdates(private val listener: (Location) -> Unit) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (Event.get(intent.action ?: "")) {
            Event.Location -> {
                intent.extras?.let { extras ->
                    val location = Location(extras.getString("provider", "fused")).also {
                        it.latitude = extras.getDouble("latitude", 0.0)
                        it.longitude = extras.getDouble("longitude", 0.0)
                        it.altitude = extras.getDouble("altitude", 0.0)
                        it.time = extras.getLong("time", 0)
                        it.accuracy = extras.getFloat("accuracy", 0f)
                        it.speed = extras.getFloat("speed", 0f)
                        it.bearing = extras.getFloat("bearing", 0f)
                    }
                    listener(location)
                }
            }
            else -> {}
        }
    }
}
