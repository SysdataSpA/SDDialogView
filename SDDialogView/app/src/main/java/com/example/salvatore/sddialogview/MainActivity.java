package com.example.salvatore.sddialogview;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.Toast;

import com.sysdata.sddialog.OnDialogCloseListener;
import com.sysdata.sddialog.SDDialogView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_CODE = 1;
    private ConstraintLayout mMainContainer;
    private SDDialogView dialogView;
    private Button mShowBtn;
    private CustomDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        mMainContainer = (ConstraintLayout) findViewById(R.id.main_container);
        mShowBtn = findViewById(R.id.show_dialog_button);
        mShowBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == mShowBtn){
            customDialog = new CustomDialog(this);
            ScaleAnimation animation = new ScaleAnimation(1f, 1f, 0f, 1f);
            animation.setInterpolator(new BounceInterpolator());
            animation.setDuration(600);
            dialogView = new SDDialogView.Builder()
                    .with(this)
                    .contentView(customDialog)
                    .cancelable(true)
                    .onDialogCloseListener(new OnDialogCloseListener() {
                        @Override
                        public void onClose(int requestCode, int resultCode) {
                            Toast.makeText(MainActivity.this, "close with "+resultCode, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .requestCode(REQUEST_CODE)
                    .enterAnimation(animation)
                    .build();
            dialogView.showDialog(mMainContainer);
        }
    }
}
