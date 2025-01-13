package ru.glebik.tinkoff_fintech.feature.channels.ui.model.mapper

import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import ru.glebik.core.utils.di.FeatureScope
import ru.glebik.core.utils.mapper.UiMapper
import ru.glebik.tinkoff_fintech.feature.channels.di.TopicUiMapperQualifier
import ru.glebik.tinkoff_fintech.feature.channels.domain.model.Stream
import ru.glebik.tinkoff_fintech.feature.channels.domain.model.Topic
import ru.glebik.tinkoff_fintech.feature.channels.ui.model.StreamUiModel
import ru.glebik.tinkoff_fintech.feature.channels.ui.model.TopicUiModel
import javax.inject.Inject

@FeatureScope
class StreamUiMapper @Inject constructor(
    @TopicUiMapperQualifier
    private val topicUiMapper: UiMapper<Topic, TopicUiModel>,
) : UiMapper<Stream, StreamUiModel> {

    override fun toDomain(ui: StreamUiModel): Stream =
        Stream(
            id = ui.id,
            name = ui.name,
            topics = ui.topics.map { topicUiMapper.toDomain(it) },
        )

    override fun toUi(domain: Stream): StreamUiModel =
        StreamUiModel(
            id = domain.id,
            name = domain.name,
            topics = domain.topics?.map { topicUiMapper.toUi(it) }?.toPersistentList()
                ?: persistentListOf(),
            isCollapsed = false,
            isTopicsLoading = true
        )
}