package com.viomi.vmui;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class VInputItem extends ConstraintLayout {
    public TextView tvDesc;
    public View layoutItem;
    public TextView tvTitle;
    public EditText edt;
    public ImageView ivClear;
    public ImageView ivVisibility;
    public View vDivider;
    boolean error;
    boolean passVisibility = false;
    boolean isPassword;
    InputMethodManager inputMethodManager;
    TextWatcher textWatcher;
    int lines;

    OnPasswordVisibilityChangeListener onPasswordVisibilityChangeListener;

    public VInputItem(Context context) {
        this(context, null);
    }

    public VInputItem(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VInputItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.inputview, this);
        tvDesc = findViewById(R.id.tv_desc);
        layoutItem = findViewById(R.id.layout_item);
        tvTitle = findViewById(R.id.title);
        edt = findViewById(R.id.edt);
        ivClear = findViewById(R.id.iv_clear);
        ivVisibility = findViewById(R.id.iv_input_visibility);
        vDivider = findViewById(R.id.v_divider);
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
                if (!TextUtils.isEmpty(text))
                    setError(false);
                ivClear.setVisibility(TextUtils.isEmpty(text)
                        || !edt.isFocused()
                        || lines > 1
                        ? GONE : VISIBLE);
                edt.setMinLines(edt.getLineCount());
                Log.d("VInputItem", "onTextChanged: " + edt.getLineCount());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (textWatcher != null)
                    textWatcher.afterTextChanged(s);
            }
        });
        edt.setOnClickListener(v -> {
            requestEditTextFocus();
        });
        edt.setOnFocusChangeListener((v, hasFocus) -> {
            Log.d("VInputItem", "OnFocusChangeListener: " + hasFocus);
            if (hasFocus)
                requestEditTextFocus();
            if (error)
                return;
            ivClear.setVisibility(TextUtils.isEmpty(edt.getText().toString())
                    || !hasFocus
                    || lines > 1
                    ? GONE : VISIBLE);
        });
        ivClear.setOnClickListener(v -> {
            if (error)
                return;
            edt.setText("");
        });
        ivVisibility.setOnClickListener(v -> {
            passVisibility = !passVisibility;
            setInputType();
            if (onPasswordVisibilityChangeListener != null)
                onPasswordVisibilityChangeListener.OnPasswordVisibilityChange(passVisibility);
        });
    }

    void setInputType() {
        edt.post(new Runnable() {
            @Override
            public void run() {
                edt.setInputType(passVisibility
                        ? InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                edt.setSelection(edt.getText().toString().length());
            }
        });
        ivVisibility.setImageResource(passVisibility ? R.mipmap.icon_input_visibility : R.mipmap.icon_input_invisibility);
    }

    public void setFilters(InputFilter[] filters){
        edt.setFilters(filters);
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
                R.styleable.VInputItem);
        isPassword = a.getBoolean(R.styleable.VInputItem_password, false);
        setPassword(isPassword);
        if (isPassword)
            setInputType();
        int pos = edt.getSelectionStart();
        edt.setSelection(pos);
        String title = a.getString(R.styleable.VInputItem_title);
        tvTitle.setVisibility(TextUtils.isEmpty(title) ? GONE : VISIBLE);
        tvTitle.setText(title);
        edt.setHint(a.getString(R.styleable.VInputItem_hint));
        int lines = a.getInteger(R.styleable.VInputItem_lines, 0);
        setLines(lines);
        int maxLength = a.getInteger(R.styleable.VInputItem_android_maxLength,0);
        setMaxLength(maxLength);
        int gravity = a.getInteger(R.styleable.VInputItem_desc_gravity, Gravity.TOP);
        setDescGravity(gravity);
        String desc = a.getString(R.styleable.VInputItem_desc);
        setDesc(desc);
        setDivider(a.getBoolean(R.styleable.VInputItem_input_divider, false));
        a.recycle();
    }

    public void setPassword(boolean password) {
        isPassword = password;
        ivVisibility.setVisibility(password ? VISIBLE : GONE);
    }

    public void setOnTextWatcher(TextWatcher textWatcher) {
        this.textWatcher = textWatcher;
    }

    public void setDesc(String desc) {
        tvDesc.setVisibility(TextUtils.isEmpty(desc) ? GONE : VISIBLE);
        tvDesc.setText(desc);
    }

    /**
     * @param gravity Gravity.TOP||Gravity.BOTTOM
     */
    public void setDescGravity(int gravity) {
        ConstraintLayout.LayoutParams layoutParams = (LayoutParams) layoutItem.getLayoutParams();
        ConstraintLayout.LayoutParams layoutParams1 = (LayoutParams) tvDesc.getLayoutParams();
        int paddingtop = tvDesc.getPaddingTop();
        int paddingbottom = tvDesc.getPaddingBottom();
        if (gravity == Gravity.TOP) {
            layoutParams.topToTop = -1;
            layoutParams.topToBottom = tvDesc.getId();

            layoutParams1.topToTop = ConstraintSet.PARENT_ID;
            layoutParams1.topToBottom = -1;

            if (paddingbottom > paddingtop) {
                tvDesc.setPadding(tvDesc.getPaddingLeft(), paddingbottom, tvDesc.getPaddingRight(), paddingtop);
            }
        } else {
            layoutParams.topToTop = ConstraintSet.PARENT_ID;
            layoutParams.topToBottom = -1;

            layoutParams1.topToTop = -1;
            layoutParams1.topToBottom = layoutItem.getId();

            if (paddingbottom < paddingtop) {
                tvDesc.setPadding(tvDesc.getPaddingLeft(), paddingbottom, tvDesc.getPaddingRight(), paddingtop);
            }
        }
        layoutItem.setLayoutParams(layoutParams);
        tvDesc.setLayoutParams(layoutParams1);
    }

    public void setLines(int lines) {
        this.lines = lines <= 0 ? Integer.MAX_VALUE : lines;
        edt.setSingleLine(this.lines == 1);
        edt.setLines(this.lines);
        edt.setMaxLines(this.lines);
    }

    public void setMaxLength(int maxLength){
        if(maxLength > 0)
            edt.setFilters( new InputFilter[]{ new InputFilter.LengthFilter( maxLength )});
        else
            edt.setFilters( new InputFilter[]{});


    }

    public void setHint(String hint) {
        edt.setHint(hint);
    }

    public void setTitle(String text) {
        tvTitle.setVisibility(TextUtils.isEmpty(text) ? GONE : VISIBLE);
        tvTitle.setText(text);
    }

    public void setText(String text) {
        edt.setText(text);
        if (!TextUtils.isEmpty(text)) {
            edt.setSelection(text.length());
        }
    }

    public String getText() {
        return edt.getText().toString();
    }

    public void setDivider(boolean visible) {
        vDivider.setVisibility(visible ? VISIBLE : GONE);
    }

    public void setError(boolean error) {
        this.error = error;
        tvTitle.setTextColor(error ? getResources().getColor(R.color.theme_red) :
                getResources().getColor(R.color.title_gray));
        if (error) {
            ivClear.setVisibility(VISIBLE);
            ivClear.setImageResource(R.mipmap.icon_lists_error);
        } else ivClear.setImageResource(R.mipmap.icon_input_clear);
    }

    public interface OnPasswordVisibilityChangeListener {
        void OnPasswordVisibilityChange(boolean passwordVisibility);
    }

    public void setOnPasswordVisibilityChangeListener(OnPasswordVisibilityChangeListener onPasswordVisibilityChangeListener) {
        this.onPasswordVisibilityChangeListener = onPasswordVisibilityChangeListener;
    }
}
