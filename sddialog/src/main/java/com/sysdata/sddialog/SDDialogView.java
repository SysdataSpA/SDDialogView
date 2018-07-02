package com.sysdata.sddialog;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.sddialog.R;

import java.lang.ref.WeakReference;

/**
 * Created by Salvatore on 28/06/2018.
 */

public class SDDialogView extends View implements View.OnClickListener{
    private int requestCode;
    private boolean cancelable;
    private WeakReference<View> contentView;
    private WeakReference<ViewGroup> parent;
    private WeakReference<View> view;
    private WeakReference<OnDialogCloseListener> onCloseListener;

    public SDDialogView(Context context) {
        super(context);
        init(context, null);
    }

    public SDDialogView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SDDialogView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        view = new WeakReference<View>(LayoutInflater.from(context).inflate(R.layout.dialog_view, null));
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.dialog_container) {
            if(cancelable) {
                closeDialog();
            }
        }
    }

    public void showDialog(ViewGroup parent){
        this.parent = new WeakReference<ViewGroup>((ViewGroup) parent.getParent());
        if (view == null || view.get() == null) {
            view = new WeakReference<View>(LayoutInflater.from(getContext()).inflate(R.layout.dialog_view, null));
        }
        LinearLayout dialogContainer = (LinearLayout) view.get().findViewById(R.id.dialog_container);
        if(dialogContainer == null)
            return;
        dialogContainer.setOnClickListener(this);
        ViewGroup.LayoutParams layoutParams = parent.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        this.parent.get().addView(view.get(), layoutParams);
        if(contentView != null && contentView.get() instanceof Compatible) {
            dialogContainer.removeAllViews();
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialogContainer.addView(contentView.get(), params);
            ((Compatible)contentView.get()).onBindParentView(this);
        }
    }

    public void closeDialog(){
        closeDialog(0);
    }

    public void closeDialog(int resultCode){
        if(onCloseListener != null && onCloseListener.get() != null)
            onCloseListener.get().onClose(requestCode,resultCode);
        if (parent != null && parent.get() != null && view != null && view.get() !=  null) {
            parent.get().removeView(view.get());
        }
        if (contentView != null && contentView.get() instanceof Compatible) {
            ((Compatible)contentView.get()).onUnbindParentView();
        }
        if (contentView != null) {
            contentView.clear();
        }
        if (parent != null) {
            parent.clear();
        }
        if (view != null) {
            view.clear();
        }
        if (onCloseListener != null) {
            onCloseListener.clear();
        }
    }

    private SDDialogView(Builder builder) {
        super(builder.context);
        cancelable = builder.cancelable;
        contentView = new WeakReference<View>(builder.contentView);
        onCloseListener = new WeakReference<OnDialogCloseListener>(builder.listener);
        requestCode = builder.requestCode;
        builder.context = null;
        builder.contentView = null;
        builder.listener = null;
    }

    public static final class Builder {
        private View contentView;
        private boolean cancelable;
        private Context context;
        private OnDialogCloseListener listener;
        private int requestCode;

        public Builder() {
        }

        public Builder contentView(View val) {
            contentView = val;
            return this;
        }

        public Builder with(Context context) {
            this.context = context;
            return this;
        }

        public SDDialogView build() {
            return new SDDialogView(this);
        }

        public Builder cancelable(boolean val) {
            cancelable = val;
            return this;
        }

        public Builder requestCode(int val){
            requestCode = val;
            return this;
        }

        public Builder onDialogCloseListener(OnDialogCloseListener val){
            listener = val;
            return this;
        }
    }

    public interface Compatible{
        void onBindParentView(SDDialogView parent);
        void onUnbindParentView();
    }
}