package com.app.departmentinfos.Adapter;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.departmentinfos.Config;
import com.app.departmentinfos.Fragments.AdminNoticesFragment;
import com.app.departmentinfos.Fragments.AllAssignmentsFragment;
import com.app.departmentinfos.Fragments.CourseNoticesFragment;
import com.app.departmentinfos.Fragments.MyAssignmentsFragment;
import com.app.departmentinfos.Fragments.SectionNoticesFragment;

public class NoticeTabsAdapter extends FragmentPagerAdapter {
    int numberOfTabs;
    String type = "";
    SharedPreferences sharedPreferences;

    public NoticeTabsAdapter(@NonNull FragmentManager fm, int numberOfTabs, Context context) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.numberOfTabs = numberOfTabs;
//        sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        type = sharedPreferences.getString(Config.TYPE, "");
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new SectionNoticesFragment();
            case 1:
                return new CourseNoticesFragment();
            case 2:
                return new AdminNoticesFragment();
            default:
                return new SectionNoticesFragment();

        }


    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}

