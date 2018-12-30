package com.example.viewpagerdemo;

import android.Manifest;
import android.app.Service;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements ContactFragment.OnFragmentInteractionListener,
                    GalleryFragment.OnFragmentInteractionListener,
                 CardFragment.OnFragmentInteractionListener{

    private SectionsPagerAdapter mSectionsPagerAdapter;
    public static BottomNavigationView mNavigation;
    public static ViewPager mViewPager;



    public static ArrayList<String> items;
    public static ArrayList<Map<String, String>> dataList;

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

        items = new ArrayList<String>();
        dataList = new ArrayList<Map<String, String>>();

        if(Permissioncheck()) {
            loadContacts();
        }
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


    @Override
    public int checkSelfPermission(String permission) {
        return super.checkSelfPermission(permission);
    }

    public boolean Permissioncheck(){
        if(checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},1);
            if(checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
                return true;
            }
            else{
                return false;
            }
        }
    }

    private void loadContacts() {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        //check there exists contact
        //cur : includes every contact
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                //check whether there exists phone number
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", new String[]{id}, null);
                    //if there are multiple contacts per id --> Make multiple list items using the name
                    while (pCur.moveToNext()) {
                        String phoneNo = addHyphenToPhone(pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));

                        HashMap tmpMap = new HashMap<String, String>();
                        tmpMap.put("name", name);
                        tmpMap.put("phone", phoneNo);
                        tmpMap.put("address", "");

                        dataList.add(tmpMap);

                        String listItem = name + ": " + phoneNo;

                        items.add(listItem);
                        // ContactFragment.adapter.notifyDataSetChanged();
                    }
                    pCur.close();
                }
            }
        }
    }

    public String addHyphenToPhone(String phoneNum) {
        return phoneNum.replaceFirst("(\\d{3})(\\d{4})(\\d+)", "($1) $2-$3");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks whether a hardware keyboard is available
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            Toast.makeText(this, "keyboard visible", Toast.LENGTH_SHORT).show();
        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            Toast.makeText(this, "keyboard hidden", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
}
