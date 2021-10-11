package com.base.baselibrary.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope

object ActivityUtils {

    //deprecated version in AppCompatActivity but can use in activity
    fun Activity.openAppSetting(REQ: Int) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, REQ)
    }

    fun openAppSetting(
        activity: AppCompatActivity,
        resultLauncher: ActivityResultLauncher<Intent>
    ) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", activity.packageName, null)
        intent.data = uri
        resultLauncher.launch(intent)
    }

    fun AppCompatActivity.getResultLauncherForIntent(onResult: (result: ActivityResult) -> Unit): ActivityResultLauncher<Intent> {
        return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            onResult.invoke(result)
        }
    }

    fun AppCompatActivity.startIntentWithResult(
        intent: Intent,
        onResult: (result: ActivityResult) -> Unit
    ) {
        getResultLauncherForIntent { result ->
            onResult.invoke(result)
        }.launch(intent)
    }

    fun ActivityResult.isResultOK() = resultCode == Activity.RESULT_OK
    fun ActivityResult.isResultCanceled() = resultCode == Activity.RESULT_CANCELED
}