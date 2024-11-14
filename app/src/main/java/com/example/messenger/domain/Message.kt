package com.example.messenger.domain

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class Message {
    @SerializedName(value = "SenderId", alternate = ["senderId"]) var senderId: String = ""
    @SerializedName(value = "SendingTime", alternate = ["sendingTime"]) var sendingTime: String = ""
    @SerializedName(value = "Text", alternate = ["text"]) var text: String = ""
    @SerializedName(value = "UserName", alternate = ["userName"]) var userName: String = ""

    fun getSendingTimeAsLocalDateTime(): LocalDateTime {
        val formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
        val zonedDateTime = ZonedDateTime.parse(sendingTime, formatter)
        return zonedDateTime.toLocalDateTime()
    }
}