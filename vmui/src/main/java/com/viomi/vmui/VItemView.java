package com.viomi.vmui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import static android.content.ContentValues.TAG;

public class VItemView extends LinearLayout {
    public FrameLayout fContent;
    public LinearLayout llContent;
    public LinearLayout layoutRightText;
    public LinearLayout llLefttextview;
    public LinearLayout llRoot;
    public FrameLayout layoutRightImage;
    public ImageView ivLeft, ivTextReddot, ivRight, ivReddot, ivArrow;
    public TextView tvLeftTitle, tvLeftSubtitle, tvRightTitle, tvRightSubtitle;
    public VSwitch vSwitch;
    public VButton mButton;
    public VButton buttonDelete;
    public View vDivider;
    int style;
    int mScaleTouchSlop;
    boolean item_select, item_check, item_switch_check;
    boolean enableSwipeDelete;

    public VItemView(Context context) {
        this(context, null);
    }

    public VItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.item_view, this);

        llRoot = findViewById(R.id.ll_root);
        fContent = findViewById(R.id.f_content);
        llContent = findViewById(R.id.ll_content);
        llLefttextview = findViewById(R.id.ll_lefttextview);
        layoutRightText = findViewById(R.id.layout_right_text);
        layoutRightImage = findViewById(R.id.layout_right_image);
        ivLeft = findViewById(R.id.iv_left);
        ivTextReddot = findViewById(R.id.iv_text_reddot);
        ivRight = findViewById(R.id.iv_right);
        ivReddot = findViewById(R.id.iv_reddot);
        ivArrow = findViewById(R.id.iv_arrow);
        tvLeftTitle = findViewById(R.id.tv_left_title);
        tvLeftSubtitle = findViewById(R.id.tv_left_subtitle);
        tvRightTitle = findViewById(R.id.tv_right_title);
        tvRightSubtitle = findViewById(R.id.tv_right_subtitle);
        vSwitch = findViewById(R.id.vswitch);
        buttonDelete = findViewById(R.id.button_delete);
        mButton = findViewById(R.id.button);
        vDivider = findViewById(R.id.v_divider);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) fContent.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        fContent.setLayoutParams(layoutParams);
        mScaleTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        init();
        initAttrs(attrs);
        setClickable(true);
    }

    private void initAttrs(AttributeSet attrs) {
        if (attrs == null)
            return;
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.VItemView);
        setItem_arrow(a.getBoolean(R.styleable.VItemView_item_arrow, true));
        setItem_src(a.getResourceId(R.styleable.VItemView_item_src, 0));
        item_select = a.getBoolean(R.styleable.VItemView_item_select, false);
        item_check = a.getBoolean(R.styleable.VItemView_item_check, false);
        item_switch_check = a.getBoolean(R.styleable.VItemView_item_switch_check, false);
        setStyle(a.getInt(R.styleable.VItemView_item_style, 0));
        String item_subrighttitle = a.getString(R.styleable.VItemView_item_subrighttitle);
        setItem_subrighttitle(item_subrighttitle);
        setItem_lefttitle_textsize(a.getDimension(R.styleable.VItemView_item_lefttitle_textsize
                , TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics())));
        setItem_righttitle_textsize(a.getDimension(R.styleable.VItemView_item_righttitle_textsize
                , TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, TextUtils.isEmpty(item_subrighttitle) ? 13 : 14, getResources().getDisplayMetrics())));
        setItem_sublefttitle_textsize(a.getDimension(R.styleable.VItemView_item_sublefttitle_textsize
                , TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics())));
        setItem_subrighttitle_textsize(a.getDimension(R.styleable.VItemView_item_subrighttitle_textsize
                , TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics())));

        setItem_lefttitle_textcolor(a.getColor(R.styleable.VItemView_item_lefttitle_textcolor
                , getResources().getColor(R.color.title_gray)));
        setItem_righttitle_textcolor(a.getColor(R.styleable.VItemView_item_righttitle_textcolor
                , getResources().getColor(R.color.tips_gray)));
        setItem_subrighttitle_textcolor(a.getColor(R.styleable.VItemView_item_subrighttitle_textcolor
                , getResources().getColor(R.color.tips_gray)));
        setItem_sublefttitle_textcolor(a.getColor(R.styleable.VItemView_item_sublefttitle_textcolor
                , getResources().getColor(R.color.tips_gray)));
        setItem_lefttitle(a.getString(R.styleable.VItemView_item_lefttitle));
        setItem_righttitle(a.getString(R.styleable.VItemView_item_righttitle));

        setItem_sublefttitle(a.getString(R.styleable.VItemView_item_sublefttitle));
        setItem_button(a.getString(R.styleable.VItemView_item_button));
        setItem_text_redot(a.getBoolean(R.styleable.VItemView_item_text_redot, false));
        setItem_img_redot(a.getBoolean(R.styleable.VItemView_item_img_redot, false));

        vDivider.setVisibility(a.getBoolean(R.styleable.VItemView_show_divider, true) ? VISIBLE : GONE);
        enableSwipeDelete = a.getBoolean(R.styleable.VItemView_enable_swipe_delete, false);
        setItem_arrow_src(a.getResourceId(R.styleable.VItemView_item_arrow_src, R.mipmap.icon_arrow_bk));
        a.recycle();
    }

    void init() {
        layoutRightImage.setVisibility(GONE);
        layoutRightText.setVisibility(GONE);
        mButton.setVisibility(GONE);
        vSwitch.setVisibility(GONE);
        ivLeft.setVisibility(GONE);
    }


    public void setItem_lefttitle_textsize(float item_lefttitle_textsize) {
        tvLeftTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, item_lefttitle_textsize);
    }

    public void setItem_righttitle_textsize(float item_righttitle_textsize) {
        tvRightTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, item_righttitle_textsize);
    }

    public void setItem_subrighttitle_textsize(float item_subrighttitle_textsize) {
        tvRightSubtitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, item_subrighttitle_textsize);
    }

    public void setItem_sublefttitle_textsize(float item_sublefttitle_textsize) {
        tvLeftSubtitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, item_sublefttitle_textsize);
    }

    public void setItem_lefttitle_textcolor(int item_lefttitle_textcolor) {
        tvLeftTitle.setTextColor(item_lefttitle_textcolor);
    }

    public void setItem_righttitle_textcolor(int item_righttitle_textcolor) {
        tvRightTitle.setTextColor(item_righttitle_textcolor);
    }

    public void setItem_subrighttitle_textcolor(int item_subrighttitle_textcolor) {
        tvRightSubtitle.setTextColor(item_subrighttitle_textcolor);
    }

    public void setItem_sublefttitle_textcolor(int item_sublefttitle_textcolor) {
        tvLeftSubtitle.setTextColor(item_sublefttitle_textcolor);
    }

    public void setItem_lefttitle(String item_lefttitle) {
        tvLeftTitle.setText(item_lefttitle);
    }

    public void setItem_righttitle(String item_righttitle) {
        tvRightTitle.setText(item_righttitle);
    }

    public void setItem_subrighttitle(String item_subrighttitle) {
        tvRightSubtitle.setVisibility(TextUtils.isEmpty(item_subrighttitle) ? GONE : VISIBLE);
        tvRightSubtitle.setText(item_subrighttitle);
    }

    public void setItem_sublefttitle(String item_sublefttitle) {
        tvLeftSubtitle.setVisibility(TextUtils.isEmpty(item_sublefttitle) ? GONE : VISIBLE);
        tvLeftSubtitle.setText(item_sublefttitle);
    }

    public void setItem_button(String item_button) {
        mButton.setText_content(item_button);
    }

    public void setItem_text_redot(boolean item_text_redot) {
        ivTextReddot.setVisibility(item_text_redot ? VISIBLE : GONE);
    }

    public void setItem_img_redot(boolean item_img_redot) {
        ivReddot.setVisibility(item_img_redot ? VISIBLE : GONE);
    }

    public void setItemSelect(boolean item_select) {
        this.item_select = item_select;
        ivRight.setImageResource(item_select ? R.mipmap.icon_check : 0);
    }

    public void setItemCheck(boolean item_check) {
        this.item_check = item_check;
        ivLeft.setImageResource(item_check ? R.mipmap.icon_check_radio : R.mipmap.icon_uncheck_radio);
    }

    public void setItem_switch_check(boolean item_switch_check) {
        this.item_switch_check = item_switch_check;
        vSwitch.setCheck(item_switch_check);
    }

    public void setItem_arrow(boolean item_arrow) {
        ivArrow.setVisibility(item_arrow ? VISIBLE : GONE);
    }

    public void setItem_src(int item_src) {
        ivLeft.setImageResource(item_src);
        ivRight.setImageResource(item_src);
    }

    public void setStyle(int style) {
        this.style = style;
        switch (style) {
            case 0://normal
                ivLeft.setVisibility(GONE);
                layoutRightText.setVisibility(VISIBLE);
                break;
            case 1://small_icon_left
                ivLeft.setVisibility(VISIBLE);
                LinearLayout.LayoutParams layoutParams = (LayoutParams) ivLeft.getLayoutParams();
                layoutParams.width = (int) getResources().getDimension(R.dimen.small_icon_size);
                layoutParams.height = (int) getResources().getDimension(R.dimen.small_icon_size);
                ivLeft.setLayoutParams(layoutParams);
                llContent.setMinimumHeight((int) getResources().getDimension(R.dimen.itemview_small_icon));
                layoutRightText.setVisibility(VISIBLE);

                break;
            case 2://small_icon_right
                layoutRightImage.setVisibility(VISIBLE);
                FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) ivRight.getLayoutParams();
                layoutParams1.width = (int) getResources().getDimension(R.dimen.small_icon_size);
                layoutParams1.height = (int) getResources().getDimension(R.dimen.small_icon_size);
                ivRight.setLayoutParams(layoutParams1);
                llContent.setMinimumHeight((int) getResources().getDimension(R.dimen.itemview_small_icon));
                layoutRightImage.setVisibility(VISIBLE);

                break;
            case 3://middle_icon_left
                ivLeft.setVisibility(VISIBLE);
                LinearLayout.LayoutParams layoutParams2 = (LayoutParams) ivLeft.getLayoutParams();
                layoutParams2.width = (int) getResources().getDimension(R.dimen.middle_icon_size);
                layoutParams2.height = (int) getResources().getDimension(R.dimen.middle_icon_size);
                ivLeft.setLayoutParams(layoutParams2);
                llContent.setMinimumHeight((int) getResources().getDimension(R.dimen.itemview_middle_icon));
                layoutRightText.setVisibility(VISIBLE);
                break;
            case 4://middle_icon_right
                layoutRightImage.setVisibility(VISIBLE);
                FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) ivRight.getLayoutParams();
                layoutParams3.width = (int) getResources().getDimension(R.dimen.middle_icon_size);
                layoutParams3.height = (int) getResources().getDimension(R.dimen.middle_icon_size);
                ivRight.setLayoutParams(layoutParams3);
                llContent.setMinimumHeight((int) getResources().getDimension(R.dimen.itemview_middle_icon));
                layoutRightImage.setVisibility(VISIBLE);
                break;
            case 5://big_icon_left
                ivLeft.setVisibility(VISIBLE);
                LinearLayout.LayoutParams layoutParams4 = (LayoutParams) ivLeft.getLayoutParams();
                layoutParams4.width = (int) getResources().getDimension(R.dimen.big_icon_size);
                layoutParams4.height = (int) getResources().getDimension(R.dimen.big_icon_size);
                ivLeft.setLayoutParams(layoutParams4);
                llContent.setMinimumHeight((int) getResources().getDimension(R.dimen.itemview_big_icon));
                layoutRightText.setVisibility(VISIBLE);
                break;
            case 6://big_icon_right
                layoutRightImage.setVisibility(VISIBLE);
                FrameLayout.LayoutParams layoutParams5 = (FrameLayout.LayoutParams) ivRight.getLayoutParams();
                layoutParams5.width = (int) getResources().getDimension(R.dimen.big_icon_size);
                layoutParams5.height = (int) getResources().getDimension(R.dimen.big_icon_size);
                ivRight.setLayoutParams(layoutParams5);
                llContent.setMinimumHeight((int) getResources().getDimension(R.dimen.itemview_big_icon));
                layoutRightImage.setVisibility(VISIBLE);
                break;
            case 7://select
                layoutRightImage.setVisibility(VISIBLE);
                setItem_arrow(false);
                setItemSelect(item_select);
                break;
            case 8://radio_check
                ivLeft.setVisibility(VISIBLE);
                setItem_arrow(false);
                setItemCheck(item_check);
                break;
            case 9://switch
                vSwitch.setVisibility(VISIBLE);
                setItem_arrow(false);
                setItem_switch_check(item_switch_check);
                break;
            case 10://button
                setItem_arrow(false);
                mButton.setVisibility(VISIBLE);
                break;
            case 11://row_title
                llContent.setMinimumHeight((int) getResources().getDimension(R.dimen.itemview_row_title));
                setItem_arrow(false);
                LinearLayout.LayoutParams layoutParams6 = (LayoutParams) llLefttextview.getLayoutParams();
                layoutParams6.topMargin = 0;
                layoutParams6.bottomMargin = 0;
                llLefttextview.setLayoutParams(layoutParams6);
                setClickable(false);
                llRoot.setBackgroundColor(Color.WHITE);
                break;
        }
    }

    public void setItem_arrow_src(int item_arrow_src) {
        ivArrow.setImageResource(item_arrow_src);
    }

    public int getStyle() {
        return style;
    }


    public boolean isItemSelected() {
        return item_select;
    }

    public boolean isItemChecked() {
        return item_check;
    }

    public boolean isItem_switch_check() {
        return item_switch_check;
    }


    public void showDivider(boolean show) {
        vDivider.setVisibility(show ? VISIBLE : GONE);
    }

    public boolean isEnableSwipeDelete() {
        return enableSwipeDelete;
    }

    public void setEnableSwipeDelete(boolean enableSwipeDelete) {
        this.enableSwipeDelete = enableSwipeDelete;
    }

    float x;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (!enableSwipeDelete)
            return super.dispatchTouchEvent(event);
        if (event.getX() > buttonDelete.getX())
            return super.dispatchTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(x - event.getX()) > mScaleTouchSlop)
                    getParent().requestDisallowInterceptTouchEvent(true);
                if (x - event.getX() > mScaleTouchSlop) {
                    if (llRoot.getScrollX() == buttonDelete.getWidth())
                        break;
                    aniTo(0, buttonDelete.getWidth());
                } else if (x - event.getX() < mScaleTouchSlop) {
                    if (llRoot.getScrollX() == 0)
                        break;
                    aniTo(buttonDelete.getWidth(), 0);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                break;
        }
        return super.dispatchTouchEvent(event);
    }

    public void closeDelete() {
        if (llRoot.getScrollX() == 0)
            return;
        aniTo(buttonDelete.getWidth(), 0);
    }

    @Override
    public boolean performLongClick() {
        if (Math.abs(getScrollX()) > mScaleTouchSlop) {
            return false;
        }
        return super.performLongClick();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!enableSwipeDelete)
            return super.onInterceptTouchEvent(event);
        if (llRoot.getScrollX() > 0)
            return true;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() > buttonDelete.getX())
                    return false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(x - event.getX()) > mScaleTouchSlop) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    ValueAnimator animator;

    void aniTo(int start, int end) {
        if (animator != null && animator.isRunning())
            return;
        animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                llRoot.scrollTo((Integer) animation.getAnimatedValue(), 0);
            }
        });
        animator.setDuration(200);
        animator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        if (animator != null)
            animator.cancel();
        super.onDetachedFromWindow();
    }
}
