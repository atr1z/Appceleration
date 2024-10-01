package mx.com.atriz.core.ext

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.POWER_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/**
 * Checks if the device has location permissions granted.
 *
 * @param requestPermissions A lambda function to be executed if location permissions are not granted.
 * @param permissionsGranted A lambda function to be executed if location permissions are granted.
 */
fun Context.checkLocationPermissions(
    requestPermissions: () -> Unit = {},
    permissionsGranted: () -> Unit = {}
) {
    val fineLocation = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
    val coarseLocation = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    if (fineLocation || coarseLocation) {
        permissionsGranted()
    } else {
        requestPermissions()
    }
}

/**
 * Checks if the device has background location permissions granted.
 * This method requires API level 29 (Android 10) or higher.
 *
 * @param requestPermissions A lambda function to be executed if background location permissions are not granted.
 * @param permissionsGranted A lambda function to be executed if background location permissions are granted.
 */
@RequiresApi(Build.VERSION_CODES.Q)
fun Context.checkBackgroundLocationPermissions(
    requestPermissions: () -> Unit = {},
    permissionsGranted: () -> Unit = {}
) {
    val fineLocation = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
    val coarseLocation = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
    val backgroundLocation = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    if (
        (fineLocation || coarseLocation) || backgroundLocation
    ) {
        permissionsGranted()
    } else {
        requestPermissions()
    }
}

/**
 * Checks if the device has camera permission granted.
 *
 * @param requestPermissions A lambda function to be executed if camera permission is not granted.
 * @param permissionsGranted A lambda function to be executed if camera permission is granted.
 */
fun Context.checkCameraPermission(
    requestPermissions: () -> Unit = {},
    permissionsGranted: () -> Unit = {}
) {
    val camera = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
    if (camera) {
        permissionsGranted()
    } else {
        requestPermissions()
    }
}

/**
 * Checks if the device has call phone permission granted.
 *
 * @param requestPermissions A lambda function to be executed if call phone permission is not granted.
 * @param permissionsGranted A lambda function to be executed if call phone permission is granted.
 */
fun Context.checkCallPermission(
    requestPermissions: () -> Unit = {},
    permissionsGranted: () -> Unit = {}
) {
    val camera = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.CALL_PHONE
    ) == PackageManager.PERMISSION_GRANTED
    if (camera) {
        permissionsGranted()
    } else {
        requestPermissions()
    }
}

/**
 * Requests that the system not put the app in battery optimization mode.
 * This method requires the user to grant permission.
 */
@SuppressLint("BatteryLife")
fun Context.requestNoBatteryOptimizations() {
    val intent: Intent = Intent()
    val packageName = packageName
    (getSystemService(POWER_SERVICE) as PowerManager?)?.let { pm ->
        if (!pm.isIgnoringBatteryOptimizations(packageName)) {
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
            intent.setData(Uri.parse("package:$packageName"))
            startActivity(intent)
        }
    }
}

/**
 * Opens a URL in the device's default browser.
 *
 * @param url The URL to open.
 */
fun AppCompatActivity.openBrowser(url: String) {
    try {
        val webpage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    } catch (e: Exception) {
        l("Could not parse $url -> ${e.message}")
    }
}

/**
 * Opens a URL in the device's default browser.
 *
 * @param url The URL to open.
 */
fun Fragment.openBrowser(url: String) {
    try {
        val webpage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        }
    } catch (e: Exception) {
        l("Could not parse $url -> ${e.message}")
    }
}

/**
 * Checks if the current device orientation is portrait.
 *
 * @param config The current device configuration.
 * @return true if the current device orientation is portrait, false otherwise.
 */
fun isScreenPortrait(config: Configuration): Boolean =
    config.orientation == Configuration.ORIENTATION_PORTRAIT
