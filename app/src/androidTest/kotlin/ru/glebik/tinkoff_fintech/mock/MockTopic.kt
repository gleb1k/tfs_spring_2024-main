package ru.glebik.tinkoff_fintech.mock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching
import com.github.tomakehurst.wiremock.matching.UrlPathPattern
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import ru.glebik.tinkoff_fintech.util.AssetsUtils

class MockTopic(private val wireMockServer: WireMockServer) {

    private val matcher = WireMock.get(urlPatternTopics)

    private val matcherUnreadMsg = WireMock.post(urlPatternUnreadMsg)

    fun withEmptyTopic(): StubMapping =
        wireMockServer.stubFor(matcher.willReturn(ok(AssetsUtils.fromAssets("empty.json"))))

    fun withSingleTopic(): StubMapping =
        wireMockServer.stubFor(matcher.willReturn(ok(AssetsUtils.fromAssets("topic/singleTopic.json"))))

    fun withListTopics(): StubMapping =
        wireMockServer.stubFor(matcher.willReturn(ok(AssetsUtils.fromAssets("topic/listTopics.json"))))

    fun withListUnreadMsg(): StubMapping =
        wireMockServer.stubFor(matcherUnreadMsg.willReturn(ok(AssetsUtils.fromAssets("topic/unreadMsgResponse.json"))))

    companion object {

        val urlPatternTopics: UrlPathPattern = urlPathMatching("/users/me/([a-zA-Z0-9]+)/topics")

        val urlPatternUnreadMsg: UrlPathPattern = urlPathMatching("/register")

        fun WireMockServer.topic(block: MockTopic.() -> Unit) {
            MockTopic(this).apply(block)
        }
    }
}
