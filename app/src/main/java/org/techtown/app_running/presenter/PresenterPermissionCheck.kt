package org.techtown.app_running.presenter

import android.app.AlertDialog
import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission

class PresenterPermissionCheck : AppCompatActivity() {

    fun checkPermission(mContext : Context) {

        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                Toast.makeText(mContext,"권한이 허용 되었습니다.",Toast.LENGTH_SHORT).show()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                AlertDialog.Builder(mContext)
                    .setMessage("권한 거절로 인해 다수의 기능이 제한됩니다.")
                    .setPositiveButton("권한 설정하러 가기") { dialog, which ->
                        try {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                .setData(Uri.parse("org.techtown.app_running.view.fragment"))
                            startActivity(intent)
                        } catch (e: ActivityNotFoundException) {
                            e.printStackTrace()
                            val intent = Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS)
                            startActivity(intent)
                        }
                    }
                    .show()
                Toast.makeText(mContext,"권한이 거부 되었습니다.",Toast.LENGTH_SHORT).show()
            }
        }

        TedPermission.with(mContext).apply {
            setPermissionListener(permissionListener)
            setRationaleMessage("정확한 위치 확인을 위해 \n권한을 허용해 주세요.")
            setDeniedMessage("권한을 거부하셨습니다. [앱 설정]->[권한] 항목에서 허용해주세요.")
            setPermissions(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            check()
        }
    }
}