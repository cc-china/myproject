package com.my_project.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.my_project.R;
import com.my_project.test_rx_java.retrofit.RetrofitUtils;
import com.my_project.utils.OpenJsonFileUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017\11\13 0013.
 */

public class DynamicFragment extends Fragment{
    @Bind(R.id.tv_test_name)
    TextView tvTestName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_text_xml, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        String school = OpenJsonFileUtils.getJson(getActivity(), "school.json");
        tvTestName.setText(school);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
