package com.viomi.vmui;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.viomi.vmui.utils.VDisplayHelper;

public class VToast extends Toast {

    private static ValueAnimator mAnimator;
    private static VToast vToast;

    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public VToast(Context context) {
        super(context);
    }

    @Override
    public void setView(View view) {
        super.setView(view);
    }

    /**
     * 正常toast
     *
     * @param context
     * @param text
     * @param duration
     * @return
     */
    public static Toast makeText(Context context, CharSequence text, int duration) {
        VToast vToast = getToast(context);
        vToast.setDuration(duration);
        View view = getToastView(context, text, -1);
        vToast.setView(view);
        vToast.setGravity(Gravity.BOTTOM, 0, 280);
        return vToast;
    }

    static VToast getToast(Context context) {
        if (vToast != null) {
            vToast.cancel();
            vToast = null;
        }
        vToast = new VToast(context);
        return vToast;
    }

    public static void dismiss() {
        if (vToast != null) {
            vToast.cancel();
            vToast = null;
        }
    }

    /**
     * 登陆成功
     *
     * @param context
     * @param text
     * @param duration
     * @return
     */
    public static Toast makeSuccessText(Context context, CharSequence text, int duration) {
        VToast vToast = getToast(context);
        vToast.setDuration(duration);
        View view = getToastView(context, text, R.mipmap.ic_toast_success);
        vToast.setView(view);
        vToast.setGravity(Gravity.BOTTOM, 0, 280);
        return vToast;
    }

    /**
     * 加载失败
     *
     * @param context
     * @param text
     * @param duration
     * @return
     */
    public static Toast makeErrorText(Context context, CharSequence text, int duration) {
        VToast vToast = getToast(context);
        vToast.setDuration(duration);
        View view = getToastView(context, text, R.mipmap.ic_toast_error);
        vToast.setView(view);
        vToast.setGravity(Gravity.BOTTOM, 0, 280);
        return vToast;
    }


    public static Toast makeLoadingToast(Context context, CharSequence text, int duration) {
        VToast vToast = getToast(context);
        vToast.setDuration(duration);
        View view = getToastView(context, text, R.mipmap.icon_loading_white);
        vToast.setView(view);
        vToast.setGravity(Gravity.BOTTOM, 0, 280);
        view.findViewById(R.id.vmui_toast_icon).addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
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
        return vToast;
    }

    /**
     * 自定义图标显示内容
     *
     * @param context
     * @param text
     * @param duration
     * @param resId
     * @return
     */
    public static Toast makeText(Context context, CharSequence text, int duration, int resId) {
        VToast vToast = getToast(context);
        vToast.setDuration(duration);
        View view = getToastView(context, text, resId);
        vToast.setView(view);
        vToast.setGravity(Gravity.BOTTOM, 0, 280);
        return vToast;
    }

    private static View getToastView(Context context, CharSequence text, int resId) {
        RelativeLayout layout = new RelativeLayout(context);
        layout.setBackground(context.getResources().getDrawable(R.drawable.vmui_toast_bg));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int lr = VDisplayHelper.dp2px(context, 24);
        int tb = VDisplayHelper.dp2px(context, 12);
        layoutParams.setMargins(lr, tb, lr, tb);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        LinearLayout container = new LinearLayout(context);
        container.setGravity(Gravity.CENTER_VERTICAL);
        TextView message = new TextView(context);

        message.setId(R.id.vmui_toast_message);
        message.setTextColor(context.getResources().getColor(R.color.white));
        message.setTextSize(14);
        message.setText(text);

        if (resId != -1) {
            ImageView icon = new ImageView(context);
            icon.setImageResource(resId);
            icon.setId(R.id.vmui_toast_icon);
            RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(VDisplayHelper.dp2px(context, 18), VDisplayHelper.dp2px(context, 18));
            lp1.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            icon.setLayoutParams(lp1);
            container.addView(icon);
            message.setPadding(VDisplayHelper.dp2px(context, 8), 0, 0, 0);
        }
        container.addView(message);
        container.setLayoutParams(layoutParams);
        layout.addView(container);
        return layout;
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