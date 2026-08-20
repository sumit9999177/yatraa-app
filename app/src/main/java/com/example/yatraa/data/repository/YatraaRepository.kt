package com.example.yatraa.data.repository

import com.example.yatraa.data.DelhiNcrData
import com.example.yatraa.data.local.AuditLogEntity
import com.example.yatraa.data.local.CouponEntity
import com.example.yatraa.data.local.DriverEntity
import com.example.yatraa.data.local.PricingConfigEntity
import com.example.yatraa.data.local.RideEntity
import com.example.yatraa.data.local.SavedPlaceEntity
import com.example.yatraa.data.local.ServiceZoneEntity
import com.example.yatraa.data.local.SupportTicketEntity
import com.example.yatraa.data.local.UserEntity
import com.example.yatraa.data.local.YatraaDatabase
import com.example.yatraa.domain.DriverMatchingEngine
import com.example.yatraa.domain.FareEngine
import com.example.yatraa.model.DriverCandidate
import com.example.yatraa.model.DriverVerificationStatus
import com.example.yatraa.model.FareEstimate
import com.example.yatraa.model.PaymentMethod
import com.example.yatraa.model.PaymentStatus
import com.example.yatraa.model.RideStatus
import com.example.yatraa.model.TicketCategory
import com.example.yatraa.model.TicketStatus
import com.example.yatraa.model.UserRole
import com.example.yatraa.model.VehicleCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class YatraaRepository(
    private val db: YatraaDatabase,
    private val scope: CoroutineScope
) {
    val allUsers: Flow<List<UserEntity>> = db.userDao().getAllUsers()
    val allDrivers: Flow<List<DriverEntity>> = db.driverDao().getAllDrivers()
    val onlineDrivers: Flow<List<DriverEntity>> = db.driverDao().getOnlineVerifiedDrivers()
    val allRides: Flow<List<RideEntity>> = db.rideDao().getAllRides()
    val activeCustomerRide: Flow<RideEntity?> = db.rideDao().getActiveRideForCustomer()
    val allPricing: Flow<List<PricingConfigEntity>> = db.pricingDao().getAllPricing()
    val allZones: Flow<List<ServiceZoneEntity>> = db.serviceZoneDao().getAllZones()
    val activeCoupons: Flow<List<CouponEntity>> = db.couponDao().getActiveCoupons()
    val savedPlaces: Flow<List<SavedPlaceEntity>> = db.savedPlaceDao().getAllSavedPlaces()
    val supportTickets: Flow<List<SupportTicketEntity>> = db.supportDao().getAllTickets()
    val auditLogs: Flow<List<AuditLogEntity>> = db.auditDao().getAllAuditLogs()

    // Platform KPIs
    val totalDriversCount: Flow<Int> = db.driverDao().getTotalDriverCount()
    val verifiedDriversCount: Flow<Int> = db.driverDao().getVerifiedDriverCount()
    val onlineDriversCount: Flow<Int> = db.driverDao().getOnlineDriverCount()
    val completedRidesCount: Flow<Int> = db.rideDao().getCompletedRideCount()
    val activeRidesCount: Flow<Int> = db.rideDao().getActiveRideCount()
    val totalRevenue: Flow<Double?> = db.rideDao().getTotalRevenue()

    init {
        scope.launch(Dispatchers.IO) {
            seedDatabaseIfEmpty()
        }
    }

    suspend fun seedDatabaseIfEmpty() = withContext(Dispatchers.IO) {
        val count = db.driverDao().getTotalDriverCount().firstOrNull() ?: 0
        if (count == 0) {
            for (user in DelhiNcrData.INITIAL_USERS) {
                db.userDao().insertOrUpdate(user)
            }
            db.driverDao().insertAll(DelhiNcrData.INITIAL_DRIVERS)
            db.pricingDao().insertAll(DelhiNcrData.INITIAL_PRICING)
            db.serviceZoneDao().insertAll(DelhiNcrData.INITIAL_ZONES)
            db.couponDao().insertAll(DelhiNcrData.INITIAL_COUPONS)
            for (place in DelhiNcrData.INITIAL_SAVED_PLACES) {
                db.savedPlaceDao().insertPlace(place)
            }
            for (ticket in DelhiNcrData.INITIAL_TICKETS) {
                db.supportDao().insertTicket(ticket)
            }
            db.auditDao().insertLog(
                AuditLogEntity(
                    actor = "SYSTEM_INITIALIZER",
                    action = "INITIAL_SEED",
                    target = "DELHI_NCR_FLEET",
                    previousValue = "EMPTY",
                    newValue = "POPULATED_5_DRIVERS"
                )
            )
        }
    }

    suspend fun calculateFareEstimates(
        pickupLat: Double,
        pickupLng: Double,
        dropLat: Double,
        dropLng: Double,
        couponCode: String?
    ): Map<VehicleCategory, FareEstimate> = withContext(Dispatchers.IO) {
        val distanceKm = DelhiNcrData.calculateDistanceKm(pickupLat, pickupLng, dropLat, dropLng)
        val coupon = if (!couponCode.isNullOrBlank()) db.couponDao().getCoupon(couponCode) else null

        val results = mutableMapOf<VehicleCategory, FareEstimate>()
        for (cat in VehicleCategory.entries) {
            val pricing = db.pricingDao().getPricingForCategory(cat) ?: PricingConfigEntity(
                category = cat,
                baseFare = if (cat == VehicleCategory.BIKE) 20.0 else 30.0,
                perKmRate = if (cat == VehicleCategory.BIKE) 7.5 else 11.5,
                perMinuteRate = 1.0,
                minimumFare = 25.0
            )
            results[cat] = FareEngine.calculateEstimate(
                category = cat,
                pricing = pricing,
                distanceKm = distanceKm,
                zoneSurge = 1.0,
                coupon = coupon
            )
        }
        results
    }

    suspend fun createAndBookRide(
        customer: UserEntity,
        category: VehicleCategory,
        pickupAddress: String,
        dropAddress: String,
        pickupLat: Double,
        pickupLng: Double,
        dropLat: Double,
        dropLng: Double,
        estimate: FareEstimate,
        paymentMethod: PaymentMethod,
        couponCode: String?
    ): RideEntity = withContext(Dispatchers.IO) {
        val rideId = "YATRAA-" + (1000..9999).random()
        val otpCode = (1000..9999).random().toString()

        val ride = RideEntity(
            rideId = rideId,
            customerId = customer.userId,
            customerName = customer.name,
            customerPhone = customer.phone,
            driverId = null,
            driverName = null,
            driverPhone = null,
            vehicleCategory = category,
            vehicleNumber = null,
            vehicleModel = null,
            pickupAddress = pickupAddress,
            dropAddress = dropAddress,
            pickupLat = pickupLat,
            pickupLng = pickupLng,
            dropLat = dropLat,
            dropLng = dropLng,
            distanceKm = estimate.distanceKm,
            durationMin = estimate.durationMinutes,
            estimatedFare = estimate.totalFare,
            finalFare = estimate.totalFare,
            baseFare = estimate.baseFare,
            distanceCharge = estimate.distanceCharge,
            timeCharge = estimate.timeCharge,
            discountAmount = estimate.discount,
            couponCode = couponCode,
            paymentMethod = paymentMethod,
            paymentStatus = PaymentStatus.PENDING,
            rideStatus = RideStatus.SEARCHING_DRIVER,
            otpCode = otpCode,
            createdAt = System.currentTimeMillis()
        )
        db.rideDao().insertOrUpdate(ride)
        ride
    }

    suspend fun findMatchingDrivers(
        pickupLat: Double,
        pickupLng: Double,
        category: VehicleCategory
    ): List<DriverCandidate> = withContext(Dispatchers.IO) {
        val drivers = db.driverDao().getAvailableDriversForCategory(category)
        DriverMatchingEngine.rankAndFindCandidates(pickupLat, pickupLng, category, drivers)
    }

    suspend fun assignDriverToRide(rideId: String, driver: DriverCandidate): Boolean = withContext(Dispatchers.IO) {
        val ride = db.rideDao().getRideByIdSync(rideId) ?: return@withContext false
        val updatedRide = ride.copy(
            driverId = driver.driverId,
            driverName = driver.name,
            driverPhone = driver.phone,
            vehicleNumber = driver.vehicleNumber,
            vehicleModel = driver.vehicleModel,
            rideStatus = RideStatus.DRIVER_ASSIGNED,
            acceptedAt = System.currentTimeMillis()
        )
        db.rideDao().insertOrUpdate(updatedRide)
        true
    }

    suspend fun updateRideStatus(rideId: String, status: RideStatus, reason: String? = null) = withContext(Dispatchers.IO) {
        val ride = db.rideDao().getRideByIdSync(rideId) ?: return@withContext
        val updatedRide = when (status) {
            RideStatus.OTP_VERIFIED, RideStatus.IN_PROGRESS -> ride.copy(
                rideStatus = status,
                startedAt = ride.startedAt ?: System.currentTimeMillis()
            )
            RideStatus.COMPLETED -> {
                // Credit driver earnings
                ride.driverId?.let { drvId ->
                    val netDriverEarning = ride.finalFare * 0.90 // 90% to driver
                    db.driverDao().addEarnings(drvId, netDriverEarning)
                }
                ride.copy(
                    rideStatus = status,
                    paymentStatus = PaymentStatus.COMPLETED,
                    completedAt = System.currentTimeMillis()
                )
            }
            RideStatus.CANCELLED_BY_CUSTOMER, RideStatus.CANCELLED_BY_DRIVER, RideStatus.CANCELLED_BY_SYSTEM -> ride.copy(
                rideStatus = status,
                cancellationReason = reason
            )
            else -> ride.copy(rideStatus = status)
        }
        db.rideDao().insertOrUpdate(updatedRide)
    }

    suspend fun rateRide(rideId: String, stars: Int, comment: String) = withContext(Dispatchers.IO) {
        db.rideDao().rateRide(rideId, stars, comment)
    }

    // Driver Operations
    suspend fun toggleDriverOnline(driverId: String, isOnline: Boolean) = withContext(Dispatchers.IO) {
        db.driverDao().setOnlineStatus(driverId, isOnline)
    }

    suspend fun updateDriverVerification(driverId: String, status: DriverVerificationStatus, adminActor: String) = withContext(Dispatchers.IO) {
        val driver = db.driverDao().getDriverById(driverId).firstOrNull()
        val oldStatus = driver?.verificationStatus?.name ?: "UNKNOWN"
        db.driverDao().setVerificationStatus(driverId, status)
        db.auditDao().insertLog(
            AuditLogEntity(
                actor = adminActor,
                action = "UPDATE_DRIVER_VERIFICATION",
                target = "Driver $driverId (${driver?.name})",
                previousValue = oldStatus,
                newValue = status.name
            )
        )
    }

    // Admin Pricing Config with Audit Log
    suspend fun updatePricingConfig(config: PricingConfigEntity, adminActor: String) = withContext(Dispatchers.IO) {
        val oldConfig = db.pricingDao().getPricingForCategory(config.category)
        val oldVal = "Base: ₹${oldConfig?.baseFare}, PerKm: ₹${oldConfig?.perKmRate}, PerMin: ₹${oldConfig?.perMinuteRate}"
        val newVal = "Base: ₹${config.baseFare}, PerKm: ₹${config.perKmRate}, PerMin: ₹${config.perMinuteRate}"
        db.pricingDao().insertOrUpdate(config)
        db.auditDao().insertLog(
            AuditLogEntity(
                actor = adminActor,
                action = "UPDATE_PRICING",
                target = "Pricing Category: ${config.category.name}",
                previousValue = oldVal,
                newValue = newVal
            )
        )
    }

    // Zone toggle with Audit Log
    suspend fun toggleZone(zoneId: String, isActive: Boolean, adminActor: String) = withContext(Dispatchers.IO) {
        db.serviceZoneDao().toggleZone(zoneId, isActive)
        db.auditDao().insertLog(
            AuditLogEntity(
                actor = adminActor,
                action = "TOGGLE_ZONE_STATUS",
                target = "Zone $zoneId",
                previousValue = (!isActive).toString(),
                newValue = isActive.toString()
            )
        )
    }

    // Coupon & Support
    suspend fun addCoupon(coupon: CouponEntity, adminActor: String) = withContext(Dispatchers.IO) {
        db.couponDao().insertCoupon(coupon)
        db.auditDao().insertLog(
            AuditLogEntity(
                actor = adminActor,
                action = "CREATE_COUPON",
                target = "Coupon ${coupon.code}",
                previousValue = "NONE",
                newValue = "${coupon.discountPercent}% Off max ₹${coupon.maxDiscount}"
            )
        )
    }

    suspend fun insertOrUpdateUser(user: UserEntity) = withContext(Dispatchers.IO) {
        db.userDao().insertOrUpdate(user)
    }

    fun getRideById(rideId: String): Flow<RideEntity?> {
        return db.rideDao().getRideById(rideId)
    }

    suspend fun addWalletBalance(userId: String, amount: Double) = withContext(Dispatchers.IO) {
        val user = db.userDao().getUserById(userId).firstOrNull() ?: return@withContext
        val updatedUser = user.copy(walletBalance = user.walletBalance + amount)
        db.userDao().insertOrUpdate(updatedUser)
    }

    suspend fun addSavedPlace(place: SavedPlaceEntity) = withContext(Dispatchers.IO) {
        db.savedPlaceDao().insertPlace(place)
    }

    suspend fun deleteSavedPlace(placeId: Long) = withContext(Dispatchers.IO) {
        db.savedPlaceDao().deletePlace(placeId)
    }

    suspend fun createSupportTicket(ticket: SupportTicketEntity) = withContext(Dispatchers.IO) {
        db.supportDao().insertTicket(ticket)
    }

    suspend fun resolveSupportTicket(ticketId: String, resolution: String, adminActor: String) = withContext(Dispatchers.IO) {
        db.supportDao().resolveTicket(ticketId, resolution)
        db.auditDao().insertLog(
            AuditLogEntity(
                actor = adminActor,
                action = "RESOLVE_SUPPORT_TICKET",
                target = "Ticket $ticketId",
                previousValue = "OPEN",
                newValue = "RESOLVED: $resolution"
            )
        )
    }
}
