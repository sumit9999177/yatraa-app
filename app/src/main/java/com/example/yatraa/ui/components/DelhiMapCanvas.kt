package com.example.yatraa.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.yatraa.data.DelhiNcrData
import com.example.yatraa.data.local.DriverEntity
import com.example.yatraa.model.DelhiLocation
import com.example.yatraa.model.RideStatus
import com.example.yatraa.model.VehicleCategory

@Composable
fun DelhiMapCanvas(
    modifier: Modifier = Modifier,
    pickupLocation: DelhiLocation?,
    dropLocation: DelhiLocation?,
    activeDrivers: List<DriverEntity> = emptyList(),
    rideStatus: RideStatus = RideStatus.IDLE,
    rideProgressFraction: Float = 0f,
    onSelectLocation: ((DelhiLocation) -> Unit)? = null
) {
    // Delhi geographic bounds
    val minLat = 28.42
    val maxLat = 28.70
    val minLng = 77.02
    val maxLng = 77.40

    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    val infiniteTransition = rememberInfiniteTransition(label = "map_animations")
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 8f,
        targetValue = 28f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAlpha"
    )

    fun latLngToScreen(lat: Double, lng: Double, width: Float, height: Float): Offset {
        val normX = ((lng - minLng) / (maxLng - minLng)).toFloat()
        val normY = (1f - ((lat - minLat) / (maxLat - minLat))).toFloat()

        val baseX = normX * width
        val baseY = normY * height

        val centerX = width / 2f
        val centerY = height / 2f

        val finalX = centerX + (baseX - centerX + offsetX) * scale
        val finalY = centerY + (baseY - centerY + offsetY) * scale

        return Offset(finalX, finalY)
    }

    Box(
        modifier = modifier
            .background(Color(0xFFF1F5F9))
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale = (scale * zoom).coerceIn(0.8f, 3.5f)
                    offsetX += pan.x / scale
                    offsetY += pan.y / scale
                }
            }
            .pointerInput(Unit) {
                detectTapGestures { tapOffset ->
                    if (onSelectLocation != null) {
                        // Interactive tap
                    }
                }
            }
            .testTag("delhi_map_canvas")
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // 1. Draw Background Map Grid & Roads
            drawDelhiRoadNetwork(width, height, scale, offsetX, offsetY)

            // 2. Draw Yamuna River
            drawYamunaRiver(width, height, scale, offsetX, offsetY)

            // 3. Draw Metro Lines (Yellow Line & Blue Line)
            drawDelhiMetroCorridors(width, height, scale, offsetX, offsetY)

            // 4. Draw Service Area Bounds / Landmarks
            for (loc in DelhiNcrData.LOCATIONS) {
                val pt = latLngToScreen(loc.lat, loc.lng, width, height)
                val isPickup = pickupLocation?.id == loc.id
                val isDrop = dropLocation?.id == loc.id

                if (!isPickup && !isDrop) {
                    // Draw subtle landmark node
                    drawCircle(
                        color = Color(0xFF64748B),
                        radius = 4f * scale.coerceAtLeast(1f),
                        center = pt
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 2f * scale.coerceAtLeast(1f),
                        center = pt
                    )
                }
            }

            // 5. Draw Active Route Line if both pickup & drop exist
            if (pickupLocation != null && dropLocation != null) {
                val pPt = latLngToScreen(pickupLocation.lat, pickupLocation.lng, width, height)
                val dPt = latLngToScreen(dropLocation.lat, dropLocation.lng, width, height)

                // Route curve with realistic mid-point
                val midLat = (pickupLocation.lat + dropLocation.lat) / 2 + 0.012
                val midLng = (pickupLocation.lng + dropLocation.lng) / 2 - 0.015
                val mPt = latLngToScreen(midLat, midLng, width, height)

                val routePath = Path().apply {
                    moveTo(pPt.x, pPt.y)
                    quadraticTo(mPt.x, mPt.y, dPt.x, dPt.y)
                }

                // Route glow
                drawPath(
                    path = routePath,
                    color = YatraaSaffron.copy(alpha = 0.35f),
                    style = Stroke(
                        width = 10f * scale.coerceAtLeast(1f),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )

                // Main route line
                drawPath(
                    path = routePath,
                    color = YatraaSaffronDark,
                    style = Stroke(
                        width = 5f * scale.coerceAtLeast(1f),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round,
                        pathEffect = if (rideStatus == RideStatus.SEARCHING_DRIVER) {
                            PathEffect.dashPathEffect(floatArrayOf(15f, 10f), 0f)
                        } else null
                    )
                )

                // Moving vehicle indicator along route if in progress
                if (rideStatus == RideStatus.IN_PROGRESS || rideStatus == RideStatus.DRIVER_ARRIVING) {
                    val t = rideProgressFraction.coerceIn(0f, 1f)
                    // Quadratic Bezier point: (1-t)^2 * P0 + 2(1-t)t * P1 + t^2 * P2
                    val curX = (1 - t) * (1 - t) * pPt.x + 2 * (1 - t) * t * mPt.x + t * t * dPt.x
                    val curY = (1 - t) * (1 - t) * pPt.y + 2 * (1 - t) * t * mPt.y + t * t * dPt.y
                    val vehiclePos = Offset(curX, curY)

                    // Vehicle pulse
                    drawCircle(
                        color = YatraaSaffron.copy(alpha = 0.4f),
                        radius = 16f * scale.coerceAtLeast(1f),
                        center = vehiclePos
                    )
                    drawCircle(
                        color = YatraaNavy,
                        radius = 9f * scale.coerceAtLeast(1f),
                        center = vehiclePos
                    )
                    drawCircle(
                        color = YatraaGold,
                        radius = 4f * scale.coerceAtLeast(1f),
                        center = vehiclePos
                    )
                }
            }

            // 6. Draw Live Nearby Drivers (Bikes / Autos)
            for (driver in activeDrivers) {
                val dPos = latLngToScreen(driver.currentLat, driver.currentLng, width, height)
                val isBike = driver.vehicleCategory == VehicleCategory.BIKE

                // Vehicle halo
                drawCircle(
                    color = if (isBike) Color(0xFF0284C7).copy(alpha = 0.25f) else Color(0xFF16A34A).copy(alpha = 0.25f),
                    radius = 12f * scale.coerceAtLeast(1f),
                    center = dPos
                )
                // Vehicle Body
                drawCircle(
                    color = if (isBike) Color(0xFF0284C7) else Color(0xFF16A34A),
                    radius = 6f * scale.coerceAtLeast(1f),
                    center = dPos
                )
                drawCircle(
                    color = Color.White,
                    radius = 2.5f * scale.coerceAtLeast(1f),
                    center = dPos
                )
            }

            // 7. Draw Pickup Marker (Green with radar pulse)
            pickupLocation?.let { pLoc ->
                val pPos = latLngToScreen(pLoc.lat, pLoc.lng, width, height)

                // Pulse ring
                drawCircle(
                    color = YatraaEmerald.copy(alpha = pulseAlpha),
                    radius = pulseRadius * scale.coerceAtLeast(1f),
                    center = pPos
                )
                // Outer ring
                drawCircle(
                    color = YatraaEmerald,
                    radius = 8f * scale.coerceAtLeast(1f),
                    center = pPos
                )
                // Inner white
                drawCircle(
                    color = Color.White,
                    radius = 4f * scale.coerceAtLeast(1f),
                    center = pPos
                )
            }

            // 8. Draw Drop Marker (Red with pin)
            dropLocation?.let { dLoc ->
                val dPos = latLngToScreen(dLoc.lat, dLoc.lng, width, height)

                // Drop Outer ring
                drawCircle(
                    color = YatraaCoral,
                    radius = 8f * scale.coerceAtLeast(1f),
                    center = dPos
                )
                // Drop Inner
                drawCircle(
                    color = Color.White,
                    radius = 3.5f * scale.coerceAtLeast(1f),
                    center = dPos
                )
            }
        }

        // Floating Map Controls (Zoom +, Zoom -, Recenter)
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            SmallFloatingActionButton(
                onClick = { scale = (scale * 1.3f).coerceAtMost(3.5f) },
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                elevation = FloatingActionButtonDefaults.elevation(3.dp),
                shape = CircleShape,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .testTag("map_zoom_in")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Zoom In", modifier = Modifier.size(18.dp))
            }

            SmallFloatingActionButton(
                onClick = { scale = (scale / 1.3f).coerceAtLeast(0.8f) },
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                elevation = FloatingActionButtonDefaults.elevation(3.dp),
                shape = CircleShape,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .testTag("map_zoom_out")
            ) {
                Icon(Icons.Default.Remove, contentDescription = "Zoom Out", modifier = Modifier.size(18.dp))
            }

            SmallFloatingActionButton(
                onClick = {
                    scale = 1f
                    offsetX = 0f
                    offsetY = 0f
                },
                containerColor = YatraaSaffron,
                contentColor = Color.Black,
                elevation = FloatingActionButtonDefaults.elevation(3.dp),
                shape = CircleShape,
                modifier = Modifier.testTag("map_recenter")
            ) {
                Icon(Icons.Default.MyLocation, contentDescription = "Recenter GPS", modifier = Modifier.size(18.dp))
            }
        }

        // Live Zone Status Badge at Top Left
        Surface(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(YatraaEmerald, CircleShape)
                )
                Text(
                    text = " Delhi-NCR Live Fleet",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }
    }
}

