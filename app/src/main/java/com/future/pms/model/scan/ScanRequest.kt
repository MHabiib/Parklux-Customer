package com.future.pms.model.scan

import com.google.gson.annotations.SerializedName

data class ScanRequest(@SerializedName("idSlot") val idSlot: String)