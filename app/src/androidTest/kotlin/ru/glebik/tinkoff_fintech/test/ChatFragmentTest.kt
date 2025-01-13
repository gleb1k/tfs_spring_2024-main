package ru.glebik.tinkoff_fintech.test

import android.util.Log
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.verify
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.glebik.tinkoff_fintech.feature.chat.ChatFragment
import ru.glebik.tinkoff_fintech.main.App
import ru.glebik.tinkoff_fintech.mock.MockMessage
import ru.glebik.tinkoff_fintech.mock.MockMessage.Companion.message
import ru.glebik.tinkoff_fintech.screen.ChatFragmentScreen
import ru.glebik.tinkoff_fintech.screen.ChatFragmentScreen.STREAM_NAME
import ru.glebik.tinkoff_fintech.screen.ChatFragmentScreen.TOPIC_NAME
import ru.glebik.tinkoff_fintech.util.AppFragmentTestRule
import ru.glebik.tinkoff_fintech.util.TestApp

@RunWith(AndroidJUnit4::class)
class ChatFragmentTest : TestCase(
    kaspressoBuilder = Kaspresso.Builder.withComposeSupport()
) {

    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    @get:Rule
    val rule = AppFragmentTestRule {
        Log.d("retrofit url", (this as TestApp).getAppComponent().retrofit().baseUrl().toString())
        val testAppComponent = this.getAppComponent()

        //КАКИМ-ТО ОБРАЗОМ Я ДОДУМАЛСЯ КАК ЗАИНЖЕКТИТЬ АППКОМПОНЕНТ В ТЕСТЫ, СЛАВА БОГУ, КАК ЖЕ Я РАД
        (this as App).appComponent = testAppComponent
    }

    @Test
    fun visibilityTest() = run {
        val scenario: FragmentScenario<ChatFragment> =
            launchFragmentInContainer(
                ChatFragmentScreen.getChatBundle(
                    TOPIC_NAME,
                    STREAM_NAME,
                )
            )

        rule.wiremockRule.message { withListMessages() }

        ChatFragmentScreen {
            step("Проверяю, что вызвался метод message") {
                verify(WireMock.getRequestedFor(MockMessage.urlPattern))
            }
            step("Проверяю, что список отображается") {
                flakySafely {
                    recycler.isVisible()
                    recycler.childAt<ChatFragmentScreen.KMessageItem>(0) {
                        step("Проверяю отображение сообщения на позиции 0") {
                            comment.hasText("Hello test3")
                            name.hasText("King Hamlet")
                        }
                    }
                    recycler.childAt<ChatFragmentScreen.KMessageItem>(2) {
                        step("Проверяю отображение сообщения на позиции 2") {
                            comment.hasText("Hello test")
                            name.hasText("Cool user")
                        }
                    }
                }
            }
            step("Проверяю, что заголовок со стримом отображается") {
                composeTestRule.onNodeWithTag("BackTopBarTitle").isDisplayed()
                composeTestRule.onNodeWithTag("BackTopBarTitle").assertTextEquals(STREAM_NAME)
            }
            step("Проверяю, что заголовок с топиком отображается") {
                composeTestRule.onNodeWithTag("TextTopic").isDisplayed()
                composeTestRule.onNodeWithTag("TextTopic").assertTextEquals("Topic: #$TOPIC_NAME")
//                не работает какао библиотека(
//                onComposeScreen<ComposeTopChatContainer>(composeTestRule) {
//                    textTopic {
//                        assertIsDisplayed()
//                        assertTextEquals(TOPIC_NAME)
//                    }
//                }
            }

            Thread.sleep(4000)
        }
    }

    @Test
    fun inputTest() = run {
        val scenario: FragmentScenario<ChatFragment> =
            launchFragmentInContainer(
                ChatFragmentScreen.getChatBundle(
                    TOPIC_NAME,
                    STREAM_NAME,
                )
            )

        rule.wiremockRule.message { withListMessages() }

        rule.wiremockRule.message { withListPostMessages() }

        ChatFragmentScreen {
            step("Проверяю, что вводитеся текст") {
                composeTestRule.onNodeWithTag("ChatTextField").isDisplayed()
                composeTestRule.onNodeWithTag("ChatTextField").performTextInput("SOME TEST")
            }
            step("Отправляю сообщение") {
                composeTestRule.onNodeWithTag("SendMessageIcon").isDisplayed()
                composeTestRule.onNodeWithTag("SendMessageIcon").performClick()
            }
            step("Проверяю, что вызвался метод message") {
                verify(WireMock.getRequestedFor(MockMessage.urlPattern))
            }
        }
        Thread.sleep(4000)
    }
}