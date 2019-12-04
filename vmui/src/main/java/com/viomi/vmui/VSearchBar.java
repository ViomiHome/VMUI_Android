package com.viomi.vmui;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.lang.reflect.Field;

public class VSearchBar extends ConstraintLayout {
    final String TAG = "VSearchBar";
    public ImageView ivSearch, ivClear;
    public TextView tvCancel;
    public EditText edtSearch;
    public RelativeLayout layoutEdit;
    InputMethodManager inputMethodManager;

    OnSearchListner listner;

    public VSearchBar(Context context) {
        this(context, null);
    }

    public VSearchBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VSearchBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.v_edittext, this);
        ivSearch = findViewById(R.id.iv_search);
        ivClear = findViewById(R.id.iv_clear);
        tvCancel = findViewById(R.id.tv_cancel);
        edtSearch = findViewById(R.id.edt);
        layoutEdit = findViewById(R.id.layout_edit);
        initAttrs(attrs);
        init();
    }

    void requestEditTextFocus() {
        edtSearch.requestFocus();
        edtSearch.setCursorVisible(true);
        inputMethodManager.showSoftInput(edtSearch, 0);
    }

    void clearEditTextFocus() {
        edtSearch.clearFocus();
        edtSearch.setCursorVisible(false);
        inputMethodManager.hideSoftInputFromWindow(edtSearch.getWindowToken(), 0);
    }

    private void initAttrs(AttributeSet attrs) {
        if (attrs == null)
            return;
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.VSearchBar);
        String textCancel = a.getString(R.styleable.VSearchBar_search_cancel_text);
        if (!TextUtils.isEmpty(textCancel))
            tvCancel.setText(textCancel);
        tvCancel.setTextColor(a.getColor(R.styleable.VSearchBar_search_cancel_color, getResources().getColor(R.color.title_gray)));
        tvCancel.setTextSize(TypedValue.COMPLEX_UNIT_PX, a.getDimension(R.styleable.VSearchBar_search_cancel_textsize
                , TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 13, getResources().getDisplayMetrics())));
        edtSearch.setTextColor(a.getColor(R.styleable.VSearchBar_search_text_color, getResources().getColor(R.color.title_gray)));
        edtSearch.setTextSize(TypedValue.COMPLEX_UNIT_PX, a.getDimension(R.styleable.VSearchBar_search_textsize
                , TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 13, getResources().getDisplayMetrics())));
        edtSearch.setHint(a.getString(R.styleable.VSearchBar_search_hint));
        edtSearch.setHintTextColor(a.getColor(R.styleable.VSearchBar_search_hint_color, getResources().getColor(R.color.tips_gray)));
        int drawable = a.getResourceId(R.styleable.VSearchBar_search_textcursor_drawable, 0);
        if (drawable != 0) {
            try {
                Field setCursor = TextView.class.getDeclaredField("mCursorDrawableRes");
                setCursor.setAccessible(true);
                setCursor.set(edtSearch, drawable);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        a.recycle();
    }


    private void init() {
        inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        ivClear.setVisibility(GONE);
        tvCancel.setVisibility(GONE);
        edtSearch.setCursorVisible(false);
        edtSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        ivSearch.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (ivSearch.getWidth() == 0)
                    return;
//                animateToCenter();
                ivSearch.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        edtSearch.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.v(TAG, "onFocusChange:" + hasFocus);
            }
        });
        edtSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tvCancel.setVisibility(View.VISIBLE);
                animateToLeft();
                requestEditTextFocus();
            }
        });
        layoutEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tvCancel.setVisibility(View.VISIBLE);
                animateToLeft();
                requestEditTextFocus();
            }
        });
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    ivClear.setVisibility(GONE);
                } else ivClear.setVisibility(VISIBLE);
                if (listner != null)
                    listner.onTextChanged(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ivClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearch.setText("");
            }
        });
        tvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearch.setText("");
                tvCancel.setVisibility(View.GONE);
                tvCancel.post(new Runnable() {
                    @Override
                    public void run() {
                        clearEditTextFocus();
                        animateToCenter();
                    }
                });
                if (listner != null)
                    listner.onCancelClick();
            }
        });
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.v(TAG, "IME_ACTION_SEARCH:" + v.getText().toString());
                    if (listner != null)
                        listner.onSearchClick(v.getText().toString());
                }
                return false;
            }
        });
    }

    public void clearInput() {
//        tvCancel.performClick();
        edtSearch.setText("");
        tvCancel.setVisibility(View.GONE);
        tvCancel.post(new Runnable() {
            @Override
            public void run() {
                clearEditTextFocus();
                animateToCenter();
            }
        });
    }

    public void requestInput() {
        edtSearch.performClick();
    }

    public interface OnSearchListner {
        void onTextChanged(CharSequence s);

        void onCancelClick();

        void onSearchClick(String text);
    }

    public void setHint(String hint) {
        edtSearch.setHint(hint);
    }

    public void setCursorDrawable(int drawable) {
        if (drawable != 0) {
            try {
                Field setCursor = TextView.class.getDeclaredField("mCursorDrawableRes");
                setCursor.setAccessible(true);
                setCursor.set(edtSearch, drawable);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    ValueAnimator animator;

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            clearEditTextFocus();
            int contentWidth = edtSearch.getRight() - ivSearch.getLeft();
            int leftMarginTarget = (layoutEdit.getWidth() - contentWidth) >> 1;
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivSearch.getLayoutParams();
            layoutParams.leftMargin = leftMarginTarget;
            ivSearch.setLayoutParams(layoutParams);
        }
    }

    void animateToCenter() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivSearch.getLayoutParams();
        int leftMargin = layoutParams.leftMargin;
        int contentWidth = edtSearch.getRight() - ivSearch.getLeft();
        int leftMarginTarget = (layoutEdit.getWidth() - contentWidth) >> 1;
        animator = ValueAnimator.ofInt(leftMargin, leftMarginTarget);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivSearch.getLayoutParams();
                layoutParams.leftMargin = (int) animation.getAnimatedValue();
                ivSearch.setLayoutParams(layoutParams);
            }
        });
        animator.setDuration(200);
        animator.start();
    }

    void animateToLeft() {
        int leftMargin = ((RelativeLayout.LayoutParams) ivSearch.getLayoutParams()).leftMargin;
        int leftMarginTarget = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 11, getResources().getDisplayMetrics());

        animator = ValueAnimator.ofInt(leftMargin, leftMarginTarget);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivSearch.getLayoutParams();
                layoutParams.leftMargin = (int) animation.getAnimatedValue();
                ivSearch.setLayoutParams(layoutParams);
            }
        });
        animator.setDuration(200);
        animator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        if (animator != null)
            animator.cancel();
        clearEditTextFocus();
        super.onDetachedFromWindow();
    }

    public OnSearchListner getListner() {
        return listner;
    }

    public void setListner(OnSearchListner listner) {
        this.listner = listner;
    }
}
