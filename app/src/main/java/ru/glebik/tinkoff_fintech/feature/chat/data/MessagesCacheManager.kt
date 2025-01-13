package ru.glebik.tinkoff_fintech.feature.chat.data

import ru.glebik.core.db.api.dao.MessageDao
import ru.glebik.core.db.api.dao.ReactionDao
import ru.glebik.core.db.api.entity.ReactionEntity
import ru.glebik.core.utils.di.FeatureScope
import ru.glebik.tinkoff_fintech.feature.chat.data.model.mapper.toDomain
import ru.glebik.tinkoff_fintech.feature.chat.data.model.mapper.toEntity
import ru.glebik.tinkoff_fintech.feature.chat.data.model.response.MessageResponse
import ru.glebik.tinkoff_fintech.feature.chat.domain.model.Message
import ru.glebik.tinkoff_fintech.feature.chat.domain.model.mapper.toEntity
import javax.inject.Inject

@FeatureScope
class MessagesCacheManager @Inject constructor(
    private val messageDao: MessageDao,
    private val reactionDao: ReactionDao,
) {

    suspend fun get(streamName: String, topicName: String?): List<Message> {
        val messagesCache = if (topicName != null) {
            messageDao.getMessagesFromTopic(streamName, topicName)
        } else
            messageDao.getMessages(streamName)
        val messagesCacheDomain = messagesCache.map {
            it.message.toDomain(it.reactions)
        }
        return messagesCacheDomain
    }

    suspend fun put(messages: List<MessageResponse>, streamName: String, topicName: String?) {
        messageDao.insertAll(messages.map {
            it.toDomain().toEntity()
        })

        val reactions = arrayListOf<ReactionEntity>()

        messages.forEach {
            it.reactionResponses?.forEach { reactionResponse ->
                reactions.add(reactionResponse.toEntity(it.id))
            }
        }

        reactionDao.insertAll(reactions)

        clearExcess(streamName, topicName)
    }

    private suspend fun clearExcess(streamName: String, topicName: String?) {
        val messagesCache = if (topicName != null) {
            messageDao.getMessagesFromTopic(streamName, topicName)
        } else
            messageDao.getMessages(streamName)

        if (messagesCache.size > 50) {
            val toRemove = messagesCache.sortedByDescending { it.message.timestamp }.drop(50)

            toRemove.forEach {
                reactionDao.delete(it.reactions)
                messageDao.delete(it.message)
            }
        }
    }

    suspend fun deleteMessage(messageId : Int) {
        messageDao.deleteById(messageId)
        reactionDao.deleteAllReactionsFromMessageById(messageId)
    }

}