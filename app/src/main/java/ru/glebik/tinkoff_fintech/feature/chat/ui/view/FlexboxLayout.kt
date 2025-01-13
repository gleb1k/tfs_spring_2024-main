package ru.glebik.tinkoff_fintech.feature.chat.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import ru.glebik.core.widget.dp
import ru.glebik.tinkoff_fintech.R
import ru.glebik.tinkoff_fintech.feature.chat.ui.model.MessageUiModel
import ru.glebik.tinkoff_fintech.feature.chat.ui.model.ReactionUiModel


class FlexboxLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0,
    defTheme: Int = 0,
) : ViewGroup(
    context, attributeSet, defStyle, defTheme
) {
    private val itemWidth = 50f.dp(context).toInt()
    private val itemHeight = 35f.dp(context).toInt()

    private val _8dp = 8f.dp(context).toInt()
    private val _6dp = 6f.dp(context).toInt()
    private val _4dp = 4f.dp(context).toInt()
    private val _2dp = 2f.dp(context).toInt()

    val plus: ImageView = ImageView(
        context
    ).apply {
        setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_plus_emoji, null))
        background = (ResourcesCompat.getDrawable(resources, R.drawable.emoji_unselected_bg, null))
        val layoutParams = LinearLayout.LayoutParams(
            itemWidth,
            itemHeight,
        )
        layoutParams.setMargins(_4dp, _2dp, _4dp, _2dp)
        setPadding(
            _6dp,
            _8dp,
            _6dp,
            _8dp,
        )
        this.layoutParams = layoutParams
        addView(this)
    }

    val views: MutableList<View> = mutableListOf()

    fun addEmoji(
        message: MessageUiModel,
        reaction: ReactionUiModel,
        onClick: (MessageUiModel, ReactionUiModel) -> Unit,
    ) {
        if (reaction.emojiCode.isBlank()) return

        val emoji = EmojiView(context).apply {
            val layoutParams = FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                itemHeight,
            )

            layoutParams.setMargins(_4dp, _2dp, _4dp, _2dp)
            setPadding(
                _6dp,
                _2dp,
                _6dp,
                _2dp,
            )
            this.layoutParams = layoutParams
            background =
                ResourcesCompat.getDrawable(resources, R.drawable.emoji_bg, null)

            isSelected = reaction.isSelected
            emojiCode = reaction.emojiCode
            emojiName = reaction.emojiName
            count = reaction.usersIds.size
            this.setOnClickListener {
                onClick(message, reaction)
            }
        }
        addView(emoji, childCount - 1)
        views.remove(plus)
        views.add(emoji)
        views.add(plus)
        invalidate()
        requestLayout()
    }

    fun removeEmoji(view: EmojiView) {
        views.remove(view)
        removeView(view)

        if (views.size == 1) {
            views.remove(plus)
        }

        invalidate()
        requestLayout()
    }

    fun removeAllEmojies() {
        removeAllViews()
        views.clear()
        addView(plus)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)

        var currentWidthShift = 0
        var currentHeightShift = 0
        var lineHeight = 0

        var maxWidth = 0

        views.forEach { view ->
            // val view = getChildAt(it)
            measureChildWithMargins(
                view,
                widthMeasureSpec,
                0,
                heightMeasureSpec,
                currentHeightShift
            )

            if (currentWidthShift + view.measuredWidth > parentWidth) {
                currentWidthShift = 0
                currentHeightShift += lineHeight
                lineHeight = 0
            }

            currentWidthShift += view.measuredWidth + view.marginStart + view.marginEnd

            maxWidth = maxOf(maxWidth, currentWidthShift)

            lineHeight = maxOf(lineHeight, view.measuredHeight + view.marginTop + view.marginBottom)
        }

        val height = currentHeightShift + lineHeight

        setMeasuredDimension(maxWidth, height)
    }

    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {

        val parentWidth = p3 - p1

        var currentWidthShift = paddingStart
        var currentHeightShift = paddingTop

        var currentMaxViewHeight = 0

        views.forEach { view ->
            //val view = getChildAt(it)

            val right = view.marginStart + view.measuredWidth + currentWidthShift + paddingEnd

            if (right > parentWidth) {
                //не влезли правым краем
                currentWidthShift = paddingStart
                currentHeightShift += currentMaxViewHeight
            }

            view.layout(
                view.marginStart + currentWidthShift,
                view.marginTop + currentHeightShift,
                view.marginStart + view.measuredWidth + currentWidthShift,
                view.marginTop + view.measuredHeight + currentHeightShift,
            )

            currentWidthShift += view.marginStart + view.measuredWidth + view.marginEnd
            val actualViewHeight = view.measuredHeight + view.marginTop + view.marginBottom

            currentMaxViewHeight = maxOf(actualViewHeight, currentMaxViewHeight)
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }
}