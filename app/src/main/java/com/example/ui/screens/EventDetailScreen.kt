package com.example.ui.screens

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Attendee
import com.example.model.Event
import com.example.ui.components.CategoryPill
import com.example.ui.components.EventCoverImage
import com.example.ui.components.PriceBadge
import com.example.ui.theme.AccentCoral
import com.example.ui.theme.BorderLight
import com.example.ui.theme.CoralContainer
import com.example.ui.theme.OnCoralContainer
import com.example.ui.theme.PurpleContainer
import com.example.ui.theme.PurpleDark
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.TextPrimaryLight
import com.example.ui.theme.TextSecondaryLight
import com.example.ui.theme.VibrantPurple

@Composable
fun EventDetailScreen(
    event: Event,
    attendees: List<Attendee>,
    onBack: () -> Unit,
    onViewAttendees: () -> Unit,
    onOpenCheckIn: () -> Unit,
    onEditEvent: (Event) -> Unit,
    onDeleteEvent: (String) -> Unit,
    onOpenPublicPage: (Event) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    val checkedInCount = attendees.count { it.isCheckedIn }
    val ratio = if (event.capacity > 0) {
        (event.registeredCount.toFloat() / event.capacity.toFloat()).coerceIn(0f, 1f)
    } else 0f
    val percentage = (ratio * 100).toInt()
    val spotsLeft = (event.capacity - event.registeredCount).coerceAtLeast(0)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
            .verticalScroll(rememberScrollState())
    ) {
        // Hero Cover Section with Gradient Scrim and Action Overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
        ) {
            EventCoverImage(
                coverName = event.coverImage,
                contentDescription = event.name,
                modifier = Modifier.fillMaxSize()
            )

            // Dark Top and Bottom Gradient Scrim
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.6f),
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.8f)
                            )
                        )
                    )
            )

            // Top navigation bar buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                        .testTag("event_detail_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(
                        onClick = { onEditEvent(event) },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.5f))
                            .testTag("event_detail_edit_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Event",
                            tint = Color.White
                        )
                    }

                    IconButton(
                        onClick = { showDeleteDialog = true },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.5f))
                            .testTag("event_detail_delete_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Event",
                            tint = AccentCoral
                        )
                    }
                }
            }

            // Bottom hero title info
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CategoryPill(category = event.category)
                    PriceBadge(price = event.price)
                    if (!event.isPublished) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFFEF3C7))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("DRAFT", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB45309))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = event.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    lineHeight = 30.sp
                )
            }
        }

        // Two Primary Organizer Action Buttons
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // "View Attendees" Button
                Button(
                    onClick = onViewAttendees,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("view_attendees_btn"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VibrantPurple)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Group, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Attendees", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White)
                            Text("${attendees.size} registrants", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                        }
                    }
                }

                // "Check-In" Button with Coral CTA Accent (#FB7185)
                Button(
                    onClick = onOpenCheckIn,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("check_in_scanner_btn"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentCoral)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.QrCodeScanner, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Check-In", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White)
                            Text("$checkedInCount checked in", fontSize = 11.sp, color = Color.White.copy(alpha = 0.85f))
                        }
                    }
                }
            }

            // Public Page / Register Attendee Preview Bar
            OutlinedButton(
                onClick = { onOpenPublicPage(event) },
                modifier = Modifier.fillMaxWidth().height(46.dp).testTag("preview_public_page_btn"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Visibility, contentDescription = null, tint = VibrantPurple, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Open Public Event / Register Page", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = VibrantPurple)
                }
            }
        }

        // Registration Progress Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "REGISTRATION PROGRESS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondaryLight,
                        letterSpacing = 1.sp
                    )

                    Text(
                        text = "$percentage% CAPACITY",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (percentage >= 90) AccentCoral else VibrantPurple
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "${event.registeredCount}",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            color = TextPrimaryLight
                        )
                        Text(
                            text = " / ${event.capacity} total seats",
                            fontSize = 14.sp,
                            color = TextSecondaryLight,
                            modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
                        )
                    }

                    Text(
                        text = "$spotsLeft left",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (spotsLeft <= 10) AccentCoral else StatusSuccess,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                LinearProgressIndicator(
                    progress = { ratio },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp)),
                    color = if (percentage >= 90) AccentCoral else VibrantPurple,
                    trackColor = Color(0xFFF3F4F6)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Stats breakdown strip
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFF8FAFC))
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Checked In", fontSize = 11.sp, color = TextSecondaryLight)
                        Text("$checkedInCount", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = StatusSuccess)
                    }
                    Box(modifier = Modifier.width(1.dp).height(24.dp).background(BorderLight))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Pending Arrival", fontSize = 11.sp, color = TextSecondaryLight)
                        Text("${(event.registeredCount - checkedInCount).coerceAtLeast(0)}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimaryLight)
                    }
                    Box(modifier = Modifier.width(1.dp).height(24.dp).background(BorderLight))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Ticket Price", fontSize = 11.sp, color = TextSecondaryLight)
                        Text(event.price, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = VibrantPurple)
                    }
                }
            }
        }

        // Event Metadata & Logistics Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "DATE & LOCATION",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondaryLight,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Date & Time
                Row(verticalAlignment = Alignment.Top) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(PurpleContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.CalendarToday, contentDescription = null, tint = VibrantPurple, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Date & Time", fontSize = 12.sp, color = TextSecondaryLight, fontWeight = FontWeight.Medium)
                        Text(event.date, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimaryLight)
                        Text(event.time, fontSize = 13.sp, color = TextSecondaryLight)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Venue
                Row(verticalAlignment = Alignment.Top) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(CoralContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = OnCoralContainer, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Venue", fontSize = 12.sp, color = TextSecondaryLight, fontWeight = FontWeight.Medium)
                        Text(event.venue, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimaryLight)
                    }
                }
            }
        }

        // About / Description Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "ABOUT THIS EVENT",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondaryLight,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = event.description,
                    fontSize = 14.sp,
                    color = TextPrimaryLight,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Organized by ${event.organizerName}",
                    fontSize = 12.sp,
                    color = TextSecondaryLight,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

    // Delete Confirmation Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Event?") },
            text = { Text("Are you sure you want to delete '${event.name}'? All registrants and tickets for this event will also be removed.") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteEvent(event.id)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentCoral)
                ) {
                    Text("Delete", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
