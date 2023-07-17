package com.nameisjayant.gradle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import com.nameisjayant.gradle.EachRow
import com.nameisjayant.gradle.ui.theme.GradleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GradleTheme {
                LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                    items(5) {
                        EachRow()
                    }
                }
            }
        }
    }
}