package br.com.mvbos.fillit.fragment;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Marcus Becker on 25/06/2017.
 */

public class PagerAdapterArray extends FragmentStatePagerAdapter implements OnFragmentInteractionListener {

    public interface DataChangeListener {
        void pagerAdapterUpdate();

    }


    private final Fragment[] frags;
    private final String[] titles;

    public PagerAdapterArray(FragmentManager fm, Fragment[] frags, String[] titles) {
        super(fm);
        this.frags = frags;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int i) {
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

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        for (Fragment f : frags) {
            if (f instanceof DataChangeListener) {
                ((DataChangeListener) f).pagerAdapterUpdate();
            }
        }
    }
}