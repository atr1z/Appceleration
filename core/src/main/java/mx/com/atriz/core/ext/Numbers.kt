package mx.com.atriz.core.ext

import java.math.RoundingMode
import java.text.DecimalFormat

fun Float.decimals(): String {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.DOWN
    return df.format(this)
}

fun Double.money(): String {
    val format = DecimalFormat("#,###.00")
    format.isDecimalSeparatorAlwaysShown = false
    return format.format(this).toString()
}
