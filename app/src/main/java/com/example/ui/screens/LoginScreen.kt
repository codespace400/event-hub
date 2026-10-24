package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.UserRole
import com.example.ui.theme.AccentCoral
import com.example.ui.theme.BorderLight
import com.example.ui.theme.CoralDark
import com.example.ui.theme.PurpleContainer
import com.example.ui.theme.PurpleDark
import com.example.ui.theme.TextPrimaryLight
import com.example.ui.theme.TextSecondaryLight
import com.example.ui.theme.VibrantPurple

@Composable
fun LoginScreen(
    onLoginSuccess: (UserRole) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedRole by remember { mutableStateOf(UserRole.ORGANIZER) }
    var email by remember { mutableStateOf("alex.organizer@eventhub.com") }
    var password by remember { mutableStateOf("••••••••") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Hero Header with Gradient Background & EventHub Branding
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(PurpleDark, VibrantPurple)
                    )
                )
                .statusBarsPadding()
                .padding(vertical = 32.dp, horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Logo Icon badge
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                        .padding(6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_eventhub_logo),
                        contentDescription = "EventHub Logo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(16.dp))
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "EventHub",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = (-0.5).sp
                )

                Text(
                    text = "Event Registration & Live Check-In Platform",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.85f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Role Segmented Toggle Control
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .testTag("role_toggle_card"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "SELECT EXPERIENCE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondaryLight,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF3F4F6))
                        .padding(4.dp)
                ) {
                    // Organizer Tab
                    val isOrg = selectedRole == UserRole.ORGANIZER
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isOrg) VibrantPurple else Color.Transparent)
                            .clickable {
                                selectedRole = UserRole.ORGANIZER
                                email = "alex.organizer@eventhub.com"
                            }
                            .padding(vertical = 12.dp)
                            .testTag("toggle_organizer"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AdminPanelSettings,
                                contentDescription = null,
                                tint = if (isOrg) Color.White else TextSecondaryLight,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Organizer",
                                fontSize = 14.sp,
                                fontWeight = if (isOrg) FontWeight.Bold else FontWeight.Medium,
                                color = if (isOrg) Color.White else TextSecondaryLight
                            )
                        }
                    }

                    // Attendee Tab
                    val isAtt = selectedRole == UserRole.ATTENDEE
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isAtt) VibrantPurple else Color.Transparent)
                            .clickable {
                                selectedRole = UserRole.ATTENDEE
                                email = "jordan.attendee@example.com"
                            }
                            .padding(vertical = 12.dp)
                            .testTag("toggle_attendee"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.ConfirmationNumber,
                                contentDescription = null,
                                tint = if (isAtt) Color.White else TextSecondaryLight,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Attendee",
                                fontSize = 14.sp,
                                fontWeight = if (isAtt) FontWeight.Bold else FontWeight.Medium,
                                color = if (isAtt) Color.White else TextSecondaryLight
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Role features highlight card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (selectedRole == UserRole.ORGANIZER) PurpleContainer else Color(0xFFFFE4E6))
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (selectedRole == UserRole.ORGANIZER) Icons.Default.QrCodeScanner else Icons.Default.ConfirmationNumber,
                    contentDescription = null,
                    tint = if (selectedRole == UserRole.ORGANIZER) VibrantPurple else CoralDark,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (selectedRole == UserRole.ORGANIZER) {
                        "Admin Mode: Create & edit events, view registrant rosters, and scan tickets."
                    } else {
                        "Attendee Mode: Browse public events, register in 1 tap, and access your QR tickets."
                    },
                    fontSize = 12.sp,
                    color = TextPrimaryLight,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Credentials Input Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Sign in to EventHub",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryLight
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = null, tint = VibrantPurple)
                    },
                    modifier = Modifier.fillMaxWidth().testTag("login_email_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VibrantPurple,
                        unfocusedBorderColor = BorderLight
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = VibrantPurple)
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth().testTag("login_password_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VibrantPurple,
                        unfocusedBorderColor = BorderLight
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Sign in CTA (Coral accent #FB7185)
                Button(
                    onClick = { onLoginSuccess(selectedRole) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("login_submit_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentCoral)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (selectedRole == UserRole.ORGANIZER) "Enter as Organizer" else "Enter as Attendee",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Quick persona shortcuts
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "OR QUICK DEMO LOGIN",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondaryLight,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Alex Organizer quick button
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, VibrantPurple.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .clickable { onLoginSuccess(UserRole.ORGANIZER) }
                        .padding(12.dp)
                        .testTag("quick_organizer_demo"),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("👔 Alex Morgan", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimaryLight)
                        Text("Organizer Admin", fontSize = 11.sp, color = VibrantPurple, fontWeight = FontWeight.Medium)
                    }
                }

                // Jordan Attendee quick button
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, AccentCoral.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .clickable { onLoginSuccess(UserRole.ATTENDEE) }
                        .padding(12.dp)
                        .testTag("quick_attendee_demo"),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🎟️ Jordan Attendee", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimaryLight)
                        Text("View Tickets & Pass", fontSize = 11.sp, color = AccentCoral, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}
