package com.example.salvatore.sddialogview;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sysdata.sddialog.SDDialogView;

import java.lang.ref.WeakReference;

/**
 * Created by Salvatore on 28/06/2018.
 */

public class CustomDialog extends RelativeLayout implements View.OnClickListener, SDDialogView.Compatible {
    private SDDialogView parent;
    private Button mCloseBtn;
    private Button mFirstBtn;
    private Button mSecondBtn;

    public CustomDialog(Context context) {
        super(context);
        init(context, null);
    }

    public CustomDialog(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomDialog(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_dialog, this, true);
        initView(view);
    }

    private void initView(View view) {
        mCloseBtn = view.findViewById(R.id.close_btn);
        mFirstBtn = view.findViewById(R.id.first_btn);
        mSecondBtn = view.findViewById(R.id.second_btn);
        mCloseBtn.setOnClickListener(this);
        mFirstBtn.setOnClickListener(this);
        mSecondBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == mCloseBtn){
            parent.closeDialog(0);
        }
        else if(view == mFirstBtn){
            Toast.makeText(getContext(), "first", Toast.LENGTH_SHORT).show();
        }
        else if(view == mSecondBtn){
            Toast.makeText(getContext(), "second", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBindParentView(SDDialogView parent) {
        this.parent = parent;
    }

    @Override
    public void onUnbindParentView() {
        this.parent = null;
    }
}
