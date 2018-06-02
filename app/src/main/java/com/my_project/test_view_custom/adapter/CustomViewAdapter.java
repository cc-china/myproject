package com.my_project.test_view_custom.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.my_project.R;
import com.my_project.test_view_custom.activity.CustomAdjustVolumeActivity;
import com.my_project.test_view_custom.activity.CustomAttributeActivity;
import com.my_project.test_view_custom.activity.CustomPolygonActivity;
import com.my_project.test_view_custom.activity.CustomViewGrounpActivity;
import com.my_project.test_view_custom.activity.CustomViewSlidingPuzzleActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017\12\7 0007.
 */

public class CustomViewAdapter extends RecyclerView.Adapter<CustomViewAdapter.ViewHolder> {
    private final Activity ctx;
    private final List<String> mList;

    public CustomViewAdapter(Activity activity, List<String> mList) {
        this.ctx = activity;
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.item_recycleview_custom, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvName.setText(mList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mList.get(position)) {
                    case "自定义正七边形view":
                        ctx.startActivity(new Intent(ctx, CustomPolygonActivity.class));
                        break;
                    case "自定义属性":
                        ctx.startActivity(new Intent(ctx,CustomAttributeActivity.class));
                        break;
                    case "音量调节的自定义view":
                        ctx.startActivity(new Intent(ctx,CustomAdjustVolumeActivity.class));
                        break;
                    case "ViewGrounp自定义view":
                        ctx.startActivity(new Intent(ctx,CustomViewGrounpActivity.class));
                        break;
                        case "仿斗鱼拼图验证码":
                        ctx.startActivity(new Intent(ctx,CustomViewSlidingPuzzleActivity.class));
                        break;
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_name)
        TextView tvName;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
