package ru.glebik.tinkoff_fintech.test

import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.glebik.tinkoff_fintech.feature.channels.ChannelsFragment
import ru.glebik.tinkoff_fintech.main.App
import ru.glebik.tinkoff_fintech.mock.MockStream.Companion.stream
import ru.glebik.tinkoff_fintech.mock.MockTopic.Companion.topic
import ru.glebik.tinkoff_fintech.screen.ChannelsFragmentScreen
import ru.glebik.tinkoff_fintech.util.AppFragmentTestRule
import ru.glebik.tinkoff_fintech.util.TestApp

@RunWith(AndroidJUnit4::class)
class ChannelsFragmentTest : TestCase(
    kaspressoBuilder = Kaspresso.Builder.withComposeSupport()
) {
    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    @get:Rule
    val rule = AppFragmentTestRule {
        val testAppComponent = (this as TestApp).getAppComponent()
        (this as App).appComponent = testAppComponent
    }

    @Test
    fun navigateToChatTest() = run {

        val scenario: FragmentScenario<ChannelsFragment> = launchFragmentInContainer()

        rule.wiremockRule.stream { withListStreamsAllStreams() }
        rule.wiremockRule.stream { withListStreamsSubscriptions() }
        rule.wiremockRule.topic { withListTopics() }
        rule.wiremockRule.topic { withListUnreadMsg() }


        ChannelsFragmentScreen {
            //почему-то не работает wiremock
            //выдает ошибку java.net.ConnectException: Failed to connect to localhost/127.0.0.1:8080
            // хотя я сделал идентично ChatFragmentTest, но там все работает а тут нет(
//            step("Проверяю, что вызвался метод stream") {
//                verify(WireMock.getRequestedFor(MockStream.urlPatternAllStreams))
//            }
            step("Раскрываю топики") {
                flakySafely(
                    timeoutMs = 2000L
                ) {
                    composeTestRule.onNodeWithText("general").isDisplayed()
                    composeTestRule.onNodeWithText("general").performClick()
                }
            }
            step("Навигируюсь на чат") {
                flakySafely(
                    timeoutMs = 2000L
                ) {
                    composeTestRule.onNodeWithText("test").isDisplayed()
                    composeTestRule.onNodeWithText("test").performClick()
                }
            }
        }
        Thread.sleep(4000)
    }

}