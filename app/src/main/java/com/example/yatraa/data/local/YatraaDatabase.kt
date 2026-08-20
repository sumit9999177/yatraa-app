package com.example.yatraa.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.yatraa.data.DelhiNcrData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        DriverEntity::class,
        RideEntity::class,
        PricingConfigEntity::class,
        ServiceZoneEntity::class,
        CouponEntity::class,
        SavedPlaceEntity::class,
        SupportTicketEntity::class,
        AuditLogEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class YatraaDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun driverDao(): DriverDao
    abstract fun rideDao(): RideDao
    abstract fun pricingDao(): PricingDao
    abstract fun serviceZoneDao(): ServiceZoneDao
    abstract fun couponDao(): CouponDao
    abstract fun savedPlaceDao(): SavedPlaceDao
    abstract fun supportDao(): SupportDao
    abstract fun auditDao(): AuditDao

    companion object {
        @Volatile
        private var INSTANCE: YatraaDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): YatraaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    YatraaDatabase::class.java,
                    "yatraa_database"
                )
                    .addCallback(YatraaDatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class YatraaDatabaseCallback(
            private val scope: CoroutineScope
        ) : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database)
                    }
                }
            }

            suspend fun populateInitialData(database: YatraaDatabase) {
                // Populate Users
                for (user in DelhiNcrData.INITIAL_USERS) {
                    database.userDao().insertOrUpdate(user)
                }
                // Populate Drivers
                database.driverDao().insertAll(DelhiNcrData.INITIAL_DRIVERS)
                // Populate Pricing
                database.pricingDao().insertAll(DelhiNcrData.INITIAL_PRICING)
                // Populate Zones
                database.serviceZoneDao().insertAll(DelhiNcrData.INITIAL_ZONES)
                // Populate Coupons
                database.couponDao().insertAll(DelhiNcrData.INITIAL_COUPONS)
                // Populate Saved Places
                for (place in DelhiNcrData.INITIAL_SAVED_PLACES) {
                    database.savedPlaceDao().insertPlace(place)
                }
                // Populate Support Tickets
                for (ticket in DelhiNcrData.INITIAL_TICKETS) {
                    database.supportDao().insertTicket(ticket)
                }
                // Seed initial Audit Log
                database.auditDao().insertLog(
                    AuditLogEntity(
                        actor = "SYSTEM_INITIALIZER",
                        action = "SEED_PLATFORM",
                        target = "DELHI_NCR_ZONE",
                        previousValue = "NONE",
                        newValue = "INITIALIZED_V1.0"
                    )
                )
            }
        }
    }
}
