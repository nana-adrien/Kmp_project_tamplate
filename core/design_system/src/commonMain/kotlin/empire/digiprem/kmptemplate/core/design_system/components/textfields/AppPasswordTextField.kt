package empire.digiprem.kmptemplate.core.design_system.components.textfields

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import empire.digiprem.kmptemplate.core.design_system.components.buttons.AppIconButton
import empire.digiprem.kmptemplate.core.design_system.components.icon.AppIconResource
import empire.digiprem.kmptemplate.core.design_system.generated.resources.Res
import empire.digiprem.kmptemplate.core.design_system.generated.resources.cd_hide_password
import empire.digiprem.kmptemplate.core.design_system.generated.resources.cd_show_password
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppPasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isPasswordVisible: Boolean,
    onToggleVisibility: () -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    supportingText: String? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Done,
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value         = value,
            onValueChange = onValueChange,
            modifier      = Modifier.fillMaxWidth(),
            label         = label?.let { { Text(it) } },
            placeholder   = placeholder?.let { { Text(it) } },
            isError       = isError,
            enabled       = enabled,
            singleLine    = true,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None
                                   else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction    = imeAction,
            ),
            trailingIcon = {
                val icon = if (isPasswordVisible) Icons.Default.Visibility
                           else Icons.Default.VisibilityOff
                val cd = stringResource(
                    if (isPasswordVisible) Res.string.cd_hide_password
                    else Res.string.cd_show_password
                )
                AppIconButton(
                    icon               = AppIconResource.VectorResource(icon),
                    contentDescription = cd,
                    onClick            = onToggleVisibility,
                )
            },
            shape = MaterialTheme.shapes.medium,
        )
        if (supportingText != null) {
            Text(
                text     = supportingText,
                style    = MaterialTheme.typography.bodySmall,
                color    = if (isError) MaterialTheme.colorScheme.error
                           else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp),
            )
        }
    }
}
