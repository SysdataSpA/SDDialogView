package com.sysdata.kt.sddialog

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import java.lang.ref.WeakReference
import android.view.animation.DecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation


/**
 * Created by Salvatore on 06/07/2018.
 */
class SDDialogView(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr), View.OnClickListener {
    private var requestCode: Int = 0
    private var cancelable: Boolean = true
    private var contentView: WeakReference<View>? = null
    var view: WeakReference<View>? = null
    var parent: WeakReference<ViewGroup>? = null
    private var onCloseListener: WeakReference<OnDialogCloseListener>? = null
    private var enterAnimation: Animation? = null
    private var exitAnimation: Animation? = null

    fun showDialog(parent: ViewGroup) {
        this.parent = WeakReference(parent.parent as ViewGroup)
        view = WeakReference(LayoutInflater.from(context).inflate(R.layout.dialog_view, parent.parent as ViewGroup, false))
        val dialogContainer: LinearLayout? = view?.get()?.findViewById<View>(R.id.dialog_container) as LinearLayout?
        dialogContainer?.let {
            it.setOnClickListener(this)
            val layoutParams = parent.layoutParams
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            this.parent?.get()?.addView(view?.get(), layoutParams)
            contentView?.get()?.let {
                // adding animation when attaching content view to parent
                it.addOnAttachAnimation(enterAnimation)
                // add content view to container with alpha background
                dialogContainer.removeAllViews()
                val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                dialogContainer.addView(it, params)
                if (it is Compatible)
                    it.onBindParentView(this)
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        this.startAnimation(getFadeInAnimation())
    }

    private fun View.addOnAttachAnimation(animation: Animation?){
        animation?.let {
            this.addOnAttachStateChangeListener(object : OnAttachStateChangeListener{
                override fun onViewDetachedFromWindow(v: View?) {}
                override fun onViewAttachedToWindow(v: View?) { v?.startAnimation(animation) }
            })
        }
    }

    fun closeDialog(resultCode: Int = 0) {
        exitAnimation?.apply {
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    removeViews(resultCode)
                }

                override fun onAnimationStart(animation: Animation?) {}
            })
            contentView?.get()?.startAnimation(this)
        } ?: removeViews(resultCode)
    }

    fun removeViews(resultCode: Int){
        onCloseListener?.get()?.onClose(requestCode, resultCode)
        parent?.get()?.let {
            it.removeView(view?.get())
            contentView?.get()?.let {
                if (it is Compatible) {
                    it.onUnbindParentView()
                }
            }
        }
        contentView?.clear()
        parent?.clear()
        view?.clear()
        onCloseListener?.clear()
    }

    override fun onClick(p0: View?) {
        p0?.let {
            if (it.id == R.id.dialog_container && cancelable) {
                closeDialog()
            }
        }
    }

    private fun getFadeInAnimation():Animation {
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.interpolator = DecelerateInterpolator()
        fadeIn.duration = 600
        return fadeIn
    }

    private fun getFadeOutAnimation():Animation {
        val fadeOut = AlphaAnimation(1f, 0f)
        fadeOut.interpolator = DecelerateInterpolator()
        fadeOut.duration = 400
        return fadeOut
    }

    interface Compatible {
        fun onBindParentView(parent: SDDialogView)
        fun onUnbindParentView()
    }

    private constructor(builder: Builder.InnerBuilder) : this(builder.context) {
        val referent = builder.contentView
        this.contentView = if (referent != null) WeakReference(referent) else null
        this.cancelable = builder.cancelable
        val listener = builder.onCloseListener
        this.onCloseListener = if (listener != null) WeakReference(listener) else null
        this.requestCode = builder.requestCode
        if(builder.useAnimations) {
            this.enterAnimation = if (builder.enterAnimation != null) builder.enterAnimation else getFadeInAnimation()
            this.exitAnimation = if (builder.exitAnimation != null) builder.exitAnimation else getFadeOutAnimation()
        }
        builder.context = null
        builder.contentView = null
        builder.onCloseListener = null
        builder.enterAnimation = null
        builder.exitAnimation = null
    }

    class Builder {
        fun with(context: Context?) = InnerBuilder(context)

        /**
         * Inner builder class to instantiate the new SDDialogView,
         * to get this you have to call Builder().with(context) and the use this to create the view
         *
         * @param context Context necessary to create the overlay view
         */
        class InnerBuilder(context: Context?) {
            //effective dialog to insert in overlay on the parent view
            var contentView: View? = null

            // if true the dialog can be closed just by clicking on the gray area outside of the dialog
            var cancelable: Boolean = true
                private set

            // if true the dialog will have an enter and exit animation
            var useAnimations: Boolean = false
                private set

            // listener called when closing the dialog
            var onCloseListener: OnDialogCloseListener? = null

            var requestCode: Int = 0
                private set

            // needed to create the new SDDialogView
            var context: Context? = null

            // custom enter animation, if you don't set the animation there will be a fade-in animation
            var enterAnimation: Animation? = null

            // custom exit animation, if you don't set the animation there will be a fade-out animation
            var exitAnimation: Animation? = null


            init {
                this.context = context
            }

            fun contentView(contentView: View) = apply { this.contentView = contentView }
            fun cancelable(cancelable: Boolean) = apply { this.cancelable = cancelable }
            fun useAnimations(useAnimations: Boolean) = apply { this.useAnimations = useAnimations }
            fun onCloseListener(listener: OnDialogCloseListener) = apply { this.onCloseListener = listener }
            fun requestCode(requestCode: Int) = apply { this.requestCode = requestCode }
            fun enterAnimation(enterAnimation: Animation) = apply { this.enterAnimation = enterAnimation }
            fun exitAnimation(exitAnimation: Animation) = apply { this.exitAnimation = exitAnimation }

            fun build() = SDDialogView(this)
        }
    }


}