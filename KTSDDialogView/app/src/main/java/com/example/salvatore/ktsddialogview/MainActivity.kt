package com.example.salvatore.ktsddialogview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.BounceInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.ScaleAnimation
import android.widget.Toast
import com.sysdata.kt.sddialog.OnDialogCloseListener
import com.sysdata.kt.sddialog.SDDialogView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        show_dialog_button.setOnClickListener {
            val customDialog = CustomDialog(this)
            val listener: OnDialogCloseListener = object : OnDialogCloseListener {
                override fun onClose(requestCode: Int, resultCode: Int) {
                    Toast.makeText(this@MainActivity, "close with $resultCode", Toast.LENGTH_SHORT).show()
                }
            }
            val fadeIn = ScaleAnimation(1f, 1f,0f, 1f)
            fadeIn.interpolator = BounceInterpolator()
            fadeIn.duration = 600
            val sdDialogView = SDDialogView.Builder().with(this)
                    .contentView(customDialog)
                    .onCloseListener(listener)
                    .cancelable(true)
                    .requestCode(1)
                    .enterAnimation(fadeIn)
                    .build()
            sdDialogView.showDialog(main_container)
        }
    }
}
