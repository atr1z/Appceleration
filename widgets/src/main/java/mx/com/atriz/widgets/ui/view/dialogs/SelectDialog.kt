package mx.com.atriz.widgets.ui.view.dialogs

import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import mx.com.atriz.core.factory.DialogFragment
import mx.com.atriz.widgets.databinding.DialogSelectBinding
import mx.com.atriz.widgets.model.Option
import mx.com.atriz.widgets.ui.view.recyclers.adapters.SelectAdapter

class SelectDialog(
    private val title: String,
    private val subtitle: String,
    private val options: List<Option>,
    private val onSelectedOption: (position: Int, option: Option) -> Unit
) : DialogFragment<DialogSelectBinding>() {
    private var selectedOptionPosition = -1
    private var selectedOption: Option? = null
    override fun getViewBinding(container: ViewGroup?, atParent: Boolean): DialogSelectBinding =
        DialogSelectBinding.inflate(layoutInflater, container, false)

    override fun onCreation(args: Bundle?) {
        super.onCreation(args)
        binding.dialogTitle.text = title
        binding.dialogSubtitle.text = subtitle
        binding.dialogOptions.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.dialogOptions.adapter = SelectAdapter(options) { position, option ->
            selectedOption = option
            selectedOptionPosition = position
        }
    }

    override fun setUpViewListeners() {
        super.setUpViewListeners()
        binding.dialogCancel.setOnClickListener { dismiss() }
        binding.dialogOk.setOnClickListener {
            selectedOption?.let { option ->
                onSelectedOption(selectedOptionPosition, option)
            }
            dismiss()
        }
    }
}
