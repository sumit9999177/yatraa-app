package com.example.yatraa.ui.customer

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricRickshaw
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Moped
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
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
import com.example.yatraa.data.DelhiNcrData
import com.example.yatraa.data.local.RideEntity
import com.example.yatraa.data.local.SavedPlaceEntity
import com.example.yatraa.model.DelhiLocation
import com.example.yatraa.model.RideStatus
import com.example.yatraa.model.TicketCategory
import com.example.yatraa.model.VehicleCategory
import com.example.yatraa.ui.YatraaMainViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CustomerHistoryAndSavedScreen(
    viewModel: YatraaMainViewModel,
    modifier: Modifier = Modifier
) {
    val activeTab by viewModel.customerTab.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Customer Sub Tabs: 1=Rides History, 2=Saved Places, 3=Offers/Referral, 4=Profile/Safety
        TabRow(
            selectedTabIndex = (activeTab - 1).coerceAtLeast(0),
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = YatraaSaffronDark
        ) {
            Tab(
                selected = activeTab == 1,
                onClick = { viewModel.setCustomerTab(1) },
                text = { Text("Trips", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(18.dp)) },
                modifier = Modifier.testTag("tab_customer_history")
            )
            Tab(
                selected = activeTab == 2,
                onClick = { viewModel.setCustomerTab(2) },
                text = { Text("Saved", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(18.dp)) },
                modifier = Modifier.testTag("tab_customer_saved")
            )
            Tab(
                selected = activeTab == 3,
                onClick = { viewModel.setCustomerTab(3) },
                text = { Text("Offers", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.CardGiftcard, contentDescription = null, modifier = Modifier.size(18.dp)) },
                modifier = Modifier.testTag("tab_customer_offers")
            )
            Tab(
                selected = activeTab == 4,
                onClick = { viewModel.setCustomerTab(4) },
                text = { Text("Profile", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(18.dp)) },
                modifier = Modifier.testTag("tab_customer_profile")
            )
        }

        when (activeTab) {
            1 -> RideHistoryTabContent(viewModel)
            2 -> SavedPlacesTabContent(viewModel)
            3 -> OffersReferralTabContent(viewModel)
            4 -> ProfileSafetyTabContent(viewModel)
            else -> RideHistoryTabContent(viewModel)
        }
    }
}

@Composable
private fun RideHistoryTabContent(viewModel: YatraaMainViewModel) {
    val rides by viewModel.allRides.collectAsState()

    if (rides.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    tint = Slate400,
                    modifier = Modifier.size(56.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "No rides booked yet",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Book your first bike or auto ride from the Home screen!",
                    style = MaterialTheme.typography.bodySmall.copy(color = Slate600)
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(rides) { ride ->
                RideHistoryCard(
                    ride = ride,
                    onClick = { viewModel.viewPastRideDetails(ride) },
                    onBookAgain = {
                        viewModel.bookAgainRide(ride)
                    }
                )
            }
        }
    }
}

