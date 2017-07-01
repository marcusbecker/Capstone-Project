package br.com.mvbos.fillit.fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.mvbos.fillit.R;
import br.com.mvbos.fillit.data.FillItContract;
import br.com.mvbos.fillit.item.FlagSpinnerAdapter;
import br.com.mvbos.fillit.model.FillModel;
import br.com.mvbos.fillit.model.FlagModel;
import br.com.mvbos.fillit.model.GasStationModel;
import br.com.mvbos.fillit.util.Converter;
import br.com.mvbos.fillit.util.ModelBuilder;

public class NewFillFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String ARG_PARAM1 = "param1";

    private GoogleMap mMap;
    private LocationRequest mLocation;
    private GoogleApiClient mClient;
    private Location mLastLocation;

    private FillModel mFill;
    private Spinner mGasStation;
    private EditText mVehicle;
    private EditText mFuel;
    private EditText mDate;
    private EditText mPrice;
    private EditText mLiters;


    private OnFragmentInteractionListener mListener;
    private FlagModel[] mFlagsList;

    public NewFillFragment() {
        // Required empty public constructor
    }

    public static NewFillFragment newInstance(final FillModel fillModel) {
        NewFillFragment fragment = new NewFillFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, fillModel);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFill = getArguments().getParcelable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_new_fill, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mGasStation = (Spinner) view.findViewById(R.id.spGasStation);
        mVehicle = (EditText) view.findViewById(R.id.etVehicle);
        mFuel = (EditText) view.findViewById(R.id.etFuel);
        mDate = (EditText) view.findViewById(R.id.etDate);
        mPrice = (EditText) view.findViewById(R.id.etPrice);
        mLiters = (EditText) view.findViewById(R.id.etLiters);

        mClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .enableAutoManage(getActivity(), this)
                .build();


        final Uri uri = FillItContract.FlagEntry.CONTENT_URI;
        final Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        mFlagsList = ModelBuilder.buildFlagList(cursor);

        SpinnerAdapter adapter = new FlagSpinnerAdapter(getContext(), mFlagsList);
        mGasStation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(NewFillFragment.class.getSimpleName(), String.format("position %d, id %d ", position, id));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i(NewFillFragment.class.getSimpleName(), "Nothing Selected");
            }
        });
        mGasStation.setAdapter(adapter);


        view.findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean success;
                final ContentResolver resolver = view.getContext().getContentResolver();
                ContentValues mFillValues = new ContentValues();

                mFill.setGasStation(0);
                mFill.setVehicle(1);
                mFill.setFuel(1);
                mFill.setPrice(Converter.toFloat(mPrice, 0f));
                mFill.setLiters(Math.round(Converter.toFloat(mLiters, 0f)));

                mFillValues.put(FillItContract.FillEntry.COLUMN_NAME_GASSTATION, mFill.getGasStation());
                mFillValues.put(FillItContract.FillEntry.COLUMN_NAME_VEHICLE, mFill.getVehicle());
                mFillValues.put(FillItContract.FillEntry.COLUMN_NAME_FUEL, mFill.getFuel());

                mFillValues.put(FillItContract.FillEntry.COLUMN_NAME_PRICE, mFill.getPrice());
                mFillValues.put(FillItContract.FillEntry.COLUMN_NAME_LITERS, mFill.getLiters());
                mFillValues.put(FillItContract.FillEntry.COLUMN_NAME_LAT, mFill.getLat());
                mFillValues.put(FillItContract.FillEntry.COLUMN_NAME_LNG, mFill.getLng());
                mFillValues.put(FillItContract.FillEntry.COLUMN_NAME_DATE, Calendar.getInstance().getTimeInMillis());


                if (mFill.getId() > 0) {
                    int res = resolver.update(
                            FillItContract.FillEntry.CONTENT_URI,
                            mFillValues,
                            FillItContract.FillEntry._ID + "=?",
                            new String[]{String.valueOf(mFill.getId())}
                    );

                    //mFillValues.put(FillItContract.FillEntry.COLUMN_NAME_DATASYNC, "");

                    success = res > 0;

                } else {
                    Uri fillUri = resolver.insert(
                            FillItContract.FillEntry.CONTENT_URI,
                            mFillValues
                    );

                    success = fillUri != null;
                }

                if (success) {
                    onButtonPressed(FillItContract.FillEntry.CONTENT_URI);
                } else {
                    Toast.makeText(getContext(), "Data base error", Toast.LENGTH_LONG).show();
                }
            }
        });

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
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;

        MarkerOptions marker = null;
        final Set<String> cache = new HashSet<>(10);
        final List<GasStationModel> coffees = Collections.EMPTY_LIST;

        for (GasStationModel cpm : coffees) {
            String strLatLng = cpm.getLat() + "," + cpm.getLng();

            if (cpm.hasLatLng() && !cache.contains(strLatLng)) {
                marker = new MarkerOptions()
                        .position(new LatLng(cpm.getLat(), cpm.getLng()))
                        .title(cpm.getName());

                mMap.addMarker(marker);

                cache.add(strLatLng);
            }
        }

        if (mLastLocation != null) {
            updateMapLocation();

        } else if (marker != null) {
            CameraPosition cp = CameraPosition.builder()
                    .target(marker.getPosition())
                    .zoom(15f)
                    .bearing(0f)
                    .tilt(45f)
                    .build();

            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocation = LocationRequest.create();
        mLocation.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocation.setInterval(1000);


        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LocationServices.FusedLocationApi.requestLocationUpdates(mClient, mLocation, this);

        } else {
            Toast.makeText(getContext(), "permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mMap != null) {
            updateMapLocation();
        }
    }

    private void updateMapLocation() {
        MarkerOptions marker = new MarkerOptions()
                .position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                .title(getString(R.string.current_location));

        mMap.addMarker(marker);


        CameraPosition cp = CameraPosition.builder()
                .target(marker.getPosition())
                .zoom(15f)
                .bearing(0f)
                .tilt(45f)
                .build();

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
    }
}
