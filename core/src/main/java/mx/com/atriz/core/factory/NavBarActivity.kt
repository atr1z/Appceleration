package mx.com.atriz.core.factory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mx.com.atriz.core.entities.NavTab
import mx.com.atriz.core.theme.AppTheme

abstract class NavBarActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val items = getTabItems(navController)
            val default = items.indexOfFirst { it.key == getDefaultTab() }
            var selectedTabIndex by rememberSaveable { mutableIntStateOf(default) }
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = {
                            NavigationBar {
                                items.forEachIndexed { index, tab ->
                                    NavigationBarItem(
                                        selected = selectedTabIndex == index,
                                        onClick = {
                                            selectedTabIndex = index
                                            navController.navigate(tab.key)
                                        },
                                        icon = {
                                            BadgedBox(badge = { tab.badgeAmount?.let { Text(it.toString()) } }) {
                                                Icon(
                                                    imageVector = if (selectedTabIndex == index) {
                                                        tab.selectedIcon
                                                    } else {
                                                        tab.unselectedIcon
                                                    },
                                                    contentDescription = tab.title
                                                )
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = getDefaultTab(),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            items.forEach { tab ->
                                composable(tab.key) { tab.content() }
                            }
                        }
                    }
                }
            }
        }
    }

    abstract fun getTabItems(navController: NavHostController): List<NavTab>

    abstract fun getDefaultTab(): String
}
