package ru.glebik.tinkoff_fintech.feature.chat.ui.vm.data

import io.mockk.mockk
import ru.glebik.tinkoff_fintech.feature.chat.ui.recycler.item.ChatItem
import ru.glebik.tinkoff_fintech.feature.chat.ui.vm.ChatReducer
import ru.glebik.tinkoff_fintech.feature.chat.ui.vm.ChatStore

class ChatReducerTestData {

    val someQuery: String = "query"

    val someUiList: List<ChatItem> = mockk()

    val stream = "stream"
    val topic = "topic"

    val chatReducer = ChatReducer()

    val chatState = ChatStore.State(
        topicName = topic,
        streamName = stream
    )

}