package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Attendee
import com.example.model.Event
import com.example.ui.components.CategoryPill
import com.example.ui.components.EventCoverImage
import com.example.ui.components.PriceBadge
import com.example.ui.components.QrCodeView
import com.example.ui.theme.AccentCoral
import com.example.ui.theme.BorderLight
import com.example.ui.theme.CoralContainer
import com.example.ui.theme.OnCoralContainer
import com.example.ui.theme.PurpleContainer
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusSuccessContainer
import com.example.ui.theme.TextPrimaryLight
import com.example.ui.theme.TextSecondaryLight
import com.example.ui.theme.VibrantPurple

@Composable
fun RegisterEventScreen(
    event: Event,
    lastRegistration: Pair<Attendee, Event>?,
    onRegister: (name: String, email: String, phone: String, quantity: Int) -> Unit,
    onBack: () -> Unit,
    onGoToMyTickets: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf(1) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    val isConfirmed = lastRegistration != null && lastRegistration.second.id == event.id

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
            .verticalScroll(rememberScrollState())
    ) {
        if (isConfirmed && lastRegistration != null) {
            // CONFIRMATION SCREEN WITH QR TICKET
            val attendee = lastRegistration.first
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Success Badge
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(StatusSuccessContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = StatusSuccess,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Registration Confirmed!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimaryLight
                )

                Text(
                    text = "You are all set for ${event.name}. Your digital pass is generated below.",
                    fontSize = 13.sp,
                    color = TextSecondaryLight,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Boarding Pass Ticket Container with QR Code
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("confirmed_qr_ticket_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Ticket Header
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                        ) {
                            EventCoverImage(
                                coverName = event.coverImage,
                                contentDescription = event.name,
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f))
                                        )
                                    )
                            )
                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(16.dp)
                            ) {
                                CategoryPill(category = event.category)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = event.name,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        // Ticket QR Code Display
                        Spacer(modifier = Modifier.height(20.dp))

                        QrCodeView(
                            data = attendee.ticketCode,
                            size = 180.dp,
                            showCenterBadge = true,
                            modifier = Modifier.testTag("ticket_qr_code")
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = attendee.ticketCode,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.Monospace,
                            color = VibrantPurple,
                            letterSpacing = 2.sp
                        )

                        Text(
                            text = "Show this QR code at venue check-in",
                            fontSize = 12.sp,
                            color = TextSecondaryLight
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Attendee details row
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF9FAFB))
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("ATTENDEE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextSecondaryLight)
                                    Text(attendee.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimaryLight)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("TICKET TYPE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextSecondaryLight)
                                    Text("${attendee.quantity}x ${attendee.ticketType}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimaryLight)
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("DATE & TIME", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextSecondaryLight)
                                    Text("${event.date} • ${event.time}", fontSize = 12.sp, color = TextPrimaryLight, fontWeight = FontWeight.Medium)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("STATUS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextSecondaryLight)
                                    Text("CONFIRMED PASS", fontSize = 12.sp, color = StatusSuccess, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Actions: Go to My Tickets & Back
                Button(
                    onClick = onGoToMyTickets,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("confirmation_go_to_tickets_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentCoral)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ConfirmationNumber, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("View In My Tickets", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Return to Events", fontWeight = FontWeight.SemiBold, color = TextSecondaryLight)
                }
            }
        } else {
            // REGISTRATION FORM
            // Hero Image Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                EventCoverImage(
                    coverName = event.coverImage,
                    contentDescription = event.name,
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Black.copy(alpha = 0.5f), Color.Transparent, Color.Black.copy(alpha = 0.75f))
                            )
                        )
                )

                // Back Button
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(16.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                        .testTag("register_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                // Event Badges & Title at bottom of hero
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CategoryPill(category = event.category)
                        PriceBadge(price = event.price)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = event.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // Event Quick Logistics strip
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CalendarToday, contentDescription = null, tint = VibrantPurple, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("${event.date} • ${event.time}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimaryLight)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = AccentCoral, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(event.venue, fontSize = 13.sp, color = TextSecondaryLight)
                    }
                }
            }

            // Registration Form Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "ATTENDEE REGISTRATION",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondaryLight,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Name
                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            errorMsg = null
                        },
                        label = { Text("Full Name *") },
                        placeholder = { Text("e.g. Jordan Miller") },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null, tint = VibrantPurple)
                        },
                        modifier = Modifier.fillMaxWidth().testTag("reg_name_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VibrantPurple,
                            unfocusedBorderColor = BorderLight
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            errorMsg = null
                        },
                        label = { Text("Email Address *") },
                        placeholder = { Text("jordan@example.com") },
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = null, tint = VibrantPurple)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth().testTag("reg_email_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VibrantPurple,
                            unfocusedBorderColor = BorderLight
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Phone
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone Number") },
                        placeholder = { Text("+1 (555) 000-0000") },
                        leadingIcon = {
                            Icon(Icons.Default.Phone, contentDescription = null, tint = VibrantPurple)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth().testTag("reg_phone_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VibrantPurple,
                            unfocusedBorderColor = BorderLight
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Ticket Quantity
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Ticket Quantity", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextPrimaryLight)
                            Text("Standard Pass", fontSize = 12.sp, color = TextSecondaryLight)
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(1, 2, 3, 4).forEach { q ->
                                val isSelected = quantity == q
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) VibrantPurple else Color(0xFFF3F4F6))
                                        .clickable { quantity = q },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$q",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = if (isSelected) Color.White else TextPrimaryLight
                                    )
                                }
                            }
                        }
                    }

                    if (errorMsg != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFFFE4E6))
                                .padding(10.dp)
                        ) {
                            Text(
                                text = errorMsg!!,
                                color = Color(0xFFE11D48),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // "+ Register" Coral CTA Button (#FB7185)
                    Button(
                        onClick = {
                            if (name.isBlank()) {
                                errorMsg = "Please enter your full name"
                                return@Button
                            }
                            if (email.isBlank() || !email.contains("@")) {
                                errorMsg = "Please enter a valid email address"
                                return@Button
                            }
                            onRegister(name, email, phone, quantity)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("submit_registration_btn"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AccentCoral)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ConfirmationNumber, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "+ Register (${if (event.price == "Free") "Free" else "$quantity x ${event.price}"})",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
