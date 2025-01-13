package ru.glebik.tinkoff_fintech.main.di

import android.content.Context
import androidx.fragment.app.Fragment


fun Context.appComponent() = (applicationContext as AppComponentProvider).appComponent

fun Fragment.appComponent(): AppComponent =
    (requireContext().applicationContext as AppComponentProvider).appComponent
