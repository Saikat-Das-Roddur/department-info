package com.app.departmentinfos.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.departmentinfos.Fragments.AllAssignmentsFragment;
import com.app.departmentinfos.Fragments.MyAssignmentsFragment;

public class AssignmentTabsAdapter extends FragmentPagerAdapter {
    int numberOfTabs;
    public AssignmentTabsAdapter(@NonNull FragmentManager fm, int numberOfTabs) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.numberOfTabs = numberOfTabs;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new AllAssignmentsFragment();
            case 1:
                return new MyAssignmentsFragment();
            default:
                return new AllAssignmentsFragment();
        }

    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
