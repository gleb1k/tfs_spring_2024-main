package ru.glebik.tinkoff_fintech.feature.chat.ui.recycler.item

import ru.glebik.core.widget.view.recycler.DiffListItem
import ru.glebik.tinkoff_fintech.feature.chat.ui.model.DateUiModel

class DateItem(
    val date: DateUiModel,
) : ChatItem {
    override fun areItemsSame(other: DiffListItem): Boolean = other is DateItem

    override fun areContentsSame(other: DiffListItem): Boolean = other is DateItem
            && other.date == date

}