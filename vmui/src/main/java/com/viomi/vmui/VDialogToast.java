package com.viomi.vmui;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

/**
 * Created by Ljh on 2020/8/4.
 * Description:
 */
public class VDialogToast {
    private Handler mHandler;
    private int mDuration;
    private Dialog dialog;
    private static ValueAnimator mAnimator;
    private static VDialogToast dialogToast;
    //
    public static final int LENGTH_SHORT = 1500;
    public static final int LENGTH_LONG = 30000;

    public VDialogToast(Context context) {
        try {
            VToast.dismiss();//隐藏Toast的显示
            //
            mHandler = new Handler();
            dialog = new Dialog(context, R.style.VToastDialogStyle);
            dialog.setContentView(R.layout.vtoast_layout);
            dialog.setCanceledOnTouchOutside(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static VDialogToast getToast(Context context){
        if(dialogToast != null){
            if(dialogToast.dialog !=null) {
                dialogToast.dialog.dismiss();
                dialogToast.dialog = null;
                dialogToast.mHandler.removeCallbacksAndMessages(null);
            }
        }
        dialogToast = new VDialogToast(context);
        return dialogToast;
    }

    public static VDialogToast makeLoadingToast(Context context, CharSequence message, int duration) {
        VDialogToast toastUtils = getToast(context);
        try {
            toastUtils.mDuration = duration;
            ((TextView)toastUtils.dialog.findViewById(R.id.mbMessage)).setText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        toastUtils.dialog.findViewById(R.id.vmui_toast_icon).addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                rotate(v);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                if (mAnimator != null)
                    mAnimator.cancel();
            }
        });
        return toastUtils;
    }

    public static VDialogToast makeLoadingToast(Context context, int resId, int duration) {
        String mes = "";
        try {
            mes = context.getResources().getString(resId);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        return makeLoadingToast(context, mes, duration);
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener){
        if(dialog != null)
            dialog.setOnDismissListener(onDismissListener);
    }

    public void show() {
        try {
            dialog.show();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (dialog != null) {
                        dialog.dismiss();
                        dialog = null;
                    }
                }
            }, mDuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dismiss() {
        Log.d("VDialogToast","dismiss");
        if(dialogToast != null){
            if(dialogToast.dialog !=null) {
                dialogToast.dialog.dismiss();
                dialogToast.dialog = null;
                Log.d("VDialogToast","dialogToast.dialog dismiss");
                dialogToast.mHandler.removeCallbacksAndMessages(null);
            }
            dialogToast = null;
        }
    }

    private static void rotate(View view) {
        if (mAnimator != null)
            mAnimator.cancel();
        mAnimator = ValueAnimator.ofInt(0, 365);
        mAnimator.addUpdateListener(valueAnimator -> view.setRotation((int) valueAnimator.getAnimatedValue()));
        mAnimator.setDuration(600);
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.start();
    }
}

