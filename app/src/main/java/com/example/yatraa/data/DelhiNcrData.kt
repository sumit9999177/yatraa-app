package com.example.yatraa.data

import com.example.yatraa.data.local.CouponEntity
import com.example.yatraa.data.local.DriverEntity
import com.example.yatraa.data.local.PricingConfigEntity
import com.example.yatraa.data.local.SavedPlaceEntity
import com.example.yatraa.data.local.ServiceZoneEntity
import com.example.yatraa.data.local.SupportTicketEntity
import com.example.yatraa.data.local.UserEntity
import com.example.yatraa.model.DelhiLocation
import com.example.yatraa.model.DriverVerificationStatus
import com.example.yatraa.model.TicketCategory
import com.example.yatraa.model.TicketStatus
import com.example.yatraa.model.UserRole
import com.example.yatraa.model.VehicleCategory
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object DelhiNcrData {
    val LOCATIONS = listOf(
        DelhiLocation(
            id = "cp",
            name = "Connaught Place (Inner Circle)",
            landmark = "Rajiv Chowk Metro Stn, Central Delhi",
            zone = "Delhi Central",
            lat = 28.6315,
            lng = 77.2167
        ),
        DelhiLocation(
            id = "india_gate",
            name = "India Gate & Kartavya Path",
            landmark = "Near C-Hexagon, Central Delhi",
            zone = "Delhi Central",
            lat = 28.6129,
            lng = 77.2295
        ),
        DelhiLocation(
            id = "hauz_khas",
            name = "Hauz Khas Social & Village",
            landmark = "Deer Park Road, South Delhi",
            zone = "South Delhi",
            lat = 28.5494,
            lng = 77.1932
        ),
        DelhiLocation(
            id = "aiims",
            name = "AIIMS & Safdarjung Hospital",
            landmark = "Sri Aurobindo Marg, Ansari Nagar",
            zone = "South Delhi",
            lat = 28.5672,
            lng = 77.2100
        ),
        DelhiLocation(
            id = "cyber_hub",
            name = "DLF Cyber Hub & Cyber City",
            landmark = "Phase 2, Gurugram NCR",
            zone = "Gurugram Cybercity",
            lat = 28.4986,
            lng = 77.0878
        ),
        DelhiLocation(
            id = "sec29_gurgaon",
            name = "Sector 29 Market & Leisure Valley",
            landmark = "Near IFFCO Chowk, Gurugram",
            zone = "Gurugram Cybercity",
            lat = 28.4682,
            lng = 77.0632
        ),
        DelhiLocation(
            id = "noida_sec18",
            name = "Noida Sector 18 & Mall of India",
            landmark = "Atta Market, Sector 18, Noida NCR",
            zone = "Noida Hub",
            lat = 28.5708,
            lng = 77.3218
        ),
        DelhiLocation(
            id = "noida_sec62",
            name = "Noida Sector 62 (IT & Institutional)",
            landmark = "Near Electronic City Metro, Noida",
            zone = "Noida Hub",
            lat = 28.6258,
            lng = 77.3621
        ),
        DelhiLocation(
            id = "airport_t3",
            name = "IGI Airport Terminal 3",
            landmark = "Indira Gandhi International Airport, New Delhi",
            zone = "Dwarka & Airport",
            lat = 28.5562,
            lng = 77.1000
        ),
        DelhiLocation(
            id = "chandni_chowk",
            name = "Chandni Chowk & Red Fort",
            landmark = "Old Delhi Heritage Corridor",
            zone = "Delhi Central",
            lat = 28.6562,
            lng = 77.2410
        ),
        DelhiLocation(
            id = "saket_mall",
            name = "Select CITYWALK Mall, Saket",
            landmark = "Press Enclave Marg, Saket District Centre",
            zone = "South Delhi",
            lat = 28.5283,
            lng = 77.2190
        ),
        DelhiLocation(
            id = "dwarka_sec21",
            name = "Dwarka Sector 21 Metro Interchange",
            landmark = "Airport Express Line, Dwarka Sub-city",
            zone = "Dwarka & Airport",
            lat = 28.5522,
            lng = 77.0583
        ),
        DelhiLocation(
            id = "karol_bagh",
            name = "Karol Bagh Market (Gaffar Market)",
            landmark = "Arya Samaj Road, West Delhi",
            zone = "Delhi Central",
            lat = 28.6517,
            lng = 77.1906
        ),
        DelhiLocation(
            id = "akshardham",
            name = "Akshardham Temple Complex",
            landmark = "NH-24, Pandav Nagar, East Delhi",
            zone = "Delhi Central",
            lat = 28.6127,
            lng = 77.2773
        ),
        DelhiLocation(
            id = "vaishali",
            name = "Vaishali Metro Station",
            landmark = "Sector 4, Vaishali, Ghaziabad NCR",
            zone = "Ghaziabad NCR",
            lat = 28.6499,
            lng = 77.3400
        )
    )

    fun calculateDistanceKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371.0 // Radius of earth in km
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        val distance = r * c
        // add road tortuosity factor (~1.25x direct distance for Delhi city roads)
        return (distance * 1.25).coerceAtLeast(1.0)
    }

    val INITIAL_USERS = listOf(
        UserEntity(
            userId = "usr_delhi_01",
            name = "Aarav Sharma",
            phone = "+91 98112 34567",
            role = UserRole.CUSTOMER,
            language = "English",
            referralCode = "AARAV50",
            walletBalance = 240.0,
            emergencyContactName = "Sunita Sharma (Mother)",
            emergencyContactPhone = "+91 98112 99887"
        ),
        UserEntity(
            userId = "usr_driver_01",
            name = "Rajesh Kumar Yadav",
            phone = "+91 99580 12345",
            role = UserRole.DRIVER,
            language = "Hindi",
            referralCode = "RAJESH_AUTO",
            walletBalance = 1450.0
        ),
        UserEntity(
            userId = "usr_admin_01",
            name = "Yatraa Ops Lead (Delhi Hub)",
            phone = "+91 99999 00000",
            role = UserRole.ADMIN,
            language = "English",
            referralCode = "OPS_MASTER",
            walletBalance = 0.0
        )
    )

    val INITIAL_DRIVERS = listOf(
        DriverEntity(
            driverId = "drv_auto_01",
            userId = "usr_driver_01",
            name = "Rajesh Kumar Yadav",
            phone = "+91 99580 12345",
            vehicleCategory = VehicleCategory.AUTO,
            vehicleNumber = "DL 1R AB 4592",
            vehicleModel = "Bajaj Compact RE CNG Auto",
            verificationStatus = DriverVerificationStatus.VERIFIED,
            isOnline = true,
            currentLat = 28.6320,
            currentLng = 77.2180,
            rating = 4.88,
            totalRides = 412,
            acceptanceRate = 96,
            cancellationRate = 2,
            dailyEarnings = 780.0,
            weeklyEarnings = 5420.0
        ),
        DriverEntity(
            driverId = "drv_bike_01",
            userId = "usr_driver_02",
            name = "Manish Rawat",
            phone = "+91 98711 65432",
            vehicleCategory = VehicleCategory.BIKE,
            vehicleNumber = "DL 3S CC 7810",
            vehicleModel = "Hero Splendor Plus (Black)",
            verificationStatus = DriverVerificationStatus.VERIFIED,
            isOnline = true,
            currentLat = 28.6290,
            currentLng = 77.2150,
            rating = 4.92,
            totalRides = 628,
            acceptanceRate = 98,
            cancellationRate = 1,
            dailyEarnings = 940.0,
            weeklyEarnings = 6800.0
        ),
        DriverEntity(
            driverId = "drv_auto_02",
            userId = "usr_driver_03",
            name = "Gurpreet Singh",
            phone = "+91 98180 77665",
            vehicleCategory = VehicleCategory.AUTO,
            vehicleNumber = "DL 1R TT 1104",
            vehicleModel = "Piaggio Ape E-City Electric",
            verificationStatus = DriverVerificationStatus.VERIFIED,
            isOnline = true,
            currentLat = 28.5680,
            currentLng = 77.2120,
            rating = 4.82,
            totalRides = 290,
            acceptanceRate = 92,
            cancellationRate = 3,
            dailyEarnings = 540.0,
            weeklyEarnings = 4100.0
        ),
        DriverEntity(
            driverId = "drv_bike_02",
            userId = "usr_driver_04",
            name = "Amit Negi",
            phone = "+91 97110 33221",
            vehicleCategory = VehicleCategory.BIKE,
            vehicleNumber = "HR 26 CY 9044",
            vehicleModel = "Honda Shine 125cc",
            verificationStatus = DriverVerificationStatus.VERIFIED,
            isOnline = true,
            currentLat = 28.4995,
            currentLng = 77.0890,
            rating = 4.79,
            totalRides = 345,
            acceptanceRate = 94,
            cancellationRate = 4,
            dailyEarnings = 610.0,
            weeklyEarnings = 4900.0
        ),
        DriverEntity(
            driverId = "drv_auto_03",
            userId = "usr_driver_05",
            name = "Vikas Sharma",
            phone = "+91 98101 22998",
            vehicleCategory = VehicleCategory.AUTO,
            vehicleNumber = "UP 16 AT 3844",
            vehicleModel = "Mahindra Treo Electric Auto",
            verificationStatus = DriverVerificationStatus.PENDING_DOCS,
            isOnline = false,
            currentLat = 28.5720,
            currentLng = 77.3240,
            rating = 4.65,
            totalRides = 110,
            acceptanceRate = 88,
            cancellationRate = 6,
            dailyEarnings = 0.0,
            weeklyEarnings = 1800.0
        )
    )

    val INITIAL_PRICING = listOf(
        PricingConfigEntity(
            category = VehicleCategory.BIKE,
            baseFare = 20.0,
            perKmRate = 7.5,
            perMinuteRate = 1.0,
            minimumFare = 25.0,
            platformCommissionPercent = 10.0
        ),
        PricingConfigEntity(
            category = VehicleCategory.AUTO,
            baseFare = 30.0,
            perKmRate = 11.5,
            perMinuteRate = 1.5,
            minimumFare = 40.0,
            platformCommissionPercent = 10.0
        ),
        PricingConfigEntity(
            category = VehicleCategory.CAB,
            baseFare = 50.0,
            perKmRate = 16.0,
            perMinuteRate = 2.0,
            minimumFare = 80.0,
            platformCommissionPercent = 12.0
        )
    )

    val INITIAL_ZONES = listOf(
        ServiceZoneEntity("zone_central", "Delhi Central & Lutyens", "Delhi NCT", true, 1.0),
        ServiceZoneEntity("zone_south", "South Delhi & Saket", "Delhi NCT", true, 1.0),
        ServiceZoneEntity("zone_gurgaon", "Gurugram Cybercity & Golf Course", "Haryana NCR", true, 1.15),
        ServiceZoneEntity("zone_noida", "Noida Expressway & Sec 18", "UP NCR", true, 1.0),
        ServiceZoneEntity("zone_airport", "Dwarka & IGI Airport T3", "Delhi NCT", true, 1.10),
        ServiceZoneEntity("zone_ghaziabad", "Ghaziabad & Vaishali", "UP NCR", true, 1.0)
    )

    val INITIAL_COUPONS = listOf(
        CouponEntity(
            code = "DELHIFIRST",
            title = "50% OFF on First 3 Rides",
            discountPercent = 50,
            maxDiscount = 50.0,
            minFare = 40.0,
            description = "Welcome offer for new Delhi-NCR riders! Max ₹50 discount."
        ),
        CouponEntity(
            code = "YATRAA20",
            title = "₹20 Flat Off on Auto/Bike",
            discountPercent = 20,
            maxDiscount = 20.0,
            minFare = 50.0,
            description = "Daily commuter special discount across all routes."
        ),
        CouponEntity(
            code = "METROCONECT",
            title = "30% Off to Metro Stations",
            discountPercent = 30,
            maxDiscount = 35.0,
            minFare = 30.0,
            description = "Last-mile connectivity discount to any Delhi Metro station."
        )
    )

    val INITIAL_SAVED_PLACES = listOf(
        SavedPlaceEntity(
            label = "HOME",
            title = "Home",
            address = "Flat 402, Mayur Vihar Phase 1, New Delhi",
            lat = 28.6080,
            lng = 77.2950
        ),
        SavedPlaceEntity(
            label = "WORK",
            title = "Cyber City Office",
            address = "DLF Building 10, Tower B, Cyber Hub, Gurugram",
            lat = 28.4986,
            lng = 77.0878
        )
    )

    val INITIAL_TICKETS = listOf(
        SupportTicketEntity(
            ticketId = "TCK-8821",
            userId = "usr_delhi_01",
            userRole = UserRole.CUSTOMER,
            rideId = "RIDE-1002",
            category = TicketCategory.FARE_DISPUTE,
            description = "Slight route deviation near Barapullah flyover, request fare review.",
            status = TicketStatus.OPEN
        ),
        SupportTicketEntity(
            ticketId = "TCK-8822",
            userId = "usr_driver_01",
            userRole = UserRole.DRIVER,
            rideId = null,
            category = TicketCategory.APP_FEEDBACK,
            description = "Daily payout settlement verification requested for Saturday.",
            status = TicketStatus.RESOLVED,
            resolutionNotes = "Settlement of ₹1,450 credited to ICICI Bank account ending 4812 on Aug 18."
        )
    )
}
