package com.nconnct.ultimatelauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        window.navigationBarColor = android.graphics.Color.TRANSPARENT
        window.decorView.systemUiVisibility =
            android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
            android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        setContent { UltimateLauncherRoot() }
    }
}

private val Ink = Color(0xFFF5F3FF)
private val Muted = Color(0xFFD8D5E8)
private val Glass = Color(0x38FFFFFF)
private val GlassEdge = Color(0x66FFFFFF)
private val Accent = Color(0xFFD0BCFF)

@Composable
private fun UltimateLauncherRoot() {
    MaterialTheme(colorScheme = darkColorScheme(
        background = Color(0xFF17142B),
        surface = Color(0xFF27223D),
        primary = Accent,
        onBackground = Ink
    )) {
        Box(Modifier.fillMaxSize()) {
            // Ambient wallpaper inspired by Pixel's expressive Material You palette.
            Box(Modifier.fillMaxSize().background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF242044), Color(0xFF4C3D75), Color(0xFF242D51), Color(0xFF17142B)),
                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                    end = androidx.compose.ui.geometry.Offset(1000f, 1800f)
                )
            ))
            Box(Modifier.align(Alignment.TopEnd).offset(x = 90.dp, y = 120.dp).size(300.dp)
                .background(Brush.radialGradient(listOf(Color(0x88D9B8FF), Color.Transparent)), CircleShape))
            Box(Modifier.align(Alignment.BottomStart).offset(x = (-90).dp, y = 80.dp).size(340.dp)
                .background(Brush.radialGradient(listOf(Color(0x6649B8D8), Color.Transparent)), CircleShape))

            Column(
                modifier = Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding()
                    .padding(horizontal = 22.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text("MONDAY, NOVEMBER 3", color = Muted, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.2.sp)
                            Text("2:01", color = Ink, fontSize = 34.sp, fontWeight = FontWeight.Light)
                        }
                        FrostedPanel(shape = CircleShape, padding = 13.dp) {
                            Text("☀️  24°", color = Ink, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                        }
                    }

                    FrostedPanel(shape = RoundedCornerShape(30.dp), padding = 20.dp) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                                Text("Good afternoon", color = Muted, fontSize = 14.sp)
                                Text("Your day,\nyour space.", color = Ink, fontSize = 29.sp, lineHeight = 34.sp, fontWeight = FontWeight.Medium)
                            }
                            Text("✦", color = Accent, fontSize = 50.sp)
                        }
                    }

                    // Familiar Pixel-style app grid, with glassy icon backplates.
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        AppShortcut("Google", "G", Color(0xFFFAFAFA), Color(0xFF4285F4))
                        AppShortcut("Photos", "✿", Color(0xFFECE7FF), Color(0xFF8B5CF6))
                        AppShortcut("Play Store", "▶", Color(0xFFE2F5EE), Color(0xFF16A085))
                        AppShortcut("Settings", "⚙", Color(0xFFE4E8F5), Color(0xFF52658F))
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    FrostedPanel(shape = RoundedCornerShape(34.dp), padding = 15.dp) {
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text("⌕", color = Ink, fontSize = 28.sp)
                            Spacer(Modifier.width(10.dp))
                            Text("Search your apps", color = Muted, fontSize = 16.sp, modifier = Modifier.weight(1f))
                            Text("🎙", fontSize = 20.sp)
                            Spacer(Modifier.width(12.dp))
                            Text("Lens", color = Muted, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    FrostedPanel(shape = RoundedCornerShape(36.dp), padding = 12.dp) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                            DockIcon("☎", Color(0xFFBCE8D6))
                            DockIcon("✉", Color(0xFFD9D2FF))
                            DockIcon("🌐", Color(0xFFBBDFFF))
                            DockIcon("▣", Color(0xFFFFD7C4))
                        }
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                        Text("•", color = Ink, fontSize = 18.sp)
                        Text("  ○  ○", color = Color(0x88FFFFFF), fontSize = 14.sp)
                    }
                    Text("Swipe up to explore apps", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, color = Color(0x99FFFFFF), fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun FrostedPanel(
    shape: androidx.compose.ui.graphics.Shape,
    padding: androidx.compose.ui.unit.Dp,
    content: @Composable () -> Unit
) {
    Surface(
        color = Color.Transparent,
        shape = shape,
        modifier = Modifier.border(1.dp, Brush.linearGradient(listOf(GlassEdge, Color(0x22FFFFFF))), shape)
            .clip(shape)
    ) {
        Box(Modifier.background(Brush.linearGradient(listOf(Color(0x44FFFFFF), Glass, Color(0x18FFFFFF)))).padding(padding)) {
            content()
        }
    }
}

@Composable
private fun AppShortcut(label: String, glyph: String, backplate: Color, glyphColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.width(70.dp).clickable { }) {
        Box(Modifier.size(58.dp).clip(RoundedCornerShape(21.dp))
            .background(Brush.linearGradient(listOf(Color(0xCCFFFFFF), backplate.copy(alpha = 0.82f))))
            .border(1.dp, Color(0x66FFFFFF), RoundedCornerShape(21.dp)),
            contentAlignment = Alignment.Center) {
            Text(glyph, color = glyphColor, fontSize = 27.sp, fontWeight = FontWeight.Bold)
        }
        Text(label, color = Ink, fontSize = 12.sp, textAlign = TextAlign.Center, maxLines = 1)
    }
}

@Composable
private fun DockIcon(glyph: String, tint: Color) {
    Box(Modifier.size(52.dp).clip(CircleShape)
        .background(Brush.verticalGradient(listOf(Color(0xCCFFFFFF), tint.copy(alpha = 0.78f))))
        .border(1.dp, Color(0x66FFFFFF), CircleShape), contentAlignment = Alignment.Center) {
        Text(glyph, color = Color(0xFF24233A), fontSize = 23.sp, fontWeight = FontWeight.SemiBold)
    }
}
