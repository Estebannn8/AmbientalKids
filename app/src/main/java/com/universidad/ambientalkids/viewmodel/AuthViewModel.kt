package com.universidad.ambientalkids.viewmodel

import android.content.Context
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.universidad.ambientalkids.R
import com.universidad.ambientalkids.events.AuthEvent
import com.universidad.ambientalkids.state.AuthState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.EmailChanged -> {
                _state.value = _state.value.copy(email = event.email)
            }
            is AuthEvent.PasswordChanged -> {
                _state.value = _state.value.copy(password = event.password)
            }
            is AuthEvent.UsernameChanged -> {
                _state.value = _state.value.copy(username = event.username)
            }
            is AuthEvent.TermsAcceptedChanged -> {
                _state.value = _state.value.copy(termsAccepted = event.accepted)
            }
            is AuthEvent.RecoveryEmailChanged -> {
                _state.value = _state.value.copy(recoveryEmail = event.email)
            }
            AuthEvent.SendPasswordReset -> {
                sendPasswordResetEmail(_state.value.recoveryEmail)
            }
            AuthEvent.Login -> {
                login()
            }
            AuthEvent.Register -> {
                register()
            }
            AuthEvent.SignInWithGoogle -> {
                // Se manejará desde la UI ya que necesita el contexto
            }
            AuthEvent.SignInWithFacebook -> {
                // Implementar lógica para Facebook
            }
            AuthEvent.NavigateToRegister -> {
                _state.value = _state.value.copy(
                    isLoginSelected = false,
                    errorMessage = null
                )
            }
            AuthEvent.NavigateToLogin -> {
                _state.value = _state.value.copy(
                    isLoginSelected = true,
                    errorMessage = null
                )
            }
            AuthEvent.ForgotPassword -> {
                // Implementar lógica para recuperación de contraseña
            }
        }
    }

    fun checkAuthState(onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isAuthChecking = true)
            delay(1000) // Pequeño delay para evitar flickering en el splash
            val isAuthenticated = auth.currentUser != null
            _state.value = _state.value.copy(isAuthChecking = false)
            onComplete(isAuthenticated)
        }
    }


    fun signInWithGoogle(context: Context) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)

            try {
                val credentialManager = CredentialManager.create(context)

                val googleIdOption = GetGoogleIdOption.Builder()
                    .setServerClientId(context.getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val response = credentialManager.getCredential(
                    request = request,
                    context = context
                )

                handleGoogleSignInResult(response.credential)
            } catch (e: GetCredentialException) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Error en autenticación: ${e.message}"
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Error desconocido: ${e.message}"
                )
            }
        }
    }

    private fun login() {
        val currentEmail = _state.value.email.trim()
        val currentPassword = _state.value.password

        if (currentEmail.isEmpty() || currentPassword.isEmpty()) {
            _state.value = _state.value.copy(
                errorMessage = "Por favor completa todos los campos."
            )
            return
        }

        if (!isValidEmail(currentEmail)) {
            _state.value = _state.value.copy(
                errorMessage = "Correo electrónico no válido."
            )
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            auth.signInWithEmailAndPassword(currentEmail, currentPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            isSuccess = true
                        )
                    } else {
                        val error = task.exception
                        val message = when {
                            error is FirebaseAuthInvalidUserException -> {
                                when (error.errorCode) {
                                    "ERROR_USER_NOT_FOUND" -> "No existe un usuario con este correo electrónico."
                                    "ERROR_USER_DISABLED" -> "Esta cuenta ha sido deshabilitada."
                                    "ERROR_INVALID_EMAIL" -> "El formato del correo electrónico es inválido."
                                    else -> "Usuario no válido."
                                }
                            }
                            error is FirebaseAuthInvalidCredentialsException -> {
                                when (error.errorCode) {
                                    "ERROR_INVALID_EMAIL" -> "El formato del correo electrónico es inválido."
                                    "ERROR_WRONG_PASSWORD" -> "Contraseña incorrecta."
                                    else -> "Credenciales inválidas."
                                }
                            }
                            error is FirebaseAuthEmailException -> {
                                "Error con el correo electrónico."
                            }
                            error is FirebaseAuthWeakPasswordException -> {
                                "La contraseña es demasiado débil."
                            }
                            error is FirebaseTooManyRequestsException -> {
                                "Demasiados intentos fallidos. Por favor, inténtalo más tarde."
                            }
                            else -> {
                                "Error al iniciar sesión."
                            }
                        }

                        _state.value = _state.value.copy(
                            isLoading = false,
                            errorMessage = message
                        )
                    }
                }
        }
    }

    private fun register() {
        val currentEmail = _state.value.email.trim()
        val currentPassword = _state.value.password
        val currentUsername = _state.value.username.trim()

        if (currentEmail.isEmpty() || currentPassword.isEmpty() || currentUsername.isEmpty()) {
            _state.value = _state.value.copy(
                errorMessage = "Por favor completa todos los campos."
            )
            return
        }

        if (!_state.value.termsAccepted) {
            _state.value = _state.value.copy(
                errorMessage = "Debes aceptar los términos y condiciones."
            )
            return
        }

        if (!isValidPassword(currentPassword)) {
            _state.value = _state.value.copy(
                errorMessage = "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial."
            )
            return
        }

        if (!isValidEmail(currentEmail)) {
            _state.value = _state.value.copy(
                errorMessage = "Por favor ingresa un correo electrónico válido."
            )
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            try {
                // Validar si el nickname ya existe
                val result = firestore.collection("usuarios")
                    .whereEqualTo("nickname", currentUsername)
                    .limit(1)
                    .get()
                    .await()

                if (!result.isEmpty) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "El nombre de usuario ya está en uso."
                    )
                    return@launch
                }

                // Crear usuario si el nickname no existe
                auth.createUserWithEmailAndPassword(currentEmail, currentPassword)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            user?.updateProfile(userProfileChangeRequest {
                                displayName = currentUsername
                            })

                            val userId = user?.uid ?: run {
                                _state.value = _state.value.copy(
                                    isLoading = false,
                                    errorMessage = "Error al obtener ID de usuario"
                                )
                                return@addOnCompleteListener
                            }

                            val userData = hashMapOf(
                                "uid" to userId,
                                "nickname" to currentUsername,
                                "correo" to currentEmail
                            )

                            firestore.collection("usuarios")
                                .document(userId)
                                .set(userData)
                                .addOnSuccessListener {
                                    _state.value = _state.value.copy(
                                        isLoading = false,
                                        isSuccess = true
                                    )
                                }
                                .addOnFailureListener { e ->
                                    _state.value = _state.value.copy(
                                        isLoading = false,
                                        errorMessage = "Error al guardar usuario: ${e.message}"
                                    )
                                }

                        } else {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                errorMessage = task.exception?.localizedMessage ?: "Error al registrar usuario."
                            )
                        }
                    }

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Error al verificar nombre de usuario: ${e.message}"
                )
            }
        }
    }

    private fun handleGoogleSignInResult(credential: Credential) {
        if (credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {

            try {
                val googleIdTokenCredential = GoogleIdTokenCredential
                    .createFrom(credential.data)

                val firebaseCredential = GoogleAuthProvider.getCredential(
                    googleIdTokenCredential.idToken,
                    null
                )

                auth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val isNewUser = task.result?.additionalUserInfo?.isNewUser ?: false
                            val user = auth.currentUser

                            if (isNewUser && user != null) {
                                val userData = hashMapOf(
                                    "uid" to user.uid,
                                    "nickname" to (user.displayName ?: "Usuario"),
                                    "correo" to (user.email ?: "")
                                )

                                firestore.collection("usuarios")
                                    .document(user.uid)
                                    .set(userData)
                                    .addOnSuccessListener {
                                        _state.value = _state.value.copy(
                                            isLoading = false,
                                            isSuccess = true
                                        )
                                    }
                                    .addOnFailureListener { e ->
                                        _state.value = _state.value.copy(
                                            isLoading = false,
                                            errorMessage = "Error al guardar usuario: ${e.message}"
                                        )
                                    }
                            } else {
                                _state.value = _state.value.copy(
                                    isLoading = false,
                                    isSuccess = true
                                )
                            }
                        } else {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                errorMessage = task.exception?.message ?: "Error en autenticación"
                            )
                        }
                    }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Error procesando credencial: ${e.message}"
                )
            }
        } else {
            _state.value = _state.value.copy(
                isLoading = false,
                errorMessage = "Tipo de credencial no soportado"
            )
        }
    }

    private fun sendPasswordResetEmail(email: String) {
        if (email.isEmpty()) {
            _state.value = _state.value.copy(
                recoveryMessage = "Por favor ingresa tu correo electrónico",
                isRecoverySuccess = false
            )
            return
        }

        if (!isValidEmail(email)) {
            _state.value = _state.value.copy(
                recoveryMessage = "Por favor ingresa un correo electrónico válido",
                isRecoverySuccess = false
            )
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                recoveryMessage = null,
                isRecoverySuccess = false
            )

            try {
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                isRecoverySuccess = true,
                                recoveryMessage = "Correo de recuperación enviado. Por favor revisa tu bandeja de entrada.",
                                recoveryEmail = email
                            )
                        } else {
                            val error = task.exception
                            val message = when (error?.message) {
                                "The email address is badly formatted." ->
                                    "El formato del correo es inválido."
                                "There is no user record corresponding to this identifier." ->
                                    "No existe una cuenta con este correo electrónico."
                                else -> error?.localizedMessage ?: "Error al enviar correo de recuperación."
                            }

                            _state.value = _state.value.copy(
                                isLoading = false,
                                isRecoverySuccess = false,
                                recoveryMessage = message
                            )
                        }
                    }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    isRecoverySuccess = false,
                    recoveryMessage = "Error inesperado: ${e.localizedMessage}"
                )
            }
        }
    }

    fun clearRecoveryState() {
        _state.value = _state.value.copy(
            recoveryMessage = null,
            isRecoverySuccess = false,
            recoveryEmail = ""
        )
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }

    fun clearFields() {
        _state.value = AuthState(isLoginSelected = _state.value.isLoginSelected)
    }

    fun logout() {
        auth.signOut()
        _state.value = AuthState()
    }

    fun getCurrentUserId(): String? = auth.currentUser?.uid

    private fun isValidPassword(password: String): Boolean {
        val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\$%^&*()_+\\-={}:;<>?,.]).{8,}\$")
        return passwordRegex.matches(password)
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$")
        return emailRegex.matches(email)
    }
}