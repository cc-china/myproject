package com.my_project.test_more_listview.interfaces;


import com.my_project.test_more_listview.DBModel;
import com.my_project.test_more_listview.model.ItemData;

/**
 * @Author Zheng Haibo
 * @PersonalWebsite http://www.mobctrl.net
 * @Description
 */
public interface ItemDataClickListener {

	public void onExpandChildren(DBModel itemData);

	public void onHideChildren(DBModel itemData);

}
