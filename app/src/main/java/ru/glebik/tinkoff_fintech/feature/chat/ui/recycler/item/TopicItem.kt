package ru.glebik.tinkoff_fintech.feature.chat.ui.recycler.item

import ru.glebik.core.widget.view.recycler.DiffListItem
import ru.glebik.tinkoff_fintech.feature.chat.ui.model.TopicCategoryUiModel

class TopicItem(
    val topic: TopicCategoryUiModel,
) : ChatItem {
    override fun areItemsSame(other: DiffListItem): Boolean = other is TopicItem

    override fun areContentsSame(other: DiffListItem): Boolean = other is TopicItem
            && other.topic.topicName == topic.topicName
}