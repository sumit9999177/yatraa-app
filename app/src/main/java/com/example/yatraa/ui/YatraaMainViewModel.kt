package com.example.yatraa.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
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
import com.example.yatraa.data.repository.YatraaRepository
import com.example.yatraa.model.CustomerScreen
import com.example.yatraa.model.DelhiLocation
import com.example.yatraa.model.DriverVerificationStatus
import com.example.yatraa.model.FareEstimate
import com.example.yatraa.model.PaymentMethod
import com.example.yatraa.model.RideStatus
import com.example.yatraa.model.TicketCategory
import com.example.yatraa.model.UserRole
import com.example.yatraa.model.VehicleCategory
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class YatraaMainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = YatraaDatabase.getDatabase(application, viewModelScope)
    val repository = YatraaRepository(db, viewModelScope)

    // Current Role
    private val _currentRole = MutableStateFlow(UserRole.CUSTOMER)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    fun setRole(role: UserRole) {
        _currentRole.value = role
    }

    // Customer Navigation Tab: 0=Home, 1=History, 2=Saved, 3=Offers, 4=Profile
    private val _customerTab = MutableStateFlow(0)
    val customerTab: StateFlow<Int> = _customerTab.asStateFlow()

    // Driver Navigation Tab: 0=Dashboard, 1=Earnings, 2=Trips, 3=KYC/Docs
    private val _driverTab = MutableStateFlow(0)
    val driverTab: StateFlow<Int> = _driverTab.asStateFlow()

    // Admin Navigation Tab: 0=Overview, 1=Fleet/Drivers, 2=Live Rides, 3=Pricing, 4=Zones, 5=Coupons, 6=Support, 7=Audit
    private val _adminTab = MutableStateFlow(0)
    val adminTab: StateFlow<Int> = _adminTab.asStateFlow()

    // Current Customer / User
    private val _currentUserId = MutableStateFlow("usr_delhi_01")
    val currentCustomer: StateFlow<UserEntity?> = repository.allUsers
        .combine(_currentUserId) { users, id ->
            users.firstOrNull { it.userId == id } ?: users.firstOrNull()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Customer Navigation Screen (Full MVP state machine)
    private val _customerScreen = MutableStateFlow(CustomerScreen.HOME)
    val customerScreen: StateFlow<CustomerScreen> = _customerScreen.asStateFlow()

    private val customerNavStack = mutableListOf(CustomerScreen.HOME)

    // Customer Auth / Onboarding State
    private val _isCustomerLoggedIn = MutableStateFlow(true)
    val isCustomerLoggedIn: StateFlow<Boolean> = _isCustomerLoggedIn.asStateFlow()

    private val _loginPhone = MutableStateFlow("9811234567")
    val loginPhone: StateFlow<String> = _loginPhone.asStateFlow()

    private val _otpInput = MutableStateFlow("")
    val otpInput: StateFlow<String> = _otpInput.asStateFlow()

    private val _generatedMockOtp = MutableStateFlow("4821")
    val generatedMockOtp: StateFlow<String> = _generatedMockOtp.asStateFlow()

    private val _otpError = MutableStateFlow<String?>(null)
    val otpError: StateFlow<String?> = _otpError.asStateFlow()

    private val _otpTimerSeconds = MutableStateFlow(30)
    val otpTimerSeconds: StateFlow<Int> = _otpTimerSeconds.asStateFlow()

    private val _isAuthLoading = MutableStateFlow(false)
    val isAuthLoading: StateFlow<Boolean> = _isAuthLoading.asStateFlow()

    // Profile Setup Form State
    private val _profileName = MutableStateFlow("Aarav Sharma")
    val profileName: StateFlow<String> = _profileName.asStateFlow()

    private val _profileEmail = MutableStateFlow("aarav.delhi@example.com")
    val profileEmail: StateFlow<String> = _profileEmail.asStateFlow()

    private val _profileEmergencyName = MutableStateFlow("Sunita Sharma")
    val profileEmergencyName: StateFlow<String> = _profileEmergencyName.asStateFlow()

    private val _profileEmergencyPhone = MutableStateFlow("+91 98112 99887")
    val profileEmergencyPhone: StateFlow<String> = _profileEmergencyPhone.asStateFlow()

    private val _profileLanguage = MutableStateFlow("English")
    val profileLanguage: StateFlow<String> = _profileLanguage.asStateFlow()

    // Selected past ride for Ride Details receipt screen
    private val _selectedPastRide = MutableStateFlow<RideEntity?>(null)
    val selectedPastRide: StateFlow<RideEntity?> = _selectedPastRide.asStateFlow()

    // Last completed ride for Payment Result & Rating screens
    private val _lastCompletedRide = MutableStateFlow<RideEntity?>(null)
    val lastCompletedRide: StateFlow<RideEntity?> = _lastCompletedRide.asStateFlow()

    // Ride Booking Customization
    private val _specialInstructions = MutableStateFlow("")
    val specialInstructions: StateFlow<String> = _specialInstructions.asStateFlow()

    private val _driverTipAmount = MutableStateFlow(0)
    val driverTipAmount: StateFlow<Int> = _driverTipAmount.asStateFlow()

    private val _ratingStars = MutableStateFlow(5)
    val ratingStars: StateFlow<Int> = _ratingStars.asStateFlow()

    private val _ratingComment = MutableStateFlow("")
    val ratingComment: StateFlow<String> = _ratingComment.asStateFlow()

    private val _selectedRatingTags = MutableStateFlow<Set<String>>(setOf("Polite Driver", "Clean Vehicle"))
    val selectedRatingTags: StateFlow<Set<String>> = _selectedRatingTags.asStateFlow()

    private var otpTimerJob: Job? = null

    // Current Driver
    private val _selectedDriverId = MutableStateFlow("drv_auto_01")
    val selectedDriverId: StateFlow<String> = _selectedDriverId.asStateFlow()

    val currentDriver: StateFlow<DriverEntity?> = repository.allDrivers
        .combine(_selectedDriverId) { drivers, id ->
            drivers.firstOrNull { it.driverId == id } ?: drivers.firstOrNull()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Active Customer Ride
    val activeCustomerRide: StateFlow<RideEntity?> = repository.activeCustomerRide
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // All database flows
    val allRides: StateFlow<List<RideEntity>> = repository.allRides
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allDrivers: StateFlow<List<DriverEntity>> = repository.allDrivers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val onlineDrivers: StateFlow<List<DriverEntity>> = repository.onlineDrivers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allPricing: StateFlow<List<PricingConfigEntity>> = repository.allPricing
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allZones: StateFlow<List<ServiceZoneEntity>> = repository.allZones
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeCoupons: StateFlow<List<CouponEntity>> = repository.activeCoupons
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedPlaces: StateFlow<List<SavedPlaceEntity>> = repository.savedPlaces
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val supportTickets: StateFlow<List<SupportTicketEntity>> = repository.supportTickets
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val auditLogs: StateFlow<List<AuditLogEntity>> = repository.auditLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Platform KPIs
    val totalDriversCount: StateFlow<Int> = repository.totalDriversCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val verifiedDriversCount: StateFlow<Int> = repository.verifiedDriversCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val onlineDriversCount: StateFlow<Int> = repository.onlineDriversCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val completedRidesCount: StateFlow<Int> = repository.completedRidesCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val activeRidesCount: StateFlow<Int> = repository.activeRidesCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val totalRevenue: StateFlow<Double?> = repository.totalRevenue
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // Customer Ride Booking State
    private val _pickupLocation = MutableStateFlow(DelhiNcrData.LOCATIONS[0]) // Connaught Place
    val pickupLocation: StateFlow<DelhiLocation> = _pickupLocation.asStateFlow()

    private val _dropLocation = MutableStateFlow(DelhiNcrData.LOCATIONS[1]) // India Gate
    val dropLocation: StateFlow<DelhiLocation> = _dropLocation.asStateFlow()

    private val _selectedVehicleCategory = MutableStateFlow(VehicleCategory.BIKE)
    val selectedVehicleCategory: StateFlow<VehicleCategory> = _selectedVehicleCategory.asStateFlow()

    private val _selectedCouponCode = MutableStateFlow<String?>("DELHIFIRST")
    val selectedCouponCode: StateFlow<String?> = _selectedCouponCode.asStateFlow()

    private val _selectedPaymentMethod = MutableStateFlow(PaymentMethod.CASH)
    val selectedPaymentMethod: StateFlow<PaymentMethod> = _selectedPaymentMethod.asStateFlow()

    private val _fareEstimates = MutableStateFlow<Map<VehicleCategory, FareEstimate>>(emptyMap())
    val fareEstimates: StateFlow<Map<VehicleCategory, FareEstimate>> = _fareEstimates.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isSearchingDestination = MutableStateFlow(false)
    val isSearchingDestination: StateFlow<Boolean> = _isSearchingDestination.asStateFlow()

    // Live ride progress & simulation
    private val _rideProgressFraction = MutableStateFlow(0f)
    val rideProgressFraction: StateFlow<Float> = _rideProgressFraction.asStateFlow()

    private val _showSafetyDialog = MutableStateFlow(false)
    val showSafetyDialog: StateFlow<Boolean> = _showSafetyDialog.asStateFlow()

    private val _showRatingDialog = MutableStateFlow(false)
    val showRatingDialog: StateFlow<Boolean> = _showRatingDialog.asStateFlow()

    // Driver Partner incoming request state
    private val _incomingRideRequest = MutableStateFlow<RideEntity?>(null)
    val incomingRideRequest: StateFlow<RideEntity?> = _incomingRideRequest.asStateFlow()

    private val _incomingRequestSecondsLeft = MutableStateFlow(15)
    val incomingRequestSecondsLeft: StateFlow<Int> = _incomingRequestSecondsLeft.asStateFlow()

    private var simulationJob: Job? = null
    private var incomingTimerJob: Job? = null

    init {
        refreshFareEstimates()
    }

    // Customer Screen Navigation Stack
    fun navigateToCustomerScreen(screen: CustomerScreen) {
        if (_customerScreen.value != screen) {
            customerNavStack.add(screen)
            _customerScreen.value = screen
        }
    }

    fun navigateBackCustomer(): Boolean {
        if (customerNavStack.size > 1) {
            customerNavStack.removeAt(customerNavStack.lastIndex)
            _customerScreen.value = customerNavStack.last()
            return true
        } else if (_customerScreen.value != CustomerScreen.HOME) {
            _customerScreen.value = CustomerScreen.HOME
            customerNavStack.clear()
            customerNavStack.add(CustomerScreen.HOME)
            return true
        }
        return false
    }

    // Customer Authentication Flows
    fun setLoginPhone(phone: String) {
        // Allow only digits up to 10 characters
        _loginPhone.value = phone.filter { it.isDigit() }.take(10)
    }

    fun setOtpInput(otp: String) {
        _otpInput.value = otp.filter { it.isDigit() }.take(6)
        _otpError.value = null
    }

    fun setProfileName(name: String) {
        _profileName.value = name
    }

    fun setProfileEmail(email: String) {
        _profileEmail.value = email
    }

    fun setProfileEmergencyName(name: String) {
        _profileEmergencyName.value = name
    }

    fun setProfileEmergencyPhone(phone: String) {
        _profileEmergencyPhone.value = phone
    }

    fun setProfileLanguage(lang: String) {
        _profileLanguage.value = lang
    }

    fun sendLoginOtp() {
        if (_loginPhone.value.length < 10) {
            _otpError.value = "Please enter a valid 10-digit Delhi-NCR mobile number"
            return
        }
        _isAuthLoading.value = true
        viewModelScope.launch {
            delay(600) // Realistic network delay simulation
            _isAuthLoading.value = false
            _generatedMockOtp.value = "4821"
            _otpInput.value = ""
            _otpError.value = null
            startOtpCountdown()
            navigateToCustomerScreen(CustomerScreen.OTP)
        }
    }

    fun resendOtp() {
        viewModelScope.launch {
            _generatedMockOtp.value = (1000..9999).random().toString()
            startOtpCountdown()
        }
    }

    private fun startOtpCountdown() {
        otpTimerJob?.cancel()
        otpTimerJob = viewModelScope.launch {
            _otpTimerSeconds.value = 30
            while (_otpTimerSeconds.value > 0) {
                delay(1000)
                _otpTimerSeconds.value -= 1
            }
        }
    }

    fun verifyCustomerOtp() {
        if (_otpInput.value.isBlank()) {
            _otpError.value = "Please enter the OTP sent to +91 ${_loginPhone.value}"
            return
        }

        // Mock verification: accepts either generatedMockOtp, "123456", or "4821"
        if (_otpInput.value == _generatedMockOtp.value || _otpInput.value == "123456" || _otpInput.value == "4821") {
            _isAuthLoading.value = true
            viewModelScope.launch {
                delay(500)
                _isAuthLoading.value = false
                _isCustomerLoggedIn.value = true
                
                val user = currentCustomer.value
                if (user == null || user.name.isBlank() || user.name == "New User") {
                    navigateToCustomerScreen(CustomerScreen.PROFILE_SETUP)
                } else {
                    navigateToCustomerScreen(CustomerScreen.HOME)
                }
            }
        } else {
            _otpError.value = "Invalid OTP code. Try entering 4821 or click Auto-fill Mock OTP."
        }
    }

    fun autoFillMockOtp() {
        _otpInput.value = _generatedMockOtp.value
        _otpError.value = null
    }

    fun saveCustomerProfile() {
        val user = currentCustomer.value ?: UserEntity(
            userId = "usr_delhi_" + (100..999).random(),
            name = _profileName.value,
            phone = "+91 ${_loginPhone.value}",
            role = UserRole.CUSTOMER
        )
        val updated = user.copy(
            name = _profileName.value.ifBlank { "Delhi Passenger" },
            phone = if (_loginPhone.value.startsWith("+91")) _loginPhone.value else "+91 ${_loginPhone.value}",
            language = _profileLanguage.value,
            emergencyContactName = _profileEmergencyName.value.ifBlank { "Family Emergency" },
            emergencyContactPhone = _profileEmergencyPhone.value.ifBlank { "+91 98112 99887" }
        )

        viewModelScope.launch {
            repository.insertOrUpdateUser(updated)
            navigateToCustomerScreen(CustomerScreen.HOME)
        }
    }

    fun logoutCustomer() {
        _isCustomerLoggedIn.value = false
        _customerScreen.value = CustomerScreen.LOGIN
        customerNavStack.clear()
        customerNavStack.add(CustomerScreen.LOGIN)
    }

    fun swapPickupAndDrop() {
        val temp = _pickupLocation.value
        _pickupLocation.value = _dropLocation.value
        _dropLocation.value = temp
        refreshFareEstimates()
    }

    fun setSpecialInstructions(notes: String) {
        _specialInstructions.value = notes
    }

    fun setDriverTip(amount: Int) {
        _driverTipAmount.value = amount
    }

    fun setRatingStars(stars: Int) {
        _ratingStars.value = stars
    }

    fun setRatingComment(comment: String) {
        _ratingComment.value = comment
    }

    fun toggleRatingTag(tag: String) {
        val current = _selectedRatingTags.value.toMutableSet()
        if (current.contains(tag)) {
            current.remove(tag)
        } else {
            current.add(tag)
        }
        _selectedRatingTags.value = current
    }

    fun viewPastRideDetails(ride: RideEntity) {
        _selectedPastRide.value = ride
        navigateToCustomerScreen(CustomerScreen.RIDE_DETAILS)
    }

    fun bookAgainRide(ride: RideEntity) {
        val p = DelhiNcrData.LOCATIONS.firstOrNull { it.name.contains(ride.pickupAddress, true) }
            ?: DelhiLocation(id = "p_custom", name = ride.pickupAddress, landmark = "Pickup", zone = "Delhi NCR", lat = ride.pickupLat, lng = ride.pickupLng)
        val d = DelhiNcrData.LOCATIONS.firstOrNull { it.name.contains(ride.dropAddress, true) }
            ?: DelhiLocation(id = "d_custom", name = ride.dropAddress, landmark = "Destination", zone = "Delhi NCR", lat = ride.dropLat, lng = ride.dropLng)

        _pickupLocation.value = p
        _dropLocation.value = d
        _selectedVehicleCategory.value = ride.vehicleCategory
        refreshFareEstimates()
        navigateToCustomerScreen(CustomerScreen.CONFIRM_RIDE)
    }

    fun addMoneyToCustomerWallet(amount: Double) {
        val user = currentCustomer.value ?: return
        viewModelScope.launch {
            repository.addWalletBalance(user.userId, amount)
        }
    }

    fun submitCustomerRatingAndFinish() {
        val ride = _lastCompletedRide.value ?: activeCustomerRide.value
        val stars = _ratingStars.value
        val comment = _ratingComment.value.ifBlank { _selectedRatingTags.value.joinToString(", ") }
        
        if (ride != null) {
            viewModelScope.launch {
                repository.rateRide(ride.rideId, stars, comment)
                _lastCompletedRide.value = null
                _customerScreen.value = CustomerScreen.HOME
                customerNavStack.clear()
                customerNavStack.add(CustomerScreen.HOME)
            }
        } else {
            _customerScreen.value = CustomerScreen.HOME
            customerNavStack.clear()
            customerNavStack.add(CustomerScreen.HOME)
        }
    }

    fun setCustomerTab(tab: Int) {
        _customerTab.value = tab
    }

    fun setDriverTab(tab: Int) {
        _driverTab.value = tab
    }

    fun setAdminTab(tab: Int) {
        _adminTab.value = tab
    }

    fun selectDriverPartner(driverId: String) {
        _selectedDriverId.value = driverId
    }

    fun setPickup(location: DelhiLocation) {
        _pickupLocation.value = location
        refreshFareEstimates()
    }

    fun setDrop(location: DelhiLocation) {
        _dropLocation.value = location
        _isSearchingDestination.value = false
        refreshFareEstimates()
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun openDestinationSearch(open: Boolean) {
        _isSearchingDestination.value = open
    }

    fun selectVehicleCategory(category: VehicleCategory) {
        _selectedVehicleCategory.value = category
    }

    fun applyCoupon(code: String?) {
        _selectedCouponCode.value = code
        refreshFareEstimates()
    }

    fun setPaymentMethod(method: PaymentMethod) {
        _selectedPaymentMethod.value = method
    }

    fun toggleSafetyDialog(show: Boolean) {
        _showSafetyDialog.value = show
    }

    fun toggleRatingDialog(show: Boolean) {
        _showRatingDialog.value = show
    }

    fun refreshFareEstimates() {
        viewModelScope.launch {
            val estimates = repository.calculateFareEstimates(
                pickupLat = _pickupLocation.value.lat,
                pickupLng = _pickupLocation.value.lng,
                dropLat = _dropLocation.value.lat,
                dropLng = _dropLocation.value.lng,
                couponCode = _selectedCouponCode.value
            )
            _fareEstimates.value = estimates
        }
    }

    // Customer: Book Ride Action
    fun requestAndBookRide() {
        val user = currentCustomer.value ?: DelhiNcrData.INITIAL_USERS[0]
        val estimate = _fareEstimates.value[_selectedVehicleCategory.value] ?: return

        viewModelScope.launch {
            val ride = repository.createAndBookRide(
                customer = user,
                category = _selectedVehicleCategory.value,
                pickupAddress = _pickupLocation.value.name,
                dropAddress = _dropLocation.value.name,
                pickupLat = _pickupLocation.value.lat,
                pickupLng = _pickupLocation.value.lng,
                dropLat = _dropLocation.value.lat,
                dropLng = _dropLocation.value.lng,
                estimate = estimate,
                paymentMethod = _selectedPaymentMethod.value,
                couponCode = _selectedCouponCode.value
            )

            // Start Intelligent Driver Matching and Simulation
            startDriverMatchingAndSimulation(ride)
        }
    }

    private fun startDriverMatchingAndSimulation(ride: RideEntity) {
        simulationJob?.cancel()
        simulationJob = viewModelScope.launch {
            // State: Searching driver (radar animation)
            delay(2200)

            val candidates = repository.findMatchingDrivers(
                pickupLat = ride.pickupLat,
                pickupLng = ride.pickupLng,
                category = ride.vehicleCategory
            )

            val assignedDriver = candidates.firstOrNull()
            if (assignedDriver != null) {
                // If the selected driver matches the current driver partner, offer incoming request popup
                if (assignedDriver.driverId == _selectedDriverId.value) {
                    _incomingRideRequest.value = ride
                    startIncomingRequestCountdown(ride, assignedDriver)
                } else {
                    // Auto-assigned by platform matching engine
                    repository.assignDriverToRide(ride.rideId, assignedDriver)
                    simulateTripLifecycle(ride.rideId)
                }
            } else {
                repository.updateRideStatus(
                    ride.rideId,
                    RideStatus.CANCELLED_BY_SYSTEM,
                    "No drivers currently available in zone"
                )
            }
        }
    }

    private fun startIncomingRequestCountdown(ride: RideEntity, driver: com.example.yatraa.model.DriverCandidate) {
        incomingTimerJob?.cancel()
        incomingTimerJob = viewModelScope.launch {
            _incomingRequestSecondsLeft.value = 15
            while (_incomingRequestSecondsLeft.value > 0 && _incomingRideRequest.value != null) {
                delay(1000)
                _incomingRequestSecondsLeft.value -= 1
            }
            if (_incomingRideRequest.value != null) {
                // Timeout, pass to next driver
                _incomingRideRequest.value = null
                repository.assignDriverToRide(ride.rideId, driver)
                simulateTripLifecycle(ride.rideId)
            }
        }
    }

    fun driverAcceptIncomingRide() {
        incomingTimerJob?.cancel()
        val ride = _incomingRideRequest.value ?: return
        val driver = currentDriver.value ?: return
        _incomingRideRequest.value = null

        viewModelScope.launch {
            val candidate = com.example.yatraa.model.DriverCandidate(
                driverId = driver.driverId,
                name = driver.name,
                phone = driver.phone,
                rating = driver.rating,
                vehicleCategory = driver.vehicleCategory,
                vehicleNumber = driver.vehicleNumber,
                vehicleModel = driver.vehicleModel,
                currentLat = driver.currentLat,
                currentLng = driver.currentLng,
                etaMinutes = 3,
                distanceToPickupKm = 1.2
            )
            repository.assignDriverToRide(ride.rideId, candidate)
            simulateTripLifecycle(ride.rideId)
        }
    }

    fun driverRejectIncomingRide() {
        incomingTimerJob?.cancel()
        _incomingRideRequest.value = null
    }

    fun cancelActiveRide(reason: String = "Cancelled by user") {
        simulationJob?.cancel()
        viewModelScope.launch {
            activeCustomerRide.value?.let { ride ->
                repository.updateRideStatus(ride.rideId, RideStatus.CANCELLED_BY_CUSTOMER, reason)
            }
        }
    }

    // Driver: Passenger OTP verification
    fun verifyOtpAndStartRide(rideId: String, enteredOtp: String, expectedOtp: String): Boolean {
        if (enteredOtp == expectedOtp) {
            viewModelScope.launch {
                repository.updateRideStatus(rideId, RideStatus.IN_PROGRESS)
            }
            return true
        }
        return false
    }

    // Complete ride
    fun completeRide(rideId: String) {
        viewModelScope.launch {
            repository.updateRideStatus(rideId, RideStatus.COMPLETED)
            val updated = repository.allRides.firstOrNull()?.firstOrNull { it.rideId == rideId }
            if (updated != null) {
                _lastCompletedRide.value = updated
            }
            if (_currentRole.value == UserRole.CUSTOMER) {
                _customerScreen.value = CustomerScreen.RIDE_COMPLETED
            } else {
                _showRatingDialog.value = true
            }
        }
    }

    // Submit rating
    fun submitRating(stars: Int, comment: String) {
        val ride = _lastCompletedRide.value ?: activeCustomerRide.value
        _showRatingDialog.value = false
        if (ride != null) {
            viewModelScope.launch {
                repository.rateRide(ride.rideId, stars, comment)
                _lastCompletedRide.value = null
                if (_currentRole.value == UserRole.CUSTOMER) {
                    _customerScreen.value = CustomerScreen.HOME
                    customerNavStack.clear()
                    customerNavStack.add(CustomerScreen.HOME)
                }
            }
        }
    }

    // Simulated lifecycle for smooth interactive demo
    private fun simulateTripLifecycle(rideId: String) {
        simulationJob?.cancel()
        simulationJob = viewModelScope.launch {
            // Driver is arriving
            delay(1500)
            repository.updateRideStatus(rideId, RideStatus.DRIVER_ARRIVING)

            // Driver arrives at pickup
            delay(3000)
            repository.updateRideStatus(rideId, RideStatus.DRIVER_ARRIVED)

            // Passenger boards & OTP verified
            delay(3000)
            repository.updateRideStatus(rideId, RideStatus.OTP_VERIFIED)
            delay(1000)
            repository.updateRideStatus(rideId, RideStatus.IN_PROGRESS)

            // Live route simulation
            for (step in 1..10) {
                _rideProgressFraction.value = step / 10f
                delay(1200)
            }

            // Ride completed
            repository.updateRideStatus(rideId, RideStatus.COMPLETED)
            val updated = repository.allRides.firstOrNull()?.firstOrNull { it.rideId == rideId }
            if (updated != null) {
                _lastCompletedRide.value = updated
            }
            if (_currentRole.value == UserRole.CUSTOMER) {
                _customerScreen.value = CustomerScreen.RIDE_COMPLETED
            } else {
                _showRatingDialog.value = true
            }
        }
    }

    // Driver Partner toggles
    fun toggleDriverOnline(isOnline: Boolean) {
        val driver = currentDriver.value ?: return
        viewModelScope.launch {
            repository.toggleDriverOnline(driver.driverId, isOnline)
        }
    }

    // Admin Operations
    fun adminUpdateDriverVerification(driverId: String, status: DriverVerificationStatus) {
        viewModelScope.launch {
            repository.updateDriverVerification(driverId, status, "ADMIN_DESK_DELHI")
        }
    }

    fun adminUpdatePricing(config: PricingConfigEntity) {
        viewModelScope.launch {
            repository.updatePricingConfig(config, "ADMIN_PRICING_DESK")
            refreshFareEstimates()
        }
    }

    fun adminToggleZone(zoneId: String, isActive: Boolean) {
        viewModelScope.launch {
            repository.toggleZone(zoneId, isActive, "ADMIN_ZONES_DESK")
        }
    }

    fun adminCreateCoupon(coupon: CouponEntity) {
        viewModelScope.launch {
            repository.addCoupon(coupon, "ADMIN_MARKETING_DESK")
            refreshFareEstimates()
        }
    }

    fun adminResolveTicket(ticketId: String, resolution: String) {
        viewModelScope.launch {
            repository.resolveSupportTicket(ticketId, resolution, "ADMIN_SUPPORT_LEAD")
        }
    }

    fun addSavedPlace(label: String, title: String, address: String, lat: Double, lng: Double) {
        viewModelScope.launch {
            repository.addSavedPlace(
                SavedPlaceEntity(
                    label = label,
                    title = title,
                    address = address,
                    lat = lat,
                    lng = lng
                )
            )
        }
    }

    fun deleteSavedPlace(placeId: Long) {
        viewModelScope.launch {
            repository.deleteSavedPlace(placeId)
        }
    }

    fun createSupportTicket(category: TicketCategory, description: String, rideId: String? = null) {
        val user = currentCustomer.value ?: return
        viewModelScope.launch {
            repository.createSupportTicket(
                SupportTicketEntity(
                    ticketId = "TCK-" + (1000..9999).random(),
                    userId = user.userId,
                    userRole = UserRole.CUSTOMER,
                    rideId = rideId,
                    category = category,
                    description = description
                )
            )
        }
    }
}
