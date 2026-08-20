package com.example.yatraa.ui.admin

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
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ElectricRickshaw
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Moped
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
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
import com.example.yatraa.data.local.AuditLogEntity
import com.example.yatraa.data.local.CouponEntity
import com.example.yatraa.data.local.DriverEntity
import com.example.yatraa.data.local.PricingConfigEntity
import com.example.yatraa.data.local.RideEntity
import com.example.yatraa.data.local.ServiceZoneEntity
import com.example.yatraa.data.local.SupportTicketEntity
import com.example.yatraa.model.DriverVerificationStatus
import com.example.yatraa.model.RideStatus
import com.example.yatraa.model.TicketStatus
import com.example.yatraa.model.VehicleCategory
import com.example.yatraa.ui.YatraaMainViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdminPanelScreen(
    viewModel: YatraaMainViewModel,
    modifier: Modifier = Modifier
) {
    val activeTab by viewModel.adminTab.collectAsState()

    val totalDrivers by viewModel.totalDriversCount.collectAsState()
    val verifiedDrivers by viewModel.verifiedDriversCount.collectAsState()
    val onlineDrivers by viewModel.onlineDriversCount.collectAsState()
    val activeRides by viewModel.activeRidesCount.collectAsState()
    val completedRides by viewModel.completedRidesCount.collectAsState()
    val totalRevenue by viewModel.totalRevenue.collectAsState()

    val allDrivers by viewModel.allDrivers.collectAsState()
    val allRides by viewModel.allRides.collectAsState()
    val allPricing by viewModel.allPricing.collectAsState()
    val allZones by viewModel.allZones.collectAsState()
    val allCoupons by viewModel.activeCoupons.collectAsState()
    val tickets by viewModel.supportTickets.collectAsState()
    val auditLogs by viewModel.auditLogs.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Admin Operations Header
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = YatraaSaffron)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "YATRAA CENTRAL OPS",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                letterSpacing = 1.sp
                            )
                        )
                        Text(
                            text = "Delhi-NCR Fleet Control & Auditing",
                            style = MaterialTheme.typography.labelSmall.copy(color = Slate400)
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = YatraaEmerald.copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, YatraaEmerald)
                ) {
                    Text(
                        text = "LIVE SYSTEM 200 OK",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = YatraaEmerald,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }

        // Scrollable Admin Sub-tabs
        ScrollableTabRow(
            selectedTabIndex = activeTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = YatraaSaffronDark,
            edgePadding = 8.dp
        ) {
            listOf(
                "Overview" to Icons.Default.Dashboard,
                "Fleet" to Icons.Default.People,
                "Live Rides" to Icons.Default.DirectionsCar,
                "Pricing" to Icons.Default.Tune,
                "Zones" to Icons.Default.Public,
                "Coupons" to Icons.Default.CardGiftcard,
                "Support" to Icons.Default.SupportAgent,
                "Audit Logs" to Icons.Default.History
            ).forEachIndexed { index, (label, icon) ->
                Tab(
                    selected = activeTab == index,
                    onClick = { viewModel.setAdminTab(index) },
                    text = { Text(label, fontWeight = FontWeight.Bold, fontSize = 11.sp) },
                    icon = { Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.testTag("admin_tab_$index")
                )
            }
        }

        when (activeTab) {
            0 -> AdminOverviewTab(
                totalDrivers = totalDrivers,
                verifiedDrivers = verifiedDrivers,
                onlineDrivers = onlineDrivers,
                activeRides = activeRides,
                completedRides = completedRides,
                totalRevenue = totalRevenue ?: 0.0,
                recentRides = allRides.take(5)
            )
            1 -> AdminFleetTab(allDrivers = allDrivers, viewModel = viewModel)
            2 -> AdminLiveRidesTab(allRides = allRides)
            3 -> AdminPricingTab(pricingList = allPricing, viewModel = viewModel)
            4 -> AdminZonesTab(zones = allZones, viewModel = viewModel)
            5 -> AdminCouponsTab(coupons = allCoupons, viewModel = viewModel)
            6 -> AdminSupportTab(tickets = tickets, viewModel = viewModel)
            7 -> AdminAuditLogsTab(logs = auditLogs)
        }
    }
}

