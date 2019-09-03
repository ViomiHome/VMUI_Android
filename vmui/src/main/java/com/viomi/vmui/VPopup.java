package com.viomi.vmui;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.viomi.vmui.Dialog.VDialogBuilder;
import com.viomi.vmui.utils.VMUIDisplayHelper;

public class VPopup extends Dialog {

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

        private boolean mShowCloseImage = true;
        private boolean mShowOperateButton = true;

        private OnViewClickListener mViewClickListener;

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

        public VPopupBuilder setContentLayoutResId(int resId) {
            mContentLayoutResId = resId;
            return this;
        }

        public VPopupBuilder setContentLayoutView(View view){
            mContentLayoutView = view;
            return this;
        }

        public VPopupBuilder setViewDismissClickListener(OnViewClickListener listener){
            mViewClickListener = listener;
            return this;
        }

        @Override
        protected void onCreateContent(Dialog dialog, ViewGroup parent, Context context) {
            mDialog = dialog;

            FrameLayout.LayoutParams lp1 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, VMUIDisplayHelper.dp2px(context, 300));
            LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, VMUIDisplayHelper.dp2px(context, 44));

            if (mShowCloseImage) {
                lp2.gravity = Gravity.END;
                lp2.setMargins(0, 16, 16, 10);
                mCloseImage = new ImageView(context);
                mCloseImage.setImageResource(R.mipmap.ic_popup_close);
                mCloseImage.setId(R.id.vmui_popup_img_close);
                mCloseImage.setLayoutParams(lp2);
                mCloseImage.setOnClickListener(this);
                parent.addView(mCloseImage);
            }
            if (mContentLayoutResId != 0) {
                View content = LayoutInflater.from(context).inflate(mContentLayoutResId, null);
                content.setLayoutParams(lp1);
                parent.addView(content);
            } else if(mContentLayoutView != null){
                mContentLayoutView.setLayoutParams(lp1);
                parent.addView(mContentLayoutView);
            } else {
                LinearLayout view = new LinearLayout(context);
                view.setLayoutParams(lp1);
                parent.addView(view);
            }

            if (mShowOperateButton) {
                mBtnOperation = new VButton(context);
                int lr = VMUIDisplayHelper.dp2px(context, 16);
                int tb = VMUIDisplayHelper.dp2px(context, 11);
                lp3.setMargins(lr, tb, lr, tb);
                mBtnOperation.setButton_style(3);
                mBtnOperation.setText_content("按钮");
                mBtnOperation.setId(R.id.vmui_popup_btn_operate);
                mBtnOperation.setOnClickListener(this);
                mBtnOperation.setTextSize(VMUIDisplayHelper.sp2px(context, 15));
                mBtnOperation.setLayoutParams(lp3);
                parent.addView(mBtnOperation);
            }
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.vmui_popup_img_close) {
                mViewClickListener.onViewClick(mDialog);
            } else if (v.getId() == R.id.vmui_popup_btn_operate) {
                mViewClickListener.onViewClick(mDialog);
            }
        }

        public interface OnViewClickListener{
            void onViewClick(Dialog dialog);
        }
    }
}
