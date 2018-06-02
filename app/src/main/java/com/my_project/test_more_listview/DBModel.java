package com.my_project.test_more_listview;

import com.my_project.test_more_listview.model.ItemData;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017\11\21 0021.
 */

public class DBModel implements Serializable{
    private int id;
    private int userId;//用户id
    private String userName;//用户名称
    private int pid;//机构code
    private String pName;//机构名称
    private int treedepth;//层级
    private int type;//0--根节点    a---叶子节点
    private String phone;//手机号
    private String oc;//范围
    private List<DBModel> children;
    private boolean expand;// 是否展开

    public List<DBModel> getChildren() {
        return children;
    }

    public void setChildren(List<DBModel> children) {
        this.children = children;
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    public String getOc() {
        return oc;
    }

    public void setOc(String oc) {
        this.oc = oc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public int getTreedepth() {
        return treedepth;
    }

    public void setTreedepth(int treedepth) {
        this.treedepth = treedepth;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
