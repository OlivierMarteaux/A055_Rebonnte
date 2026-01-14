package com.oliviermarteaux.localshared.composables

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.KeyboardType
import com.oliviermarteaux.shared.composables.SharedFilledTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SharedFilledIntTextField(
    value: Int,
    modifier: Modifier = Modifier,
    textFieldModifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = true,
    textStyle: TextStyle = LocalTextStyle.current,
    label: String = "",
    placeholder: String = "",
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: String ?  = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource? = null,
    shape: Shape = MaterialTheme.shapes.extraSmall,
    colors: TextFieldColors = TextFieldDefaults.colors(
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent
    ),
    imeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Number,
    //_ icon params
    icon: ImageVector? = null,
    iconModifier: Modifier = Modifier,
    contentDescription: String? = null,
    tint: Color = LocalContentColor.current,
    bottomPadding: Dp = 0.dp,
    errorText: String? = null,
    onValueChange: (String) -> Unit = {},
    //_ on value in trailing lambda
    onConfirm: (Int) -> Unit
) {
    var intValue by remember { mutableIntStateOf(value) }
    var textValue by remember { mutableStateOf(intValue.toString()) }

    var intPickerDialog: Boolean by rememberSaveable { mutableStateOf(false) }
    fun toggleIntPickerDialog() { intPickerDialog = !intPickerDialog }

    IntPickerDialog(
        show = intPickerDialog,
        value = value,
        range = 0..50,
        onDismiss = { toggleIntPickerDialog() },
        onConfirm = {
            intValue = it
            onConfirm(it)
            toggleIntPickerDialog()
        }
    )

    // 🔑 KEEP TEXT IN SYNC WITH VALUE
    LaunchedEffect(value) {
        textValue = value.toString()
    }

    ClickableReadOnlyField(
        onClick = { toggleIntPickerDialog() }
    ) {
        SharedFilledTextField(
            value = textValue,
            onValueChange = onValueChange,
            modifier = modifier,
            textFieldModifier = textFieldModifier,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle,
            label = label,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            prefix = prefix,
            suffix = suffix,
            supportingText = supportingText,
            isError = isError,
            visualTransformation = visualTransformation,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            interactionSource = interactionSource,
            shape = shape,
            colors = colors,
            imeAction = imeAction,
            keyboardType = keyboardType,
            icon = icon,
            iconModifier = iconModifier,
            contentDescription = contentDescription,
            tint = tint,
            bottomPadding = bottomPadding,
            errorText = errorText,
        )
    }
}