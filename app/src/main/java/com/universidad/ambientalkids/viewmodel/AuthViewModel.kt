package com.universidad.ambientalkids.viewmodel

import android.content.Context
import android.util.Log
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
                _state.value = _state.value.copy(username = event.username.take(13))
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
        Log.d("AuthViewModel", "Starting registration")

        val currentEmail = _state.value.email.trim()
        val currentPassword = _state.value.password
        val currentUsername = _state.value.username.trim()

        // Validaciones iniciales
        if (currentEmail.isEmpty() || currentPassword.isEmpty() || currentUsername.isEmpty()) {
            _state.value = _state.value.copy(
                errorMessage = "Por favor completa todos los campos.",
                isLoading = false
            )
            return
        }

        // Validación de longitud del nickname
        if (!isValidUsername(currentUsername)) {
            _state.value = _state.value.copy(
                errorMessage = "El nombre de usuario no puede tener más de 13 caracteres.",
                isLoading = false
            )
            return
        }

        if (!_state.value.termsAccepted) {
            _state.value = _state.value.copy(
                errorMessage = "Debes aceptar los términos y condiciones.",
                isLoading = false
            )
            return
        }

        if (!isValidPassword(currentPassword)) {
            _state.value = _state.value.copy(
                errorMessage = "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial.",
                isLoading = false
            )
            return
        }

        if (!isValidEmail(currentEmail)) {
            _state.value = _state.value.copy(
                errorMessage = "Por favor ingresa un correo electrónico válido.",
                isLoading = false
            )
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            Log.d("AuthViewModel", "isLoading set to true")

            try {
                // 1. Verificar si el nickname ya existe
                val nicknameQuery = firestore.collection("usuarios")
                    .whereEqualTo("nickname", currentUsername)
                    .limit(1)
                    .get()
                    .await()

                if (!nicknameQuery.isEmpty) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "El nombre de usuario ya está en uso. Por favor elige otro."
                    )
                    return@launch
                }

                // 2. Crear usuario en Firebase Auth
                auth.createUserWithEmailAndPassword(currentEmail, currentPassword)
                    .addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            val user = auth.currentUser
                            user?.updateProfile(userProfileChangeRequest {
                                displayName = currentUsername
                            })?.addOnCompleteListener { profileTask ->
                                if (profileTask.isSuccessful) {
                                    saveUserDataToFirestore(
                                        userId = user.uid,
                                        username = currentUsername,
                                        email = currentEmail
                                    )
                                } else {
                                    _state.value = _state.value.copy(
                                        isLoading = false,
                                        errorMessage = "Error al actualizar el perfil: ${profileTask.exception?.localizedMessage ?: ""}"
                                    )
                                }
                            }
                        } else {
                            val error = authTask.exception
                            val message = when (error) {
                                is com.google.firebase.auth.FirebaseAuthUserCollisionException -> {
                                    "Ya existe una cuenta con este correo electrónico."
                                }
                                is FirebaseAuthWeakPasswordException -> {
                                    "La contraseña es demasiado débil. Asegúrate de incluir mayúsculas, minúsculas, números y símbolos."
                                }
                                is FirebaseAuthInvalidCredentialsException -> {
                                    "El correo electrónico no tiene un formato válido."
                                }
                                else -> {
                                    "Error al registrar usuario: ${error?.localizedMessage ?: "Error desconocido."}"
                                }
                            }

                            _state.value = _state.value.copy(
                                isLoading = false,
                                errorMessage = message
                            )
                        }
                    }

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Error en el registro: ${e.message}"
                )
            }
        }
    }

    private fun saveUserDataToFirestore(
        userId: String,
        username: String,
        email: String
    ) {
        val userData = hashMapOf(
            "uid" to userId,
            "nickname" to username,
            "correo" to email,
            "nivel" to 1,
            "exp" to 0,
            "logros" to emptyList<String>(),
            "insignias" to emptyList<String>(),
            "progresoCategorias" to mapOf(
                "agua" to 0,
                "biologia" to 0,
                "tierra" to 0,
                "reciclaje" to 0
            ),
            "leccionesCompletadas" to emptyMap<String, Any>(),
            "racha" to mapOf(
                "actual" to 0,
                "maxima" to 0,
                "ultimoRegistro" to null
            )
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
                    errorMessage = "Error al guardar datos del usuario: ${e.message}"
                )
                // Opcional: eliminar el usuario creado en Auth si falla Firestore
                auth.currentUser?.delete()
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
                            handleNewGoogleUser(task.result?.additionalUserInfo?.isNewUser ?: false)
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

    private fun handleNewGoogleUser(isNewUser: Boolean) {
        if (isNewUser) {
            saveNewGoogleUser()
        } else {
            _state.value = _state.value.copy(
                isLoading = false,
                isSuccess = true
            )
        }
    }

    private fun saveNewGoogleUser() {
        val user = auth.currentUser ?: run {
            _state.value = _state.value.copy(
                isLoading = false,
                errorMessage = "Usuario no encontrado"
            )
            return
        }

        viewModelScope.launch {
            try {
                val uniqueNickname = generateUniqueNickname()

                val userData = hashMapOf(
                    "uid" to user.uid,
                    "nickname" to uniqueNickname,
                    "correo" to (user.email ?: ""),
                    "nivel" to 1,
                    "exp" to 0,
                    "logros" to emptyList<String>(),
                    "insignias" to emptyList<String>(),
                    "progresoCategorias" to mapOf(
                        "agua" to 0,
                        "biologia" to 0,
                        "tierra" to 0,
                        "reciclaje" to 0
                    ),
                    "leccionesCompletadas" to emptyMap<String, Any>(),
                    "racha" to mapOf(
                        "actual" to 0,
                        "maxima" to 0,
                        "ultimoRegistro" to null
                    )
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

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Error al generar nickname: ${e.message}"
                )
            }
        }
    }

    private suspend fun generateUniqueNickname(): String {
        var nickname: String
        var exists: Boolean
        val usuariosRef = firestore.collection("usuarios")

        do {
            nickname = generateRandomEnvironmentalNickname()
            exists = try {
                val query = usuariosRef
                    .whereEqualTo("nickname", nickname)
                    .limit(1)

                !query.get().await().isEmpty
            } catch (e: Exception) {
                Log.e("FirestoreError", "Error al verificar nickname: ${e.message}")
                throw Exception("No se pudo verificar el nickname. Intenta nuevamente.")
            }
        } while (exists)

        return nickname
    }

    private fun generateRandomEnvironmentalNickname(): String {
        val environmentalPrefixes = listOf(
            "Eco", "Green", "Bio", "Nature", "Earth", "Pure",
            "Clean", "Solar", "Eco", "Leaf", "Rain", "Wind",
            "Ocean", "Forest", "River", "Recycle"
        )

        val environmentalSuffixes = listOf(
            "Hero", "Kid", "Guard", "Saver", "Friend", "Champ",
            "Love", "Life", "Star", "Power", "Walk", "Shine",
            "Bloom", "Spark", "Grow", "Pure"
        )

        val randomPrefix = environmentalPrefixes.random()
        val randomSuffix = environmentalSuffixes.random()

        // Calculamos cuántos caracteres tenemos disponibles para el número
        val totalLength = randomPrefix.length + randomSuffix.length
        val remainingChars = 13 - totalLength

        return if (remainingChars >= 3) {
            // Si tenemos espacio para al menos 3 dígitos
            val randomNumber = (100..999).random()
            "$randomPrefix$randomSuffix$randomNumber"
        } else if (remainingChars == 2) {
            // Si solo tenemos espacio para 2 dígitos
            val randomNumber = (10..99).random()
            "$randomPrefix$randomSuffix$randomNumber"
        } else if (remainingChars == 1) {
            // Si solo tenemos espacio para 1 dígito
            val randomNumber = (0..9).random()
            "$randomPrefix$randomSuffix$randomNumber"
        } else {
            // Si la combinación de prefijo + sufijo ya tiene 13+ caracteres
            // Tomamos solo el prefijo + sufijo y lo truncamos a 13 caracteres
            (randomPrefix + randomSuffix).take(13)
        }
    }

    private fun isValidUsername(username: String): Boolean {
        return username.length <= 13 && username.isNotBlank()
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