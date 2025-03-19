package com.imarkoff.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

interface NumberEnum {
    val value: Int
}

/**
 * Serializer for int enums.
 * @param T The enum type.
 * @param enumValues The values of the enum.
 * @param serialName The name of the enum.
 * то не я то копілот
 */
abstract class NumberEnumSerializer<T>(
    private val enumValues: Array<T>,
    private val serialName: String
) : KSerializer<T> where T : Enum<T>, T : NumberEnum {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(serialName, PrimitiveKind.INT)

    override fun serialize(encoder: Encoder, value: T) {
        encoder.encodeInt(value.value)
    }

    override fun deserialize(decoder: Decoder): T {
        val value = decoder.decodeInt()
        return enumValues.first { (it as NumberEnum).value == value }
    }
}