package com.citcall.citcallautoreaddemo
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.citcall.citcallautoreaddemo.helper.PermissionHelper
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Thread.sleep(1000)
        installSplashScreen()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnBegin = findViewById<Button>(R.id.btnBegin)

        btnBegin.setOnClickListener{
            if (!PermissionHelper.hasReadCallLogPermission(this)) {
                showAlertDialog()
            } else {
                moveToPhoneNumber()
            }
        }

    }

    private fun showAlertDialog() {
//        lateinit var editTextPhone: TextInputEditText
//
//        editTextPhone = findViewById(R.id.editTextPhone)
//
//        val phone = editTextPhone.editableText.toString()
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.request_call_log_title))
            .setMessage(getString(R.string.request_call_log_description))
            .setPositiveButton(getString(R.string.yes)){ dialog, which ->
                if (!PermissionHelper.hasReadCallLogPermission(this)) {
                    PermissionHelper.requestReadCallLogPermission(this)
                } else {
                    moveToPhoneNumber()
                }
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
            PermissionHelper.READ_CALL_LOG_PERMISSION_REQ_CODE -> {
                if (!PermissionHelper.hasReadCallLogPermission(this)){
                    val mainIntent = Intent(this, NoPermissionActivity::class.java)
                    startActivity(mainIntent)
                    finish()
                } else {
                    moveToPhoneNumber()
                }
            }
        }
    }

    private fun moveToPhoneNumber(){
        val phoneIntent = Intent(this, PhoneNumberActivity::class.java)
        startActivity(phoneIntent)
        finish()
    }
}
