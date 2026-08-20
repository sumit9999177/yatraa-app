package com.example.yatraa.ui.driver

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricRickshaw
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Moped
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate800
import com.example.ui.theme.YatraaCoral
import com.example.ui.theme.YatraaEmerald
import com.example.ui.theme.YatraaGold
import com.example.ui.theme.YatraaNavy
import com.example.ui.theme.YatraaSaffron
import com.example.ui.theme.YatraaSaffronDark
import com.example.ui.theme.YatraaSaffronLight
import com.example.yatraa.data.local.DriverEntity
import com.example.yatraa.data.local.RideEntity
import com.example.yatraa.model.DelhiLocation
import com.example.yatraa.model.DriverVerificationStatus
import com.example.yatraa.model.RideStatus
import com.example.yatraa.model.VehicleCategory
import com.example.yatraa.ui.YatraaMainViewModel
import com.example.yatraa.ui.components.DelhiMapCanvas
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DriverPartnerScreen(
    viewModel: YatraaMainViewModel,
    modifier: Modifier = Modifier
) {
    val activeTab by viewModel.driverTab.collectAsState()
    val driver by viewModel.currentDriver.collectAsState()
    val allDrivers by viewModel.allDrivers.collectAsState()
    val incomingRide by viewModel.incomingRideRequest.collectAsState()
    val incomingTimer by viewModel.incomingRequestSecondsLeft.collectAsState()
    val allRides by viewModel.allRides.collectAsState()

    var showDriverSwitcher by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Driver Partner Header & Profile Switcher
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = YatraaNavy,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { showDriverSwitcher = true }
                ) {
                    Surface(
                        shape = CircleShape,
                        color = YatraaSaffron,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = (driver?.name?.take(1) ?: "D"),
                                fontWeight = FontWeight.Black,
                                color = Color.Black,
                                fontSize = 16.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = driver?.name ?: "Driver Partner",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                Icons.Default.SwapHoriz,
                                contentDescription = "Switch Driver",
                                tint = YatraaSaffron,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = "${driver?.vehicleCategory?.displayName} • ${driver?.vehicleNumber ?: "DL-1R"} • ${driver?.rating ?: 4.8} ★",
                            style = MaterialTheme.typography.labelSmall.copy(color = Slate400)
                        )
                    }

                    DropdownMenu(
                        expanded = showDriverSwitcher,
                        onDismissRequest = { showDriverSwitcher = false }
                    ) {
                        allDrivers.forEach { d ->
                            DropdownMenuItem(
                                text = {
                                    Text("${d.name} (${d.vehicleCategory.displayName} - ${d.vehicleNumber})")
                                },
                                onClick = {
                                    viewModel.selectDriverPartner(d.driverId)
                                    showDriverSwitcher = false
                                }
                            )
                        }
                    }
                }

                // Online/Offline Switch
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (driver?.isOnline == true) "ONLINE" else "OFFLINE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Black,
                            color = if (driver?.isOnline == true) YatraaEmerald else Slate400,
                            fontSize = 11.sp
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Switch(
                        checked = driver?.isOnline == true,
                        onCheckedChange = { viewModel.toggleDriverOnline(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = YatraaEmerald,
                            uncheckedThumbColor = Slate400,
                            uncheckedTrackColor = Color(0xFF334155)
                        ),
                        modifier = Modifier.testTag("driver_online_toggle")
                    )
                }
            }
        }

        // Driver Navigation Tabs: 0=Duty/Radar, 1=Today's Earnings, 2=Trips, 3=KYC Docs
        TabRow(
            selectedTabIndex = activeTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = YatraaSaffronDark
        ) {
            Tab(
                selected = activeTab == 0,
                onClick = { viewModel.setDriverTab(0) },
                text = { Text("Duty Map", fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                icon = { Icon(Icons.Default.Dashboard, contentDescription = null, modifier = Modifier.size(16.dp)) },
                modifier = Modifier.testTag("tab_driver_duty")
            )
            Tab(
                selected = activeTab == 1,
                onClick = { viewModel.setDriverTab(1) },
                text = { Text("Earnings", fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, modifier = Modifier.size(16.dp)) },
                modifier = Modifier.testTag("tab_driver_earnings")
            )
            Tab(
                selected = activeTab == 2,
                onClick = { viewModel.setDriverTab(2) },
                text = { Text("Trips", fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                icon = { Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(16.dp)) },
                modifier = Modifier.testTag("tab_driver_trips")
            )
            Tab(
                selected = activeTab == 3,
                onClick = { viewModel.setDriverTab(3) },
                text = { Text("KYC Docs", fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                icon = { Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(16.dp)) },
                modifier = Modifier.testTag("tab_driver_kyc")
            )
        }

        // Active Trip for this driver if assigned
        val activeDriverRide = allRides.firstOrNull {
            it.driverId == driver?.driverId &&
            it.rideStatus != RideStatus.COMPLETED &&
            !it.rideStatus.name.startsWith("CANCELLED")
        }

        when (activeTab) {
            0 -> DriverDutyMapTab(
                driver = driver,
                activeRide = activeDriverRide,
                incomingRide = incomingRide,
                incomingTimer = incomingTimer,
                viewModel = viewModel
            )
            1 -> DriverEarningsTab(driver = driver, allRides = allRides)
            2 -> DriverTripHistoryTab(driver = driver, allRides = allRides)
            3 -> DriverKycDocsTab(driver = driver)
        }
    }
}

@Composable
private fun DriverDutyMapTab(
    driver: DriverEntity?,
    activeRide: RideEntity?,
    incomingRide: RideEntity?,
    incomingTimer: Int,
    viewModel: YatraaMainViewModel
) {
    val context = LocalContext.current
    var showOtpDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // 1. Live Delhi Map Canvas showing Driver's territory
        DelhiMapCanvas(
            modifier = Modifier.fillMaxSize(),
            pickupLocation = if (activeRide != null) DelhiLocation("p", activeRide.pickupAddress, "", "NCR", activeRide.pickupLat, activeRide.pickupLng) else null,
            dropLocation = if (activeRide != null) DelhiLocation("d", activeRide.dropAddress, "", "NCR", activeRide.dropLat, activeRide.dropLng) else null,
            rideStatus = activeRide?.rideStatus ?: RideStatus.IDLE
        )

        // 2. Offline / Searching overlay if driver is offline
        if (driver?.isOnline != true && activeRide == null) {
            Surface(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(24.dp),
                shape = RoundedCornerShape(16.dp),
                color = YatraaNavy.copy(alpha = 0.95f),
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.PowerSettingsNew,
                        contentDescription = null,
                        tint = Slate400,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "You are currently OFFLINE",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Text(
                        text = "Toggle ONLINE at top to start receiving Delhi-NCR ride requests",
                        style = MaterialTheme.typography.bodySmall.copy(color = Slate400),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.toggleDriverOnline(true) },
                        colors = ButtonDefaults.buttonColors(containerColor = YatraaEmerald),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("btn_driver_go_online")
                    ) {
                        Text("GO ONLINE NOW", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 3. Active Ride Control Sheet for Driver
        if (activeRide != null) {
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(10.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "PASSENGER: ${activeRide.customerName}",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Earnings: ₹${(activeRide.finalFare * 0.90).toInt()} (90% payout)",
                                style = MaterialTheme.typography.bodySmall.copy(color = YatraaEmerald, fontWeight = FontWeight.Bold)
                            )
                        }

                        IconButton(
                            onClick = {
                                val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${activeRide.customerPhone}"))
                                context.startActivity(callIntent)
                            }
                        ) {
                            Icon(Icons.Default.Call, contentDescription = "Call Passenger", tint = YatraaEmerald)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Pickup: ${activeRide.pickupAddress}",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Drop: ${activeRide.dropAddress}",
                        style = MaterialTheme.typography.bodySmall.copy(color = Slate600),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    when (activeRide.rideStatus) {
                        RideStatus.DRIVER_ASSIGNED, RideStatus.DRIVER_ARRIVING -> {
                            Button(
                                onClick = { viewModel.repository.let { /* arrived */ } },
                                colors = ButtonDefaults.buttonColors(containerColor = YatraaSaffronDark),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("btn_driver_arrived")
                            ) {
                                Icon(Icons.Default.LocationOn, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("I Have Arrived at Pickup Point", fontWeight = FontWeight.Bold)
                            }
                        }

                        RideStatus.DRIVER_ARRIVED -> {
                            Button(
                                onClick = { showOtpDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = YatraaEmerald),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("btn_driver_enter_otp")
                            ) {
                                Text("Ask Passenger for OTP & Start Ride", fontWeight = FontWeight.Bold)
                            }
                        }

                        RideStatus.OTP_VERIFIED, RideStatus.IN_PROGRESS -> {
                            Button(
                                onClick = { viewModel.completeRide(activeRide.rideId) },
                                colors = ButtonDefaults.buttonColors(containerColor = YatraaEmerald),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("btn_driver_complete_ride")
                            ) {
                                Text("Complete Ride & Collect ₹${activeRide.finalFare.toInt()}", fontWeight = FontWeight.Bold)
                            }
                        }

                        else -> {}
                    }
                }
            }
        }

        // 4. Incoming Ride Request Bottom Popup (15-sec timer)
        if (incomingRide != null) {
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .testTag("incoming_ride_request_sheet"),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                colors = CardDefaults.cardColors(containerColor = YatraaNavy),
                elevation = CardDefaults.cardElevation(16.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(YatraaSaffron, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("$incomingTimer", fontWeight = FontWeight.Black, color = Color.Black)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "NEW RIDE REQUEST",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        color = YatraaSaffron
                                    )
                                )
                                Text(
                                    text = "${incomingRide.vehicleCategory.displayName} • ${incomingRide.distanceKm} km trip",
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color.White)
                                )
                            }
                        }

                        Text(
                            text = "₹${(incomingRide.finalFare * 0.90).toInt()}",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                color = YatraaEmerald
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF1E293B)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "Pickup: ${incomingRide.pickupAddress}",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.White, fontWeight = FontWeight.SemiBold)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Drop: ${incomingRide.dropAddress}",
                                style = MaterialTheme.typography.bodySmall.copy(color = Slate400)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.driverRejectIncomingRide() },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_driver_reject_ride"),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, Slate400)
                        ) {
                            Text("Reject", color = Slate400)
                        }

                        Button(
                            onClick = { viewModel.driverAcceptIncomingRide() },
                            modifier = Modifier
                                .weight(1.5f)
                                .testTag("btn_driver_accept_ride"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = YatraaEmerald)
                        ) {
                            Text("ACCEPT RIDE", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // 5. Driver OTP Input Dialog
        if (showOtpDialog && activeRide != null) {
            DriverOtpVerificationDialog(
                expectedOtp = activeRide.otpCode,
                onDismiss = { showOtpDialog = false },
                onVerify = { entered ->
                    val success = viewModel.verifyOtpAndStartRide(activeRide.rideId, entered, activeRide.otpCode)
                    if (success) {
                        showOtpDialog = false
                    }
                }
            )
        }
    }
}

