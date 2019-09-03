package com.viomi.vmui.Dialog;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.viomi.vmui.R;
import com.viomi.vmui.utils.VMUIDisplayHelper;

public class VDialogView extends LinearLayout {
    private int mMinWidth;
    private int mMaxWidth;
    private int mMaxHeight;

    public VDialogView(Context context) {
        this(context, null, 0);
    }

    public VDialogView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VDialogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mMinWidth = VMUIDisplayHelper.getAttrDimen(context, R.attr.dialog_min_width);
        mMaxWidth = VMUIDisplayHelper.getAttrDimen(context, R.attr.dialog_max_width);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (mMaxWidth > 0 && widthSize > mMaxWidth) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxWidth, widthMode);
        }
        if (mMaxHeight > 0 && heightSize > mMaxHeight) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, heightMode);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST) {
            int measureWidth = getMeasuredWidth();
            if (measureWidth < mMinWidth && mMinWidth < widthSize) {
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(mMinWidth, MeasureSpec.EXACTLY);
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    public void setMaxWidth(int maxWidth) {
        this.mMaxWidth = maxWidth;
    }

    public void setMinWidth(int minWidth) {
        this.mMinWidth = minWidth;
    }
}
