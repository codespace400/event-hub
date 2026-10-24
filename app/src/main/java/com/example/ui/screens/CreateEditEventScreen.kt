package com.example.ui.screens

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Event
import com.example.ui.components.EventCoverImage
import com.example.ui.theme.AccentCoral
import com.example.ui.theme.BorderLight
import com.example.ui.theme.PurpleContainer
import com.example.ui.theme.TextPrimaryLight
import com.example.ui.theme.TextSecondaryLight
import com.example.ui.theme.VibrantPurple

@Composable
fun CreateEditEventScreen(
    editingEvent: Event?,
    onSave: (
        name: String,
        description: String,
        date: String,
        time: String,
        venue: String,
        coverImage: String,
        capacity: Int,
        price: String,
        category: String,
        isPublished: Boolean
    ) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf(editingEvent?.name ?: "") }
    var description by remember { mutableStateOf(editingEvent?.description ?: "") }
    var date by remember { mutableStateOf(editingEvent?.date ?: "Dec 12, 2026") }
    var time by remember { mutableStateOf(editingEvent?.time ?: "06:00 PM - 10:00 PM") }
    var venue by remember { mutableStateOf(editingEvent?.venue ?: "Skyline Grand Ballroom, Hall 4") }
    var coverImage by remember { mutableStateOf(editingEvent?.coverImage ?: "cover_tech_summit") }
    var capacityText by remember { mutableStateOf(editingEvent?.capacity?.toString() ?: "150") }
    var isFree by remember { mutableStateOf(editingEvent?.price?.equals("Free", ignoreCase = true) ?: false) }
    var priceText by remember { mutableStateOf(if (editingEvent?.price != null && editingEvent.price != "Free") editingEvent.price.replace("$", "") else "45") }
    var category by remember { mutableStateOf(editingEvent?.category ?: "Technology") }

    val categories = listOf("Technology", "Music", "Design", "Networking", "Conference", "Workshop")
    val availableCovers = listOf(
        Pair("cover_tech_summit", "Tech & Cyber"),
        Pair("cover_music_fest", "Neon Concert"),
        Pair("cover_design_expo", "Design Studio")
    )

    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        // Top App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.White)
                    .testTag("create_event_back_btn")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimaryLight
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = if (editingEvent != null) "Edit Event" else "Create New Event",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryLight
                )
                Text(
                    text = "Publish to public & attendee discovery",
                    fontSize = 12.sp,
                    color = TextSecondaryLight
                )
            }
        }

        // Cover Image Upload / Selection Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Image, contentDescription = null, tint = VibrantPurple, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "EVENT COVER IMAGE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondaryLight,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Current selected cover preview
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    EventCoverImage(
                        coverName = coverImage,
                        contentDescription = "Event Cover Preview",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("Choose from visual library:", fontSize = 12.sp, color = TextSecondaryLight, fontWeight = FontWeight.Medium)

                Spacer(modifier = Modifier.height(8.dp))

                // Thumbnails selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    availableCovers.forEach { (coverKey, label) ->
                        val isSelected = coverImage == coverKey
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) VibrantPurple else BorderLight,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable { coverImage = coverKey }
                                .padding(4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .clip(RoundedCornerShape(6.dp))
                            ) {
                                EventCoverImage(
                                    coverName = coverKey,
                                    contentDescription = label,
                                    modifier = Modifier.fillMaxSize()
                                )
                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(4.dp)
                                            .size(18.dp)
                                            .clip(CircleShape)
                                            .background(VibrantPurple),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(12.dp)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) VibrantPurple else TextSecondaryLight
                            )
                        }
                    }
                }
            }
        }

        // Details Form Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Event Name
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        errorMessage = null
                    },
                    label = { Text("Event Name *") },
                    placeholder = { Text("e.g. AI Product Summit 2026") },
                    leadingIcon = {
                        Icon(Icons.Default.Title, contentDescription = null, tint = VibrantPurple)
                    },
                    modifier = Modifier.fillMaxWidth().testTag("input_event_name"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VibrantPurple,
                        unfocusedBorderColor = BorderLight
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Category selector
                Text("Category", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextSecondaryLight)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { cat ->
                        val isSel = category == cat
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSel) VibrantPurple else Color(0xFFF3F4F6))
                                .clickable { category = cat }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = cat,
                                fontSize = 12.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSel) Color.White else TextSecondaryLight
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Event Description") },
                    placeholder = { Text("Describe what attendees will experience, speakers, agenda...") },
                    leadingIcon = {
                        Icon(Icons.Default.Description, contentDescription = null, tint = VibrantPurple)
                    },
                    modifier = Modifier.fillMaxWidth().height(100.dp).testTag("input_event_description"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VibrantPurple,
                        unfocusedBorderColor = BorderLight
                    ),
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Date and Time
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = date,
                        onValueChange = { date = it },
                        label = { Text("Date *") },
                        placeholder = { Text("Nov 20, 2026") },
                        leadingIcon = {
                            Icon(Icons.Default.CalendarToday, contentDescription = null, tint = VibrantPurple)
                        },
                        modifier = Modifier.weight(1f).testTag("input_event_date"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VibrantPurple,
                            unfocusedBorderColor = BorderLight
                        ),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = time,
                        onValueChange = { time = it },
                        label = { Text("Time *") },
                        placeholder = { Text("06:00 PM") },
                        modifier = Modifier.weight(1f).testTag("input_event_time"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VibrantPurple,
                            unfocusedBorderColor = BorderLight
                        ),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Venue
                OutlinedTextField(
                    value = venue,
                    onValueChange = { venue = it },
                    label = { Text("Venue / Location *") },
                    placeholder = { Text("e.g. Metro Convention Center, Hall B") },
                    leadingIcon = {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = AccentCoral)
                    },
                    modifier = Modifier.fillMaxWidth().testTag("input_event_venue"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VibrantPurple,
                        unfocusedBorderColor = BorderLight
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Capacity
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = capacityText,
                            onValueChange = { capacityText = it },
                            label = { Text("Capacity (Seats) *") },
                            leadingIcon = {
                                Icon(Icons.Default.Group, contentDescription = null, tint = VibrantPurple)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth().testTag("input_event_capacity"),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = VibrantPurple,
                                unfocusedBorderColor = BorderLight
                            ),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf(50, 100, 250).forEach { inc ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(PurpleContainer)
                                    .clickable {
                                        val cur = capacityText.toIntOrNull() ?: 0
                                        capacityText = (cur + inc).toString()
                                    }
                                    .padding(horizontal = 8.dp, vertical = 10.dp)
                            ) {
                                Text("+$inc", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = VibrantPurple)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Ticket Price Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Free Admission", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextPrimaryLight)
                        Text("No charge for registered attendees", fontSize = 12.sp, color = TextSecondaryLight)
                    }
                    Switch(
                        checked = isFree,
                        onCheckedChange = { isFree = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = VibrantPurple
                        )
                    )
                }

                if (!isFree) {
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = priceText,
                        onValueChange = { priceText = it },
                        label = { Text("Ticket Price ($ USD)") },
                        leadingIcon = {
                            Icon(Icons.Default.AttachMoney, contentDescription = null, tint = AccentCoral)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth().testTag("input_event_price"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VibrantPurple,
                            unfocusedBorderColor = BorderLight
                        ),
                        singleLine = true
                    )
                }
            }
        }

        // Error message banner if any
        if (errorMessage != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFFFE4E6))
                    .padding(12.dp)
            ) {
                Text(
                    text = errorMessage!!,
                    color = Color(0xFFE11D48),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Publish Actions Row with Accent Coral CTA
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Save as Draft
            OutlinedButton(
                onClick = {
                    if (name.isBlank()) {
                        errorMessage = "Please enter an event name"
                        return@OutlinedButton
                    }
                    val cap = capacityText.toIntOrNull() ?: 100
                    val finalPrice = if (isFree) "Free" else "$$priceText"
                    onSave(
                        name,
                        description,
                        date,
                        time,
                        venue,
                        coverImage,
                        cap,
                        finalPrice,
                        category,
                        false
                    )
                },
                modifier = Modifier.weight(1f).height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save Draft", fontWeight = FontWeight.SemiBold, color = TextSecondaryLight)
            }

            // Publish Button (Coral #FB7185)
            Button(
                onClick = {
                    if (name.isBlank()) {
                        errorMessage = "Please enter an event name"
                        return@Button
                    }
                    val cap = capacityText.toIntOrNull() ?: 100
                    val finalPrice = if (isFree) "Free" else "$$priceText"
                    onSave(
                        name,
                        description,
                        date,
                        time,
                        venue,
                        coverImage,
                        cap,
                        finalPrice,
                        category,
                        true
                    )
                },
                modifier = Modifier.weight(1.3f).height(50.dp).testTag("publish_event_btn"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentCoral)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Publish, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (editingEvent != null) "Update Event" else "Publish Event",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 15.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
