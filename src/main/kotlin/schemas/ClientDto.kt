package com.imarkoff.schemas

import com.imarkoff.schemas.enums.Gender
import com.imarkoff.schemas.enums.HealthState
import com.imarkoff.schemas.enums.PhysicalActivityLevel
import com.imarkoff.schemas.enums.TrainingExperienceLevel
import com.imarkoff.schemas.enums.TrainingGoal
import kotlinx.serialization.Serializable

@Serializable
data class ClientDto (
    val id: String,  // uuid v4 a.k.a. guid
    val height: Int,
    val coach: String?, // uuid
    val activePeriodId: String?, // uuid
    val physicalActivityLevel: PhysicalActivityLevel,
    val trainingExperienceLevel: TrainingExperienceLevel,
    val goal: TrainingGoal,
    val healthState: HealthState,
    val healthDetails: String?,
    val note: String?,
    val trainingPlan: TrainingPlanShortDto?,
    val activeWorkoutSession: String?, // uuid
    val dateAssigned: String, // ISO 8601
    val dateModified: String?, // ISO 8601

    override val account: AccountDto,
    override val phoneNumber: String,
    override val birthDate: String,
    override val pronoun: Gender?,
    override val profilePictureId: String?
) : IAccountBase