package com.my_project.test_PreferenceDemoActivity;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.my_project.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by cmm on 2020/3/6.
 */

public class PreferenceDemoFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesMode(MODE_PRIVATE);
        addPreferencesFromResource(R.xml.preference);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Preference one = findPreference("one");
        one.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getActivity(), "one", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        findPreference("two").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getActivity(), "two", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        findPreference("three").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getActivity(), "再点一下试试", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        findPreference("four").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getActivity(), "试试就试试", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }


}
