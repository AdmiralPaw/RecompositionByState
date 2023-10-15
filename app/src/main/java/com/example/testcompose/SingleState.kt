package com.example.testcompose


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class SingleState<S>(
    initialState: S,
) {
    private val _stateFlow = MutableStateFlow(initialState)
    val stateFlow: StateFlow<S> = _stateFlow.asStateFlow()

    fun changeState(copy: S.() -> S) {
        _stateFlow.value = copy(_stateFlow.value)
    }
}
