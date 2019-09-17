package com.viomi.vmui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.viomi.vmui.R;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends BaseAdapter {
    private Context mContext;
    private List<DataHolder> mData = new ArrayList<>();

    public ListAdapter(Context context, List<String> data) {
        this.mContext = context;
        initData(data);
    }

    private void initData(List<String> data) {
        for (int i = 0; i < data.size(); i++) {
            DataHolder holder = new DataHolder(data.get(i), false);
            mData.add(holder);
        }
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.size() > position ? mData.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getSelectContent() {
        String content = "";
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).checked) {
                content += mData.get(i).strName + ",";
            }
        }
        if (!content.isEmpty()) {
            content = content.substring(0, content.length() - 1);
        }
        return content;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.txtName = convertView.findViewById(R.id.txtName);
            viewHolder.chbChoice = convertView.findViewById(R.id.chbChoice);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txtName.setText(mData.get(position).strName);
        viewHolder.chbChoice.setChecked(mData.get(position).checked);
        return convertView;
    }

    public class ViewHolder {
        TextView txtName;
        CheckBox chbChoice;
    }

    public class DataHolder {
        public String strName;
        public boolean checked;

         DataHolder(String strName, boolean checked) {
            this.strName = strName;
            this.checked = checked;
        }
    }
}
