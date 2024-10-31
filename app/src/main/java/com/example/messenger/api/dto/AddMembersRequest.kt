package com.example.messenger.api.dto

import com.google.gson.annotations.SerializedName

class AddMembersRequest {
    @SerializedName("chat_id") var chatId: Int = -1
    @SerializedName("members_ids") var membersIds: List<Int> = ArrayList<Int>()
}