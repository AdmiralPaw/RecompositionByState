package com.example.testcompose.ui.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.testcompose.data.SomeModel
import com.example.testcompose.presentation.SimplePm
import com.example.testcompose.utils.collectAsStateOptimized
import com.example.testcompose.utils.compareUpdateWith
import kotlinx.collections.immutable.ImmutableList
import kotlin.system.measureNanoTime

private const val isOmptimized = true

private class UIState {
    var someValue = mutableStateOf(0)
    val listValue = mutableStateListOf<SomeModel>()
    val tickValue = mutableStateOf(false)

    companion object {
        @SuppressLint("ComposableNaming")
        @Composable
        fun getRememberedState() = remember { UIState() }
    }
}

@Composable
fun Screen (pm: SimplePm) {
    val time = measureNanoTime {
        if (!isOmptimized) {
            val state = pm.stateFlow.collectAsState().value
            Screen(state = state)
        } else {
            val uiState = UIState.getRememberedState()

            pm.stateFlow.collectAsStateOptimized {
                uiState.tickValue.value = this.tick
                uiState.someValue.value = this.someValue
                uiState.listValue.compareUpdateWith(
                    newList = this.listItems,
                    compareBy<SomeModel> { it.property }.thenBy { it.lambda.toString() }
                )
            }
            ScreenOpt(
                uiState.tickValue,
                uiState.someValue,
                uiState.listValue
            )
        }
    }
    Log.i("Time", time.toString())
}

@Composable
private fun ScreenOpt(
    tickValue: MutableState<Boolean>,
    someValue: MutableState<Int>,
    listValue: SnapshotStateList<SomeModel>
) {
    Log.i("Recomposing", "ScreenOpt")
    Column {
        TickOpt(tickValue)
        AnotherTextOpt(text = someValue)
        TestingLazyListOpt(listItems = listValue)
    }
}

@Composable
private fun Screen(state: SimplePm.MyState) {
    Log.i("Recomposing", "Screen")
    Column {
        Tick(tick = state.tick)
        AnotherText(text = state.someValue)
        TestingLazyList(listItems = state.listItems)
    }
}

@Composable
fun TickOpt(tick: MutableState<Boolean>) {
    Box(
        modifier = Modifier
            .background(color = if (tick.value) Color.Green else Color.Red, shape = CircleShape)
            .size(10.dp)
    )
}


@Composable
fun Tick(tick: Boolean) {
    Box(
        modifier = Modifier
            .background(color = if (tick) Color.Green else Color.Red, shape = CircleShape)
            .size(10.dp)
    )
}

@Composable
private fun AnotherTextOpt(text: MutableState<Int>) {
    Log.i("Recomposing","AnotherTextOpt")
    Text(text = "Current update: ${text.value}")
}

@Composable
private fun AnotherText(text: Int) {
    Log.i("Recomposing","AnotherText")
    Text(text = "Current update: $text")
}

@Composable
private fun TestingLazyListOpt(listItems: SnapshotStateList<SomeModel>) {
    Log.i("Recomposing","TestingLazyList")
    LazyColumn {
        Log.i("Recomposing","LazyColumn")
        items(listItems) {
            TestButton(it.property, it.lambda)
        }
    }
}

@Composable
private fun TestingLazyList(listItems: ImmutableList<SomeModel>) {
    Log.i("Recomposing","TestingLazyList")
    LazyColumn {
        Log.i("Recomposing","LazyColumn")
        items(listItems) {
            TestButton(it.property, it.lambda)
        }
    }
}

@Composable
private fun TestButton(property: Int, lambda: ((Int) -> Unit)?) {
    Log.i("Recomposing","TestButton object $property")
    Button(onClick = { lambda?.invoke(property) }, enabled = lambda != null) {
        TestedText(property)
    }
}

@Composable
private fun TestedText(property: Int) {
    Log.i("Recomposing","TestedText")
    Text(text = "Click me $property")
}
