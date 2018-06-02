package com.my_project.test_more_listview.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.my_project.R;
import com.my_project.test_more_listview.DBModel;
import com.my_project.test_more_listview.activity.OrganizationDetails;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017\11\27 0027.
 */

public class SearchOrganizationAdapter extends RecyclerView.Adapter<SearchOrganizationAdapter.ViewHolder> {
    private Activity ctx;
    private List<DBModel> mList;

    public SearchOrganizationAdapter(Activity activity) {
        this.ctx = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.item_recycleview_organization_search, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvName.setText(mList.get(position).getUserName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx,OrganizationDetails.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("itemData",mList.get(position));
                intent.putExtras(bundle);
                ctx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void setData(List<DBModel> data) {
        this.mList = data;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.iv_details)
        ImageView ivDetails;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
