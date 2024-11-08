package com.citcall.citcallautoreaddemo

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.citcall.citcallautoreaddemo.helper.PermissionHelper
import com.citcall.citcallautoreaddemo.http.APIInterface
import com.citcall.citcallautoreaddemo.http.RequestOTPModel
import com.citcall.citcallautoreaddemo.http.ResponseOTPClass
import com.citcall.citcallautoreaddemo.http.ServiceBuilder
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PhoneNumberActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_phone_number)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnReqOTP = findViewById<Button>(R.id.btnReqOTP)

        val progressbar = findViewById<ProgressBar>(R.id.phoneNumberProgressBar)

        btnReqOTP.setOnClickListener{
            btnReqOTP.visibility = View.INVISIBLE
            progressbar.visibility = View.VISIBLE
            if (!PermissionHelper.hasReadPhoneStatePermission(this)){
                showDialogPermission()
            } else {
                requestOtp()
            }
        }
    }

    private fun showDialogPermission(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.request_call_log_title))
            .setMessage(getString(R.string.request_call_log_description))
            .setPositiveButton(getString(R.string.yes)){ _, _ ->
                PermissionHelper.requestReadPhoneStatePermission(this)
            }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermissionHelper.READ_PHONE_STATE_PERMISSION_REQ_CODE -> {
                if (!PermissionHelper.hasReadPhoneStatePermission(this)){
                    val mainIntent = Intent(this, NoPermissionActivity::class.java)
                    startActivity(mainIntent)
                    finish()
                } else {
                    Thread.sleep(1000)
                    requestOtp()
                }
            }
        }
    }

    private fun requestOtp(){
        val editTextPhone: TextInputEditText = findViewById(R.id.editTextPhone)
        val phone = editTextPhone.editableText.toString()

        val retrofit = ServiceBuilder.buildService(APIInterface::class.java)

        val obj = RequestOTPModel(phone,1)
        retrofit.requestOTP(obj).enqueue(
            object :Callback<ResponseOTPClass>{
                override fun onResponse(call: Call<ResponseOTPClass>, response: Response<ResponseOTPClass>) {
                    Log.d("TAG", "Response: ${response.body()}")
                    moveToOtp(
                        response.body()!!.trxid,
                        response.body()!!.first_token,
                        response.body()!!.length
                    )
                }

                override fun onFailure(p0: Call<ResponseOTPClass>, p1: Throwable) {
                    Log.e("TAG", "error $p1")
                }
            }
        )
    }

    fun moveToOtp(trxid: String,firstToken: String, length: Int){
        val otpIntent = Intent(this@PhoneNumberActivity, OtpActivity::class.java)
        otpIntent.putExtra("trxid", trxid)
        otpIntent.putExtra("token", firstToken)
        otpIntent.putExtra("length", length.toString())
        startActivity(otpIntent)
        finish()
    }
}

