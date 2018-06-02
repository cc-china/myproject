package com.my_project.test.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.my_project.R;

/**
 * Created by nolan on 2017/8/2.
 */

public class FashionBannerPagerAdapter extends PagerAdapter {
    private Activity ctx;
    private int[] mList;

    public FashionBannerPagerAdapter(Activity activity, int[] mList) {
        this.ctx = activity;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        if (mList != null && mList.length > 0) {
            return mList.length == 1 ? 1 : Integer.MAX_VALUE;
        }
        return 0;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = View.inflate(ctx, R.layout.view_banner, null);
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        icon.setImageResource(mList[position%4]);
//        switch (position) {
//            case 0:
//        icon.setImageResource(R.mipmap.a);
//                break;
//            case 1:
//        icon.setImageResource(R.mipmap.b);
//                break;
//            case 2:
//        icon.setImageResource(R.mipmap.c);
//                break;
//            case 3:
//        icon.setImageResource(R.mipmap.d);
//                break;
//        }
//        Glide.with(ctx).load(mList.get(position % mList.size())).into(icon);
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public int[] getList() {
        return mList;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void clear() {
//        mList.clear();
        notifyDataSetChanged();
    }
}
