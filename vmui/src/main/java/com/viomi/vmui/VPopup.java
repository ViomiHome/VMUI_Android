package com.viomi.vmui;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.LayoutDirection;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.viomi.vmui.Dialog.VDialog;
import com.viomi.vmui.Dialog.VDialogBuilder;
import com.viomi.vmui.utils.VDisplayHelper;
import com.viomi.vmui.utils.VResHelper;

public class VPopup extends VDialog {

    public VPopup(@NonNull Context context) {
        super(context, R.style.VMUI_Popup);
    }

    public VPopup(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected VPopup(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class VPopupBuilder extends VDialogBuilder<VPopupBuilder> implements View.OnClickListener {

        private ImageView mCloseImage;
        private VButton mBtnOperation;
        private int mContentLayoutResId;
        private View mContentLayoutView;
        private Dialog mDialog;
        private String mButtonContent = "按钮";
        private boolean mShowCloseImage = true;
        private boolean mShowOperateButton = true;

        private OnViewClickListener mViewClickListener;
        private OnViewDismissClickListener mViewDismissClickListener;

        public VPopupBuilder(Context context) {
            super(context);
        }

        public VPopupBuilder setShowCloseImage(boolean value) {
            mShowCloseImage = value;
            return this;
        }

        public VPopupBuilder setShowOperateButton(boolean value) {
            mShowOperateButton = value;
            return this;
        }

        public VPopupBuilder setOperateButtonText(String text) {
            mButtonContent = text;
            return this;
        }

        public VPopupBuilder setContentLayoutResId(int resId) {
            mContentLayoutResId = resId;
            return this;
        }

        public VPopupBuilder setContentLayoutView(View view) {
            mContentLayoutView = view;
            return this;
        }

        public VPopupBuilder setViewDismissClickListener(OnViewDismissClickListener listener) {
            mViewDismissClickListener = listener;
            return this;
        }

        public VPopupBuilder setViewClickListener(OnViewClickListener listener) {
            mViewClickListener = listener;
            return this;
        }

        @Override
        protected void onCreateHeadImage(VDialog dialog, ViewGroup parent, Context context) {

            if (hasHeadImage()) {
                mHeadImage = new ImageView(context);
                mHeadImage.setImageResource(mImgResId);
                RelativeLayout layout = new RelativeLayout(context);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layout.setLayoutParams(lp);
                lp1.addRule(RelativeLayout.CENTER_HORIZONTAL);
                int top = VDisplayHelper.dp2px(context, 11);
                lp1.setMargins(0, top, 0, 0);
                mHeadImage.setLayoutParams(lp1);
                layout.addView(mHeadImage);

                if (mShowCloseImage) {
                    showCloseImage(context);
                    layout.addView(mCloseImage);
                }
                parent.addView(layout);
            }
        }

        @Override
        protected void onCreateTitle(VDialog dialog, ViewGroup parent, Context context) {
            mTitleView = new VTextView(context);
            mTitleView.setEnabled(false);
            mTitleView.setText(mTitle);
            mTitleView.setId(R.id.vmui_dialog_title_id);
            VResHelper.assignTextViewWithAttr(mTitleView, R.attr.popup_title_style);
            RelativeLayout rtl = new RelativeLayout(context);
            if (hasTitle()) {
                RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp1.addRule(RelativeLayout.CENTER_HORIZONTAL);

                if (hasHeadImage()) {
                    int top = VDisplayHelper.dp2px(context, 12);
                    lp1.setMargins(0, top, 0, 0);
                } else {
                    if (mShowCloseImage) {
                        showCloseImage(context);
                        rtl.addView(mCloseImage);
                    }
                    int top = VDisplayHelper.dp2px(context, 26);
                    lp1.setMargins(0, top, 0, 0);
                }
                mTitleView.setLayoutParams(lp1);
                rtl.addView(mTitleView);
                parent.addView(rtl);
            }
        }

        @Override
        protected void onCreateContent(Dialog dialog, ViewGroup parent, Context context) {

            mDialog = dialog;
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, VDisplayHelper.dp2px(context, 400));
            parent.setLayoutParams(lp);
            RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            RelativeLayout layout = new RelativeLayout(context);
            layout.setLayoutParams(lp1);
            RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, VDisplayHelper.dp2px(context, 44));

            if (!hasHeadImage() && !hasTitle() && mShowCloseImage) {
                showCloseImage(context);
                layout.addView(mCloseImage);
            }

            if (mContentLayoutResId != 0) {
                View content = LayoutInflater.from(context).inflate(mContentLayoutResId, null);
                content.setLayoutParams(lp2);
                lp2.addRule(RelativeLayout.CENTER_HORIZONTAL);
                layout.addView(content);
            } else if (mContentLayoutView != null) {
                mContentLayoutView.setLayoutParams(lp2);
                lp2.addRule(RelativeLayout.CENTER_HORIZONTAL);
                layout.addView(mContentLayoutView);
            } else {
                LinearLayout view = new LinearLayout(context);
                view.setLayoutParams(lp2);
                lp2.addRule(RelativeLayout.CENTER_HORIZONTAL);
                layout.addView(view);
            }
            if (mShowOperateButton) {
                RelativeLayout rtl = new RelativeLayout(context);
                RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, VDisplayHelper.dp2px(context, 64));

                mBtnOperation = generateActionButton(context, mButtonContent);
                mBtnOperation.setId(R.id.vmui_popup_btn_operate);
                mBtnOperation.setOnClickListener(this);
                int lr = VDisplayHelper.dp2px(context, 16);
                int tb = VDisplayHelper.dp2px(context, 10);
                lp3.setMargins(lr, 0, lr, tb);
                lp3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                lp4.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                mBtnOperation.setLayoutParams(lp3);
                rtl.setLayoutParams(lp4);
                rtl.addView(mBtnOperation);
                layout.addView(rtl);
            }
            parent.addView(layout);
        }

