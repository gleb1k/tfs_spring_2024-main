package ru.glebik.tinkoff_fintech.mock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching
import com.github.tomakehurst.wiremock.matching.UrlPathPattern
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import ru.glebik.tinkoff_fintech.util.AssetsUtils

class MockMessage(private val wireMockServer: WireMockServer) {
    private val matcher = WireMock.get(urlPattern)

    private val matcherPost = WireMock.post(urlPattern)

    fun withEmptyMessage(): StubMapping =
        wireMockServer.stubFor(matcher.willReturn(ok(AssetsUtils.fromAssets("empty.json"))))

    fun withSingleMessage(): StubMapping =
        wireMockServer.stubFor(matcher.willReturn(ok(AssetsUtils.fromAssets("message/singleMessage.json"))))

    fun withListMessages(): StubMapping =
        wireMockServer.stubFor(matcher.willReturn(ok(AssetsUtils.fromAssets("message/listMessages.json"))))

    fun withListPostMessages(): StubMapping =
        wireMockServer.stubFor(matcherPost.willReturn(ok(AssetsUtils.fromAssets("message/listMessagesPost.json"))))

    companion object {

        val urlPattern: UrlPathPattern = urlPathMatching("/messages")

        fun WireMockServer.message(block: MockMessage.() -> Unit) {
            MockMessage(this).apply(block)
        }
    }
}
