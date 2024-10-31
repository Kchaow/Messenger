package com.example.messenger.api.dto

import com.google.gson.annotations.SerializedName

class CreateChatRequest {
    @SerializedName("is_direct") var isDirect: Boolean = false
    @SerializedName("members_ids") var membersIds: List<Int> = ArrayList<Int>()
    var name: String = ""
}