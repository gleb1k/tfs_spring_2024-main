package ru.glebik.tinkoff_fintech.feature.channels.ui.model

import kotlinx.collections.immutable.PersistentList

data class StreamUiModel(
    val id: Int,
    val name: String,
    val topics: PersistentList<TopicUiModel>,
    val isCollapsed: Boolean,
    val isTopicsLoading: Boolean,
)

