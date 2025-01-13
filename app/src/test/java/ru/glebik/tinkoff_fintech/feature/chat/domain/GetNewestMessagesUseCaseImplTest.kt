package ru.glebik.tinkoff_fintech.feature.chat.domain

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.glebik.core.utils.ResultWrapper
import ru.glebik.tinkoff_fintech.feature.chat.data.MessageRepository
import ru.glebik.tinkoff_fintech.feature.chat.domain.model.Message
import ru.glebik.tinkoff_fintech.feature.chat.domain.usecase.GetNewestMessagesUseCaseImpl

class GetNewestMessagesUseCaseImplTest : BehaviorSpec({

    lateinit var messageRepository: MessageRepository

    lateinit var useCase: GetNewestMessagesUseCaseImpl

    beforeTest {
        messageRepository = mockk()
        useCase = GetNewestMessagesUseCaseImpl(messageRepository)
    }

    Given("GetNewestMessagesUseCaseImpl") {
        When("invoke") {
            And("when expected success") {
                val stream = "stream"
                val topicName = "topic"

                val expectedMessages = List(20) {
                    mockk<Message>().apply {
                        every { topic } returns topicName
                        every { streamName } returns stream
                    }
                }

                //todo как правильно флоу замокать?
                val expectedData = flow { emit(ResultWrapper.Success(expectedMessages)) }

                coEvery {
                    messageRepository.newestMessagesFlow(stream, topicName)
                } returns expectedData


                val actual = useCase(stream, topicName)
                Then("should return success and flow is result wrapper with messages list") {
                    //?
                    val result = actual as Flow<ResultWrapper.Success<*>>
                    result shouldBe expectedData
                }
            }
            And("when expected failed") {
                val stream = "bad stream"
                val topicName = "bad topic"

                val expectedData: Flow<ResultWrapper.Failed> = mockk()

                coEvery {
                    messageRepository.newestMessagesFlow(stream, topicName)
                } returns expectedData

                val actual = useCase(stream, topicName)
                Then("should return failed") {
                    val result = actual as Flow<ResultWrapper.Failed>
                    result shouldBe expectedData
                }
            }
        }
    }
})