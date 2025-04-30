package com.evaluation.mangascope.presentation.signIn

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.evaluation.mangascope.R
import com.evaluation.mangascope.presentation.components.EmailField
import com.evaluation.mangascope.presentation.components.PasswordField
import com.evaluation.mangascope.presentation.theme.MangaScopeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(modifier: Modifier = Modifier) {
    val focusManger = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.sign_in))
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Spacer(modifier = Modifier.weight(0.5f))
            Card {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(R.string.zenithra),
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.welcome_back),
                        style = MaterialTheme.typography.displaySmall,
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource(R.string.please_enter_your_details_to_sign_in),
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        LoginIconButton(painter = painterResource(R.drawable.baseline_google_24))
                        LoginIconButton(painter = painterResource(R.drawable.baseline_apple_24))
                    }
                    TextDivider(
                        text = stringResource(R.string.or),
                        modifier = Modifier.padding(vertical = 16.dp),
                    )
                    EmailField(
                        state = rememberTextFieldState(),
                        focusManager = focusManger,
                        keyboardController = keyboardController,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(text = stringResource(R.string.your_email_address))
                        },
                    )
                    PasswordField(
                        state = rememberTextFieldState(),
                        focusManager = focusManger,
                        keyboardController = keyboardController,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 18.dp),
                        placeholder = {
                            Text(text = stringResource(R.string.password))
                        },
                    )
                    ForgotPasswordText(modifier = Modifier.align(Alignment.End))
                    Button(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(text = stringResource(R.string.sign_in))
                    }
                    SignUpText(modifier = Modifier.padding(vertical = 22.dp))
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun LoginIconButton(
    painter: Painter,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    OutlinedIconButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(
            painter = painter,
            contentDescription = null,
            tint = Color.Unspecified,
        )
    }
}

@Composable
private fun TextDivider(
    text: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodySmall,
    textColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HorizontalDivider(modifier = Modifier.weight(1f))
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 4.dp),
            style = textStyle,
            color = textColor,
        )
        HorizontalDivider(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun ForgotPasswordText(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Text(
        text =
            buildAnnotatedString {
                withLink(
                    LinkAnnotation.Clickable(
                        tag = "ForgotPassword",
                        styles = TextLinkStyles(style = SpanStyle(color = MaterialTheme.colorScheme.primary)),
                        linkInteractionListener = {
                            onClick()
                        },
                    ),
                ) {
                    append(stringResource(R.string.forgot_password))
                }
            },
        modifier = modifier.minimumInteractiveComponentSize(),
        style = MaterialTheme.typography.bodySmall,
    )
}

@Composable
private fun SignUpText(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val text =
        buildAnnotatedString {
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
                append(stringResource(R.string.don_t_have_an_account).plus(" "))
            }
            withLink(
                LinkAnnotation.Clickable(
                    tag = "SignUp",
                    styles =
                        TextLinkStyles(
                            style =
                                SpanStyle(
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold,
                                ),
                        ),
                    linkInteractionListener = {
                        onClick()
                    },
                ),
            ) {
                append(stringResource(R.string.sign_up))
            }
        }

    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodyMedium,
    )
}

@Preview(showSystemUi = true)
@Composable
private fun SignInScreenPreview() {
    MangaScopeTheme {
        SignInScreen()
    }
}
