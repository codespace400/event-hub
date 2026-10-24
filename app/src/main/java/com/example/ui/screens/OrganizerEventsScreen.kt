package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Event
import com.example.model.UserSession
import com.example.ui.components.EventCard
import com.example.ui.theme.AccentCoral
import com.example.ui.theme.BorderLight
import com.example.ui.theme.CoralDark
import com.example.ui.theme.PurpleContainer
import com.example.ui.theme.PurpleDark
import com.example.ui.theme.TextPrimaryLight
import com.example.ui.theme.TextSecondaryLight
import com.example.ui.theme.VibrantPurple

@Composable
fun OrganizerEventsScreen(
    events: List<Event>,
    userSession: UserSession,
    searchQuery: String,
    selectedCategory: String,
    onSearchChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onEventClick: (Event) -> Unit,
    onCreateEventClick: () -> Unit,
    onSwitchRole: () -> Unit,
    onOpenCheckInScanner: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf("All", "Technology", "Music", "Design", "Networking")

    val totalRegistrations = events.sumOf { it.registeredCount }
    val totalCapacity = events.sumOf { it.capacity }
    val averageFill = if (totalCapacity > 0) ((totalRegistrations.toFloat() / totalCapacity) * 100).toInt() else 0

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateEventClick,
                containerColor = AccentCoral,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.testTag("create_event_fab")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create Event",
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    ) { innerPadding ->
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
                                    text = "ORGANIZER",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                        Text(
                            text = "Welcome, ${userSession.name}",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }

                    // Role switch button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White.copy(alpha = 0.2f))
                            .clickable(onClick = onSwitchRole)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("switch_to_attendee_btn"),
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
                                text = "Attendee View",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // Quick Stats Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Stat 1: Total Events
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Event, contentDescription = null, tint = VibrantPurple, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Events", fontSize = 11.sp, color = TextSecondaryLight, fontWeight = FontWeight.Medium)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "${events.size}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimaryLight)
                    }
                }

                // Stat 2: Total Registrations
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Group, contentDescription = null, tint = AccentCoral, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Attendees", fontSize = 11.sp, color = TextSecondaryLight, fontWeight = FontWeight.Medium)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "$totalRegistrations", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimaryLight)
                    }
                }

                // Stat 3: Capacity Rate
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.TrendingUp, contentDescription = null, tint = VibrantPurple, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Occupancy", fontSize = 11.sp, color = TextSecondaryLight, fontWeight = FontWeight.Medium)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "$averageFill%", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = VibrantPurple)
                    }
                }
            }

            // Search Bar & Action Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchChange,
                    placeholder = { Text("Search events, venues...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = VibrantPurple)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("events_search_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = VibrantPurple,
                        unfocusedBorderColor = BorderLight
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(10.dp))

                // Primary Coral "+ Create Event" CTA Button
                Button(
                    onClick = onCreateEventClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentCoral),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp),
                    modifier = Modifier.testTag("create_event_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Create",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Category Chips Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { category ->
                    val isSelected = selectedCategory == category
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) VibrantPurple else Color.White)
                            .clickable { onCategoryChange(category) }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                            .testTag("category_chip_$category"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = category,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else TextSecondaryLight
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Events List
            if (events.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Event,
                            contentDescription = null,
                            tint = Color(0xFFCBD5E1),
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No events found",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryLight
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Try adjusting your search or create your first event!",
                            fontSize = 13.sp,
                            color = TextSecondaryLight
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onCreateEventClick,
                            colors = ButtonDefaults.buttonColors(containerColor = AccentCoral),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("+ Create Event", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(events, key = { it.id }) { event ->
                        EventCard(
                            event = event,
                            onClick = { onEventClick(event) },
                            isOrganizerView = true
                        )
                    }
                }
            }
        }
    }
}