@Composable
private fun DriverOtpVerificationDialog(
    expectedOtp: String,
    onDismiss: () -> Unit,
    onVerify: (String) -> Unit
) {
    var enteredOtp by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Enter Passenger 4-Digit OTP", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Ask the passenger for the 4-digit PIN displayed on their app to verify pickup and begin trip.",
                    style = MaterialTheme.typography.bodySmall.copy(color = Slate600)
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = enteredOtp,
                    onValueChange = {
                        if (it.length <= 4) {
                            enteredOtp = it
                            isError = false
                        }
                    },
                    label = { Text("4-Digit OTP (e.g. $expectedOtp)") },
                    isError = isError,
                    supportingText = if (isError) {
                        { Text("Invalid OTP code. Please recheck.", color = YatraaCoral) }
                    } else null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("driver_otp_input_field"),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (enteredOtp == expectedOtp) {
                        onVerify(enteredOtp)
                    } else {
                        isError = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = YatraaEmerald),
                modifier = Modifier.testTag("btn_verify_otp_start")
            ) {
                Text("Verify & Start Trip")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun DriverEarningsTab(
    driver: DriverEntity?,
    allRides: List<RideEntity>
) {
    val driverEarnings = driver?.dailyEarnings ?: 1840.0
    val todayCompletedRides = allRides.filter { it.driverId == driver?.driverId && it.rideStatus == RideStatus.COMPLETED }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Today Earnings Hero
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = YatraaNavy)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "TODAY'S NET EARNINGS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = YatraaSaffron,
                            letterSpacing = 1.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "₹${driverEarnings.toInt()}",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    )
                    Text(
                        text = "90% Net Payout • 10% Platform Fee",
                        style = MaterialTheme.typography.bodySmall.copy(color = Slate400)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Daily Target Progress Bar
                    val target = 2500f
                    val prog = (driverEarnings / target).toFloat().coerceIn(0f, 1f)
                    Text(
                        text = "Daily Target: ₹${driverEarnings.toInt()} / ₹2,500 (${(prog * 100).toInt()}%)",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.White, fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { prog },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = YatraaEmerald,
                        trackColor = Color(0xFF334155)
                    )
                }
            }
        }

        // Stats Grid
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Total Trips", style = MaterialTheme.typography.labelSmall.copy(color = Slate600))
                        Text(
                            text = "${driver?.totalRides ?: 420}",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Partner Rating", style = MaterialTheme.typography.labelSmall.copy(color = Slate600))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${driver?.rating ?: 4.88}",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                            )
                            Icon(Icons.Default.Star, contentDescription = null, tint = YatraaGold, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "Today's Trip Payouts",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
        }

        if (todayCompletedRides.isEmpty()) {
            item {
                Text(
                    text = "No completed trips yet today. Stay online to get orders!",
                    style = MaterialTheme.typography.bodySmall.copy(color = Slate600)
                )
            }
        } else {
            items(todayCompletedRides) { r ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "${r.pickupAddress} -> ${r.dropAddress}",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${r.distanceKm} km • Cash/UPI",
                                style = MaterialTheme.typography.labelSmall.copy(color = Slate600)
                            )
                        }
                        Text(
                            text = "+₹${(r.finalFare * 0.90).toInt()}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = YatraaEmerald
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DriverTripHistoryTab(
    driver: DriverEntity?,
    allRides: List<RideEntity>
) {
    val myRides = allRides.filter { it.driverId == driver?.driverId }

    if (myRides.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("No trip history yet for this partner.")
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(myRides) { r ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Trip #${r.rideId}",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Earned ₹${(r.finalFare * 0.90).toInt()}",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    color = YatraaEmerald
                                )
                            )
                        }
                        Text(text = "Rider: ${r.customerName} (${r.customerPhone})", style = MaterialTheme.typography.bodySmall.copy(color = Slate600))
                        Text(text = "${r.pickupAddress} to ${r.dropAddress}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@Composable
private fun DriverKycDocsTab(driver: DriverEntity?) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.VerifiedUser,
                        contentDescription = null,
                        tint = when (driver?.verificationStatus) {
                            DriverVerificationStatus.VERIFIED -> YatraaEmerald
                            DriverVerificationStatus.PENDING_DOCS -> YatraaSaffronDark
                            else -> YatraaCoral
                        },
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("KYC Verification Status", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                        Text(
                            text = driver?.verificationStatus?.name ?: "VERIFIED",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = when (driver?.verificationStatus) {
                                    DriverVerificationStatus.VERIFIED -> YatraaEmerald
                                    DriverVerificationStatus.PENDING_DOCS -> YatraaSaffronDark
                                    else -> YatraaCoral
                                }
                            )
                        )
                    }
                }
            }
        }

        item {
            Text("Uploaded Government Documents", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
        }

        val docs = listOf(
            "Commercial Driving License (Delhi Transport Dept)" to "DL-0420180019283 (Valid till 2030)",
            "Vehicle Registration Certificate (RC)" to (driver?.vehicleNumber ?: "DL 1R AB 4592"),
            "Vehicle Insurance & Fitness Certificate" to "Policy #BAJAJ-ALL-98213 (Active)",
            "Aadhaar Identity Card" to "XXXX-XXXX-8921 (Verified)"
        )

        items(docs) { (title, subtitle) ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = title, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                        Text(text = subtitle, style = MaterialTheme.typography.labelSmall.copy(color = Slate600))
                    }
                    Icon(Icons.Default.CheckCircle, contentDescription = "Verified", tint = YatraaEmerald, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}
