package com.bear.englishlearning.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val BearBrown = Color(0xFF8D6E63)
private val BearBrownDark = Color(0xFF6D4C41)
private val BearEarInner = Color(0xFFFFAB91)
private val BearSnout = Color(0xFFD7CCC8)
private val BearEyeNose = Color(0xFF3E2723)
private val BearBlush = Color(0x20FF6B6B)
private val BearMouth = Color(0xFF5D4037)

@Composable
fun BearIcon(
    modifier: Modifier = Modifier,
    size: Dp = 80.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val cx = w / 2f
        val cy = h * 0.52f

        val headRadius = w * 0.38f
        val earRadius = w * 0.14f
        val earInnerRadius = w * 0.08f

        // Ear positions
        val earY = cy - headRadius * 0.72f
        val earLx = cx - headRadius * 0.65f
        val earRx = cx + headRadius * 0.65f

        // Left Ear (outer)
        drawCircle(BearBrownDark, earRadius, Offset(earLx, earY))
        // Right Ear (outer)
        drawCircle(BearBrownDark, earRadius, Offset(earRx, earY))
        // Left Ear (inner)
        drawCircle(BearEarInner, earInnerRadius, Offset(earLx, earY))
        // Right Ear (inner)
        drawCircle(BearEarInner, earInnerRadius, Offset(earRx, earY))

        // Head
        drawCircle(BearBrown, headRadius, Offset(cx, cy))

        // Snout
        val snoutCx = cx
        val snoutCy = cy + headRadius * 0.28f
        val snoutRx = headRadius * 0.45f
        val snoutRy = headRadius * 0.34f
        drawOval(
            color = BearSnout,
            topLeft = Offset(snoutCx - snoutRx, snoutCy - snoutRy),
            size = Size(snoutRx * 2, snoutRy * 2)
        )

        // Eyes
        val eyeY = cy - headRadius * 0.1f
        val eyeOffsetX = headRadius * 0.35f
        val eyeRx = headRadius * 0.1f
        val eyeRy = headRadius * 0.12f
        // Left Eye
        drawOval(
            color = BearEyeNose,
            topLeft = Offset(cx - eyeOffsetX - eyeRx, eyeY - eyeRy),
            size = Size(eyeRx * 2, eyeRy * 2)
        )
        // Right Eye
        drawOval(
            color = BearEyeNose,
            topLeft = Offset(cx + eyeOffsetX - eyeRx, eyeY - eyeRy),
            size = Size(eyeRx * 2, eyeRy * 2)
        )

        // Eye highlights
        val highlightR = headRadius * 0.04f
        drawCircle(Color.White, highlightR, Offset(cx - eyeOffsetX + eyeRx * 0.3f, eyeY - eyeRy * 0.4f))
        drawCircle(Color.White, highlightR, Offset(cx + eyeOffsetX + eyeRx * 0.3f, eyeY - eyeRy * 0.4f))

        // Nose
        val noseRx = headRadius * 0.14f
        val noseRy = headRadius * 0.1f
        drawOval(
            color = BearEyeNose,
            topLeft = Offset(cx - noseRx, snoutCy - snoutRy * 0.35f - noseRy),
            size = Size(noseRx * 2, noseRy * 2)
        )

        // Mouth (cute W shape)
        val mouthY = snoutCy + snoutRy * 0.15f
        val mouthW = headRadius * 0.15f
        drawMouth(cx, mouthY, mouthW, headRadius * 0.1f)

        // Blush
        val blushY = cy + headRadius * 0.1f
        val blushOffsetX = headRadius * 0.6f
        val blushRx = headRadius * 0.14f
        val blushRy = headRadius * 0.09f
        drawOval(
            color = BearBlush,
            topLeft = Offset(cx - blushOffsetX - blushRx, blushY - blushRy),
            size = Size(blushRx * 2, blushRy * 2)
        )
        drawOval(
            color = BearBlush,
            topLeft = Offset(cx + blushOffsetX - blushRx, blushY - blushRy),
            size = Size(blushRx * 2, blushRy * 2)
        )
    }
}

private fun DrawScope.drawMouth(cx: Float, y: Float, halfWidth: Float, depth: Float) {
    val path = Path().apply {
        moveTo(cx - halfWidth * 1.5f, y)
        quadraticBezierTo(cx - halfWidth * 0.5f, y + depth, cx, y)
        quadraticBezierTo(cx + halfWidth * 0.5f, y + depth, cx + halfWidth * 1.5f, y)
    }
    drawPath(path, BearMouth, style = Stroke(width = halfWidth * 0.15f))
}
