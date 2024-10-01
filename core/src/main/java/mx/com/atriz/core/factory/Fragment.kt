package mx.com.atriz.core.factory

import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentFilter
import android.content.res.Configuration
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity.RECEIVER_NOT_EXPORTED
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import mx.com.atriz.core.entities.Event
import mx.com.atriz.core.interfaces.AppInterface
import mx.com.atriz.core.receivers.LocationUpdates

abstract class Fragment<viewBinding : ViewBinding, navigation : AppInterface> :
    Fragment() {

    private var _binding: viewBinding? = null
    private lateinit var locationReceiver: LocationUpdates
    val binding get() = _binding!!
    var i: navigation? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = getViewBinding(container, false)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBack()
                }
            }
        getMenu()?.let { menuRes ->
            requireActivity().addMenuProvider(
                object : MenuProvider {
                    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                        menuInflater.inflate(menuRes, menu)
                    }

                    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                        onMenuItemClicked(menuItem)
                        return false
                    }
                },
                viewLifecycleOwner,
                Lifecycle.State.RESUMED
            )
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        onCreation(arguments)
        setUp(resources.configuration)
        setUpViewObservers()
        setUpViewListeners()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED, stateMachinery())
        }
        locationReceiver = LocationUpdates(::onLocationReceived)
        return binding.root
    }

    abstract fun getViewBinding(
        container: ViewGroup? = null,
        atParent: Boolean = false
    ): viewBinding

    open fun getMenu(): Int? = null
    open fun onCreation(args: Bundle? = null) {}
    open fun setUp(config: Configuration) {}
    open fun setUpViewListeners() {}
    open fun setUpViewObservers() {}
    open suspend fun stateMachinery(): suspend CoroutineScope.() -> Unit = {}
    open fun onBack() {}
    open fun allowLocationUpdates() = false

    open fun onMenuItemClicked(item: MenuItem) {}

    open fun onArgumentsReady(argument: Bundle) {}

    @Suppress("UNCHECKED_CAST")
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AppInterface) {
            i = context as navigation
        }
    }

    override fun onResume() {
        super.onResume()
        arguments?.let { onArgumentsReady(it) }
        if (allowLocationUpdates()) {
            getLocationUpdates()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        if (allowLocationUpdates()) {
            removeLocationUpdates()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        updateUiMode(newConfig)
        setUp(newConfig)
    }

    private fun updateUiMode(configuration: Configuration) {
        when (configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> AppCompatDelegate.MODE_NIGHT_NO
            Configuration.UI_MODE_NIGHT_YES -> AppCompatDelegate.MODE_NIGHT_YES
        }
    }

    open fun onLocationReceived(location: Location) {}

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    open fun getLocationUpdates() {
        val intentFilter = IntentFilter().apply {
            addAction(Event.Location.value)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireActivity().registerReceiver(
                locationReceiver,
                intentFilter,
                RECEIVER_NOT_EXPORTED
            )
        } else {
            requireActivity().registerReceiver(locationReceiver, intentFilter)
        }
    }

    open fun removeLocationUpdates() {
        requireActivity().unregisterReceiver(locationReceiver)
    }

    /*fun setUpResponsiveForm(
        config: Configuration,
        fields: List<Form.Field>?,
        recyclerView: RecyclerView
    ) {
        fields?.let {
            val isTablet = requireActivity().isDevicePhone()
            val isPortrait = config.isScreenPortrait()
            val responsiveColumns: Int
            val dateResponsiveSpan: Int
            val fieldResponseSpan: Int
            if (isPortrait) {
                if (isTablet) {
                    responsiveColumns = 4
                    dateResponsiveSpan = 2
                    fieldResponseSpan = 2
                } else {
                    responsiveColumns = 2
                    dateResponsiveSpan = 2
                    fieldResponseSpan = 2
                }
            } else {
                if (isTablet) {
                    responsiveColumns = 8
                    dateResponsiveSpan = 4
                    fieldResponseSpan = 4
                } else {
                    responsiveColumns = 4
                    dateResponsiveSpan = 2
                    fieldResponseSpan = 2
                }
            }
            val manager = GridLayoutManager(requireContext(), responsiveColumns)
            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (it[position].type) {
                        "date" -> dateResponsiveSpan
                        else -> fieldResponseSpan
                    }
                }
            }
            recyclerView.apply {
                layoutManager = manager
            }
        }
    }*/
}
