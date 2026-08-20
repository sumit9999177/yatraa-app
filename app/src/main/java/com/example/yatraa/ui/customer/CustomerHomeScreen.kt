package com.example.yatraa.ui.customer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import com.example.yatraa.model.DelhiLocation
import com.example.yatraa.model.PaymentMethod
import com.example.yatraa.model.RideStatus
import com.example.yatraa.model.VehicleCategory
import com.example.yatraa.ui.YatraaMainViewModel
import com.example.yatraa.ui.components.DelhiMapCanvas
import com.example.yatraa.ui.components.VehicleOptionCard

@Composable
fun CustomerHomeScreen(
    viewModel: YatraaMainViewModel,
    modifier: Modifier = Modifier
) {
    val pickup by viewModel.pickupLocation.collectAsState()
    val drop by viewModel.dropLocation.collectAsState()
    val onlineDrivers by viewModel.onlineDrivers.collectAsState()
    val selectedCategory by viewModel.selectedVehicleCategory.collectAsState()
    val estimates by viewModel.fareEstimates.collectAsState()
    val selectedCoupon by viewModel.selectedCouponCode.collectAsState()
    val selectedPayment by viewModel.selectedPaymentMethod.collectAsState()
    val activeRide by viewModel.activeCustomerRide.collectAsState()
    val isSearching by viewModel.isSearchingDestination.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val coupons by viewModel.activeCoupons.collectAsState()
    val savedPlaces by viewModel.savedPlaces.collectAsState()

    var showCouponSelector by remember { mutableStateOf(false) }
    var showPaymentSelector by remember { mutableStateOf(false) }

    val currentEstimate = estimates[selectedCategory]

    Box(modifier = modifier.fillMaxSize()) {
        // 1. Live Delhi Interactive Map Canvas
        DelhiMapCanvas(
            modifier = Modifier.fillMaxSize(),
            pickupLocation = pickup,
            dropLocation = drop,
            activeDrivers = onlineDrivers,
            rideStatus = activeRide?.rideStatus ?: RideStatus.IDLE
        )

        // 2. Upper Search & Quick Destination Bar
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            // Destination Search Pill Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.openDestinationSearch(true) }
                    .testTag("customer_destination_search_bar"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    // Pickup Row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(YatraaEmerald, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "PICKUP LOCATION",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate400
                                )
                            )
                            Text(
                                text = pickup.name,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                        color = Slate200
                    )

                    // Drop Row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(YatraaCoral, RoundedCornerShape(2.dp))
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "WHERE TO?",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = YatraaSaffronDark
                                )
                            )
                            Text(
                                text = drop.name,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = YatraaSaffronDark
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Quick Landmark / Saved Shortcut Chips
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    QuickChip(
                        title = "Home",
                        icon = Icons.Default.Home,
                        onClick = {
                            val home = savedPlaces.firstOrNull { it.label == "HOME" }
                            if (home != null) {
                                viewModel.setDrop(
                                    DelhiLocation(
                                        id = "saved_home",
                                        name = home.title,
                                        landmark = home.address,
                                        zone = "Delhi NCR",
                                        lat = home.lat,
                                        lng = home.lng
                                    )
                                )
                            } else {
                                viewModel.setDrop(DelhiNcrData.LOCATIONS[0])
                            }
                        }
                    )
                }
                item {
                    QuickChip(
                        title = "Cyber Hub",
                        icon = Icons.Default.Work,
                        onClick = { viewModel.setDrop(DelhiNcrData.LOCATIONS[4]) }
                    )
                }
                item {
                    QuickChip(
                        title = "Airport T3",
                        icon = Icons.Default.Place,
                        onClick = { viewModel.setDrop(DelhiNcrData.LOCATIONS[8]) }
                    )
                }
                item {
                    QuickChip(
                        title = "Noida Sec 18",
                        icon = Icons.Default.NearMe,
                        onClick = { viewModel.setDrop(DelhiNcrData.LOCATIONS[6]) }
                    )
                }
            }
        }

        // 3. Bottom Booking Sheet
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Trip overview summary (Distance & Duration)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Choose Vehicle",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        if (currentEstimate != null) {
                            Text(
                                text = "• ${currentEstimate.distanceKm} km (~${currentEstimate.durationMinutes} min)",
                                style = MaterialTheme.typography.bodySmall.copy(color = Slate600)
                            )
                        }
                    }

                    // Safety Shield Trigger
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFEE2E2),
                        modifier = Modifier
                            .clickable { viewModel.toggleSafetyDialog(true) }
                            .testTag("btn_safety_center_trigger")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = "Safety",
                                tint = YatraaCoral,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Safety",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = YatraaCoral
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Vehicle Options: Bike & Auto & Cab preview
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    VehicleOptionCard(
                        category = VehicleCategory.BIKE,
                        estimate = estimates[VehicleCategory.BIKE],
                        isSelected = selectedCategory == VehicleCategory.BIKE,
                        onSelect = { viewModel.selectVehicleCategory(VehicleCategory.BIKE) }
                    )
                    VehicleOptionCard(
                        category = VehicleCategory.AUTO,
                        estimate = estimates[VehicleCategory.AUTO],
                        isSelected = selectedCategory == VehicleCategory.AUTO,
                        onSelect = { viewModel.selectVehicleCategory(VehicleCategory.AUTO) }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Promo Coupon & Payment Method Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Payment Method Button
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Slate100,
                        modifier = Modifier
                            .clickable { showPaymentSelector = true }
                            .testTag("btn_select_payment_method")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Payment,
                                contentDescription = null,
                                tint = YatraaNavy,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = when (selectedPayment) {
                                    PaymentMethod.CASH -> "Cash on Drop"
                                    PaymentMethod.UPI -> "UPI / GPay"
                                    PaymentMethod.WALLET -> "Wallet (₹240)"
                                },
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                        }
                    }

                    // Coupon Button
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (selectedCoupon != null) YatraaSaffronLight else Slate100,
                        border = BorderStroke(1.dp, if (selectedCoupon != null) YatraaSaffronDark else Slate200),
                        modifier = Modifier
                            .clickable { showCouponSelector = true }
                            .testTag("btn_apply_coupon")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Percent,
                                contentDescription = null,
                                tint = if (selectedCoupon != null) YatraaSaffronDark else Slate600,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = selectedCoupon ?: "Apply Coupon",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (selectedCoupon != null) YatraaSaffronDark else Slate600
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Primary CTA: Book Ride
                Button(
                    onClick = { viewModel.requestAndBookRide() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("btn_book_ride"),
                    colors = ButtonDefaults.buttonColors(containerColor = YatraaSaffronDark),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Book ${selectedCategory.displayName}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        if (currentEstimate != null) {
                            Text(
                                text = "• ₹${currentEstimate.totalFare.toInt()}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White)
                    }
                }
            }
        }

        // 4. Destination Search Overlay Dialog
        AnimatedVisibility(
            visible = isSearching,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it })
        ) {
            DestinationSearchSheet(
                searchQuery = searchQuery,
                onQueryChange = { viewModel.setSearchQuery(it) },
                onSelectPlace = { loc -> viewModel.setDrop(loc) },
                onClose = { viewModel.openDestinationSearch(false) }
            )
        }

        // 5. Coupon Selection Modal
        if (showCouponSelector) {
            CouponSelectorModal(
                coupons = coupons,
                selectedCoupon = selectedCoupon,
                onSelect = {
                    viewModel.applyCoupon(it)
                    showCouponSelector = false
                },
                onDismiss = { showCouponSelector = false }
            )
        }

        // 6. Payment Selection Modal
        if (showPaymentSelector) {
            PaymentSelectorModal(
                selectedMethod = selectedPayment,
                onSelect = {
                    viewModel.setPaymentMethod(it)
                    showPaymentSelector = false
                },
                onDismiss = { showPaymentSelector = false }
            )
        }
    }
}

