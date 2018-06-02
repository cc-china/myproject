package com.my_project.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.my_project.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017\11\13 0013.
 */

public class MessageFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_message, null);
        View id = view.findViewById(R.id.btn_test);

        id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = "h";
                switch (s) {
                    case "h":
                        System.out.print("");
                        Toast.makeText(getActivity(),"哈哈",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
