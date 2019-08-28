package com.viomi.vmui.Dialog;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import com.viomi.vmui.R;
import com.viomi.vmui.utils.VMUIDisplayHelper;
import com.viomi.vmui.utils.VMUIResHelper;

import java.util.ArrayList;
import java.util.List;

import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

public abstract class VDialogBuilder<T extends VDialogBuilder> {

    private VDialog mDialog;
    protected String mTitle;

    protected Context mContext;
    private TextView mTitleView;
    private ImageView mHeadImage;
    protected int mImgResId = -1;
    private boolean mCancelable = true;
    protected LinearLayout mActionContainer;
    private boolean mCanceledOnTouchOutside = true;
    private int mActionContainerOrientation = HORIZONTAL;
    protected List<VDialogAction> mActions = new ArrayList<>();

    //---------divider---------------------
    private int mActionDividerThickness = 0;
    private int mActionDividerColorRes = 0;
    private int mActionDividerInsetStart = 0;
    private int mActionDividerInsetEnd = 0;

    protected LinearLayout mRootView;
    protected VDialogView mDialogView;

    public VDialogBuilder(Context context) {
        this.mContext = context;
    }

    /**
     * 设置对话框顶部的标题文字
     */
    @SuppressWarnings("unchecked")
    public T setTitle(String title) {
        if (title != null && title.length() > 0) {
            this.mTitle = title + mContext.getString(R.string.vmui_tool_fixellipsize);
        }
        return (T) this;
    }

    /**
     * 设置对话框顶部的标题文字
     */
    public T setTitle(int resId) {
        return setTitle(mContext.getResources().getString(resId));
    }

