// ui/home/HomeScreen.kt
package com.composeapp.ui.home

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.composeapp.R
import com.composeapp.data.local.ThemePreference
import com.composeapp.data.local.TokenManager
import com.composeapp.ui.components.ButtonPrimary
import com.composeapp.ui.components.CustomBottomSheet
import com.composeapp.ui.components.rememberCustomBottomSheetState
import com.composeapp.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val tokenManager: TokenManager
) : ViewModel() {
    fun logout() {
        tokenManager.clearToken()
    }
}

// Data class for menu items
data class MenuItem(
    val id: Int,
    val title: String,
    val icon: Int,
    val route: String,
    val backgroundColor: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    themePreference: ThemePreference,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val isDarkMode by themePreference.isDarkMode
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberCustomBottomSheetState(skipPartiallyExpanded = true)
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Animation state for theme toggle
    var themeIconScale by remember { mutableStateOf(1f) }
    val scaleAnimation by animateFloatAsState(
        targetValue = themeIconScale,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "theme_icon_scale"
    )

    // Menu items
    val menuItems = remember {
        listOf(
            MenuItem(1, "Notes", R.drawable.ic_notes, Screen.Notes.route, Color(0xFF6366F1)),
            MenuItem(2, "Tasks", R.drawable.ic_tasks, Screen.Notes.route, Color(0xFFEC4899)),
            MenuItem(3, "Calendar", R.drawable.ic_calendar, Screen.Notes.route, Color(0xFF8B5CF6)),
            MenuItem(4, "Profile", R.drawable.ic_profile, Screen.Notes.route, Color(0xFF14B8A6)),
            MenuItem(5, "Settings", R.drawable.ic_settings, Screen.Notes.route, Color(0xFFF59E0B)),
            MenuItem(6, "Help", R.drawable.ic_info, Screen.Notes.route, Color(0xFF10B981))
        )
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Welcome Back",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "Have a productive day!",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    // Theme toggle button with smooth animation
                    IconButton(
                        onClick = {
                            // Trigger scale animation
                            themeIconScale = 0.7f
                            scope.launch {
                                kotlinx.coroutines.delay(150)
                                themePreference.toggleTheme()
                                themeIconScale = 1f
                            }
                        },
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .size(40.dp)
                    ) {
                        // Crossfade animation for icon transition
                        Crossfade(
                            targetState = isDarkMode,
                            animationSpec = tween(
                                durationMillis = 400,
                                easing = FastOutSlowInEasing
                            ),
                            label = "theme_icon_crossfade"
                        ) { darkMode ->
                            Icon(
                                painter = painterResource(
                                    id = if (darkMode) R.drawable.ic_dark else R.drawable.ic_light
                                ),
                                contentDescription = if (darkMode) "Switch to Light Mode" else "Switch to Dark Mode",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .size(24.dp)
                                    .scale(scaleAnimation)
                            )
                        }
                    }

                    // More options button with scale animation
                    var moreButtonScale by remember { mutableStateOf(1f) }
                    val moreButtonScaleAnim by animateFloatAsState(
                        targetValue = moreButtonScale,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        ),
                        label = "more_button_scale"
                    )

                    IconButton(
                        onClick = {
                            moreButtonScale = 0.8f
                            scope.launch {
                                kotlinx.coroutines.delay(100)
                                moreButtonScale = 1f
                                bottomSheetState.show()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More Options",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.scale(moreButtonScaleAnim)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->
        // Animated content with fade and slide
        AnimatedContent(
            targetState = isDarkMode,
            transitionSpec = {
                fadeIn(
                    animationSpec = tween(300, easing = LinearEasing)
                ) + slideInVertically(
                    animationSpec = tween(300, easing = FastOutSlowInEasing),
                    initialOffsetY = { it / 20 }
                ) togetherWith fadeOut(
                    animationSpec = tween(200, easing = LinearEasing)
                )
            },
            label = "content_transition"
        ) { _ ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // User Info Card with scale animation on theme change
                val cardScale by animateFloatAsState(
                    targetValue = 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    label = "card_scale"
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(cardScale)
                        .clickable {
                            navController.navigate(Screen.Second.route)
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "JD",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "John Doe",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "john.doe@example.com",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            )
                        }

                        // Status badge with animated color
                        AnimatedContent(
                            targetState = isDarkMode,
                            transitionSpec = {
                                fadeIn(tween(300)) togetherWith fadeOut(tween(300))
                            },
                            label = "badge_transition"
                        ) { darkMode ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (darkMode) Color(0xFF1E293B) else Color(0xFFFEF3C7)
                            ) {
                                Text(
                                    text = if (darkMode) "🌙 Dark" else "☀️ Light",
                                    modifier = Modifier
                                        .padding(horizontal = 12.dp, vertical = 6.dp),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = if (darkMode) Color(0xFFFBBF24) else Color(0xFFF59E0B)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))

                // Menu Grid with staggered animation
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(menuItems) { item ->
                        var isPressed by remember { mutableStateOf(false) }
                        val scale by animateFloatAsState(
                            targetValue = if (isPressed) 0.95f else 1f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessMedium
                            ),
                            label = "menu_item_scale"
                        )

                        MenuItemCard(
                            item = item,
                            scale = scale,
                            onClick = {
                                isPressed = true
                                scope.launch {
                                    kotlinx.coroutines.delay(100)
                                    isPressed = false
                                    navController.navigate(item.route)
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    // Bottom Sheet with smooth animations
    CustomBottomSheet(
        state = bottomSheetState,
        title = "More Options",
        onDismiss = {
            scope.launch {
                bottomSheetState.hide()
            }
        },
        content = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val options = listOf(
                    Triple(R.drawable.ic_profile, "Account Settings", "Manage your account"),
                    Triple(R.drawable.ic_notifications, "Notifications", "Manage notifications"),
                    Triple(R.drawable.ic_privacy, "Privacy & Security", "Control your privacy"),
                    Triple(R.drawable.ic_help, "Help & Support", "Get help and feedback"),
                    Triple(R.drawable.ic_exit, "Logout", "Click to login page"),
                )

                options.forEachIndexed { index, (icon, title, subtitle) ->
                    var isPressed by remember { mutableStateOf(false) }
                    val itemScale by animateFloatAsState(
                        targetValue = if (isPressed) 0.97f else 1f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        ),
                        label = "option_scale_$index"
                    )

                    OptionItem(
                        icon = icon,
                        title = title,
                        subtitle = subtitle,
                        scale = itemScale,
                        onClick = {
                            isPressed = true
//                            logoutPressed = true
                            scope.launch {
                                kotlinx.coroutines.delay(100)
//                                logoutPressed = false
                                bottomSheetState.hide()
                                showLogoutDialog = true
                            }
                        }
                    )

                    if (index < options.size - 1) {
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                    }
                }
            }
        },
        footerContent = {
            Spacer(modifier = Modifier.height(16.dp))
//            Column(
//                modifier = Modifier.fillMaxWidth(),
//                verticalArrangement = Arrangement.spacedBy(12.dp)
//            ) {
//                var logoutPressed by remember { mutableStateOf(false) }
//                val logoutScale by animateFloatAsState(
//                    targetValue = if (logoutPressed) 0.95f else 1f,
//                    animationSpec = spring(
//                        dampingRatio = Spring.DampingRatioMediumBouncy,
//                        stiffness = Spring.StiffnessMedium
//                    ),
//                    label = "logout_scale"
//                )
//
//                Box(modifier = Modifier.scale(logoutScale)) {
//                    ButtonPrimary(
//                        text = "Logout",
//                        onClick = {
//                            logoutPressed = true
//                            scope.launch {
//                                kotlinx.coroutines.delay(100)
//                                logoutPressed = false
//                                bottomSheetState.hide()
//                                showLogoutDialog = true
//                            }
//                        },
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                }
//
//                TextButton(
//                    onClick = {
//                        scope.launch { bottomSheetState.hide() }
//                    },
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Text("Cancel")
//                }
//            }
        }
    )

    // Logout Confirmation Dialog with animations
    AnimatedVisibility(
        visible = showLogoutDialog,
        enter = fadeIn(tween(300)) + scaleIn(
            initialScale = 0.8f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        ),
        exit = fadeOut(tween(200)) + scaleOut(targetScale = 0.8f, animationSpec = tween(200))
    ) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text(
                    text = "Logout",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            },
            text = {
                Text("Are you sure you want to logout?")
            },
            confirmButton = {
                ButtonPrimary(
                    text = "Logout",
                    onClick = {
                        viewModel.logout()
                        showLogoutDialog = false
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun MenuItemCard(
    item: MenuItem,
    scale: Float,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .scale(scale)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = item.backgroundColor.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(item.backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = item.icon),
                    contentDescription = item.title,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun OptionItem(
    icon: Int,
    title: String,
    subtitle: String,
    scale: Float,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Text
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}