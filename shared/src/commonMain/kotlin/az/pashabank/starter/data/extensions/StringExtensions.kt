@file:OptIn(FormatStringsInDatetimeFormats::class, ExperimentalTime::class)

package az.pashabank.starter.data.extensions

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

fun String.getDate(): LocalDateTime {
    return try {
        Instant.parse(this).toLocalDateTime(TimeZone.currentSystemDefault())
    } catch (_: Exception) {
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    }
}