@Composable
private fun AdminOverviewTab(
    totalDrivers: Int,
    verifiedDrivers: Int,
    onlineDrivers: Int,
    activeRides: Int,
    completedRides: Int,
    totalRevenue: Double,
    recentRides: List<RideEntity>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("Real-Time Delhi-NCR Platform KPIs", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
        }

        item {
            // KPI Grid
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    KpiCard(
                        title = "Gross Revenue",
                        value = "₹${totalRevenue.toInt()}",
                        subtitle = "Completed Rides",
                        color = YatraaEmerald,
                        modifier = Modifier.weight(1f)
                    )
                    KpiCard(
                        title = "Active In-Flight",
                        value = "$activeRides",
                        subtitle = "Live Trips",
                        color = YatraaSaffronDark,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    KpiCard(
                        title = "Online Fleet",
                        value = "$onlineDrivers / $totalDrivers",
                        subtitle = "$verifiedDrivers Verified",
                        color = Color(0xFF0284C7),
                        modifier = Modifier.weight(1f)
                    )
                    KpiCard(
                        title = "Completed Trips",
                        value = "$completedRides",
                        subtitle = "Delhi-NCR total",
                        color = YatraaNavy,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        item {
            Text("Recent Dispatched Rides", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
        }

        items(recentRides) { r ->
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
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Trip #${r.rideId} • ${r.vehicleCategory.displayName}", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                        Text(text = "${r.pickupAddress} -> ${r.dropAddress}", style = MaterialTheme.typography.labelSmall.copy(color = Slate600), maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    Text(
                        text = "₹${r.finalFare.toInt()}",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
private fun KpiCard(
    title: String,
    value: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(text = title, style = MaterialTheme.typography.labelSmall.copy(color = Slate600))
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Black,
                    color = color
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = subtitle, style = MaterialTheme.typography.labelSmall.copy(color = Slate400, fontSize = 10.sp))
        }
    }
}

@Composable
private fun AdminFleetTab(
    allDrivers: List<DriverEntity>,
    viewModel: YatraaMainViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("Driver Partners Fleet (${allDrivers.size})", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
        }

        items(allDrivers) { d ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_driver_card_${d.driverId}"),
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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = d.name, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                                Spacer(modifier = Modifier.width(4.dp))
                                if (d.verificationStatus == DriverVerificationStatus.VERIFIED) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = YatraaEmerald, modifier = Modifier.size(14.dp))
                                }
                            }
                            Text(text = "${d.vehicleCategory.displayName} • ${d.vehicleNumber} • ${d.phone}", style = MaterialTheme.typography.bodySmall.copy(color = Slate600))
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (d.isOnline) Color(0xFFDCFCE7) else Slate100
                        ) {
                            Text(
                                text = if (d.isOnline) "ONLINE" else "OFFLINE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (d.isOnline) YatraaEmerald else Slate400,
                                    fontSize = 10.sp
                                ),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = Slate200)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Status: ${d.verificationStatus.name}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = when (d.verificationStatus) {
                                    DriverVerificationStatus.VERIFIED -> YatraaEmerald
                                    DriverVerificationStatus.PENDING_DOCS -> YatraaSaffronDark
                                    else -> YatraaCoral
                                }
                            )
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            if (d.verificationStatus != DriverVerificationStatus.VERIFIED) {
                                Button(
                                    onClick = { viewModel.adminUpdateDriverVerification(d.driverId, DriverVerificationStatus.VERIFIED) },
                                    colors = ButtonDefaults.buttonColors(containerColor = YatraaEmerald),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.height(32.dp)
                                ) {
                                    Text("Approve KYC", fontSize = 11.sp)
                                }
                            } else {
                                OutlinedButton(
                                    onClick = { viewModel.adminUpdateDriverVerification(d.driverId, DriverVerificationStatus.SUSPENDED) },
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.height(32.dp)
                                ) {
                                    Text("Suspend", fontSize = 11.sp, color = YatraaCoral)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminLiveRidesTab(allRides: List<RideEntity>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("Live Dispatch & All Rides (${allRides.size})", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
        }

        items(allRides) { r ->
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
                        Text(text = "Trip #${r.rideId}", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                        Text(
                            text = r.rideStatus.displayLabel,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (r.rideStatus == RideStatus.COMPLETED) YatraaEmerald else YatraaSaffronDark
                            )
                        )
                    }
                    Text(text = "Passenger: ${r.customerName} (${r.customerPhone})", style = MaterialTheme.typography.bodySmall.copy(color = Slate600))
                    Text(text = "Driver: ${r.driverName ?: "Awaiting match"} (${r.vehicleNumber ?: "N/A"})", style = MaterialTheme.typography.bodySmall.copy(color = Slate600))
                    Text(text = "Route: ${r.pickupAddress} -> ${r.dropAddress}", style = MaterialTheme.typography.bodySmall)
                    Text(text = "Fare: ₹${r.finalFare.toInt()} (${r.distanceKm} km)", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = YatraaEmerald))
                }
            }
        }
    }
}

