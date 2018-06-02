package com.my_project.sqlitedatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2017\11\21 0021.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    //MySQL没有Boolean类型数据，用 a 代表 true ；用 0 代表 false

    private static String DB_NAME = "organization_relationship_db";
    private static int VERSION = 1;
    private static DataBaseHelper mInstance;
    public String USERTABLE = "User_organization_relationship_Table";
    public String id = "id";
    public String userId = "userId";
    public String userName = "userName";
    public String pid = "pid";
    public String pName = "pName";
    public String treeDepth = "treeDepth";
    public String type = "type";
    public String phone = "phone";
    public String oc = "oc";

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建一张数据库表
        String sql = "create table IF NOT EXISTS " + USERTABLE + " ("
                + id + " INTEGER primary key autoincrement not null ,"
                + userId + " INTEGER ,"
                + userName + " VARCHAR ,"
                + pid + " INTEGER ,"
                + pName + " VARCHAR ,"
                + treeDepth + " INTEGER ,"
                + type + " INTEGER ,"
                + phone + " VARCHAR ,"
                + oc + " VARCHAR" + " );";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public synchronized static DataBaseHelper getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new DataBaseHelper(ctx);
        }
        return mInstance;
    }

    /**
     * 查询
     *
     * @param sql
     * @param selectionArgs
     * @return
     */
    public synchronized Cursor query(String sql, String[] selectionArgs) {
        Cursor cursor = getReadableDatabase().rawQuery(sql, selectionArgs);
        return cursor;
    }
}
