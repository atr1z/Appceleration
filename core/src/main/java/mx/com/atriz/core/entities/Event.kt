package mx.com.atriz.core.entities

/**
 * Enum class representing different events that can occur.
 */
enum class Event(val value: String) {
    /**
     * Event indicating a location change.
     */
    Location("location_changed"),

    /**
     * Event indicating a network change.
     */
    Network("network_changed"),

    /**
     * Event indicating an internet connectivity change.
     */
    Internet("internet_changed");

    companion object {
        /**
         * Gets the [Event] enum value for the given string value.
         *
         * @param value The string value.
         * @return The corresponding [Event] enum value.
         */
        fun get(value: String): Event = values().first { it.value == value }
    }
}
