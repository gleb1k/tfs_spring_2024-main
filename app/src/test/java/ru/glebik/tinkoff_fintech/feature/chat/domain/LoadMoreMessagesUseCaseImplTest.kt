package ru.glebik.tinkoff_fintech.feature.chat.domain

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import ru.glebik.core.utils.ResultWrapper
import ru.glebik.tinkoff_fintech.feature.chat.data.MessageRepository
import ru.glebik.tinkoff_fintech.feature.chat.domain.model.Message
import ru.glebik.tinkoff_fintech.feature.chat.domain.usecase.LoadMoreMessagesUseCaseImpl

class LoadMoreMessagesUseCaseImplTest : BehaviorSpec({

    lateinit var messageRepository: MessageRepository

    lateinit var useCase: LoadMoreMessagesUseCaseImpl

    beforeTest {
        messageRepository = mockk()
        useCase = LoadMoreMessagesUseCaseImpl(messageRepository)
    }

    Given("LoadMoreMessagesUseCaseImpl") {
        When("invoke") {
            And("when expected success") {
                val messageId = 1
                val stream = "stream"
                val topicName = "topic"

                val expectedMessages = List(20) {
                    mockk<Message>().apply {
                        every { topic } returns topicName
                        every { streamName } returns stream
                    }
                }

                val expectedData = ResultWrapper.Success(expectedMessages)

                coEvery {
                    messageRepository.loadMoreMessages(stream, topicName, messageId)
                } returns expectedData

                val actual = useCase(stream, topicName, messageId)
                Then("should return success and loaded messages") {
                    val result = actual as ResultWrapper.Success
                    result shouldBe expectedData

                    result.data.forEach {
                        it.topic shouldBe topicName
                        it.streamName shouldBe stream
                    }
                }
            }
            And("when expected failed") {
                val messageId = 0
                val stream = "bad stream"
                val topicName = "bad topic"

                val expectedData: ResultWrapper.Failed = mockk()

                coEvery {
                    messageRepository.loadMoreMessages(stream, topicName, messageId)
                } returns expectedData

                val actual = useCase(stream, topicName, messageId)
                Then("should return failed") {
                    val result = actual as ResultWrapper.Failed
                    result shouldBe expectedData
                }
            }
        }
    }
})