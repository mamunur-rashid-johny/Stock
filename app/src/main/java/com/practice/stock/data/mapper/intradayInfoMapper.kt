package com.practice.stock.data.mapper

import com.practice.stock.data.remote.dto.IntradayInfoDto
import com.practice.stock.domain.model.IntradayInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


fun IntradayInfoDto.toIntradayInfo(): IntradayInfo {
    val pattern = "yyyy-MM-dd HH:mm:ss"
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    val localDateTime = LocalDateTime.parse(timeStamp,formatter)
    return IntradayInfo(
        date = localDateTime,
        close = close
    )

}