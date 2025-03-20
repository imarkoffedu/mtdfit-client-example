package com.imarkoff.schemas

import com.imarkoff.schemas.enums.ComplexityLevel
import com.imarkoff.utils.DateString
import kotlinx.serialization.Serializable

@Serializable
data class TrainingPlanShortDto(
    val id: String, // uuid
    val name: String,
    val description: String?,
    val complexity: ComplexityLevel?,
    val dateCreated: DateString,
    val dateModified: DateString?
)
