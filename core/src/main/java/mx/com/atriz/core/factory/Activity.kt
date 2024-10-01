package mx.com.atriz.core.factory

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.content.res.Configuration
import android.location.Location
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import mx.com.atriz.core.entities.Event
import mx.com.atriz.core.receivers.LocationUpdates

abstract class Activity<viewBinding : ViewBinding> : AppCompatActivity() {

    private var _binding: viewBinding? = null
    private lateinit var locationReceiver: LocationUpdates
    open var navController: NavController? = null
    open val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = getViewBinding()
        setContentView(binding.root)
        updateUiMode(resources.configuration)
        setUpNavigation()?.let { navigation ->
            with(supportFragmentManager.findFragmentById(navigation)) {
                (this as NavHostFragment).findNavController().let {
                    this@Activity.navController = it
                }
            }
        }
        onCreation(savedInstanceState)
        setUpViewListeners()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED, stateMachinery())
        }
        locationReceiver = LocationUpdates(::onLocationReceived)
    }

    abstract fun getViewBinding(): viewBinding

    open fun setUpNavigation(): Int? = null

    open fun onCreation(savedInstanceState: Bundle?) {}

    open fun setUpViewListeners() {}

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    open fun getLocationUpdates() {
        val intentFilter = IntentFilter().apply {
            addAction(Event.Location.value)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(locationReceiver, intentFilter, RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(locationReceiver, intentFilter)
        }
    }

    open fun removeLocationUpdates() {
        unregisterReceiver(locationReceiver)
    }

    open fun onLocationReceived(location: Location) {}

    open suspend fun stateMachinery(): suspend CoroutineScope.() -> Unit = {}

    private fun updateUiMode(configuration: Configuration) {
        when (configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> AppCompatDelegate.MODE_NIGHT_NO
            Configuration.UI_MODE_NIGHT_YES -> AppCompatDelegate.MODE_NIGHT_YES
        }
    }
}
