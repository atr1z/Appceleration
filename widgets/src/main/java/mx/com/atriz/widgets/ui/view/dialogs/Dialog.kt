package mx.com.atriz.widgets.ui.view.dialogs

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import mx.com.atriz.core.factory.DialogFragment
import mx.com.atriz.widgets.databinding.DialogModalBinding

class Dialog(
    private val title: String,
    private val subtitle: String,
    private val positiveButtonText: String,
    private val negativeButtonText: String? = null,
    private val onPositiveAction: () -> Unit,
    private val onNegativeAction: () -> Unit
) : DialogFragment<DialogModalBinding>() {
    override fun getViewBinding(container: ViewGroup?, atParent: Boolean): DialogModalBinding =
        DialogModalBinding.inflate(layoutInflater, container, false)

    override fun onCreation(args: Bundle?) {
        super.onCreation(args)
        binding.dialogTitle.text = title
        binding.dialogSubtitle.text = subtitle
        binding.dialogOk.text = positiveButtonText
        negativeButtonText?.let {
            binding.dialogCancel.visibility = View.VISIBLE
            binding.dialogCancel.text = negativeButtonText
        }
    }

    override fun setUpViewListeners() {
        super.setUpViewListeners()
        binding.dialogOk.setOnClickListener {
            dismiss()
            onPositiveAction()
        }
        binding.dialogCancel.setOnClickListener {
            dismiss()
            onNegativeAction()
        }
    }
}
