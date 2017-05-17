package com.hut.cwp.mcar.activitys.business.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hut.cwp.mcar.R;

import java.util.List;

/**
 * Created by hutcwp on 2017/5/17.
 * Mail : hutcwp@foxmail.com
 * Blog : hutcwp.club
 * GitHub : github.com/hutcwp
 */

public class AreaSelectAdapter extends ArrayAdapter<String> {

    int resourceID;

    public AreaSelectAdapter(@NonNull Context context, @LayoutRes int resource, List<String> datas) {
        super(context, resource, datas);

        resourceID = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(getContext()).inflate(resourceID, null);
        TextView tvName = (TextView) view.findViewById(R.id.tv_name);
        tvName.setText(getItem(position));
        return view;

    }


}
