package ru.glebik.tinkoff_fintech.feature.channels.ui.model.mapper

import ru.glebik.tinkoff_fintech.feature.channels.domain.model.Topic
import ru.glebik.tinkoff_fintech.feature.channels.ui.model.TopicUiModel

fun Topic.toUi(): TopicUiModel = TopicUiModel(
    id, name, unreadMessagesCount
)

