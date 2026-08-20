package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.Slate400
import com.example.ui.theme.YatraaNavy
import com.example.ui.theme.YatraaSaffron
import com.example.ui.theme.YatraaSaffronDark
import com.example.yatraa.model.CustomerScreen
import com.example.yatraa.model.UserRole
import com.example.yatraa.ui.YatraaMainViewModel
import com.example.yatraa.ui.admin.AdminPanelScreen
import com.example.yatraa.ui.components.AppRoleSwitcherBar
import com.example.yatraa.ui.customer.CustomerConfirmRideScreen
import com.example.yatraa.ui.customer.CustomerDestinationSearchScreen
import com.example.yatraa.ui.customer.CustomerFareEstimateDetailsScreen
import com.example.yatraa.ui.customer.CustomerHistoryAndSavedScreen
import com.example.yatraa.ui.customer.CustomerHomeScreen
import com.example.yatraa.ui.customer.CustomerLoginScreen
import com.example.yatraa.ui.customer.CustomerOtpScreen
import com.example.yatraa.ui.customer.CustomerPaymentResultScreen
import com.example.yatraa.ui.customer.CustomerPickupDropConfirmScreen
import com.example.yatraa.ui.customer.CustomerProfileSetupScreen
import com.example.yatraa.ui.customer.CustomerRatingFeedbackScreen
import com.example.yatraa.ui.customer.CustomerRideActiveScreen
import com.example.yatraa.ui.customer.CustomerRideCompletedScreen
import com.example.yatraa.ui.customer.CustomerRideDetailsScreen
import com.example.yatraa.ui.customer.CustomerSplashScreen
import com.example.yatraa.ui.driver.DriverPartnerScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val mainViewModel: YatraaMainViewModel = viewModel()
                YatraaMainApp(viewModel = mainViewModel)
            }
        }
    }
}

