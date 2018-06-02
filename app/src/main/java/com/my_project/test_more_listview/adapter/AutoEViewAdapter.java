package com.my_project.test_more_listview.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.my_project.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017\11\23 0023.
 */

public class AutoEViewAdapter extends ArrayAdapter<String> {

    private Context ctx;

    public AutoEViewAdapter(@NonNull Context context, ArrayList<String> str) {
        super(context, R.layout.view_autocompletete_textview,0,str);
        this.ctx = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            view = LayoutInflater.from(ctx).inflate(R.layout.view_autocompletete_textview, parent, false);
        } else {
            view = convertView;
        }
        TextView textView = (TextView) view.findViewById(R.id.text1);
        textView.setText(getItem(position));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx,"等会我再写点击事件",Toast.LENGTH_SHORT).show();

            }
        });
        return super.getView(position, convertView, parent);
    }
}
