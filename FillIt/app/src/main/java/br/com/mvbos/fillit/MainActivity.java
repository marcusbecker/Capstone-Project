package br.com.mvbos.fillit;


import android.content.Intent;
import android.content.UriMatcher;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.Locale;

import br.com.mvbos.fillit.data.FillItContract;
import br.com.mvbos.fillit.fragment.ListFillFragment;
import br.com.mvbos.fillit.fragment.ListVehicleFragment;
import br.com.mvbos.fillit.fragment.MainCollectionPagerAdapter;
import br.com.mvbos.fillit.fragment.NewFillFragment;
import br.com.mvbos.fillit.fragment.NewVehicleFragment;
import br.com.mvbos.fillit.fragment.OnFragmentInteractionListener;
import br.com.mvbos.fillit.model.FillModel;
import br.com.mvbos.fillit.model.VehicleModel;
import br.com.mvbos.fillit.sync.DataSyncService;
import br.com.mvbos.fillit.sync.DataSyncStarter;
import br.com.mvbos.fillit.util.Converter;
import br.com.mvbos.fillit.util.ModelBuilder;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private Fragment mContent;
    public static final String CURRENT_FRAGMENT = "myFragmentName";

    private final UriMatcher mMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int VEHICLE = 300;
    private static final int VEHICLE_WITH_ID = 301;
    private static final int FILL = 400;
    private static final int FILL_WITH_ID = 401;

    private MainCollectionPagerAdapter mCollectionPagerAdapter;
    private ViewPager mViewPager;

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

        final Fragment[] frags = new Fragment[]{
                ListFillFragment.newInstance("", ""),
                ListVehicleFragment.newInstance("", "")};
        final String[] titles = new String[]{
                getString(R.string.label_tab_one),
                getString(R.string.label_tab_two)};

        mCollectionPagerAdapter = new MainCollectionPagerAdapter(
                getSupportFragmentManager(),
                frags,
                titles);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCollectionPagerAdapter);

    }

    protected void _onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataSyncStarter.scheduleTask(this);

        setContentView(R.layout.activity_main);

        final String authority = FillItContract.CONTENT_AUTHORITY;
        mMatcher.addURI(authority, FillItContract.PATH_VEHICLE, VEHICLE);
        mMatcher.addURI(authority, FillItContract.PATH_VEHICLE + "/#", VEHICLE_WITH_ID);

        if (savedInstanceState != null) {
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, CURRENT_FRAGMENT);
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //finish();
            //return;
        }

        //transaction.addToBackStack(null);

        if (mContent == null) {
            mContent = ListVehicleFragment.newInstance(null, null);
        }

        startFragment();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mContent != null) {
            getSupportFragmentManager().putFragment(outState, CURRENT_FRAGMENT, mContent);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

        final int match = mMatcher.match(uri);
        final Cursor query;

        switch (match) {
            case VEHICLE:
                mContent = ListVehicleFragment.newInstance(null, null);
                break;
            case VEHICLE_WITH_ID:
                query = getContentResolver().query(uri, null, null, null, null);
                VehicleModel v = ModelBuilder.buildVehicle(query);
                mContent = NewVehicleFragment.newInstance(v);
                break;
            case FILL:
                mContent = ListFillFragment.newInstance(null, null);
                break;
            case FILL_WITH_ID:
                query = getContentResolver().query(uri, null, null, null, null);
                FillModel f = ModelBuilder.buildFill(query);
                mContent = NewFillFragment.newInstance(f);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        startFragment();
    }

    public void addVehicle(View view) {
        //New vehicle
        mContent = NewVehicleFragment.newInstance(new VehicleModel(0));
        startFragment();
    }

    public void addFill(View view) {
        //New Fill
        mContent = NewFillFragment.newInstance(new FillModel(0));
        startFragment();
    }

    private void startFragment() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.mainFragment, mContent)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (mContent != null) {
            mContent.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    public void sync(View view) {
        Intent intent = new Intent(this, DataSyncService.class);
        startService(intent);
    }
}
