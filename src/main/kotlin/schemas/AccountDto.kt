package com.imarkoff.schemas

import com.imarkoff.schemas.enums.Gender
import com.imarkoff.schemas.enums.Role
import kotlinx.serialization.Serializable

@Serializable
data class AccountDto(
    val id: String,
    val email: String,
    val role: Role,
    val firstName: String,
    val lastName: String?
)

interface IAccountBase {
    val account: AccountDto
    val phoneNumber: String
    val birthDate: String // YYYY-MM-DD
    val pronoun: Gender? // will be orphaned and replaced by gender with normal enums soon
    val profilePictureId: String? // uuid
}