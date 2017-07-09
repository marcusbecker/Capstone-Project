package br.com.mvbos.fillit;


import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import br.com.mvbos.fillit.data.FillItContract;
import br.com.mvbos.fillit.fragment.NewFillFragment;
import br.com.mvbos.fillit.fragment.NewVehicleFragment;
import br.com.mvbos.fillit.fragment.OnFragmentInteractionListener;
import br.com.mvbos.fillit.model.FillModel;
import br.com.mvbos.fillit.model.VehicleModel;
import br.com.mvbos.fillit.sync.DataSyncStarter;
import br.com.mvbos.fillit.util.ModelBuilder;

public class DetailActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    public static final String BUNDLE_URI = "b_uri";
    public static final String BUNDLE_ADD = "b_add";
    public static final int ADD_VEHICLE = 30;
    public static final int ADD_FILL = 40;
    private static final String CURRENT_FRAGMENT = "currentFragment";

    private Fragment mContent;

    private final UriMatcher mMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int VEHICLE = 300;
    private static final int VEHICLE_WITH_ID = 301;
    private static final int FILL = 400;
    private static final int FILL_WITH_ID = 401;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataSyncStarter.scheduleTask(this);

        final String authority = FillItContract.CONTENT_AUTHORITY;
        mMatcher.addURI(authority, FillItContract.PATH_VEHICLE, VEHICLE);
        mMatcher.addURI(authority, FillItContract.PATH_VEHICLE + "/#", VEHICLE_WITH_ID);
        mMatcher.addURI(authority, FillItContract.PATH_FILL, FILL);
        mMatcher.addURI(authority, FillItContract.PATH_FILL + "/#", FILL_WITH_ID);

        setContentView(R.layout.activity_detail);

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(BUNDLE_URI)) {
                onFragmentInteraction((Uri) bundle.getParcelable(BUNDLE_URI));

            } else if (bundle.containsKey(BUNDLE_ADD)) {
                int sel = bundle.getInt(BUNDLE_ADD);
                if (sel == ADD_VEHICLE) {
                    mContent = NewVehicleFragment.newInstance(new VehicleModel(0));
                    startFragment();
                } else if (sel == ADD_FILL) {
                    mContent = NewFillFragment.newInstance(new FillModel(0));
                    startFragment();
                }
            }
        }
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
                //mContent = ListVehicleFragment.newInstance(null, null);
                mContent = null;
                finish();
                break;
            case VEHICLE_WITH_ID:
                query = getContentResolver().query(uri, null, null, null, null);
                VehicleModel v = ModelBuilder.buildVehicle(query);
                mContent = NewVehicleFragment.newInstance(v);
                break;
            case FILL:
                //mContent = ListFillFragment.newInstance(null, null);
                mContent = null;
                finish();
                break;
            case FILL_WITH_ID:
                query = getContentResolver().query(uri, null, null, null, null);
                FillModel f = ModelBuilder.buildFill(query);
                mContent = NewFillFragment.newInstance(f);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (mContent != null) {
            startFragment();
        }
    }

    private void startFragment() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.mainFragment, mContent)
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
}