@Composable
private fun QuickChip(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, Slate200),
        shadowElevation = 2.dp,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = YatraaSaffronDark, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}

@Composable
private fun DestinationSearchSheet(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onSelectPlace: (DelhiLocation) -> Unit,
    onClose: () -> Unit
) {
    val filtered = DelhiNcrData.LOCATIONS.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
        it.landmark.contains(searchQuery, ignoreCase = true) ||
        it.zone.contains(searchQuery, ignoreCase = true)
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("destination_search_sheet"),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onClose, modifier = Modifier.testTag("btn_close_search")) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
                Text(
                    text = "Select Destination",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = onQueryChange,
                placeholder = { Text("Search Delhi-NCR landmarks, Metro, Cyber City...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = YatraaSaffronDark) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_destination_search"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = YatraaSaffronDark,
                    unfocusedBorderColor = Slate200
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Popular Delhi-NCR Locations",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Slate600
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filtered) { loc ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectPlace(loc) }
                            .testTag("place_item_${loc.id}"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Slate50(MaterialTheme.colorScheme))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = YatraaSaffronLight,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = YatraaSaffronDark,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = loc.name,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "${loc.landmark} • ${loc.zone}",
                                    style = MaterialTheme.typography.bodySmall.copy(color = Slate600),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Slate50(colorScheme: androidx.compose.material3.ColorScheme): Color {
    return if (colorScheme.surface == Color.White) Color(0xFFF8FAFC) else Color(0xFF1E293B)
}

@Composable
private fun CouponSelectorModal(
    coupons: List<com.example.yatraa.data.local.CouponEntity>,
    selectedCoupon: String?,
    onSelect: (String?) -> Unit,
    onDismiss: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CardGiftcard, contentDescription = null, tint = YatraaSaffronDark)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Apply Coupon Code", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                coupons.forEach { cp ->
                    val isApplied = selectedCoupon == cp.code
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { onSelect(if (isApplied) null else cp.code) }
                            .testTag("coupon_card_${cp.code}"),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isApplied) YatraaSaffronLight else Slate100
                        ),
                        border = BorderStroke(1.dp, if (isApplied) YatraaSaffronDark else Slate200)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = cp.code,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Black,
                                        color = YatraaSaffronDark
                                    )
                                )
                                Text(text = cp.title, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                                Text(text = cp.description, style = MaterialTheme.typography.labelSmall.copy(color = Slate600, fontSize = 10.sp))
                            }
                            Text(
                                text = if (isApplied) "Applied ✓" else "Apply",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isApplied) YatraaEmerald else YatraaNavy
                                )
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
private fun PaymentSelectorModal(
    selectedMethod: PaymentMethod,
    onSelect: (PaymentMethod) -> Unit,
    onDismiss: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Select Payment Method", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                PaymentMethod.entries.forEach { method ->
                    val isSelected = selectedMethod == method
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { onSelect(method) }
                            .testTag("payment_method_${method.name.lowercase()}"),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) YatraaSaffronLight else Slate100
                        ),
                        border = BorderStroke(1.dp, if (isSelected) YatraaSaffronDark else Slate200)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = when (method) {
                                    PaymentMethod.CASH -> Icons.Default.Payment
                                    PaymentMethod.UPI -> Icons.Default.Payment
                                    PaymentMethod.WALLET -> Icons.Default.Payment
                                },
                                contentDescription = null,
                                tint = if (isSelected) YatraaSaffronDark else Slate600
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = method.displayName,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Confirm")
            }
        }
    )
}
