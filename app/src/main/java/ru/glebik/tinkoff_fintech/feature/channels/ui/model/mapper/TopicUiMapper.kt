package ru.glebik.tinkoff_fintech.feature.channels.ui.model.mapper

import ru.glebik.core.utils.di.FeatureScope
import ru.glebik.core.utils.mapper.UiMapper
import ru.glebik.tinkoff_fintech.feature.channels.domain.model.Topic
import ru.glebik.tinkoff_fintech.feature.channels.ui.model.TopicUiModel
import javax.inject.Inject

@FeatureScope
class TopicUiMapper @Inject constructor() : UiMapper<Topic, TopicUiModel> {
    override fun toDomain(ui: TopicUiModel): Topic =
        Topic(
            id = ui.id,
            name = ui.name,
            unreadMessagesCount = ui.messagesCount

        )

    override fun toUi(domain: Topic): TopicUiModel =
        TopicUiModel(
            id = domain.id,
            name = domain.name,
            messagesCount = domain.unreadMessagesCount
        )
}