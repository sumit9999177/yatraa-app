package com.example.yatraa.ui.customer

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricRickshaw
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Moped
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import com.example.yatraa.model.CustomerScreen
import com.example.yatraa.model.DelhiLocation
import com.example.yatraa.model.PaymentMethod
import com.example.yatraa.model.RideStatus
import com.example.yatraa.model.VehicleCategory
import com.example.yatraa.ui.YatraaMainViewModel
import com.example.yatraa.ui.components.DelhiMapCanvas
import com.example.yatraa.ui.components.VehicleOptionCard

/**
 * Screen 6: Destination Search Screen
 */
@Composable
fun CustomerDestinationSearchScreen(
    viewModel: YatraaMainViewModel,
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val pickup by viewModel.pickupLocation.collectAsState()
    val savedPlaces by viewModel.savedPlaces.collectAsState()

    var selectedFilterCategory by remember { mutableStateOf("All") }
    val filterCategories = listOf("All", "Metro", "Office/IT", "Airports", "Markets")

    val filteredLocations = remember(searchQuery, selectedFilterCategory) {
        DelhiNcrData.LOCATIONS.filter { loc ->
            val matchQuery = searchQuery.isBlank() || loc.name.contains(searchQuery, ignoreCase = true) || loc.landmark.contains(searchQuery, ignoreCase = true)
            val matchCategory = when (selectedFilterCategory) {
                "Metro" -> loc.name.contains("Metro", true) || loc.landmark.contains("Metro", true)
                "Office/IT" -> loc.name.contains("Cyber", true) || loc.name.contains("Noida", true) || loc.landmark.contains("Hub", true) || loc.landmark.contains("Tech", true)
                "Airports" -> loc.name.contains("Airport", true) || loc.name.contains("Railway", true)
                "Markets" -> loc.name.contains("Chowk", true) || loc.name.contains("Mall", true) || loc.name.contains("Market", true) || loc.name.contains("Saket", true)
                else -> true
            }
            matchQuery && matchCategory
        }
    }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .testTag("customer_destination_search_screen"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header Bar
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { viewModel.navigateBackCustomer() },
                            modifier = Modifier.testTag("search_back_btn")
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = YatraaNavy)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Set Destination",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = YatraaNavy
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Current Pickup Pill
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Slate100,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.NearMe, contentDescription = null, tint = YatraaEmerald, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Pickup: ${pickup.name}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Slate800,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Search Destination Text Field
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.setSearchQuery(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("destination_search_input"),
                        leadingIcon = {
                            Icon(Icons.Default.Place, contentDescription = null, tint = YatraaCoral)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear", tint = Slate400)
                                }
                            }
                        },
                        placeholder = { Text("Search Delhi-NCR landmarks or areas", color = Slate400, fontSize = 14.sp) },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = YatraaSaffron,
                            unfocusedBorderColor = Slate200,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Slate100
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Category Filter Chips
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filterCategories) { cat ->
                            val isSelected = selectedFilterCategory == cat
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) YatraaSaffronDark else Slate100,
                                border = if (isSelected) null else BorderStroke(1.dp, Slate200),
                                modifier = Modifier.clickable { selectedFilterCategory = cat }
                            ) {
                                Text(
                                    text = cat,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else Slate800
                                )
                            }
                        }
                    }
                }
            }

            // Search Results List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Saved Places Shortcuts
                if (searchQuery.isBlank() && savedPlaces.isNotEmpty()) {
                    item {
                        Text(
                            text = "SAVED PLACES",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate400,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(top = 12.dp, bottom = 6.dp)
                        )
                    }

                    items(savedPlaces) { place ->
                        Card(
                            onClick = {
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
                                viewModel.navigateToCustomerScreen(CustomerScreen.PICKUP_DROP_CONFIRM)
                            },
                            colors = CardDefaults.cardColors(containerColor = Slate100),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    modifier = Modifier.size(36.dp),
                                    shape = CircleShape,
                                    color = YatraaSaffron.copy(alpha = 0.2f)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = if (place.label == "HOME") Icons.Default.Home else if (place.label == "WORK") Icons.Default.Work else Icons.Default.LocationOn,
                                            contentDescription = null,
                                            tint = YatraaSaffronDark,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(place.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = YatraaNavy)
                                    Text(place.address, fontSize = 12.sp, color = Slate600, maxLines = 1)
                                }
                            }
                        }
                    }

                    item {
                        Text(
                            text = "POPULAR DELHI-NCR HUBS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate400,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(top = 16.dp, bottom = 6.dp)
                        )
                    }
                }

                items(filteredLocations) { loc ->
                    Card(
                        onClick = {
                            viewModel.setDrop(loc)
                            viewModel.navigateToCustomerScreen(CustomerScreen.PICKUP_DROP_CONFIRM)
                        },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Slate200),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .testTag("destination_item_${loc.id}")
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                modifier = Modifier.size(40.dp),
                                shape = CircleShape,
                                color = YatraaSaffronLight.copy(alpha = 0.4f)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = if (loc.name.contains("Metro")) Icons.Default.Train else Icons.Default.Place,
                                        contentDescription = null,
                                        tint = YatraaSaffronDark,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = loc.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = YatraaNavy
                                )
                                Text(
                                    text = "${loc.landmark} • ${loc.zone}",
                                    fontSize = 12.sp,
                                    color = Slate600,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Slate400, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}

