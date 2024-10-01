package mx.com.atriz.widgets.model

import androidx.annotation.StringRes

data class Alert(
    @StringRes val title: Int,
    @StringRes val message: Int,
    @StringRes val positive: Int = android.R.string.ok,
    @StringRes val negative: Int = android.R.string.cancel,
    val onPositive: () -> Unit = {},
    val onNegative: () -> Unit = {},
    val onNeutral: () -> Unit = {}
)
