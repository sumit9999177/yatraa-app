package com.example.yatraa.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yatraa.model.DriverVerificationStatus
import com.example.yatraa.model.RideStatus
import com.example.yatraa.model.VehicleCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE userId = :userId LIMIT 1")
    fun getUserById(userId: String): Flow<UserEntity?>

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(user: UserEntity)

    @Query("SELECT COUNT(*) FROM users")
    fun getUserCount(): Flow<Int>
}

@Dao
interface DriverDao {
    @Query("SELECT * FROM drivers WHERE driverId = :driverId LIMIT 1")
    fun getDriverById(driverId: String): Flow<DriverEntity?>

    @Query("SELECT * FROM drivers")
    fun getAllDrivers(): Flow<List<DriverEntity>>

    @Query("SELECT * FROM drivers WHERE isOnline = 1 AND verificationStatus = 'VERIFIED'")
    fun getOnlineVerifiedDrivers(): Flow<List<DriverEntity>>

    @Query("SELECT * FROM drivers WHERE isOnline = 1 AND verificationStatus = 'VERIFIED' AND vehicleCategory = :category")
    suspend fun getAvailableDriversForCategory(category: VehicleCategory): List<DriverEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(driver: DriverEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(drivers: List<DriverEntity>)

    @Update
    suspend fun updateDriver(driver: DriverEntity)

    @Query("UPDATE drivers SET isOnline = :isOnline WHERE driverId = :driverId")
    suspend fun setOnlineStatus(driverId: String, isOnline: Boolean)

    @Query("UPDATE drivers SET verificationStatus = :status WHERE driverId = :driverId")
    suspend fun setVerificationStatus(driverId: String, status: DriverVerificationStatus)

    @Query("UPDATE drivers SET dailyEarnings = dailyEarnings + :amount, totalRides = totalRides + 1 WHERE driverId = :driverId")
    suspend fun addEarnings(driverId: String, amount: Double)

    @Query("SELECT COUNT(*) FROM drivers")
    fun getTotalDriverCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM drivers WHERE verificationStatus = 'VERIFIED'")
    fun getVerifiedDriverCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM drivers WHERE isOnline = 1")
    fun getOnlineDriverCount(): Flow<Int>
}

@Dao
interface RideDao {
    @Query("SELECT * FROM rides WHERE rideId = :rideId LIMIT 1")
    fun getRideById(rideId: String): Flow<RideEntity?>

    @Query("SELECT * FROM rides WHERE rideId = :rideId LIMIT 1")
    suspend fun getRideByIdSync(rideId: String): RideEntity?

    @Query("SELECT * FROM rides ORDER BY createdAt DESC")
    fun getAllRides(): Flow<List<RideEntity>>

    @Query("SELECT * FROM rides WHERE customerId = :customerId ORDER BY createdAt DESC")
    fun getRidesForCustomer(customerId: String): Flow<List<RideEntity>>

    @Query("SELECT * FROM rides WHERE driverId = :driverId ORDER BY createdAt DESC")
    fun getRidesForDriver(driverId: String): Flow<List<RideEntity>>

    @Query("SELECT * FROM rides WHERE rideStatus NOT IN ('COMPLETED', 'CANCELLED_BY_CUSTOMER', 'CANCELLED_BY_DRIVER', 'CANCELLED_BY_SYSTEM') ORDER BY createdAt DESC LIMIT 1")
    fun getActiveRideForCustomer(): Flow<RideEntity?>

    @Query("SELECT * FROM rides WHERE driverId = :driverId AND rideStatus NOT IN ('COMPLETED', 'CANCELLED_BY_CUSTOMER', 'CANCELLED_BY_DRIVER', 'CANCELLED_BY_SYSTEM') ORDER BY createdAt DESC LIMIT 1")
    fun getActiveRideForDriver(driverId: String): Flow<RideEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(ride: RideEntity)

    @Query("UPDATE rides SET rideStatus = :status WHERE rideId = :rideId")
    suspend fun updateRideStatus(rideId: String, status: RideStatus)

    @Query("UPDATE rides SET ratingStars = :stars, ratingComment = :comment WHERE rideId = :rideId")
    suspend fun rateRide(rideId: String, stars: Int, comment: String)

    @Query("SELECT COUNT(*) FROM rides WHERE rideStatus = 'COMPLETED'")
    fun getCompletedRideCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM rides WHERE rideStatus NOT IN ('COMPLETED', 'CANCELLED_BY_CUSTOMER', 'CANCELLED_BY_DRIVER', 'CANCELLED_BY_SYSTEM')")
    fun getActiveRideCount(): Flow<Int>

    @Query("SELECT SUM(finalFare) FROM rides WHERE rideStatus = 'COMPLETED'")
    fun getTotalRevenue(): Flow<Double?>
}

@Dao
interface PricingDao {
    @Query("SELECT * FROM pricing_config")
    fun getAllPricing(): Flow<List<PricingConfigEntity>>

    @Query("SELECT * FROM pricing_config WHERE category = :category LIMIT 1")
    suspend fun getPricingForCategory(category: VehicleCategory): PricingConfigEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(config: PricingConfigEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(configs: List<PricingConfigEntity>)
}

@Dao
interface ServiceZoneDao {
    @Query("SELECT * FROM service_zones")
    fun getAllZones(): Flow<List<ServiceZoneEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(zones: List<ServiceZoneEntity>)

    @Query("UPDATE service_zones SET isActive = :isActive WHERE zoneId = :zoneId")
    suspend fun toggleZone(zoneId: String, isActive: Boolean)
}

@Dao
interface CouponDao {
    @Query("SELECT * FROM coupons WHERE isActive = 1")
    fun getActiveCoupons(): Flow<List<CouponEntity>>

    @Query("SELECT * FROM coupons WHERE code = :code AND isActive = 1 LIMIT 1")
    suspend fun getCoupon(code: String): CouponEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoupon(coupon: CouponEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(coupons: List<CouponEntity>)
}

@Dao
interface SavedPlaceDao {
    @Query("SELECT * FROM saved_places")
    fun getAllSavedPlaces(): Flow<List<SavedPlaceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlace(place: SavedPlaceEntity)

    @Query("DELETE FROM saved_places WHERE placeId = :placeId")
    suspend fun deletePlace(placeId: Long)
}

@Dao
interface SupportDao {
    @Query("SELECT * FROM support_tickets ORDER BY createdAt DESC")
    fun getAllTickets(): Flow<List<SupportTicketEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTicket(ticket: SupportTicketEntity)

    @Query("UPDATE support_tickets SET status = 'RESOLVED', resolutionNotes = :resolution WHERE ticketId = :ticketId")
    suspend fun resolveTicket(ticketId: String, resolution: String)
}

@Dao
interface AuditDao {
    @Query("SELECT * FROM audit_logs ORDER BY timestamp DESC")
    fun getAllAuditLogs(): Flow<List<AuditLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: AuditLogEntity)
}
