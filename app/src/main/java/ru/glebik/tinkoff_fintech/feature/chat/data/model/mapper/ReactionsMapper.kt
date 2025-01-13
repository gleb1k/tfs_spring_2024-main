package ru.glebik.tinkoff_fintech.feature.chat.data.model.mapper

import ru.glebik.core.db.api.entity.ReactionEntity
import ru.glebik.core.utils.orEmpty
import ru.glebik.tinkoff_fintech.feature.chat.data.model.response.ReactionResponse
import ru.glebik.tinkoff_fintech.feature.chat.domain.model.Reaction

fun ReactionResponse.toEntity(messageId: Int): ReactionEntity = ReactionEntity(
    messageId = messageId,
    emojiName = emojiName.orEmpty(),
    emojiCode = emojiCode.orEmpty(),
    reactionType = reactionType.orEmpty(),
    userEmail = user?.email.orEmpty(),
    userFullName = user?.fullName.orEmpty(),
    userId = userId.orEmpty()
)

fun List<ReactionEntity>?.toDomainList(messageId: Int): List<Reaction> {
    if (this == null) return listOf()

    val reactionsMap = mutableMapOf<Pair<String, String>, ArrayList<Int>>()

    this.forEach {
        val element = reactionsMap[it.emojiName to it.emojiCode]

        if (element != null) {
            reactionsMap[it.emojiName to it.emojiCode]?.add(it.userId)
        } else {
            val name = it.emojiName
            val code = it.emojiCode
            reactionsMap[name to code] = arrayListOf(it.userId)
        }
    }

    val resultReactions = arrayListOf<Reaction>()

    reactionsMap.forEach {
        resultReactions.add(
            Reaction(
                messageId = messageId,
                emojiName = it.key.first,
                emojiCode = it.key.second,
                usersIds = it.value
            )
        )
    }

    return resultReactions
}

//если оставить этот метод здесь, то JVM не может отличить их, и ругается
//Platform declaration clash: The following declarations have the same JVM signature (toDomainListReactions(Ljava/util/List;I)Ljava/util/List;
//поэтому я скопировал этот метод в другое место
//fun List<ReactionResponse>?.toDomainList(messageId: Int): List<Reaction> {
//    if (this == null) return listOf()
//
//    val reactionsMap = mutableMapOf<Pair<String, String>, ArrayList<Int>>()
//
//    this.forEach {
//        val element = reactionsMap[it.emojiName to it.emojiCode]
//
//        if (element != null) {
//            reactionsMap[it.emojiName to it.emojiCode]?.add(it.userId ?: 0)
//        } else {
//            val name = it.emojiName.orEmpty()
//            val code = it.emojiCode.orEmpty()
//            reactionsMap[name to code] = arrayListOf(it.userId ?: 0)
//        }
//    }
//
//    val resultReactions = arrayListOf<Reaction>()
//
//    reactionsMap.forEach {
//        resultReactions.add(
//            Reaction(
//                messageId = messageId,
//                emojiName = it.key.first,
//                emojiCode = it.key.second,
//                usersIds = it.value
//            )
//        )
//    }
//
//    return resultReactions
//}

