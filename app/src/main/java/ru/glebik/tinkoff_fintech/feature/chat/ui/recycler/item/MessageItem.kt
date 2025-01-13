package ru.glebik.tinkoff_fintech.feature.chat.ui.recycler.item

import ru.glebik.core.widget.view.recycler.DiffListItem
import ru.glebik.tinkoff_fintech.feature.chat.ui.model.MessageUiModel

class MessageItem(
    val message: MessageUiModel,
) : ChatItem {
    override fun areItemsSame(other: DiffListItem): Boolean = other is MessageItem

    override fun areContentsSame(other: DiffListItem): Boolean = other is MessageItem
            && other.message == message

    override fun toString(): String = "$message"
}