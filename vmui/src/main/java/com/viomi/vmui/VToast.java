package com.viomi.vmui;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.viomi.vmui.utils.VMUIDisplayHelper;

public class VToast extends Toast {

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
     * @param context
     * @param text
     * @param duration
     * @return
     */
    public static Toast makeText(Context context, CharSequence text, int duration) {
        VToast vToast = new VToast(context);
        vToast.setDuration(duration);
        View view = getToastView(context, text, -1);
        vToast.setView(view);
        return vToast;
    }

    /**
     * 登陆成功
     * @param context
     * @param text
     * @param duration
     * @return
     */
    public static Toast makeSuccessText(Context context,CharSequence text,int duration){
        VToast vToast = new VToast(context);
        vToast.setDuration(duration);
        View view = getToastView(context, text, R.mipmap.ic_toast_success);
        vToast.setView(view);
        return vToast;
    }

    /**
     * 加载失败
     * @param context
     * @param text
     * @param duration
     * @return
     */
    public static Toast makeErrorText(Context context,CharSequence text,int duration){
        VToast vToast = new VToast(context);
        vToast.setDuration(duration);
        View view = getToastView(context,text,R.mipmap.ic_toast_error);
        vToast.setView(view);
        return vToast;
    }

    /**
     * 自定义图标显示内容
     * @param context
     * @param text
     * @param duration
     * @param resId
     * @return
     */
    public static Toast makeText(Context context,CharSequence text,int duration,int resId){
        VToast vToast = new VToast(context);
        vToast.setDuration(duration);
        View view = getToastView(context, text, resId);
        vToast.setView(view);
        return vToast;
    }

    private static View getToastView(Context context, CharSequence text, int resId) {
        RelativeLayout layout = new RelativeLayout(context);
        layout.setBackground(context.getDrawable(R.drawable.vmui_toast_bg));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int lr = VMUIDisplayHelper.dp2px(context, 24);
        int tb = VMUIDisplayHelper.dp2px(context, 12);
        layoutParams.setMargins(lr, tb, lr, tb);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
        LinearLayout container = new LinearLayout(context);
        container.setLayoutParams(layoutParams);
        VTextView message = new VTextView(context);
        message.setId(R.id.vmui_toast_message);
        message.setTextColor(context.getResources().getColor(R.color.white));
        message.setTextSize(14);
        message.setText(text);

        if (resId != -1) {
            ImageView icon = new ImageView(context);
            icon.setImageResource(resId);
            container.addView(icon);
            message.setPadding(13, 0, 0, 0);
        }
        container.addView(message);
        layout.addView(container);
        return layout;
    }
}