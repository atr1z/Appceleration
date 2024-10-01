package mx.com.atriz.core.interfaces

import androidx.annotation.IdRes
import androidx.navigation.NavDirections
import mx.com.atriz.core.entities.Permission

interface AppInterface {

    fun goTo(@IdRes fragment: Int) {}

    fun goTo(options: NavDirections) {}

    fun popUp() {}

    fun popBack() {}

    fun onPermissionDenied(permission: Permission.Type) {}

    fun onPermissionGranted(permission: Permission.Type) {}
}
