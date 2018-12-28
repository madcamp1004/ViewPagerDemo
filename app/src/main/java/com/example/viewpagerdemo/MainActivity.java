package com.example.viewpagerdemo;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements ContactFragment.OnFragmentInteractionListener,
                    GalleryFragment.OnFragmentInteractionListener,
                 CardFragment.OnFragmentInteractionListener{

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private BottomNavigationView mNavigation;
    private ViewPager mViewPager;

    private ViewPager.OnPageChangeListener mOnPageChangeListener
            = new ViewPager.SimpleOnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            // Log.i("Position", "******" + String.valueOf(position) + "*****");
            switch (position) {
                case 0:
                    mNavigation.setSelectedItemId(R.id.navigation_contacts);
                    return;
                case 1:
                    mNavigation.setSelectedItemId(R.id.navigation_gallery);
                    return;
                case 2:
                    mNavigation.setSelectedItemId(R.id.navigation_card);
                    return;
            }
            Log.i("Error", "***** onPageSelected *****");
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int navigationId = item.getItemId();
            switch (navigationId) {
                case R.id.navigation_contacts:
                    mViewPager.setCurrentItem(0, true);
                    return true;
                case R.id.navigation_gallery:
                    mViewPager.setCurrentItem(1, true);
                    return true;
                case R.id.navigation_card:
                    mViewPager.setCurrentItem(2, true);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
        mNavigation = (BottomNavigationView) findViewById(R.id.navigation);
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public static class PlaceholderFragment extends Fragment {
        private static final String SECTION_NUMBER = "section_number";

        public PlaceholderFragment(){
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.textView);
            textView.setText(String.valueOf(getArguments().getInt(SECTION_NUMBER)));
            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) { super(fm); }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return ContactFragment.newInstance();
                case 1:
                    return GalleryFragment.newInstance();
                case 2:
                    return CardFragment.newInstance();
            }
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 3;
        }


    }

}
