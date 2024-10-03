package mx.com.atriz.core.entities

enum class Actions(val value: Int) {
    TAKE_PICTURE(0),
    PICTURE(1);
    companion object {
        fun get(value: Int) = entries.first { it.value == value }
    }
}
