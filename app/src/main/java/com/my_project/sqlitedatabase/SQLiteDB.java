package com.my_project.sqlitedatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.my_project.test_more_listview.DBModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2017\11\21 0021.
 */

public class SQLiteDB {
    private Context ctx;
    private static DataBaseHelper openHelper;
    private SQLiteDatabase db;

    public SQLiteDB(Context ctx) {
        this.ctx = ctx;

    }

    public void openDB() {
        /**
         * 创建数据库
         */
        openHelper = DataBaseHelper.getInstance(ctx);
        if (db != null) {
            return;
        }
        try {
            db = openHelper.getWritableDatabase();
        } catch (Exception e) {
            db = openHelper.getReadableDatabase();
        }
    }

    /**
     * 插入语句
     */
    public void insert(DBModel model) {
        ContentValues values = putContentValues(model);
        db.insert(openHelper.USERTABLE, null, values);
        Toast.makeText(ctx, "插入一条数据", Toast.LENGTH_SHORT).show();
    }

    /**
     * 查找最高层
     *
     * @return
     */
    public DBModel findFirstLevelTableData() {
        StringBuffer sql = new StringBuffer();
        DBModel user = null;
        sql.append(" select * from ").append(openHelper.USERTABLE).append(" order by treeDepth limit 1");
        Cursor cursor = openHelper.query(sql.toString(), null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                user = getObjectData(cursor);
            }
        }
        cursor.close();
        return user;
    }

    private DBModel getObjectData(Cursor cursor) {
        int i = 0;
        DBModel module = new DBModel();
//        module.setId(cursor.isNull(i) ? null : cursor.getInt(i));
//        module.setUserId(cursor.getInt(i++));
//        module.setUserName(cursor.getString(i++));
//        module.setPid(cursor.getInt(i++));
//        module.setpName(cursor.getString(i++));
//        module.setTreedepth(cursor.getInt(i++));
//        module.setType(cursor.getInt(i++));
//        module.setPhone(cursor.getString(i++));
//        module.setOc(cursor.getString(i++));

        //根据列名获取列索引

        module.setId(cursor.getInt(cursor.getColumnIndex("id")));
        module.setUserId(cursor.getInt(cursor.getColumnIndex("userId")));
        module.setUserName(cursor.getString(cursor.getColumnIndex("userName")));
        module.setPid(cursor.getInt(cursor.getColumnIndex("pid")));
        module.setpName(cursor.getString(cursor.getColumnIndex("pName")));
        module.setTreedepth(cursor.getInt(cursor.getColumnIndex("treeDepth")));
        module.setType(cursor.getInt(cursor.getColumnIndex("type")));
        module.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
        module.setOc(cursor.getString(cursor.getColumnIndex("oc")));
        return module;

    }

    /**
     * 查找根节点
     *
     * @param
     * @return
     */
    public List<DBModel> findTableRootNode(int pid) {
        List<DBModel> list = new ArrayList<DBModel>();
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from ").append(openHelper.USERTABLE).append(" where pid = ").append(pid);
        Cursor cursor = openHelper.query(sql.toString(), null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                DBModel user = getObjectData(cursor);
                list.add(user);
            }
        }
        cursor.close();


        return list;
    }

    /**
     * 查找叶子节点
     *
     * @param
     * @param
     * @return
     */
    public List<DBModel> findAllOrgBook(int level, String oc) {
        List<DBModel> list = new ArrayList<DBModel>();
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from ").append(openHelper.USERTABLE);
        if (!TextUtils.isEmpty(oc)) {
            sql.append(" where oc like '%").append(oc).append("%'");
        }
        sql.append(" group by  oc");
        Cursor cursor = openHelper.query(sql.toString(), null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                DBModel user = getObjectData(cursor);
                String op = user.getOc();
                if (!TextUtils.isEmpty(op)) {
                    String[] ops = op.split("-");
                    level = findOrgBookLevel(level, oc);
                    if (ops.length - level == 1) {
//                        user.setOrg(true);
                        list.add(user);
                    }
                }
            }
        }
        cursor.close();
        return list;
    }

    public int findOrgBookLevel(int level, String oc) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from ").append(openHelper.USERTABLE);
        if (!TextUtils.isEmpty(oc)) {
            sql.append(" where oc like '%").append(oc).append("%'");
        }
        sql.append(" where treeDepth > ").append(level);
        sql.append(" order by treeDepth limit a");
        Cursor cursor = openHelper.query(sql.toString(), null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToNext()) {
                DBModel user = getObjectData(cursor);
                level = user.getTreedepth() - 1;
            }
        }
        cursor.close();
        return level;
    }


    private ContentValues putContentValues(DBModel model) {
        ContentValues values = new ContentValues();
        values.put(openHelper.id, model.getId());
        values.put(openHelper.userId, model.getUserId());
        values.put(openHelper.userName, model.getUserName());
        values.put(openHelper.pid, model.getPid());
        values.put(openHelper.pName, model.getpName());
        values.put(openHelper.treeDepth, model.getTreedepth());
        values.put(openHelper.type, model.getType());
        values.put(openHelper.phone, model.getPhone());
        values.put(openHelper.oc, model.getOc());
        return values;
    }

    public void destoryInstance() {
        if (openHelper != null) {
            openHelper.close();
        }
    }

    /**
     * TODO -- 查询单条数据
     */
    public DBModel findOneData(String text) {
        StringBuffer sql = new StringBuffer();
        DBModel user = null;
        sql.append(" select * from ").append(openHelper.USERTABLE).append(" where userName =" + "'").append(text).append("'");
        Cursor cursor = openHelper.query(sql.toString(), null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                user = getObjectData(cursor);
            }
        }
        cursor.close();
        return user;
    }

    /**
     * 查询表中所有数据
     */
    public List<DBModel> findAllData() {
        StringBuffer sql = new StringBuffer();
        List<DBModel> list = new ArrayList<>();
        DBModel model = null;
        sql.append("select * from ").append(openHelper.USERTABLE);
        Cursor cursor = openHelper.query(sql.toString(), null);
        while (cursor.moveToNext()) {
            model = getObjectData(cursor);
            list.add(model);
        }
        return list;
    }

    /**
     * 根据模糊字符查询表数据
     */
    public List<DBModel> findSomeOneData(String text) {
        StringBuffer sql = new StringBuffer();
        List<DBModel> list = new ArrayList<>();
        DBModel model = null;
        sql.append("select * from ").append(openHelper.USERTABLE)
                .append(" where " + openHelper.userName + " like ").append("'%").append(text).append("%'");
        Cursor cursor = openHelper.query(sql.toString(), null);
        while (cursor.moveToNext()) {
            model = getObjectData(cursor);
            list.add(model);
        }
        return list;
    }
}
