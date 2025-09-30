// ui/home/HomeScreen.kt
package com.composeapp.ui.home

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
    val bottomSheetState = rememberCustomBottomSheetState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Menu items
    val menuItems = remember {
        listOf(
            MenuItem(1, "Notes", R.drawable.ic_notes, Screen.Notes.route, Color(0xFF6366F1)),
            MenuItem(2, "Tasks", R.drawable.ic_tasks, Screen.Notes.route, Color(0xFFEC4899)),
            MenuItem(3, "Calendar", R.drawable.ic_calendar, Screen.Notes.route, Color(0xFF8B5CF6)),
            MenuItem(4, "Profile", R.drawable.ic_profile, Screen.Notes.route, Color(0xFF14B8A6)),
            MenuItem(5, "Settings", R.drawable.ic_setting, Screen.Notes.route, Color(0xFFF59E0B)),
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
                    // Theme toggle button with custom icons
                    IconButton(
                        onClick = { themePreference.toggleTheme() },
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .size(40.dp)
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (isDarkMode) R.drawable.ic_dark else R.drawable.ic_light
                            ),
                            contentDescription = if (isDarkMode) "Switch to Light Mode" else "Switch to Dark Mode",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // More options button
                    IconButton(
                        onClick = {
                            scope.launch { bottomSheetState.show() }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More Options",
                            tint = MaterialTheme.colorScheme.onSurface
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // User Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
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

                    // Status badge
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isDarkMode) Color(0xFF1E293B) else Color(0xFFFEF3C7)
                    ) {
                        Text(
                            text = if (isDarkMode) "🌙 Dark" else "☀️ Light",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = if (isDarkMode) Color(0xFFFBBF24) else Color(0xFFF59E0B)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Section Title
//            Text(
//                text = "Quick Access",
//                style = MaterialTheme.typography.titleLarge.copy(
//                    fontWeight = FontWeight.Bold
//                ),
//                color = MaterialTheme.colorScheme.onSurface,
//                modifier = Modifier.padding(bottom = 16.dp)
//            )

            // Menu Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(menuItems) { item ->
                    MenuItemCard(
                        item = item,
                        onClick = {
                            navController.navigate(item.route)
                        }
                    )
                }
            }
        }
    }

    // Bottom Sheet for More Options
    CustomBottomSheet(
        state = bottomSheetState,
        title = "More Options",
        onDismiss = {
            scope.launch { bottomSheetState.hide() }
        },
        content = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Account Option
                OptionItem(
                    icon = R.drawable.ic_profile,
                    title = "Account Settings",
                    subtitle = "Manage your account",
                    onClick = {
                        scope.launch {
                            bottomSheetState.hide()
                            // Navigate to account settings
                        }
                    }
                )

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                // Notifications Option
                OptionItem(
                    icon = R.drawable.ic_notifications,
                    title = "Notifications",
                    subtitle = "Manage notifications",
                    onClick = {
                        scope.launch {
                            bottomSheetState.hide()
                            // Navigate to notifications
                        }
                    }
                )

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                // Privacy Option
                OptionItem(
                    icon = R.drawable.ic_privacy,
                    title = "Privacy & Security",
                    subtitle = "Control your privacy",
                    onClick = {
                        scope.launch {
                            bottomSheetState.hide()
                            // Navigate to privacy settings
                        }
                    }
                )

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                // Help Option
                OptionItem(
                    icon = R.drawable.ic_help,
                    title = "Help & Support",
                    subtitle = "Get help and feedback",
                    onClick = {
                        scope.launch {
                            bottomSheetState.hide()
                            // Navigate to help
                        }
                    }
                )
            }
        },
        footerContent = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ButtonPrimary(
                    text = "Logout",
                    onClick = {
                        scope.launch {
                            bottomSheetState.hide()
                            showLogoutDialog = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                TextButton(
                    onClick = {
                        scope.launch { bottomSheetState.hide() }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancel")
                }
            }
        }
    )

    // Logout Confirmation Dialog
    if (showLogoutDialog) {
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

//@Composable
//private fun MenuItemCard(
//    item: MenuItem,
//    onClick: () -> Unit
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .aspectRatio(1f)
//            .clickable(onClick = onClick),
//        shape = RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = item.backgroundColor.copy(alpha = 0.1f)
//        ),
////        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(20.dp),
//            horizontalAlignment = Alignment.Start,
//            verticalArrangement = Arrangement.SpaceBetween
//        ) {
//            // Icon
//            Box(
//                modifier = Modifier
//                    .size(48.dp)
//                    .clip(RoundedCornerShape(12.dp))
//                    .background(item.backgroundColor),
//                contentAlignment = Alignment.Center
//            ) {
//                Icon(
//                    painter = painterResource(id = item.icon),
//                    contentDescription = item.title,
//                    tint = Color.White,
//                    modifier = Modifier.size(24.dp)
//                )
//            }
//
//            // Title
//            Text(
//                text = item.title,
//                style = MaterialTheme.typography.titleMedium.copy(
//                    fontWeight = FontWeight.Bold
//                ),
//                color = MaterialTheme.colorScheme.onSurface
//            )
//        }
//    }
//}
@Composable
private fun MenuItemCard(
    item: MenuItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
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
            horizontalAlignment = Alignment.CenterHorizontally, // center horizontally
            verticalArrangement = Arrangement.Center             // center vertically
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

            Spacer(modifier = Modifier.height(8.dp)) // add spacing between icon and text

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
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
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