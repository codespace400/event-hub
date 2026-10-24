package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CheckInResult
import com.example.model.Attendee
import com.example.model.Event
import com.example.ui.theme.AccentCoral
import com.example.ui.theme.BorderLight
import com.example.ui.theme.CoralContainer
import com.example.ui.theme.OnCoralContainer
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusSuccessContainer
import com.example.ui.theme.StatusWarning
import com.example.ui.theme.StatusWarningContainer
import com.example.ui.theme.TextPrimaryLight
import com.example.ui.theme.TextSecondaryLight
import com.example.ui.theme.VibrantPurple

@Composable
fun CheckInScannerScreen(
    event: Event,
    attendees: List<Attendee>,
    scannerQuery: String,
    lastCheckInResult: CheckInResult?,
    recentScans: List<CheckInResult>,
    onQueryChange: (String) -> Unit,
    onSubmitCheckIn: (String?) -> Unit,
    onClearResult: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var flashEnabled by remember { mutableStateOf(false) }

    // Animated scanning laser line
    val infiniteTransition = rememberInfiniteTransition(label = "scanner")
    val laserOffsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laser"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A)) // Dark immersive scanner viewport
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        // Top App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.15f))
                    .testTag("scanner_back_btn")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "QR Check-In Scanner",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = event.name,
                    fontSize = 12.sp,
                    color = Color(0xFFA78BFA),
                    maxLines = 1
                )
            }

            IconButton(
                onClick = { flashEnabled = !flashEnabled },
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (flashEnabled) AccentCoral else Color.White.copy(alpha = 0.15f))
            ) {
                Icon(
                    imageVector = Icons.Default.FlashOn,
                    contentDescription = "Torch",
                    tint = Color.White
                )
            }
        }

        // Camera Viewfinder Box with Reticle Corner Brackets & Laser
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(270.dp)
                .padding(horizontal = 24.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            // Dark Camera Preview Simulation Surface
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF1E293B))
            )

            // Viewfinder Reticle Frame (220 x 220 dp)
            Box(
                modifier = Modifier
                    .size(220.dp)
                    .testTag("qr_viewfinder_frame")
            ) {
                // Draw custom 4 corner L-brackets and scanning laser beam
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val cornerLength = 28.dp.toPx()
                    val strokeW = 4.dp.toPx()
                    val cornerColor = Color(0xFFFB7185) // Accent Coral

                    // Top-Left corner
                    drawLine(cornerColor, Offset(0f, 0f), Offset(cornerLength, 0f), strokeW)
                    drawLine(cornerColor, Offset(0f, 0f), Offset(0f, cornerLength), strokeW)

                    // Top-Right corner
                    drawLine(cornerColor, Offset(size.width - cornerLength, 0f), Offset(size.width, 0f), strokeW)
                    drawLine(cornerColor, Offset(size.width, 0f), Offset(size.width, cornerLength), strokeW)

                    // Bottom-Left corner
                    drawLine(cornerColor, Offset(0f, size.height - cornerLength), Offset(0f, size.height), strokeW)
                    drawLine(cornerColor, Offset(0f, size.height), Offset(cornerLength, size.height), strokeW)

                    // Bottom-Right corner
                    drawLine(cornerColor, Offset(size.width - cornerLength, size.height), Offset(size.width, size.height), strokeW)
                    drawLine(cornerColor, Offset(size.width, size.height - cornerLength), Offset(size.width, size.height), strokeW)

                    // Animated Laser line
                    val currentLaserY = size.height * laserOffsetY
                    drawLine(
                        color = Color(0xFFFB7185),
                        start = Offset(10.dp.toPx(), currentLaserY),
                        end = Offset(size.width - 10.dp.toPx(), currentLaserY),
                        strokeWidth = 2.5.dp.toPx()
                    )
                }

                // Inner QR target icon
                Icon(
                    imageVector = Icons.Default.QrCode,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier
                        .size(80.dp)
                        .align(Alignment.Center)
                )
            }

            // Viewfinder instruction text
            Text(
                text = "Align attendee QR code inside frame",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp)
            )
        }

        // Live Feedback States Overlay (Success, Already Checked In, Not Found, Wrong Event)
        AnimatedVisibility(visible = lastCheckInResult != null) {
            val result = lastCheckInResult
            if (result != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    when (result) {
                        is CheckInResult.Success -> {
                            Card(
                                modifier = Modifier.fillMaxWidth().testTag("check_in_success_feedback"),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF064E3B)),
                                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF10B981))
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = Color(0xFF34D399),
                                        modifier = Modifier.size(36.dp)
                                    )
                                    Spacer(modifier = Modifier.width(14.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "CHECK-IN SUCCESSFUL!",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color(0xFF34D399),
                                            letterSpacing = 0.5.sp
                                        )
                                        Text(
                                            text = result.attendee.name,
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "Pass: ${result.attendee.ticketCode} • ${result.attendee.ticketType}",
                                            fontSize = 12.sp,
                                            color = Color(0xFFA7F3D0)
                                        )
                                    }
                                    IconButton(onClick = onClearResult) {
                                        Icon(Icons.Default.Close, contentDescription = "Dismiss", tint = Color.White)
                                    }
                                }
                            }
                        }

                        is CheckInResult.AlreadyCheckedIn -> {
                            Card(
                                modifier = Modifier.fillMaxWidth().testTag("check_in_already_checked_in_feedback"),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF78350F)),
                                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFF59E0B))
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = null,
                                        tint = Color(0xFFFBBF24),
                                        modifier = Modifier.size(36.dp)
                                    )
                                    Spacer(modifier = Modifier.width(14.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "ALREADY CHECKED IN!",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color(0xFFFBBF24),
                                            letterSpacing = 0.5.sp
                                        )
                                        Text(
                                            text = result.attendee.name,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "Scanned earlier at ${result.time}. Duplicate entry detected.",
                                            fontSize = 12.sp,
                                            color = Color(0xFFFDE68A)
                                        )
                                    }
                                    IconButton(onClick = onClearResult) {
                                        Icon(Icons.Default.Close, contentDescription = "Dismiss", tint = Color.White)
                                    }
                                }
                            }
                        }

                        is CheckInResult.NotFound -> {
                            Card(
                                modifier = Modifier.fillMaxWidth().testTag("check_in_invalid_feedback"),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF881337)),
                                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFF43F5E))
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Error,
                                        contentDescription = null,
                                        tint = Color(0xFFFDA4AF),
                                        modifier = Modifier.size(36.dp)
                                    )
                                    Spacer(modifier = Modifier.width(14.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "TICKET NOT FOUND",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color(0xFFFDA4AF),
                                            letterSpacing = 0.5.sp
                                        )
                                        Text(
                                            text = "No registrant matches '${result.query}'",
                                            fontSize = 14.sp,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "Verify ticket code or register attendee directly.",
                                            fontSize = 12.sp,
                                            color = Color(0xFFFECDD3)
                                        )
                                    }
                                    IconButton(onClick = onClearResult) {
                                        Icon(Icons.Default.Close, contentDescription = "Dismiss", tint = Color.White)
                                    }
                                }
                            }
                        }

                        is CheckInResult.WrongEvent -> {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF581C87)),
                                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFA855F7))
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Error,
                                        contentDescription = null,
                                        tint = Color(0xFFD8B4FE),
                                        modifier = Modifier.size(36.dp)
                                    )
                                    Spacer(modifier = Modifier.width(14.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "DIFFERENT EVENT PASS",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color(0xFFD8B4FE)
                                        )
                                        Text(
                                            text = "${result.attendee.name}'s ticket belongs to '${result.eventName}'",
                                            fontSize = 13.sp,
                                            color = Color.White
                                        )
                                    }
                                    IconButton(onClick = onClearResult) {
                                        Icon(Icons.Default.Close, contentDescription = "Dismiss", tint = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Quick Simulated Scan Chips (Crucial for testability in simulator!)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Text(
                text = "TAP TEST TICKET TO SIMULATE SCAN",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF94A3B8),
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                attendees.take(6).forEach { attendee ->
                    val isCheckedIn = attendee.isCheckedIn
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .border(
                                width = 1.dp,
                                color = if (isCheckedIn) Color(0xFF10B981) else Color(0xFFFB7185),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .background(Color(0xFF1E293B))
                            .clickable { onSubmitCheckIn(attendee.ticketCode) }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .testTag("scan_chip_${attendee.ticketCode}")
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = attendee.name.split(" ").firstOrNull() ?: attendee.name,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isCheckedIn) "✓" else "Scan",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCheckedIn) Color(0xFF34D399) else Color(0xFFFB7185)
                                )
                            }
                            Text(
                                text = attendee.ticketCode,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFFA78BFA)
                            )
                        }
                    }
                }

                // Invalid test chip
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0xFFEF4444), RoundedCornerShape(12.dp))
                        .background(Color(0xFF1E293B))
                        .clickable { onSubmitCheckIn("EH-9999-FAKE") }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Column {
                        Text("Fake Ticket", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFCA5A5))
                        Text("EH-9999-X", fontSize = 11.sp, fontFamily = FontFamily.Monospace, color = Color(0xFFFCA5A5))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Manual Search Fallback Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "MANUAL SEARCH FALLBACK",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF94A3B8),
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = scannerQuery,
                        onValueChange = onQueryChange,
                        placeholder = { Text("Enter code (EH-8910-T) or name...", color = Color(0xFF64748B)) },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFFA78BFA))
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("manual_checkin_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color(0xFF0F172A),
                            unfocusedContainerColor = Color(0xFF0F172A),
                            focusedBorderColor = Color(0xFFA78BFA),
                            unfocusedBorderColor = Color(0xFF334155)
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Button(
                        onClick = { onSubmitCheckIn(null) },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentCoral),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(52.dp).testTag("manual_checkin_submit_btn")
                    ) {
                        Text("Check In", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                    }
                }
            }
        }

        // Recent Scans History
        if (recentScans.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .padding(bottom = 32.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.History, contentDescription = null, tint = Color(0xFF94A3B8), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "RECENT CHECK-IN ACTIVITY",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF94A3B8),
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                recentScans.take(4).forEach { scan ->
                    when (scan) {
                        is CheckInResult.Success -> {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF1E293B))
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("✓", color = Color(0xFF34D399), fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(scan.attendee.name, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                }
                                Text(scan.attendee.ticketCode, color = Color(0xFFA78BFA), fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                            }
                        }
                        is CheckInResult.AlreadyCheckedIn -> {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF1E293B))
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("⚠", color = Color(0xFFFBBF24), fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(scan.attendee.name, color = Color.White, fontSize = 13.sp)
                                }
                                Text("Duplicate", color = Color(0xFFFBBF24), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}
