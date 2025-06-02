package com.example.mywordle.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mywordle.FragmentMain;
import com.example.mywordle.FragmentSettings;
import com.example.mywordle.FragmentStatistics;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FragmentStatistics();
            case 1:
                return new FragmentMain();
            case 2:
                return new FragmentSettings();
            default:
                // Если вдруг position вышел за [0..2], то бросаем исключение:
                throw new IllegalArgumentException("Invalid position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
