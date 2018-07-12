package com.sysdata.sddialog;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.example.sddialog.R;

import java.lang.ref.WeakReference;

/**
 * Created by Salvatore on 28/06/2018.
 */

public class SDDialogView extends View implements View.OnClickListener {
    private Animation exitAnimation;
    private Animation enterAnimation;
    private int requestCode;
    private boolean cancelable;
    private WeakReference<View> contentView;
    private WeakReference<ViewGroup> parent;
    private WeakReference<View> view;
    private WeakReference<OnDialogCloseListener> onCloseListener;

    public SDDialogView(Context context) {
        super(context);
        init(context);
    }

    public SDDialogView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SDDialogView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        view = new WeakReference<View>(LayoutInflater.from(context).inflate(R.layout.dialog_view, null));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.dialog_container) {
            if (cancelable) {
                closeDialog();
            }
        }
    }

    public void showDialog(ViewGroup parent) {
        this.parent = new WeakReference<>((ViewGroup) parent.getParent());
        if (view == null || view.get() == null) {
            view = new WeakReference<>(LayoutInflater.from(getContext()).inflate(R.layout.dialog_view, null));
        }
        LinearLayout dialogContainer = view.get().findViewById(R.id.dialog_container);
        if (dialogContainer == null)
            return;
        dialogContainer.setOnClickListener(this);
        ViewGroup.LayoutParams layoutParams = parent.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        this.parent.get().addView(view.get(), layoutParams);
        if (contentView != null && contentView.get() instanceof Compatible) {
            if (enterAnimation != null) {
                contentView.get().addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
                    @Override
                    public void onViewAttachedToWindow(View view) {
                        view.startAnimation(enterAnimation);
                    }

                    @Override
                    public void onViewDetachedFromWindow(View view) {
                    }
                });
            }
            dialogContainer.removeAllViews();
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialogContainer.addView(contentView.get(), params);
            ((Compatible) contentView.get()).onBindParentView(this);
        }
    }

    public void closeDialog() {
        closeDialog(0);
    }

    public void closeDialog(final int resultCode) {
        if (exitAnimation != null) {
            exitAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    removeViews(resultCode);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            if (contentView != null && contentView.get() != null)
                contentView.get().startAnimation(exitAnimation);
        } else
            removeViews(resultCode);
    }

    public void removeViews(int resultCode) {
        if (onCloseListener != null && onCloseListener.get() != null)
            onCloseListener.get().onClose(requestCode, resultCode);
        if (parent != null && parent.get() != null && view != null && view.get() != null) {
            parent.get().removeView(view.get());
        }
        if (contentView != null && contentView.get() != null && contentView.get() instanceof Compatible) {
            ((Compatible) contentView.get()).onUnbindParentView();
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

    private SDDialogView(Builder.InnerBuilder builder) {
        super(builder.context);
        cancelable = builder.cancelable;
        contentView = new WeakReference<>(builder.contentView);
        onCloseListener = new WeakReference<>(builder.listener);
        requestCode = builder.requestCode;
        if (builder.useAnimations) {
            enterAnimation = builder.enterAnimation != null ? builder.enterAnimation : getFadeInAnimation();
            exitAnimation = builder.exitAnimation != null ? builder.exitAnimation : getFadeOutAnimation();
        }
        builder.context = null;
        builder.contentView = null;
        builder.listener = null;
        builder.enterAnimation = null;
    }

    private Animation getFadeInAnimation() {
        AlphaAnimation fadeIn = new AlphaAnimation(0f, 1f);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(600);
        return fadeIn;
    }

    private Animation getFadeOutAnimation() {
        AlphaAnimation fadeOut = new AlphaAnimation(1f, 0f);
        fadeOut.setInterpolator(new DecelerateInterpolator());
        fadeOut.setDuration(400);
        return fadeOut;
    }

    public static final class Builder {
        public InnerBuilder with(Context context) {
            return new InnerBuilder(context);
        }

        /**
         * Inner builder class to instantiate the new SDDialogView,
         * to get this you have to call Builder().with(context) and the use this to create the view
         */
        public static final class InnerBuilder {
            //effective dialog to insert in overlay on the parent view
            private View contentView;
            // if true the dialog can be closed just by clicking on the gray area outside of the dialog
            private boolean cancelable = true;
            // if true the dialog will have enter and exit animations
            private boolean useAnimations;
            // needed to create the new SDDialogView
            private Context context;
            // listener called when closing the dialog
            private OnDialogCloseListener listener;
            private int requestCode;
            // custom enter animation, if you don't set the animation there will be a fade-in animation
            private Animation enterAnimation;
            // custom exit animation, if you don't set the animation there will be a fade-out animation
            private Animation exitAnimation;

            InnerBuilder(Context context) {
                this.context = context;
            }

            public InnerBuilder contentView(View val) {
                contentView = val;
                return this;
            }

            public SDDialogView build() {
                return new SDDialogView(this);
            }

            public InnerBuilder cancelable(boolean val) {
                cancelable = val;
                return this;
            }

            public InnerBuilder useAnimations(boolean val) {
                useAnimations = val;
                return this;
            }

            public InnerBuilder requestCode(int val) {
                requestCode = val;
                return this;
            }

            public InnerBuilder enterAnimation(Animation val) {
                enterAnimation = val;
                return this;
            }

            public InnerBuilder exitAnimation(Animation val) {
                exitAnimation = val;
                return this;
            }

            public InnerBuilder onDialogCloseListener(OnDialogCloseListener val) {
                listener = val;
                return this;
            }
        }
    }

    public interface Compatible {
        void onBindParentView(SDDialogView parent);

        void onUnbindParentView();
    }
}