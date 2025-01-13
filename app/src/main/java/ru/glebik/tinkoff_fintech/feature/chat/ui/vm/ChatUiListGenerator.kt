package ru.glebik.tinkoff_fintech.feature.chat.ui.vm

import ru.glebik.tinkoff_fintech.feature.chat.ui.model.DateUiModel
import ru.glebik.tinkoff_fintech.feature.chat.ui.model.TopicCategoryUiModel
import ru.glebik.tinkoff_fintech.feature.chat.ui.recycler.item.ChatItem
import ru.glebik.tinkoff_fintech.feature.chat.ui.recycler.item.DateItem
import ru.glebik.tinkoff_fintech.feature.chat.ui.recycler.item.MessageItem
import ru.glebik.tinkoff_fintech.feature.chat.ui.recycler.item.TopicItem

object ChatUiListGenerator {

    fun updateWhenCollisionPossible(
        stateList: List<ChatItem>,
        newestList: List<MessageItem>,
        includeTopics: Boolean = false,
    ): List<ChatItem> {

        val stateMessages = stateList.filterIsInstance<MessageItem>()
        //совпадения элементов, возвращаются колизии из нового списка (т.е данные обновятся)
        val collisions = newestList
            .filter { newestItem ->
                stateMessages.any { stateItem -> newestItem.message.id == stateItem.message.id }
            }.toSet()

        //старые элементы, которые не были обновлены (не вошли в колизию)
        val stateExtraBlock = stateMessages.filterNot { stateItem ->
            collisions.any { collisionItem -> stateItem.message.id == collisionItem.message.id }
        }.toSet()

        //новые элементы, которые не вошли в колизию
        val newestExtraBlock = newestList.filterNot { newestItem ->
            collisions.any { collisionItem -> newestItem.message.id == collisionItem.message.id }
        }.toSet()

        val messagesItems = (stateExtraBlock + collisions + newestExtraBlock)
            .toList()
            .sortedByDescending { it.message.timestamp }

        return if (includeTopics) {
            addDateAndTopicItems(messagesItems)
        } else {
            addDateItems(messagesItems)
        }
    }

    fun updateSingleMessage(
        stateList: List<ChatItem>,
        newMessage: MessageItem,
    ): List<ChatItem> {

        val updatedMessages =
            stateList.map { if (it is MessageItem && it.message.id == newMessage.message.id) newMessage else it }

        return updatedMessages
    }

    fun deleteMessage(
        stateList: List<ChatItem>,
        messageId: Int,
        includeTopics : Boolean
    ): List<ChatItem> {
        val messagesAfterDelete =
            stateList.filterIsInstance<MessageItem>().filter { it.message.id != messageId }

        return if (includeTopics) {
            addDateAndTopicItems(messagesAfterDelete)
        } else {
            addDateItems(messagesAfterDelete)
        }
    }

    private fun addDateItems(
        messages: List<MessageItem>,
    ): List<ChatItem> {
        if (messages.isEmpty()) return emptyList()
        val sortedMessages = messages.sortedByDescending { it.message.timestamp }

        val chatUiList = arrayListOf<ChatItem>()
        var nextDateToAdd = sortedMessages.first().message.dateAndTime

        sortedMessages.forEach {
            if (it.message.dateAndTime.day != nextDateToAdd.day) {
                chatUiList.add(
                    DateItem(
                        DateUiModel(
                            day = nextDateToAdd.day,
                            monthNum = nextDateToAdd.month
                        )
                    )
                )
                nextDateToAdd = it.message.dateAndTime
            }
            chatUiList.add(it)
        }

        chatUiList.add(
            DateItem(
                DateUiModel(
                    day = nextDateToAdd.day,
                    monthNum = nextDateToAdd.month
                )
            )
        )

        return chatUiList
    }

    private fun addDateAndTopicItems(
        messages: List<MessageItem>,
    ): List<ChatItem> {
        if (messages.isEmpty()) return emptyList()
        val sortedMessages = messages.sortedByDescending { it.message.timestamp }

        val chatUiList = arrayListOf<ChatItem>()
        var nextDateToAdd = sortedMessages.first().message.dateAndTime
        var nextTopicToAdd = TopicCategoryUiModel(
            sortedMessages.first().message.topic.orEmpty(),
            sortedMessages.first().message.streamName
        )

        sortedMessages.forEach {
            if (it.message.dateAndTime.day != nextDateToAdd.day) {
                chatUiList.add(
                    DateItem(
                        DateUiModel(
                            day = nextDateToAdd.day,
                            monthNum = nextDateToAdd.month
                        )
                    )
                )
                nextDateToAdd = it.message.dateAndTime
            }
            if (it.message.topic != nextTopicToAdd.topicName) {
                chatUiList.add(
                    TopicItem(
                        TopicCategoryUiModel(nextTopicToAdd.topicName, nextTopicToAdd.streamName)
                    )
                )
                nextTopicToAdd =
                    TopicCategoryUiModel(it.message.topic.orEmpty(), it.message.streamName)
            }
            chatUiList.add(it)
        }

        chatUiList.add(
            DateItem(
                DateUiModel(
                    day = nextDateToAdd.day,
                    monthNum = nextDateToAdd.month
                )
            )
        )
        chatUiList.add(
            TopicItem(
                TopicCategoryUiModel(nextTopicToAdd.topicName, nextTopicToAdd.streamName)
            )
        )

        return chatUiList
    }
}