/**
 * Screen 7 & 8: Pickup & Destination Confirmation + Vehicle Selection (Bike vs Auto)
 */
@Composable
fun CustomerPickupDropConfirmScreen(
    viewModel: YatraaMainViewModel,
    modifier: Modifier = Modifier
) {
    val pickup by viewModel.pickupLocation.collectAsState()
    val drop by viewModel.dropLocation.collectAsState()
    val selectedCategory by viewModel.selectedVehicleCategory.collectAsState()
    val estimates by viewModel.fareEstimates.collectAsState()
    val onlineDrivers by viewModel.onlineDrivers.collectAsState()

    val currentEstimate = estimates[selectedCategory]

    Surface(
        modifier = modifier
            .fillMaxSize()
            .testTag("customer_route_confirm_screen"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header Bar
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { viewModel.navigateBackCustomer() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = YatraaNavy)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Confirm Route & Select Ride",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = YatraaNavy
                    )
                }
            }

            // Map Preview
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                DelhiMapCanvas(
                    modifier = Modifier.fillMaxSize(),
                    pickupLocation = pickup,
                    dropLocation = drop,
                    activeDrivers = onlineDrivers,
                    rideStatus = RideStatus.IDLE
                )

                // Route summary floating pill
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 16.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = YatraaNavy.copy(alpha = 0.92f),
                    shadowElevation = 6.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Navigation, contentDescription = null, tint = YatraaSaffron, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${currentEstimate?.distanceKm ?: 5.2} km • ~${currentEstimate?.durationMinutes ?: 15} mins",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            // Bottom Confirmation & Vehicle Selection Sheet
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                tonalElevation = 8.dp,
                shadowElevation = 16.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // Pickup & Drop Address Box with Swap button
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Slate100,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(YatraaEmerald)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = pickup.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = YatraaNavy,
                                        maxLines = 1
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .padding(start = 3.dp, top = 2.dp, bottom = 2.dp)
                                        .width(2.dp)
                                        .height(16.dp)
                                        .background(Slate400.copy(alpha = 0.5f))
                                )

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(YatraaCoral)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = drop.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = YatraaNavy,
                                        maxLines = 1
                                    )
                                }
                            }

                            IconButton(
                                onClick = { viewModel.swapPickupAndDrop() },
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color.White, CircleShape)
                                    .testTag("swap_route_btn")
                            ) {
                                Icon(Icons.Default.SwapVert, contentDescription = "Swap", tint = YatraaSaffronDark)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "CHOOSE VEHICLE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate600,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Vehicle Options: Bike & Auto
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        VehicleOptionCard(
                            category = VehicleCategory.BIKE,
                            estimate = estimates[VehicleCategory.BIKE],
                            isSelected = selectedCategory == VehicleCategory.BIKE,
                            onSelect = { viewModel.selectVehicleCategory(VehicleCategory.BIKE) },
                            modifier = Modifier.weight(1f)
                        )

                        VehicleOptionCard(
                            category = VehicleCategory.AUTO,
                            estimate = estimates[VehicleCategory.AUTO],
                            isSelected = selectedCategory == VehicleCategory.AUTO,
                            onSelect = { viewModel.selectVehicleCategory(VehicleCategory.AUTO) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Fare Breakdown Teaser & CTA
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "₹${currentEstimate?.totalFare?.toInt() ?: 45}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black,
                                color = YatraaNavy
                            )
                            TextButton(
                                onClick = { viewModel.navigateToCustomerScreen(CustomerScreen.FARE_ESTIMATE_DETAILS) },
                                modifier = Modifier.padding(0.dp)
                            ) {
                                Text(
                                    text = "View Transparent Fare Details ⓘ",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = YatraaSaffronDark
                                )
                            }
                        }

                        Button(
                            onClick = { viewModel.navigateToCustomerScreen(CustomerScreen.CONFIRM_RIDE) },
                            colors = ButtonDefaults.buttonColors(containerColor = YatraaSaffronDark),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .height(50.dp)
                                .testTag("continue_to_confirm_ride_btn")
                        ) {
                            Text("Continue", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}

/**
 * Screen 9: Fare Estimate Screen / Transparent Breakdown Details
 */
@Composable
fun CustomerFareEstimateDetailsScreen(
    viewModel: YatraaMainViewModel,
    modifier: Modifier = Modifier
) {
    val selectedCategory by viewModel.selectedVehicleCategory.collectAsState()
    val estimates by viewModel.fareEstimates.collectAsState()
    val coupon by viewModel.selectedCouponCode.collectAsState()
    val pickup by viewModel.pickupLocation.collectAsState()
    val drop by viewModel.dropLocation.collectAsState()

    val estimate = estimates[selectedCategory]

    Surface(
        modifier = modifier
            .fillMaxSize()
            .testTag("customer_fare_estimate_screen"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { viewModel.navigateBackCustomer() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = YatraaNavy)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Transparent Fare Breakdown",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = YatraaNavy
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Vehicle Badge Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = YatraaNavy),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier.size(48.dp),
                            shape = CircleShape,
                            color = YatraaSaffron
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = if (selectedCategory == VehicleCategory.BIKE) Icons.Default.Moped else Icons.Default.ElectricRickshaw,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(selectedCategory.displayName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(
                                text = "${estimate?.distanceKm ?: 5.0} km • ~${estimate?.durationMinutes ?: 15} min travel",
                                color = Slate400,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "FARE CALCULATION (ZERO SURGE GUARANTEE)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate600,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = Slate100),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        FareRow(title = "Base Fare", subtitle = "Initial pickup & first km", amount = "₹${estimate?.baseFare?.toInt() ?: 25}")
                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Slate200)

                        FareRow(
                            title = "Distance Rate",
                            subtitle = "${estimate?.distanceKm ?: 5.0} km × ₹${if (selectedCategory == VehicleCategory.BIKE) "7.5" else "11.5"}/km",
                            amount = "₹${estimate?.distanceCharge?.toInt() ?: 35}"
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Slate200)

                        FareRow(
                            title = "Time Charge",
                            subtitle = "${estimate?.durationMinutes ?: 15} mins × ₹1.0/min",
                            amount = "₹${estimate?.timeCharge?.toInt() ?: 15}"
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Slate200)

                        FareRow(
                            title = "Delhi Municipal & Platform Fee",
                            subtitle = "Transparent 10% platform fee",
                            amount = "Included"
                        )

                        if ((estimate?.discount ?: 0.0) > 0.0) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Slate200)
                            FareRow(
                                title = "Coupon Discount (${coupon ?: "DELHI"})",
                                subtitle = "Promotional offer applied",
                                amount = "-₹${estimate?.discount?.toInt()}",
                                isDiscount = true
                            )
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = YatraaSaffron.copy(alpha = 0.5f))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Total Estimated Fare", fontWeight = FontWeight.Black, fontSize = 16.sp, color = YatraaNavy)
                                Text("No hidden meter charges", fontSize = 11.sp, color = YatraaEmerald, fontWeight = FontWeight.Bold)
                            }
                            Text(
                                text = "₹${estimate?.totalFare?.toInt() ?: 50}",
                                fontWeight = FontWeight.Black,
                                fontSize = 24.sp,
                                color = YatraaSaffronDark
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Driver Economics Highlight
                Card(
                    colors = CardDefaults.cardColors(containerColor = YatraaSaffronLight.copy(alpha = 0.35f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = YatraaSaffronDark, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "90% of your fare goes directly to the driver partner without exploitative commission.",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = YatraaNavy
                        )
                    }
                }
            }

            Button(
                onClick = { viewModel.navigateToCustomerScreen(CustomerScreen.CONFIRM_RIDE) },
                colors = ButtonDefaults.buttonColors(containerColor = YatraaSaffronDark),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .padding(top = 16.dp)
                    .testTag("fare_details_continue_btn")
            ) {
                Text("Proceed to Confirm Ride", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }
    }
}

@Composable
private fun FareRow(
    title: String,
    subtitle: String,
    amount: String,
    isDiscount: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = if (isDiscount) YatraaEmerald else YatraaNavy)
            Text(subtitle, fontSize = 11.sp, color = Slate600)
        }
        Text(
            text = amount,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = if (isDiscount) YatraaEmerald else YatraaNavy
        )
    }
}

