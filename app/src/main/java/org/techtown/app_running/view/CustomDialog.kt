package org.techtown.app_running.view

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.techtown.app_running.R
import org.techtown.app_running.databinding.FragmentPaswordDialogBinding

class CustomDialog(context: Context) : View.OnClickListener {
    private val TAG: String = "CustomDialog 로그"
    private val dialog = Dialog(context)

    private lateinit var cancel: ImageView
    private lateinit var send: Button
    private lateinit var email: EditText
    private lateinit var auth: FirebaseAuth


    fun showDialog() {

        dialog.apply {
            setContentView(R.layout.fragment_pasword_dialog)
            setCanceledOnTouchOutside(false)
            setCancelable(true)
//            window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
            show()
        }

        cancel = dialog.findViewById(R.id.cancel)
        send = dialog.findViewById(R.id.send)
        email = dialog.findViewById(R.id.email)

        cancel.setOnClickListener(this)
        send.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            cancel.id -> {
                dialog.dismiss()
            }
            send.id -> {
                var stremail = email.text.toString()

                if (stremail != null)
                    Log.d(TAG, "onClick: str = ${stremail}")

                auth = Firebase.auth
                auth.sendPasswordResetEmail(stremail).addOnCompleteListener{task ->
                    if(task.isSuccessful){
                        Toast.makeText(p0.context,"메일을 보내드렸습니다.",Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "onClick: SendEmail is success.")
                        dialog.dismiss()
                }else {
                        Toast.makeText(p0.context,"가입되어 있지 않은 이메일 입니다.",Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "onClick: SendEmail is fail.")
                }
            }
        }
    }
//
}
}


























