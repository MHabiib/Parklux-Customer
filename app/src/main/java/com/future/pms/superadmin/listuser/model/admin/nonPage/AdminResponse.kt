package com.future.pms.superadmin.listuser.model.admin.nonPage

import com.google.gson.annotations.SerializedName

data class AdminResponse(@SerializedName("body") val body: AdminDetail,
    @SerializedName("statusCode") val statusCode: String, @SerializedName("statusCodeValue")
    val statusCodeValue: Int)