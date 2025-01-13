package ru.glebik.tinkoff_fintech.feature.chat.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import ru.glebik.core.widget.R
import ru.glebik.core.widget.sp

class EmojiView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0,
    defTheme: Int = 0,
) : View(
    context, attributeSet, defStyle, defTheme
) {

    var emojiCode: String = ""
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    var emojiName: String = ""
        set(value) {
            if (field != value) {
                field = value
            }
        }

    var count: Int = 0
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    private val textToDraw
        get() = "$emojiCode $count"

    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 22f.sp(context)
    }

    private val textRect = Rect()

    init {
        context.withStyledAttributes(attributeSet, R.styleable.EmojiView) {
            getInt(R.styleable.EmojiView_count, 0)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        textPaint.getTextBounds(textToDraw, 0, textToDraw.length, textRect)

        val actualWidth =
            resolveSize(paddingStart + paddingEnd + textRect.width(), widthMeasureSpec)

        val actualHeight =
            resolveSize(paddingTop + paddingBottom + textRect.height(), heightMeasureSpec)

        setMeasuredDimension(actualWidth, actualHeight)
    }

    override fun onDraw(canvas: Canvas) {
        val topOffset = height / 2 - textRect.exactCenterY()
        canvas.drawText(textToDraw, paddingStart.toFloat(), topOffset, textPaint)
    }
}