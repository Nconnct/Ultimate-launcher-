package com.nconnct.ultimatelauncher

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import android.webkit.WebView
import android.webkit.WebViewClient
import android.net.Uri

data class LauncherApp(val title: String, val packageName: String, val icon: ImageBitmap)

class MainActivity : ComponentActivity() {
    private fun drawableBitmap(drawable: Drawable): ImageBitmap {
        val width = drawable.intrinsicWidth.coerceAtLeast(1)
        val height = drawable.intrinsicHeight.coerceAtLeast(1)
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap.asImageBitmap()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        window.navigationBarColor = android.graphics.Color.TRANSPARENT
        window.decorView.systemUiVisibility =  android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        val launcherIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        val apps = packageManager.queryIntentActivities(launcherIntent, 0)
            .mapNotNull { info ->
                runCatching {
                    LauncherApp(
                        info.loadLabel(packageManager).toString(),
                        info.activityInfo.packageName,
                        drawableBitmap(info.loadIcon(packageManager))
                    )
                }.getOrNull()
            }
            .distinctBy { it.packageName }
            .sortedBy { it.title.lowercase() }

        setContent {
            UltimateLauncherRoot(
                apps = apps,
                launchApp = { pkg -> packageManager.getLaunchIntentForPackage(pkg)?.let { startActivity(it) } }
            )
        }
    }
}

private val Ink = Color(0xFFF5F3FF)
private val Muted = Color(0xFFD8D5E8)
private val Glass = Color(0x38FFFFFF)
private val GlassEdge = Color(0x66FFFFFF)
private val Accent = Color(0xFFD0BCFF)

