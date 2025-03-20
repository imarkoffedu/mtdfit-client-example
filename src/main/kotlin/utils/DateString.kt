package com.imarkoff.utils

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

typealias DateString = String

/**
 * Parses a string to a LocalDateTime object in UTC.
 */
fun DateString.parseUTC(
    formatter: DateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME
): LocalDateTime = LocalDateTime
    .parse(this, formatter)
    .atZone(ZoneOffset.UTC)
    .withZoneSameInstant(ZoneId.systemDefault())
    .toLocalDateTime()