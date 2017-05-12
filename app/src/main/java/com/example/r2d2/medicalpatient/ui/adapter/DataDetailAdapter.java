package com.example.r2d2.medicalpatient.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.r2d2.medicalpatient.R;
import com.example.r2d2.medicalpatient.data.realm.Data;

import io.realm.OrderedRealmCollection;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * 修改自RealmBaseAdapter
 * Created by Lollipop on 2017/5/9.
 */

public class DataDetailAdapter extends BaseAdapter {

    @Nullable
    protected OrderedRealmCollection<Data> adapterData;
    private final RealmChangeListener<OrderedRealmCollection<Data>> listener;
    private String tag;
    private TextView currentData;
    private TextView dataUnit;

    private String dataValue = new String();
    private String unit;
    private int color = 0;

    public DataDetailAdapter(@Nullable OrderedRealmCollection<Data> data, String tag, TextView currentData, TextView dataUnit) {
        if (data != null && !data.isManaged())
            throw new IllegalStateException("Only use this adapter with managed list, " +
                    "for un-managed lists you can just use the BaseAdapter");
        this.adapterData = data;
        this.tag = tag;
        this.currentData = currentData;
        this.dataUnit = dataUnit;
        this.listener = new RealmChangeListener<OrderedRealmCollection<Data>>() {
            @Override
            public void onChange(OrderedRealmCollection<Data> results) {
                updateCurrentData(results.get(results.size()-1));
                notifyDataSetChanged();
            }
        };

        if (data != null) {
            addListener(data);
        }
    }

    private void updateCurrentData(Data item) {
        setParams(item, currentData.getContext());
        currentData.setText(dataValue);
        currentData.setTextColor(color);
        dataUnit.setText(unit);
    }

    private void addListener(@NonNull OrderedRealmCollection<Data> data) {
        if (data instanceof RealmResults) {
            RealmResults<Data> results = (RealmResults<Data>) data;
            //noinspection unchecked
            results.addChangeListener((RealmChangeListener) listener);
        } else if (data instanceof RealmList) {
            RealmList<Data> list = (RealmList<Data>) data;
            //noinspection unchecked
            list.addChangeListener((RealmChangeListener) listener);
        } else {
            throw new IllegalArgumentException("RealmCollection not supported: " + data.getClass());
        }
    }

    private void removeListener(@NonNull OrderedRealmCollection<Data> data) {
        if (data instanceof RealmResults) {
            RealmResults<Data> results = (RealmResults<Data>) data;
            //noinspection unchecked
            results.removeChangeListener((RealmChangeListener) listener);
        } else if (data instanceof RealmList) {
            RealmList<Data> list = (RealmList<Data>) data;
            //noinspection unchecked
            list.removeChangeListener((RealmChangeListener) listener);
        } else {
            throw new IllegalArgumentException("RealmCollection not supported: " + data.getClass());
        }
    }

    /**
     * Returns how many items are in the data set.
     *
     * @return the number of items.
     */
    @Override
    public int getCount() {
        if (adapterData == null) {
            return 0;
        }
        return adapterData.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     * data set.
     * @return The data at the specified position.
     */
    @Override
    @Nullable
    public Data getItem(int position) {
        if (adapterData == null) {
            return null;
        }
        return adapterData.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list. Note that item IDs are not stable so you
     * cannot rely on the item ID being the same after {@link #notifyDataSetChanged()} or
     * {@link #updateData(OrderedRealmCollection)} has been called.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        // TODO: find better solution once we have unique IDs
        return position;
    }

    /**
     * Updates the data associated with the Adapter.
     *
     * Note that RealmResults and RealmLists are "live" views, so they will automatically be updated to reflect the
     * latest changes. This will also trigger {@code notifyDataSetChanged()} to be called on the adapter.
     *
     * This method is therefore only useful if you want to display data based on a new query without replacing the
     * adapter.
     *
     * @param data the new {@link OrderedRealmCollection} to display.
     */
    @SuppressWarnings("WeakerAccess")
    public void updateData(@Nullable OrderedRealmCollection<Data> data) {
        if (listener != null) {
            if (adapterData != null) {
                removeListener(adapterData);
            }
            if (data != null) {
                addListener(data);
            }
        }

        this.adapterData = data;
        notifyDataSetChanged();
    }

    private static class ViewHolder{
        TextView dateText;
        TextView dataText;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.data_item,viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.dateText = (TextView) view.findViewById(R.id.data_date);
            viewHolder.dataText = (TextView) view.findViewById(R.id.data_value);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (adapterData != null){
            Data item = adapterData.get(adapterData.size()-position-1);
            viewHolder.dateText.setText(item.getCreate_time());
            setParams(item, viewGroup.getContext());
            viewHolder.dataText.setText(dataValue);
            viewHolder.dataText.setTextColor(color);
        }
        return view;
    }

    private void setParams(Data item, Context context){
        switch (tag){
            case "pressure":
                dataValue = item.getPressure()+"";
                unit = "Pa";
                color = ContextCompat.getColor(context, R.color.simpleBlue);
                break;
            case "temperature":
                dataValue = item.getTemperature()+"";
                unit = "℃";
                color = ContextCompat.getColor(context, R.color.simpleOrange);
                break;
            case "pulse":
                dataValue = item.getPulse()+"";
                unit = "次/分";
                color = ContextCompat.getColor(context, R.color.simpleGreen);
                break;
            case "angle":
                dataValue = item.getAngle()+"";
                unit = "度";
                color = ContextCompat.getColor(context, R.color.simpleAmber);
                break;
            default:
                dataValue = "0";
                unit = "";
                color = ContextCompat.getColor(context, R.color.white);
        }
    }
}
