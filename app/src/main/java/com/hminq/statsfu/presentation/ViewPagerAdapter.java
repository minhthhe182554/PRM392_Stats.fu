package com.hminq.statsfu.presentation;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.hminq.statsfu.presentation.firsttab.FirstTabFragment;
import com.hminq.statsfu.presentation.secondtab.SecondTabFragment;
import com.hminq.statsfu.presentation.thirdtab.ThirdTabFragment;


public class ViewPagerAdapter extends FragmentStateAdapter {
    private static final int TAB_NUMS = 3;

    public ViewPagerAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FirstTabFragment();
            case 1:
                return new SecondTabFragment();
            case 2:
                return new ThirdTabFragment();
            default:
                throw new IllegalArgumentException("Invalid tab position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return TAB_NUMS;
    }
}