@Composable
private fun RideHistoryCard(
    ride: RideEntity,
    onClick: () -> Unit,
    onBookAgain: () -> Unit
) {
    val dateFormatter = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
    val dateStr = dateFormatter.format(Date(ride.createdAt))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("ride_history_card_${ride.rideId}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = when (ride.vehicleCategory) {
                            VehicleCategory.BIKE -> Icons.Default.Moped
                            VehicleCategory.AUTO -> Icons.Default.ElectricRickshaw
                            VehicleCategory.CAB -> Icons.Default.DirectionsCar
                        },
                        contentDescription = null,
                        tint = YatraaSaffronDark,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = ride.vehicleCategory.displayName,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when (ride.rideStatus) {
                        RideStatus.COMPLETED -> Color(0xFFDCFCE7)
                        RideStatus.CANCELLED_BY_CUSTOMER, RideStatus.CANCELLED_BY_DRIVER, RideStatus.CANCELLED_BY_SYSTEM -> Color(0xFFFEE2E2)
                        else -> YatraaSaffronLight
                    }
                ) {
                    Text(
                        text = ride.rideStatus.displayLabel,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = when (ride.rideStatus) {
                                RideStatus.COMPLETED -> Color(0xFF15803D)
                                RideStatus.CANCELLED_BY_CUSTOMER, RideStatus.CANCELLED_BY_DRIVER, RideStatus.CANCELLED_BY_SYSTEM -> YatraaCoral
                                else -> YatraaSaffronDark
                            },
                            fontSize = 10.sp
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Pickup: ${ride.pickupAddress}",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Drop: ${ride.dropAddress}",
                style = MaterialTheme.typography.bodySmall.copy(color = Slate600),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = Slate200)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "₹${ride.finalFare.toInt()} • $dateStr",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                    )
                    if (ride.ratingStars != null) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = YatraaGold, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(2.dp))
                            Text("${ride.ratingStars} ★", style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp))
                        }
                    }
                }

                OutlinedButton(
                    onClick = onBookAgain,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("btn_book_again_${ride.rideId}")
                ) {
                    Icon(Icons.Default.Replay, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Book Again", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun SavedPlacesTabContent(viewModel: YatraaMainViewModel) {
    val savedPlaces by viewModel.savedPlaces.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(
                    text = "Saved Addresses & Quick Destinations",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
            }

            items(savedPlaces) { place ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.setDrop(
                                DelhiLocation(
                                    id = "place_${place.placeId}",
                                    name = place.title,
                                    landmark = place.address,
                                    zone = "Delhi NCR",
                                    lat = place.lat,
                                    lng = place.lng
                                )
                            )
                            viewModel.setCustomerTab(0)
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Surface(
                                shape = CircleShape,
                                color = YatraaSaffronLight,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = when (place.label) {
                                            "HOME" -> Icons.Default.Home
                                            "WORK" -> Icons.Default.Work
                                            else -> Icons.Default.LocationOn
                                        },
                                        contentDescription = null,
                                        tint = YatraaSaffronDark,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = place.title,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = place.address,
                                    style = MaterialTheme.typography.bodySmall.copy(color = Slate600),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        IconButton(onClick = { viewModel.deleteSavedPlace(place.placeId) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Slate400)
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showAddDialog = true },
            containerColor = YatraaSaffronDark,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("fab_add_saved_place")
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Place")
        }

        if (showAddDialog) {
            AddSavedPlaceDialog(
                onDismiss = { showAddDialog = false },
                onAdd = { label, title, address, lat, lng ->
                    viewModel.addSavedPlace(label, title, address, lat, lng)
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
private fun AddSavedPlaceDialog(
    onDismiss: () -> Unit,
    onAdd: (label: String, title: String, address: String, lat: Double, lng: Double) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var label by remember { mutableStateOf("HOME") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Saved Place", fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Place Name (e.g. Gym, Parents' Home)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address / Landmark") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("HOME", "WORK", "OTHER").forEach { l ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (label == l) YatraaSaffronLight else Slate100,
                            border = BorderStroke(1.dp, if (label == l) YatraaSaffronDark else Slate200),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { label = l }
                        ) {
                            Text(
                                text = l,
                                modifier = Modifier.padding(vertical = 6.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && address.isNotBlank()) {
                        onAdd(label, title, address, 28.6129, 77.2295)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = YatraaSaffronDark)
            ) {
                Text("Save Place")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
private fun OffersReferralTabContent(viewModel: YatraaMainViewModel) {
    val coupons by viewModel.activeCoupons.collectAsState()
    val user by viewModel.currentCustomer.collectAsState()
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Referral Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = YatraaNavy),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(YatraaSaffron, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.CardGiftcard, contentDescription = null, tint = Color.Black)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Refer & Earn ₹50 Cash",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = "Share with Delhi friends & earn free rides",
                                style = MaterialTheme.typography.bodySmall.copy(color = YatraaSaffronLight)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF1E293B),
                        border = BorderStroke(1.dp, YatraaSaffronDark)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = user?.referralCode ?: "YATRAA50",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 2.sp,
                                    color = YatraaGold
                                )
                            )
                            Button(
                                onClick = {
                                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(
                                            Intent.EXTRA_TEXT,
                                            "Book affordable Bike & Auto rides across Delhi-NCR with YATRAA! Use my code ${user?.referralCode ?: "YATRAA50"} to get 50% off on your first ride: https://yatraa.app/join"
                                        )
                                    }
                                    context.startActivity(Intent.createChooser(shareIntent, "Share Referral Code"))
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = YatraaSaffronDark),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("btn_share_referral")
                            ) {
                                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Share Code")
                            }
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "Active Delhi-NCR Promo Codes",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
        }

        items(coupons) { cp ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = YatraaSaffronLight
                        ) {
                            Text(
                                text = cp.code,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = YatraaSaffronDark
                                ),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = cp.title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                        Text(text = cp.description, style = MaterialTheme.typography.bodySmall.copy(color = Slate600))
                    }

                    Button(
                        onClick = {
                            viewModel.applyCoupon(cp.code)
                            viewModel.setCustomerTab(0)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = YatraaSaffronDark),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Use")
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileSafetyTabContent(viewModel: YatraaMainViewModel) {
    val user by viewModel.currentCustomer.collectAsState()
    val context = LocalContext.current
    var showSupportDialog by remember { mutableStateOf(false) }

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
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = YatraaSaffronLight,
                        modifier = Modifier.size(54.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = (user?.name?.take(1) ?: "A"),
                                fontWeight = FontWeight.Black,
                                fontSize = 24.sp,
                                color = YatraaSaffronDark
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = user?.name ?: "Aarav Sharma",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = user?.phone ?: "+91 98112 34567",
                            style = MaterialTheme.typography.bodySmall.copy(color = Slate600)
                        )
                        Text(
                            text = "Wallet: ₹${user?.walletBalance?.toInt() ?: 240} • Language: ${user?.language ?: "English"}",
                            style = MaterialTheme.typography.labelSmall.copy(color = YatraaEmerald, fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "Safety & Emergency Settings",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Emergency Contact", fontWeight = FontWeight.Bold)
                            Text(
                                text = "${user?.emergencyContactName ?: "Sunita Sharma"} (${user?.emergencyContactPhone ?: "+91 98112 99887"})",
                                style = MaterialTheme.typography.bodySmall.copy(color = Slate600)
                            )
                        }
                        IconButton(
                            onClick = {
                                val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${user?.emergencyContactPhone ?: "9811299887"}"))
                                context.startActivity(dialIntent)
                            }
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = "Call Emergency Contact", tint = YatraaEmerald)
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Slate200)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Safety Center & 112 Access", fontWeight = FontWeight.Bold)
                            Text("Instant dialer & Delhi Police SOS", style = MaterialTheme.typography.bodySmall.copy(color = Slate600))
                        }
                        Button(
                            onClick = { viewModel.toggleSafetyDialog(true) },
                            colors = ButtonDefaults.buttonColors(containerColor = YatraaCoral),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Open SOS")
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "Help & Support Tickets",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Have an issue with your trip or fare?",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { showSupportDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = YatraaNavy),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("btn_raise_support_ticket")
                    ) {
                        Icon(Icons.Default.SupportAgent, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Raise Support Dispute / Ticket")
                    }
                }
            }
        }
    }

    if (showSupportDialog) {
        CreateSupportTicketModal(
            onDismiss = { showSupportDialog = false },
            onSubmit = { category, description ->
                viewModel.createSupportTicket(category, description)
                showSupportDialog = false
            }
        )
    }
}

@Composable
private fun CreateSupportTicketModal(
    onDismiss: () -> Unit,
    onSubmit: (TicketCategory, String) -> Unit
) {
    var category by remember { mutableStateOf(TicketCategory.FARE_DISPUTE) }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Submit Yatraa Support Ticket", fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Select Issue Category:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(6.dp))

                TicketCategory.entries.take(4).forEach { cat ->
                    val isSel = category == cat
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSel) YatraaSaffronLight else Slate100,
                        border = BorderStroke(1.dp, if (isSel) YatraaSaffronDark else Slate200),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp)
                            .clickable { category = cat }
                    ) {
                        Text(
                            text = cat.label,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal),
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Describe what happened...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (description.isNotBlank()) {
                        onSubmit(category, description)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = YatraaSaffronDark)
            ) {
                Text("Submit Ticket")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
