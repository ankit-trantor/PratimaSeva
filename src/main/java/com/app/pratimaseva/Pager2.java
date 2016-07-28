package com.app.pratimaseva;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by dharmik on 24-07-2016.
 */
public class Pager2 extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public Pager2(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                eligiblitiesFragment tab1 = new eligiblitiesFragment();
                return tab1;
            case 1:
                 NonligiblitiesFragment tab2 = new NonligiblitiesFragment();
                return tab2;
            case 2:
                preparationFragment tab3 = new preparationFragment();
                return tab3;
            case 3:
                DonationFragment tab4 = new DonationFragment();
                return tab4;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}

