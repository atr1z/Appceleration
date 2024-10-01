package mx.com.atriz.widgets.ext

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.text.DecimalFormat

fun EditText.moneyTextWatcher(
    decimalFormat: DecimalFormat = DecimalFormat("0.00"),
    onTextChanged: (String) -> Unit = {}
) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            // Do nothing.
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Do nothing.
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val text = s.toString()
            val formattedText = decimalFormat.format(text.toDouble())
            this@moneyTextWatcher.setText(formattedText)
            this@moneyTextWatcher.setSelection(formattedText.length)
            onTextChanged(formattedText)
        }
    })
}

fun EditText.onChange(onTextChanged: (String) -> Unit = {}) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            onTextChanged(p0.toString())
        }

        override fun afterTextChanged(p0: Editable?) {}
    })
}