        private void showCloseImage(Context context) {
            RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            lp2.setMargins(0, (int) getBaseContext().getResources().getDimension(R.dimen.margin),
                    (int) getBaseContext().getResources().getDimension(R.dimen.margin), 0);
            mCloseImage = new ImageView(context);
            mCloseImage.setImageResource(R.mipmap.ic_popup_close);
            mCloseImage.setId(R.id.vmui_popup_img_close);
            mCloseImage.setLayoutParams(lp2);
            mCloseImage.setOnClickListener(this);
        }

        /**
         * 生成适用于对话框的按钮
         */
        private VButton generateActionButton(Context context, CharSequence text) {
            // button 有提供 buttonStyle, 覆盖第三个参数不是好选择
            VButton button = new VButton(context);
            button.setText_content(text.toString());
            button.setButton_style(8);
            TypedArray a;
            a = context.obtainStyledAttributes(null, R.styleable.DialogActionStyleDef, R.attr.popup_action_style, 0);
            int count = a.getIndexCount();
            for (int i = 0; i < count; i++) {
                int attr = a.getIndex(i);
                if (attr == R.styleable.DialogActionStyleDef_android_gravity) {
                    button.setGravity(a.getInt(attr, -1));
                } else if (attr == R.styleable.DialogActionStyleDef_android_textColor) {
                    button.tvContent.setTextColor(a.getColorStateList(attr).getDefaultColor());
                } else if (attr == R.styleable.DialogActionStyleDef_android_textSize) {
                    button.setTextSize(a.getDimensionPixelSize(attr, 0));
                } else if (attr == R.styleable.DialogActionStyleDef_android_textStyle) {
                    int styleIndex = a.getInt(attr, -1);
                    button.tvContent.setTypeface(null, styleIndex);
                }
            }
            a.recycle();
            return button;
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.vmui_popup_img_close) {
                if (mViewDismissClickListener != null) {
                    mViewDismissClickListener.onViewDismissClick(mDialog);
                }
            } else if (v.getId() == R.id.vmui_popup_btn_operate) {
                if (mViewClickListener != null) {
                    mViewClickListener.onViewClick(mDialog);
                }
            }
        }

        public interface OnViewClickListener {
            void onViewClick(Dialog dialog);
        }

        public interface OnViewDismissClickListener {
            void onViewDismissClick(Dialog dialog);
        }
    }
}
