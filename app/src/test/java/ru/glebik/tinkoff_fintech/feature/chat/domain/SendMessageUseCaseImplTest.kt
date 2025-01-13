package ru.glebik.tinkoff_fintech.feature.chat.domain

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import ru.glebik.core.utils.ResultWrapper
import ru.glebik.tinkoff_fintech.feature.chat.data.MessageRepository
import ru.glebik.tinkoff_fintech.feature.chat.domain.model.Message
import ru.glebik.tinkoff_fintech.feature.chat.domain.usecase.SendMessageUseCaseImpl

class SendMessageUseCaseImplTest : BehaviorSpec({

    lateinit var messageRepository: MessageRepository

    lateinit var sendMessageUseCase: SendMessageUseCaseImpl

    beforeTest {
        messageRepository = mockk()
        sendMessageUseCase = SendMessageUseCaseImpl(messageRepository)
    }

    Given("SendMessageUseCaseImpl") {
        When("invoke") {
            And("when expected success") {
                val contentToSend = "contentToSend"
                val streamName = "stream"
                val topicName = "topic"

                val expectedData: ResultWrapper.Success<Message> = mockk {
                    every { data.content } returns contentToSend
                    every { data.topic } returns topicName
                    every { data.streamName } returns streamName
                }

                coEvery {
                    messageRepository.sendMessage(streamName, topicName, contentToSend)
                } returns expectedData

                val actual = sendMessageUseCase(streamName, topicName, contentToSend)
                Then("should send content and return message with content is $contentToSend").config(
                    coroutineTestScope = true
                ) {
                    val result = actual as ResultWrapper.Success
                    result shouldBe expectedData

                    result.data.content shouldBe contentToSend
                    result.data.topic shouldBe topicName
                    result.data.streamName shouldBe streamName
                }
            }
            And("when expected failed") {
                val contentToSend = ""
                val badStreamName = "null"
                val badTopicName = "null"

                val expectedData: ResultWrapper.Failed = mockk()

                coEvery {
                    messageRepository.sendMessage(badStreamName, badTopicName, contentToSend)
                } returns expectedData

                val actual = sendMessageUseCase(badStreamName, badTopicName, contentToSend)
                Then("should return failed").config(
                    coroutineTestScope = true
                ) {
                    val result = actual as ResultWrapper.Failed
                    result shouldBe expectedData
                }
            }
        }
    }
})