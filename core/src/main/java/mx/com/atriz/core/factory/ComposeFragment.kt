package mx.com.atriz.core.factory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import mx.com.atriz.core.theme.AppTheme

abstract class ComposeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            AppTheme {
                Component()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onCreation(savedInstanceState)
        setUpViewListeners()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED, stateMachinery())
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                eventMachinery()
            }
        }
    }

    @Composable
    abstract fun Component()

    open fun onCreation(savedInstanceState: Bundle?) {}

    open fun setUpViewListeners() {}

    open suspend fun stateMachinery(): suspend CoroutineScope.() -> Unit = {}

    open suspend fun eventMachinery() = Unit
}
