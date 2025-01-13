package ru.glebik.tinkoff_fintech.mock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching
import com.github.tomakehurst.wiremock.matching.UrlPathPattern
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import ru.glebik.tinkoff_fintech.util.AssetsUtils

class MockStream(private val wireMockServer: WireMockServer) {

    private val matcherAllStreams = WireMock.get(urlPatternAllStreams)

    private val matcherSubscriptions = WireMock.get(urlPatternSubscriptions)

    fun withEmptyStreamAllStreams(): StubMapping =
        wireMockServer.stubFor(matcherAllStreams.willReturn(ok(AssetsUtils.fromAssets("empty.json"))))

    fun withSingleStreamAllStreams(): StubMapping =
        wireMockServer.stubFor(matcherAllStreams.willReturn(ok(AssetsUtils.fromAssets("stream/singleStream.json"))))

    fun withListStreamsAllStreams(): StubMapping =
        wireMockServer.stubFor(matcherAllStreams.willReturn(ok(AssetsUtils.fromAssets("stream/listStreams.json"))))

    fun withListStreamsSubscriptions(): StubMapping =
        wireMockServer.stubFor(matcherSubscriptions.willReturn(ok(AssetsUtils.fromAssets("stream/listStreams.json"))))

    companion object {

        val urlPatternAllStreams: UrlPathPattern = urlPathMatching("/streams")
        val urlPatternSubscriptions: UrlPathPattern = urlPathMatching("/users/me/subscriptions")

        fun WireMockServer.stream(block: MockStream.() -> Unit) {
            MockStream(this).apply(block)
        }
    }
}
