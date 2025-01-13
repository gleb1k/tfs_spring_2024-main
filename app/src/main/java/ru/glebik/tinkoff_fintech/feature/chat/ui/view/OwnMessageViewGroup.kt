package ru.glebik.tinkoff_fintech.feature.chat.ui.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import ru.glebik.tinkoff_fintech.R

class OwnMessageViewGroup @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0,
    defTheme: Int = 0,
) : BaseMessageViewGroup(
    context, attributeSet, defStyle, defTheme
) {

    init {
        inflate(context, R.layout.own_chat_layout, this)
    }

    override val comment: TextView = findViewById(R.id.comment)

    override val textBackgroundView: LinearLayout = findViewById(R.id.textBackgroundView)

    override val flex: FlexboxLayout = findViewById(R.id.flexboxContainter)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)

        measureChildWithMargins(
            textBackgroundView,
            widthMeasureSpec,
            paddingEnd + paddingStart,
            heightMeasureSpec,
            0
        )

        val textBackgroundWidth =
            textBackgroundView.measuredWidth + textBackgroundView.marginStart + textBackgroundView.marginEnd

        measureChildWithMargins(
            flex,
            widthMeasureSpec,
            paddingEnd + paddingStart,
            heightMeasureSpec,
            0
        )
        val flexWidth = flex.measuredWidth + flex.marginStart + flex.marginEnd

        val childWidth = maxOf(textBackgroundWidth, flexWidth) + paddingEnd + paddingStart

        val commentHeight =
            textBackgroundView.measuredHeight + textBackgroundView.marginTop + textBackgroundView.marginBottom
        val flexHeight = flex.measuredHeight + flex.marginTop + flex.marginBottom

        val actualHeight = commentHeight + flexHeight + paddingTop + paddingBottom

        setMeasuredDimension(minOf(childWidth, parentWidth), actualHeight)
    }

    //p1 - left
    //p2 - top
    //p3 - right
    //p4 - bottom
    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {

        val textBgStart = p3 - p1 - textBackgroundView.measuredWidth + paddingStart
        val textBgEnd = p3 - p1
//        val textBgStart = textBackgroundView.marginStart + paddingStart
//        val textBgEnd = minOf(p3, textBgStart + textBackgroundView.measuredWidth)
        val textBgTop = paddingTop + textBackgroundView.marginTop
        val textBgBottom = textBgTop + textBackgroundView.measuredHeight

        textBackgroundView.layout(
            textBgStart,
            textBgTop,
            textBgEnd,
            textBgBottom
        )

//        val flexStart = textBgStart
//        val flexEnd = minOf(p3, textBgStart + flex.measuredWidth)

        val flexStart = p3 - p1 - flex.measuredWidth + paddingStart
        val flexEnd = p3 - p1

        flex.layout(
            flexStart,
            textBgBottom + flex.marginTop + textBackgroundView.marginBottom,
            flexEnd,
            textBgBottom + flex.marginTop + flex.measuredHeight + textBackgroundView.marginBottom
        )

    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

}