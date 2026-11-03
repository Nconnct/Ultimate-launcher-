package com.nconnct.ultimatelauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { UltimateLauncherRoot() }
    }
}

@Composable
private fun UltimateLauncherRoot() {
    MaterialTheme(colorScheme = darkColorScheme(
        background = Color(0xFF101116),
        surface = Color(0xFF191A22),
        primary = Color(0xFFB9C7FF)
    )) {
        Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF101116)) {
            Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Ultimate Launcher", style = MaterialTheme.typography.headlineMedium, color = Color.White)
                    Text("Workspace foundation", style = MaterialTheme.typography.titleMedium, color = Color(0xFFB9C7FF))
                    Surface(color = Color(0xCC242631), shape = RoundedCornerShape(28.dp)) {
                        Text("Glass surfaces and workspace engine are being built next.", modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp), color = Color(0xFFE6E7F0))
                    }
                }
            }
        }
    }
}
