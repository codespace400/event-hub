package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.AccentCoral
import com.example.ui.theme.CoralContainer
import com.example.ui.theme.OnCoralContainer
import com.example.ui.theme.OnPurpleContainer
import com.example.ui.theme.PurpleContainer
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusSuccessContainer
import com.example.ui.theme.VibrantPurple

@Composable
fun EventCoverImage(
    coverName: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current
    val resourceId = when (coverName) {
        "cover_tech_summit" -> R.drawable.cover_tech_summit
        "cover_music_fest" -> R.drawable.cover_music_fest
        "cover_design_expo" -> R.drawable.cover_design_expo
        else -> {
            val dynamicId = context.resources.getIdentifier(coverName, "drawable", context.packageName)
            if (dynamicId != 0) dynamicId else R.drawable.cover_tech_summit
        }
    }

    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = resourceId),
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun StatusBadge(
    text: String,
    isCheckedIn: Boolean,
    modifier: Modifier = Modifier
) {
    val bgColor = if (isCheckedIn) StatusSuccessContainer else PurpleContainer
    val textColor = if (isCheckedIn) StatusSuccess else OnPurpleContainer

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun CategoryPill(
    category: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Black.copy(alpha = 0.55f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = category.uppercase(),
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun PriceBadge(
    price: String,
    modifier: Modifier = Modifier
) {
    val isFree = price.equals("Free", ignoreCase = true)
    val bgColor = if (isFree) CoralContainer else VibrantPurple
    val textColor = if (isFree) OnCoralContainer else Color.White

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = price,
            color = textColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
