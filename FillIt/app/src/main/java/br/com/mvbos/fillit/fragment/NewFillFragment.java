package br.com.mvbos.fillit.fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import br.com.mvbos.fillit.R;
import br.com.mvbos.fillit.data.FillItContract;
import br.com.mvbos.fillit.item.FlagSpinnerAdapter;
import br.com.mvbos.fillit.item.VehicleSpinnerAdapter;
import br.com.mvbos.fillit.model.FillModel;
import br.com.mvbos.fillit.model.FlagModel;
import br.com.mvbos.fillit.model.FuelModel;
import br.com.mvbos.fillit.model.GasStationModel;
import br.com.mvbos.fillit.model.VehicleModel;
import br.com.mvbos.fillit.util.Converter;
import br.com.mvbos.fillit.util.ModelBuilder;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.google.android.gms.internal.zzt.TAG;

public class NewFillFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String ARG_PARAM1 = "param1";
    private static final int MAP_UPDATE_LOCATION_CHANGE = 3;
    private static final int MAP_UPDATE_MAP_READY = 5;
    private static final int MAP_UPDATE_PLACE_PICKER = 7;

    private static final int PERMISSIONS_REQUEST_LOCATION = 5;

    private static final int PLACE_PICKER_REQUEST = 101;

    private GoogleMap mMap;
    private LocationRequest mLocation;
    private GoogleApiClient mClient;

    private Marker mLastMarker;
    private LatLng mLastLocation;

    private FillModel mFill;
    private Spinner mGasStation;
    private Spinner mVehicle;
    private Spinner mFuel;
    private Button mDate;
    private EditText mPrice;
    private EditText mLiters;
    private EditText mTotal;


    private OnFragmentInteractionListener mListener;
    private FuelModel[] mFuelList;
    private FlagModel[] mFlagsList;
    private VehicleModel[] mVehicleList;

    public NewFillFragment() {
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

        if (mFill.getId() > 0) {
            if (mFill.hasLatLng()) {
                mLastLocation = new LatLng(mFill.getLat(), mFill.getLng());
            }

        } else {
            mClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .enableAutoManage(getActivity(), this)
                    .build();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mClient != null) {
            mClient.stopAutoManage(getActivity());
            mClient.disconnect();
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
        mVehicle = (Spinner) view.findViewById(R.id.spVehicle);
        mFuel = (Spinner) view.findViewById(R.id.spFuel);

        mDate = (Button) view.findViewById(R.id.btnDate);
        mPrice = (EditText) view.findViewById(R.id.etPrice);
        mLiters = (EditText) view.findViewById(R.id.etLiters);
        mTotal = (EditText) view.findViewById(R.id.etTotal);


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        setFlagSpinner();

        setVehicleSpinner();

        setFuelSpinner();

        Date fillDate;
        if (mFill.getId() > 0) {
            final NumberFormat df = DecimalFormat.getInstance();
            fillDate = new Date(mFill.getDate());

            mPrice.setText(df.format(mFill.getPrice()));
            mLiters.setText(df.format(mFill.getLiters()));
            mTotal.setText(df.format(mFill.getLiters() * mFill.getPrice()));

        } else {
            fillDate = new Date();
        }

        mDate.setText(SimpleDateFormat.getDateInstance().format(fillDate));

        final View mapButton = view.findViewById(R.id.mapFloatButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickPlace(view);
            }
        });

        view.findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                persist(view);
            }
        });

    }

    private void pickPlace(View view) {
        try {

            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                    .setCountry(Locale.getDefault().getCountry())
                    .build();

            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .setFilter(typeFilter)
                            .build(getActivity());


            startActivityForResult(intent, PLACE_PICKER_REQUEST);


        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private void persist(View view) {
        boolean success;
        final ContentResolver resolver = view.getContext().getContentResolver();

        mFill.setGasStation(0);
        mFill.setVehicle(mVehicleList[mVehicle.getSelectedItemPosition()].getId());
        mFill.setFuel(mFuelList[mFuel.getSelectedItemPosition()].getId());
        mFill.setPrice(Converter.toFloat(mPrice, 0f));
        mFill.setLiters(Math.round(Converter.toFloat(mLiters, 0f)));

        if (mLastLocation != null) {
            mFill.setLat(mLastLocation.latitude);
            mFill.setLng(mLastLocation.longitude);
        }

        ContentValues mFillValues = new ContentValues();

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

    private void setFuelSpinner() {
        final Uri uri = FillItContract.FuelEntry.CONTENT_URI;
        final Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        mFuelList = ModelBuilder.buildFuelList(cursor);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_item);

        for (FuelModel m : mFuelList) {
            adapter.add(m.getName());
        }
        mFuel.setAdapter(adapter);
        mFuel.setSelection((int) mFill.getFuel());
    }

    private void setVehicleSpinner() {
        final Uri uri = FillItContract.VehicleEntry.CONTENT_URI;
        final Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        mVehicleList = ModelBuilder.buildVehicleList(cursor);

        SpinnerAdapter adapter = new VehicleSpinnerAdapter(getContext(), mVehicleList);
        mVehicle.setAdapter(adapter);
        mVehicle.setSelection((int) mFill.getVehicle());
    }

    private void setFlagSpinner() {
        final Uri uri = FillItContract.FlagEntry.CONTENT_URI;
        final Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        mFlagsList = ModelBuilder.buildFlagList(cursor);

        SpinnerAdapter adapter = new FlagSpinnerAdapter(getContext(), mFlagsList);
        mGasStation.setAdapter(adapter);
        mGasStation.setSelection((int) mFill.getGasStation());
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
            updateMapLocation(MAP_UPDATE_MAP_READY);

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

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mLocation = LocationRequest.create();
            mLocation.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocation.setInterval(TimeUnit.MINUTES.toMillis(1));

            LocationServices.FusedLocationApi.requestLocationUpdates(mClient, mLocation, this);

        } else {
            String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(getActivity(), permissions, PERMISSIONS_REQUEST_LOCATION);
        }
    }


    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onConnected(null);
                } else {
                    Log.d("", "permission denied");
                }

                return;
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = new LatLng(location.getLatitude(), location.getLongitude());
        if (mMap != null) {
            updateMapLocation(MAP_UPDATE_LOCATION_CHANGE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getContext(), data);

                mLastLocation = place.getLatLng();
                updateMapLocation(MAP_UPDATE_PLACE_PICKER);

                Log.i(TAG, "Place: " + place.getName());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getContext(), data);
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
            }
        }
    }

    private void updateMapLocation(int event) {

        if (mLastMarker == null) {
            MarkerOptions markerOption = new MarkerOptions()
                    .position(mLastLocation)
                    .title(getString(R.string.current_location));

            mLastMarker = mMap.addMarker(markerOption);

        } else {
            mLastMarker.setPosition(mLastLocation);
        }


        CameraPosition cp = CameraPosition.builder()
                .target(mLastMarker.getPosition())
                .zoom(16f)
                .bearing(0f)
                .tilt(45f)
                .build();

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
    }
}