private fun DrawScope.drawDelhiRoadNetwork(
    width: Float,
    height: Float,
    scale: Float,
    offsetX: Float,
    offsetY: Float
) {
    val roadColor = Color(0xFFCBD5E1)
    val highwayColor = Color(0xFFE2E8F0)

    val centerX = width * 0.48f + offsetX * scale
    val centerY = height * 0.45f + offsetY * scale

    drawCircle(
        color = highwayColor,
        radius = 120f * scale,
        center = Offset(centerX, centerY),
        style = Stroke(width = 8f * scale)
    )

    drawCircle(
        color = highwayColor,
        radius = 210f * scale,
        center = Offset(centerX, centerY),
        style = Stroke(width = 6f * scale)
    )

    // Expressways
    drawLine(
        color = Color(0xFF94A3B8),
        start = Offset(centerX - 180f * scale, centerY + 240f * scale),
        end = Offset(centerX, centerY),
        strokeWidth = 5f * scale,
        cap = StrokeCap.Round
    )

    drawLine(
        color = Color(0xFF94A3B8),
        start = Offset(centerX + 260f * scale, centerY + 200f * scale),
        end = Offset(centerX, centerY),
        strokeWidth = 5f * scale,
        cap = StrokeCap.Round
    )

    drawLine(
        color = roadColor,
        start = Offset(width, centerY - 20f * scale),
        end = Offset(centerX, centerY),
        strokeWidth = 4f * scale
    )

    drawLine(
        color = roadColor,
        start = Offset(centerX - 40f * scale, 0f),
        end = Offset(centerX, centerY),
        strokeWidth = 4f * scale
    )
}

