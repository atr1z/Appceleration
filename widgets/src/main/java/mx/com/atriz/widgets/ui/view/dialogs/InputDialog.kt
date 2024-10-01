package mx.com.atriz.widgets.ui.view.dialogs

import android.os.Bundle
import android.view.ViewGroup
import mx.com.atriz.core.factory.DialogFragment
import mx.com.atriz.widgets.R
import mx.com.atriz.widgets.databinding.DialogInputBinding

class InputDialog(
    private val title: Any,
    private val defaultValue: Any? = null,
    private val onPositiveAction: (value: String) -> Unit,
    private val onNegativeAction: () -> Unit
) : DialogFragment<DialogInputBinding>() {
    override fun getViewBinding(container: ViewGroup?, atParent: Boolean): DialogInputBinding =
        DialogInputBinding.inflate(layoutInflater, container, false)

    override fun onCreation(args: Bundle?) {
        super.onCreation(args)
        binding.dialogTitle.text = when (title) {
            is Int -> getString(title)
            is String -> title
            else -> ""
        }
        defaultValue?.let { default ->
            when (default) {
                is String -> binding.dialogInput.setText(default)
                is Int,
                is Double,
                is Float,
                is Long -> binding.dialogInput.setText(default.toString())
            }
        }
        binding.dialogOk.text = getString(R.string.ok)
        binding.dialogCancel.text = getString(R.string.cancel)
    }

    override fun setUpViewListeners() {
        super.setUpViewListeners()
        binding.dialogOk.setOnClickListener {
            dismiss()
            try {
                onPositiveAction(binding.dialogInput.text.toString())
            } catch (_: Exception) {
            }
        }
        binding.dialogCancel.setOnClickListener {
            dismiss()
            onNegativeAction()
        }
    }
}
