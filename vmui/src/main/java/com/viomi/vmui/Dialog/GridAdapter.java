package com.viomi.vmui.Dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.viomi.vmui.R;

import java.util.ArrayList;
import java.util.List;

public class GridAdapter extends BaseAdapter {
    private Context mContext;
    private List<DataHolder> mData = new ArrayList<>();

    public GridAdapter(Context context, List<DataHolder> list) {
        this.mContext = context;
        this.mData = list;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getItemName(int position){
        return mData.get(position).name;
    }

    public void addItem(String name,int resId){
        DataHolder dataHolder = new DataHolder(name, resId);
        mData.add(dataHolder);
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = convertView.findViewById(R.id.txtName);
            viewHolder.imageView = convertView.findViewById(R.id.imgLogo);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(mData.get(position).name);
        viewHolder.imageView.setImageResource(mData.get(position).resId);
        return convertView;
    }

    public class ViewHolder {
        ImageView imageView;
        TextView textView;
    }

    public class DataHolder {
        String name;
        int resId;

        public DataHolder(String name, int resId) {
            this.name = name;
            this.resId = resId;
        }
    }
}
