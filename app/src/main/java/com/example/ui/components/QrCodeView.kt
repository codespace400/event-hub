package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AccentCoral
import com.example.ui.theme.VibrantPurple
import kotlin.math.abs

/**
 * Renders a high-fidelity, deterministic QR Code representation for a given ticket code.
 * Includes official QR finder patterns, timing belts, and deterministic pseudo-random data modules.
 */
@Composable
fun QrCodeView(
    data: String,
    modifier: Modifier = Modifier,
    size: Dp = 180.dp,
    moduleColor: Color = Color(0xFF1E1B4B),
    backgroundColor: Color = Color.White,
    showCenterBadge: Boolean = true
) {
    val matrixSize = 25
    val grid = remember(data) {
        generateQrMatrix(data, matrixSize)
    }

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize().aspectRatio(1f)) {
            val moduleWidth = this.size.width / matrixSize
            val moduleHeight = this.size.height / matrixSize

            for (row in 0 until matrixSize) {
                for (col in 0 until matrixSize) {
                    if (grid[row][col]) {
                        drawRect(
                            color = moduleColor,
                            topLeft = Offset(col * moduleWidth, row * moduleHeight),
                            size = Size(moduleWidth + 0.5f, moduleHeight + 0.5f)
                        )
                    }
                }
            }
        }

        if (showCenterBadge) {
            Box(
                modifier = Modifier
                    .size(size * 0.22f)
                    .clip(RoundedCornerShape(6.dp))
                    .background(VibrantPurple),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ConfirmationNumber,
                    contentDescription = null,
                    tint = AccentCoral,
                    modifier = Modifier.size(size * 0.13f)
                )
            }
        }
    }
}

private fun generateQrMatrix(data: String, size: Int): Array<BooleanArray> {
    val matrix = Array(size) { BooleanArray(size) }

    // Helper to draw standard 7x7 QR finder patterns
    fun drawFinderPattern(startRow: Int, startCol: Int) {
        for (r in 0 until 7) {
            for (c in 0 until 7) {
                val isOuter = r == 0 || r == 6 || c == 0 || c == 6
                val isInner = r in 2..4 && c in 2..4
                matrix[startRow + r][startCol + c] = isOuter || isInner
            }
        }
    }

    // 1. Draw 3 standard corner finder patterns
    drawFinderPattern(0, 0)
    drawFinderPattern(0, size - 7)
    drawFinderPattern(size - 7, 0)

    // 2. Clear separators around finder patterns
    for (i in 0..7) {
        if (i < size) {
            matrix[7][i] = false
            matrix[i][7] = false
            matrix[7][size - 1 - i] = false
            matrix[i][size - 8] = false
            matrix[size - 8][i] = false
            matrix[size - 1 - i][7] = false
        }
    }

    // 3. Timing patterns
    for (i in 8 until size - 8) {
        matrix[6][i] = (i % 2 == 0)
        matrix[i][6] = (i % 2 == 0)
    }

    // 4. Fill data modules deterministically based on input hash
    val hash = abs(data.hashCode())
    var currentBit = hash
    for (r in 0 until size) {
        for (c in 0 until size) {
            // Skip finder patterns, separators and center badge area
            val inTopLeftFinder = r <= 7 && c <= 7
            val inTopRightFinder = r <= 7 && c >= size - 8
            val inBottomLeftFinder = r >= size - 8 && c <= 7
            val inCenterBadge = r in 10..14 && c in 10..14
            val inTimingPattern = (r == 6 && c in 8 until size - 8) || (c == 6 && r in 8 until size - 8)

            if (!inTopLeftFinder && !inTopRightFinder && !inBottomLeftFinder && !inCenterBadge && !inTimingPattern) {
                currentBit = (currentBit * 31 + r * 17 + c * 13 + data.length) % 1000000007
                matrix[r][c] = (currentBit % 2 == 0) || ((r + c + hash) % 3 == 0)
            }
        }
    }

    return matrix
}
