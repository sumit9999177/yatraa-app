package com.example.yatraa.domain

import com.example.yatraa.data.DelhiNcrData
import com.example.yatraa.data.local.DriverEntity
import com.example.yatraa.model.DriverCandidate
import com.example.yatraa.model.VehicleCategory
import kotlin.math.roundToInt

object DriverMatchingEngine {

    fun rankAndFindCandidates(
        pickupLat: Double,
        pickupLng: Double,
        category: VehicleCategory,
        drivers: List<DriverEntity>
    ): List<DriverCandidate> {
        val matchingDrivers = drivers.filter {
            it.isOnline &&
            it.verificationStatus == com.example.yatraa.model.DriverVerificationStatus.VERIFIED &&
            it.vehicleCategory == category
        }

        return matchingDrivers.map { driver ->
            val dist = DelhiNcrData.calculateDistanceKm(
                driver.currentLat,
                driver.currentLng,
                pickupLat,
                pickupLng
            )
            // Estimated time for driver to reach passenger pickup (at ~20km/h in Delhi traffic)
            val etaMinutes = (dist * 2.5).roundToInt().coerceAtLeast(1).coerceAtMost(12)
            DriverCandidate(
                driverId = driver.driverId,
                name = driver.name,
                phone = driver.phone,
                rating = driver.rating,
                vehicleCategory = driver.vehicleCategory,
                vehicleNumber = driver.vehicleNumber,
                vehicleModel = driver.vehicleModel,
                currentLat = driver.currentLat,
                currentLng = driver.currentLng,
                etaMinutes = etaMinutes,
                distanceToPickupKm = Math.round(dist * 10.0) / 10.0
            )
        }.sortedBy { it.distanceToPickupKm }
    }
}
