package br.com.mvbos.fillit;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import br.com.mvbos.fillit.data.FillItContract;
import br.com.mvbos.fillit.fragment.ListFillFragment;
import br.com.mvbos.fillit.fragment.ListVehicleFragment;
import br.com.mvbos.fillit.fragment.OnFragmentInteractionListener;
import br.com.mvbos.fillit.fragment.PagerAdapterArray;
import br.com.mvbos.fillit.sync.DataSyncService;
import br.com.mvbos.fillit.sync.DataSyncStarter;
import br.com.mvbos.fillit.util.PrefsUtil;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private final UriMatcher mMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int VEHICLE = 300;
    private static final int VEHICLE_WITH_ID = 301;
    private static final int FILL = 400;
    private static final int FILL_WITH_ID = 401;

    private ViewPager mViewPager;
    private PagerAdapterArray mCollectionPagerAdapter;


    private boolean leave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataSyncStarter.scheduleTask(this);

        final String authority = FillItContract.CONTENT_AUTHORITY;
        mMatcher.addURI(authority, FillItContract.PATH_VEHICLE, VEHICLE);
        mMatcher.addURI(authority, FillItContract.PATH_VEHICLE + "/#", VEHICLE_WITH_ID);
        mMatcher.addURI(authority, FillItContract.PATH_FILL, FILL);
        mMatcher.addURI(authority, FillItContract.PATH_FILL + "/#", FILL_WITH_ID);

        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = getSharedPreferences(PrefsUtil.NAME, Context.MODE_PRIVATE);
        mViewPager = (ViewPager) findViewById(R.id.pager);

        if (!sharedPref.contains(PrefsUtil.PREF_FIRST_USE)) {
            sharedPref.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sp, String key) {
                    if (PrefsUtil.PREF_FIRST_USE.equals(key)) {
                        createPagerView();
                        mViewPager.setVisibility(View.VISIBLE);
                        findViewById(R.id.lnLoading).setVisibility(View.GONE);
                    }
                }
            });

            startSyncData();
            mViewPager.setVisibility(View.GONE);
            findViewById(R.id.lnLoading).setVisibility(View.VISIBLE);

        } else {
            createPagerView();
        }
    }

    private void createPagerView() {
        final Fragment[] frags = new Fragment[]{
                ListFillFragment.newInstance(),
                ListVehicleFragment.newInstance()};

        final String[] titles = new String[]{
                getString(R.string.label_tab_one),
                getString(R.string.label_tab_two)};

        mCollectionPagerAdapter = new PagerAdapterArray(
                getSupportFragmentManager(),
                frags,
                titles);

        mViewPager.setAdapter(mCollectionPagerAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (leave) {
            mCollectionPagerAdapter.notifyDataSetChanged();
            leave = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_report_increase:
                showReport();
                return true;
            case R.id.menu_config:
                showConfig();
                return true;
            case R.id.menu_sync_data:
                startSyncData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        leave = true;
        Intent i = new Intent(this, DetailActivity.class);
        i.putExtra(DetailActivity.BUNDLE_URI, uri);
        startActivity(i);
    }

    public void addVehicle(View view) {
        //New vehicle
        leave = true;
        Intent i = new Intent(this, DetailActivity.class);
        i.putExtra(DetailActivity.BUNDLE_ADD, DetailActivity.ADD_VEHICLE);
        startActivity(i);
    }

    public void addFill(View view) {
        //New Fill
        leave = true;
        Intent i = new Intent(this, DetailActivity.class);
        i.putExtra(DetailActivity.BUNDLE_ADD, DetailActivity.ADD_FILL);
        startActivity(i);
    }

    private void showReport() {
        Intent intent = new Intent(this, ReportIncreaseActivity.class);
        startActivity(intent);
    }

    private void showConfig() {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }

    private void startSyncData() {
        Intent intent = new Intent(this, DataSyncService.class);
        startService(intent);
    }
}
