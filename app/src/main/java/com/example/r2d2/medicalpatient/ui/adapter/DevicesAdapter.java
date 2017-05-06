package com.example.r2d2.medicalpatient.ui.adapter;

import android.content.Context;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by Lollipop on 2017/5/5.
 */

public class DevicesAdapter extends SimpleAdapter {
    private List<Map<String, String>> devices;
    public DevicesAdapter(Context context, List<Map<String, String>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.devices = data;
    }

    @Override
    public Map<String, String> getItem(int position) {
        return devices.get(position);
    }

    @Override
    public int getCount() {
        return devices.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
