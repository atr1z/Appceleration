package mx.com.atriz.core.entities

import android.Manifest
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import mx.com.atriz.core.entities.Permission.Type.BACKGROUND_LOCATION
import mx.com.atriz.core.entities.Permission.Type.CALLS
import mx.com.atriz.core.entities.Permission.Type.CAMERA
import mx.com.atriz.core.entities.Permission.Type.LOCATION
import mx.com.atriz.core.entities.Permission.Type.MICROPHONE

data class Permission(
    @StringRes val title: Int,
    @StringRes val explanation: Int,
    @DrawableRes val background: Int? = null,
    val policyLink: String? = null,
    val type: Type
) {
    enum class Type {
        LOCATION,
        BACKGROUND_LOCATION,
        CAMERA,
        MICROPHONE,
        CALLS
    }

    fun getPermissionList(): Array<String> {
        return when (this.type) {
            LOCATION -> arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )

            BACKGROUND_LOCATION -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            } else {
                arrayOf()
            }

            CAMERA -> arrayOf(Manifest.permission.CAMERA)
            MICROPHONE -> arrayOf(Manifest.permission.RECORD_AUDIO)
            CALLS -> arrayOf(Manifest.permission.CALL_PHONE)
        }
    }
}
