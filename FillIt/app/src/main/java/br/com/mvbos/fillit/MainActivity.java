package br.com.mvbos.fillit;


import android.content.Intent;
import android.content.UriMatcher;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import br.com.mvbos.fillit.data.FillItContract;
import br.com.mvbos.fillit.fragment.ListVehicleFragment;
import br.com.mvbos.fillit.fragment.NewVehicleFragment;
import br.com.mvbos.fillit.fragment.OnFragmentInteractionListener;
import br.com.mvbos.fillit.model.VehicleModel;
import br.com.mvbos.fillit.sync.DataSyncService;
import br.com.mvbos.fillit.sync.DataSyncStarter;
import br.com.mvbos.fillit.util.ModelBuilder;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private Fragment mContent;
    public static final String CURRENT_FRAGMENT = "myFragmentName";

    private final UriMatcher mMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int VEHICLE = 300;
    private static final int VEHICLE_WITH_ID = 301;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        switch (match) {
            case VEHICLE:
                mContent = ListVehicleFragment.newInstance(null, null);
                break;
            case VEHICLE_WITH_ID:
                final Cursor query = getContentResolver().query(uri, null, null, null, null);
                VehicleModel v = ModelBuilder.buildVehicle(query);
                mContent = NewVehicleFragment.newInstance(v);
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
