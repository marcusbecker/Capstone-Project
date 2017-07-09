package br.com.mvbos.fillit.fragment;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.mvbos.fillit.R;
import br.com.mvbos.fillit.data.FillItContract;
import br.com.mvbos.fillit.item.VehicleAdapter;

/**
 * Created by Marcus Becker on 11/06/2017.
 */

public class ListVehicleFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener, PagerAdapterArray.DataChangeListener {

    public static final int ID_VEHICLE_LOADER = 25;

    private RecyclerView mRecyclerView;

    private OnFragmentInteractionListener mListener;
    private VehicleAdapter mAdapter;

    public ListVehicleFragment() {
    }

    public static ListVehicleFragment newInstance() {
        return new ListVehicleFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_vehicle, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new VehicleAdapter(null, this);
        mAdapter.setPath(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES));

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(ID_VEHICLE_LOADER, null, this);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id) {
            case ID_VEHICLE_LOADER:
                final String[] mProjection = {
                        FillItContract.VehicleEntry.TABLE_NAME + "." + FillItContract.VehicleEntry._ID,
                        FillItContract.VehicleEntry.COLUMN_NAME_PHOTO,
                        FillItContract.VehicleEntry.COLUMN_NAME_NAME,
                        FillItContract.VehicleEntry.COLUMN_NAME_FUEL,
                        FillItContract.FuelEntry.COLUMN_NAME_NAME
                };

                final String mSelectionClause = null;
                final String[] mSelectionArgs = {};
                final String mSortOrder = null;

                final Uri uri = FillItContract.BASE_CONTENT_URI
                        .buildUpon()
                        .appendPath(FillItContract.PATH_VEHICLE_JOIN_FUEL)
                        .build();

                return new CursorLoader(getActivity(),
                        uri,
                        mProjection,
                        mSelectionClause,
                        mSelectionArgs,
                        mSortOrder);

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            mAdapter.swapCursor(data);
            mRecyclerView.setVisibility(View.VISIBLE);
            getView().findViewById(R.id.tvNoData).setVisibility(View.GONE);

        } else {
            mRecyclerView.setVisibility(View.GONE);
            getView().findViewById(R.id.tvNoData).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onClick(View view) {
        final Cursor c = mAdapter.getCursor();
        final int ci = c.getColumnIndex(FillItContract.VehicleEntry._ID);
        final int itemPosition = mRecyclerView.getChildLayoutPosition(view);

        c.moveToPosition(itemPosition);
        onButtonPressed(FillItContract.VehicleEntry.buildVehicleUri(c.getLong(ci)));
    }

    @Override
    public void pagerAdapterUpdate() {
        if (isAdded()) {
            getLoaderManager().restartLoader(ID_VEHICLE_LOADER, null, this);
        }
    }
}