private fun DrawScope.drawYamunaRiver(
    width: Float,
    height: Float,
    scale: Float,
    offsetX: Float,
    offsetY: Float
) {
    val riverColor = Color(0xFFBAE6FD)
    val riverPath = Path().apply {
        val startX = width * 0.62f + offsetX * scale
        val startY = 0f + offsetY * scale
        moveTo(startX, startY)
        cubicTo(
            startX + 20f * scale, height * 0.3f,
            startX - 40f * scale, height * 0.65f,
            startX + 50f * scale, height
        )
    }

    drawPath(
        path = riverPath,
        color = riverColor,
        style = Stroke(width = 16f * scale, cap = StrokeCap.Round)
    )
}

private fun DrawScope.drawDelhiMetroCorridors(
    width: Float,
    height: Float,
    scale: Float,
    offsetX: Float,
    offsetY: Float
) {
    val centerX = width * 0.48f + offsetX * scale
    val centerY = height * 0.45f + offsetY * scale

    val yellowPath = Path().apply {
        moveTo(centerX - 30f * scale, 20f)
        lineTo(centerX, centerY)
        lineTo(centerX - 120f * scale, centerY + 220f * scale)
    }
    drawPath(
        path = yellowPath,
        color = Color(0xFFEAB308).copy(alpha = 0.55f),
        style = Stroke(width = 3f * scale, pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f))
    )

    val bluePath = Path().apply {
        moveTo(centerX - 200f * scale, centerY + 90f * scale)
        lineTo(centerX, centerY)
        lineTo(centerX + 220f * scale, centerY + 50f * scale)
    }
    drawPath(
        path = bluePath,
        color = Color(0xFF2563EB).copy(alpha = 0.55f),
        style = Stroke(width = 3f * scale, pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f))
    )
}
