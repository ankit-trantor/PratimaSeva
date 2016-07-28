package com.app.pratimaseva;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by dharmik on 03-07-2016.
 */
public class Pager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount) {
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
                BloodneedFragment tab1 = new BloodneedFragment();
                return tab1;
            case 1:
                BloodSupplyFragment tab2 = new BloodSupplyFragment();
                return tab2;
            case 2:
                BdonationprocessFragment tab3 = new BdonationprocessFragment();
                return tab3;
            case 3:
                BComponentsFragment tab4 = new BComponentsFragment();
                return tab4;
            case 4:
                BDonorsFragment tab5 = new BDonorsFragment();
                return tab5;
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
