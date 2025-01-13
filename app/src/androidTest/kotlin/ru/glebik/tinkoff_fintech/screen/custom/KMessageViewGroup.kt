package ru.glebik.tinkoff_fintech.screen.custom

import android.view.View
import androidx.test.espresso.DataInteraction
import io.github.kakaocup.kakao.common.builders.ViewBuilder
import io.github.kakaocup.kakao.common.views.KBaseView
import org.hamcrest.Matcher
import ru.glebik.tinkoff_fintech.feature.chat.ui.view.MessageViewGroup

class KMessageViewGroup : KBaseView<MessageViewGroup> {
    constructor(function: ViewBuilder.() -> Unit) : super(function)
    constructor(parent: Matcher<View>, function: ViewBuilder.() -> Unit) : super(
        parent,
        function
    )

    constructor(parent: DataInteraction, function: ViewBuilder.() -> Unit) : super(
        parent,
        function
    )

}