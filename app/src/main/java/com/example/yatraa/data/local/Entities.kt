package com.example.yatraa.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.yatraa.model.DriverVerificationStatus
import com.example.yatraa.model.PaymentMethod
import com.example.yatraa.model.PaymentStatus
import com.example.yatraa.model.RideStatus
import com.example.yatraa.model.TicketCategory
import com.example.yatraa.model.TicketStatus
import com.example.yatraa.model.UserRole
import com.example.yatraa.model.VehicleCategory

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val userId: String,
    val name: String,
    val phone: String,
    val role: UserRole,
    val profilePhoto: String? = null,
    val language: String = "English",
    val referralCode: String = "YATRAA50",
    val walletBalance: Double = 150.0,
    val emergencyContactName: String = "Family Contact",
    val emergencyContactPhone: String = "+91 98765 43210",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "drivers")
data class DriverEntity(
    @PrimaryKey val driverId: String,
    val userId: String,
    val name: String,
    val phone: String,
    val vehicleCategory: VehicleCategory,
    val vehicleNumber: String,
    val vehicleModel: String,
    val verificationStatus: DriverVerificationStatus,
    val isOnline: Boolean,
    val currentLat: Double,
    val currentLng: Double,
    val rating: Double,
    val totalRides: Int,
    val acceptanceRate: Int,
    val cancellationRate: Int,
    val dailyEarnings: Double = 0.0,
    val weeklyEarnings: Double = 0.0,
    val activeRideId: String? = null,
    val licenseNumber: String = "DL-142019003892",
    val rcNumber: String = "RC-DEL-9921",
    val policeVerificationDone: Boolean = true
)

@Entity(tableName = "rides")
data class RideEntity(
    @PrimaryKey val rideId: String,
    val customerId: String,
    val customerName: String,
    val customerPhone: String,
    val driverId: String?,
    val driverName: String?,
    val driverPhone: String?,
    val vehicleCategory: VehicleCategory,
    val vehicleNumber: String?,
    val vehicleModel: String?,
    val pickupAddress: String,
    val dropAddress: String,
    val pickupLat: Double,
    val pickupLng: Double,
    val dropLat: Double,
    val dropLng: Double,
    val distanceKm: Double,
    val durationMin: Int,
    val estimatedFare: Double,
    val finalFare: Double,
    val baseFare: Double,
    val distanceCharge: Double,
    val timeCharge: Double,
    val discountAmount: Double = 0.0,
    val couponCode: String? = null,
    val paymentMethod: PaymentMethod = PaymentMethod.CASH,
    val paymentStatus: PaymentStatus = PaymentStatus.PENDING,
    val rideStatus: RideStatus = RideStatus.REQUESTED,
    val otpCode: String = "4821",
    val createdAt: Long = System.currentTimeMillis(),
    val acceptedAt: Long? = null,
    val startedAt: Long? = null,
    val completedAt: Long? = null,
    val ratingStars: Int? = null,
    val ratingComment: String? = null,
    val cancellationReason: String? = null
)

@Entity(tableName = "pricing_config")
data class PricingConfigEntity(
    @PrimaryKey val category: VehicleCategory,
    val baseFare: Double,
    val perKmRate: Double,
    val perMinuteRate: Double,
    val minimumFare: Double,
    val platformCommissionPercent: Double = 10.0,
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "service_zones")
data class ServiceZoneEntity(
    @PrimaryKey val zoneId: String,
    val name: String,
    val region: String,
    val isActive: Boolean = true,
    val surgeMultiplier: Double = 1.0
)

@Entity(tableName = "coupons")
data class CouponEntity(
    @PrimaryKey val code: String,
    val title: String,
    val discountPercent: Int,
    val maxDiscount: Double,
    val minFare: Double,
    val description: String,
    val isActive: Boolean = true
)

@Entity(tableName = "saved_places")
data class SavedPlaceEntity(
    @PrimaryKey(autoGenerate = true) val placeId: Long = 0,
    val label: String, // HOME, WORK, GYM, METRO, OTHER
    val title: String,
    val address: String,
    val lat: Double,
    val lng: Double
)

@Entity(tableName = "support_tickets")
data class SupportTicketEntity(
    @PrimaryKey val ticketId: String,
    val userId: String,
    val userRole: UserRole,
    val rideId: String?,
    val category: TicketCategory,
    val description: String,
    val status: TicketStatus = TicketStatus.OPEN,
    val resolutionNotes: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "audit_logs")
data class AuditLogEntity(
    @PrimaryKey(autoGenerate = true) val logId: Long = 0,
    val actor: String,
    val action: String,
    val target: String,
    val previousValue: String,
    val newValue: String,
    val timestamp: Long = System.currentTimeMillis()
)
