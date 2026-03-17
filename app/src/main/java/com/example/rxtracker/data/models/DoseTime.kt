package com.example.rxtracker.data.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalTime

@Serializable
data class DoseTime(
    @Serializable(with = LocalTimeSerializer::class)
    val time: LocalTime,
    val quantity: Double = 1.0
)

object LocalTimeSerializer : KSerializer<LocalTime> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "LocalTime",
        PrimitiveKind.STRING
    )

    override fun serialize(
        encoder: Encoder,
        value: LocalTime
    ) = encoder.encodeString(value.toString())

    override fun deserialize(decoder: Decoder): LocalTime = LocalTime.parse(decoder.decodeString())
}