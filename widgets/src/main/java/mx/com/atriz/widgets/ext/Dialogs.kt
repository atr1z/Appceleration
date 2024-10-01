package mx.com.atriz.widgets.ext

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.MaterialDatePicker
import mx.com.atriz.widgets.R
import mx.com.atriz.widgets.model.Option
import mx.com.atriz.widgets.ui.view.dialogs.Dialog
import mx.com.atriz.widgets.ui.view.dialogs.InputDialog
import mx.com.atriz.widgets.ui.view.dialogs.SelectDialog
import java.util.Date
import java.util.TimeZone

fun AppCompatActivity.showDatePicker(
    title: String,
    onDateSelected: (Date) -> Unit
) {
    val datePicker =
        MaterialDatePicker.Builder.datePicker()
            .setTitleText(title)
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()
    datePicker.addOnPositiveButtonClickListener {
        val timeZoneUTC = TimeZone.getDefault()
        val offsetFromUTC = timeZoneUTC.getOffset(Date().time) * -1
        onDateSelected(Date(it + offsetFromUTC))
    }
    datePicker.show(supportFragmentManager, "datePicker")
}

fun Fragment.showDatePicker(
    title: String,
    onDateSelected: (Date) -> Unit
) {
    val datePicker =
        MaterialDatePicker.Builder.datePicker()
            .setTitleText(title)
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()
    datePicker.addOnPositiveButtonClickListener {
        val timeZoneUTC = TimeZone.getDefault()
        val offsetFromUTC = timeZoneUTC.getOffset(Date().time) * -1
        onDateSelected(Date(it + offsetFromUTC))
    }
    datePicker.show(childFragmentManager, "datePicker")
}

fun Fragment.showSelectDialog(
    title: String,
    subtitle: String,
    options: List<Option>,
    onSelectedOption: (position: Int, option: Option) -> Unit
) {
    val selectDialog = SelectDialog(title, subtitle, options, onSelectedOption)
    selectDialog.show(childFragmentManager, "selectDialog")
}

fun Fragment.showDialog(
    title: String,
    subtitle: String,
    positiveText: String = getString(R.string.ok),
    negativeText: String? = null,
    onPositiveAction: () -> Unit = {},
    onNegativeAction: () -> Unit = {}
) {
    val selectDialog =
        Dialog(title, subtitle, positiveText, negativeText, onPositiveAction, onNegativeAction)
    selectDialog.show(childFragmentManager, "modalDialog")
}

fun Fragment.showInputDialog(
    title: Any,
    default: Any? = null,
    onPositiveAction: (value: String) -> Unit,
    onNegativeAction: () -> Unit = {}
) {
    val inputDialog = InputDialog(title, default, onPositiveAction, onNegativeAction)
    inputDialog.show(childFragmentManager, "inputDialog")
}
