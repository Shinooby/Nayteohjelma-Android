package siirtyminenfragmenteilla.app;


import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import siirtyminenfragmenteilla.app.Viewpagerin_fragmentit.kasvi_frag;
import siirtyminenfragmenteilla.app.Viewpagerin_fragmentit.kotielain_frag;
import siirtyminenfragmenteilla.app.Viewpagerin_fragmentit.metsa_frag;
import siirtyminenfragmenteilla.app.Viewpagerin_fragmentit.perus_frag;
import siirtyminenfragmenteilla.app.Viewpagerin_fragmentit.yleis_frag;
import siirtyminenfragmenteilla.app.Viewpagerin_fragmentit.yritys_frag;


public class FragmentHolder extends FragmentActivity {

    //CollectionPagerAdapter mCollectionPagerAdapter;
    ViewPager mViewPager;

    ActionBar.Tab tabyleis, tabperus, tabkasvi, tabkotielain,
            tabmetsa, tabyritys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_holder);
/*
        mCollectionPagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager());
        mViewPager= (ViewPager) findViewById(R.id.viewPager);

        mViewPager.setAdapter(mCollectionPagerAdapter);
*/
        // alustetaan ViewPager
        final ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

  /*
        //ActionBar
        final  ActionBar actionBar = getSupportActionBar();





        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            // show the given tab
            pager.setCurrentItem(tab.getPosition());
            }
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            // hide the given tab
            }
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            // jos haluat refreshiä
            }

        };

        tabyleis = actionBar.newTab().setText("Yleistiedot").setTabListener(tabListener);
        tabperus =actionBar.newTab().setText("Perustiedot").setTabListener(tabListener);
        tabkasvi = actionBar.newTab().setText("Kasvituotanto").setTabListener(tabListener);
        tabkotielain= actionBar.newTab().setText("Kotieläintuotanto").setTabListener(tabListener);
        tabmetsa= actionBar.newTab().setText("Metsätalous").setTabListener(tabListener);
        tabyritys= actionBar.newTab().setText("Yritystoiminta").setTabListener(tabListener);

        actionBar.addTab(tabyleis);
        actionBar.addTab(tabperus);
        actionBar.addTab(tabkasvi);
        actionBar.addTab(tabkotielain);
        actionBar.addTab(tabmetsa);
        actionBar.addTab(tabyritys);

        // Listener joka tarkkailee vaihtuuko sivu ja muuttaa Tabia sen mukaan
        pager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the
                        // corresponding tab.
                        getActionBar().setSelectedNavigationItem(position);
                    }
                });
*/
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {
                // Navigointi hipaisuilla.
                case 0: return yleis_frag.newInstance("Placeholder text");
                case 1: return perus_frag.newInstance("SecondFragment, Instance 1");
                case 2: return kasvi_frag.newInstance("ThirdFragment, Instance 1");
                case 3: return kotielain_frag.newInstance("ThirdFragment, Instance 2");
                case 4: return metsa_frag.newInstance("ThirdFragment, Instance 3");
                case 5: return yritys_frag.newInstance("Placeholder text");
                default: return yleis_frag.newInstance("ThirdFragment, Default");
            }
        }

        @Override
        public int getCount() {
            return 6;
        }
    }
}
