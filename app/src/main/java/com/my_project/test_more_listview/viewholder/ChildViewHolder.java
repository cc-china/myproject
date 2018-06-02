package com.my_project.test_more_listview.viewholder;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.my_project.R;
import com.my_project.sqlitedatabase.SQLiteDB;
import com.my_project.test_more_listview.DBModel;
import com.my_project.test_more_listview.activity.OrganizationDetails;
import com.my_project.test_more_listview.activity.OrganizationPersonsListActivity;
import com.my_project.test_more_listview.model.ItemData;

/**
 * @Author Zheng Haibo
 * @PersonalWebsite http://www.mobctrl.net
 * @Description
 */
public class ChildViewHolder extends BaseViewHolder {

	private Context mContext;
	public TextView text;
	public ImageView image,ivDetails;
	public RelativeLayout relativeLayout;
	private int itemMargin;
	private int offsetMargin;

	public ChildViewHolder(View itemView, Context mContext) {
		super(itemView);
		text = (TextView) itemView.findViewById(R.id.text);
		image = (ImageView) itemView.findViewById(R.id.image);
		ivDetails = (ImageView) itemView.findViewById(R.id.iv_details);
		relativeLayout = (RelativeLayout) itemView.findViewById(R.id.container);
		itemMargin = itemView.getContext().getResources()
				.getDimensionPixelSize(R.dimen.item_margin);
		offsetMargin = itemView.getContext().getResources()
				.getDimensionPixelSize(R.dimen.expand_size);
		this.mContext=mContext;
	}

	public void bindView(final DBModel itemData, int position) {
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) image
				.getLayoutParams();
		params.leftMargin = itemMargin * itemData.getTreedepth() + offsetMargin;
		image.setLayoutParams(params);
		text.setText(itemData.getUserName());
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
		relativeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				//查询叶子节点下的人员名单，赋值给人员列表适配器
				SQLiteDB sqLiteDB = new SQLiteDB(mContext);
				sqLiteDB.openDB();
				List<DBModel> data = sqLiteDB.findTableRootNode(itemData.getId());
				Intent intent = new Intent(mContext,OrganizationPersonsListActivity.class);
				intent.putExtra("personData",(Serializable)data);
				mContext.startActivity(intent);
				sqLiteDB.destoryInstance();
			}
		});
	}

	private void openFileInSystem(String path, Context context) {
		try {
			MimeTypeMap myMime = MimeTypeMap.getSingleton();
			Intent newIntent = new Intent(Intent.ACTION_VIEW);
			String mimeType = myMime.getMimeTypeFromExtension(fileExt(path)
					.substring(1));
			newIntent.setDataAndType(Uri.fromFile(new File(path)), mimeType);
			newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(newIntent);
		} catch (Exception e) {
			Toast.makeText(context, "No handler for this type of file.",
					Toast.LENGTH_LONG).show();
		}
	}

	@SuppressLint("DefaultLocale")
	private String fileExt(String url) {
		if (url.indexOf("?") > -1) {
			url = url.substring(0, url.indexOf("?"));
		}
		if (url.lastIndexOf(".") == -1) {
			return null;
		} else {
			String ext = url.substring(url.lastIndexOf("."));
			if (ext.indexOf("%") > -1) {
				ext = ext.substring(0, ext.indexOf("%"));
			}
			if (ext.indexOf("/") > -1) {
				ext = ext.substring(0, ext.indexOf("/"));
			}
			return ext.toLowerCase();
		}
	}

}
