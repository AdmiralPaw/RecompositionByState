package com.example.testcompose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.testcompose.presentation.SimplePm
import com.example.testcompose.ui.screen.Screen
import com.example.testcompose.ui.theme.TestComposeTheme
import kotlinx.coroutines.cancel


class TestingActivity : ComponentActivity()  {
    private val simplePm = SimplePm(::someMethode)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TestComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Screen(pm = simplePm)
                }
            }
        }
    }

    private fun someMethode(value: Int) {
        Toast.makeText(this, "Click $value", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        simplePm.scope.cancel()
        super.onDestroy()
    }
}