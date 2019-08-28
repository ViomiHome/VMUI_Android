package com.viomi.vmui.Dialog;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;

import androidx.annotation.IntDef;

import com.viomi.vmui.R;
import com.viomi.vmui.utils.VMUIDrawableHelper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class VDialogAction {
    @IntDef({ACTION_PROP_NEGATIVE, ACTION_PROP_NEUTRAL, ACTION_PROP_POSITIVE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Prop {
    }

    //用于标记positive/negative/neutral
    public static final int ACTION_PROP_POSITIVE = 0;
    public static final int ACTION_PROP_NEUTRAL = 1;
    public static final int ACTION_PROP_NEGATIVE = 2;


    private Context mContext;
    private CharSequence mStr;
    private int mIconRes;
    private int mActionProp;
    private ActionListener mOnClickListener;
    private Button mButton;
    private boolean mIsEnabled = true;

    public interface ActionListener {
        void onClick(VDialog dialog, int index);
    }

    public VDialogAction(Context context, CharSequence str,ActionListener onClickListener) {
        this.mContext = context;
        this.mStr = str;
        this.mOnClickListener = onClickListener;
    }

    public VDialogAction(Context context, int strRes,ActionListener onClickListener) {
        this.mContext = mContext;
        this.mStr = mContext.getResources().getString(strRes);
        this.mOnClickListener = onClickListener;
    }

    public VDialogAction(Context context, int iconRes, CharSequence str, @Prop int actionProp, ActionListener onClickListener){
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

    public Button buildActionView(final VDialog dialog, final int index) {

        mButton = generateActionButton(dialog.getContext(),mStr,-1);
        //mButton = new Button(dialog.getContext());
        //mButton.setText(mStr);
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
    private Button generateActionButton(Context context, CharSequence text, int iconRes) {
        // button 有提供 buttonStyle, 覆盖第三个参数不是好选择
        Button button = new Button(context);
        button.setMinimumHeight(0);
        button.setPressed(true);
        button.setEnabled(true);
        TypedArray a = context.obtainStyledAttributes(null, R.styleable.DialogActionStyleDef, R.attr.dialog_action_style, 0);
        int count = a.getIndexCount();
        int paddingHor = 0, iconSpace = 0;
        ColorStateList negativeTextColor = null, positiveTextColor = null;
        for (int i = 0; i < count; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.DialogActionStyleDef_android_gravity) {
                button.setGravity(a.getInt(attr, -1));
            } else if (attr == R.styleable.DialogActionStyleDef_android_textColor) {
                button.setTextColor(a.getColorStateList(attr));
            } else if (attr == R.styleable.DialogActionStyleDef_android_textSize) {
                button.setTextSize(TypedValue.COMPLEX_UNIT_PX, a.getDimensionPixelSize(attr, 0));
            } else if (attr == R.styleable.DialogActionStyleDef_dialog_action_button_padding_horizontal) {
                paddingHor = a.getDimensionPixelSize(attr, 0);
            } else if (attr == R.styleable.DialogActionStyleDef_android_background) {
                VMUIDrawableHelper.setBackground(button, a.getDrawable(attr));
            } else if (attr == R.styleable.DialogActionStyleDef_android_minWidth) {
                int miniWidth = a.getDimensionPixelSize(attr, 0);
                button.setMinWidth(miniWidth);
                button.setMinimumWidth(miniWidth);
            } else if (attr == R.styleable.DialogActionStyleDef_dialog_positive_action_text_color) {
                positiveTextColor = a.getColorStateList(attr);
            } else if (attr == R.styleable.DialogActionStyleDef_dialog_negative_action_text_color) {
                negativeTextColor = a.getColorStateList(attr);
            } else if (attr == R.styleable.DialogActionStyleDef_dialog_action_icon_space) {
                iconSpace = a.getDimensionPixelSize(attr, 0);
            } else if(attr == R.styleable.DialogActionStyleDef_android_height){
                button.setHeight(a.getDimensionPixelSize(attr,0));
            }
        }

        a.recycle();
        button.setPadding(paddingHor, 0, paddingHor, 0);
        if (iconRes <= 0) {
            button.setText(text);
        } else {
            //button.setText(VMUISpanHelper.generateSideIconText(true, iconSpace, text, ContextCompat.getDrawable(context, iconRes)));
        }

        button.setClickable(true);
        button.setEnabled(mIsEnabled);

        if (mActionProp == ACTION_PROP_NEGATIVE) {
            button.setTextColor(negativeTextColor);
        } else if (mActionProp == ACTION_PROP_POSITIVE) {
            button.setTextColor(positiveTextColor);
        }
        return button;
    }
}
