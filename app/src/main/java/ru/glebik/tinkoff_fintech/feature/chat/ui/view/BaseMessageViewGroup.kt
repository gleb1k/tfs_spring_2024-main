package ru.glebik.tinkoff_fintech.feature.chat.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import ru.glebik.tinkoff_fintech.feature.chat.ui.model.MessageUiModel
import ru.glebik.tinkoff_fintech.feature.chat.ui.model.ReactionUiModel

abstract class BaseMessageViewGroup @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0,
    defTheme: Int = 0,
) : ViewGroup(
    context, attributeSet, defStyle, defTheme
) {

    abstract val comment: TextView

    abstract val textBackgroundView: LinearLayout

    abstract val flex: FlexboxLayout

    fun changeComment(newValue: String) {
        if (comment.text == newValue) return
        comment.text = newValue
        requestLayout()
        invalidate()
    }

    fun addReactions(
        message: MessageUiModel,
        onReactionClick: (MessageUiModel, ReactionUiModel) -> Unit,
    ) {
        flex.removeAllEmojies()

        message.reactions.forEach {
            flex.addEmoji(message, it, onReactionClick)
        }
        requestLayout()
        invalidate()
    }
}