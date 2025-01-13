package ru.glebik.tinkoff_fintech.util

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class AppFragmentTestRule(
    private val configuration: Application.() -> Unit,
) : TestRule {

    val wiremockRule = WireMockRule()

    override fun apply(base: Statement?, description: Description?): Statement {
        return RuleChain.emptyRuleChain()
            .around(wiremockRule)
            .apply { configuration(ApplicationProvider.getApplicationContext()) }
            .apply(base, description)
    }
}
