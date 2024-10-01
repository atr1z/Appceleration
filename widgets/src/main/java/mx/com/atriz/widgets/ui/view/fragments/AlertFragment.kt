package mx.com.atriz.widgets.ui.view.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import mx.com.atriz.widgets.databinding.DialogAlertBinding
import mx.com.atriz.widgets.model.Alert

class AlertFragment(private val alert: Alert) : DialogFragment() {

    private var _binding: DialogAlertBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogAlertBinding.inflate(layoutInflater)
        val view = binding.root

        binding.title.text = getString(alert.title)
        binding.message.text = getString(alert.message)
        binding.positiveText.text = getString(alert.positive)
        binding.negativeText.text = getString(alert.negative)

        binding.negative.setOnClickListener {
            alert.onNegative()
            dismiss()
        }

        binding.positive.setOnClickListener {
            alert.onPositive()
            dismiss()
        }

        binding.close.setOnClickListener {
            alert.onNeutral()
            dismiss()
        }

        return AlertDialog.Builder(requireContext())
            .setView(view)
            .create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
