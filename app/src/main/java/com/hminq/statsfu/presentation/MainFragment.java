package com.hminq.statsfu.presentation;

import static com.hminq.statsfu.presentation.constants.ResourceConstants.IC_TAB_1;
import static com.hminq.statsfu.presentation.constants.ResourceConstants.IC_TAB_2;
import static com.hminq.statsfu.presentation.constants.ResourceConstants.IC_TAB_3;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.hminq.statsfu.R;
import com.hminq.statsfu.databinding.FragmentMainBinding;

public class MainFragment extends Fragment {
    private FragmentMainBinding binding;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;

    public MainFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        viewPager = binding.viewPager;
        tabLayout = binding.tabLayout;

        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            //set tab icon
            switch (position) {
                case 0:
                    tab.setIcon(IC_TAB_1); // tab1 always selected by default
                    break;
                case 1:
                    tab.setIcon(IC_TAB_2);
                    break;
                case 2:
                    tab.setIcon(IC_TAB_3);
                    break;
                default:
                    break;
            }
        }).attach();


        return binding.getRoot();
    }
}