@Composable
private fun UltimateLauncherRoot(apps: List<LauncherApp>, launchApp: (String) -> Unit) {
    var drawerOpen by remember { mutableStateOf(false) }
    var universalSearchOpen by remember { mutableStateOf(false) }
    var search by remember { mutableStateOf("") }
    var universalQuery by remember { mutableStateOf("") }
    var universalTab by remember { mutableStateOf("Apps") }
    var webSubmitted by remember { mutableStateOf(false) }
    MaterialTheme(colorScheme = darkColorScheme(background = Color(0xFF17142B), surface = Color(0xFF27223D), primary = Accent, onBackground = Ink)) {
        Box(Modifier.fillMaxSize()) {
            Box(Modifier.fillMaxSize().background(Brush.linearGradient(listOf(Color(0xFF242044), Color(0xFF4C3D75), Color(0xFF242D51), Color(0xFF17142B)))))
            Box(Modifier.align(Alignment.TopEnd).offset(x = 90.dp, y = 120.dp).size(300.dp).background(Brush.radialGradient(listOf(Color(0x88D9B8FF), Color.Transparent)), CircleShape))
            Box(Modifier.align(Alignment.BottomStart).offset(x = (-90).dp, y = 80.dp).size(340.dp).background(Brush.radialGradient(listOf(Color(0x6649B8D8), Color.Transparent)), CircleShape))

            if (!drawerOpen && !universalSearchOpen) {
                Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().padding(horizontal = 22.dp, vertical = 12.dp).pointerInput(Unit) { var total = 0f; detectVerticalDragGestures(onVerticalDrag = { change, amount -> total += amount; if (kotlin.math.abs(total) > 65f) { if (total < 0) drawerOpen = true else universalSearchOpen = true; total = 0f; change.consume() } }, onDragEnd = { total = 0f }, onDragCancel = { total = 0f }) }, verticalArrangement = Arrangement.SpaceBetween) {
                    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text("YOUR HOME SCREEN", color = Muted, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.2.sp)
                                Text("Welcome", color = Ink, fontSize = 34.sp, fontWeight = FontWeight.Light)
                            }
                            FrostedPanel(CircleShape, 13.dp) { Text("✦", color = Accent, fontSize = 24.sp) }
                        }
                        FrostedPanel(RoundedCornerShape(30.dp), 20.dp) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                                    Text("Material You • Liquid Glass", color = Muted, fontSize = 14.sp)
                                    Text("Your apps.\nYour space.", color = Ink, fontSize = 29.sp, lineHeight = 34.sp, fontWeight = FontWeight.Medium)
                                }
                                Text("✧", color = Accent, fontSize = 50.sp)
                            }
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            apps.take(4).forEach { app -> HomeApp(app, launchApp) }
                        }
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        FrostedPanel(RoundedCornerShape(34.dp), 15.dp) {
                            Row(Modifier.fillMaxWidth().clickable { drawerOpen = true }, verticalAlignment = Alignment.CenterVertically) {
                                Text("⌕", color = Ink, fontSize = 28.sp)
                                Spacer(Modifier.width(10.dp))
                                Text("Swipe down: Universal Search", color = Muted, fontSize = 16.sp, modifier = Modifier.weight(1f))
                                Text("↑", color = Ink, fontSize = 22.sp)
                            }
                        }
                        FrostedPanel(RoundedCornerShape(36.dp), 12.dp) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                                apps.take(4).forEach { app -> DockApp(app, launchApp) }
                            }
                        }
                        Text("Swipe up for apps • Swipe down for universal search", Modifier.fillMaxWidth().clickable { universalSearchOpen = true }, textAlign = TextAlign.Center, color = Color(0x99FFFFFF), fontSize = 12.sp)
                    }
                }
            } else if (drawerOpen) {
                Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().padding(horizontal = 18.dp, vertical = 12.dp).pointerInput(Unit) { var drag = 0f; detectVerticalDragGestures(onVerticalDrag = { change, amount -> drag += amount; if (drag > 70f) { drawerOpen = false; drag = 0f; change.consume() } }, onDragEnd = { drag = 0f }, onDragCancel = { drag = 0f }) }) {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text("All apps", color = Ink, fontSize = 28.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                        Text("✕", color = Ink, fontSize = 24.sp, modifier = Modifier.clickable { drawerOpen = false }.padding(8.dp))
                    }
                    Spacer(Modifier.height(14.dp))
                    FrostedPanel(RoundedCornerShape(28.dp), 14.dp) {
                        BasicTextField(value = search, onValueChange = { search = it }, singleLine = true,
                            textStyle = androidx.compose.ui.text.TextStyle(color = Ink, fontSize = 16.sp),
                            decorationBox = { inner -> if (search.isEmpty()) Text("Search installed apps", color = Muted); inner() })
                    }
                    Spacer(Modifier.height(14.dp))
                    val filtered = apps.filter { it.title.contains(search, ignoreCase = true) }
                    LazyVerticalGrid(columns = GridCells.Fixed(4), contentPadding = PaddingValues(bottom = 20.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
                        items(filtered, key = { it.packageName }) { app ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().clickable { launchApp(app.packageName) }) {
                                Image(app.icon, app.title, Modifier.size(54.dp))
                                Spacer(Modifier.height(7.dp))
                                Text(app.title, color = Ink, fontSize = 11.sp, maxLines = 2, textAlign = TextAlign.Center)
                            }
                        }
                    }
                }
            }
            if (universalSearchOpen) {
                Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().padding(horizontal = 16.dp, vertical = 12.dp)) {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text("Universal Search", color = Ink, fontSize = 27.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                        Text("✕", color = Ink, fontSize = 24.sp, modifier = Modifier.clickable { universalSearchOpen = false }.padding(8.dp))
                    }
                    Spacer(Modifier.height(12.dp))
                    FrostedPanel(RoundedCornerShape(28.dp), 15.dp) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("⌕", color = Ink, fontSize = 26.sp)
                            Spacer(Modifier.width(8.dp))
                            Box(Modifier.weight(1f)) {
                                if (universalQuery.isEmpty()) Text("Search apps, contacts, files or the web…", color = Muted, fontSize = 14.sp)
                                BasicTextField(value = universalQuery, onValueChange = { universalQuery = it; webSubmitted = false }, singleLine = true, textStyle = androidx.compose.ui.text.TextStyle(color = Ink, fontSize = 15.sp))
                            }
                            Text("↵", color = Accent, fontSize = 22.sp, modifier = Modifier.clickable { webSubmitted = true })
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Apps", "Contacts", "Files", "Web").forEach { tab ->
                            FrostedPanel(RoundedCornerShape(24.dp), 10.dp) {
                                Text(tab, color = if (universalTab == tab) Accent else Muted, fontSize = 13.sp, modifier = Modifier.clickable { universalTab = tab; webSubmitted = false }.padding(horizontal = 6.dp))
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    when (universalTab) {
                        "Apps" -> {
                            val matches = apps.filter { it.title.contains(universalQuery, true) }
                            LazyVerticalGrid(columns = GridCells.Fixed(4), contentPadding = PaddingValues(bottom = 18.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                items(matches, key = { it.packageName }) { app ->
                                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().clickable { launchApp(app.packageName) }) {
                                        Image(app.icon, app.title, Modifier.size(50.dp))
                                        Spacer(Modifier.height(6.dp))
                                        Text(app.title, color = Ink, fontSize = 11.sp, maxLines = 2, textAlign = TextAlign.Center)
                                    }
                                }
                            }
                        }
                        "Contacts" -> Text("Contact lookup is planned; it will require Contacts permission.", color = Muted, modifier = Modifier.padding(12.dp))
                        "Files" -> Text("On-device file indexing is planned; storage/media permission support is not enabled yet.", color = Muted, modifier = Modifier.padding(12.dp))
                        "Web" -> if (webSubmitted && universalQuery.isNotBlank()) {
                            AndroidView(modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(22.dp)), factory = { context ->
                                WebView(context).apply { settings.javaScriptEnabled = true; settings.domStorageEnabled = true; webViewClient = WebViewClient(); loadUrl("https://www.google.com/search?q=" + Uri.encode(universalQuery)) }
                            }, update = { view -> if (view.url?.contains(Uri.encode(universalQuery)) != true) view.loadUrl("https://www.google.com/search?q=" + Uri.encode(universalQuery)) })
                        } else Text("Type a query and tap ↵ to search the web inside the launcher.", color = Muted, modifier = Modifier.padding(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun FrostedPanel(shape: androidx.compose.ui.graphics.Shape, padding: androidx.compose.ui.unit.Dp, content: @Composable () -> Unit) {
    Surface(color = Color.Transparent, shape = shape, modifier = Modifier.border(1.dp, Brush.linearGradient(listOf(GlassEdge, Color(0x22FFFFFF))), shape).clip(shape)) {
        Box(Modifier.background(Brush.linearGradient(listOf(Color(0x44FFFFFF), Glass, Color(0x18FFFFFF)))).padding(padding)) { content() }
    }
}

@Composable
private fun HomeApp(app: LauncherApp, launchApp: (String) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.width(70.dp).clickable { launchApp(app.packageName) }) {
        Box(Modifier.size(58.dp).clip(RoundedCornerShape(21.dp)).background(Color(0x44FFFFFF)).border(1.dp, Color(0x66FFFFFF), RoundedCornerShape(21.dp)), contentAlignment = Alignment.Center) {
            Image(app.icon, app.title, Modifier.size(46.dp))
        }
        Text(app.title, color = Ink, fontSize = 11.sp, textAlign = TextAlign.Center, maxLines = 1)
    }
}

@Composable
private fun DockApp(app: LauncherApp, launchApp: (String) -> Unit) {
    Box(Modifier.size(52.dp).clip(CircleShape).background(Brush.verticalGradient(listOf(Color(0xCCFFFFFF), Color(0x66FFFFFF)))).border(1.dp, Color(0x66FFFFFF), CircleShape).clickable { launchApp(app.packageName) }, contentAlignment = Alignment.Center) {
        Image(app.icon, app.title, Modifier.size(38.dp))
    }
}
