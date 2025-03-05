package mx.com.atriz.core.entities

import androidx.compose.runtime.Composable

data class NavRoute(val key: String, val screen: @Composable () -> Unit)
