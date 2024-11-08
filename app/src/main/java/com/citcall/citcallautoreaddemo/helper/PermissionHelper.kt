package com.citcall.citcallautoreaddemo.helper

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionHelper{
    private val READ_CALL_LOG_PERMISSION = arrayOf(Manifest.permission.READ_CALL_LOG)
    val READ_CALL_LOG_PERMISSION_REQ_CODE = 0

    private val READ_PHONE_STATE_PERMISSION = arrayOf(Manifest.permission.READ_PHONE_STATE)
    val READ_PHONE_STATE_PERMISSION_REQ_CODE = 1

    fun hasReadCallLogPermission(activity: Activity): Boolean{
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED
    }

    fun hasReadPhoneStatePermission(activity: Activity): Boolean{
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
    }

    fun  requestReadCallLogPermission(activity: Activity?) {
        ActivityCompat.requestPermissions(activity!!, READ_CALL_LOG_PERMISSION, READ_CALL_LOG_PERMISSION_REQ_CODE)
    }

    fun  requestReadPhoneStatePermission(activity: Activity?) {
        ActivityCompat.requestPermissions(activity!!, READ_PHONE_STATE_PERMISSION, READ_PHONE_STATE_PERMISSION_REQ_CODE)
    }

    fun shouldShowRequestPermissionRationale(activity: Activity): Boolean{
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, READ_CALL_LOG_PERMISSION.get(0))
    }

    fun launchPermissionSettings(activity: Activity) {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = Uri.fromParts("package", activity.packageName, null)
        activity.startActivity(intent)
    }
}