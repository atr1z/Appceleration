package mx.com.atriz.core.entities

import java.io.Serializable

data class Network(
    val internet: Boolean,
    val wifi: Boolean,
    val network: Boolean
) : Serializable
