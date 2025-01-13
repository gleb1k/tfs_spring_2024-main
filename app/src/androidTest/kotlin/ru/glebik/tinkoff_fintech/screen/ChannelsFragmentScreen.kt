package ru.glebik.tinkoff_fintech.screen

import com.kaspersky.kaspresso.screens.KScreen
import ru.glebik.tinkoff_fintech.feature.channels.ChannelsFragment

object ChannelsFragmentScreen : KScreen<ChannelsFragmentScreen>() {

    override val layoutId: Int = ru.glebik.core.presentation.R.layout.fragment_composable
    override val viewClass: Class<*> = ChannelsFragment::class.java


}