package mx.com.atriz.core.ext

import android.os.Build
import android.os.Bundle
import java.io.Serializable

@Suppress("DEPRECATION")
inline fun <reified T : Serializable> Bundle.getArgument(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.getSerializable(key, T::class.java)
    } else {
        this.getSerializable(key) as T
    }
}
