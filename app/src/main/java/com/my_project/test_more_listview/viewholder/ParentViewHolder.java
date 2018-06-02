package com.my_project.test_more_listview.viewholder;

import java.util.List;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.my_project.R;
import com.my_project.test_more_listview.DBModel;
import com.my_project.test_more_listview.activity.OrganizationDetails;
import com.my_project.test_more_listview.interfaces.ItemDataClickListener;
import com.my_project.test_more_listview.model.ItemData;

/**
 * @Author Zheng Haibo
 * @PersonalWebsite http://www.mobctrl.net
 * @Description
 */
public class ParentViewHolder extends BaseViewHolder {

    private Context mContext;
    public ImageView image, ivDetails;
    public TextView text;
    public ImageView expand;
    public TextView count;
    public RelativeLayout relativeLayout;
    private int itemMargin;

    public ParentViewHolder(View itemView, Context mContext) {
        super(itemView);
        image = (ImageView) itemView.findViewById(R.id.image);
        text = (TextView) itemView.findViewById(R.id.text);
        expand = (ImageView) itemView.findViewById(R.id.expand);
        ivDetails = (ImageView) itemView.findViewById(R.id.iv_details);
        count = (TextView) itemView.findViewById(R.id.count);
        relativeLayout = (RelativeLayout) itemView.findViewById(R.id.container);
        itemMargin = itemView.getContext().getResources()
                .getDimensionPixelSize(R.dimen.item_margin);
        this.mContext = mContext;
    }

    public void bindView(final DBModel itemData, final int position,
                         final ItemDataClickListener imageClickListener) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) expand
                .getLayoutParams();
        params.leftMargin = itemMargin * itemData.getTreedepth();
        expand.setLayoutParams(params);
        text.setText(itemData.getUserName());
        if (itemData.isExpand()) {
            expand.setRotation(45);
            List<DBModel> children = itemData.getChildren();
            if (children != null) {
                count.setText(String.format("(%s)", itemData.getChildren()
                        .size()));
            }
            count.setVisibility(View.VISIBLE);
        } else {
            expand.setRotation(0);
            count.setVisibility(View.GONE);
        }
        relativeLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (imageClickListener != null) {
                    if (itemData.isExpand()) {
                        imageClickListener.onHideChildren(itemData);
                        itemData.setExpand(false);
                        rotationExpandIcon(45, 0);
                        count.setVisibility(View.GONE);
                    } else {
                        imageClickListener.onExpandChildren(itemData);
                        itemData.setExpand(true);
                        rotationExpandIcon(0, 45);
                        List<DBModel> children = itemData.getChildren();
                        if (children != null) {
                            count.setText(String.format("(%s)", itemData
                                    .getChildren().size()));
                        }
                        count.setVisibility(View.VISIBLE);
                    }
                }

            }
        });
        image.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(view.getContext(), "longclick",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        ivDetails.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到组织详情
                Intent intent = new Intent(mContext,OrganizationDetails.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("itemData",itemData);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void rotationExpandIcon(float from, float to) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);
            valueAnimator.setDuration(150);
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    expand.setRotation((Float) valueAnimator.getAnimatedValue());
                }
            });
            valueAnimator.start();
        }
    }
}
