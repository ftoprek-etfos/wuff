package com.filiptoprek.wuff.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.filiptoprek.wuff.R
import com.filiptoprek.wuff.presentation.auth.AuthViewModel
import com.filiptoprek.wuff.presentation.auth.LoginScreen
import com.filiptoprek.wuff.presentation.auth.LandingScreen
import com.filiptoprek.wuff.presentation.auth.RegisterScreen
import com.filiptoprek.wuff.domain.model.core.BottomNavigationItem
import com.filiptoprek.wuff.presentation.core.HomeScreen
import com.filiptoprek.wuff.presentation.core.ProfileScreen
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseUser

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupNavGraph(
    navController: NavHostController = rememberNavController(),
    viewModel: AuthViewModel,
    googleSignInClient: GoogleSignInClient
) {
    val items = listOf(
        BottomNavigationItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            hasNews = false,
            route = Routes.Home.route
        ),
        BottomNavigationItem(
            title = "Reservations",
            selectedIcon = Icons.Filled.CheckCircle,
            unselectedIcon = Icons.Outlined.CheckCircle,
            hasNews = false,
            route = Routes.Home.route
        ),
        BottomNavigationItem(
            title = "Profile",
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person,
            hasNews = false,
            route = Routes.Profile.route
        ),
    )
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }
    val currentUser: FirebaseUser? by viewModel.currentUserLiveData.observeAsState()

    Scaffold(
        bottomBar = {
            if (currentUser != null) {
                NavigationBar(
                    containerColor = colorResource(R.color.gray),
                    contentColor = colorResource(R.color.green_accent)
                ){
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = colorResource(R.color.green_accent),
                                unselectedIconColor = Color(255,255,255,50),
                                indicatorColor = colorResource(R.color.gray)
                            ),
                            selected = index == selectedItemIndex,
                            onClick = {
                                selectedItemIndex = index
                                navController.navigate(item.route)
                            },
                            label = {
                                Text(text = item.title)
                            },
                            alwaysShowLabel = false,
                            icon = {
                                BadgedBox(
                                    badge = {
                                        if (item.badgeCount != null) {
                                            Badge {
                                                Text(text = item.badgeCount.toString())
                                            }
                                        } else if (item.hasNews) {
                                            Badge()
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (selectedItemIndex == index) {
                                            item.selectedIcon
                                        } else item.unselectedIcon,
                                        contentDescription = item.title,
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination =
            if (viewModel.currentUser == null) {
                Routes.LandingScreen.route
            } else {
                Routes.Home.route
            },
            modifier = Modifier.padding(paddingValues)
        )
        {
            composable(
                route = Routes.LandingScreen.route
            ) {
                LandingScreen(navController)
            }
            composable(
                route = Routes.Login.route
            ) {
                LoginScreen(navController, viewModel, googleSignInClient)
            }
            composable(
                route = Routes.Register.route
            ) {
                RegisterScreen(navController, viewModel, googleSignInClient)
            }
            composable(
                route = Routes.Home.route
            ) {
                HomeScreen(navController, viewModel)
            }
            composable(
                route = Routes.Profile.route
            ) {
                ProfileScreen(navController, viewModel)
            }
        }
    }
}