package com.my_project.test_more_listview.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.my_project.R;
import com.my_project.test_more_listview.DBModel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017\11\24 0024.
 */

public class OrganizationPersonsAdapter extends RecyclerView.Adapter<OrganizationPersonsAdapter.ViewHolder> {

    private Activity ctx;
    private List<DBModel> mList;

    public OrganizationPersonsAdapter(Activity activity, List<DBModel> mList) {
        this.ctx = activity;
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.item_recycleview_organization_person_list, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvName.setText(mList.get(position).getUserName());
        holder.tvPhone.setText("电话: "+mList.get(position).getPhone());
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_phone)
        TextView tvPhone;
        @Bind(R.id.iv_details)
        ImageView ivDetails;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
