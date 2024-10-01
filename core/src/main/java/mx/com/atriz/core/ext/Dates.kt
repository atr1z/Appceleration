package mx.com.atriz.core.ext

import android.os.Build
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

fun Date.readable(): String {
    return SimpleDateFormat("yyyy", Locale.getDefault()).format(this)
}

fun String.isoToDate(): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
    val time = LocalDateTime.parse(this, formatter)
    return time.atOffset(ZoneOffset.UTC)
        .atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()
}

fun LocalDateTime.toTime(): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm a")
    return this.format(formatter)
}

fun LocalDateTime.timeLeft(): String {
    val now = LocalDateTime.now()
    val remainingTime = Duration.between(now, this)
    val hours = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        remainingTime.toHoursPart()
    } else {
        remainingTime.toHours()
    }
    val minutes = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        remainingTime.toMinutesPart()
    } else {
        remainingTime.toMinutes()
    }
    return "$hours h $minutes m"
}

fun Date.full(): String {
    val dateFormat = SimpleDateFormat("EEEE dd 'de' MMMM 'de' yyyy HH:mm:ss", Locale.getDefault())
    return dateFormat.format(this)
}

fun Date.day(): String {
    val dateFormat = SimpleDateFormat("EEEE dd 'de' MMMM 'de' yyyy", Locale.getDefault())
    return dateFormat.format(this)
}

fun Date.simple(): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return dateFormat.format(this)
}
