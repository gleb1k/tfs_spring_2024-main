package ru.glebik.tinkoff_fintech.feature.chat.ui.vm

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import ru.glebik.core.utils.ResultWrapper
import ru.glebik.tinkoff_fintech.feature.chat.ui.vm.data.ChatReducerTestData

class ChatReducerTest : BehaviorSpec({
    ChatReducerTestData().apply {
        Given("ChatReducer") {
            When("reduce") {
                And("message is SetError") {
                    And("error is null") {
                        val expectedError = null
                        val expectedLoading = false

                        var actual: ChatStore.State
                        chatReducer.run {
                            actual = chatState.reduce(
                                ChatStore.Message.SetError(
                                    ResultWrapper.Failed(null)
                                )
                            )
                        }
                        Then("should return state with error null and loading false") {
                            actual.errorMessage shouldBe expectedError
                            actual.isLoading shouldBe expectedLoading
                        }
                    }
                    And("error is not null") {
                        val errorMessage = "error!"

                        val expectedError = errorMessage
                        val expectedLoading = false

                        var actual: ChatStore.State
                        chatReducer.run {
                            actual = chatState.reduce(
                                ChatStore.Message.SetError(
                                    ResultWrapper.Failed(Throwable(expectedError), expectedError)
                                )
                            )
                        }
                        Then("should return state with error is $errorMessage and loading is false") {
                            actual.errorMessage shouldBe expectedError
                            actual.isLoading shouldBe expectedLoading
                        }
                    }
                }
                And("message is SetLoading") {
                    val expectedLoading = true
                    val expectedError = null

                    var actual: ChatStore.State
                    chatReducer.run {
                        actual =
                            chatState.reduce(ChatStore.Message.SetLoading)
                    }
                    Then("should return state with loading is $expectedLoading and error is $expectedError") {
                        actual.errorMessage shouldBe expectedError
                        actual.isLoading shouldBe expectedLoading
                    }
                }
                And("message is SetEmojiSheetState") {
                    val isShow = true
                    val messageId = 1

                    val expectedIsShow = isShow
                    val expectedMessageId = messageId

                    var actual: ChatStore.State
                    chatReducer.run {
                        actual = chatState.reduce(
                            ChatStore.Message.SetEmojiSheetState(
                                expectedIsShow,
                                expectedMessageId
                            )
                        )
                    }
                    Then("should return state with sheetState contains isShow is $isShow and messageId is $messageId") {
                        actual.bottomSheetState.isShow shouldBe expectedIsShow
                        actual.bottomSheetState.messageId shouldBe messageId
                    }
                }
                And("message is SetUiList") {
                    val expectedList = someUiList
                    val expectedError = null
                    val expectedLoading = false


                    var actual: ChatStore.State
                    chatReducer.run {
                        actual =
                            chatState.reduce(ChatStore.Message.SetUiList(expectedList))
                    }
                    Then("should return state with chatUiList is $expectedList and loading is $expectedLoading and error is $expectedError") {
                        actual.errorMessage shouldBe expectedError
                        actual.isLoading shouldBe expectedLoading
                        actual.chatUiList shouldBe expectedList
                    }
                }
                And("message is SetChatFieldQuery") {
                    val expected = someQuery

                    var actual: ChatStore.State
                    chatReducer.run {
                        actual =
                            chatState.reduce(ChatStore.Message.SetChatFieldQuery(expected))
                    }
                    Then("should return state with chatQuery is $expected") {
                        actual.chatQuery shouldBe expected
                    }
                }
            }
        }
    }
})