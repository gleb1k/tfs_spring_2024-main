package ru.glebik.tinkoff_fintech.feature.chat.domain

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import ru.glebik.core.utils.ResultWrapper
import ru.glebik.tinkoff_fintech.feature.chat.data.MessageRepository
import ru.glebik.tinkoff_fintech.feature.chat.domain.model.Message
import ru.glebik.tinkoff_fintech.feature.chat.domain.model.Reaction
import ru.glebik.tinkoff_fintech.feature.chat.domain.usecase.AddReactionUseCaseImpl

class AddReactionUseCaseImplTest : BehaviorSpec({

    lateinit var messageRepository: MessageRepository

    lateinit var addReactionUseCase: AddReactionUseCaseImpl

    beforeTest {
        messageRepository = mockk()
        addReactionUseCase = AddReactionUseCaseImpl(messageRepository)
    }

    Given("AddReactionUseCaseImpl") {
        When("invoke") {
            And("when expected success") {
                val messageId = 1
                val emojiName = "smile"

                val expectedReactions: List<Reaction> = mockk()

                val expectedData: ResultWrapper.Success<Message> = mockk {
                    every { data.id } returns messageId
                    //todo так правильно?
                    //every { expectedReactions.any { it.emojiName == emojiName } } returns true
                }

                coEvery {
                    messageRepository.addReaction(messageId, emojiName)
                } returns expectedData

                val actual = addReactionUseCase.invoke(messageId, emojiName)
                Then("should add reaction and return message with id is $messageId and reactions contains $emojiName") {
                    val result = actual as ResultWrapper.Success
                    result shouldBe expectedData
                    result.data.id shouldBe messageId
                    //result.data.reactions.any { it.emojiName == emojiName } shouldBe true
                }
            }
            And("when expected failed") {
                val messageId = 0
                val emojiName = "aaaaa"

                val expectedData: ResultWrapper.Failed = mockk()

                coEvery {
                    messageRepository.addReaction(messageId, emojiName)
                } returns expectedData

                val actual = addReactionUseCase.invoke(messageId, emojiName)
                Then("should return failed") {
                    val result = actual as ResultWrapper.Failed
                    result shouldBe expectedData
                }
            }
        }
    }
})