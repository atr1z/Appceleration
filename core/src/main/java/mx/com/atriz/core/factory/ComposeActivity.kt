package mx.com.atriz.core.factory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mx.com.atriz.core.entities.NavRoute
import mx.com.atriz.core.theme.AppTheme

abstract class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AppTheme {
                NavHost(navController, startDestination = getInitialView()) {
                    setNavigation(navController).forEach { route ->
                        composable(route.key) { route.screen }
                    }
                }
            }
        }
    }

    abstract fun setNavigation(navController: NavHostController): List<NavRoute>

    abstract fun getInitialView(): String
}
