package com.future.pms.model.history

import com.google.gson.annotations.SerializedName

data class HistoryResponse(@SerializedName("body") val body: History, @SerializedName("statusCode") val statusCode: String,
    @SerializedName("statusCodeValue") val statusCodeValue: Int)