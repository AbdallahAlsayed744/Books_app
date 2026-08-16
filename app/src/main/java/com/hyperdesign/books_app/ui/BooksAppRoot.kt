package com.hyperdesign.books_app.ui

import androidx.annotation.StringRes
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import com.hyperdesign.books_app.R
import com.hyperdesign.navigation.BookList
import com.hyperdesign.navigation.Home
import com.hyperdesign.navigation.Search
import com.hyperdesign.navigation.Settings
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.hyperdesign.books_app.ui.theme.Books_appTheme
import com.hyperdesign.design_system.theme.BookTheme
import com.hyperdesign.navigation.FeatureEntryProvider
import com.hyperdesign.navigation.LocalSharedTransitionScope
import com.hyperdesign.navigation.install
import kotlinx.coroutines.launch
import org.koin.compose.getKoin

private data class TopLevelDestination(
    val key: NavKey,
    val icon: ImageVector,
    @StringRes val labelRes: Int,
)

private val topLevelDestinations = listOf(
    TopLevelDestination(Home, Icons.Filled.Home, R.string.nav_home),
    TopLevelDestination(Search, Icons.Filled.Search, R.string.nav_search),
    TopLevelDestination(BookList, Icons.Filled.Favorite, R.string.nav_favorites),
    TopLevelDestination(Settings, Icons.Filled.Settings, R.string.nav_settings),
)


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun BookAppRoot() {

    BookTheme(){
        val scope = rememberCoroutineScope()


        var setupDismissed by rememberSaveable { mutableStateOf(false) }
//        if (!NetworkConstants.HAS_ACCESS_TOKEN && !setupDismissed) {
//            ApiKeySetupScreen(onContinue = { setupDismissed = true })
//            return@MovieAppTheme
//        }

        val backStack = rememberNavBackStack(Home)
        val entryProviders = getKoin().getAll<FeatureEntryProvider>()

        val currentTop = backStack.lastOrNull()
        val showBottomBar = topLevelDestinations.any { it.key == currentTop }

        Scaffold(
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            bottomBar = {
                if (showBottomBar) {
                    NavigationBar {
                        topLevelDestinations.forEach { dest ->
                            val label = stringResource(dest.labelRes)
                            NavigationBarItem(
                                selected = currentTop == dest.key,
                                onClick = {
                                    backStack.clear()
                                    backStack.add(dest.key)
                                },
                                icon = { Icon(dest.icon, contentDescription = label) },
                                label = { Text(label) },
                            )
                        }
                    }
                }
            },
        ) { padding ->
            SharedTransitionLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .consumeWindowInsets(padding),
            ) {
                CompositionLocalProvider(LocalSharedTransitionScope provides this) {
                    NavDisplay(
                        backStack = backStack,
                        onBack = { backStack.removeLastOrNull() },
                        modifier = Modifier.fillMaxSize(),
                        sharedTransitionScope = this@SharedTransitionLayout,
                        transitionSpec = navForwardSpec,
                        popTransitionSpec = navPopSpec,
                        predictivePopTransitionSpec = navPredictivePopSpec,
                        entryProvider = entryProvider {
                            entryProviders.forEach { provider -> install(provider, backStack) }
                        },
                    )
                }
            }
        }

    }
}