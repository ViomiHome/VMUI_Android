package com.viomi.vmui.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;

import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import com.viomi.vmui.R;
import com.viomi.vmui.VActionSheet;
import com.viomi.vmui.VButton;
import com.viomi.vmui.VPopup;
import com.viomi.vmui.VTextView;
import com.viomi.vmui.utils.VDisplayHelper;
import com.viomi.vmui.utils.VResHelper;

import java.util.ArrayList;
import java.util.List;

import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

public abstract class VDialogBuilder<T extends VDialogBuilder> {

    private VDialog mDialog;
    protected String mTitle;
    protected String mSubTitle;
    protected Context mContext;
    protected VTextView mTitleView;
    protected VTextView mSubTitleView;
    protected ImageView mHeadImage;
    protected int mImgResId = -1;
    private boolean mCancelable = true;
    protected LinearLayout mActionContainer;
    private boolean mCanceledOnTouchOutside = true;
    private int mActionContainerOrientation = HORIZONTAL;
    protected List<VDialogAction> mActions = new ArrayList<>();

    protected LinearLayout mRootView;
    protected VDialogView mDialogView;
    float titleSize;
    float subtextSize;

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
     * 设置对话框顶部的副标题文字
     */
    public T setTitle(int resId) {
        return setTitle(mContext.getResources().getString(resId));
    }

    /**
     * 设置对话框顶部的副标题文字
     */
    public T setSubTitle(int resId) {
        return setSubTitle(mContext.getResources().getString(resId));
    }

