package ru.glebik.tinkoff_fintech.feature.chat

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.glebik.core.navigation.ClientScreen
import ru.glebik.core.presentation.UsableFragmentScreen

class ChatScreen(
    val topicName: String?,
    val streamName: String,
) : ClientScreen()

val CHAT_SCREEN: Map<Class<*>, (ClientScreen) -> FragmentScreen> = mapOf(
    ChatScreen::class.java to { screen ->
        screen as ChatScreen
        UsableFragmentScreen(screen) {
            ChatFragment.newInstance(
                screen.topicName,
                screen.streamName
            )
        }
    },
)