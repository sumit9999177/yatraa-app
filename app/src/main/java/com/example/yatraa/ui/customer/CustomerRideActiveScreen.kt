package com.example.yatraa.ui.customer

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricRickshaw
import androidx.compose.material.icons.filled.LocalPolice
import androidx.compose.material.icons.filled.Moped
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.yatraa.data.DelhiNcrData
import com.example.yatraa.data.local.RideEntity
import com.example.yatraa.model.DelhiLocation
import com.example.yatraa.model.RideStatus
import com.example.yatraa.model.VehicleCategory
import com.example.yatraa.ui.YatraaMainViewModel
import com.example.yatraa.ui.components.DelhiMapCanvas
import com.example.yatraa.ui.components.RatingAndFeedbackDialog
import com.example.yatraa.ui.components.SafetyCenterDialog

@Composable
fun CustomerRideActiveScreen(
    ride: RideEntity,
    viewModel: YatraaMainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val progressFraction by viewModel.rideProgressFraction.collectAsState()
    val showSafety by viewModel.showSafetyDialog.collectAsState()
    val showRating by viewModel.showRatingDialog.collectAsState()

    val pLoc = DelhiLocation(
        id = "p",
        name = ride.pickupAddress,
        landmark = "Pickup Point",
        zone = "Delhi NCR",
        lat = ride.pickupLat,
        lng = ride.pickupLng
    )
    val dLoc = DelhiLocation(
        id = "d",
        name = ride.dropAddress,
        landmark = "Destination",
        zone = "Delhi NCR",
        lat = ride.dropLat,
        lng = ride.dropLng
    )

    Box(modifier = modifier.fillMaxSize()) {
        // 1. Live Delhi Navigation Map
        DelhiMapCanvas(
            modifier = Modifier.fillMaxSize(),
            pickupLocation = pLoc,
            dropLocation = dLoc,
            rideStatus = ride.rideStatus,
            rideProgressFraction = progressFraction
        )

        // 2. Upper Floating Status Banner
        Surface(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 12.dp, start = 14.dp, end = 14.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 6.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(
                                color = when (ride.rideStatus) {
                                    RideStatus.SEARCHING_DRIVER -> YatraaSaffronDark
                                    RideStatus.DRIVER_ARRIVING, RideStatus.DRIVER_ARRIVED -> YatraaGold
                                    RideStatus.IN_PROGRESS, RideStatus.OTP_VERIFIED -> YatraaEmerald
                                    RideStatus.COMPLETED -> YatraaEmerald
                                    else -> YatraaCoral
                                },
                                shape = CircleShape
                            )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = ride.rideStatus.displayLabel,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Trip ID: ${ride.rideId}",
                            style = MaterialTheme.typography.labelSmall.copy(color = Slate600)
                        )
                    }
                }

                // Safety Trigger Icon
                IconButton(
                    onClick = { viewModel.toggleSafetyDialog(true) },
                    modifier = Modifier.testTag("active_ride_safety_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Safety Center",
                        tint = YatraaCoral
                    )
                }
            }
        }

        // 3. Dynamic Bottom Action & Info Sheet based on Ride State Machine
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(12.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                when (ride.rideStatus) {
                    RideStatus.SEARCHING_DRIVER -> {
                        SearchingDriverView(
                            ride = ride,
                            onCancel = { viewModel.cancelActiveRide("User cancelled during search") }
                        )
                    }

                    RideStatus.DRIVER_ASSIGNED, RideStatus.DRIVER_ARRIVING, RideStatus.DRIVER_ARRIVED -> {
                        DriverAssignedView(
                            ride = ride,
                            onCallDriver = {
                                val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${ride.driverPhone ?: "9958012345"}"))
                                context.startActivity(callIntent)
                            },
                            onCancel = { viewModel.cancelActiveRide("Cancelled before pickup") }
                        )
                    }

                    RideStatus.OTP_VERIFIED, RideStatus.IN_PROGRESS -> {
                        LiveRideInProgressView(
                            ride = ride,
                            progressFraction = progressFraction,
                            onEmergency = { viewModel.toggleSafetyDialog(true) }
                        )
                    }

                    RideStatus.COMPLETED -> {
                        TripCompletedView(
                            ride = ride,
                            onRateRide = { viewModel.toggleRatingDialog(true) }
                        )
                    }

                    RideStatus.CANCELLED_BY_CUSTOMER, RideStatus.CANCELLED_BY_DRIVER, RideStatus.CANCELLED_BY_SYSTEM -> {
                        TripCancelledView(
                            ride = ride,
                            onDismiss = { viewModel.cancelActiveRide() }
                        )
                    }

                    else -> {
                        Text("Ready for next trip")
                    }
                }
            }
        }

        // Safety Dialog
        if (showSafety) {
            SafetyCenterDialog(
                onDismiss = { viewModel.toggleSafetyDialog(false) },
                rideInfo = "Trip ${ride.rideId} with ${ride.driverName ?: "Partner"} (${ride.vehicleNumber ?: "DL"}) to ${ride.dropAddress}"
            )
        }

        // Rating Dialog
        if (showRating) {
            RatingAndFeedbackDialog(
                driverName = ride.driverName ?: "Driver Partner",
                vehicleInfo = "${ride.vehicleCategory.displayName} • ${ride.vehicleNumber ?: "DL-1R"}",
                fareAmount = ride.finalFare,
                onDismiss = { viewModel.toggleRatingDialog(false) },
                onSubmitRating = { stars, comment ->
                    viewModel.submitRating(stars, comment)
                }
            )
        }
    }
}

