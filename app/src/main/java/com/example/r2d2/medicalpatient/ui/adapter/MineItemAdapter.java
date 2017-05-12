package com.example.r2d2.medicalpatient.ui.adapter;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.r2d2.medicalpatient.R;

import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

/**
 * Created by Lollipop on 2017/5/10.
 */

public class MineItemAdapter extends BaseAdapter {
    private List<Map<String, Object>> items;

    public static class ViewHolder{
        AppCompatImageView imageView;
        AppCompatTextView textView;
    }

    public MineItemAdapter(List<Map<String, Object>> items) {
        super();
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Map<String, Object> getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mine_item, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (AppCompatImageView) view.findViewById(R.id.item_img);
            viewHolder.textView = (AppCompatTextView) view.findViewById(R.id.item_text);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (items!=null && items.size()>0) {
            viewHolder.imageView.setImageResource((Integer) items.get(i).get("resourceId"));
            viewHolder.textView.setText((CharSequence) items.get(i).get("text"));
        }
        return view;
    }
}
