package com.my_project.test_image_choose.adapter;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.my_project.R;
import com.my_project.test_image_choose.interfaces.AddMyDataImagerClickInterface;
import com.my_project.test_image_choose.interfaces.DeletePhotosListener;
import com.my_project.test_image_choose.model.PhotoInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nolan on 2017/9/10.
 */

public class AddMyDataImageAdapter extends RecyclerView.Adapter<AddMyDataImageAdapter.ViewHolder> {
    private int type;
    private Activity ctx;
    private AddMyDataImagerClickInterface addMyDataImagerClickInterface;
    private List<PhotoInfo> mData = new ArrayList<>();
    private int photo_size;

    public AddMyDataImageAdapter(Activity activity, int type, List<PhotoInfo> photosLists, int photoSize) {
        this.ctx = activity;
        this.type = type;
        this.mData = photosLists;
        this.photo_size = photoSize;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(ctx, R.layout.item_recycleview_add_phone, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (TextUtils.equals("default", mData.get(position).getPhotoPath())) {
            holder.ivDelete.setVisibility(View.GONE);
//            Glide.with(ctx).load(R.mipmap.icon_pic_add).centerCrop().into(holder.ivHeadImg);
            holder.ivHeadImg.setImageBitmap(BitmapFactory.decodeResource(
                    ctx.getResources(), R.mipmap.icon_pic_add));
            holder.ivHeadImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addMyDataImagerClickInterface.onItemClick(position);
                }
            });
        } else {
            if (type == 1) {
                holder.ivDelete.setVisibility(View.GONE);
            } else {
                holder.ivDelete.setVisibility(View.VISIBLE);
            }
//            Glide.with(ctx).load(mData.get(position).getPhotoPath()).placeholder(R.mipmap.default_head).error(R.mipmap.default_head).
//                    centerCrop().into(holder.ivHeadImg);
            holder.ivHeadImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addMyDataImagerClickInterface.onItemClick(position);
                }
            });
        }
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mData.get(position).getPhotoPath().startsWith("http")) {
                    if (null != deletePhotosListener) {
                        deletePhotosListener.deletePhoto(mData.get(position).getId() + "");
                    }
                }
                mData.remove(position);
                if (mData.size() <= 0) {
                    PhotoInfo photoInfo = new PhotoInfo();
                    photoInfo.setPhotoPath("default");
                    mData.add(photoInfo);
                    if (null != deletePhotosListener) {
                        deletePhotosListener.photoPosition(position);
                    }
                } else {
                    if (position == photo_size - 1) {
                        PhotoInfo photoInfo = new PhotoInfo();
                        photoInfo.setPhotoPath("default");
                        mData.add(photoInfo);
                    }
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public void setOnItemClickListener(AddMyDataImagerClickInterface addMyDataImagerClickInterface) {
        this.addMyDataImagerClickInterface = addMyDataImagerClickInterface;
    }

    public DeletePhotosListener deletePhotosListener;

    public void setDeletePhotosListener(DeletePhotosListener deletePhotosListener) {
        this.deletePhotosListener = deletePhotosListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_photo)
        ImageView ivHeadImg;
        @Bind(R.id.iv_delete)
        ImageView ivDelete;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