/**
 * Screen 10: Confirm Ride Screen
 */
@Composable
fun CustomerConfirmRideScreen(
    viewModel: YatraaMainViewModel,
    modifier: Modifier = Modifier
) {
    val pickup by viewModel.pickupLocation.collectAsState()
    val drop by viewModel.dropLocation.collectAsState()
    val selectedCategory by viewModel.selectedVehicleCategory.collectAsState()
    val estimates by viewModel.fareEstimates.collectAsState()
    val selectedPayment by viewModel.selectedPaymentMethod.collectAsState()
    val selectedCoupon by viewModel.selectedCouponCode.collectAsState()
    val coupons by viewModel.activeCoupons.collectAsState()
    val specialNotes by viewModel.specialInstructions.collectAsState()

    val estimate = estimates[selectedCategory]
    var showCouponDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .testTag("customer_confirm_ride_screen"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { viewModel.navigateBackCustomer() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = YatraaNavy)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Review & Confirm Ride",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = YatraaNavy
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Ride Route Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = Slate100),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                modifier = Modifier.size(36.dp),
                                shape = CircleShape,
                                color = YatraaSaffron
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = if (selectedCategory == VehicleCategory.BIKE) Icons.Default.Moped else Icons.Default.ElectricRickshaw,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(selectedCategory.displayName, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = YatraaNavy)
                                Text(
                                    text = "Estimated Fare: ₹${estimate?.totalFare?.toInt() ?: 50} • ~${estimate?.durationMinutes ?: 15} mins",
                                    fontSize = 12.sp,
                                    color = YatraaSaffronDark,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Slate200)

                        // Pickup
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(YatraaEmerald))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Pickup: ${pickup.name}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = YatraaNavy)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Drop
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(YatraaCoral))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Destination: ${drop.name}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = YatraaNavy)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Payment Method Selector
                Text("PAYMENT METHOD", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate600, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(8.dp))

                PaymentMethod.entries.forEach { method ->
                    val isSelected = selectedPayment == method
                    Card(
                        onClick = { viewModel.setPaymentMethod(method) },
                        colors = CardDefaults.cardColors(containerColor = if (isSelected) YatraaSaffronLight.copy(alpha = 0.35f) else Color.White),
                        border = BorderStroke(1.dp, if (isSelected) YatraaSaffronDark else Slate200),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { viewModel.setPaymentMethod(method) },
                                colors = RadioButtonDefaults.colors(selectedColor = YatraaSaffronDark)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(method.displayName, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = YatraaNavy)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Coupon / Promo Code Selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("OFFERS & PROMOS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate600, letterSpacing = 1.sp)
                    if (selectedCoupon != null) {
                        TextButton(onClick = { viewModel.applyCoupon(null) }) {
                            Text("Remove", fontSize = 11.sp, color = YatraaCoral, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))

                Card(
                    onClick = { showCouponDialog = true },
                    colors = CardDefaults.cardColors(containerColor = Slate100),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocalOffer, contentDescription = null, tint = YatraaSaffronDark, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (selectedCoupon != null) "Applied: $selectedCoupon (₹${estimate?.discount?.toInt()} Saved)" else "Select Promo Coupon",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (selectedCoupon != null) YatraaEmerald else YatraaNavy
                            )
                        }
                        Text("Change >", fontSize = 12.sp, color = YatraaSaffronDark, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Note to Driver
                Text("NOTE TO DRIVER (OPTIONAL)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate600, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = specialNotes,
                    onValueChange = { viewModel.setSpecialInstructions(it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("e.g. Near Metro Gate 2 / Call when reached", color = Slate400, fontSize = 12.sp) },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )
            }

            // Bottom CTA
            Button(
                onClick = {
                    viewModel.requestAndBookRide()
                    viewModel.navigateToCustomerScreen(CustomerScreen.ACTIVE_RIDE)
                },
                colors = ButtonDefaults.buttonColors(containerColor = YatraaSaffronDark),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .padding(top = 16.dp)
                    .testTag("book_yatraa_ride_btn")
            ) {
                Text("Confirm & Request Ride (₹${estimate?.totalFare?.toInt() ?: 50})", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }
    }

    // Coupon Selection Dialog
    if (showCouponDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showCouponDialog = false },
            title = { Text("Select Delhi Promo Offer", fontWeight = FontWeight.Bold, color = YatraaNavy) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    coupons.forEach { c ->
                        Card(
                            onClick = {
                                viewModel.applyCoupon(c.code)
                                showCouponDialog = false
                            },
                            colors = CardDefaults.cardColors(containerColor = Slate100),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(c.code, fontWeight = FontWeight.Black, color = YatraaSaffronDark, fontSize = 14.sp)
                                    Text("${c.discountPercent}% OFF", fontWeight = FontWeight.Bold, color = YatraaEmerald, fontSize = 12.sp)
                                }
                                Text(c.description, fontSize = 11.sp, color = Slate600, modifier = Modifier.padding(top = 2.dp))
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCouponDialog = false }) {
                    Text("Close", color = YatraaNavy, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}
