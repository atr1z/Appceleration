package mx.com.atriz.core.ext

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import mx.com.atriz.core.entities.Event
import java.util.Locale

fun Context.sendLocation(location: Location) {
    val intent = Intent(Event.Location.value)
    val bundle = Bundle().also {
        it.putDouble("latitude", location.latitude)
        it.putDouble("longitude", location.longitude)
        it.putDouble("altitude", location.altitude)
        it.putFloat("accuracy", location.accuracy)
        it.putFloat("bearing", location.bearing)
        it.putFloat("speed", location.speed)
        it.putLong("time", location.time)
        it.putString("provider", location.provider)
    }
    intent.putExtras(bundle)
    intent.setPackage(packageName)
    sendBroadcast(intent)
}

@Suppress("DEPRECATION")
fun Location.toAddress(maxResults: Int = 1, context: Context): String {
    val builder = StringBuilder()
    val coder = Geocoder(context, Locale.getDefault())
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            coder.getFromLocation(latitude, longitude, maxResults) { addresses ->
                for (i in 0..addresses[0].maxAddressLineIndex) {
                    builder.append(addresses[0].getAddressLine(i)).append("\n")
                }
            }
        } else {
            coder.getFromLocation(this.latitude, longitude, maxResults)?.let { addresses ->
                for (i in 0..addresses[0].maxAddressLineIndex) {
                    builder.append(addresses[0].getAddressLine(i)).append("\n")
                }
            }
        }
    } catch (_: Exception) {
    }

    return builder.toString()
}
