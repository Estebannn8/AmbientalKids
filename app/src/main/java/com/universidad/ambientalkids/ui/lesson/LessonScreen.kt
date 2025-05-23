package com.universidad.ambientalkids.ui.lesson

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.universidad.ambientalkids.R
import com.universidad.ambientalkids.navigation.AppScreens
import com.universidad.ambientalkids.ui.components.CustomButton
import com.universidad.ambientalkids.ui.components.LoadingOverlay
import com.universidad.ambientalkids.data.model.ActivityType
import com.universidad.ambientalkids.events.LessonEvent
import com.universidad.ambientalkids.state.LessonState
import com.universidad.ambientalkids.ui.components.lesson.AllLessonsCompletedScreen
import com.universidad.ambientalkids.ui.components.lesson.ErrorScreen
import com.universidad.ambientalkids.ui.components.lesson.FeedbackOverlay
import com.universidad.ambientalkids.ui.components.lesson.LessonCompleteScreen
import com.universidad.ambientalkids.ui.components.lesson.LessonLockedScreen
import com.universidad.ambientalkids.ui.lesson.activities.DragPairsActivity
import com.universidad.ambientalkids.ui.lesson.activities.FillBlankActivity
import com.universidad.ambientalkids.ui.lesson.activities.MatchingActivity
import com.universidad.ambientalkids.ui.lesson.activities.MultipleChoiceActivity
import com.universidad.ambientalkids.ui.lesson.activities.SentenceBuilderActivity
import com.universidad.ambientalkids.ui.lesson.activities.TeachingActivity
import com.universidad.ambientalkids.viewmodel.LessonsViewModel
import com.universidad.ambientalkids.viewmodel.LessonsViewModel.LoadingState
import com.universidad.ambientalkids.viewmodel.UserViewModel

