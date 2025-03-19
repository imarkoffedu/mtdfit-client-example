package com.imarkoff.schemas.enums

import com.imarkoff.utils.NumberEnum
import com.imarkoff.utils.NumberEnumSerializer
import kotlinx.serialization.Serializable

/**
 * io.ktor.serialization.JsonConvertException: Illegal input: Unexpected JSON token at offset 714:
 * Expected quotation mark '"', but had '3' instead at path:
 */
@Serializable(with = ComplexityLevelSerializer::class)
enum class ComplexityLevel(override val value: Int) : NumberEnum {
    Beginner(1),
    Easy(2),
    Intermediate(3),
    Advanced(4),
    Expert(5)
}

object ComplexityLevelSerializer : NumberEnumSerializer<ComplexityLevel>(
    ComplexityLevel.entries.toTypedArray(),
    "ComplexityLevel"
)