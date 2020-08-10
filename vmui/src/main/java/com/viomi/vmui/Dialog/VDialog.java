package com.viomi.vmui.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.TransformationMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.viomi.vmui.R;
import com.viomi.vmui.VTextView;
import com.viomi.vmui.adapter.ListAdapter;
import com.viomi.vmui.entity.VLocationEntity;
import com.viomi.vmui.utils.VDisplayHelper;
import com.viomi.vmui.utils.VDateFormatUtils;
import com.viomi.vmui.utils.VResHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class VDialog extends Dialog {
    private View mContentView;
    private boolean mIsAnimating = false;
    private int mAnimationDuration = 200;
    private boolean mCancelable = false;
    private boolean mCanceledOnTouchOutside = false;
    private boolean mCanceledOnTouchOutsideSet = false;

    public VDialog(@NonNull Context context) {
        super(context, R.style.VMUI_Dialog);
    }

    public VDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    protected VDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private void init() {
        setCancelable(mCancelable);
        setCanceledOnTouchOutside(mCanceledOnTouchOutside);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDialog();
    }

    private void initDialog() {
        //noinspection ConstantConditions
        getWindow().getDecorView().setPadding(0, 0, 0, 0);

        // 在底部，宽度撑满
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM | Gravity.CENTER;

        int screenWidth = VDisplayHelper.getScreenWidth(getContext());
        int screenHeight = VDisplayHelper.getScreenHeight(getContext());
        params.width = screenWidth < screenHeight ? screenWidth : screenHeight;
        getWindow().setAttributes(params);
        setCanceledOnTouchOutside(mCanceledOnTouchOutside);
    }

    @Override
    public void setContentView(int layoutResID) {
        mContentView = LayoutInflater.from(getContext()).inflate(layoutResID, null);
        super.setContentView(layoutResID);
    }

    @Override
    public void setContentView(@NonNull View view, @Nullable ViewGroup.LayoutParams params) {
        mContentView = view;
        super.setContentView(view, params);
    }

    @Override
    public void addContentView(@NonNull View view, @Nullable ViewGroup.LayoutParams params) {
        mContentView = view;
        super.addContentView(view, params);
    }

    @Override
    public void setCancelable(boolean flag) {
        super.setCancelable(flag);
        this.mCancelable = flag;
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
        if (cancel && !mCancelable) {
            mCancelable = true;
        }
        mCanceledOnTouchOutside = cancel;
        mCanceledOnTouchOutsideSet = true;
    }

    /**
     * BottomSheet升起动画
     */
    private void animateUp() {
        if (mContentView == null) {
            return;
        }
        TranslateAnimation translate = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f
        );
        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(translate);
        set.addAnimation(alpha);
        set.setInterpolator(new DecelerateInterpolator());
        set.setDuration(mAnimationDuration);
        set.setFillAfter(true);
        mContentView.startAnimation(set);
    }

    /**
     * BottomSheet降下动画
     */
    private void animateDown() {
        if (mContentView == null) {
            return;
        }
        final Runnable dismissTask = new Runnable() {
            @Override
            public void run() {
                // java.lang.IllegalArgumentException: View=com.android.internal.policy.PhoneWindow$DecorView{22dbf5b V.E...... R......D 0,0-1080,1083} not attached to window manager
                // 在dismiss的时候可能已经detach了，简单try-catch一下
                try {
                    VDialog.super.dismiss();
                } catch (Exception e) {

                }
            }
        };
        if (mContentView.getHeight() == 0) {
            // TranslateAnimation will not call onAnimationEnd if its height is 0.
            // At this case, we run dismiss task immediately.
            dismissTask.run();
            return;
        }
        TranslateAnimation translate = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f
        );
        AlphaAnimation alpha = new AlphaAnimation(1, 0);
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(translate);
        set.addAnimation(alpha);
        set.setInterpolator(new DecelerateInterpolator());
        set.setDuration(mAnimationDuration);
        set.setFillAfter(true);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mIsAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mIsAnimating = false;
                /**
                 * Bugfix： Attempting to destroy the window while drawing!
                 */
                mContentView.post(dismissTask);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mContentView.startAnimation(set);
    }

    @Override
    public void show() {
        super.show();
        animateUp();
    }

    @Override
    public void dismiss() {
        if (mIsAnimating) {
            return;
        }
        animateDown();
    }

    boolean shouldWindowCloseOnTouchOutside() {
        if (!mCanceledOnTouchOutsideSet) {
            if (Build.VERSION.SDK_INT < 11) {
                mCanceledOnTouchOutside = true;
            } else {
                TypedArray a = getContext().obtainStyledAttributes(
                        new int[]{android.R.attr.windowCloseOnTouchOutside});
                mCanceledOnTouchOutside = a.getBoolean(0, true);
                a.recycle();
            }
            mCanceledOnTouchOutsideSet = true;
        }
        return mCanceledOnTouchOutside;
    }

    public void cancelOutSide() {
        if (mCancelable && isShowing() && shouldWindowCloseOnTouchOutside()) {
            cancel();
        }
    }

    public static class MessageDialogBuilder extends VDialogBuilder<MessageDialogBuilder> {
        protected CharSequence mMessage;
        private VTextView mTextView;

        public MessageDialogBuilder(Context context) {
            super(context);
        }

        /**
         * 设置对话框的消息文本
         */
        public MessageDialogBuilder setMessage(CharSequence message) {
            this.mMessage = message;
            return this;
        }

        /**
         * 设置对话框的消息文本
         */
        public MessageDialogBuilder setMessage(int resId) {
            return setMessage(getBaseContext().getResources().getString(resId));
        }

        @Override
        protected void onCreateContent(Dialog dialog, ViewGroup parent, Context context) {
            if (mMessage != null && mMessage.length() != 0) {
                RelativeLayout rtl = new RelativeLayout(context);
                RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                int lr = VDisplayHelper.dp2px(context, 36);
                int top = VDisplayHelper.dp2px(context, 10);
                int bottom = VDisplayHelper.dp2px(context, 28);
                lp2.addRule(RelativeLayout.CENTER_HORIZONTAL);
                mTextView = new VTextView(context, true);
                mTextView.setEnabled(false);
                mTextView.setText(mMessage);
                mTextView.setTextColor(context.getResources().getColor(R.color.content_gray));
                //Linkify.addLinks(mTextView, Linkify.ALL);
                mTextView.setLinksClickable(true);
                mTextView.setLinkTextColor(context.getResources().getColor(R.color.text_green));
                if (hasTitle()) {
                    lp2.setMargins(lr, top, lr, bottom);
                    mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                } else if (hasHeadImage()) {
                    lp2.setMargins(lr, top, lr, bottom);
                    mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                } else {
                    lp2.setMargins(lr, bottom, lr, bottom);
                    mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                }
                mTextView.setLayoutParams(lp2);
                rtl.setLayoutParams(lp1);
                rtl.addView(mTextView);
                parent.addView(rtl);
            }
        }

        public static void assignMessageTvWithAttr(Context context, TextView messageTv, boolean hasTitle, int defAttr) {
            VResHelper.assignTextViewWithAttr(messageTv, defAttr);

            if (!hasTitle) {
                TypedArray a = messageTv.getContext().obtainStyledAttributes(null,
                        R.styleable.VMUIDialogMessageTvCustomDef, defAttr, 0);
                int count = a.getIndexCount();
                for (int i = 0; i < count; i++) {
                    int attr = a.getIndex(i);
                    if (attr == R.styleable.VMUIDialogMessageTvCustomDef_dialog_paddingTopWhenNotTitle) {
                        messageTv.setPadding(
                                messageTv.getPaddingLeft(),
                                a.getDimensionPixelSize(attr, messageTv.getPaddingTop()),
                                messageTv.getPaddingRight(),
                                messageTv.getPaddingBottom()
                        );
                    }
                }
                a.recycle();
            }
        }
    }

    public static class EditTextDialogBuilder extends VDialogBuilder<EditTextDialogBuilder> implements View.OnClickListener {

        private String mPlaceholder;
        private String mTipsText;
        private EditText mEditText;
        private TextView mTxtTips;
        private TransformationMethod mTransformationMethod;
        private RelativeLayout mMainLayout;
        private ImageView mRightImageView;
        private int mInputType = InputType.TYPE_CLASS_TEXT;
        private boolean mShowTipsText = false;

        public EditTextDialogBuilder(Context context) {
            super(context);
        }

        public EditTextDialogBuilder setShowTipsText(boolean value) {
            mShowTipsText = value;
            return this;
        }

        public EditTextDialogBuilder setPlaceholder(String placeholder) {
            this.mPlaceholder = placeholder;
            return this;
        }

        public EditTextDialogBuilder setPlaceholder(int resId) {
            return setPlaceholder(getBaseContext().getResources().getString(resId));
        }

        public EditTextDialogBuilder setTipsText(String text) {
            this.mTipsText = text;
            return this;
        }

        /**
         * 设置 EditText 的 inputType
         */
        public EditTextDialogBuilder setInputType(int inputType) {
            mInputType = inputType;
            return this;
        }

        /**
         * 设置密码不可见（new PasswordTransformationMethod()）
         *
         * @param method
         * @return
         */
        public EditTextDialogBuilder setTransformationMethod(TransformationMethod method) {
            mTransformationMethod = method;
            return this;
        }

        protected RelativeLayout.LayoutParams createEditTextLayoutParams() {
            RelativeLayout.LayoutParams editLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            editLp.addRule(RelativeLayout.LEFT_OF, mRightImageView.getId());
            editLp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            return editLp;
        }

        protected RelativeLayout.LayoutParams createRightIconLayoutParams() {
            RelativeLayout.LayoutParams rightIconLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rightIconLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            rightIconLp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            rightIconLp.leftMargin = VDisplayHelper.dpToPx(5);
            return rightIconLp;
        }

        public EditText getEditText() {
            return mEditText;
        }

        @Override
        protected void onCreateContent(Dialog dialog, ViewGroup parent, Context context) {
            mEditText = new AppCompatEditText(context);
            mTxtTips = new VTextView(context);
            mTxtTips.setTextColor(context.getResources().getColor(R.color.tips_gray));
            mTxtTips.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            mTxtTips.setText(mTipsText);
            MessageDialogBuilder.assignMessageTvWithAttr(context, mEditText, hasTitle(), R.attr.dialog_edit_content_style);
            mEditText.setFocusable(true);
            mEditText.setFocusableInTouchMode(true);
            mEditText.setImeOptions(EditorInfo.IME_ACTION_GO);
            mEditText.setId(R.id.vmui_dialog_edit_input);
            mEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() > 0) {
                        mRightImageView.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            mRightImageView = new ImageView(context);
            mRightImageView.setId(R.id.vmui_dialog_edit_right_icon);
            mRightImageView.setImageResource(R.mipmap.icon_clear);
            mRightImageView.setVisibility(View.GONE);
            mRightImageView.setOnClickListener(this);

            mMainLayout = new RelativeLayout(context);
            mMainLayout.setBackgroundResource(R.drawable.vmui_edittext_bg_border_bottom);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            if (mTransformationMethod != null) {
                mEditText.setTransformationMethod(mTransformationMethod);
            } else {
                mEditText.setInputType(mInputType);
            }

            lp.leftMargin = mEditText.getPaddingLeft();
            lp.rightMargin = mEditText.getPaddingRight();
            lp.topMargin = mEditText.getPaddingTop();
            if (mShowTipsText) {
                lp1.leftMargin = mEditText.getPaddingLeft();
                lp1.rightMargin = mEditText.getPaddingRight();
                lp1.topMargin = VDisplayHelper.dp2px(context, 8);
                lp1.bottomMargin = mEditText.getPaddingBottom();
                mTxtTips.setLayoutParams(lp1);
            } else {
                lp.bottomMargin = mEditText.getPaddingBottom();
            }
            mEditText.setBackgroundResource(0);
            mEditText.setPadding(0, 0, 0, VDisplayHelper.dp2px(context, 15));
            mRightImageView.setPadding(0, 0, 0, VDisplayHelper.dp2px(context, 15));
            RelativeLayout.LayoutParams editLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            editLp.addRule(RelativeLayout.LEFT_OF, mRightImageView.getId());
            editLp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            if (mPlaceholder != null) {
                mEditText.setHint(mPlaceholder);
            }
            mMainLayout.setLayoutParams(lp);
            mMainLayout.addView(mEditText, createEditTextLayoutParams());
            mMainLayout.addView(mRightImageView, createRightIconLayoutParams());

            parent.addView(mMainLayout);
            if (mShowTipsText) {
                parent.addView(mTxtTips);
            }
        }

        @Override
        protected void onAfter(Dialog dialog, LinearLayout parent, Context context) {
            super.onAfter(dialog, parent, context);
            final InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            dialog.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    inputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
                }
            });
            mEditText.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mEditText.requestFocus();
                    inputMethodManager.showSoftInput(mEditText, 0);
                }
            }, 300);
        }

        @Override
        public void onClick(View v) {
            mEditText.getText().clear();
            mRightImageView.setVisibility(View.GONE);
        }
    }

    public static class EditAuthTextDialogBuilder extends VDialogBuilder<EditAuthTextDialogBuilder> implements View.OnClickListener {

        private String mPlaceholder;
        private EditText mEditText;
        private TextView mTxtTips;
        private String mTipsText;

        private TransformationMethod mTransformationMethod;
        private RelativeLayout mMainLayout;
        private ImageView mAuthCodeView;
        private ImageView mRefreshCodeView;
        private int mInputType = InputType.TYPE_CLASS_TEXT;
        private boolean mShowTipsText = false;
        private onRefreshClickListener mRefreshClickListener;

        public EditAuthTextDialogBuilder(Context context) {
            super(context);
        }

        public EditAuthTextDialogBuilder setShowTipsText(boolean value) {
            mShowTipsText = value;
            return this;
        }

        public EditAuthTextDialogBuilder setTipsText(String text) {
            this.mTipsText = text;
            return this;
        }

        public EditAuthTextDialogBuilder setAuthCodeView(ImageView imageView) {
            this.mAuthCodeView = imageView;
            return this;
        }

        public EditAuthTextDialogBuilder setPlaceholder(String placeholder) {
            this.mPlaceholder = placeholder;
            return this;
        }

        public EditAuthTextDialogBuilder setPlaceholder(int resId) {
            return setPlaceholder(getBaseContext().getResources().getString(resId));
        }

        /**
         * 设置 EditText 的 inputType
         */
        public EditAuthTextDialogBuilder setInputType(int inputType) {
            mInputType = inputType;
            return this;
        }

        /**
         * 设置密码不可见（new PasswordTransformationMethod()）
         *
         * @param method
         * @return
         */
        public EditAuthTextDialogBuilder setTransformationMethod(TransformationMethod method) {
            mTransformationMethod = method;
            return this;
        }

        public EditAuthTextDialogBuilder setRefreshClickListener(onRefreshClickListener listener) {
            this.mRefreshClickListener = listener;
            return this;
        }

        protected RelativeLayout.LayoutParams createEditTextLayoutParams() {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.LEFT_OF, mRefreshCodeView.getId());
            lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            return lp;
        }

        protected RelativeLayout.LayoutParams createRefreshIconLayoutParams(Context context) {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            lp.addRule(RelativeLayout.LEFT_OF, mAuthCodeView.getId());
            lp.width = VDisplayHelper.dp2px(context, 15);
            lp.height = VDisplayHelper.dp2px(context, 15);
            lp.leftMargin = VDisplayHelper.dpToPx(5);
            return lp;
        }

        protected RelativeLayout.LayoutParams createAuthCodeIconLayoutParams(Context context) {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.width = VDisplayHelper.dp2px(context, 82);
            lp.height = VDisplayHelper.dp2px(context, 34);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            lp.leftMargin = VDisplayHelper.dpToPx(8);
            return lp;
        }

        public EditText getEditText() {
            return mEditText;
        }

        @Override
        protected void onCreateContent(Dialog dialog, ViewGroup parent, Context context) {
            mEditText = new AppCompatEditText(context);
            mTxtTips = new VTextView(context);
            mTxtTips.setTextColor(context.getResources().getColor(R.color.tips_gray));
            mTxtTips.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            mTxtTips.setText(mTipsText);
            MessageDialogBuilder.assignMessageTvWithAttr(context, mEditText, hasTitle(), R.attr.dialog_edit_content_style);
            mEditText.setFocusable(true);
            mEditText.setFocusableInTouchMode(true);
            mEditText.setImeOptions(EditorInfo.IME_ACTION_GO);
            mEditText.setId(R.id.vmui_dialog_edit_input);
            mEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() > 0) {

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            if (mAuthCodeView == null) {
                mAuthCodeView = new ImageView(context);
            }
            mAuthCodeView.setId(R.id.vmui_dialog_auth_code_icon);

            mRefreshCodeView = new ImageView(context);
            mRefreshCodeView.setId(R.id.vmui_dialog_refresh_icon);
            mRefreshCodeView.setImageResource(R.mipmap.icon_tips_refresh);


            mRefreshCodeView.setOnClickListener(this);

            mMainLayout = new RelativeLayout(context);
            mMainLayout.setBackgroundResource(R.drawable.vmui_edittext_bg_border_bottom);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, VDisplayHelper.dp2px(context, 50));
            LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            if (mTransformationMethod != null) {
                mEditText.setTransformationMethod(mTransformationMethod);
            } else {
                mEditText.setInputType(mInputType);
            }

            lp.leftMargin = mEditText.getPaddingLeft();
            lp.rightMargin = mEditText.getPaddingRight();
            lp.topMargin = mEditText.getPaddingTop();
            if (mShowTipsText) {
                lp1.leftMargin = mEditText.getPaddingLeft();
                lp1.rightMargin = mEditText.getPaddingRight();
                lp1.topMargin = VDisplayHelper.dp2px(context, 8);
                lp1.bottomMargin = mEditText.getPaddingBottom();
                mTxtTips.setLayoutParams(lp1);
            } else {
                lp.bottomMargin = mEditText.getPaddingBottom();
            }
            mEditText.setBackgroundResource(0);
            mEditText.setPadding(0, 0, 0, 0);
            if (mPlaceholder != null) {
                mEditText.setHint(mPlaceholder);
            }
            mMainLayout.setLayoutParams(lp);
            mMainLayout.addView(mEditText, createEditTextLayoutParams());
            mMainLayout.addView(mRefreshCodeView, createRefreshIconLayoutParams(context));
            mMainLayout.addView(mAuthCodeView, createAuthCodeIconLayoutParams(context));

            parent.addView(mMainLayout);
            if (mShowTipsText) {
                parent.addView(mTxtTips);
            }
        }

        @Override
        protected void onAfter(Dialog dialog, LinearLayout parent, Context context) {
            super.onAfter(dialog, parent, context);
            final InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            dialog.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    inputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
                }
            });
            mEditText.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mEditText.requestFocus();
                    inputMethodManager.showSoftInput(mEditText, 0);
                }
            }, 300);
        }

        @Override
        public void onClick(View v) {
            mEditText.getText().clear();
            if (mRefreshClickListener != null) mRefreshClickListener.onRefreshViewClick();
        }

        public interface onRefreshClickListener {
            void onRefreshViewClick();
        }
    }

    public static class SingleCheckableDialogBuilder extends VDialogBuilder<SingleCheckableDialogBuilder> {
        private PickerView mPickerView;
        private List<String> mData = new ArrayList<>();
        private String mSelected;

        public SingleCheckableDialogBuilder(Context context) {
            super(context);
        }

        public SingleCheckableDialogBuilder setData(List<String> list) {
            mData = list;
            return this;
        }

        public String getSelected() {
            return mSelected;
        }

        public PickerView getPickerView() {
            return mPickerView;
        }

        @Override
        protected void onCreateTitle(VDialog dialog, ViewGroup parent, Context context) {
            super.onCreateTitle(dialog, parent, context);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) this.mTitleView.getLayoutParams();
            int top = VDisplayHelper.dp2px(context, 22);
            lp.setMargins(0, top, 0, top);
            this.mTitleView.setLayoutParams(lp);
            this.mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        }

        @Override
        protected void onCreateContent(Dialog dialog, ViewGroup parent, Context context) {
            RelativeLayout view = new RelativeLayout(context);
            View foreground = new View(context);
            mPickerView = new PickerView(context);
            mPickerView.setDataList(mData);
            view.setBackground(getBaseContext().getResources().getDrawable(R.drawable.dialog_picker_bg));
            RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, VDisplayHelper.dp2px(mContext, 206));
            RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, VDisplayHelper.dp2px(mContext, 206));

            mPickerView.setLayoutParams(lp1);
            view.setLayoutParams(lp2);
            mPickerView.setOnSelectListener(new PickerView.OnSelectListener() {
                @Override
                public void onSelect(View view, String selected) {
                    mSelected = selected;
                }
            });
            mPickerView.setSelected(0);
            view.addView(mPickerView);
            view.addView(foreground);
            parent.addView(view);
        }

    }

    public static class MultiCheckableDialogBuilder extends VDialogBuilder<MultiCheckableDialogBuilder> {
        private ListView mListView;
        private ListAdapter mAdapter;
        private List<String> mData = new ArrayList<>();
        private AdapterView.OnItemClickListener onItemClickListener;

        public MultiCheckableDialogBuilder(Context context) {
            super(context);
        }

        public MultiCheckableDialogBuilder setData(List<String> list) {
            this.mData = list;
            return this;
        }

        private void initData() {
            onItemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ((ListAdapter.DataHolder) mAdapter.getItem(position)).checked = !((ListAdapter.DataHolder) mAdapter.getItem(position)).checked;
                    mAdapter.notifyDataSetChanged();
                }
            };
        }

        public String getSelectContent() {
            return mAdapter.getSelectContent();
        }

        @Override
        protected void onCreateTitle(VDialog dialog, ViewGroup parent, Context context) {
            super.onCreateTitle(dialog, parent, context);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) this.mTitleView.getLayoutParams();
            int top = VDisplayHelper.dp2px(context, 22);
            lp.setMargins(0, top, 0, 0);
            this.mTitleView.setLayoutParams(lp);
            this.mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        }

        @Override
        protected void onCreateContent(Dialog dialog, ViewGroup parent, Context context) {
            initData();
            mListView = new ListView(context, null, 0, R.style.CustomListViewTheme);
            mListView.setBackground(getBaseContext().getDrawable(R.drawable.dialog_picker_bg));
            mAdapter = new ListAdapter(dialog.getContext(), mData);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 800);
            lp.setMargins(0, VDisplayHelper.dp2px(context, 22), 0, 0);
            mListView.setLayoutParams(lp);
            mListView.setAdapter(mAdapter);
            mListView.setVerticalScrollBarEnabled(false);
            mListView.setOnItemClickListener(onItemClickListener);
            mListView.setDivider(getBaseContext().getDrawable(R.color.divider_gray));
            mListView.setDividerHeight(1);
            mListView.setSelector(getBaseContext().getDrawable(R.color.transparent));
            parent.addView(mListView);
        }
    }

    public static class LocationPickerDialogBuilder extends VDialogBuilder<LocationPickerDialogBuilder> {

        protected PickerView mProvincePickerView;
        protected PickerView mCityPickerView;
        protected PickerView mDistrictPickerView;
        protected Context mContext;
        private int mProvinceIndex = 0, mCityIndex = 0, mDistrictIndex = 0;
        private String mSelectContent;
        private List<VLocationEntity.ResultBean> resultBeans;
        private List<String> mProvinces = new ArrayList<>(), mCities = new ArrayList<>(), mDistricts = new ArrayList<>();

        public LocationPickerDialogBuilder(Context context) {
            super(context);
            this.mContext = context;
        }

        private void initData() {
            String content = VResHelper.readAssetsFile(mContext, "city.json", "UTF-8");
            resultBeans = JSON.parseObject(content, VLocationEntity.class).getResult();
            for (int i = 0; i < resultBeans.size(); i++) {
                mProvinces.add(resultBeans.get(i).getProvince());
            }
            initLocationUnits(0);
            mProvincePickerView.setDataList(mProvinces);
            mProvincePickerView.setSelected(0);
            mCityPickerView.setDataList(mCities);
            mCityPickerView.setSelected(0);
            mDistrictPickerView.setDataList(mDistricts);
            mDistrictPickerView.setSelected(0);
            setSelectContent(mDistricts.get(mDistrictIndex));
            setCanScroll();
        }

        private void initLocationUnits(int i) {
            mCities.clear();
            mDistricts.clear();
            for (int j = 0; j < resultBeans.get(i).getCity().size(); j++) {
                mCities.add(resultBeans.get(i).getCity().get(j).getCity());
                for (int k = 0; k < resultBeans.get(i).getCity().get(j).getDistrict().size(); k++) {
                    mDistricts.add(resultBeans.get(i).getCity().get(j).getDistrict().get(k).getDistrict());
                }
            }
        }

        private void initCityUnit(int i) {
            mCities.clear();
            for (int j = 0; j < resultBeans.get(i).getCity().size(); j++) {
                mCities.add(resultBeans.get(i).getCity().get(j).getCity());
            }
        }

        private void initDistrictUnit(int i, int j) {
            mDistricts.clear();
            for (int k = 0; k < resultBeans.get(i).getCity().get(j).getDistrict().size(); k++) {
                mDistricts.add(resultBeans.get(i).getCity().get(j).getDistrict().get(k).getDistrict());
            }
        }

        private void setCanScroll() {
            mProvincePickerView.setCanScroll(mProvinces.size() > 1);
            mCityPickerView.setCanScroll(mCities.size() > 1);
            mDistrictPickerView.setCanScroll(mDistricts.size() > 1);
        }

        private void setProvinceIndex(String province) {
            for (int i = 0; i < resultBeans.size(); i++) {
                if (resultBeans.get(i).getProvince().equals(province)) {
                    mProvinceIndex = i;
                    break;
                }
            }
        }

        private void setCityIndex(String city) {
            for (int i = 0; i < mCities.size(); i++) {
                if (mCities.get(i).equals(city)) {
                    mCityIndex = i;
                    break;
                }
            }
        }

        private void linkageCityUnit(final boolean showAnim) {
            initCityUnit(mProvinceIndex);

            mCityPickerView.setSelected(0);
            if (showAnim) {
                mCityPickerView.startAnim();
            }
            mCityPickerView.setCanScroll(mCities.size() > 1);
        }

        private void linkageDistrictUnit(final boolean showAnim) {
            initDistrictUnit(mProvinceIndex, mCityIndex);
            mDistrictPickerView.setSelected(0);
            if (showAnim) {
                mDistrictPickerView.startAnim();
            }
        }

        private void setSelectContent(String district) {
            JSONObject object = new JSONObject();
            object.put("province", resultBeans.get(mProvinceIndex).getProvince());
            object.put("city", mCities.get(mCityIndex));
            object.put("district", district);
            this.mSelectContent = object.toJSONString();
        }

        public String getSelectContent() {
            return this.mSelectContent;
        }

        @Override
        protected void onCreateTitle(VDialog dialog, ViewGroup parent, Context context) {
            super.onCreateTitle(dialog, parent, context);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) this.mTitleView.getLayoutParams();
            int top = VDisplayHelper.dp2px(context, 22);
            lp.setMargins(0, top, 0, 0);
            this.mTitleView.setLayoutParams(lp);
            this.mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        }

        @Override
        protected void onCreateContent(Dialog dialog, ViewGroup parent, Context context) {
            mProvincePickerView = new PickerView(context);
            mCityPickerView = new PickerView(context);
            mDistrictPickerView = new PickerView(context);

            LinearLayout ll = new LinearLayout(context);
            ll.setBackground(getBaseContext().getDrawable(R.drawable.dialog_picker_bg));
            ll.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, VDisplayHelper.dp2px(context, 22), 0, 0);
            ll.setLayoutParams(lp);
            LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(0, VDisplayHelper.dp2px(mContext, 206));
            lp1.weight = 1.0f;
            mProvincePickerView.setLayoutParams(lp1);
            mCityPickerView.setLayoutParams(lp1);
            mDistrictPickerView.setLayoutParams(lp1);

            mCityPickerView.setCanShowAnim(false);
            mDistrictPickerView.setCanShowAnim(false);
            mProvincePickerView.setCanScrollLoop(true);
            mCityPickerView.setCanScrollLoop(false);
            mDistrictPickerView.setCanScrollLoop(false);

            initData();

            ll.addView(mProvincePickerView);
            ll.addView(mCityPickerView);
            ll.addView(mDistrictPickerView);

            mProvincePickerView.setOnSelectListener(new PickerView.OnSelectListener() {
                @Override
                public void onSelect(View view, String selected) {
                    setProvinceIndex(selected);
                    linkageCityUnit(true);
                }
            });

            mCityPickerView.setOnSelectListener(new PickerView.OnSelectListener() {
                @Override
                public void onSelect(View view, String selected) {
                    setCityIndex(selected);
                    linkageDistrictUnit(true);
                }
            });

            mDistrictPickerView.setOnSelectListener(new PickerView.OnSelectListener() {
                @Override
                public void onSelect(View view, String selected) {
                    setSelectContent(selected);
                }
            });

            parent.addView(ll);
        }
    }

    public static class DatePickerBuilder extends VDialogBuilder<DatePickerBuilder> {
        protected PickerView mYearPickerView;
        protected PickerView mMonthPickerView;
        protected PickerView mDayPickerView;

        protected long beginTimestamp = VDateFormatUtils.str2Long("2009-05-01", 0);
        protected long endTimestamp = System.currentTimeMillis();
        protected int mShowDateFormatPattern = 0;
        private Calendar mBeginTime, mEndTime, mSelectedTime;
        private int mBeginYear, mBeginMonth, mBeginDay, mEndYear, mEndMonth, mEndDay;
        private List<String> mYearUnits = new ArrayList<>(), mMonthUnits = new ArrayList<>(), mDayUnits = new ArrayList<>();
        /**
         * 时间单位的最大显示值
         */
        private static final int MAX_MONTH_UNIT = 12;
        /**
         * 级联滚动延迟时间
         */
        private static final long LINKAGE_DELAY_DEFAULT = 100L;
        private DecimalFormat mDecimalFormat = new DecimalFormat("00");

        private void initData() {
            mSelectedTime.setTimeInMillis(mBeginTime.getTimeInMillis());

            mBeginYear = mBeginTime.get(Calendar.YEAR);
            // Calendar.MONTH 值为 0-11
            mBeginMonth = mBeginTime.get(Calendar.MONTH) + 1;
            mBeginDay = mBeginTime.get(Calendar.DAY_OF_MONTH);

            mEndYear = mEndTime.get(Calendar.YEAR);
            mEndMonth = mEndTime.get(Calendar.MONTH) + 1;
            mEndDay = mEndTime.get(Calendar.DAY_OF_MONTH);

            boolean canSpanYear = mBeginYear != mEndYear;
            boolean canSpanMon = !canSpanYear && mBeginMonth != mEndMonth;
            if (canSpanYear) {
                initDateUnits(MAX_MONTH_UNIT, mBeginTime.getActualMaximum(Calendar.DAY_OF_MONTH));
            } else if (canSpanMon) {
                initDateUnits(mEndMonth, mBeginTime.getActualMaximum(Calendar.DAY_OF_MONTH));
            } else {
                initDateUnits(mEndMonth, mBeginTime.getActualMaximum(Calendar.DAY_OF_MONTH));
            }
        }

        private void initDateUnits(int endMonth, int endDay) {
            for (int i = mBeginYear; i <= mEndYear; i++) {
                mYearUnits.add(String.valueOf(i));
            }

            for (int i = mBeginMonth; i <= endMonth; i++) {
                mMonthUnits.add(mDecimalFormat.format(i));
            }

            for (int i = mBeginDay; i <= endDay; i++) {
                mDayUnits.add(mDecimalFormat.format(i));
            }

            mYearPickerView.setDataList(mYearUnits);
            mYearPickerView.setSelected(0);
            mMonthPickerView.setDataList(mMonthUnits);
            mMonthPickerView.setSelected(0);
            mDayPickerView.setDataList(mDayUnits);
            mDayPickerView.setSelected(0);

            setCanScroll();
        }

        private void setCanScroll() {
            mYearPickerView.setCanScroll(mYearUnits.size() > 1);
            mMonthPickerView.setCanScroll(mMonthUnits.size() > 1);
            mDayPickerView.setCanScroll(mDayUnits.size() > 1);
        }

        public DatePickerBuilder(Context context) {
            super(context);
        }

        /**
         * 日期开始时间
         *
         * @param timestamp 格式（long）
         * @return
         */
        public DatePickerBuilder setBeginTimestamp(long timestamp) {
            this.beginTimestamp = timestamp;
            return this;
        }

        /**
         * 日期开始时间
         *
         * @param beginTime 格式（yyyy-MM-dd）
         * @return
         */
        public DatePickerBuilder setBeginTimestamp(String beginTime) {
            this.beginTimestamp = VDateFormatUtils.str2Long(beginTime, 0);
            return this;
        }

        /**
         * 日期结束时间
         *
         * @param endTime 格式（yyyy-MM-dd）
         * @return
         */
        public DatePickerBuilder setEndTimestamp(String endTime) {
            this.endTimestamp = VDateFormatUtils.str2Long(endTime, 0);
            return this;
        }

        /**
         * 日期结束时间
         *
         * @param timestamp 格式（long）
         * @return
         */
        public DatePickerBuilder setEndTimestamp(long timestamp) {
            this.endTimestamp = timestamp;
            return this;
        }

        public String getSelectTime() {
            return VDateFormatUtils.long2Str(mSelectedTime.getTimeInMillis(), mShowDateFormatPattern);
        }

        /**
         * 展示日期格式类型
         *
         * @param typ 0：yyyy-MM-dd，1：MM-dd，2：dd
         * @return
         */
        public DatePickerBuilder setShowDateFormatPattern(int typ) {
            this.mShowDateFormatPattern = typ;
            return this;
        }

        @Override
        protected void onCreateTitle(VDialog dialog, ViewGroup parent, Context context) {
            super.onCreateTitle(dialog, parent, context);
            RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) this.mTitleView.getLayoutParams();

            if (!hasSubTitle()) {
                int top = VDisplayHelper.dp2px(context, 22);
                lp1.setMargins(0, top, 0, top);
                this.mTitleView.setLayoutParams(lp1);
                this.mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            } else {
                RelativeLayout.LayoutParams lp2 = (RelativeLayout.LayoutParams) this.mSubTitleView.getLayoutParams();
                int top1 = VDisplayHelper.dp2px(context, 13);
                lp1.setMargins(0, top1, 0, 0);
                this.mTitleView.setLayoutParams(lp1);
                int top2 = VDisplayHelper.dp2px(context, 2);
                lp2.setMargins(0, top2, 0, top1);
                this.mSubTitleView.setLayoutParams(lp2);
                this.mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                this.mSubTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                this.mSubTitleView.setTextColor(context.getResources().getColor(R.color.title_gray));
            }
        }

        @Override
        protected void onCreateContent(Dialog dialog, ViewGroup parent, Context context) {
            mBeginTime = Calendar.getInstance();
            mBeginTime.setTimeInMillis(beginTimestamp);
            mEndTime = Calendar.getInstance();
            mEndTime.setTimeInMillis(endTimestamp);
            mSelectedTime = Calendar.getInstance();

            mYearPickerView = new PickerView(context, "年");
            mMonthPickerView = new PickerView(context, "月");
            mDayPickerView = new PickerView(context, "日");

            LinearLayout ll = new LinearLayout(context);
            ll.setBackground(getBaseContext().getResources().getDrawable(R.drawable.dialog_picker_bg));
            ll.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ll.setLayoutParams(lp);
            LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(0, VDisplayHelper.dp2px(mContext, 206));
            lp1.weight = 1.0f;
            LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(0, VDisplayHelper.dp2px(mContext, 206));
            lp2.weight = 1.0f;
            mYearPickerView.setLayoutParams(lp1);
            mMonthPickerView.setLayoutParams(lp2);
            mDayPickerView.setLayoutParams(lp2);
            mMonthPickerView.setCanShowAnim(false);
            mDayPickerView.setCanShowAnim(false);
            mYearPickerView.setCanScrollLoop(true);
            mMonthPickerView.setCanScrollLoop(false);
            mDayPickerView.setCanScrollLoop(false);

            initData();

            if (mShowDateFormatPattern == 0) {
                ll.addView(mYearPickerView);
                ll.addView(mMonthPickerView);
                ll.addView(mDayPickerView);
            } else if (mShowDateFormatPattern == 1) {
                ll.addView(mMonthPickerView);
                ll.addView(mDayPickerView);
            } else {
                ll.addView(mDayPickerView);
            }

            mYearPickerView.setOnSelectListener(new PickerView.OnSelectListener() {
                @Override
                public void onSelect(View view, String selected) {
                    if (view == null || TextUtils.isEmpty(selected)) return;

                    int timeUnit;
                    try {
                        timeUnit = Integer.parseInt(selected);
                    } catch (Throwable ignored) {
                        return;
                    }

                    mSelectedTime.set(Calendar.YEAR, timeUnit);
                    linkageMonthUnit(true, LINKAGE_DELAY_DEFAULT);
                }
            });
            mMonthPickerView.setOnSelectListener(new PickerView.OnSelectListener() {
                @Override
                public void onSelect(View view, String selected) {
                    int timeUnit;
                    try {
                        timeUnit = Integer.parseInt(selected);
                    } catch (Throwable ignored) {
                        return;
                    }

                    int lastSelectedMonth = mSelectedTime.get(Calendar.MONTH) + 1;
                    mSelectedTime.add(Calendar.MONTH, timeUnit - lastSelectedMonth);
                    linkageDayUnit(true, LINKAGE_DELAY_DEFAULT);
                }
            });
            mDayPickerView.setOnSelectListener(new PickerView.OnSelectListener() {
                @Override
                public void onSelect(View view, String selected) {
                    int timeUnit;
                    try {
                        timeUnit = Integer.parseInt(selected);
                    } catch (Throwable ignored) {
                        return;
                    }

                    mSelectedTime.set(Calendar.DAY_OF_MONTH, timeUnit);
                }
            });
            parent.addView(ll);
        }

        /**
         * 联动“月”变化
         *
         * @param showAnim 是否展示滚动动画
         * @param delay    联动下一级延迟时间
         */
        private void linkageMonthUnit(final boolean showAnim, final long delay) {
            int minMonth;
            int maxMonth;
            int selectedYear = mSelectedTime.get(Calendar.YEAR);
            if (mBeginYear == mEndYear) {
                minMonth = mBeginMonth;
                maxMonth = mEndMonth;
            } else if (selectedYear == mBeginYear) {
                minMonth = mBeginMonth;
                maxMonth = MAX_MONTH_UNIT;
            } else if (selectedYear == mEndYear) {
                minMonth = 1;
                maxMonth = mEndMonth;
            } else {
                minMonth = 1;
                maxMonth = MAX_MONTH_UNIT;
            }

            // 重新初始化时间单元容器
            mMonthUnits.clear();
            for (int i = minMonth; i <= maxMonth; i++) {
                mMonthUnits.add(mDecimalFormat.format(i));
            }
            mMonthPickerView.setDataList(mMonthUnits);

            // 确保联动时不会溢出或改变关联选中值
            int selectedMonth = getValueInRange(mSelectedTime.get(Calendar.MONTH) + 1, minMonth, maxMonth);
            mSelectedTime.set(Calendar.MONTH, selectedMonth - 1);
            mMonthPickerView.setSelected(selectedMonth - minMonth);
            if (showAnim) {
                mMonthPickerView.startAnim();
            }

            // 联动“日”变化
            mMonthPickerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    linkageDayUnit(showAnim, delay);
                }
            }, delay);
        }

        /**
         * 联动“日”变化
         *
         * @param showAnim 是否展示滚动动画
         * @param delay    联动下一级延迟时间
         */
        private void linkageDayUnit(final boolean showAnim, final long delay) {
            int minDay;
            int maxDay;
            int selectedYear = mSelectedTime.get(Calendar.YEAR);
            int selectedMonth = mSelectedTime.get(Calendar.MONTH) + 1;
            if (mBeginYear == mEndYear && mBeginMonth == mEndMonth) {
                minDay = mBeginDay;
                maxDay = mEndDay;
            } else if (selectedYear == mBeginYear && selectedMonth == mBeginMonth) {
                minDay = mBeginDay;
                maxDay = mSelectedTime.getActualMaximum(Calendar.DAY_OF_MONTH);
            } else if (selectedYear == mEndYear && selectedMonth == mEndMonth) {
                minDay = 1;
                maxDay = mEndDay;
            } else {
                minDay = 1;
                maxDay = mSelectedTime.getActualMaximum(Calendar.DAY_OF_MONTH);
            }

            mDayUnits.clear();
            for (int i = minDay; i <= maxDay; i++) {
                mDayUnits.add(mDecimalFormat.format(i));
            }
            mDayPickerView.setDataList(mDayUnits);

            int selectedDay = getValueInRange(mSelectedTime.get(Calendar.DAY_OF_MONTH), minDay, maxDay);
            mSelectedTime.set(Calendar.DAY_OF_MONTH, selectedDay);
            mDayPickerView.setSelected(selectedDay - minDay);
            if (showAnim) {
                mDayPickerView.startAnim();
            }
        }

        private int getValueInRange(int value, int minValue, int maxValue) {
            if (value < minValue) {
                return minValue;
            } else if (value > maxValue) {
                return maxValue;
            } else {
                return value;
            }
        }
    }
}
