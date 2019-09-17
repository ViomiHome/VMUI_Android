package com.viomi.vmui;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class VItemView extends LinearLayout {
    public FrameLayout fContent;
    public LinearLayout llContent;
    public LinearLayout layoutRightText;
    public LinearLayout llLefttextview;
    public LinearLayout llRoot;
    public FrameLayout layoutRightImage;
    public ImageView ivLeft, ivTextReddot, ivRight, ivReddot, ivArrow;
    public TextView tvLeftTitle, tvLeftSubtitle, tvRightTitle, tvRightSubtitle;
    public Switch vSwitch;
    public VButton mButton;
    public VButton buttonDelete;
    int style;
    int item_lefttitle_textsize;
    int item_righttitle_textsize;
    int item_subrighttitle_textsize;
    int item_sublefttitle_textsize;
    int item_lefttitle_textcolor;
    int item_righttitle_textcolor;
    int item_subrighttitle_textcolor;
    int item_sublefttitle_textcolor;
    String item_lefttitle, item_righttitle, item_subrighttitle, item_sublefttitle, item_button;
    boolean item_text_redot, item_img_redot, item_select, item_check, item_switch_check, item_arrow;


    int item_src;

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
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) fContent.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        fContent.setLayoutParams(layoutParams);
        initAttrs(attrs);
        init();
    }

    private void initAttrs(AttributeSet attrs) {
        if (attrs == null)
            return;
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.VItemView);
        style = a.getInt(R.styleable.VItemView_item_style, 0);
        item_lefttitle_textsize = (int) a.getDimension(R.styleable.VItemView_item_lefttitle_textsize
                , TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, getResources().getDisplayMetrics()));
        item_righttitle_textsize = (int) a.getDimension(R.styleable.VItemView_item_righttitle_textsize
                , TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 13, getResources().getDisplayMetrics()));
        item_subrighttitle_textsize = (int) a.getDimension(R.styleable.VItemView_item_subrighttitle_textsize
                , TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics()));
        item_sublefttitle_textsize = (int) a.getDimension(R.styleable.VItemView_item_sublefttitle_textsize
                , TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics()));
        item_lefttitle_textcolor = a.getColor(R.styleable.VItemView_item_lefttitle_textcolor
                , getResources().getColor(R.color.title_gray));
        item_righttitle_textcolor = a.getColor(R.styleable.VItemView_item_righttitle_textcolor
                , getResources().getColor(R.color.tips_gray));
        item_subrighttitle_textcolor = a.getColor(R.styleable.VItemView_item_subrighttitle_textcolor
                , getResources().getColor(R.color.tips_gray));
        item_sublefttitle_textcolor = a.getColor(R.styleable.VItemView_item_sublefttitle_textcolor
                , getResources().getColor(R.color.tips_gray));
        item_lefttitle = a.getString(R.styleable.VItemView_item_lefttitle);
        item_righttitle = a.getString(R.styleable.VItemView_item_righttitle);
        item_subrighttitle = a.getString(R.styleable.VItemView_item_subrighttitle);
        item_sublefttitle = a.getString(R.styleable.VItemView_item_sublefttitle);
        item_button = a.getString(R.styleable.VItemView_item_button);
        item_text_redot = a.getBoolean(R.styleable.VItemView_item_text_redot, false);
        item_img_redot = a.getBoolean(R.styleable.VItemView_item_img_redot, false);
        item_select = a.getBoolean(R.styleable.VItemView_item_select, false);
        item_check = a.getBoolean(R.styleable.VItemView_item_check, false);
        item_switch_check = a.getBoolean(R.styleable.VItemView_item_switch_check, false);
        item_arrow = a.getBoolean(R.styleable.VItemView_item_arrow, true);
        item_src = a.getResourceId(R.styleable.VItemView_item_src, 0);
        a.recycle();
    }

    void init() {
        layoutRightImage.setVisibility(GONE);
        layoutRightText.setVisibility(GONE);
        mButton.setVisibility(GONE);
        vSwitch.setVisibility(GONE);
        ivLeft.setVisibility(GONE);
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
                ivLeft.setImageResource(item_src);
                break;
            case 2://small_icon_right
                layoutRightImage.setVisibility(VISIBLE);
                FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) ivRight.getLayoutParams();
                layoutParams1.width = (int) getResources().getDimension(R.dimen.small_icon_size);
                layoutParams1.height = (int) getResources().getDimension(R.dimen.small_icon_size);
                ivRight.setLayoutParams(layoutParams1);
                llContent.setMinimumHeight((int) getResources().getDimension(R.dimen.itemview_small_icon));
                layoutRightImage.setVisibility(VISIBLE);
                ivRight.setImageResource(item_src);
                break;
            case 3://middle_icon_left
                ivLeft.setVisibility(VISIBLE);
                LinearLayout.LayoutParams layoutParams2 = (LayoutParams) ivLeft.getLayoutParams();
                layoutParams2.width = (int) getResources().getDimension(R.dimen.middle_icon_size);
                layoutParams2.height = (int) getResources().getDimension(R.dimen.middle_icon_size);
                ivLeft.setLayoutParams(layoutParams2);
                llContent.setMinimumHeight((int) getResources().getDimension(R.dimen.itemview_middle_icon));
                layoutRightText.setVisibility(VISIBLE);
                ivLeft.setImageResource(item_src);
                break;
            case 4://middle_icon_right
                layoutRightImage.setVisibility(VISIBLE);
                FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) ivRight.getLayoutParams();
                layoutParams3.width = (int) getResources().getDimension(R.dimen.middle_icon_size);
                layoutParams3.height = (int) getResources().getDimension(R.dimen.middle_icon_size);
                ivRight.setLayoutParams(layoutParams3);
                llContent.setMinimumHeight((int) getResources().getDimension(R.dimen.itemview_middle_icon));
                layoutRightImage.setVisibility(VISIBLE);
                ivRight.setImageResource(item_src);
                break;
            case 5://big_icon_left
                ivLeft.setVisibility(VISIBLE);
                LinearLayout.LayoutParams layoutParams4 = (LayoutParams) ivLeft.getLayoutParams();
                layoutParams4.width = (int) getResources().getDimension(R.dimen.big_icon_size);
                layoutParams4.height = (int) getResources().getDimension(R.dimen.big_icon_size);
                ivLeft.setLayoutParams(layoutParams4);
                llContent.setMinimumHeight((int) getResources().getDimension(R.dimen.itemview_big_icon));
                layoutRightText.setVisibility(VISIBLE);
                ivLeft.setImageResource(item_src);
                break;
            case 6://big_icon_right
                layoutRightImage.setVisibility(VISIBLE);
                FrameLayout.LayoutParams layoutParams5 = (FrameLayout.LayoutParams) ivRight.getLayoutParams();
                layoutParams5.width = (int) getResources().getDimension(R.dimen.big_icon_size);
                layoutParams5.height = (int) getResources().getDimension(R.dimen.big_icon_size);
                ivRight.setLayoutParams(layoutParams5);
                llContent.setMinimumHeight((int) getResources().getDimension(R.dimen.itemview_big_icon));
                layoutRightImage.setVisibility(VISIBLE);
                ivRight.setImageResource(item_src);
                break;
            case 7://select
                layoutRightImage.setVisibility(VISIBLE);
                item_arrow = false;
                ivRight.setImageResource(item_select ? R.mipmap.icon_check : 0);
                break;
            case 8://radio_check
                ivLeft.setVisibility(VISIBLE);
                item_arrow = false;
                ivLeft.setImageResource(item_check ? R.mipmap.icon_check_radio : R.mipmap.icon_uncheck_radio);
                break;
            case 9://switch
                vSwitch.setVisibility(VISIBLE);
                item_arrow = false;
                vSwitch.setChecked(item_switch_check);
                break;
            case 10://button
                item_arrow = false;
                mButton.setVisibility(VISIBLE);
                mButton.setText_content(item_button);
                break;
            case 11://row_title
                llContent.setMinimumHeight((int) getResources().getDimension(R.dimen.itemview_row_title));
                item_arrow = false;
                LinearLayout.LayoutParams layoutParams6 = (LayoutParams) llLefttextview.getLayoutParams();
                layoutParams6.topMargin = 0;
                layoutParams6.bottomMargin = 0;
                llLefttextview.setLayoutParams(layoutParams6);
                break;
        }
        tvLeftTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, item_lefttitle_textsize);
        tvRightTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, item_righttitle_textsize);
        tvLeftSubtitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, item_sublefttitle_textsize);
        tvRightSubtitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, item_subrighttitle_textsize);
        tvLeftTitle.setTextColor(item_lefttitle_textcolor);
        tvRightTitle.setTextColor(item_righttitle_textcolor);
        tvLeftSubtitle.setTextColor(item_sublefttitle_textcolor);
        tvRightSubtitle.setTextColor(item_subrighttitle_textcolor);
        tvLeftTitle.setText(item_lefttitle);
        tvRightTitle.setText(item_righttitle);
        tvLeftSubtitle.setText(item_sublefttitle);
        tvRightSubtitle.setText(item_subrighttitle);
        tvLeftSubtitle.setVisibility(TextUtils.isEmpty(item_sublefttitle) ? GONE : VISIBLE);
        tvRightSubtitle.setVisibility(TextUtils.isEmpty(item_subrighttitle) ? GONE : VISIBLE);
        ivTextReddot.setVisibility(item_text_redot ? VISIBLE : GONE);
        ivReddot.setVisibility(item_img_redot ? VISIBLE : GONE);
        ivArrow.setVisibility(item_arrow ? VISIBLE : GONE);
    }

    public void setItem_lefttitle_textsize(int item_lefttitle_textsize) {
        this.item_lefttitle_textsize = item_lefttitle_textsize;
        init();
    }

    public void setItem_righttitle_textsize(int item_righttitle_textsize) {
        this.item_righttitle_textsize = item_righttitle_textsize;
        init();
    }

    public void setItem_subrighttitle_textsize(int item_subrighttitle_textsize) {
        this.item_subrighttitle_textsize = item_subrighttitle_textsize;
    }

    public void setItem_sublefttitle_textsize(int item_sublefttitle_textsize) {
        this.item_sublefttitle_textsize = item_sublefttitle_textsize;
        init();
    }

    public void setItem_lefttitle_textcolor(int item_lefttitle_textcolor) {
        this.item_lefttitle_textcolor = item_lefttitle_textcolor;
        init();
    }

    public void setItem_righttitle_textcolor(int item_righttitle_textcolor) {
        this.item_righttitle_textcolor = item_righttitle_textcolor;
        init();
    }

    public void setItem_subrighttitle_textcolor(int item_subrighttitle_textcolor) {
        this.item_subrighttitle_textcolor = item_subrighttitle_textcolor;
    }

    public void setItem_sublefttitle_textcolor(int item_sublefttitle_textcolor) {
        this.item_sublefttitle_textcolor = item_sublefttitle_textcolor;
        init();
    }

    public void setItem_lefttitle(String item_lefttitle) {
        this.item_lefttitle = item_lefttitle;
        init();
    }

    public void setItem_righttitle(String item_righttitle) {
        this.item_righttitle = item_righttitle;
        init();
    }

    public void setItem_subrighttitle(String item_subrighttitle) {
        this.item_subrighttitle = item_subrighttitle;
        init();
    }

    public void setItem_sublefttitle(String item_sublefttitle) {
        this.item_sublefttitle = item_sublefttitle;
        init();
    }

    public void setItem_button(String item_button) {
        this.item_button = item_button;
        init();
    }

    public void setItem_text_redot(boolean item_text_redot) {
        this.item_text_redot = item_text_redot;
        init();
    }

    public void setItem_img_redot(boolean item_img_redot) {
        this.item_img_redot = item_img_redot;
        init();
    }

    public void setItem_select(boolean item_select) {
        this.item_select = item_select;
        init();
    }

    public void setItem_check(boolean item_check) {
        this.item_check = item_check;
        init();
    }

    public void setItem_switch_check(boolean item_switch_check) {
        this.item_switch_check = item_switch_check;
        init();
    }

    public void setItem_arrow(boolean item_arrow) {
        this.item_arrow = item_arrow;
        init();
    }

    public void setItem_src(int item_src) {
        this.item_src = item_src;
        init();
    }

    public void setStyle(int style) {
        this.style = style;
        init();
    }

    public int getStyle() {
        return style;
    }

    public int getItem_lefttitle_textsize() {
        return item_lefttitle_textsize;
    }

    public int getItem_righttitle_textsize() {
        return item_righttitle_textsize;
    }

    public int getItem_subrighttitle_textsize() {
        return item_subrighttitle_textsize;
    }

    public int getItem_sublefttitle_textsize() {
        return item_sublefttitle_textsize;
    }

    public int getItem_lefttitle_textcolor() {
        return item_lefttitle_textcolor;
    }

    public int getItem_righttitle_textcolor() {
        return item_righttitle_textcolor;
    }

    public int getItem_subrighttitle_textcolor() {
        return item_subrighttitle_textcolor;
    }

    public int getItem_sublefttitle_textcolor() {
        return item_sublefttitle_textcolor;
    }

    public String getItem_lefttitle() {
        return item_lefttitle;
    }

    public String getItem_righttitle() {
        return item_righttitle;
    }

    public String getItem_subrighttitle() {
        return item_subrighttitle;
    }

    public String getItem_sublefttitle() {
        return item_sublefttitle;
    }

    public String getItem_button() {
        return item_button;
    }

    public boolean isItem_text_redot() {
        return item_text_redot;
    }

    public boolean isItem_img_redot() {
        return item_img_redot;
    }

    public boolean isItem_select() {
        return item_select;
    }

    public boolean isItem_check() {
        return item_check;
    }

    public boolean isItem_switch_check() {
        return item_switch_check;
    }

    public boolean isItem_arrow() {
        return item_arrow;
    }

    public int getItem_src() {
        return item_src;
    }


}
