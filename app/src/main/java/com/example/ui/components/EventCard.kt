package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Event
import com.example.ui.theme.AccentCoral
import com.example.ui.theme.BorderLight
import com.example.ui.theme.TextMutedLight
import com.example.ui.theme.TextPrimaryLight
import com.example.ui.theme.TextSecondaryLight
import com.example.ui.theme.VibrantPurple

@Composable
fun EventCard(
    event: Event,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isOrganizerView: Boolean = true
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("event_card_${event.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Cover Image with Top Badges and Gradient Scrim
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                EventCoverImage(
                    coverName = event.coverImage,
                    contentDescription = event.name,
                    modifier = Modifier.fillMaxWidth().height(150.dp)
                )

                // Bottom scrim
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                            )
                        )
                )

                // Top badges row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CategoryPill(category = event.category)
                    PriceBadge(price = event.price)
                }

                if (isOrganizerView && !event.isPublished) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(12.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFFEF3C7))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "DRAFT",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFD97706)
                        )
                    }
                }
            }

            // Card Body Info
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = event.name,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryLight,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Date & Time
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = VibrantPurple,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${event.date} • ${event.time}",
                        fontSize = 13.sp,
                        color = TextSecondaryLight,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Venue
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = AccentCoral,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = event.venue,
                        fontSize = 13.sp,
                        color = TextSecondaryLight,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Registration capacity indicator
                val ratio = if (event.capacity > 0) {
                    (event.registeredCount.toFloat() / event.capacity.toFloat()).coerceIn(0f, 1f)
                } else 0f
                val percentage = (ratio * 100).toInt()

                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Group,
                                contentDescription = null,
                                tint = TextMutedLight,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${event.registeredCount} / ${event.capacity} registered",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimaryLight
                            )
                        }

                        Text(
                            text = "$percentage% full",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (percentage >= 90) AccentCoral else VibrantPurple
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    LinearProgressIndicator(
                        progress = { ratio },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = if (percentage >= 90) AccentCoral else VibrantPurple,
                        trackColor = Color(0xFFF3F4F6)
                    )
                }
            }
        }
    }
}
