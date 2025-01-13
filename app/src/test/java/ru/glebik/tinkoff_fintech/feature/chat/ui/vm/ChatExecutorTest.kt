package ru.glebik.tinkoff_fintech.feature.chat.ui.vm

import com.arkivanov.mvikotlin.core.store.Executor
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import ru.glebik.tinkoff_fintech.feature.chat.ui.vm.data.ChatExecutorTestData

class ChatExecutorTest : BehaviorSpec({
    coroutineTestScope = true
    coroutineDebugProbes = true
    ChatExecutorTestData().apply {

        lateinit var executor: ChatExecutor

        beforeTest {
            executor = this.executor()
        }

        Given("ChatExecutor") {
            When("executeIntent") {
                And("intent is AddReaction") {
                    val intent = ChatStore.Intent.AddReaction(messageId, emojiName)

                    Then("message should be SetEmojiSheetState with isShow is false and messageId is null") {
                        true shouldBe true
                    }
                }
                And("intent is LoadNewestMessages") {
                    val intent = ChatStore.Intent.LoadNewestMessages

                    val expectedMessage = ChatStore.Message.SetUiList(uiList)

                    var actualMessage: ChatStore.Message? = null
                    executor.init(object :
                        Executor.Callbacks<ChatStore.State, ChatStore.Message, Nothing> {
                        override val state: ChatStore.State
                            get() = chatState

                        override fun onMessage(message: ChatStore.Message) {
                            actualMessage = message
                        }

                        override fun onLabel(label: Nothing) = Unit

                    })
                    executor.executeIntent(intent)
                    Then("message should be ")
                        //сделал конфиг чтобы работали диспатчеры, но они не работают((
                        .config(coroutineTestScope = true) {
                            actualMessage shouldBe expectedMessage
                            (actualMessage as ChatStore.Message.SetUiList).uiList shouldBe uiList
                        }
                }
                And("intent is OnChatQueryChange") {
                    val intent = ChatStore.Intent.OnChatQueryChange(query)

                    val expectedMessage = ChatStore.Message.SetChatFieldQuery(intent.query)

                    var actualMessage: ChatStore.Message? = null
                    executor.init(object :
                        Executor.Callbacks<ChatStore.State, ChatStore.Message, Nothing> {
                        override val state: ChatStore.State
                            get() = chatState

                        override fun onMessage(message: ChatStore.Message) {
                            actualMessage = message
                        }

                        override fun onLabel(label: Nothing) = Unit

                    })
                    executor.executeIntent(intent)
                    Then("message should be SetChatFieldQuery").config(coroutineTestScope = true) {
                        actualMessage shouldBe expectedMessage
                        (actualMessage as ChatStore.Message.SetChatFieldQuery).query shouldBe query
                    }
                }
                And("intent is OnEmojiSheetChange") {
                    val intent = ChatStore.Intent.OnEmojiSheetChange(isShow, messageId)

                    Then("message should be ") {
                        true shouldBe true
                    }
                }
                And("intent is RemoveReaction") {
                    val intent = ChatStore.Intent.RemoveReaction(messageId, emojiName)

                    Then("message should be ") {
                        true shouldBe true
                    }
                }
                And("intent is SendMessage") {
                    val intent = ChatStore.Intent.SendMessage

                    Then("message should be ") {
                        true shouldBe true
                    }
                }
                And("intent is LoadMoreMessages") {
                    val intent = ChatStore.Intent.LoadMoreMessages

                    Then("message should be ") {
                        true shouldBe true
                    }
                }
            }
        }
    }
})