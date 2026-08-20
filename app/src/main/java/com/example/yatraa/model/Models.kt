package com.example.yatraa.model

enum class UserRole {
    CUSTOMER,
    DRIVER,
    ADMIN
}

enum class VehicleCategory(val displayName: String, val capacity: Int, val description: String) {
    BIKE("Yatraa Bike", 1, "Quickest in Delhi traffic • Helmet provided"),
    AUTO("Yatraa Auto", 3, "Comfortable & meter-transparent • Up to 3 seats"),
    CAB("Yatraa Cab", 4, "Air-conditioned sedan (Coming Soon)")
}

enum class RideStatus(val displayLabel: String) {
    IDLE("Ready"),
    REQUESTED("Ride Requested"),
    SEARCHING_DRIVER("Finding Nearest Driver..."),
    DRIVER_ASSIGNED("Driver Assigned"),
    DRIVER_ARRIVING("Driver on the Way"),
    DRIVER_ARRIVED("Driver Arrived at Pickup"),
    OTP_VERIFIED("OTP Verified"),
    IN_PROGRESS("Ride in Progress"),
    COMPLETED("Ride Completed"),
    CANCELLED_BY_CUSTOMER("Cancelled by Passenger"),
    CANCELLED_BY_DRIVER("Cancelled by Driver"),
    CANCELLED_BY_SYSTEM("Cancelled (No Driver Available)")
}

enum class PaymentMethod(val displayName: String) {
    CASH("Cash on Drop"),
    UPI("UPI (GPay / PhonePe / Paytm)"),
    WALLET("Yatraa Wallet")
}

enum class PaymentStatus {
    PENDING,
    COMPLETED,
    FAILED,
    REFUNDED
}

enum class DriverVerificationStatus(val label: String) {
    VERIFIED("Verified Partner"),
    PENDING_DOCS("Verification Pending"),
    REJECTED("Rejected"),
    SUSPENDED("Suspended")
}

enum class TicketCategory(val label: String) {
    FARE_DISPUTE("Fare & Billing Dispute"),
    DRIVER_BEHAVIOR("Driver Conduct & Safety"),
    LOST_ITEM("Lost & Found Item"),
    ROUTE_ISSUE("Incorrect Route Taken"),
    APP_FEEDBACK("App or Payment Glitch")
}

enum class TicketStatus {
    OPEN,
    IN_PROGRESS,
    RESOLVED
}

data class DelhiLocation(
    val id: String,
    val name: String,
    val landmark: String,
    val zone: String,
    val lat: Double,
    val lng: Double,
    val isPopular: Boolean = true
)

data class FareEstimate(
    val category: VehicleCategory,
    val baseFare: Double,
    val distanceCharge: Double,
    val timeCharge: Double,
    val discount: Double,
    val totalFare: Double,
    val distanceKm: Double,
    val durationMinutes: Int,
    val etaMinutes: Int
)

data class DriverCandidate(
    val driverId: String,
    val name: String,
    val phone: String,
    val rating: Double,
    val vehicleCategory: VehicleCategory,
    val vehicleNumber: String,
    val vehicleModel: String,
    val currentLat: Double,
    val currentLng: Double,
    val etaMinutes: Int,
    val distanceToPickupKm: Double
)

enum class CustomerScreen {
    SPLASH,
    LOGIN,
    OTP,
    PROFILE_SETUP,
    HOME,
    DESTINATION_SEARCH,
    PICKUP_DROP_CONFIRM,
    FARE_ESTIMATE_DETAILS,
    CONFIRM_RIDE,
    ACTIVE_RIDE,
    SAFETY_CENTER,
    RIDE_COMPLETED,
    PAYMENT_RESULT,
    RATING,
    RIDE_HISTORY,
    RIDE_DETAILS,
    SAVED_PLACES,
    OFFERS_REFERRAL,
    PROFILE_SETTINGS
}
