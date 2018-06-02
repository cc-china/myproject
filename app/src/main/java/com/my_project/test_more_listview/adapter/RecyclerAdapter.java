package com.my_project.test_more_listview.adapter;

/**
 * Created by Administrator on 2017\11\17 0017.
 */

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.my_project.R;
import com.my_project.sqlitedatabase.SQLiteDB;
import com.my_project.test_more_listview.DBModel;
import com.my_project.test_more_listview.interfaces.ItemDataClickListener;
import com.my_project.test_more_listview.interfaces.OnScrollToListener;
import com.my_project.test_more_listview.model.ItemData;
import com.my_project.test_more_listview.viewholder.BaseViewHolder;
import com.my_project.test_more_listview.viewholder.ChildViewHolder;
import com.my_project.test_more_listview.viewholder.ParentViewHolder;
import com.my_project.utils.OpenJsonFileUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @Author Zheng Haibo
 * @PersonalWebsite http://www.mobctrl.net
 * @Description
 */
public class RecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Context mContext;
    private List<DBModel> mDataSet;
    private OnScrollToListener onScrollToListener;

    public void setOnScrollToListener(OnScrollToListener onScrollToListener) {
        this.onScrollToListener = onScrollToListener;
    }

    public RecyclerAdapter(Context context) {
        mContext = context;
        mDataSet = new ArrayList<>();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType ) {
        View view = null;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(mContext).inflate(
                        R.layout.item_recycler_child, parent, false);
                return new ChildViewHolder(view,mContext);
            case 1:
                view = LayoutInflater.from(mContext).inflate(
                        R.layout.item_recycler_parent, parent, false);
                return new ParentViewHolder(view,mContext);
            default:
                view = LayoutInflater.from(mContext).inflate(
                        R.layout.item_recycler_child, parent, false);
                return new ChildViewHolder(view,mContext);
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        switch (getItemViewType(position)) {
//            0--根节点    a---叶子节点
            case 0:
                ChildViewHolder textViewHolder = (ChildViewHolder) holder;
                textViewHolder.bindView(mDataSet.get(position), position);

                break;
            case 1:
                ParentViewHolder imageViewHolder = (ParentViewHolder) holder;
                imageViewHolder.bindView(mDataSet.get(position), position,
                        imageClickListener);
                break;
            default:
                break;
        }
    }

    private ItemDataClickListener imageClickListener = new ItemDataClickListener() {

        @Override
        public void onExpandChildren(DBModel itemData) {
            int position = getCurrentPosition(itemData.getId());
//            List<ItemData> children = getChildrenByPath(itemData.getPath(),
//                    itemData.getTreeDepth());

            List<DBModel> children = getChildrenByPath(itemData);
            Log.d("tag----children", children.toString());
            if (children == null) {
                return;
            }
            addAll(children, position + 1);// 插入到点击点的下方
            itemData.setChildren(children);
            if (onScrollToListener != null) {
                onScrollToListener.scrollTo(position + 1);
            }
        }

        @Override
        public void onHideChildren(DBModel itemData) {
            int position = getCurrentPosition(itemData.getId());
            List<DBModel> children = itemData.getChildren();
            if (children == null) {
                return;
            }
            removeAll(position + 1, getChildrenCount(itemData) - 1);
            if (onScrollToListener != null) {
                onScrollToListener.scrollTo(position);
            }
            itemData.setChildren(null);
        }
    };

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    private int getChildrenCount(DBModel item) {
        List<DBModel> list = new ArrayList<DBModel>();
        printChild(item, list);
        return list.size();
    }

    private void printChild(DBModel item, List<DBModel> list) {
        list.add(item);
        if (item.getChildren() != null) {
            for (int i = 0; i < item.getChildren().size(); i++) {
                printChild(item.getChildren().get(i), list);
            }
        }
    }


    public List<DBModel> getChildrenByPath(DBModel itemData) {
        List<DBModel> list = new ArrayList<DBModel>();
        SQLiteDB sqLiteDB = new SQLiteDB(mContext);
        sqLiteDB.openDB();
        //查询根节点
        List<DBModel> noChildrenList = sqLiteDB.findTableRootNode(itemData.getId());
        //查询叶子节点
//        List<DBModel> hasChildrenList = sqLiteDB.findAllOrgBook(itemData.getTreedepth(), null);
        list.addAll(noChildrenList);
//        list.addAll(hasChildrenList);
        sqLiteDB.destoryInstance();

        return list;
    }

    private void parseData(String school) {
        try {
            JSONArray jb = new JSONArray(school);
            if (jb.length() > 0) {
                for (int i = 0; i < jb.length(); i++) {
                    JSONObject object = jb.getJSONObject(i);
                    String name = object.getString("name");
                    String pid = object.getString("pid");
                    String id = object.getString("id");
                    boolean hasChildren = object.getBoolean("hasChildren");
                    int treeDepth = object.getInt("treeDepth");
                    int type = object.getInt("type");
                    JSONArray personData = object.getJSONArray("personData");
                    JSONArray children = object.getJSONArray("children");
                    if (personData.length() > 0) {

                    }
                    if (hasChildren && children.length() > 0) {

                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据路径获取子目录或文件
     *
     * @param path
     * @param treeDepth
     * @return
     */
    public List<ItemData> getChildrenByPath(String path, int treeDepth) {
        treeDepth++;
        try {
            //初始化目录结构集合
            List<ItemData> list = new ArrayList<>();
            File file = new File(path);
            File[] children = file.listFiles();
            //初始化文件结构集合
            List<ItemData> fileList = new ArrayList<>();
            for (File child : children) {
                if (child.isDirectory()) {
                    list.add(new ItemData(ItemData.ITEM_TYPE_PARENT, child
                            .getName(), child.getAbsolutePath(), UUID
                            .randomUUID().toString(), treeDepth, null));
                } else {
                    fileList.add(new ItemData(ItemData.ITEM_TYPE_CHILD, child
                            .getName(), child.getAbsolutePath(), UUID
                            .randomUUID().toString(), treeDepth, null));
                }
            }
            Collections.sort(list);
            Collections.sort(fileList);
            list.addAll(fileList);
            return list;
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 从position开始删除，删除
     *
     * @param position
     * @param itemCount 删除的数目
     */
    protected void removeAll(int position, int itemCount) {
        for (int i = 0; i < itemCount; i++) {
            mDataSet.remove(position);
        }
        notifyItemRangeRemoved(position, itemCount);
    }

    protected int getCurrentPosition(int uuid) {
        for (int i = 0; i < mDataSet.size(); i++) {
            if (uuid == mDataSet.get(i).getId()) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getItemViewType(int position) {
        return mDataSet.get(position).getType();
    }

    public void add(DBModel text, int position) {
        mDataSet.add(position, text);
        notifyItemInserted(position);
    }

    public void addAll(List<DBModel> list, int position) {
        mDataSet.addAll(position, list);
        //添加数据伴随默认的动画效果
        notifyItemRangeInserted(position, list.size());
    }

    public void delete(int pos) {
        if (pos >= 0 && pos < mDataSet.size()) {
            if (mDataSet.get(pos).getType() == ItemData.ITEM_TYPE_PARENT
                    && mDataSet.get(pos).isExpand()) {// 父组件并且子节点已经展开
                for (int i = 0; i < mDataSet.get(pos).getChildren().size() + 1; i++) {
                    mDataSet.remove(pos);
                }
                notifyItemRangeRemoved(pos, mDataSet.get(pos).getChildren()
                        .size() + 1);
            } else {// 孩子节点，或没有展开的父节点
                mDataSet.remove(pos);
                notifyItemRemoved(pos);
            }
        }
    }
}

