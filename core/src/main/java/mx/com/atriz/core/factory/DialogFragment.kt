package mx.com.atriz.core.factory

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class DialogFragment<viewBinding : ViewBinding> :
    BottomSheetDialogFragment() {
    private var _binding: viewBinding? = null
    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = getViewBinding()
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        onCreation(arguments)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp(resources.configuration)
        setUpViewObservers()
        setUpViewListeners()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED, stateMachinery())
        }
    }

    abstract fun getViewBinding(
        container: ViewGroup? = null,
        atParent: Boolean = false
    ): viewBinding

    open fun onCreation(args: Bundle? = null) {}
    open fun setUp(config: Configuration) {}
    open fun setUpViewListeners() {}
    open fun setUpViewObservers() {}
    open suspend fun stateMachinery(): suspend CoroutineScope.() -> Unit = {}
    open fun onBack() {}
    open fun onArgumentsReady(argument: Bundle) {}

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
}
