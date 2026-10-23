package com.bangla.dhonihobar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.bangla.dhonihobar.ui.DhonihobarApp
import com.bangla.dhonihobar.ui.theme.DhoniHobarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DhoniHobarTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    DhonihobarApp()
                }
            }
        }
    }
}
