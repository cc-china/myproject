package com.my_project.test_refreing_data.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.my_project.R;
import com.my_project.test_refreing_data.view.SlideListView;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nolan on 2017/9/14.
 */

public class TestRefreshingAdapter extends RecyclerView.Adapter<TestRefreshingAdapter.ViewHolder> {
    private Activity ctx;
    private List<String> mList;

    public TestRefreshingAdapter(Activity activity, List<String> mList) {
        this.ctx = activity;
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(ctx, R.layout.item_test_refreshing, null);
        SlideListView slideView = new SlideListView(ctx, view);
        return new ViewHolder(slideView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvName.setText(mList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, "点击了" + mList.get(position) + "条数据", Toast.LENGTH_LONG).show();
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
        @Bind(R.id.back)
        TextView back;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
