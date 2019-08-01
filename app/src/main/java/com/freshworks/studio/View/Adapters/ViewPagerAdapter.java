package com.freshworks.studio.View.Adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.freshworks.studio.Utility.Common;
import com.freshworks.studio.View.Fragments.FavouriteFragment;
import com.freshworks.studio.View.Fragments.TrendingFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private Fragment[] childFragments;
    private String[] titleFragments;

    public ViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);

        childFragments = new Fragment[]{
                new TrendingFragment(context),
                new FavouriteFragment(context),

        };

        titleFragments = new String[]{Common.Fragment_Trending, Common.Fragment_Favourite};
    }

    @Override
    public Fragment getItem(int position) {
        return childFragments[position];
    }

    @Override
    public int getCount() {
        return childFragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = titleFragments[position];
        return title;
    }
}
