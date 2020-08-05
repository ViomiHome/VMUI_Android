package com.viomi.vmui;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Handler;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

/**
 * Created by Ljh on 2020/8/4.
 * Description:
 */
public class VDialogToast {

    private Context mContext;
    private Handler mHandler;
    private TextView mTextView;
    private int mDuration;
    private Dialog dialog;
    private static ValueAnimator mAnimator;
    //
    public static final int LENGTH_SHORT = 1500;
    public static final int LENGTH_LONG = 30000;

    public VDialogToast(Context context) {
        try {
            VToast.dismiss();//隐藏Toast的显示
            //
            mContext = context;
            mHandler = new Handler();
            if(dialog != null)
                dialog.dismiss();
            dialog = new Dialog(mContext, R.style.VToastDialogStyle);
            dialog.setContentView(R.layout.vtoast_layout);
            dialog.setCanceledOnTouchOutside(false);
            mTextView = (TextView) dialog.findViewById(R.id.mbMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static VDialogToast makeLoadingToast(Context context, CharSequence message,
                                       int duration) {
        VDialogToast toastUtils = new VDialogToast(context);
        try {
            toastUtils.mDuration = duration;
            toastUtils.mTextView.setText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        toastUtils.dialog.findViewById(R.id.vmui_toast_icon).setVisibility(View.VISIBLE);
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
        dialog.setOnDismissListener(onDismissListener);
    }

    public void show() {
        try {
            dialog.show();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                }
            }, mDuration);
        } catch (Exception e) {
            e.printStackTrace();
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

