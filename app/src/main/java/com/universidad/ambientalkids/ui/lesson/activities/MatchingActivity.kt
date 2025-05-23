package com.universidad.ambientalkids.ui.lesson.activities

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universidad.ambientalkids.data.model.MatchingPair
import com.universidad.ambientalkids.events.LessonEvent
import com.universidad.ambientalkids.state.LessonState
import com.universidad.ambientalkids.ui.lesson.TemaVisual

@Composable
fun MatchingActivity(
    state: LessonState,
    onEvent: (LessonEvent) -> Unit,
    temaVisual: TemaVisual
) {
    val activity = state.currentActivity ?: return

    val leftItems = remember { (activity.matchingPairs?.map { it.leftItem } ?: emptyList()).shuffled() }
    val rightItems = remember {
        activity.shuffledRightItems ?: (activity.matchingPairs?.map { it.rightItem } ?: emptyList()).shuffled()
    }

    val pastelColors = remember {
        listOf(
            Color(0xDFB39DDB).copy(alpha = 0.7f), Color(0xDF81C784).copy(alpha = 0.7f),
            Color(0xDF64B5F6).copy(alpha = 0.7f), Color(0xDFFFB74D).copy(alpha = 0.7f),
            Color(0xDFE57373).copy(alpha = 0.7f), Color(0xDF9575CD).copy(alpha = 0.7f),
            Color(0xDF4DB6AC).copy(alpha = 0.7f), Color(0xDFFFD54F).copy(alpha = 0.7f)
        )
    }

    val pairColors = remember { mutableStateOf(mapOf<MatchingPair, Color>()) }

    fun isItemMatched(item: String, isLeft: Boolean): Boolean {
        return if (isLeft) {
            state.matchedPairs.any { it.leftItem == item }
        } else {
            state.matchedPairs.any { it.rightItem == item }
        }
    }

    fun tryMatchingPair(left: String?, right: String?) {
        if (left != null && right != null) {
            if (!isItemMatched(left, true) && !isItemMatched(right, false)) {
                val newPair = MatchingPair(left, right)
                onEvent(LessonEvent.MatchPair(newPair))

                if (newPair !in pairColors.value) {
                    val availableColors = pastelColors.filter {
                        it !in pairColors.value.values
                    }
                    val randomColor = availableColors.randomOrNull() ?: pastelColors.random()
                    pairColors.value = pairColors.value + (newPair to randomColor)
                }

                onEvent(LessonEvent.SelectLeftItem(null))
                onEvent(LessonEvent.SelectRightItem(null))
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = activity.title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Top
        ) {
            // Columna izquierda
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                leftItems.forEach { item ->
                    MatchingItem(
                        text = item,
                        isSelected = state.selectedLeft == item,
                        isMatched = isItemMatched(item, true),
                        pairColor = state.matchedPairs.firstOrNull { it.leftItem == item }?.let {
                            pairColors.value[it]
                        },
                        onClick = {
                            if (!isItemMatched(item, true)) {
                                val newLeft = if (state.selectedLeft == item) null else item
                                onEvent(LessonEvent.SelectLeftItem(newLeft))

                                if (newLeft != null && state.selectedRight != null) {
                                    tryMatchingPair(newLeft, state.selectedRight)
                                }
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.width(32.dp))

            // Columna derecha
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                rightItems.forEach { item ->
                    MatchingItem(
                        text = item,
                        isSelected = state.selectedRight == item,
                        isMatched = isItemMatched(item, false),
                        pairColor = state.matchedPairs.firstOrNull { it.rightItem == item }?.let {
                            pairColors.value[it]
                        },
                        onClick = {
                            if (!isItemMatched(item, false)) {
                                val newRight = if (state.selectedRight == item) null else item
                                onEvent(LessonEvent.SelectRightItem(newRight))

                                if (newRight != null && state.selectedLeft != null) {
                                    tryMatchingPair(state.selectedLeft, newRight)
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MatchingItem(
    text: String,
    isSelected: Boolean,
    isMatched: Boolean,
    pairColor: Color?,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isMatched -> pairColor ?: Color(0xFF4CAF50)
        else -> Color.White.copy(alpha = 0.7f)
    }
    val textColor = if (isMatched) Color.White else Color.Black
    val borderColor = when {
        isSelected -> Color(0xFF1976D2)
        isMatched -> Color.Black.copy(alpha = 0.5f)
        else -> Color(0xFFBBBBBB)
    }
    val borderWidth = if (isSelected) 2.dp else 1.dp

    Surface(
        modifier = Modifier
            .width(160.dp)
            .height(60.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(borderWidth, borderColor),
        color = backgroundColor,
        shadowElevation = 2.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                fontSize = 16.sp,
                color = textColor,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}
