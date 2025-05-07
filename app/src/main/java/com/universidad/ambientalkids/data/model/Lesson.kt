package com.universidad.finankids.data.model

import com.universidad.ambientalkids.data.model.ActivityContent

data class Lesson(
    val id: String = "",
    val title: String = "", // lecciones
    val order: Int = 0,
    val baseExp: Int = 100,
    val baseDinero: Int = 500,
    val lives: Int = 5,
    val activities: List<ActivityContent> = emptyList()
)