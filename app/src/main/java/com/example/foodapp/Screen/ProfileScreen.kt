package com.example.foodapp.Screen

import android.app.Activity
import android.util.Patterns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodapp.R
import com.example.foodapp.Retrofit.AuthViewModel
import com.example.foodapp.Retrofit.FoodsViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel = viewModel(),
    foodsViewModel: FoodsViewModel = viewModel()
) {
    val userState by authViewModel.currentUser.collectAsState()
    val favorites by foodsViewModel.favoriteFoods.collectAsState()
    val orders by foodsViewModel.orderItems.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var statusMessage by remember { mutableStateOf<String?>(null) }
    var statusColor by remember { mutableStateOf(Color.Gray) }
    var processingAction by remember { mutableStateOf<AuthAction?>(null) }
    val successColor = Color(0xFF2E7D32)

    LaunchedEffect(userState?.uid) {
        foodsViewModel.setActiveUser(userState?.uid)
        if (userState != null) {
            processingAction = null
            statusColor = successColor
            statusMessage = "Welcome, ${userState?.email ?: "friend"}!"
            email = ""
            password = ""
        } else {
            processingAction = null
        }
    }

    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        when (result.resultCode) {
            Activity.RESULT_OK -> {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
                    authViewModel.loginWithGoogle(account) { success, error ->
                        processingAction = null
                        if (success) {
                            statusColor = successColor
                            statusMessage = "Signed in with Google"
                        } else {
                            statusColor = Color.Red
                            statusMessage = error ?: "Unable to sign in with Google"
                        }
                    }
                } catch (e: ApiException) {
                    processingAction = null
                    statusColor = Color.Red
                    statusMessage = e.localizedMessage ?: "Google Sign-In failed"
                }
            }

            Activity.RESULT_CANCELED -> {
                processingAction = null
                statusColor = Color.Gray
                statusMessage = "Google sign-in cancelled"
            }

            else -> {
                processingAction = null
                statusColor = Color.Red
                statusMessage = "Could not complete Google Sign-In"
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (userState == null) {
            AuthForm(
                email = email,
                password = password,
                processingAction = processingAction,
                onEmailChange = { email = it },
                onPasswordChange = { password = it },
                onSignIn = {
                    val trimmedEmail = email.trim()
                    email = trimmedEmail

                    when {
                        trimmedEmail.isEmpty() -> {
                            statusColor = Color.Red
                            statusMessage = "Enter your email"
                        }

                        !isValidEmail(trimmedEmail) -> {
                            statusColor = Color.Red
                            statusMessage = "Invalid email address"
                        }

                        password.length < 6 -> {
                            statusColor = Color.Red
                            statusMessage = "Password must be at least 6 characters"
                        }

                        else -> {
                            processingAction = AuthAction.SIGN_IN
                            statusColor = Color.Gray
                            statusMessage = "Signing you in..."
                            authViewModel.loginWithEmail(trimmedEmail, password) { success, error ->
                                processingAction = null
                                if (success) {
                                    statusColor = successColor
                                    statusMessage = "Welcome back!"
                                    email = ""
                                    password = ""
                                } else {
                                    statusColor = Color.Red
                                    statusMessage = error ?: "Could not sign in. Please try again"
                                }
                            }
                        }
                    }
                },
                onSignUp = {
                    val trimmedEmail = email.trim()
                    email = trimmedEmail

                    when {
                        trimmedEmail.isEmpty() -> {
                            statusColor = Color.Red
                            statusMessage = "Enter your email"
                        }

                        !isValidEmail(trimmedEmail) -> {
                            statusColor = Color.Red
                            statusMessage = "Invalid email address"
                        }

                        password.length < 6 -> {
                            statusColor = Color.Red
                            statusMessage = "Password must be at least 6 characters"
                        }

                        else -> {
                            processingAction = AuthAction.SIGN_UP
                            statusColor = Color.Gray
                            statusMessage = "Creating your account..."
                            authViewModel.registerWithEmail(trimmedEmail, password) { success, error ->
                                processingAction = null
                                if (success) {
                                    statusColor = successColor
                                    statusMessage = "Account created! Welcome."
                                    email = ""
                                    password = ""
                                } else {
                                    statusColor = Color.Red
                                    statusMessage = error ?: "Could not create account. Please try again"
                                }
                            }
                        }
                    }
                },
                onGoogleSignIn = { context ->
                    if (processingAction != null) return@AuthForm
                    processingAction = AuthAction.GOOGLE
                    statusColor = Color.Gray
                    statusMessage = "Opening Google..."
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestIdToken(context.getString(R.string.default_web_client_id))
                        .build()
                    val client = GoogleSignIn.getClient(context, gso)
                    googleLauncher.launch(client.signInIntent)
                }
            )
        } else {
            LoggedInDashboard(
                email = userState?.email ?: "",
                favoriteCount = favorites.size,
                orderCount = orders.size,
                onSignOut = {
                    processingAction = null
                    foodsViewModel.setActiveUser(null)
                    foodsViewModel.clearCurrentOrder()
                    authViewModel.signOut()
                    statusColor = Color.Gray
                    statusMessage = "You have signed out"
                }
            )
        }

        statusMessage?.let {
            Spacer(Modifier.height(16.dp))
            Text(
                text = it,
                color = statusColor,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

private enum class AuthAction {
    SIGN_IN,
    SIGN_UP,
    GOOGLE
}

private fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

@Composable
private fun AuthForm(
    email: String,
    password: String,
    processingAction: AuthAction?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignIn: () -> Unit,
    onSignUp: () -> Unit,
    onGoogleSignIn: (android.content.Context) -> Unit
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Welcome", style = MaterialTheme.typography.headlineSmall)
            Text(
                text = "Sign in to sync your favourites and orders",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                label = { Text("Email") },
                singleLine = true,
                enabled = processingAction == null,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = { Text("Password") },
                singleLine = true,
                enabled = processingAction == null,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Button(
                onClick = onSignIn,
                modifier = Modifier.fillMaxWidth(),
                enabled = processingAction == null
            ) {
                if (processingAction == AuthAction.SIGN_IN) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .width(20.dp)
                            .height(20.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                } else {
                    Text("Sign In")
                }
            }

            OutlinedButton(
                onClick = onSignUp,
                modifier = Modifier.fillMaxWidth(),
                enabled = processingAction == null
            ) {
                if (processingAction == AuthAction.SIGN_UP) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .width(20.dp)
                            .height(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Create account")
                }
            }

            Divider()

            OutlinedButton(
                onClick = { onGoogleSignIn(context) },
                modifier = Modifier.fillMaxWidth(),
                enabled = processingAction == null || processingAction == AuthAction.GOOGLE
            ) {
                if (processingAction == AuthAction.GOOGLE) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .width(20.dp)
                            .height(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Continue with Google")
                }
            }
        }
    }
}

@Composable
private fun LoggedInDashboard(
    email: String,
    favoriteCount: Int,
    orderCount: Int,
    onSignOut: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Profile", style = MaterialTheme.typography.headlineSmall)
            Text("Email: $email")

            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("Favourite items")
                Text(favoriteCount.toString(), fontWeight = FontWeight.Bold)
            }

            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("Orders")
                Text(orderCount.toString(), fontWeight = FontWeight.Bold)
            }

            Divider()

            TextButton(
                onClick = onSignOut,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign out")
            }
        }
    }
}
