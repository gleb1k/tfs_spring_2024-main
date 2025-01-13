package ru.glebik.core.presentation.mvi

interface ScreenState {
    val isLoading: Boolean
    val errorMessage: String?
}
