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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ElectricRickshaw
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Moped
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.example.yatraa.model.CustomerScreen
import com.example.yatraa.model.PaymentStatus
import com.example.yatraa.model.VehicleCategory
import com.example.yatraa.ui.YatraaMainViewModel

/**
 * Screen 18: Ride Completed Screen
 */
@Composable
fun CustomerRideCompletedScreen(
    viewModel: YatraaMainViewModel,
    modifier: Modifier = Modifier
) {
    val completedRide by viewModel.lastCompletedRide.collectAsState()

    Surface(
        modifier = modifier
            .fillMaxSize()
            .testTag("customer_ride_completed_screen"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(24.dp))

                // Success Badge
                Surface(
                    modifier = Modifier.size(80.dp),
                    shape = CircleShape,
                    color = YatraaEmerald.copy(alpha = 0.15f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            tint = YatraaEmerald,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "You have Arrived!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = YatraaNavy
                )

                Text(
                    text = "Hope you enjoyed your Yatraa trip",
                    fontSize = 14.sp,
                    color = Slate600,
                    modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                )

                // Trip Summary Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = Slate100),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("FINAL PAYABLE FARE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate600, letterSpacing = 1.sp)
                                Text(
                                    text = "₹${completedRide?.finalFare?.toInt() ?: 50}",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Black,
                                    color = YatraaSaffronDark
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = YatraaEmerald.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "PAID VIA ${completedRide?.paymentMethod?.name ?: "CASH"}",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = YatraaEmerald
                                )
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp), color = Slate200)

                        // Stats Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TripStatItem(label = "Distance", value = "${completedRide?.distanceKm ?: 5.2} km")
                            TripStatItem(label = "Duration", value = "${completedRide?.durationMin ?: 16} mins")
                            TripStatItem(label = "Driver", value = completedRide?.driverName ?: "Verified Partner")
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp), color = Slate200)

                        // Locations
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(YatraaEmerald))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(completedRide?.pickupAddress ?: "Pickup Location", fontSize = 12.sp, color = Slate800, maxLines = 1)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(YatraaCoral))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(completedRide?.dropAddress ?: "Drop Location", fontSize = 12.sp, color = Slate800, maxLines = 1)
                        }
                    }
                }
            }

            Column(modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)) {
                Button(
                    onClick = { viewModel.navigateToCustomerScreen(CustomerScreen.PAYMENT_RESULT) },
                    colors = ButtonDefaults.buttonColors(containerColor = YatraaSaffronDark),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("view_payment_receipt_btn")
                ) {
                    Text("View Payment Receipt", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = { viewModel.navigateToCustomerScreen(CustomerScreen.RATING) },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("rate_driver_btn")
                ) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = YatraaGold, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Rate Driver & Leave Feedback", fontWeight = FontWeight.Bold, color = YatraaNavy, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
private fun TripStatItem(label: String, value: String) {
    Column {
        Text(label, fontSize = 11.sp, color = Slate600)
        Text(value, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = YatraaNavy)
    }
}

/**
 * Screen 19: Payment Result Screen
 */
@Composable
fun CustomerPaymentResultScreen(
    viewModel: YatraaMainViewModel,
    modifier: Modifier = Modifier
) {
    val completedRide by viewModel.lastCompletedRide.collectAsState()

    Surface(
        modifier = modifier
            .fillMaxSize()
            .testTag("customer_payment_result_screen"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(48.dp),
                        shape = CircleShape,
                        color = YatraaEmerald.copy(alpha = 0.2f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = YatraaEmerald, modifier = Modifier.size(28.dp))
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Payment Successful", fontSize = 20.sp, fontWeight = FontWeight.Black, color = YatraaNavy)
                        Text("Transaction ID: TXN-${completedRide?.rideId ?: "48291"}", fontSize = 12.sp, color = Slate600)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Receipt Breakdown Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = Slate100),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("DIGITAL TAX INVOICE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate600, letterSpacing = 1.sp)
                        Spacer(modifier = Modifier.height(12.dp))

                        ReceiptLine(title = "Trip Base Fare", amount = "₹${completedRide?.baseFare?.toInt() ?: 25}")
                        ReceiptLine(title = "Distance Charge (${completedRide?.distanceKm ?: 5.0} km)", amount = "₹${completedRide?.distanceCharge?.toInt() ?: 35}")
                        ReceiptLine(title = "Time Charge (${completedRide?.durationMin ?: 15} mins)", amount = "₹${completedRide?.timeCharge?.toInt() ?: 15}")
                        ReceiptLine(title = "Delhi Municipal Cess & Tolls", amount = "₹0 (Included)")

                        if ((completedRide?.discountAmount ?: 0.0) > 0.0) {
                            ReceiptLine(title = "Promotional Discount", amount = "-₹${completedRide?.discountAmount?.toInt()}", isGreen = true)
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Slate200)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total Paid", fontWeight = FontWeight.Black, fontSize = 16.sp, color = YatraaNavy)
                            Text("₹${completedRide?.finalFare?.toInt() ?: 50}", fontWeight = FontWeight.Black, fontSize = 20.sp, color = YatraaSaffronDark)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Payment Mode", fontSize = 12.sp, color = Slate600)
                            Text(completedRide?.paymentMethod?.displayName ?: "Cash on Drop", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = YatraaNavy)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Safety & Fair Pricing Guarantee Banner
                Card(
                    colors = CardDefaults.cardColors(containerColor = YatraaSaffronLight.copy(alpha = 0.35f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Receipt, contentDescription = null, tint = YatraaSaffronDark, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "GST & Delhi Passenger Welfare charges are paid. A copy of this receipt has been emailed.",
                            fontSize = 12.sp,
                            color = YatraaNavy
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(bottom = 16.dp)) {
                Button(
                    onClick = { viewModel.navigateToCustomerScreen(CustomerScreen.RATING) },
                    colors = ButtonDefaults.buttonColors(containerColor = YatraaSaffronDark),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("continue_to_rating_btn")
                ) {
                    Text("Rate Driver & Finish", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }
}

@Composable
private fun ReceiptLine(title: String, amount: String, isGreen: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, fontSize = 13.sp, color = if (isGreen) YatraaEmerald else Slate600)
        Text(amount, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = if (isGreen) YatraaEmerald else YatraaNavy)
    }
}

/**
 * Screen 20: Rating & Feedback Screen
 */
@Composable
fun CustomerRatingFeedbackScreen(
    viewModel: YatraaMainViewModel,
    modifier: Modifier = Modifier
) {
    val completedRide by viewModel.lastCompletedRide.collectAsState()
    val ratingStars by viewModel.ratingStars.collectAsState()
    val ratingComment by viewModel.ratingComment.collectAsState()
    val selectedTags by viewModel.selectedRatingTags.collectAsState()
    val tipAmount by viewModel.driverTipAmount.collectAsState()

    val complimentTags = listOf(
        "Polite Driver",
        "Clean Vehicle",
        "Safe Driving",
        "Punctual",
        "Great Route",
        "Comfortable Ride"
    )

    val tipOptions = listOf(0, 10, 20, 50, 100)

    Surface(
        modifier = modifier
            .fillMaxSize()
            .testTag("customer_rating_feedback_screen"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Rate Your Driver",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = YatraaNavy
                )

                Text(
                    text = "How was your ride with ${completedRide?.driverName ?: "Yatraa Partner"}?",
                    fontSize = 13.sp,
                    color = Slate600,
                    modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
                )

                // 5-Star Rating Selector
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 1..5) {
                        IconButton(
                            onClick = { viewModel.setRatingStars(i) },
                            modifier = Modifier
                                .size(44.dp)
                                .testTag("star_rating_$i")
                        ) {
                            Icon(
                                imageVector = if (i <= ratingStars) Icons.Default.Star else Icons.Outlined.Star,
                                contentDescription = "$i Stars",
                                tint = if (i <= ratingStars) YatraaGold else Slate400,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Compliment Badges
                Text("WHAT WENT WELL?", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate600, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        complimentTags.take(3).forEach { tag ->
                            val isSelected = selectedTags.contains(tag)
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) YatraaSaffronDark else Slate100,
                                border = if (isSelected) null else BorderStroke(1.dp, Slate200),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { viewModel.toggleRatingTag(tag) }
                            ) {
                                Text(
                                    text = tag,
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
                                    fontSize = 11.sp,
                                    textAlign = TextAlign.Center,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else Slate800,
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        complimentTags.drop(3).forEach { tag ->
                            val isSelected = selectedTags.contains(tag)
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) YatraaSaffronDark else Slate100,
                                border = if (isSelected) null else BorderStroke(1.dp, Slate200),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { viewModel.toggleRatingTag(tag) }
                            ) {
                                Text(
                                    text = tag,
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
                                    fontSize = 11.sp,
                                    textAlign = TextAlign.Center,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else Slate800,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Optional Driver Tip
                Text("ADD DRIVER TIP (OPTIONAL)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate600, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    tipOptions.forEach { tip ->
                        val isSelected = tipAmount == tip
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) YatraaEmerald else Slate100,
                            border = if (isSelected) null else BorderStroke(1.dp, Slate200),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.setDriverTip(tip) }
                        ) {
                            Text(
                                text = if (tip == 0) "No Tip" else "+₹$tip",
                                modifier = Modifier.padding(vertical = 8.dp),
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else Slate800
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Additional Feedback Comments
                OutlinedTextField(
                    value = ratingComment,
                    onValueChange = { viewModel.setRatingComment(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("rating_comment_input"),
                    placeholder = { Text("Write extra comments for your driver partner...", color = Slate400, fontSize = 13.sp) },
                    shape = RoundedCornerShape(12.dp),
                    minLines = 2,
                    maxLines = 3
                )
            }

            Column(modifier = Modifier.padding(top = 20.dp, bottom = 16.dp)) {
                Button(
                    onClick = { viewModel.submitCustomerRatingAndFinish() },
                    colors = ButtonDefaults.buttonColors(containerColor = YatraaSaffronDark),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("submit_rating_btn")
                ) {
                    Text("Submit Feedback & Done", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }
}

/**
 * Screen 21: Ride Details / Past Trip Receipt & Support Screen
 */
@Composable
fun CustomerRideDetailsScreen(
    viewModel: YatraaMainViewModel,
    modifier: Modifier = Modifier
) {
    val ride by viewModel.selectedPastRide.collectAsState()

    Surface(
        modifier = modifier
            .fillMaxSize()
            .testTag("customer_ride_details_screen"),
        color = MaterialTheme.colorScheme.background
    ) {
        if (ride == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No ride selected", color = Slate600)
            }
        } else {
            val r = ride!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { viewModel.navigateBackCustomer() }) {
                            Icon(Icons.Default.ArrowForward, contentDescription = "Back", tint = YatraaNavy)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Trip Summary & Receipt",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = YatraaNavy
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Vehicle & Status Header
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Slate100),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (r.vehicleCategory == VehicleCategory.BIKE) Icons.Default.Moped else Icons.Default.ElectricRickshaw,
                                        contentDescription = null,
                                        tint = YatraaSaffronDark,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(r.vehicleCategory.displayName, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = YatraaNavy)
                                        Text("Trip #${r.rideId}", fontSize = 12.sp, color = Slate600)
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (r.rideStatus == com.example.yatraa.model.RideStatus.COMPLETED) Color(0xFFDCFCE7) else Slate200
                                ) {
                                    Text(
                                        text = r.rideStatus.displayLabel,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (r.rideStatus == com.example.yatraa.model.RideStatus.COMPLETED) Color(0xFF15803D) else YatraaCoral
                                    )
                                }
                            }

                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Slate200)

                            // Route
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(YatraaEmerald))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(r.pickupAddress, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = YatraaNavy)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(YatraaCoral))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(r.dropAddress, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = YatraaNavy)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Driver info
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Slate100),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                modifier = Modifier.size(44.dp),
                                shape = CircleShape,
                                color = YatraaNavy
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text((r.driverName?.take(1) ?: "D"), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(r.driverName ?: "Verified Partner", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = YatraaNavy)
                                Text("${r.vehicleModel ?: "Delhi Vehicle"} • ${r.vehicleNumber ?: "DL 1R"}", fontSize = 12.sp, color = Slate600)
                            }
                            if (r.ratingStars != null) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Star, contentDescription = null, tint = YatraaGold, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("${r.ratingStars} ★", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = YatraaNavy)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Price Breakdown
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Slate100),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("FARE BREAKDOWN", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate600, letterSpacing = 1.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            ReceiptLine(title = "Base Fare", amount = "₹${r.baseFare.toInt()}")
                            ReceiptLine(title = "Distance (${r.distanceKm} km)", amount = "₹${r.distanceCharge.toInt()}")
                            ReceiptLine(title = "Time (${r.durationMin} mins)", amount = "₹${r.timeCharge.toInt()}")
                            if (r.discountAmount > 0.0) {
                                ReceiptLine(title = "Discount Applied", amount = "-₹${r.discountAmount.toInt()}", isGreen = true)
                            }
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Slate200)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Total Paid", fontWeight = FontWeight.Black, fontSize = 15.sp, color = YatraaNavy)
                                Text("₹${r.finalFare.toInt()}", fontWeight = FontWeight.Black, fontSize = 18.sp, color = YatraaSaffronDark)
                            }
                        }
                    }
                }

                Column(modifier = Modifier.padding(top = 16.dp)) {
                    Button(
                        onClick = { viewModel.bookAgainRide(r) },
                        colors = ButtonDefaults.buttonColors(containerColor = YatraaSaffronDark),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("book_again_details_btn")
                    ) {
                        Text("Book this Trip Again", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}
