package br.com.mvbos.fillit;


import android.content.UriMatcher;
import android.content.res.Configuration;
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

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private Fragment mContent;
    public static final String CURRENT_FRAGMENT = "myFragmentName";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

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
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final int match = matcher.match(FillItContract.VehicleEntry.CONTENT_URI);

        final String path = uri.getPath();
        final String lastPathSegment = uri.getLastPathSegment();

        //Update Vehicle
        mContent = NewVehicleFragment.newInstance(new VehicleModel(1));
        startFragment();

        System.out.println(uri);
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
}
