package ru.glebik.core.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen

inline fun<reified T : ClientScreen> T.toCiceroneScreen(
    enterAnimResId: Int? = null,
    exitAnimResId: Int? = null
): FragmentScreen {
    animEnterResId = enterAnimResId
    animOutResId = exitAnimResId
    val handler = SCREEN_MAP[T::class.java]
        ?: throw IllegalStateException("Can't find handler for screen $this")
    return handler(this)
}