    /**
     * 设置对话框顶部的标题文字
     */
    @SuppressWarnings("unchecked")
    public T setSubTitle(String subTitle) {
        if (subTitle != null && subTitle.length() > 0) {
            this.mSubTitle = subTitle + mContext.getString(R.string.vmui_tool_fixellipsize);
        }
        return (T) this;
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

    /**
     * 判断对话框是否需要显示subtitle
     *
     * @return 是否有title
     */
    protected boolean hasSubTitle() {
        return mSubTitle != null && mSubTitle.length() != 0;
    }

    protected boolean hasHeadImage() {
        return mImgResId != -1;
    }

    public Context getBaseContext() {
        return mContext;
    }

    public T setActionContainerOrientation(int orientation) {
        mActionContainerOrientation = orientation;
        return (T) this;
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
     *
     * @param str      文案
     * @param prop     按钮类型
     * @param listener 点击回调时间
     * @return
     */
    public T addAction(CharSequence str, @VDialogAction.Prop int prop, VDialogAction.ActionListener listener) {
        return addAction(0, str, prop, listener);
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
    public VDialog showDialog() {
        final VDialog dialog = createDialog();
        dialog.show();
        return dialog;
    }

    public VActionSheet showActionSheet() {
        VActionSheet actionSheet = createActionSheet();
        actionSheet.show();
        return actionSheet;
    }

    public VPopup showPopup() {
        VPopup popup = createPopup();
//        WindowManager.LayoutParams params = popup.getWindow().getAttributes();
//        FloatWindowManager.getInstance().applyOrShowFloatWindow(mContext);
//
//
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
//            params.type = WindowManager.LayoutParams.TYPE_PHONE;
//            popup.getWindow().setAttributes(params);
//        } else {
//            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
//            popup.getWindow().setAttributes(params);
//        }

        popup.show();
        return popup;
    }

    public VActionSheet createActionSheet() {
        VActionSheet actionSheet = new VActionSheet(mContext);
        Context sheetContext = actionSheet.getContext();
        mRootView = (LinearLayout) View.inflate(mContext, R.layout.vdialog, null);
        mDialogView = mRootView.findViewById(R.id.dialog);

        // title
        onCreateSheetTitle(actionSheet, mDialogView, sheetContext);

        //content
        onCreateContent(actionSheet, mDialogView, sheetContext);

        // 操作
        onCreateHandlerBar(actionSheet, mDialogView, sheetContext);

        actionSheet.addContentView(mRootView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        actionSheet.setCancelable(mCancelable);
        actionSheet.setCanceledOnTouchOutside(mCanceledOnTouchOutside);
        onAfter(actionSheet, mRootView, sheetContext);
        return actionSheet;
    }

    public VPopup createPopup() {
        VPopup popup = new VPopup(mContext);
        Context popupContext = popup.getContext();
        mRootView = (LinearLayout) LayoutInflater.from(popupContext).inflate(R.layout.vdialog, null);
        mDialogView = mRootView.findViewById(R.id.dialog);
        // head image
        onCreateHeadImage(popup, mDialogView, popupContext);

        // title
        onCreateTitle(popup, mDialogView, popupContext);

        //content
        onCreateContent(popup, mDialogView, popupContext);
        popup.setContentView(mRootView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        popup.setCancelable(mCancelable);
        popup.setCanceledOnTouchOutside(mCanceledOnTouchOutside);
        return popup;
    }

    public VDialog createDialog() {
        return createDialog(R.style.VMUI_Dialog);
    }

    public VDialog createDialog(@StyleRes int style) {
        mDialog = new VDialog(mContext, style);
        Context dialogContext = mDialog.getContext();

        mRootView = (LinearLayout) LayoutInflater.from(dialogContext).inflate(R.layout.vdialog, null);
        mDialogView = mRootView.findViewById(R.id.dialog);

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
            int top = VDisplayHelper.dp2px(context, 27);
            lp.setMargins(0, top, 0, 0);
            mHeadImage.setLayoutParams(lp);
            parent.addView(mHeadImage);
        }
    }

    /**
     * 创建顶部的标题区域
     */
    protected void onCreateTitle(VDialog dialog, ViewGroup parent, Context context) {
        mTitleView = new VTextView(context);
        mTitleView.setEnabled(false);
        mTitleView.setText(mTitle);
        mTitleView.setId(R.id.vmui_dialog_title_id);
        VResHelper.assignTextViewWithAttr(mTitleView, R.attr.dialog_title_style);
        RelativeLayout rtl = new RelativeLayout(context);
        if (hasTitle() && !hasSubTitle()) {
            RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp2.addRule(RelativeLayout.CENTER_HORIZONTAL);

            if (hasHeadImage()) {
                int top = VDisplayHelper.dp2px(context, 13);
                lp2.setMargins(0, top, 0, 0);
            } else {
                int top = VDisplayHelper.dp2px(context, 28);
                lp2.setMargins(0, top, 0, 0);
            }
            mTitleView.setLayoutParams(lp2);
            rtl.addView(mTitleView);
            parent.addView(rtl);
        } else if (hasTitle() && hasSubTitle()) {
            mSubTitleView = new VTextView(context);
            mSubTitleView.setEnabled(false);
            mSubTitleView.setText(mSubTitle);
            RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp2.addRule(RelativeLayout.CENTER_HORIZONTAL);
            lp3.addRule(RelativeLayout.CENTER_HORIZONTAL);
            if (hasHeadImage() || hasSubTitle()) {
                int top1 = VDisplayHelper.dp2px(context, 13);
                lp2.setMargins(0, top1, 0, 0);

                if (hasSubTitle()) {
                    int top2 = VDisplayHelper.dp2px(context, 2);
                    lp3.setMargins(0, top2, 0, 0);
                    lp3.addRule(RelativeLayout.BELOW, R.id.vmui_dialog_title_id);
                }
            } else {
                int top = VDisplayHelper.dp2px(context, 28);
                lp2.setMargins(0, top, 0, 0);
            }
            mTitleView.setLayoutParams(lp2);
            rtl.addView(mTitleView);
            if (hasSubTitle()) {
                mSubTitleView.setLayoutParams(lp3);
                rtl.addView(mSubTitleView);

            }
            parent.addView(rtl);
        }
    }

    protected void onCreateSheetTitle(VActionSheet sheet, ViewGroup parent, Context context) {
        if (hasTitle()) {
            mTitleView = new VTextView(context, true);
            mTitleView.setEnabled(false);
            mTitleView.setText(mTitle);

            VResHelper.assignTextViewWithAttr(mTitleView, R.attr.sheet_title_style);
            RelativeLayout rtl = new RelativeLayout(context);
            RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
            int lr = VDisplayHelper.dp2px(context, 28);
            int tb = VDisplayHelper.dp2px(context, 16);
            lp2.setMargins(lr, tb, lr, tb);
            lp2.addRule(RelativeLayout.CENTER_HORIZONTAL);
            lp2.addRule(RelativeLayout.CENTER_VERTICAL);

            rtl.setLayoutParams(lp1);
            mTitleView.setLayoutParams(lp2);

            rtl.addView(mTitleView);
            View view = new View(context);
            view.setLayoutParams(lp3);
            view.setBackground(context.getResources().getDrawable(R.drawable.divider_line));
            parent.addView(rtl);
            parent.addView(view);
        }
    }

    /**
     * 创建中间的区域
     */
    protected abstract void onCreateContent(Dialog dialog, ViewGroup parent, Context context);

    /**
     * 创建底部的操作栏区域
     */
    protected void onCreateHandlerBar(final Dialog dialog, ViewGroup parent, Context context) {
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
                VButton actionView = action.buildActionView(dialog, i);

                // add divider
                if (i > 0) {
                    if (mActionContainerOrientation == VERTICAL) {
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
                        View view = new View(context);
                        view.setLayoutParams(lp);
                        view.setBackground(context.getDrawable(R.drawable.divider_line));
                        mActionContainer.addView(view);
                    } else if (mActionContainerOrientation == HORIZONTAL) {
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT);
                        int tb = VDisplayHelper.dp2px(context, 18);
                        lp.setMargins(0, tb, 0, tb);
                        View view = new View(context);
                        view.setLayoutParams(lp);
                        view.setBackgroundColor(context.getResources().getColor(R.color.divider_color));
                        mActionContainer.addView(view);
                    }
                }
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
                                int childPaddingHor = Math.max(0, lastChild.getPaddingLeft() - VDisplayHelper.dp2px(mContext, 3));
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

    protected void onAfter(Dialog dialog, LinearLayout parent, Context context) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.cancelOutSide();
            }
        };
    }
}
