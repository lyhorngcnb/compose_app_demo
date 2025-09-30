// ui/components/Toast.kt
package com.composeapp.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composeapp.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Toast Status Types
enum class ToastStatus {
    SUCCESS,
    ERROR,
    WARNING,
    INFO
}

// Toast Data Class
data class ToastData(
    val message: String,
    val status: ToastStatus,
    val actionText: String? = null,
    val onActionClick: (() -> Unit)? = null,
    val duration: Long = 3000L
)

// Toast State Manager
class ToastState {
    var currentToast by mutableStateOf<ToastData?>(null)
        private set

    fun show(toast: ToastData) {
        currentToast = toast
    }

    fun dismiss() {
        currentToast = null
    }
}

// Remember Toast State
@Composable
fun rememberToastState(): ToastState {
    return remember { ToastState() }
}

// Toast Host Composable
@Composable
fun ToastHost(
    toastState: ToastState,
    modifier: Modifier = Modifier
) {
    val toast = toastState.currentToast
    val scope = rememberCoroutineScope()

    LaunchedEffect(toast) {
        if (toast != null) {
            delay(toast.duration)
            toastState.dismiss()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        AnimatedVisibility(
            visible = toast != null,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(300, easing = FastOutSlowInEasing)
            ) + fadeIn(animationSpec = tween(300)),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(300, easing = FastOutSlowInEasing)
            ) + fadeOut(animationSpec = tween(300))
        ) {
            if (toast != null) {
                ToastItem(
                    toast = toast,
                    onDismiss = { toastState.dismiss() }
                )
            }
        }
    }
}

// Individual Toast Item
@Composable
private fun ToastItem(
    toast: ToastData,
    onDismiss: () -> Unit
) {
    val (backgroundColor, iconRes) = when (toast.status) {
        ToastStatus.SUCCESS -> Pair(Color(0xFF56A74B), R.drawable.ic_success)
        ToastStatus.ERROR -> Pair(Color(0xFFE03044), R.drawable.ic_error)
        ToastStatus.WARNING -> Pair(Color(0xFFFF9800), R.drawable.ic_warning)
        ToastStatus.INFO -> Pair(Color(0xFFAFAFAF), R.drawable.ic_info)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Status Icon
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = toast.status.name,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Message Text
            Text(
                text = toast.message,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            // Action Button (if provided)
            if (toast.actionText != null && toast.onActionClick != null) {
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = toast.actionText,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        toast.onActionClick.invoke()
                        onDismiss()
                    }
                )
            }
        }
    }
}

// Extension Functions for Easy Usage
fun ToastState.showSuccess(
    message: String,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    duration: Long = 3000L
) {
    show(
        ToastData(
            message = message,
            status = ToastStatus.SUCCESS,
            actionText = actionText,
            onActionClick = onActionClick,
            duration = duration
        )
    )
}

fun ToastState.showError(
    message: String,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    duration: Long = 3000L
) {
    show(
        ToastData(
            message = message,
            status = ToastStatus.ERROR,
            actionText = actionText,
            onActionClick = onActionClick,
            duration = duration
        )
    )
}

fun ToastState.showWarning(
    message: String,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    duration: Long = 3000L
) {
    show(
        ToastData(
            message = message,
            status = ToastStatus.WARNING,
            actionText = actionText,
            onActionClick = onActionClick,
            duration = duration
        )
    )
}

fun ToastState.showInfo(
    message: String,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    duration: Long = 3000L
) {
    show(
        ToastData(
            message = message,
            status = ToastStatus.INFO,
            actionText = actionText,
            onActionClick = onActionClick,
            duration = duration
        )
    )
}

// Use THis Toast Guide for Usage
/*
// Success Toast
toastState.showSuccess("Login successful!")

// Error Toast
toastState.showError("Invalid credentials")

// Warning Toast
toastState.showWarning("Please check your input")

// Info Toast
toastState.showInfo("Remember to verify your email")

// With Action Button
toastState.showError(
    message = "Login failed",
    actionText = "RETRY",
    onActionClick = { /* retry logic */ }
)

// Custom Duration (default is 3 seconds)
toastState.showSuccess(
    message = "Saved!",
    duration = 5000L // 5 seconds
)
// Dismiss manually
toastState.dismiss()
 */
