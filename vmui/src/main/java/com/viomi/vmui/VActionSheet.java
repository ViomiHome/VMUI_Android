package com.viomi.vmui;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.viomi.vmui.adapter.GridAdapter;
import com.viomi.vmui.Dialog.VDialog;
import com.viomi.vmui.Dialog.VDialogBuilder;
import com.viomi.vmui.utils.VDisplayHelper;

import java.util.ArrayList;
import java.util.List;

public class VActionSheet extends VDialog {

    public VActionSheet(@NonNull Context context) {
        super(context, R.style.VMUI_ActionSheet);
    }

    public VActionSheet(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected VActionSheet(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
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

        public MultiButtonActionSheetBuilder addItem(String itemName) {
            mData.add(itemName);
            return this;
        }

        public MultiButtonActionSheetBuilder setOnSheetItemClickListener(OnSheetItemClickListener listener) {
            onSheetItemClickListener = listener;
            return this;
        }

        @Override
        protected void onCreateContent(Dialog dialog, ViewGroup parent, Context context) {
            mDialog = dialog;
            mListView = new ListView(context);
            mAdapter = new ArrayAdapter<String>(context, R.layout.sheet_item, mData);
            mListView.setAdapter(mAdapter);

            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mListView.setLayoutParams(lp);
            mListView.setAdapter(mAdapter);
            mListView.setVerticalScrollBarEnabled(false);
            mListView.setDivider(getBaseContext().getDrawable(R.color.divider_gray));
            mListView.setDividerHeight(1);
            mListView.setSelector(getBaseContext().getResources().getDrawable(R.color.transparent));
            mListView.setOnItemClickListener(this);
            parent.addView(mListView);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (onSheetItemClickListener != null) {
                onSheetItemClickListener.onItemClick(mDialog, mAdapter.getItem(position).toString());
            }
        }

        public interface OnSheetItemClickListener {
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

        public ShareSheetDialogBuilder setOnSheetItemClickListener(OnSheetItemClickListener listener) {
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
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, VDisplayHelper.dp2px(context, 126));
            lp.setMargins(36, 0, 36, 0);
            mGridView.setLayoutParams(lp);
            mGridView.setSelector(getBaseContext().getDrawable(R.color.transparent));
            mGridView.setOnItemClickListener(this);
            parent.addView(mGridView);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (onSheetItemClickListener != null) {
                onSheetItemClickListener.onItemClick(mDialog, mAdapter.getItemName(position));
            }
        }

        public interface OnSheetItemClickListener {
            void onItemClick(Dialog dialog, String selected);
        }
    }
}
