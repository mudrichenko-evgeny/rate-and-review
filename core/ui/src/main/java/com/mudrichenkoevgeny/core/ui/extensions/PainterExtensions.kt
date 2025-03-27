package com.mudrichenkoevgeny.core.ui.extensions

import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp

fun Painter.addPadding(padding: Dp): Painter {
    return object : Painter() {
        override val intrinsicSize = this@addPadding.intrinsicSize

        override fun DrawScope.onDraw() {
            val paddingPx = with(density) { padding.toPx() }
            translate(paddingPx, paddingPx) {
                with(this@addPadding) {
                    draw(size = size.copy(width = size.width - 2 * paddingPx, height = size.height - 2 * paddingPx))
                }
            }
        }
    }
}