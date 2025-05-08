package com.universidad.finankids.ui

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.universidad.ambientalkids.R
import com.universidad.ambientalkids.navigation.navigateToLesson
import com.universidad.ambientalkids.navigation.navigateToScreen
import com.universidad.ambientalkids.ui.components.BottomMenu
import com.universidad.ambientalkids.ui.components.LoadingOverlay
import com.universidad.ambientalkids.ui.theme.AppTypography
import com.universidad.finankids.viewmodel.UserViewModel
import kotlin.math.abs

@Composable
fun HomeScreen(
    navController: NavController,
    userViewModel: UserViewModel
) {

    // Estados observables
    val userState by userViewModel.state.collectAsState()
    val currentSectionIndex = userState.currentSectionIndex

    // Verificar si los datos están cargados
    val isDataLoaded = remember(userState) {
        userState.userData.uid.isNotEmpty() && !userState.isLoading
    }

    // Mostrar loading si los datos no están listos
    if (!isDataLoaded) {
        LoadingOverlay()
    }

    val sections = listOf(
        Section(
            "biologia",
            Color(0x0A746474),
            Color(0xFFBEEDB2),
            Color(0xDC62D744),
            "lima",
            R.drawable.rana_inicio,
            R.drawable.texto_reciclaje,
            20.dp, 55.dp,
            R.drawable.ic_lima_arrow_left,
            R.drawable.ic_lima_arrow_right,
            R.drawable.ic_jugar_lima,
            R.drawable.ic_estrella_lima
        ),
        Section(
            "agua",
            Color(0x0A79AFD4),
            Color(0xFFBDD4E5),
            Color(0xDC0270BA),
            "azul",
            R.drawable.gota_inicio,
            R.drawable.texto_agua,
            20.dp, 55.dp,
            R.drawable.ic_azul_arrow_left,
            R.drawable.ic_azul_arrow_right,
            R.drawable.ic_jugar_azul,
            R.drawable.ic_estrella_azul
        ),
        Section(
            "tierra",
            Color(0x0AE4C78A),
            Color(0xFFFFBA88),
            Color(0xDCED7621),
            "naranja",
            R.drawable.conejo_inicio,
            R.drawable.texto_tierra,
            20.dp, 40.dp,
            R.drawable.ic_naranja_arrow_left,
            R.drawable.ic_naranja_arrow_right,
            R.drawable.ic_jugar_naranja,
            R.drawable.ic_estrella_naranja
        ),
        Section(
            "reciclaje",
            Color(0x0AB28F76),
            Color(0xFF89C5A7),
            Color(0xDC208B56),
            "verde",
            R.drawable.zorro_inicio,
            R.drawable.texto_biologia,
            25.dp, 55.dp,
            R.drawable.ic_verde_arrow_left,
            R.drawable.ic_verde_arrow_right,
            R.drawable.ic_jugar_verde,
            R.drawable.ic_estrella_verde
        )
    )

    var selectedItem by remember { mutableStateOf("inicio") }
    val section = sections[currentSectionIndex]

    val sectionScore = when (section.name) {
        "agua" -> userState.userData.progresoCategorias["agua"] ?: 0
        "biologia" -> userState.userData.progresoCategorias["biologia"] ?: 0
        "tierra" -> userState.userData.progresoCategorias["tierra"] ?: 0
        "reciclaje" -> userState.userData.progresoCategorias["reciclaje"] ?: 0
        else -> 0
    }


    // Log de estado
    LaunchedEffect(userState) {
        Log.d("HomeScreen", "Estado actual del usuario: $userState")
    }

    // Efecto para loguear cambios en el índice de sección
    LaunchedEffect(currentSectionIndex) {
        Log.d("SectionDebug", "Current section index changed to: $currentSectionIndex")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(section.backgroundColor)
            .padding(WindowInsets.statusBars.asPaddingValues()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        ) {
            // --- Avatar y Nivel ---
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(3f)
                    .padding(start = 16.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .zIndex(3f),
                    contentAlignment = Alignment.Center
                ) {

                    // Nivel
                    val levelIconRes = when (section.color) {
                        "azul" -> R.drawable.ic_nivel_azul
                        "lima" -> R.drawable.ic_nivel_lima
                        "naranja" -> R.drawable.ic_nivel_naranja
                        "verde" -> R.drawable.ic_nivel_verde
                        else -> R.drawable.ic_nivel_azul
                    }
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .align(Alignment.Center)
                            .offset(y = 11.5.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = levelIconRes),
                            contentDescription = "Icono de nivel",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                        Text(
                            text = "${userState.nivel}",  // <- Nivel
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // --- Barra de experiencia ---
            val expBarRes = when (section.color) {
                "azul" -> R.drawable.ic_bar_azul
                "lima" -> R.drawable.ic_bar_lima
                "naranja" -> R.drawable.ic_bar_naranja
                "verde" -> R.drawable.ic_bar_verde
                else -> R.drawable.ic_bar_azul
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(32.dp)
                    .align(Alignment.BottomStart)
                    .offset(x = 80.dp, y = (-30).dp)
                    .zIndex(1f)
            ) {
                // Fondo de la barra
                Image(
                    painter = painterResource(id = expBarRes),
                    contentDescription = "Barra de experiencia",
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(0f),
                    contentScale = ContentScale.FillBounds
                )

                // Progreso
                Box(
                    modifier = Modifier
                        .offset(y = 4.dp, x = 5.2.dp)
                        .clip(RoundedCornerShape(
                            topEnd = 8.dp,
                            bottomEnd = 8.dp,
                            topStart = 8.dp,
                            bottomStart = 8.dp
                        ))
                        .padding(end = 12.dp)
                        .fillMaxWidth(userState.levelProgress)
                        .height(18.9.dp)
                        .zIndex(1f)
                        .background(section.progressColor)

                )

                Text(
                    text = "${userState.expInCurrentLevel} / ${userState.expNeededToNextLevel} XP",
                    color = Color(0xFF636363),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .zIndex(3f)
                        .offset(y = (-2.8).dp, x = (-20).dp)
                )
            }

            // --- Nickname y Dinero ---
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .align(Alignment.BottomStart)
                    .offset(x = 88.dp, y = (-68).dp) // Justo encima de la barra
                    .zIndex(3f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppTypography.ItimStroke(
                    text = userState.userData.nickname,   // <- Nickname
                    strokeColor = Color.White,
                    fillColor = Color.White,
                    fontSize = 22.sp,
                    textAlign = TextAlign.Start,
                    lineHeight = 1.sp,
                    letterSpacing = 1.sp
                )


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 20.dp)
                ) {
                    Text(
                        text = "${userState.userData.dinero}", // Dinero actual
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Image(
                        painter = painterResource(id = R.drawable.ic_coin),
                        contentDescription = "Moneda",
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

        }


        // Contenido principal (sección media + puntuación)
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Sección Media
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    var buildingClicked by remember { mutableStateOf(false) }
                    val buildingScale by animateFloatAsState(
                        targetValue = if (buildingClicked) 1.05f else 1f,
                        animationSpec = tween(durationMillis = 200),
                        finishedListener = { buildingClicked = false }
                    )

                    Box(
                        modifier = Modifier
                            .size(500.dp)
                            .padding(16.dp)
                            .pointerInput(currentSectionIndex) {
                                val currentSection = sections[currentSectionIndex]

                                awaitEachGesture {
                                    val down = awaitFirstDown()
                                    var isDrag = false
                                    var dragDistance = 0f
                                    var isGestureHandled = false

                                    val dragThreshold = 50f

                                    while (true) {
                                        val event = awaitPointerEvent()
                                        val drag = event.changes.firstOrNull()

                                        if (drag != null && drag.pressed) {
                                            dragDistance = drag.position.x - down.position.x
                                            if (abs(dragDistance) > 10f) {
                                                isDrag = true
                                            }
                                        } else {
                                            if (!isGestureHandled) {
                                                isGestureHandled = true

                                                if (isDrag && abs(dragDistance) > dragThreshold) {
                                                    if (dragDistance > 0) {
                                                        userViewModel.setCurrentSection((currentSectionIndex - 1 + sections.size) % sections.size)
                                                    } else {
                                                        userViewModel.setCurrentSection((currentSectionIndex + 1) % sections.size)
                                                    }
                                                } else if (!isDrag) {
                                                    buildingClicked = true
                                                    Log.d("Building", "Tap en ${currentSection.name}")
                                                }
                                            }
                                            break
                                        }
                                    }
                                }
                            }
                    ) {
                        Image(
                            painter = painterResource(id = section.buildingImage),
                            contentDescription = "Edificio de ${section.name}",
                            modifier = Modifier
                                .fillMaxSize()
                                .scale(buildingScale),
                            contentScale = ContentScale.Fit
                        )

                        // Flecha izquierda
                        var leftArrowClicked by remember { mutableStateOf(false) }
                        val interactionSource2 = remember { MutableInteractionSource() }
                        val leftArrowScale by animateFloatAsState(
                            targetValue = if (leftArrowClicked) 1.2f else 1f,
                            animationSpec = tween(durationMillis = 200),
                            finishedListener = { leftArrowClicked = false }
                        )
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .padding(bottom = 10.dp, start = 10.dp)
                                .align(Alignment.BottomStart)
                                .clickable (
                                    interactionSource = interactionSource2,
                                    indication = null // <-- esto elimina el sombreado
                                ) {
                                    leftArrowClicked = true
                                    userViewModel.setCurrentSection((currentSectionIndex - 1 + sections.size) % sections.size)
                                }
                        ) {
                            Image(
                                painter = painterResource(id = section.leftArrowIcon),
                                contentDescription = "Flecha izquierda",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .scale(leftArrowScale),
                                contentScale = ContentScale.Fit
                            )
                        }

                        // Texto Seccion
                        Image(
                            painter = painterResource(id = section.textImage),
                            contentDescription = "Texto de ${section.name}",
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = section.textBottomPadding)
                                .size(width = 300.dp, height = section.textHeight),
                            contentScale = ContentScale.Fit
                        )


                        // Flecha derecha
                        var rightArrowClicked by remember { mutableStateOf(false) }
                        val interactionSource = remember { MutableInteractionSource() }
                        val rightArrowScale by animateFloatAsState(
                            targetValue = if (rightArrowClicked) 1.2f else 1f,
                            animationSpec = tween(durationMillis = 200),
                            finishedListener = { rightArrowClicked = false }
                        )
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .padding(bottom = 10.dp, end = 10.dp)
                                .align(Alignment.BottomEnd)
                                .clickable (
                                    interactionSource = interactionSource,
                                    indication = null // <-- esto elimina el sombreado
                                ) {
                                    rightArrowClicked = true
                                    userViewModel.setCurrentSection((currentSectionIndex + 1) % sections.size)
                                }
                        ) {
                            Image(
                                painter = painterResource(id = section.rightArrowIcon),
                                contentDescription = "Flecha derecha",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .scale(rightArrowScale),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                }

                // Boton jugar y Puntuacion
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(bottom = 20.dp)
                ) {
                    // Puntuación y estrella
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 10.dp)
                    ) {

                        // Puntuacion
                        Text(
                            text = "$sectionScore",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(end = 8.dp).offset(y = 2.dp)
                        )

                        Image(
                            painter = painterResource(id = section.starIcon),
                            contentDescription = "Estrella",
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    // Botón de jugar
                    var playButtonClicked by remember { mutableStateOf(false) }
                    val playButtonScale by animateFloatAsState(
                        targetValue = if (playButtonClicked) 1.1f else 1f,
                        animationSpec = tween(durationMillis = 200),
                        finishedListener = { playButtonClicked = false }
                    )

                    Image(
                        painter = painterResource(id = section.playButtonIcon),
                        contentDescription = "Jugar",
                        modifier = Modifier
                            .scale(playButtonScale)
                            .clickable {
                                playButtonClicked = true
                                val categoryId = when(section.name) {
                                    "agua" -> "agua"
                                    "biologia" -> "biologia"
                                    "tierra" -> "tierra"
                                    "reciclaje" -> "reciclaje"
                                    else -> "agua"
                                }
                                navigateToLesson(navController, categoryId)
                            },
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }

        // Bottom Menu
        BottomMenu(
            isHomeSection = true,
            sectionColor = section.color,
            menuBackgroundColor = section.menuBackgroundColor,
            selectedItem = selectedItem,
            onItemSelected = { item ->
                selectedItem = item
                navigateToScreen(navController, item)
            }
        )

        Spacer(modifier = Modifier.height(10.dp))
    }
}

data class Section(
    val name: String,
    val backgroundColor: Color,
    val menuBackgroundColor: Color,
    val progressColor: Color,
    val color: String,
    val buildingImage: Int,
    val textImage: Int,
    val textBottomPadding: Dp,
    val textHeight: Dp,
    val leftArrowIcon: Int,
    val rightArrowIcon: Int,
    val playButtonIcon: Int,
    val starIcon: Int
)

