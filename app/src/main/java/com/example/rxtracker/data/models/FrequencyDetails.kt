package com.example.rxtracker.data.models

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonClassDiscriminator
import java.time.DayOfWeek

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("type")
sealed class FrequencyDetails {
    @Serializable
    @SerialName("OnceDaily")
    object OnceDaily : FrequencyDetails()

    @Serializable
    @SerialName("AsNeeded")
    object AsNeeded : FrequencyDetails()

    @Serializable
    @SerialName("MultipleTimes")
    data class MultipleTimes(val timesPerDay: Int) : FrequencyDetails()

    @Serializable
    @SerialName("EveryXHours")
    data class EveryXHours(val hours: Int) : FrequencyDetails()

    @Serializable
    @SerialName("EveryXDays")
    data class EveryXDays(val days: Int) : FrequencyDetails()

    @Serializable
    @SerialName("SpecificWeekdays")
    data class SpecificWeekdays(val days: Set<@Serializable(with = DayOfWeekSerializer::class) DayOfWeek>) :
        FrequencyDetails()

    @Serializable
    @SerialName("Cyclic")
    data class Cyclic(val intakeDays: Int, val pauseDays: Int) : FrequencyDetails()

}

object DayOfWeekSerializer : KSerializer<DayOfWeek> {
    override val descriptor = PrimitiveSerialDescriptor("DayOfWeek", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: DayOfWeek
    ) = encoder.encodeString(value.name)

    override fun deserialize(decoder: Decoder) = DayOfWeek.valueOf(decoder.decodeString())
}