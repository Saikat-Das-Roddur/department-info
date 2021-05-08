package com.app.departmentinfos.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.departmentinfos.Fragments.AllCoursesFragment;
import com.app.departmentinfos.Fragments.MyCoursesFragment;

public class CoursesTabsAdapter extends FragmentPagerAdapter {
    int numberOfTabs;
    public CoursesTabsAdapter(@NonNull FragmentManager fm, int numberOfTabs) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.numberOfTabs=numberOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new AllCoursesFragment();
            case 1:
                return new MyCoursesFragment();
            default:
                return new AllCoursesFragment();
        }

    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
