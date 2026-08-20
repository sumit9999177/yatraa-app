package com.example.yatraa.domain

import com.example.yatraa.data.local.CouponEntity
import com.example.yatraa.data.local.PricingConfigEntity
import com.example.yatraa.model.FareEstimate
import com.example.yatraa.model.VehicleCategory
import kotlin.math.roundToInt

object FareEngine {

    fun calculateEstimate(
        category: VehicleCategory,
        pricing: PricingConfigEntity,
        distanceKm: Double,
        zoneSurge: Double = 1.0,
        coupon: CouponEntity? = null
    ): FareEstimate {
        // Average city speeds: Bike ~24 km/h (2.5 min/km), Auto ~18 km/h (3.3 min/km), Cab ~20 km/h (3.0 min/km)
        val minutesPerKm = when (category) {
            VehicleCategory.BIKE -> 2.4
            VehicleCategory.AUTO -> 3.2
            VehicleCategory.CAB -> 3.0
        }
        val durationMinutes = (distanceKm * minutesPerKm).roundToInt().coerceAtLeast(5)
        val etaMinutes = when (category) {
            VehicleCategory.BIKE -> 2 + (distanceKm * 0.4).roundToInt().coerceAtMost(6)
            VehicleCategory.AUTO -> 3 + (distanceKm * 0.5).roundToInt().coerceAtMost(8)
            VehicleCategory.CAB -> 5 + (distanceKm * 0.6).roundToInt().coerceAtMost(10)
        }

        val baseFare = pricing.baseFare
        val distanceCharge = (distanceKm * pricing.perKmRate) * zoneSurge
        val timeCharge = durationMinutes * pricing.perMinuteRate

        val subtotal = (baseFare + distanceCharge + timeCharge).coerceAtLeast(pricing.minimumFare)

        var discount = 0.0
        if (coupon != null && subtotal >= coupon.minFare) {
            val potentialDiscount = (subtotal * (coupon.discountPercent / 100.0))
            discount = potentialDiscount.coerceAtMost(coupon.maxDiscount)
        }

        val totalFare = (subtotal - discount).coerceAtLeast(pricing.minimumFare)

        return FareEstimate(
            category = category,
            baseFare = Math.round(baseFare * 10.0) / 10.0,
            distanceCharge = Math.round(distanceCharge * 10.0) / 10.0,
            timeCharge = Math.round(timeCharge * 10.0) / 10.0,
            discount = Math.round(discount * 10.0) / 10.0,
            totalFare = Math.round(totalFare * 1.0).toDouble(),
            distanceKm = Math.round(distanceKm * 10.0) / 10.0,
            durationMinutes = durationMinutes,
            etaMinutes = etaMinutes
        )
    }
}
