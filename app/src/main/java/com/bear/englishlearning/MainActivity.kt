package com.bear.englishlearning

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.bear.englishlearning.ui.navigation.AppNavigation
import com.bear.englishlearning.ui.theme.BearEnglishTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BearEnglishTheme {
                AppNavigation()
            }
        }
    }
}
