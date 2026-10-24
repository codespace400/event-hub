package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Attendee
import com.example.model.Event
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
import kotlinx.coroutines.launch

@Composable
fun AttendeeListScreen(
    event: Event,
    attendees: List<Attendee>,
    searchQuery: String,
    selectedFilter: String,
    onSearchChange: (String) -> Unit,
    onFilterChange: (String) -> Unit,
    onToggleCheckIn: (String) -> Unit,
    onExport: () -> Unit,
    onOpenCheckInScanner: () -> Unit,
    onBack: () -> Unit,
    exportBannerMessage: String?,
    onDismissExportMessage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val filterTabs = listOf("All", "Checked-In", "Pending")
    val checkedInCount = attendees.count { it.isCheckedIn }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
            .statusBarsPadding()
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .testTag("attendees_back_btn")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimaryLight
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Attendee Roster",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryLight
                )
                Text(
                    text = event.name,
                    fontSize = 12.sp,
                    color = VibrantPurple,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )
            }

            // Export Button (Downloads/Copies CSV)
            OutlinedButton(
                onClick = onExport,
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
                modifier = Modifier.testTag("export_attendees_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.FileDownload,
                    contentDescription = null,
                    tint = VibrantPurple,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Export",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = VibrantPurple
                )
            }
        }

        // Export Confirmation Banner
        if (exportBannerMessage != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = StatusSuccessContainer)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = StatusSuccess, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = exportBannerMessage,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF065F46)
                        )
                    }
                    IconButton(onClick = onDismissExportMessage, modifier = Modifier.size(24.dp)) {
                        Text("✕", fontSize = 12.sp, color = Color(0xFF065F46), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Summary Bar & Fast Scan Button
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "$checkedInCount of ${attendees.size} Checked In",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryLight
                    )
                    Text(
                        text = "Tap any row to manually toggle check-in",
                        fontSize = 11.sp,
                        color = TextSecondaryLight
                    )
                }

                // Check-in scanner button with coral accent
                Button(
                    onClick = onOpenCheckInScanner,
                    colors = ButtonDefaults.buttonColors(containerColor = AccentCoral),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("attendees_open_scanner_btn")
                ) {
                    Icon(Icons.Default.QrCodeScanner, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Scanner", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }

        // Search Input
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = { Text("Search by name, email, or ticket code...") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, tint = VibrantPurple)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .testTag("attendee_search_input"),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = VibrantPurple,
                unfocusedBorderColor = BorderLight
            ),
            singleLine = true
        )

        // Filter Tabs (All / Checked-In / Pending)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filterTabs.forEach { tab ->
                val isSelected = selectedFilter == tab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) VibrantPurple else Color.White)
                        .clickable { onFilterChange(tab) }
                        .padding(vertical = 8.dp)
                        .testTag("attendee_filter_$tab"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tab,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else TextSecondaryLight
                    )
                }
            }
        }

        // Attendee Table / Rows
        if (attendees.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFFCBD5E1), modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("No registrants match your search", fontWeight = FontWeight.SemiBold, color = TextSecondaryLight)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(attendees, key = { it.id }) { attendee ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onToggleCheckIn(attendee.id) }
                            .testTag("attendee_row_${attendee.id}"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Left: Avatar & Info
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val initials = attendee.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("").uppercase()
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(if (attendee.isCheckedIn) StatusSuccessContainer else PurpleContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = initials,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = if (attendee.isCheckedIn) StatusSuccess else VibrantPurple
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = attendee.name,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimaryLight
                                    )
                                    Text(
                                        text = attendee.email,
                                        fontSize = 12.sp,
                                        color = TextSecondaryLight
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = attendee.ticketCode,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace,
                                            color = VibrantPurple
                                        )
                                        Text(
                                            text = " • ${attendee.quantity} ticket",
                                            fontSize = 11.sp,
                                            color = TextSecondaryLight
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            // Right: Check-In Status Column / Action
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (attendee.isCheckedIn) StatusSuccessContainer else CoralContainer)
                                    .clickable { onToggleCheckIn(attendee.id) }
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (attendee.isCheckedIn) Icons.Default.Check else Icons.Default.HourglassEmpty,
                                        contentDescription = null,
                                        tint = if (attendee.isCheckedIn) StatusSuccess else OnCoralContainer,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (attendee.isCheckedIn) "Checked In" else "Pending",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (attendee.isCheckedIn) StatusSuccess else OnCoralContainer
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
