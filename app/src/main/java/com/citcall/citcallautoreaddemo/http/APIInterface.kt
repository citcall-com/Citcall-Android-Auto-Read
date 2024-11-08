package com.citcall.citcallautoreaddemo.http

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface APIInterface {

    @POST("mrequest.php")
    fun requestOTP(@Body requestOTPModel: RequestOTPModel): Call<ResponseOTPClass>

    @POST("mval.php")
    fun requestVerify(@Body requestVerifiyModel: RequestVerifiyModel): Call<ResponseVerifyClass>
}
