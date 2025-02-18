package mx.com.atriz.core.factory

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import mx.com.atriz.core.interfaces.AppInterface

abstract class Fragment<viewBinding : ViewBinding, navigation : AppInterface> :
    Fragment() {

    private var _binding: viewBinding? = null
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
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                locationUpdates()
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                eventMachinery()
            }
        }
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
    open suspend fun eventMachinery(): Unit = Unit
    open suspend fun locationUpdates(): Unit = Unit
    open fun onBack() {}

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
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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
