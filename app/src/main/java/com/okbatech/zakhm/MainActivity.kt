package com.okbatech.zakhm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.okbatech.zakhm.presentation.navigation.ZakhmNavGraph
import com.okbatech.zakhm.ui.theme.ZakhmTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZakhmTheme {
                ZakhmNavGraph()
            }
        }
    }
}