@Composable
fun YatraaMainApp(viewModel: YatraaMainViewModel) {
    val currentRole by viewModel.currentRole.collectAsState()
    val activeCustomerRide by viewModel.activeCustomerRide.collectAsState()
    val customerScreen by viewModel.customerScreen.collectAsState()
    val customerTab by viewModel.customerTab.collectAsState()

    val showBottomBar = currentRole == UserRole.CUSTOMER &&
        (customerScreen == CustomerScreen.HOME ||
         customerScreen == CustomerScreen.RIDE_HISTORY ||
         customerScreen == CustomerScreen.SAVED_PLACES ||
         customerScreen == CustomerScreen.OFFERS_REFERRAL ||
         customerScreen == CustomerScreen.PROFILE_SETTINGS)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            AppRoleSwitcherBar(
                currentRole = currentRole,
                onRoleSelected = { viewModel.setRole(it) }
            )
        },
        bottomBar = {
            if (showBottomBar) {
                CustomerBottomNavBar(
                    selectedTab = customerTab,
                    onTabSelect = { tabIndex ->
                        viewModel.setCustomerTab(tabIndex)
                        when (tabIndex) {
                            0 -> viewModel.navigateToCustomerScreen(CustomerScreen.HOME)
                            1 -> viewModel.navigateToCustomerScreen(CustomerScreen.RIDE_HISTORY)
                            2 -> viewModel.navigateToCustomerScreen(CustomerScreen.SAVED_PLACES)
                            3 -> viewModel.navigateToCustomerScreen(CustomerScreen.OFFERS_REFERRAL)
                            4 -> viewModel.navigateToCustomerScreen(CustomerScreen.PROFILE_SETTINGS)
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentRole) {
                UserRole.CUSTOMER -> {
                    when (customerScreen) {
                        CustomerScreen.SPLASH -> CustomerSplashScreen(viewModel = viewModel)
                        CustomerScreen.LOGIN -> CustomerLoginScreen(viewModel = viewModel)
                        CustomerScreen.OTP -> CustomerOtpScreen(viewModel = viewModel)
                        CustomerScreen.PROFILE_SETUP -> CustomerProfileSetupScreen(viewModel = viewModel)
                        CustomerScreen.HOME -> {
                            if (customerTab == 0) {
                                CustomerHomeScreen(viewModel = viewModel)
                            } else {
                                CustomerHistoryAndSavedScreen(viewModel = viewModel)
                            }
                        }
                        CustomerScreen.DESTINATION_SEARCH -> CustomerDestinationSearchScreen(viewModel = viewModel)
                        CustomerScreen.PICKUP_DROP_CONFIRM -> CustomerPickupDropConfirmScreen(viewModel = viewModel)
                        CustomerScreen.FARE_ESTIMATE_DETAILS -> CustomerFareEstimateDetailsScreen(viewModel = viewModel)
                        CustomerScreen.CONFIRM_RIDE -> CustomerConfirmRideScreen(viewModel = viewModel)
                        CustomerScreen.ACTIVE_RIDE -> {
                            if (activeCustomerRide != null) {
                                CustomerRideActiveScreen(
                                    ride = activeCustomerRide!!,
                                    viewModel = viewModel
                                )
                            } else {
                                CustomerHomeScreen(viewModel = viewModel)
                            }
                        }
                        CustomerScreen.RIDE_COMPLETED -> CustomerRideCompletedScreen(viewModel = viewModel)
                        CustomerScreen.PAYMENT_RESULT -> CustomerPaymentResultScreen(viewModel = viewModel)
                        CustomerScreen.RATING -> CustomerRatingFeedbackScreen(viewModel = viewModel)
                        CustomerScreen.RIDE_DETAILS -> CustomerRideDetailsScreen(viewModel = viewModel)
                        CustomerScreen.RIDE_HISTORY,
                        CustomerScreen.SAVED_PLACES,
                        CustomerScreen.OFFERS_REFERRAL,
                        CustomerScreen.PROFILE_SETTINGS -> {
                            CustomerHistoryAndSavedScreen(viewModel = viewModel)
                        }
                        CustomerScreen.SAFETY_CENTER -> {
                            CustomerHomeScreen(viewModel = viewModel)
                        }
                    }
                }

                UserRole.DRIVER -> {
                    DriverPartnerScreen(viewModel = viewModel)
                }

                UserRole.ADMIN -> {
                    AdminPanelScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun CustomerBottomNavBar(
    selectedTab: Int,
    onTabSelect: (Int) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = { onTabSelect(0) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home", modifier = Modifier.size(22.dp)) },
            label = { Text("Home", fontWeight = FontWeight.Bold, fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = YatraaSaffronDark,
                selectedTextColor = YatraaSaffronDark,
                indicatorColor = YatraaSaffron.copy(alpha = 0.2f),
                unselectedIconColor = Slate400,
                unselectedTextColor = Slate400
            ),
            modifier = Modifier.testTag("nav_bottom_home")
        )

        NavigationBarItem(
            selected = selectedTab == 1,
            onClick = { onTabSelect(1) },
            icon = { Icon(Icons.Default.History, contentDescription = "Trips", modifier = Modifier.size(22.dp)) },
            label = { Text("Trips", fontWeight = FontWeight.Bold, fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = YatraaSaffronDark,
                selectedTextColor = YatraaSaffronDark,
                indicatorColor = YatraaSaffron.copy(alpha = 0.2f),
                unselectedIconColor = Slate400,
                unselectedTextColor = Slate400
            ),
            modifier = Modifier.testTag("nav_bottom_trips")
        )

        NavigationBarItem(
            selected = selectedTab == 2,
            onClick = { onTabSelect(2) },
            icon = { Icon(Icons.Default.LocationOn, contentDescription = "Saved", modifier = Modifier.size(22.dp)) },
            label = { Text("Saved", fontWeight = FontWeight.Bold, fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = YatraaSaffronDark,
                selectedTextColor = YatraaSaffronDark,
                indicatorColor = YatraaSaffron.copy(alpha = 0.2f),
                unselectedIconColor = Slate400,
                unselectedTextColor = Slate400
            ),
            modifier = Modifier.testTag("nav_bottom_saved")
        )

        NavigationBarItem(
            selected = selectedTab == 3,
            onClick = { onTabSelect(3) },
            icon = { Icon(Icons.Default.CardGiftcard, contentDescription = "Offers", modifier = Modifier.size(22.dp)) },
            label = { Text("Offers", fontWeight = FontWeight.Bold, fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = YatraaSaffronDark,
                selectedTextColor = YatraaSaffronDark,
                indicatorColor = YatraaSaffron.copy(alpha = 0.2f),
                unselectedIconColor = Slate400,
                unselectedTextColor = Slate400
            ),
            modifier = Modifier.testTag("nav_bottom_offers")
        )

        NavigationBarItem(
            selected = selectedTab == 4,
            onClick = { onTabSelect(4) },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile", modifier = Modifier.size(22.dp)) },
            label = { Text("Profile", fontWeight = FontWeight.Bold, fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = YatraaSaffronDark,
                selectedTextColor = YatraaSaffronDark,
                indicatorColor = YatraaSaffron.copy(alpha = 0.2f),
                unselectedIconColor = Slate400,
                unselectedTextColor = Slate400
            ),
            modifier = Modifier.testTag("nav_bottom_profile")
        )
    }
}
