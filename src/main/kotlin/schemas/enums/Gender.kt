package com.imarkoff.schemas.enums

import com.imarkoff.utils.NumberEnum
import com.imarkoff.utils.NumberEnumSerializer
import kotlinx.serialization.Serializable

@Serializable(with = GenderSerializer::class)
enum class Gender(override val value: Int): NumberEnum {
    Male(0),
    Female(1)
}

object GenderSerializer : NumberEnumSerializer<Gender>(
    Gender.entries.toTypedArray(),
    "Gender"
)