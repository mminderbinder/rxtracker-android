package com.example.rxtracker.utils

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath

fun DrawScope.drawRound(color: Color) {
    drawCircle(color = color)
}

fun DrawScope.drawOvalShape(color: Color) {
    drawOval(color = color)
}

fun DrawScope.drawOblong(color: Color) {
    val r = size.height / 2f
    drawRoundRect(color = color, cornerRadius = CornerRadius(r))
}

fun DrawScope.drawCapsule(leftColor: Color, rightColor: Color) {
    val r = CornerRadius(size.height / 2f)
    val mid = size.width / 2f

    val leftClip = Path().apply {
        addRect(androidx.compose.ui.geometry.Rect(0f, 0f, mid, size.height))
    }
    val rightClip = Path().apply {
        addRect(androidx.compose.ui.geometry.Rect(mid, 0f, size.width, size.height))
    }

    clipPath(leftClip) { drawRoundRect(color = leftColor, cornerRadius = r) }
    clipPath(rightClip) { drawRoundRect(color = rightColor, cornerRadius = r) }
}

fun DrawScope.drawDiamond(color: Color) {
    val cx = size.width / 2f
    val cy = size.height / 2f
    val t = 0.15f

    val top = Offset(cx, 0f)
    val right = Offset(size.width, cy)
    val bot = Offset(cx, size.height)
    val left = Offset(0f, cy)

    fun lerp(a: Offset, b: Offset, f: Float) =
        Offset(a.x + (b.x - a.x) * f, a.y + (b.y - a.y) * f)

    drawPath(
        color = color,
        path = Path().apply {
            moveTo(lerp(top, right, t).x, lerp(top, right, t).y)
            lineTo(lerp(right, top, t).x, lerp(right, top, t).y)
            quadraticTo(right.x, right.y, lerp(right, bot, t).x, lerp(right, bot, t).y)
            lineTo(lerp(bot, right, t).x, lerp(bot, right, t).y)
            quadraticTo(bot.x, bot.y, lerp(bot, left, t).x, lerp(bot, left, t).y)
            lineTo(lerp(left, bot, t).x, lerp(left, bot, t).y)
            quadraticTo(left.x, left.y, lerp(left, top, t).x, lerp(left, top, t).y)
            lineTo(lerp(top, left, t).x, lerp(top, left, t).y)
            quadraticTo(top.x, top.y, lerp(top, right, t).x, lerp(top, right, t).y)
            close()
        }
    )
}

fun DrawScope.drawTriangle(color: Color) {
    val w = size.width
    val h = size.height
    val t = 0.30f

    val top = Offset(w / 2f, 0f)
    val bottomL = Offset(0f, h)
    val bottomR = Offset(w, h)

    fun lerp(a: Offset, b: Offset, f: Float) = Offset(a.x + (b.x - a.x) * f, a.y + (b.y - a.y) * f)

    val path = Path().apply {
        moveTo(lerp(top, bottomR, t).x, lerp(top, bottomR, t).y)
        quadraticTo(top.x, top.y, lerp(top, bottomL, t).x, lerp(top, bottomL, t).y)
        lineTo(lerp(bottomL, top, t).x, lerp(bottomL, top, t).y)
        quadraticTo(bottomL.x, bottomL.y, lerp(bottomL, bottomR, t).x, lerp(bottomL, bottomR, t).y)
        lineTo(lerp(bottomR, bottomL, t).x, lerp(bottomR, bottomL, t).y)
        quadraticTo(bottomR.x, bottomR.y, lerp(bottomR, top, t).x, lerp(bottomR, top, t).y)
        close()
    }
    drawPath(path = path, color = color)
}