package br.com.mvbos.fillit;


import android.content.UriMatcher;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import br.com.mvbos.fillit.data.FillItContract;
import br.com.mvbos.fillit.fragment.ListVehicleFragment;
import br.com.mvbos.fillit.fragment.NewVehicleFragment;
import br.com.mvbos.fillit.fragment.OnFragmentInteractionListener;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        //transaction.addToBackStack(null);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.mainFragment, ListVehicleFragment.newInstance(null, null))
                .commit();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final int match = matcher.match(FillItContract.VehicleEntry.CONTENT_URI);

        final String path = uri.getPath();
        final String lastPathSegment = uri.getLastPathSegment();

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.mainFragment, NewVehicleFragment.newInstance(0))
                .addToBackStack(null)
                .commit();

        System.out.println(uri);
    }
}
