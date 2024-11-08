package com.citcall.citcallautoreaddemo.http

data class RequestOTPModel(
    val msisdn: String,
    val retry: Int
)
