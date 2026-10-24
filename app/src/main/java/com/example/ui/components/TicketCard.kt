package com.example.ui.components

import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Attendee
import com.example.model.Event
import com.example.ui.theme.AccentCoral
import com.example.ui.theme.CoralContainer
import com.example.ui.theme.OnCoralContainer
import com.example.ui.theme.OnPurpleContainer
import com.example.ui.theme.PurpleContainer
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusSuccessContainer
import com.example.ui.theme.TextPrimaryLight
import com.example.ui.theme.TextSecondaryLight
import com.example.ui.theme.VibrantPurple

@Composable
fun TicketCard(
    attendee: Attendee,
    event: Event,
    onExpandTicket: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onExpandTicket)
            .testTag("ticket_card_${attendee.ticketCode}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header: Event Title and Cover Mini-Banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F3FF))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    EventCoverImage(
                        coverName = event.coverImage,
                        contentDescription = event.name,
                        modifier = Modifier.fillMaxWidth().height(60.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    CategoryPill(category = event.category)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = event.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryLight,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${event.date} • ${event.time}",
                        fontSize = 12.sp,
                        color = VibrantPurple,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Perforated Divider with Side Notches
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .background(Color.White)
            ) {
                // Dashed line across center
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .align(Alignment.Center)
                ) {
                    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 12f), 0f)
                    drawLine(
                        color = Color(0xFFCBD5E1),
                        start = Offset(24.dp.toPx(), 0f),
                        end = Offset(size.width - 24.dp.toPx(), 0f),
                        strokeWidth = 2.dp.toPx(),
                        pathEffect = pathEffect
                    )
                }
            }

            // Ticket Body: Attendee info + QR Code Preview
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "ATTENDEE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF94A3B8),
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = attendee.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryLight
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "VENUE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF94A3B8),
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = event.venue,
                        fontSize = 12.sp,
                        color = TextSecondaryLight,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "PASS CODE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF94A3B8),
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = attendee.ticketCode,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace,
                        color = VibrantPurple
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // QR Code View
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    QrCodeView(
                        data = attendee.ticketCode,
                        size = 96.dp,
                        showCenterBadge = true
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tap to enlarge",
                        fontSize = 10.sp,
                        color = VibrantPurple,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Bottom Bar: Status Badge and Action
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFAFAFA))
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (attendee.isCheckedIn) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(StatusSuccessContainer)
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = StatusSuccess,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "CHECKED IN",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = StatusSuccess
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(CoralContainer)
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCode,
                            contentDescription = null,
                            tint = OnCoralContainer,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "READY TO SCAN",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnCoralContainer
                        )
                    }
                }

                Text(
                    text = "${attendee.quantity}x ${attendee.ticketType}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondaryLight
                )
            }
        }
    }
}
