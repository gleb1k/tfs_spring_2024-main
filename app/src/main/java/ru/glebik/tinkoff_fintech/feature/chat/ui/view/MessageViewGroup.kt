package ru.glebik.tinkoff_fintech.feature.chat.ui.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import ru.glebik.tinkoff_fintech.R

class MessageViewGroup @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0,
    defTheme: Int = 0,
) : BaseMessageViewGroup(
    context, attributeSet, defStyle, defTheme
) {

    init {
        inflate(context, R.layout.chat_layout, this)
    }

    val avatar: ImageView = findViewById(R.id.avatar)

    val name: TextView = findViewById(R.id.name)

    override val comment: TextView = findViewById(R.id.comment)

    override val textBackgroundView: LinearLayout = findViewById(R.id.textBackgroundView)

    override val flex: FlexboxLayout = findViewById(R.id.flexboxContainter)

    fun changeName(newValue: String) {
        if (name.text == newValue) return
        name.text = newValue
        requestLayout()
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)

        measureChildWithMargins(avatar, widthMeasureSpec, 0, heightMeasureSpec, 0)
        val avatarWidth = avatar.measuredWidth + avatar.marginStart + avatar.marginEnd

        measureChildWithMargins(
            textBackgroundView,
            widthMeasureSpec,
            avatarWidth + paddingEnd + paddingStart,
            heightMeasureSpec,
            0
        )
        val textBackgroundWidth =
            textBackgroundView.measuredWidth + textBackgroundView.marginStart + textBackgroundView.marginEnd

        measureChildWithMargins(
            flex,
            widthMeasureSpec,
            avatarWidth + paddingEnd + paddingStart,
            heightMeasureSpec,
            0
        )
        val flexWidth = flex.measuredWidth + flex.marginStart + flex.marginEnd

        val childWidth = maxOf(
            avatarWidth + textBackgroundWidth,
            avatarWidth + flexWidth
        ) + paddingEnd + paddingStart

        val avatarHeight = avatar.measuredHeight + avatar.marginTop + avatar.marginBottom
        val nameHeight = name.measuredHeight + name.marginTop + name.marginBottom
        val commentHeight = comment.measuredHeight + comment.marginTop + comment.marginBottom
        val flexHeight = flex.measuredHeight + flex.marginTop + flex.marginBottom

        val actualHeight = maxOf(
            avatarHeight,
            nameHeight + commentHeight + flexHeight
        ) + paddingTop + paddingBottom


        setMeasuredDimension(minOf(childWidth, parentWidth), actualHeight)
    }

    //p1 - left
    //p2 - top
    //p3 - right
    //p4 - bottom
    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
        val avatarStart = paddingStart + avatar.marginStart
        val avatarEnd = avatarStart + avatar.measuredWidth
        val avatarTop = paddingTop + avatar.marginTop
        val avatarBottom = avatarTop + avatar.measuredHeight

        avatar.layout(
            avatarStart,
            avatarTop,
            avatarEnd,
            avatarBottom
        )

        val textBgStart = textBackgroundView.marginStart + avatarEnd + avatar.marginEnd
        val textBgEnd = textBgStart + textBackgroundView.measuredWidth
        val textBgTop = paddingTop + textBackgroundView.marginTop
        val textBgBottom = textBgTop + textBackgroundView.measuredHeight

        textBackgroundView.layout(
            textBgStart,
            textBgTop,
            textBgEnd,
            textBgBottom
        )

        flex.layout(
            textBgStart,
            textBgBottom + flex.marginTop + textBackgroundView.marginBottom,
            minOf(p3, textBgStart + flex.measuredWidth),
            textBgBottom + flex.marginTop + flex.measuredHeight + textBackgroundView.marginBottom
        )

    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

}