@Composable
private fun SearchingDriverView(
    ride: RideEntity,
    onCancel: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "radar")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "radar_pulse"
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(YatraaSaffronLight, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = YatraaSaffronDark,
                strokeWidth = 3.dp,
                modifier = Modifier.size(64.dp)
            )
            Icon(
                imageVector = when (ride.vehicleCategory) {
                    VehicleCategory.BIKE -> Icons.Default.Moped
                    VehicleCategory.AUTO -> Icons.Default.ElectricRickshaw
                    VehicleCategory.CAB -> Icons.Default.DirectionsCar
                },
                contentDescription = null,
                tint = YatraaSaffronDark,
                modifier = Modifier.size(30.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Connecting with nearby ${ride.vehicleCategory.displayName}s...",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Checking verified drivers near ${ride.pickupAddress}",
            style = MaterialTheme.typography.bodySmall.copy(color = Slate600),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(18.dp))

        OutlinedButton(
            onClick = onCancel,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("btn_cancel_search")
        ) {
            Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Cancel Ride Request")
        }
    }
}

@Composable
private fun DriverAssignedView(
    ride: RideEntity,
    onCallDriver: () -> Unit,
    onCancel: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // High-Visibility OTP Banner
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = YatraaSaffronLight,
            border = BorderStroke(1.dp, YatraaSaffronDark)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "START RIDE PIN / OTP",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Black,
                            color = YatraaSaffronDark,
                            fontSize = 10.sp
                        )
                    )
                    Text(
                        text = "Share with driver at pickup",
                        style = MaterialTheme.typography.bodySmall.copy(color = Slate600, fontSize = 11.sp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = YatraaNavy
                ) {
                    Text(
                        text = ride.otpCode,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 4.sp,
                            color = YatraaGold
                        ),
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Driver Profile Card
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Surface(
                    shape = CircleShape,
                    color = Slate100,
                    modifier = Modifier.size(50.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = (ride.driverName?.take(1) ?: "D"),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = YatraaNavy
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = ride.driverName ?: "Rajesh Kumar",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.VerifiedUser,
                            contentDescription = "Verified Partner",
                            tint = YatraaEmerald,
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    Text(
                        text = "${ride.vehicleModel ?: "Bajaj RE Auto"} • 4.88 ★",
                        style = MaterialTheme.typography.bodySmall.copy(color = Slate600)
                    )

                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Slate100,
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Text(
                            text = ride.vehicleNumber ?: "DL 1R AB 4592",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = YatraaNavy,
                                fontSize = 11.sp
                            ),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                        )
                    }
                }
            }

            // Call Driver Button
            Surface(
                shape = CircleShape,
                color = YatraaEmerald,
                modifier = Modifier
                    .size(44.dp)
                    .clickable(onClick = onCallDriver)
                    .testTag("btn_call_driver")
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Call Driver",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Pickup / Drop route
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Slate100, RoundedCornerShape(10.dp))
                .padding(10.dp)
        ) {
            Text(
                text = "Pickup: ${ride.pickupAddress}",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Drop: ${ride.dropAddress}",
                style = MaterialTheme.typography.bodySmall.copy(color = Slate600),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier
                    .weight(1f)
                    .testTag("btn_cancel_assigned_ride"),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Cancel Ride")
            }
        }
    }
}

@Composable
private fun LiveRideInProgressView(
    ride: RideEntity,
    progressFraction: Float,
    onEmergency: () -> Unit
) {
    val estimatedSpeed = (28 + (progressFraction * 14).toInt())
    val remainingDistance = (ride.distanceKm * (1f - progressFraction)).coerceAtLeast(0.1)

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Riding to Destination",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "${ride.vehicleCategory.displayName} • ${ride.vehicleNumber ?: "DL-1R"}",
                    style = MaterialTheme.typography.bodySmall.copy(color = Slate600)
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = YatraaNavy,
                modifier = Modifier.padding(4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = null,
                        tint = YatraaGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$estimatedSpeed km/h",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Live Trip Progress Bar
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Progress: ${(progressFraction * 100).toInt()}%",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = String.format("%.1f km remaining", remainingDistance),
                    style = MaterialTheme.typography.labelSmall.copy(color = Slate600)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { progressFraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = YatraaSaffronDark,
                trackColor = Slate200
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Safety Center Banner Button
        Button(
            onClick = onEmergency,
            colors = ButtonDefaults.buttonColors(containerColor = YatraaCoral),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("btn_in_ride_emergency_sos")
        ) {
            Icon(Icons.Default.Security, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Emergency SOS & Share Live Trip", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun TripCompletedView(
    ride: RideEntity,
    onRateRide: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(Color(0xFFDCFCE7), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Completed",
                tint = YatraaEmerald,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "You've Arrived!",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black)
        )
        Text(
            text = "Thank you for riding with Yatraa Delhi-NCR",
            style = MaterialTheme.typography.bodySmall.copy(color = Slate600)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Fare Receipt Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Slate100)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total Final Fare", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = "₹${ride.finalFare.toInt()}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black)
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Slate200)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Payment Status", style = MaterialTheme.typography.bodySmall.copy(color = Slate600))
                    Text(
                        text = "${ride.paymentMethod.displayName} (Paid ✓)",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = YatraaEmerald
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onRateRide,
            colors = ButtonDefaults.buttonColors(containerColor = YatraaSaffronDark),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("btn_open_rating_dialog")
        ) {
            Icon(Icons.Default.Star, contentDescription = null, tint = YatraaGold)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Rate Driver & Finish", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun TripCancelledView(
    ride: RideEntity,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Cancelled",
            tint = YatraaCoral,
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Ride Cancelled",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = ride.cancellationReason ?: "This ride has ended.",
            style = MaterialTheme.typography.bodySmall.copy(color = Slate600),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Book Another Ride")
        }
    }
}
