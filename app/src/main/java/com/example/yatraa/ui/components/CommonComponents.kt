package com.example.yatraa.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricRickshaw
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.LocalPolice
import androidx.compose.material.icons.filled.Moped
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.ui.theme.YatraaEmeraldLight
import com.example.ui.theme.YatraaGold
import com.example.ui.theme.YatraaNavy
import com.example.ui.theme.YatraaSaffron
import com.example.ui.theme.YatraaSaffronDark
import com.example.ui.theme.YatraaSaffronLight
import com.example.yatraa.model.FareEstimate
import com.example.yatraa.model.UserRole
import com.example.yatraa.model.VehicleCategory

@Composable
fun AppRoleSwitcherBar(
    currentRole: UserRole,
    onRoleSelected: (UserRole) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = YatraaNavy,
        shadowElevation = 6.dp
    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(YatraaSaffron, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Y", fontWeight = FontWeight.Black, color = Color.Black, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "YATRAA",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp,
                            color = Color.White
                        )
                    )
                    Text(
                        text = " • Delhi-NCR",
                        style = MaterialTheme.typography.labelSmall.copy(color = YatraaSaffronLight)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = YatraaSaffron.copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, YatraaSaffron.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = when (currentRole) {
                            UserRole.CUSTOMER -> "Rider View"
                            UserRole.DRIVER -> "Partner View"
                            UserRole.ADMIN -> "Operations Ops"
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = YatraaSaffron,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Role Switcher Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF1E293B))
                    .padding(3.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                RoleTabItem(
                    title = "Passenger",
                    icon = Icons.Default.Person,
                    isSelected = currentRole == UserRole.CUSTOMER,
                    onClick = { onRoleSelected(UserRole.CUSTOMER) },
                    testTag = "tab_role_customer",
                    modifier = Modifier.weight(1f)
                )
                RoleTabItem(
                    title = "Driver Partner",
                    icon = Icons.Default.ElectricRickshaw,
                    isSelected = currentRole == UserRole.DRIVER,
                    onClick = { onRoleSelected(UserRole.DRIVER) },
                    testTag = "tab_role_driver",
                    modifier = Modifier.weight(1.1f)
                )
                RoleTabItem(
                    title = "Admin Panel",
                    icon = Icons.Default.AdminPanelSettings,
                    isSelected = currentRole == UserRole.ADMIN,
                    onClick = { onRoleSelected(UserRole.ADMIN) },
                    testTag = "tab_role_admin",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun RoleTabItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .testTag(testTag),
        color = if (isSelected) YatraaSaffron else Color.Transparent,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) Color.Black else Slate400,
                modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) Color.Black else Slate400,
                    fontSize = 11.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun VehicleOptionCard(
    category: VehicleCategory,
    estimate: FareEstimate?,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isCab = category == VehicleCategory.CAB
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = !isCab, onClick = onSelect)
            .testTag("vehicle_option_${category.name.lowercase()}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) YatraaSaffronLight else MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) YatraaSaffronDark else Slate200
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Surface(
                    shape = CircleShape,
                    color = when (category) {
                        VehicleCategory.BIKE -> Color(0xFFE0F2FE)
                        VehicleCategory.AUTO -> Color(0xFFDCFCE7)
                        VehicleCategory.CAB -> Color(0xFFF3E8FF)
                    },
                    modifier = Modifier.size(46.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = when (category) {
                                VehicleCategory.BIKE -> Icons.Default.Moped
                                VehicleCategory.AUTO -> Icons.Default.ElectricRickshaw
                                VehicleCategory.CAB -> Icons.Default.DirectionsCar
                            },
                            contentDescription = category.displayName,
                            tint = when (category) {
                                VehicleCategory.BIKE -> Color(0xFF0284C7)
                                VehicleCategory.AUTO -> Color(0xFF16A34A)
                                VehicleCategory.CAB -> Color(0xFF7E22CE)
                            },
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = category.displayName,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        if (isCab) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFFF3E8FF)
                            ) {
                                Text(
                                    text = "SOON",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFF7E22CE)
                                    ),
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                    }

                    Text(
                        text = if (estimate != null && !isCab) {
                            "${estimate.etaMinutes} min away • ${category.description}"
                        } else {
                            category.description
                        },
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Slate600,
                            fontSize = 11.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            if (estimate != null && !isCab) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "₹${estimate.totalFare.toInt()}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    if (estimate.discount > 0) {
                        Text(
                            text = "Save ₹${estimate.discount.toInt()}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = YatraaEmerald,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SafetyCenterDialog(
    onDismiss: () -> Unit,
    rideInfo: String? = null
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = "Safety Shield",
                    tint = YatraaCoral,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Yatraa Safety Center", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Your safety is our 1st priority in Delhi-NCR. Access 24x7 emergency tools below:",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Slate600)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Action 1: Police 112 Dial
                Button(
                    onClick = {
                        val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:112"))
                        context.startActivity(dialIntent)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = YatraaCoral),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("safety_dial_112")
                ) {
                    Icon(Icons.Default.LocalPolice, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Call Police Emergency (112)", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Action 2: Share Live Trip via Android Share Sheet
                OutlinedButton(
                    onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "I'm riding with YATRAA in Delhi-NCR! Track my live trip: ${rideInfo ?: "https://yatraa.app/track/live-delhi"}\nEmergency: 112"
                            )
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share Live Trip with Family"))
                    },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("safety_share_trip")
                ) {
                    Icon(Icons.Default.Share, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Share Live Trip with Family")
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Action 3: Yatraa 24x7 Safety Helpline
                OutlinedButton(
                    onClick = {
                        val helplineIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:18001239287"))
                        context.startActivity(helplineIntent)
                    },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("safety_dial_yatraa")
                ) {
                    Icon(Icons.Default.Phone, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Yatraa 24x7 Helpline")
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
fun RatingAndFeedbackDialog(
    driverName: String,
    vehicleInfo: String,
    fareAmount: Double,
    onDismiss: () -> Unit,
    onSubmitRating: (stars: Int, comment: String) -> Unit
) {
    var selectedStars by remember { mutableIntStateOf(5) }
    var comment by remember { mutableStateOf("") }
    val tags = listOf("On-time Pickup", "Polite Driver", "Clean Vehicle", "Safe Driving", "Correct Route")
    val selectedTags = remember { mutableStateOf(setOf<String>()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Rate your Yatraa Ride", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Trip with $driverName ($vehicleInfo)", style = MaterialTheme.typography.bodyMedium.copy(color = Slate600))
                Text("Fare Paid: ₹${fareAmount.toInt()}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = YatraaEmerald))

                Spacer(modifier = Modifier.height(16.dp))

                // 5-Star Row
                Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                    for (i in 1..5) {
                        IconButton(
                            onClick = { selectedStars = i },
                            modifier = Modifier.testTag("rating_star_$i")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "$i Stars",
                                tint = if (i <= selectedStars) YatraaGold else Slate200,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Feedback Tags
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    tags.take(3).forEach { tag ->
                        val isSelected = selectedTags.value.contains(tag)
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) YatraaSaffronLight else Slate100,
                            border = BorderStroke(1.dp, if (isSelected) YatraaSaffronDark else Slate200),
                            modifier = Modifier
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                                .clickable {
                                    val current = selectedTags.value.toMutableSet()
                                    if (isSelected) current.remove(tag) else current.add(tag)
                                    selectedTags.value = current
                                }
                        ) {
                            Text(
                                text = tag,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text("Optional feedback comments...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("rating_comment_input"),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val fullComment = if (selectedTags.value.isNotEmpty()) {
                        "${selectedTags.value.joinToString(", ")}. $comment".trim()
                    } else comment
                    onSubmitRating(selectedStars, fullComment)
                },
                colors = ButtonDefaults.buttonColors(containerColor = YatraaSaffronDark),
                modifier = Modifier.testTag("rating_submit_button")
            ) {
                Text("Submit Review")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Skip")
            }
        }
    )
}
