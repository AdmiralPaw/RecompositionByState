package com.example.testcompose.utils

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.StateFlow

@SuppressLint("ComposableNaming")
@Composable
inline fun <T> StateFlow<T>.collectAsStateOptimized(
    crossinline block: T.() -> Unit
) = LaunchedEffect(
    collectAsState().value
) { block(value) }

inline fun <reified T> MutableList<T>.compareUpdateWith(
    newList: List<T>,
    comparator: Comparator<in T>
) {
    if (this.isEmpty() && newList.isEmpty()) return
    newList.forEachIndexed { index, newItem ->
        if (index <= lastIndex) {
            val oldItem = this[index]
            if (comparator.compare(oldItem, newItem) != 0)
                this[index] = newItem
        }
        else {
            this.add(index, newItem)
        }
    }
    if (newList.lastIndex < this.lastIndex) {
        repeat(this.lastIndex - newList.lastIndex) {
            removeLast()
        }
    }
}