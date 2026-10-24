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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.Attendee
import com.example.model.Event
import com.example.model.UserSession
import com.example.ui.components.CategoryPill
import com.example.ui.components.QrCodeView
import com.example.ui.components.StatusBadge
import com.example.ui.components.TicketCard
import com.example.ui.theme.AccentCoral
import com.example.ui.theme.CoralContainer
import com.example.ui.theme.PurpleDark
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusSuccessContainer
import com.example.ui.theme.TextPrimaryLight
import com.example.ui.theme.TextSecondaryLight
import com.example.ui.theme.VibrantPurple

@Composable
fun MyTicketsScreen(
    tickets: List<Pair<Attendee, Event>>,
    userSession: UserSession,
    onBrowseEvents: () -> Unit,
    onSwitchRole: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("All") }
    var inspectedTicket by remember { mutableStateOf<Pair<Attendee, Event>?>(null) }

    val filteredTickets = tickets.filter { (attendee, _) ->
        when (selectedFilter) {
            "Valid" -> !attendee.isCheckedIn
            "Checked In" -> attendee.isCheckedIn
            else -> true
        }
    }

    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF9FAFB))
                .padding(innerPadding)
        ) {
            // Header Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(VibrantPurple, PurpleDark)
                        )
                    )
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "EventHub",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(AccentCoral)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "ATTENDEE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                        Text(
                            text = "Passes for ${userSession.name}",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }

                    // Role Switcher to Organizer
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White.copy(alpha = 0.2f))
                            .clickable(onClick = onSwitchRole)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("switch_to_organizer_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.SwapHoriz,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Organizer View",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // Subheader Row: Filter Tabs & Browse Events CTA
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Filter Tabs
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("All", "Valid", "Checked In").forEach { tab ->
                        val isSelected = selectedFilter == tab
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) VibrantPurple else Color.White)
                                .clickable { selectedFilter = tab }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .testTag("my_tickets_filter_$tab")
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

                // Browse Events Button (Accent Coral #FB7185)
                Button(
                    onClick = onBrowseEvents,
                    colors = ButtonDefaults.buttonColors(containerColor = AccentCoral),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("browse_events_cta_btn")
                ) {
                    Icon(Icons.Default.Explore, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Explore", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            // Ticket List
            if (filteredTickets.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.ConfirmationNumber,
                            contentDescription = null,
                            tint = Color(0xFFCBD5E1),
                            modifier = Modifier.size(60.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No tickets found",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryLight
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Register for upcoming events to view your digital QR tickets here.",
                            fontSize = 13.sp,
                            color = TextSecondaryLight,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onBrowseEvents,
                            colors = ButtonDefaults.buttonColors(containerColor = AccentCoral),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Browse Upcoming Events", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredTickets, key = { it.first.id }) { (attendee, event) ->
                        TicketCard(
                            attendee = attendee,
                            event = event,
                            onExpandTicket = { inspectedTicket = Pair(attendee, event) }
                        )
                    }
                }
            }
        }
    }

    // Fullscreen QR Code Inspection Modal
    if (inspectedTicket != null) {
        val (attendee, event) = inspectedTicket!!
        Dialog(onDismissRequest = { inspectedTicket = null }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .testTag("fullscreen_qr_modal"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CategoryPill(category = event.category)
                        IconButton(onClick = { inspectedTicket = null }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondaryLight)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = event.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryLight,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "${event.date} • ${event.time}",
                        fontSize = 13.sp,
                        color = VibrantPurple,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Enlarged QR Code
                    QrCodeView(
                        data = attendee.ticketCode,
                        size = 220.dp,
                        showCenterBadge = true
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = attendee.ticketCode,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = VibrantPurple,
                        letterSpacing = 2.sp
                    )

                    Text(
                        text = "${attendee.name} • ${attendee.ticketType}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondaryLight
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    if (attendee.isCheckedIn) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(StatusSuccessContainer)
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text("✓ ALREADY CHECKED IN", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = StatusSuccess)
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(CoralContainer)
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text("READY FOR ENTRANCE SCAN", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AccentCoral)
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { inspectedTicket = null },
                        colors = ButtonDefaults.buttonColors(containerColor = VibrantPurple),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Text("Done", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}
