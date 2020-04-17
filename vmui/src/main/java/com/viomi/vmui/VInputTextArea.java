package com.viomi.vmui;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class VInputTextArea extends LinearLayout {
    public EditText edt;
    public TextView tvCount;
    InputMethodManager inputMethodManager;
    TextWatcher textWatcher;
    boolean showCount;
    int maxcount;
    int curCount;

    public VInputTextArea(Context context) {
        this(context, null);
    }

    public VInputTextArea(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VInputTextArea(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.input_text_area, this);
        edt = findViewById(R.id.edt);
        tvCount = findViewById(R.id.tv_count);
        inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        initAttrs(attrs);
        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (textWatcher != null)
                    textWatcher.beforeTextChanged(s, start, count, after);
            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                if (textWatcher != null)
                    textWatcher.onTextChanged(text, start, before, count);
                edt.setMinLines(edt.getLineCount());
                curCount = text.length();
                tvCount.setText(curCount + "/" + maxcount);
                if (curCount > maxcount) {
                    edt.setText(text.subSequence(0, maxcount));
                    edt.setSelection(maxcount);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (textWatcher != null)
                    textWatcher.afterTextChanged(s);
            }
        });
        edt.setOnClickListener(v -> requestEditTextFocus());
        edt.setOnFocusChangeListener((v, hasFocus) -> {
            Log.d("VInputItem", "OnFocusChangeListener: " + hasFocus);
            if (hasFocus)
                requestEditTextFocus();
        });
    }


    private void requestEditTextFocus() {
        edt.requestFocus();
        edt.setCursorVisible(true);
        inputMethodManager.showSoftInput(edt, 0);
    }

    private void clearEditTextFocus() {
        edt.clearFocus();
        edt.setCursorVisible(false);
        inputMethodManager.hideSoftInputFromWindow(edt.getWindowToken(), 0);
    }

    private void initAttrs(AttributeSet attrs) {
        if (attrs == null)
            return;
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.VInputTextArea);
        setHint(a.getString(R.styleable.VInputTextArea_hint_text));
        showCount = a.getBoolean(R.styleable.VInputTextArea_show_count, true);
        showCount(showCount);
        maxcount = a.getInteger(R.styleable.VInputTextArea_max_count, 100);
        setMaxcount(maxcount);
        a.recycle();
    }

    public void showCount(boolean show) {
        showCount = show;
        tvCount.setVisibility(showCount ? VISIBLE : GONE);
        edt.setPadding(edt.getPaddingLeft(), edt.getPaddingTop(), edt.getPaddingRight(), showCount ? 0 : edt.getPaddingTop());
    }

    public void setMaxcount(int maxcount) {
        this.maxcount = maxcount;
        curCount = edt.getText().toString().length();
        if (curCount > maxcount) {
            curCount = maxcount;
            edt.setText(edt.getText().toString().subSequence(0, maxcount));
            edt.setSelection(maxcount);
            edt.setMinLines(edt.getLineCount());
        }
        tvCount.setText(curCount + "/" + maxcount);
    }

    public void setOnTextWatcher(TextWatcher textWatcher) {
        this.textWatcher = textWatcher;
    }


    public void setText(String text) {
        edt.setText(text);
        if (!TextUtils.isEmpty(text)) {
            edt.setSelection(text.length());
        }
    }

    public void setHint(String hint) {
        edt.setHint(hint);
    }

    public String getText() {
        return edt.getText().toString();
    }

}
