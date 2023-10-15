package com.example.testcompose.presentation

import com.example.testcompose.SingleState
import com.example.testcompose.data.SomeModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.LinkedList
import java.util.Queue

class SimplePm(
    private val someCallback: (Int) -> Unit
): SingleState<SimplePm.MyState>(
    initialState = MyState()
) {
    val scope = CoroutineScope(Job())
    private val initSize = 30
    private var indicesToUpdate: Queue<Int> = LinkedList(
        listOf(0, 1, 2, 2, 2, 3, initSize - 1, 3, 0, 10)
    )

    data class MyState(
        val listItems: ImmutableList<SomeModel> = persistentListOf(
            *buildList {
                repeat(30) { add(SomeModel(it)) }
            }.toTypedArray()
        ),
        val someValue: Int = 0,
        val tick: Boolean = false
    )

    init {
        testingSubscribe()
    }

    private fun testingSubscribe() = scope.launch {
        while (true) {
            delay(1000)
            indicesToUpdate.poll()?.let {
                changeState {

                    val incomingList = listItems
                        .toMutableList()
                        .apply {
                            println("==========================\nupdate $it")
                            set(it, SomeModel(it).apply { lambda = { someCallback(it) } })
                        }.toImmutableList()

                    copy(
                        listItems = incomingList,
                        someValue = it,
                        tick = !tick
                    )
                }
            }
        }
    }
}