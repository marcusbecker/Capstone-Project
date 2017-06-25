package br.com.mvbos.fillit.fragment;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Marcus Becker on 25/06/2017.
 */

public class MainCollectionPagerAdapter extends FragmentStatePagerAdapter implements OnFragmentInteractionListener {


    private final Fragment[] frags;
    private final String[] titles;

    public MainCollectionPagerAdapter(FragmentManager fm, Fragment[] frags, String[] titles) {
        super(fm);
        this.frags = frags;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int i) {
        //Bundle args = new Bundle();
        // Our object is just an integer :-P
        //args.putInt(DemoObjectFragment.ARG_OBJECT, i + 1);
        //fragment.setArguments(args);

        return frags[i];
    }

    @Override
    public int getCount() {
        return frags.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}