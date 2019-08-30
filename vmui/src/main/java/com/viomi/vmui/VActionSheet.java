package com.viomi.vmui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.viomi.vmui.Dialog.GridAdapter;
import com.viomi.vmui.Dialog.VDialogBuilder;
import com.viomi.vmui.utils.VMUIDisplayHelper;

import java.util.ArrayList;
import java.util.List;

public class VActionSheet extends Dialog {
    private View mContentView;
    private boolean mIsAnimating = false;
    private int mAnimationDuration = 200;
    private OnBottomSheetShowListener mOnBottomSheetShowListener;

    public VActionSheet(@NonNull Context context) {
        super(context, R.style.VMUI_ActionSheet);
    }

    public VActionSheet(@NonNull Context context, int themeResId) {
        super(context, R.style.VMUI_ActionSheet);
    }

    protected VActionSheet(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void setOnBottomSheetShowListener(OnBottomSheetShowListener onBottomSheetShowListener) {
        mOnBottomSheetShowListener = onBottomSheetShowListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //noinspection ConstantConditions
        getWindow().getDecorView().setPadding(0, 0, 0, 0);

        // 在底部，宽度撑满
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM | Gravity.CENTER;

        int screenWidth = VMUIDisplayHelper.getScreenWidth(getContext());
        int screenHeight = VMUIDisplayHelper.getScreenHeight(getContext());
        params.width = screenWidth < screenHeight ? screenWidth : screenHeight;
        getWindow().setAttributes(params);
        setCanceledOnTouchOutside(true);
    }

    @Override
    public void addContentView(@NonNull View view, @Nullable ViewGroup.LayoutParams params) {
        mContentView = view;
        super.addContentView(view, params);
    }

    @Override
    public void setContentView(int layoutResID) {
        mContentView = LayoutInflater.from(getContext()).inflate(layoutResID, null);
        super.setContentView(mContentView);
    }

    @Override
    public void setContentView(@NonNull View view, ViewGroup.LayoutParams params) {
        mContentView = view;
        super.setContentView(view, params);
    }

    public View getContentView() {
        return mContentView;
    }

    @Override
    public void setContentView(@NonNull View view) {
        mContentView = view;
        super.setContentView(view);
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
                    VActionSheet.super.dismiss();
                } catch (Exception e) {
                    //QMUILog.w(TAG, "dismiss error\n" + Log.getStackTraceString(e));
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
        if (mOnBottomSheetShowListener != null) {
            mOnBottomSheetShowListener.onShow();
        }
    }

    @Override
    public void dismiss() {
        if (mIsAnimating) {
            return;
        }
        animateDown();
    }

    public interface OnBottomSheetShowListener {
        void onShow();
    }

    public static class MultiButtonActionSheetBuilder extends VDialogBuilder<MultiButtonActionSheetBuilder> implements AdapterView.OnItemClickListener {
        private Dialog mDialog;
        private ListView mListView;
        private BaseAdapter mAdapter;
        List<String> mData = new ArrayList<>();
        private OnSheetItemClickListener onSheetItemClickListener;
        public MultiButtonActionSheetBuilder(Context context) {
            super(context);
        }

        public MultiButtonActionSheetBuilder addItem(String itemName){
            mData.add(itemName);
            return this;
        }

        public MultiButtonActionSheetBuilder setOnSheetItemClickListener(OnSheetItemClickListener listener){
            onSheetItemClickListener = listener;
            return this;
        }

        @Override
        protected void onCreateContent(Dialog dialog, ViewGroup parent, Context context) {
            mDialog = dialog;
            mListView = new ListView(context);
            mAdapter = new ArrayAdapter<String>(context,R.layout.sheet_item,mData);
            mListView.setAdapter(mAdapter);

            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mListView.setLayoutParams(lp);
            mListView.setAdapter(mAdapter);
            mListView.setVerticalScrollBarEnabled(false);
            mListView.setDivider(getBaseContext().getDrawable(R.color.divider_gray));
            mListView.setDividerHeight(1);
            mListView.setSelector(getBaseContext().getDrawable(R.color.transparent));
            mListView.setOnItemClickListener(this);
            parent.addView(mListView);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(onSheetItemClickListener != null){
                onSheetItemClickListener.onItemClick(mDialog,mAdapter.getItem(position).toString());
            }
        }

        public interface OnSheetItemClickListener{
            void onItemClick(Dialog dialog, String selected);
        }
    }

    public static class ShareSheetDialogBuilder extends VDialogBuilder<ShareSheetDialogBuilder> implements AdapterView.OnItemClickListener {
        private GridView mGridView;
        private int mNumColumns = 0;
        private GridAdapter mAdapter;
        private Dialog mDialog;
        private OnSheetItemClickListener onSheetItemClickListener;
        List<GridAdapter.DataHolder> mData = new ArrayList<>();

        public ShareSheetDialogBuilder(Context context) {
            super(context);
            this.mAdapter = new GridAdapter(context, mData);
        }

        public ShareSheetDialogBuilder addItem(String name, int resId) {
            mAdapter.addItem(name, resId);
            return this;
        }

        public ShareSheetDialogBuilder setNumColumns(int value) {
            mNumColumns = value;
            return this;
        }

        public ShareSheetDialogBuilder setOnSheetItemClickListener(OnSheetItemClickListener listener){
            onSheetItemClickListener = listener;
            return this;
        }

        @Override
        protected void onCreateContent(Dialog dialog, ViewGroup parent, Context context) {
            mDialog = dialog;
            mGridView = new GridView(context);
            mGridView.setAdapter(mAdapter);
            if (mNumColumns != 0) {
                mGridView.setNumColumns(mNumColumns);
            } else if (mData.size() <= 4) {
                mGridView.setNumColumns(mData.size());
            } else {
                mGridView.setNumColumns(4);
            }
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mGridView.setLayoutParams(lp);
            mGridView.setSelector(getBaseContext().getDrawable(R.color.transparent));
            mGridView.setOnItemClickListener(this);
            parent.addView(mGridView);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(onSheetItemClickListener != null){
                onSheetItemClickListener.onItemClick(mDialog,mAdapter.getItemName(position));
            }
        }

        public interface OnSheetItemClickListener{
            void onItemClick(Dialog dialog, String selected);
        }
    }
}
