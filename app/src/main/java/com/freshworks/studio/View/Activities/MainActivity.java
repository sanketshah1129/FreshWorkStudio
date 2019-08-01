package com.freshworks.studio.View.Activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.freshworks.studio.R;
import com.freshworks.studio.View.Adapters.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;

        final ViewPager viewPager = findViewById(R.id.view_pager);
        final ViewPagerAdapter adapter = new ViewPagerAdapter(context, getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {

                page.setTranslationX(-position * page.getWidth());

                if (position < -1) {    // [-Infinity,-1)
                    page.setAlpha(0);
                } else if (position <= 0) {    // [-1,0]
                    page.setAlpha(1);
                    page.setPivotX(0);
                    page.setRotationY(90 * Math.abs(position));

                } else if (position <= 1) {    // (0,1]
                    page.setAlpha(1);
                    page.setPivotX(page.getWidth());
                    page.setRotationY(-90 * Math.abs(position));

                } else {    // (1,+Infinity]
                    page.setAlpha(0);

                }

            }
        });

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == 1) {
                    viewPager.setCurrentItem(tab.getPosition());

                    Fragment fragment = adapter.getItem(tab.getPosition());

                    try {
                        Class c = fragment.getClass();
                        Method loadFragment = c.getMethod("loadData");
                        loadFragment.invoke(fragment);
                    } catch (IllegalAccessException e) {
                    } catch (InvocationTargetException e) {
                    } catch (NoSuchMethodException e) {
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