@Composable
private fun AdminPricingTab(
    pricingList: List<PricingConfigEntity>,
    viewModel: YatraaMainViewModel
) {
    var editingConfig by remember { mutableStateOf<PricingConfigEntity?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Fare Formula & Dynamic Pricing Rules", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
        }

        items(pricingList) { p ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_pricing_card_${p.category.name.lowercase()}"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (p.category == VehicleCategory.BIKE) Icons.Default.Moped else Icons.Default.ElectricRickshaw,
                                contentDescription = null,
                                tint = YatraaSaffronDark
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Yatraa ${p.category.displayName}", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                        }

                        IconButton(
                            onClick = { editingConfig = p },
                            modifier = Modifier.testTag("btn_edit_pricing_${p.category.name.lowercase()}")
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Rates", tint = YatraaSaffronDark)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("Base Fare", style = MaterialTheme.typography.labelSmall.copy(color = Slate600))
                            Text("₹${p.baseFare.toInt()}", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                        }
                        Column {
                            Text("Per Km Rate", style = MaterialTheme.typography.labelSmall.copy(color = Slate600))
                            Text("₹${p.perKmRate}/km", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                        }
                        Column {
                            Text("Per Min Rate", style = MaterialTheme.typography.labelSmall.copy(color = Slate600))
                            Text("₹${p.perMinuteRate}/min", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                        }
                        Column {
                            Text("Min Fare", style = MaterialTheme.typography.labelSmall.copy(color = Slate600))
                            Text("₹${p.minimumFare.toInt()}", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                        }
                    }
                }
            }
        }
    }

    if (editingConfig != null) {
        EditPricingModal(
            config = editingConfig!!,
            onDismiss = { editingConfig = null },
            onSave = { updated ->
                viewModel.adminUpdatePricing(updated)
                editingConfig = null
            }
        )
    }
}

