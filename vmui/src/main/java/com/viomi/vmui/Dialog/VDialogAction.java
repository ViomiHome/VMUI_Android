package com.viomi.vmui.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.view.View;

import androidx.annotation.IntDef;

import com.viomi.vmui.R;
import com.viomi.vmui.VButton;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class VDialogAction {
    @IntDef({ACTION_PROP_NEGATIVE, ACTION_PROP_NEUTRAL, ACTION_PROP_POSITIVE, ACTION_PROP_DANGER, ACTION_PROP_COMMON})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Prop {
    }

    //用于标记positive/negative/neutral
    public static final int ACTION_PROP_POSITIVE = 0;
    public static final int ACTION_PROP_NEUTRAL = 1;
    public static final int ACTION_PROP_NEGATIVE = 2;
    public static final int ACTION_PROP_DANGER = 3;
    public static final int ACTION_PROP_COMMON = 4;


    private Context mContext;
    private CharSequence mStr;
    private int mIconRes;
    private int mActionProp;
    private ActionListener mOnClickListener;
    private VButton mButton;
    private boolean mIsEnabled = true;

    public interface ActionListener {
        void onClick(Dialog dialog, int index);
    }

    public VDialogAction(Context context, CharSequence str, ActionListener onClickListener) {
        this.mContext = context;
        this.mStr = str;
        this.mOnClickListener = onClickListener;
    }

    public VDialogAction(Context context, int strRes, ActionListener onClickListener) {
        this.mContext = mContext;
        this.mStr = mContext.getResources().getString(strRes);
        this.mOnClickListener = onClickListener;
    }

    public VDialogAction(Context context, int iconRes, CharSequence str, @Prop int actionProp, ActionListener onClickListener) {
        this.mContext = context;
        this.mIconRes = iconRes;
        this.mStr = str;
        this.mActionProp = actionProp;
        this.mOnClickListener = onClickListener;
    }

    public void setOnClickListener(ActionListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public void setEnabled(boolean enabled) {
        mIsEnabled = enabled;
        if (mButton != null) {
            mButton.setEnabled(enabled);
        }
    }

    public VButton buildActionView(final Dialog dialog, final int index) {

        mButton = generateActionButton(dialog.getContext(), mStr, mIconRes);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null && mButton.isEnabled()) {
                    mOnClickListener.onClick(dialog, index);
                }
            }
        });
        return mButton;
    }

    /**
     * 生成适用于对话框的按钮
     */
    private VButton generateActionButton(Context context, CharSequence text, int iconRes) {
        // button 有提供 buttonStyle, 覆盖第三个参数不是好选择
        VButton button = new VButton(context);
        button.setButton_style(7);
        button.setMinimumHeight(0);

        TypedArray a;
        if (mActionProp == ACTION_PROP_DANGER || mActionProp == ACTION_PROP_COMMON) {
            a = context.obtainStyledAttributes(null, R.styleable.DialogActionStyleDef, R.attr.sheet_action_style, 0);
        } else {
            a = context.obtainStyledAttributes(null, R.styleable.DialogActionStyleDef, R.attr.dialog_action_style, 0);
        }
        int count = a.getIndexCount();
        int paddingHor = 0;
        ColorStateList commonBackground = null, negativeTextColor = null, positiveTextColor = null, commonTextColor = null, dangerTextColor = null;
        for (int i = 0; i < count; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.DialogActionStyleDef_android_gravity) {
                button.setGravity(a.getInt(attr, -1));
            } else if (attr == R.styleable.DialogActionStyleDef_android_textColor) {
                button.tvContent.setTextColor(a.getColorStateList(attr).getDefaultColor());
            } else if (attr == R.styleable.DialogActionStyleDef_android_textSize) {
                button.setTextSize(a.getDimensionPixelSize(attr, 0));
            } else if (attr == R.styleable.DialogActionStyleDef_dialog_action_button_padding_horizontal) {
                paddingHor = a.getDimensionPixelSize(attr, 0);
            } else if (attr == R.styleable.DialogActionStyleDef_android_background) {
                button.setBackground(a.getDrawable(attr));
            } else if (attr == R.styleable.DialogActionStyleDef_android_minWidth) {
                int miniWidth = a.getDimensionPixelSize(attr, 0);
                button.setMinimumWidth(miniWidth);
            } else if (attr == R.styleable.DialogActionStyleDef_dialog_positive_action_text_color) {
                positiveTextColor = a.getColorStateList(attr);
            } else if (attr == R.styleable.DialogActionStyleDef_dialog_negative_action_text_color) {
                negativeTextColor = a.getColorStateList(attr);
            } else if (attr == R.styleable.DialogActionStyleDef_dialog_danger_action_text_color) {
                dangerTextColor = a.getColorStateList(attr);
            } else if (attr == R.styleable.DialogActionStyleDef_dialog_common_action_text_color) {
                commonTextColor = a.getColorStateList(attr);
            } else if (attr == R.styleable.DialogActionStyleDef_android_textStyle) {
                int styleIndex = a.getInt(attr, -1);
                button.tvContent.setTypeface(null, styleIndex);
            } else if(attr == R.styleable.DialogActionStyleDef_dialog_common_action_background){
                commonBackground = a.getColorStateList(attr);
            }
        }

        a.recycle();
        button.setPadding(paddingHor, 0, paddingHor, 0);
        if (iconRes <= 0) {
            button.setText_content(text.toString());
        } else {
            button.setDrawable_right(context.getDrawable(iconRes));
            button.setText_content(text.toString());
        }

        button.setEnabled(mIsEnabled);

        if (mActionProp == ACTION_PROP_NEGATIVE) {
            button.tvContent.setTextColor(negativeTextColor.getDefaultColor());
        } else if (mActionProp == ACTION_PROP_POSITIVE) {
            button.tvContent.setTextColor(positiveTextColor.getDefaultColor());
        } else if (mActionProp == ACTION_PROP_DANGER) {
            button.tvContent.setTextColor(dangerTextColor.getDefaultColor());
            button.setBackgroundColor(commonBackground.getDefaultColor());
        } else if (mActionProp == ACTION_PROP_COMMON) {
            button.tvContent.setTextColor(commonTextColor.getDefaultColor());
            button.setBackgroundColor(commonBackground.getDefaultColor());
        }
        return button;
    }
}
