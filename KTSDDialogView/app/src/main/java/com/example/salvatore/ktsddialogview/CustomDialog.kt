package com.example.salvatore.ktsddialogview

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import com.sysdata.kt.sddialog.SDDialogView
import kotlinx.android.synthetic.main.custom_dialog.view.*

/**
 * Created by Salvatore on 06/07/2018.
 */
class CustomDialog(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr), View.OnClickListener, SDDialogView.Compatible {

    private var parent: SDDialogView? = null

    init {
        context?.let {
            val inflater = it.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            inflater.inflate(R.layout.custom_dialog, this, true)
            close_btn.setOnClickListener(this)
            first_btn.setOnClickListener(this)
            second_btn.setOnClickListener(this)
        }
    }

    override fun onClick(v: View?) {
        v?.let {
            when (v) {
                close_btn -> parent?.closeDialog(0)
                first_btn -> Toast.makeText(context, "first", Toast.LENGTH_SHORT).show()
                second_btn -> Toast.makeText(context, "second", Toast.LENGTH_SHORT).show()
                else -> Log.i(this.javaClass.name, " view not handled ")
            }
        }
    }

    override fun onBindParentView(parent: SDDialogView) {
        this.parent = parent
    }

    override fun onUnbindParentView() {
        this.parent = null
    }

}