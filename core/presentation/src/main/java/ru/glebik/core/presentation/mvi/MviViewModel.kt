package ru.glebik.core.presentation.mvi


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkivanov.mvikotlin.core.binder.Binder
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.extensions.coroutines.bind
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class MviViewModel<in Intent : Any, out State : Any, out Label : Any>(
    private val store: Store<Intent, State, Label>
) : ViewModel() {
    private val _state = MutableStateFlow(store.state)
    val state: StateFlow<State>
        get() = _state.asStateFlow()

    private val _label = MutableSharedFlow<Label>()
    val label: SharedFlow<Label>
        get() = _label.asSharedFlow()

    private val binder: Binder

    init {
        binder = bind(Dispatchers.Main.immediate) {
            store.states bindTo (::acceptState)
            store.labels bindTo (::acceptLabel)
        }
        binder.start()
    }

    private fun acceptState(state: State) {
        _state.value = state
    }

    private fun acceptLabel(label: Label) {
        viewModelScope.launch {
            _label.emit(label)
        }
    }

    override fun onCleared() {
        super.onCleared()
        binder.stop()
        store.dispose()
    }
}