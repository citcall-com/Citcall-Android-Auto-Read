package com.citcall.citcallautoreaddemo


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.citcall.citcallautoreaddemo.http.APIInterface
import com.citcall.citcallautoreaddemo.http.RequestVerifiyModel
import com.citcall.citcallautoreaddemo.http.ResponseVerifyClass
import com.citcall.citcallautoreaddemo.http.ServiceBuilder
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtpActivity : AppCompatActivity() {

    private var trxid: String = ""
    private var firstToken: String = ""
    private var length: Int = 0
    private var verified = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_otp)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val filter = IntentFilter().apply {
            addAction("miscall")
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter)

        val textFirstToken = findViewById<TextView>(R.id.textFirstToken)

        trxid = intent.getStringExtra("trxid").toString()
        firstToken = intent.getStringExtra("token").toString()
        length = intent.getStringExtra("length").toString().toInt()

        textFirstToken.text = firstToken

        val btnVerify = findViewById<Button>(R.id.btnVerify)

        btnVerify.setOnClickListener{
            verifyOtp()
        }
    }

    private val receiver = object : BroadcastReceiver(){
        override fun onReceive(ctx: Context?, int: Intent?) {
            val fourDigit = int?.getStringExtra("last_four_digit").toString()
            Log.i("CITCALL", "Four Digits $fourDigit")
            setOtp(fourDigit)
        }
    }

    fun setOtp(fourDigit: String){
        val editTextOtp: TextInputEditText = findViewById(R.id.fourDigitsEditText)
        editTextOtp.setText(fourDigit)
        verifyOtp()
    }

    private fun verifyOtp() {
        Thread.sleep(500)
        val editTextOtp: TextInputEditText = findViewById(R.id.fourDigitsEditText)
        val otp = editTextOtp.editableText.toString()
        if (otp != "" && !verified) {
            val allCaller: String = firstToken + otp
            if (allCaller.length == length) {
                val retrofit = ServiceBuilder.buildService(APIInterface::class.java)

                val obj = RequestVerifiyModel(trxid, allCaller)

                retrofit.requestVerify(obj).enqueue(
                    object : Callback<ResponseVerifyClass>{
                        override fun onResponse(
                            call: Call<ResponseVerifyClass>,
                            response: Response<ResponseVerifyClass>
                        ) {
                            Log.d("TAG", "Response: ${response.body()}")
                            if (!response.body()!!.error) {
                                verified = true
                                moveToSuccess()
                            }
                        }

                        override fun onFailure(p0: Call<ResponseVerifyClass>, p1: Throwable) {
                            Log.e("TAG", "error $p1")
                        }
                    }
                )
            } else {
                showToast("Please fill otp with spesific length")
            }
        }
    }

    private fun showToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun moveToSuccess() {
        val intent = Intent(this, SuccessActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onPause() {
        super.onPause()
        try {
            unregisterReceiver(receiver)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
    val filter = IntentFilter().apply {
        addAction("miscall")
    }
    LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter)
    }
}
