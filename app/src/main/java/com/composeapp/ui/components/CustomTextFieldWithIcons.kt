import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomTextFieldWithIcons(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    isPassword: Boolean = false,
    isError: Boolean = false,
    modifier: Modifier = Modifier,
    outlineColor: Color = Color.Gray,
    focusedOutlineColor: Color = Color.Blue,
    errorOutlineColor: Color = Color.Red,
    leadingIcon: (@Composable (() -> Unit))? = null,
    trailingIcon: (@Composable (() -> Unit))? = null,
    showPassword: Boolean = false,
    onPasswordVisibilityToggle: (() -> Unit)? = null
) {
    var isFocused by remember { mutableStateOf(false) }

    val focusModifier = Modifier
        .onFocusChanged { state -> isFocused = state.isFocused }

    val currentOutlineColor = when {
        isError -> errorOutlineColor
        isFocused -> focusedOutlineColor
        else -> outlineColor
    }

    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = currentOutlineColor,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .then(focusModifier),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (leadingIcon != null) {
                Box(modifier = Modifier.padding(end = 8.dp)) {
                    leadingIcon()
                }
            }

            Box(modifier = Modifier.weight(1f)) {
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    singleLine = true,
                    visualTransformation = if (isPassword && !showPassword) PasswordVisualTransformation() else VisualTransformation.None,
                    textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                    modifier = Modifier.fillMaxWidth()
                )

                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            }

            if (trailingIcon != null) {
                Box(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable {
                            onPasswordVisibilityToggle?.invoke()
                        }
                ) {
                    trailingIcon()
                }
            }
        }
    }
}