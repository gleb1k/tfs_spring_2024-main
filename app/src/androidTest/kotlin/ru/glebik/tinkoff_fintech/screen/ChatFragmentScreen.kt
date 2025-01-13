package ru.glebik.tinkoff_fintech.screen

import android.os.Bundle
import android.view.View
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher
import ru.glebik.tinkoff_fintech.R
import ru.glebik.tinkoff_fintech.feature.chat.ChatFragment
import ru.glebik.tinkoff_fintech.screen.custom.KFlexboxLayout
import ru.glebik.tinkoff_fintech.screen.custom.KMessageViewGroup

object ChatFragmentScreen : KScreen<ChatFragmentScreen>() {

    override val layoutId: Int = R.layout.fragment_chat
    override val viewClass: Class<*> = ChatFragment::class.java

    val recycler = KRecyclerView(
        builder = { withId(R.id.rv_chat) },
        itemTypeBuilder = {
            itemType(::KMessageItem)
        }
    )

    class KMessageItem(parent: Matcher<View>) : KRecyclerItem<KMessageItem>(parent) {
        val messageViewGroup = KMessageViewGroup(parent) { withId(R.id.message) }

        val avatar = KImageView(parent) { withId(R.id.avatar) }
        val name = KTextView(parent) { withId(R.id.name) }
        val comment = KTextView(parent) { withId(R.id.comment) }
        val flex = KFlexboxLayout(parent) { withId(R.id.flexboxContainter) }

    }

    const val TOPIC_NAME = "swimming turtles"
    const val STREAM_NAME = "general"

    fun getChatBundle(
        topicName: String = TOPIC_NAME,
        streamName: String = STREAM_NAME,
    ): Bundle =
        Bundle().also { bundle ->
            bundle.putString(ChatFragment.CHAT_FRAGMENT_TAG_TOPIC, topicName)
            bundle.putString(ChatFragment.CHAT_FRAGMENT_TAG_STREAM, streamName)
        }
}

//не работает(
class ComposeTopChatContainer(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<ComposeTopChatContainer>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { withId(R.id.app_bar_container) }
    ) {

    val textTopic: KNode = child {
        hasTestTag("TextTopic")
    }
}