@Composable
fun LessonScreen(
    category: String,
    userViewModel: UserViewModel,
    lessonsViewModel: LessonsViewModel,
    navController: NavController
) {
    val userState by userViewModel.state.collectAsState()
    val lessonState by lessonsViewModel.state.collectAsState()
    val loadingState by lessonsViewModel.loadingState.collectAsState()

    // Efecto para cargar lecciones
    LaunchedEffect(category, userState.userData.leccionesCompletadas) {
        val completedLessons = userState.userData.leccionesCompletadas ?: emptyMap()
        lessonsViewModel.sendEvent(
            LessonEvent.LoadLessonAndInitialize(
                categoryId = category,
                completedLessons = completedLessons
            )
        )
    }

    LaunchedEffect(lessonsViewModel.state) {
        lessonsViewModel.state.collect { state ->
            if (state.currentLesson == null &&
                !state.isLoading &&
                lessonsViewModel.loadingState.value == LoadingState.Idle) {
                navController.popBackStack()
            }
        }
    }

    when {
        // 1. Estado de carga
        loadingState is LoadingState.LoadingLessons || lessonState.isLoading -> {
            LoadingOverlay()
        }

        // 2. Estado de error (usando when con asignación)
        loadingState.let { it is LoadingState.LessonsLoaded && !it.success } -> {
            ErrorScreen(
                message = "Error al cargar las lecciones",
                onRetry = {
                    userState.userData.leccionesCompletadas?.let { completed ->
                        lessonsViewModel.sendEvent(
                            LessonEvent.LoadLessonAndInitialize(category, completed)
                        )
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        // 3. Lección cargada y disponible
        lessonState.currentLesson != null && lessonState.currentActivity != null -> {
            LessonContentScreen(
                lessonState = lessonState,
                onEvent = lessonsViewModel::sendEvent,
                navController = navController,
                userViewModel = userViewModel,
                category = category
            )
        }

        // 4. Todas las lecciones completadas (usando let para el smart cast)
        loadingState.let { it is LoadingState.LessonsLoaded } &&
                lessonState.currentLesson == null -> {
            // Verificar si todas las lecciones de esta categoría están completadas
            val categoryCompletedLessons = userState.userData.leccionesCompletadas?.get(category) as? Map<*, *>
            val allLessonsInCategory = lessonsViewModel.allLessons
            val allCompleted = allLessonsInCategory.isNotEmpty() &&
                    allLessonsInCategory.all { lesson ->
                        categoryCompletedLessons?.containsKey(lesson.id) == true
                    }

            if (allCompleted) {
                AllLessonsCompletedScreen(
                    onBack = { navController.popBackStack() }
                )
            } else {
                // Estado inesperado, recargar
                LaunchedEffect(Unit) {
                    lessonsViewModel.sendEvent(
                        LessonEvent.LoadLessonAndInitialize(
                            category,
                            userState.userData.leccionesCompletadas ?: emptyMap()
                        )
                    )
                }
            }
        }

        // 5. Estado inesperado (recargar)
        else -> {
            LoadingOverlay()
            LaunchedEffect(Unit) {
                userState.userData.leccionesCompletadas?.let { completed ->
                    lessonsViewModel.sendEvent(
                        LessonEvent.LoadLessonAndInitialize(category, completed)
                    )
                }
            }
        }
    }
}

@Composable
fun LessonContentScreen(
    lessonState: LessonState,
    onEvent: (LessonEvent) -> Unit,
    navController: NavController,
    userViewModel: UserViewModel,
    category: String
) {

    // Obtener el tema visual para la categoría actual
    val temaVisual = remember(category) {
        val tema = TemaVisualManager.obtenerTemaPorCategoria(category) ?: TemaVisualManager.obtenerTemaPorCategoria("agua")!!
        Log.d("TemaVisual", "Categoría seleccionada: $category")
        Log.d("TemaVisual", "Tema aplicado: ${tema.baseColor}, Icono: ${tema.categoryIcon}")
        tema
    }

    LaunchedEffect(lessonState.currentLesson) {
        if (lessonState.currentLesson == null) {
            navController.popBackStack()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // Fondo de pantalla
        Image(
            painter = painterResource(temaVisual.background),
            contentDescription = "Background",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize().alpha(0.7f)
        )

        when {
            lessonState.showCompleteScreen -> {
                LessonCompleteScreen(
                    exp = lessonState.earnedExp,
                    dinero = lessonState.earnedDinero,
                    onContinue = {
                        onEvent(LessonEvent.CompleteLesson)
                        userViewModel.markLessonAsCompleted(
                            lessonState.currentLesson?.id ?: "",
                            lessonState.earnedExp,
                            lessonState.earnedDinero
                        )
                        navController.popBackStack()
                        navController.navigate(AppScreens.HomeScreen.route)
                    },
                    temaVisual = temaVisual
                )
            }
            lessonState.isLessonLocked -> {
                LessonLockedScreen(
                    onRestart = { onEvent(LessonEvent.RestartLesson) },
                    onExit = { onEvent(LessonEvent.ExitLesson) },
                    temaVisual = temaVisual
                )
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent) // Cambiado a transparente
                ) {
                    LessonHeader(
                        progress = lessonState.progress,
                        lives = lessonState.lives,
                        onBackPressed = { onEvent(LessonEvent.ShowExitConfirmation) },
                        temaVisual = temaVisual
                    )

                    Box(modifier = Modifier.weight(1f)) {
                        lessonState.currentActivity?.let { activity ->
                            when (activity.type) {
                                ActivityType.Teaching -> {
                                    TeachingActivity(
                                        state = lessonState,
                                        onEvent = onEvent,
                                        temaVisual = temaVisual
                                    )
                                }
                                ActivityType.MultipleChoice -> {
                                    MultipleChoiceActivity(
                                        state = lessonState,
                                        onEvent = onEvent,
                                        temaVisual = temaVisual
                                    )
                                }
                                ActivityType.FillBlank -> {
                                    FillBlankActivity(
                                        state = lessonState,
                                        onEvent = onEvent,
                                        temaVisual = temaVisual
                                    )
                                }
                                ActivityType.Matching -> {
                                    MatchingActivity(
                                        state = lessonState,
                                        onEvent = onEvent,
                                        temaVisual = temaVisual
                                    )
                                }
                                ActivityType.DragPairs -> {
                                    DragPairsActivity(
                                        state = lessonState,
                                        onEvent = onEvent,
                                        temaVisual = temaVisual
                                    )
                                }
                                ActivityType.SentenceBuilder -> {
                                    SentenceBuilderActivity(
                                        state = lessonState,
                                        onEvent = onEvent,
                                        temaVisual = temaVisual
                                    )
                                }
                            }
                        }
                    }

                    if (lessonState.showFeedback) {
                        FeedbackOverlay(
                            isCorrect = lessonState.lastAnswerCorrect ?: false,
                            feedbackText = lessonState.feedbackText,
                            onDismiss = {
                                onEvent(LessonEvent.HideFeedback)

                                if (lessonState.lastAnswerCorrect == true) {
                                    if (lessonState.isLastActivityInLesson) {
                                        onEvent(LessonEvent.ShowCompleteScreen)
                                    } else {
                                        onEvent(LessonEvent.MoveToNextActivity)
                                    }
                                } else {
                                    onEvent(LessonEvent.ResetCurrentActivity)
                                }
                            }
                        )
                    }

                    if (lessonState.showExitConfirmation) {
                        AlertDialog(
                            onDismissRequest = { onEvent(LessonEvent.HideExitConfirmation) },
                            title = { Text("¿Seguro que quieres salir?") },
                            text = { Text("Si sales ahora perderás el progreso de esta lección.") },
                            confirmButton = {
                                TextButton(onClick = {
                                    onEvent(LessonEvent.HideExitConfirmation)
                                    onEvent(LessonEvent.ExitLesson)
                                }) {
                                    Text("Salir")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = {
                                    onEvent(LessonEvent.HideExitConfirmation)
                                }) {
                                    Text("Continuar")
                                }
                            }
                        )
                    }

                    BottomSection(
                        onContinue = { onEvent(LessonEvent.ContinueActivity) },
                        temaVisual = temaVisual
                    )
                }
            }
        }
    }
}

@Composable
fun LessonHeader(
    progress: Float,
    lives: Int,
    onBackPressed: () -> Unit,
    temaVisual: TemaVisual
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(80.dp)
    ) {
        // Boton Salir
        Box(
            modifier = Modifier
                .size(40.dp)
                .padding(bottom = 10.dp, start = 10.dp)
                .align(Alignment.CenterStart)
                .clickable(onClick = { onBackPressed() })
        ) {
            Image(
                painter = painterResource(temaVisual.CloseIcon),
                contentDescription = "Flecha atras",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        // Barra de progreso
        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(32.dp)
                .align(Alignment.Center)
        ) {
            Image(
                painter = painterResource(temaVisual.progressBar),
                contentDescription = "Barra de progreso",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )

            // Progreso actual
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(19.dp)
                    .padding(end = 11.dp)
                    .zIndex(3f)
                    .offset(y = 3.9.dp, x = 5.2.dp)
                    .clip(RoundedCornerShape(
                        topEnd = 8.dp,
                        bottomEnd = 8.dp,
                        topStart = 8.dp,
                        bottomStart = 8.dp
                    ))
                    .background(temaVisual.progressColor)
            )
        }

        // Vidas
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(y = (-3).dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_full_heart),
                contentDescription = "Vidas",
                modifier = Modifier.size(22.dp)
            )
            Text(
                text = "x$lives",
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

@Composable
fun BottomSection(
    onContinue: () -> Unit,
    temaVisual: TemaVisual
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(80.dp)
    ){
        CustomButton(
            modifier = Modifier.align(Alignment.Center),
            buttonText = "CONTINUAR",
            gradientLight = temaVisual.gradientLight,
            gradientDark = temaVisual.gradientDark,
            baseColor = temaVisual.baseColor,
            onClick = onContinue
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LessonHeaderPreview() {
    // Creamos un tema visual de ejemplo para la previsualización
    val previewTheme = TemaVisual(
        baseColor = Color(0xFF4285F4),
        gradientLight = Color(0xFF34A853),
        gradientDark = Color(0xFF4285F4),
        progressColor = Color(0xFFFBBC05),
        progressBar = R.drawable.ic_bar_azul,
        CloseIcon = R.drawable.ic_close_azul,
        categoryIcon = R.drawable.zorro_inicio,
        background = R.drawable.background_agua,
        categoryIconFeliz = R.drawable.zorro_feliz,
        categoryIconTriste = R.drawable.zorro_triste
    )

    LessonHeader(
        progress = 1f, // 65% de progreso
        lives = 3,        // 3 vidas
        onBackPressed = { /* Acción al presionar el botón de retroceso */ },
        temaVisual = previewTheme
    )
}
