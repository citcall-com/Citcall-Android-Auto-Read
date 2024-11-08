package com.citcall.citcallautoreaddemo.http

data class ResponseOTPClass(
    val error: Boolean,
    val trxid: String,
    val first_token: String,
    val length: Int
)
