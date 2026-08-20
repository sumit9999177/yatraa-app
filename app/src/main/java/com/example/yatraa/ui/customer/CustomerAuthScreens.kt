package com.example.yatraa.ui.customer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricRickshaw
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Moped
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Slate100
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
import com.example.ui.theme.YatraaSaffronLight
import com.example.yatraa.model.CustomerScreen
import com.example.yatraa.ui.YatraaMainViewModel
import kotlinx.coroutines.delay

/**
 * Screen 1: Splash Screen
 */
@Composable
fun CustomerSplashScreen(
    viewModel: YatraaMainViewModel,
    modifier: Modifier = Modifier
) {
    val isLoggedIn by viewModel.isCustomerLoggedIn.collectAsState()

    LaunchedEffect(Unit) {
        delay(2200)
        if (isLoggedIn) {
            viewModel.navigateToCustomerScreen(CustomerScreen.HOME)
        } else {
            viewModel.navigateToCustomerScreen(CustomerScreen.LOGIN)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(YatraaNavy)
            .testTag("customer_splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            // Yatraa Logo Badge
            Surface(
                modifier = Modifier.size(100.dp),
                shape = RoundedCornerShape(28.dp),
                color = YatraaSaffron,
                shadowElevation = 12.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.ElectricRickshaw,
                        contentDescription = "Yatraa Logo",
                        tint = Color.White,
                        modifier = Modifier.size(56.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "YATRAA",
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 4.sp
            )

            Text(
                text = "यात्रा • Aapki Apni Sawari",
                color = YatraaSaffronLight,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Delhi-NCR's Fast, Safe & Transparent Bike + Auto Mobility",
                color = Slate400,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            CircularProgressIndicator(
                color = YatraaSaffron,
                strokeWidth = 3.dp,
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Connecting Delhi-NCR Zones...",
                color = Slate400,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = {
                    if (isLoggedIn) {
                        viewModel.navigateToCustomerScreen(CustomerScreen.HOME)
                    } else {
                        viewModel.navigateToCustomerScreen(CustomerScreen.LOGIN)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = YatraaSaffronDark),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("splash_continue_btn")
            ) {
                Text("Get Started", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
            }
        }
    }
}

/**
 * Screen 2: Login / Mobile Number Screen
 */
@Composable
fun CustomerLoginScreen(
    viewModel: YatraaMainViewModel,
    modifier: Modifier = Modifier
) {
    val phone by viewModel.loginPhone.collectAsState()
    val error by viewModel.otpError.collectAsState()
    val isLoading by viewModel.isAuthLoading.collectAsState()
    var termsAgreed by remember { mutableStateOf(true) }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .testTag("customer_login_screen"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Spacer(modifier = Modifier.height(24.dp))

                // Brand Header
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.size(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = YatraaSaffron
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.ElectricRickshaw,
                                contentDescription = "Yatraa",
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "YATRAA",
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp,
                            color = YatraaNavy,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Delhi-NCR Passenger Login",
                            fontSize = 12.sp,
                            color = Slate600
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Enter your mobile number",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = YatraaNavy
                )

                Text(
                    text = "We will send a 4-digit verification code to your phone",
                    fontSize = 14.sp,
                    color = Slate600,
                    modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                )

                // Phone Input with +91 Prefix
                OutlinedTextField(
                    value = phone,
                    onValueChange = { viewModel.setLoginPhone(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("login_phone_input"),
                    leadingIcon = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(start = 12.dp, end = 8.dp)
                        ) {
                            Text("🇮🇳 +91", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = YatraaNavy)
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(24.dp)
                                    .background(Slate200)
                            )
                        }
                    },
                    trailingIcon = {
                        if (phone.length == 10) {
                            Icon(Icons.Default.CheckCircle, contentDescription = "Valid", tint = YatraaEmerald)
                        }
                    },
                    placeholder = { Text("10-digit mobile number", color = Slate400) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = YatraaSaffron,
                        unfocusedBorderColor = Slate200,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Slate100
                    )
                )

                if (error != null) {
                    Text(
                        text = error!!,
                        color = YatraaCoral,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 4.dp, top = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Delhi-NCR Zones Badge
                Card(
                    colors = CardDefaults.cardColors(containerColor = YatraaSaffronLight.copy(alpha = 0.35f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = YatraaSaffronDark, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Active in Central Delhi, South Delhi, Noida, Gurgaon & Dwarka",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = YatraaNavy
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Terms Checkbox
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { termsAgreed = !termsAgreed }
                ) {
                    Checkbox(
                        checked = termsAgreed,
                        onCheckedChange = { termsAgreed = it },
                        colors = CheckboxDefaults.colors(checkedColor = YatraaSaffronDark),
                        modifier = Modifier.testTag("terms_checkbox")
                    )
                    Text(
                        text = "I agree to Yatraa's Terms of Service & Privacy Policy",
                        fontSize = 12.sp,
                        color = Slate600,
                        lineHeight = 16.sp
                    )
                }
            }

            Column(modifier = Modifier.padding(bottom = 16.dp)) {
                Button(
                    onClick = { viewModel.sendLoginOtp() },
                    enabled = phone.length == 10 && termsAgreed && !isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = YatraaSaffronDark,
                        disabledContainerColor = Slate200
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("login_submit_btn")
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(22.dp))
                    } else {
                        Text("Get Verification Code", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Demo Mode: Any 10-digit number is accepted. Default code is 4821",
                    fontSize = 11.sp,
                    color = Slate400,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

/**
 * Screen 3: OTP Verification Screen
 */
@Composable
fun CustomerOtpScreen(
    viewModel: YatraaMainViewModel,
    modifier: Modifier = Modifier
) {
    val phone by viewModel.loginPhone.collectAsState()
    val otpInput by viewModel.otpInput.collectAsState()
    val generatedOtp by viewModel.generatedMockOtp.collectAsState()
    val timer by viewModel.otpTimerSeconds.collectAsState()
    val error by viewModel.otpError.collectAsState()
    val isLoading by viewModel.isAuthLoading.collectAsState()

    Surface(
        modifier = modifier
            .fillMaxSize()
            .testTag("customer_otp_screen"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Spacer(modifier = Modifier.height(16.dp))

                TextButton(
                    onClick = { viewModel.navigateToCustomerScreen(CustomerScreen.LOGIN) },
                    modifier = Modifier.padding(start = 0.dp)
                ) {
                    Text("← Change Mobile Number", color = YatraaSaffronDark, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Verify Mobile Number",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = YatraaNavy
                )

                Text(
                    text = "Enter the 4-digit code sent to +91 $phone",
                    fontSize = 14.sp,
                    color = Slate600,
                    modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                )

                // OTP Input Field
                OutlinedTextField(
                    value = otpInput,
                    onValueChange = { viewModel.setOtpInput(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("otp_input_field"),
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = YatraaSaffronDark)
                    },
                    placeholder = { Text("Enter OTP (e.g. 4821)", color = Slate400) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = YatraaSaffron,
                        unfocusedBorderColor = Slate200,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Slate100
                    )
                )

                if (error != null) {
                    Text(
                        text = error!!,
                        color = YatraaCoral,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 4.dp, top = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Auto Fill Helper Card for Testing
                Card(
                    colors = CardDefaults.cardColors(containerColor = Slate100),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Mock OTP Generated: $generatedOtp",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = YatraaNavy
                            )
                            Text(
                                text = "Tap auto-fill for instant testing",
                                fontSize = 11.sp,
                                color = Slate600
                            )
                        }

                        Button(
                            onClick = { viewModel.autoFillMockOtp() },
                            colors = ButtonDefaults.buttonColors(containerColor = YatraaNavy),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("autofill_otp_btn")
                        ) {
                            Text("Auto-fill", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Resend Timer Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (timer > 0) {
                        Text(
                            text = "Resend OTP in ${timer}s",
                            fontSize = 13.sp,
                            color = Slate600,
                            fontWeight = FontWeight.Medium
                        )
                    } else {
                        TextButton(
                            onClick = { viewModel.resendOtp() },
                            modifier = Modifier.testTag("resend_otp_btn")
                        ) {
                            Text("Resend Code Now", color = YatraaSaffronDark, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }

            Column(modifier = Modifier.padding(bottom = 16.dp)) {
                Button(
                    onClick = { viewModel.verifyCustomerOtp() },
                    enabled = otpInput.isNotBlank() && !isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = YatraaSaffronDark,
                        disabledContainerColor = Slate200
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("verify_otp_btn")
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(22.dp))
                    } else {
                        Text("Verify & Continue", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
            }
        }
    }
}

/**
 * Screen 4: Profile Setup Screen
 */
@Composable
fun CustomerProfileSetupScreen(
    viewModel: YatraaMainViewModel,
    modifier: Modifier = Modifier
) {
    val name by viewModel.profileName.collectAsState()
    val email by viewModel.profileEmail.collectAsState()
    val emergencyName by viewModel.profileEmergencyName.collectAsState()
    val emergencyPhone by viewModel.profileEmergencyPhone.collectAsState()
    val language by viewModel.profileLanguage.collectAsState()

    val languages = listOf("English", "हिन्दी", "ਪੰਜਾਬੀ")

    Surface(
        modifier = modifier
            .fillMaxSize()
            .testTag("customer_profile_setup_screen"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Welcome to Yatraa!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = YatraaNavy
                )

                Text(
                    text = "Set up your passenger profile for personalized rides and safety alerts",
                    fontSize = 13.sp,
                    color = Slate600,
                    modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                )

                // Full Name
                Text("Full Name", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = YatraaNavy)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { viewModel.setProfileName(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("profile_name_input"),
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = YatraaSaffronDark) },
                    placeholder = { Text("Your full name", color = Slate400) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Email
                Text("Email Address (Optional)", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = YatraaNavy)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { viewModel.setProfileEmail(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("profile_email_input"),
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = YatraaSaffronDark) },
                    placeholder = { Text("e.g. name@example.com", color = Slate400) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Preferred Language
                Text("Preferred App Language", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = YatraaNavy)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    languages.forEach { lang ->
                        val isSelected = language == lang
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.setProfileLanguage(lang) },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) YatraaSaffronDark else Slate100,
                            border = if (isSelected) null else BorderStroke(1.dp, Slate200)
                        ) {
                            Text(
                                text = lang,
                                modifier = Modifier.padding(vertical = 12.dp),
                                textAlign = TextAlign.Center,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 13.sp,
                                color = if (isSelected) Color.White else Slate800
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Safety & Emergency Contact Section
                Card(
                    colors = CardDefaults.cardColors(containerColor = Slate100),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = YatraaEmerald, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Emergency Safety Contact",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = YatraaNavy
                            )
                        }
                        Text(
                            text = "Used for 1-tap SOS trip sharing with your family",
                            fontSize = 11.sp,
                            color = Slate600,
                            modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
                        )

                        OutlinedTextField(
                            value = emergencyName,
                            onValueChange = { viewModel.setProfileEmergencyName(it) },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Contact Name (e.g. Mom / Spouse)", color = Slate400) },
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = emergencyPhone,
                            onValueChange = { viewModel.setProfileEmergencyPhone(it) },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Contact Phone (+91 ...)", color = Slate400) },
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)) {
                Button(
                    onClick = { viewModel.saveCustomerProfile() },
                    enabled = name.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = YatraaSaffronDark),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("save_profile_btn")
                ) {
                    Text("Save & Explore Delhi", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}
