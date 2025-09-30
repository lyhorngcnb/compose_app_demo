// ui/components/CustomBottomSheet.kt
package com.composeapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

// Bottom Sheet State Manager
@OptIn(ExperimentalMaterial3Api::class)
class CustomBottomSheetState(
    val sheetState: SheetState
) {
    suspend fun show() {
        sheetState.show()
    }

    suspend fun hide() {
        sheetState.hide()
    }

    val isVisible: Boolean
        get() = sheetState.isVisible
}

// Remember Bottom Sheet State
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberCustomBottomSheetState(
    skipPartiallyExpanded: Boolean = false,
    confirmValueChange: (SheetValue) -> Boolean = { true }
): CustomBottomSheetState {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded,
        confirmValueChange = confirmValueChange
    )
    return remember { CustomBottomSheetState(sheetState) }
}

// Custom Bottom Sheet Composable
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomBottomSheet(
    state: CustomBottomSheetState,
    title: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    showDragHandle: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    footerContent: (@Composable ColumnScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    if (state.isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = state.sheetState,
            dragHandle = if (showDragHandle) {
                {
                    BottomSheetDefaults.DragHandle(
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )
                }
            } else null,
            containerColor = containerColor,
            contentColor = contentColor,
            scrimColor = scrimColor,
            modifier = modifier,
            windowInsets = BottomSheetDefaults.windowInsets
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                // Header Title
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = contentColor,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                ) {
                    content()
                }

                // Footer (if provided)
                if (footerContent != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp, bottom = 24.dp)
                    ) {
                        footerContent()
                    }
                }
            }
        }
    }
}

// Extension function for easy showing
suspend fun CustomBottomSheetState.showSheet() {
    this.show()
}

suspend fun CustomBottomSheetState.hideSheet() {
    this.hide()
}

// ============================================
// USAGE EXAMPLES
// ============================================

/*
// Example 1: Simple Bottom Sheet with Single Button
@Composable
fun SimpleBottomSheetExample() {
    val bottomSheetState = rememberCustomBottomSheetState()
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        Button(onClick = {
            scope.launch { bottomSheetState.show() }
        }) {
            Text("Show Bottom Sheet")
        }
    }

    CustomBottomSheet(
        state = bottomSheetState,
        title = "Confirmation",
        onDismiss = { 
            scope.launch { bottomSheetState.hide() }
        },
        content = {
            Text(
                text = "Are you sure you want to delete this item?",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        },
        footerContent = {
            ButtonPrimary(
                text = "Delete",
                onClick = {
                    scope.launch {
                        // Handle delete action
                        bottomSheetState.hide()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}

// Example 2: Bottom Sheet with Multiple Buttons
@Composable
fun MultiButtonBottomSheetExample() {
    val bottomSheetState = rememberCustomBottomSheetState()
    val scope = rememberCoroutineScope()
    var selectedOption by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        Button(onClick = {
            scope.launch { bottomSheetState.show() }
        }) {
            Text("Show Options")
        }
    }

    CustomBottomSheet(
        state = bottomSheetState,
        title = "Select Action",
        onDismiss = { 
            scope.launch { bottomSheetState.hide() }
        },
        content = {
            // Custom content
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Choose an action to perform:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                
                RadioButton(
                    selected = selectedOption == "Edit",
                    onClick = { selectedOption = "Edit" }
                )
                Text("Edit")
                
                RadioButton(
                    selected = selectedOption == "Share",
                    onClick = { selectedOption = "Share" }
                )
                Text("Share")
            }
        },
        footerContent = {
            // Multiple buttons in footer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        scope.launch { bottomSheetState.hide() }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }
                
                ButtonPrimary(
                    text = "Confirm",
                    onClick = {
                        scope.launch {
                            // Handle action
                            bottomSheetState.hide()
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    )
}

// Example 3: Bottom Sheet with Form Content
@Composable
fun FormBottomSheetExample() {
    val bottomSheetState = rememberCustomBottomSheetState()
    val scope = rememberCoroutineScope()
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        Button(onClick = {
            scope.launch { bottomSheetState.show() }
        }) {
            Text("Add User")
        }
    }

    CustomBottomSheet(
        state = bottomSheetState,
        title = "Add New User",
        onDismiss = { 
            scope.launch { bottomSheetState.hide() }
        },
        content = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Name Field
                Column {
                    Text(
                        text = "Name",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    CustomTextFieldWithIcons(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = "Enter name",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                // Email Field
                Column {
                    Text(
                        text = "Email",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    CustomTextFieldWithIcons(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = "Enter email",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        footerContent = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ButtonPrimary(
                    text = "Save",
                    onClick = {
                        scope.launch {
                            // Handle save
                            bottomSheetState.hide()
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
}

// Example 4: Bottom Sheet with Scrollable Content
@Composable
fun ScrollableBottomSheetExample() {
    val bottomSheetState = rememberCustomBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        Button(onClick = {
            scope.launch { bottomSheetState.show() }
        }) {
            Text("Show Terms")
        }
    }

    CustomBottomSheet(
        state = bottomSheetState,
        title = "Terms and Conditions",
        onDismiss = { 
            scope.launch { bottomSheetState.hide() }
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                repeat(10) { index ->
                    Text(
                        text = "Section ${index + 1}: Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        },
        footerContent = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        scope.launch { bottomSheetState.hide() }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Decline")
                }
                
                ButtonPrimary(
                    text = "Accept",
                    onClick = {
                        scope.launch { bottomSheetState.hide() }
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    )
}

// Example 5: Integration in Login Screen
@Composable
fun LoginScreenWithBottomSheet(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val bottomSheetState = rememberCustomBottomSheetState()
    val scope = rememberCoroutineScope()
    
    // Your login screen content
    Column(modifier = Modifier.fillMaxSize()) {
        // ... login UI ...
        
        TextButton(onClick = {
            scope.launch { bottomSheetState.show() }
        }) {
            Text("Forgot Password?")
        }
    }

    // Forgot Password Bottom Sheet
    CustomBottomSheet(
        state = bottomSheetState,
        title = "Reset Password",
        onDismiss = { 
            scope.launch { bottomSheetState.hide() }
        },
        content = {
            var email by remember { mutableStateOf("") }
            
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Enter your email address and we'll send you a link to reset your password.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                
                CustomTextFieldWithIcons(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "Enter your email",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        footerContent = {
            ButtonPrimary(
                text = "Send Reset Link",
                onClick = {
                    scope.launch {
                        // Handle password reset
                        bottomSheetState.hide()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}
*/