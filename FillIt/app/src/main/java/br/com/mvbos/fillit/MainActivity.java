package br.com.mvbos.fillit;


import android.content.Intent;
import android.content.UriMatcher;
import android.net.Uri;
import android.os.Bundle;
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
import br.com.mvbos.fillit.fragment.MainCollectionPagerAdapter;
import br.com.mvbos.fillit.fragment.OnFragmentInteractionListener;
import br.com.mvbos.fillit.sync.DataSyncService;
import br.com.mvbos.fillit.sync.DataSyncStarter;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private final UriMatcher mMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int VEHICLE = 300;
    private static final int VEHICLE_WITH_ID = 301;
    private static final int FILL = 400;
    private static final int FILL_WITH_ID = 401;

    private MainCollectionPagerAdapter mCollectionPagerAdapter;
    private ViewPager mViewPager;

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

        mViewPager = (ViewPager) findViewById(R.id.pager);

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
            case R.id.menu_sync_data:
                sync();
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


    private void sync() {
        Intent intent = new Intent(this, DataSyncService.class);
        startService(intent);
    }
}