@Composable
private fun EditPricingModal(
    config: PricingConfigEntity,
    onDismiss: () -> Unit,
    onSave: (PricingConfigEntity) -> Unit
) {
    var baseFare by remember { mutableStateOf(config.baseFare.toString()) }
    var perKmRate by remember { mutableStateOf(config.perKmRate.toString()) }
    var perMinRate by remember { mutableStateOf(config.perMinuteRate.toString()) }
    var minFare by remember { mutableStateOf(config.minimumFare.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Update Pricing: ${config.category.displayName}", fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = baseFare,
                    onValueChange = { baseFare = it },
                    label = { Text("Base Fare (₹)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = perKmRate,
                    onValueChange = { perKmRate = it },
                    label = { Text("Per Km Rate (₹/km)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = perMinRate,
                    onValueChange = { perMinRate = it },
                    label = { Text("Per Minute Rate (₹/min)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = minFare,
                    onValueChange = { minFare = it },
                    label = { Text("Minimum Fare (₹)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updated = config.copy(
                        baseFare = baseFare.toDoubleOrNull() ?: config.baseFare,
                        perKmRate = perKmRate.toDoubleOrNull() ?: config.perKmRate,
                        perMinuteRate = perMinRate.toDoubleOrNull() ?: config.perMinuteRate,
                        minimumFare = minFare.toDoubleOrNull() ?: config.minimumFare
                    )
                    onSave(updated)
                },
                colors = ButtonDefaults.buttonColors(containerColor = YatraaSaffronDark),
                modifier = Modifier.testTag("btn_save_pricing_config")
            ) {
                Text("Save Rates & Audit")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
private fun AdminZonesTab(
    zones: List<ServiceZoneEntity>,
    viewModel: YatraaMainViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("Delhi-NCR Service Operating Zones (${zones.size})", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
        }

        items(zones) { z ->
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
                        Text(text = z.name, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                        Text(text = "Base Surge: ${z.surgeMultiplier}x • Priority Active", style = MaterialTheme.typography.bodySmall.copy(color = Slate600))
                    }

                    Switch(
                        checked = z.isActive,
                        onCheckedChange = { viewModel.adminToggleZone(z.zoneId, it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = YatraaEmerald
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminCouponsTab(
    coupons: List<CouponEntity>,
    viewModel: YatraaMainViewModel
) {
    var showCreateCoupon by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text("Active Promo Campaigns", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
            }

            items(coupons) { cp ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = cp.code, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = YatraaSaffronDark))
                            Text(text = "${cp.discountPercent}% OFF (Max ₹${cp.maxDiscount.toInt()})", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = YatraaEmerald))
                        }
                        Text(text = cp.title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                        Text(text = cp.description, style = MaterialTheme.typography.bodySmall.copy(color = Slate600))
                    }
                }
            }
        }

        Button(
            onClick = { showCreateCoupon = true },
            colors = ButtonDefaults.buttonColors(containerColor = YatraaSaffronDark),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .testTag("btn_create_coupon")
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp))
            Text("Create Promo")
        }

        if (showCreateCoupon) {
            CreateCouponModal(
                onDismiss = { showCreateCoupon = false },
                onSave = {
                    viewModel.adminCreateCoupon(it)
                    showCreateCoupon = false
                }
            )
        }
    }
}

@Composable
private fun CreateCouponModal(
    onDismiss: () -> Unit,
    onSave: (CouponEntity) -> Unit
) {
    var code by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var discountPercent by remember { mutableStateOf("25") }
    var maxDiscount by remember { mutableStateOf("60") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Promo Code", fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = code,
                    onValueChange = { code = it.uppercase() },
                    label = { Text("Promo Code (e.g. MONSOON30)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Campaign Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedTextField(
                        value = discountPercent,
                        onValueChange = { discountPercent = it },
                        label = { Text("Discount %") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = maxDiscount,
                        onValueChange = { maxDiscount = it },
                        label = { Text("Max Cap (₹)") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (code.isNotBlank()) {
                        onSave(
                            CouponEntity(
                                code = code,
                                title = title,
                                description = desc,
                                discountPercent = discountPercent.toIntOrNull() ?: 20,
                                maxDiscount = maxDiscount.toDoubleOrNull() ?: 50.0,
                                minFare = 30.0
                            )
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = YatraaSaffronDark)
            ) {
                Text("Launch Coupon")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
private fun AdminSupportTab(
    tickets: List<SupportTicketEntity>,
    viewModel: YatraaMainViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("Customer & Partner Support Tickets (${tickets.size})", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
        }

        items(tickets) { t ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Ticket #${t.ticketId}", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = if (t.status == TicketStatus.OPEN) Color(0xFFFEE2E2) else Color(0xFFDCFCE7)
                        ) {
                            Text(
                                text = t.status.name,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (t.status == TicketStatus.OPEN) YatraaCoral else YatraaEmerald,
                                    fontSize = 10.sp
                                ),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Category: ${t.category.label}", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                    Text(text = t.description, style = MaterialTheme.typography.bodySmall.copy(color = Slate600))

                    if (t.resolutionNotes != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Resolution: ${t.resolutionNotes}", style = MaterialTheme.typography.labelSmall.copy(color = YatraaEmerald, fontWeight = FontWeight.Bold))
                    }

                    if (t.status == TicketStatus.OPEN) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.adminResolveTicket(t.ticketId, "Resolved & credit applied if eligible.") },
                            colors = ButtonDefaults.buttonColors(containerColor = YatraaEmerald),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text("Resolve Dispute")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminAuditLogsTab(logs: List<AuditLogEntity>) {
    val dateFormatter = SimpleDateFormat("dd MMM HH:mm:ss", Locale.getDefault())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text("System Audit Trail & Security Logs (${logs.size})", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
        }

        items(logs) { l ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = l.action, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black, color = YatraaSaffronDark))
                        Text(text = dateFormatter.format(Date(l.timestamp)), style = MaterialTheme.typography.labelSmall.copy(color = Slate400, fontSize = 10.sp))
                    }
                    Text(text = "Actor: ${l.actor} • Target: ${l.target}", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold))
                    Text(text = "From [${l.previousValue}] -> To [${l.newValue}]", style = MaterialTheme.typography.labelSmall.copy(color = Slate600, fontSize = 11.sp))
                }
            }
        }
    }
}