    /**
     * 设置对话框顶部的图标资源
     *
     * @param resId
     * @return
     */
    public T setHeadImage(int resId) {
        this.mImgResId = resId;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T setCancelable(boolean cancelable) {
        mCancelable = cancelable;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
        mCanceledOnTouchOutside = canceledOnTouchOutside;
        return (T) this;
    }

    /**
     * 判断对话框是否需要显示title
     *
     * @return 是否有title
     */
    protected boolean hasTitle() {
        return mTitle != null && mTitle.length() != 0;
    }

    protected boolean hasHeadImage() {
        return mImgResId != -1;
    }

    public Context getBaseContext() {
        return mContext;
    }

    //region 添加action

    /**
     * 添加对话框底部的操作按钮
     */
    @SuppressWarnings("unchecked")
    public T addAction(@Nullable VDialogAction action) {
        if (action != null) {
            mActions.add(action);
        }

        return (T) this;
    }

    /**
     * 添加无图标正常类型的操作按钮
     *
     * @param strResId 文案
     * @param listener 点击回调事件
     */
    public T addAction(int strResId, VDialogAction.ActionListener listener) {
        return addAction(0, strResId, listener);
    }

    /**
     * 添加无图标正常类型的操作按钮
     *
     * @param str      文案
     * @param listener 点击回调事件
     */
    public T addAction(CharSequence str, VDialogAction.ActionListener listener) {
        return addAction(0, str, VDialogAction.ACTION_PROP_NEUTRAL, listener);
    }

    /**
     * 添加无图标指定类型的操作按钮
     * @param str       文案
     * @param prop      按钮类型
     * @param listener  点击回调时间
     * @return
     */
    public T addAction(CharSequence str,@VDialogAction.Prop int prop, VDialogAction.ActionListener listener){
        return addAction(0,str,prop,listener);
    }


    /**
     * 添加普通类型的操作按钮
     *
     * @param iconResId 图标
     * @param strResId  文案
     * @param listener  点击回调事件
     */
    public T addAction(int iconResId, int strResId, VDialogAction.ActionListener listener) {
        return addAction(iconResId, strResId, VDialogAction.ACTION_PROP_NEUTRAL, listener);
    }

    /**
     * 添加普通类型的操作按钮
     *
     * @param iconResId 图标
     * @param str       文案
     * @param listener  点击回调事件
     */
    public T addAction(int iconResId, CharSequence str, VDialogAction.ActionListener listener) {
        return addAction(iconResId, str, VDialogAction.ACTION_PROP_NEUTRAL, listener);
    }


    /**
     * 添加操作按钮
     *
     * @param iconRes  图标
     * @param strRes   文案
     * @param prop     属性
     * @param listener 点击回调事件
     */
    public T addAction(int iconRes, int strRes, @VDialogAction.Prop int prop, VDialogAction.ActionListener listener) {
        return addAction(iconRes, mContext.getResources().getString(strRes), prop, listener);
    }

    /**
     * 添加操作按钮
     *
     * @param iconRes  图标
     * @param str      文案
     * @param prop     属性
     * @param listener 点击回调事件
     */
    @SuppressWarnings("unchecked")
    public T addAction(int iconRes, CharSequence str, @VDialogAction.Prop int prop, VDialogAction.ActionListener listener) {
        VDialogAction action = new VDialogAction(mContext, iconRes, str, prop, listener);
        mActions.add(action);
        return (T) this;
    }


    //endregion

    /**
     * 产生一个 Dialog 并显示出来
     */
    public VDialog show() {
        final VDialog dialog = create();
        dialog.show();
        return dialog;
    }

    public VDialog create() {
        return create(R.style.VMUI_Dialog);
    }

    public VDialog create(@StyleRes int style) {
        mDialog = new VDialog(mContext, style);
        Context dialogContext = mDialog.getContext();

        mRootView = (LinearLayout) LayoutInflater.from(dialogContext).inflate(R.layout.vdialog, null);
        mDialogView = mRootView.findViewById(R.id.dialog);
        //mDialogView.setOnDecorationListener(mOnDecorationListener);

        // head image
        onCreateHeadImage(mDialog, mDialogView, dialogContext);

        // title
        onCreateTitle(mDialog, mDialogView, dialogContext);

        //content
        onCreateContent(mDialog, mDialogView, dialogContext);

        // 操作
        onCreateHandlerBar(mDialog, mDialogView, dialogContext);


        mDialog.addContentView(mRootView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mDialog.setCancelable(mCancelable);
        mDialog.setCanceledOnTouchOutside(mCanceledOnTouchOutside);
        onAfter(mDialog, mRootView, dialogContext);
        return mDialog;
    }

    /**
     * 创建顶部的图标区域
     *
     * @param dialog
     * @param parent
     * @param context
     */
    protected void onCreateHeadImage(VDialog dialog, ViewGroup parent, Context context) {
        if (hasHeadImage()) {
            mHeadImage = new ImageView(context);
            mHeadImage.setImageResource(mImgResId);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mHeadImage.setLayoutParams(lp);
            parent.addView(mHeadImage);
        }
    }

    /**
     * 创建顶部的标题区域
     */
    protected void onCreateTitle(VDialog dialog, ViewGroup parent, Context context) {
        if (hasTitle()) {
            mTitleView = new TextView(context);

            mTitleView.setText(mTitle);

            VMUIResHelper.assignTextViewWithAttr(mTitleView, R.attr.dialog_title_style);
            //onConfigTitleView(mTitleView);
            RelativeLayout rtl = new RelativeLayout(dialog.getContext());
            RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp2.addRule(RelativeLayout.CENTER_HORIZONTAL);
            rtl.setLayoutParams(lp1);
            mTitleView.setLayoutParams(lp2);
            rtl.addView(mTitleView);
            parent.addView(rtl);
        }
    }

    /**
     * 创建中间的区域
     */
    protected abstract void onCreateContent(VDialog dialog, ViewGroup parent, Context context);

    /**
     * 创建底部的操作栏区域
     */
    protected void onCreateHandlerBar(final VDialog dialog, ViewGroup parent, Context context) {
        int size = mActions.size();
        if (size > 0) {
            TypedArray a = context.obtainStyledAttributes(null, R.styleable.DialogActionContainerCustomDef, R.attr.dialog_action_container_style, 0);
            int count = a.getIndexCount();
            int justifyContent = 1, spaceCustomIndex = 0;
            int actionHeight = -1, actionSpace = 0;
            for (int i = 0; i < count; i++) {
                int attr = a.getIndex(i);
                if (attr == R.styleable.DialogActionContainerCustomDef_dialog_action_container_justify_content) {
                    justifyContent = a.getInteger(attr, justifyContent);
                } else if (attr == R.styleable.DialogActionContainerCustomDef_dialog_action_container_custom_space_index) {
                    spaceCustomIndex = a.getInteger(attr, 0);
                } else if (attr == R.styleable.DialogActionContainerCustomDef_dialog_action_space) {
                    actionSpace = a.getDimensionPixelSize(attr, 0);
                } else if (attr == R.styleable.DialogActionContainerCustomDef_dialog_action_height) {
                    actionHeight = a.getDimensionPixelSize(attr, 0);
                }
            }
            a.recycle();
            int spaceInsertPos = -1;
            if (mActionContainerOrientation == VERTICAL) {
                spaceInsertPos = -1;
            } else if (justifyContent == 0) {
                spaceInsertPos = size;
            } else if (justifyContent == 1) {
                spaceInsertPos = 0;
            } else if (justifyContent == 3) {
                spaceInsertPos = spaceCustomIndex;
            }


            mActionContainer = new LinearLayout(context, null);
            mActionContainer.setOrientation(mActionContainerOrientation == VERTICAL ? VERTICAL : HORIZONTAL);
            mActionContainer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            for (int i = 0; i < size; i++) {
                if (spaceInsertPos == i) {
                    mActionContainer.addView(createActionContainerSpace(context));
                }
                VDialogAction action = mActions.get(i);

                LinearLayout.LayoutParams actionLp;
                if (mActionContainerOrientation == VERTICAL) {
                    actionLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, actionHeight);
                } else {
                    actionLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, actionHeight);
                    if (spaceInsertPos >= 0) {
                        if (i >= spaceInsertPos) {
                            actionLp.leftMargin = actionSpace;
                        } else {
                            actionLp.rightMargin = actionSpace;
                        }
                    }
                    if (justifyContent == 2) {
                        actionLp.weight = 1;
                    }
                }
                Button actionView = action.buildActionView(mDialog, i);

                // add divider
                if (mActionDividerThickness > 0 && i > 0 && spaceInsertPos != i) {
                    if (mActionContainerOrientation == VERTICAL) {
                        //actionView.onlyShowTopDivider(mActionDividerInsetStart, mActionDividerInsetEnd, mActionDividerThickness, ContextCompat.getColor(context, mActionDividerColorRes));
                    } else {
                        //actionView.onlyShowLeftDivider(mActionDividerInsetStart, mActionDividerInsetEnd, mActionDividerThickness, ContextCompat.getColor(context, mActionDividerColorRes));
                    }
                }

                //actionView.setChangeAlphaWhenDisable(mChangeAlphaForPressOrDisable);
                //actionView.setChangeAlphaWhenPress(mChangeAlphaForPressOrDisable);
                mActionContainer.addView(actionView, actionLp);
            }

            if (spaceInsertPos == size) {
                mActionContainer.addView(createActionContainerSpace(context));
            }

            if (mActionContainerOrientation == HORIZONTAL) {
                mActionContainer.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        int width = right - left;
                        int childCount = mActionContainer.getChildCount();
                        if (childCount > 0) {
                            View lastChild = mActionContainer.getChildAt(childCount - 1);
                            // 如果ActionButton的宽度过宽，则减小padding
                            if (lastChild.getRight() > width) {
                                int childPaddingHor = Math.max(0, lastChild.getPaddingLeft() - VMUIDisplayHelper.dp2px(mContext, 3));
                                for (int i = 0; i < childCount; i++) {
                                    mActionContainer.getChildAt(i).setPadding(childPaddingHor, 0, childPaddingHor, 0);
                                }
                            }
                        }

                    }
                });
            }
            parent.addView(mActionContainer);
        }
    }

    private View createActionContainerSpace(Context context) {
        Space space = new Space(context);
        LinearLayout.LayoutParams spaceLp = new LinearLayout.LayoutParams(0, 0);
        spaceLp.weight = 1;
        space.setLayoutParams(spaceLp);
        return space;
    }

    protected void onAfter(VDialog dialog, LinearLayout parent, Context context) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.cancelOutSide();
            }
        };
    }
}
