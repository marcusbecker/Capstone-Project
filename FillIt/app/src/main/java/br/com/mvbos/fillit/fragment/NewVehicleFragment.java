package br.com.mvbos.fillit.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.mvbos.fillit.R;
import br.com.mvbos.fillit.data.FillItContract;
import br.com.mvbos.fillit.model.FuelModel;
import br.com.mvbos.fillit.model.VehicleModel;
import br.com.mvbos.fillit.util.FileUtil;
import br.com.mvbos.fillit.util.ModelBuilder;

public class NewVehicleFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private static final String ARG_PARAM_ID = "param_id";
    private static final String ARG_PARAM_PATH = "param_path";
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int PERMISSIONS_REQUEST_CAPTURE_IMAGE = 2;

    private VehicleModel mVehicle;

    private Spinner mFuel;
    private EditText mName;
    private ImageButton mPhoto;

    private String mCurrentPhotoPath;
    private OnFragmentInteractionListener mListener;

    private File mStorageDir;
    private List<FuelModel> mFuelsList;


    public NewVehicleFragment() {
    }

    public static NewVehicleFragment newInstance(final VehicleModel v) {
        NewVehicleFragment fragment = new NewVehicleFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_ID, v);

        fragment.setArguments(args);
        return fragment;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            File photoFile = null;
            try {
                if (mVehicle.hasPhoto()) {
                    photoFile = new File(mStorageDir, mVehicle.getPhoto());
                } else {
                    photoFile = FileUtil.createImageFile(mStorageDir);
                }

                mCurrentPhotoPath = photoFile.getAbsolutePath();

            } catch (IOException ex) {
                Log.e(NewVehicleFragment.class.getName(), ex.getMessage());
            }

            if (photoFile != null) {
                if (isEnableVersionBoolean()) {
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

                } else {
                    Uri photoURI = FileProvider.getUriForFile(getActivity(),
                            FileUtil.AUTHORITY,
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (isEnableVersionBoolean()) {
            if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                FileUtil.setPic(mPhoto, imageBitmap, mCurrentPhotoPath);
                mVehicle.setPhoto(new File(mCurrentPhotoPath).getName());
            }
        } else {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                FileUtil.setPic(mPhoto, mCurrentPhotoPath);
                mVehicle.setPhoto(new File(mCurrentPhotoPath).getName());
            }
        }
    }

    private boolean isEnableVersionBoolean() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStorageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (getArguments() != null) {
            mVehicle = getArguments().getParcelable(ARG_PARAM_ID);
        }

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(ARG_PARAM_PATH)) {
                mCurrentPhotoPath = savedInstanceState.getString(ARG_PARAM_PATH);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mVehicle.setName(mName.getText().toString());
        mVehicle.setFuel(mFuelsList.get(mFuel.getSelectedItemPosition()).getId());
        outState.putString(ARG_PARAM_PATH, mCurrentPhotoPath);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_new_vehicle, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFuel = (Spinner) view.findViewById(R.id.spFuel);
        mName = (EditText) view.findViewById(R.id.etName);
        mPhoto = (ImageButton) view.findViewById(R.id.ibPhoto);

        mName.setText(mVehicle.getName());

        final Uri fuelUri = FillItContract.FuelEntry.CONTENT_URI;
        final Cursor cursor = getContext().getContentResolver().query(fuelUri, null, null, null, null);

        mFuelsList = new ArrayList<>(cursor != null ? cursor.getCount() + 1 : 0);
        mFuelsList.add(new FuelModel(0, getString(R.string.fuel_default), 0));
        mFuelsList.addAll(Arrays.asList(ModelBuilder.buildFuelList(cursor)));

        int selected = 0;
        CharSequence[] fuelArray = new String[mFuelsList.size()];

        for (int i = 0; i < mFuelsList.size(); i++) {
            fuelArray[i] = mFuelsList.get(i).getName();
            if (mFuelsList.get(i).getId() == mVehicle.getFuel()) {
                selected = i;
            }
        }

        final ArrayAdapter<CharSequence> spinnerAdapter = new ArrayAdapter<>(getContext(), R.layout.item_simple_spinner, fuelArray);
        mFuel.setAdapter(spinnerAdapter);
        mFuel.setSelection(selected);
        mFuel.setOnItemSelectedListener(this);

        if (mVehicle.hasPhoto()) {
            view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mPhoto.post(new Runnable() {
                        //TODO why called 3 times?
                        public void run() {
                            final File photoFile = new File(mStorageDir, mVehicle.getPhoto());
                            FileUtil.setPic(mPhoto, photoFile.getAbsolutePath());
                        }
                    });
                }
            });
        }

        view.findViewById(R.id.ibPhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                        Toast.makeText(getContext(), getString(R.string.error_permission), Toast.LENGTH_SHORT).show();
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAPTURE_IMAGE);
                    }
                } else {
                    dispatchTakePictureIntent();
                }
            }
        });


        view.findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean success;
                final ContentResolver resolver = view.getContext().getContentResolver();
                ContentValues mVehicleValues = new ContentValues();

                final long fuelId = mFuelsList.get(mFuel.getSelectedItemPosition()).getId();
                mVehicle.setFuel(fuelId);
                mVehicle.setName(mName.getText().toString());

                mVehicleValues.put(FillItContract.VehicleEntry.COLUMN_NAME_PHOTO, mVehicle.getPhoto());
                mVehicleValues.put(FillItContract.VehicleEntry.COLUMN_NAME_NAME, mVehicle.getName());
                mVehicleValues.put(FillItContract.VehicleEntry.COLUMN_NAME_FUEL, mVehicle.getFuel());

                if (mVehicle.getId() > 0) {
                    int res = resolver.update(
                            FillItContract.VehicleEntry.CONTENT_URI,
                            mVehicleValues,
                            FillItContract.VehicleEntry._ID + "=?",
                            new String[]{String.valueOf(mVehicle.getId())}
                    );

                    success = res > 0;

                } else {
                    Uri vehicleUri = resolver.insert(
                            FillItContract.VehicleEntry.CONTENT_URI,
                            mVehicleValues
                    );

                    success = vehicleUri != null;
                }

                if (success) {
                    onButtonPressed(FillItContract.VehicleEntry.CONTENT_URI);
                } else {
                    Toast.makeText(getContext(), R.string.error_base, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CAPTURE_IMAGE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                } else {
                    Log.d("", "permission denied");
                }
            }
        }
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
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        mVehicle.setFuel(mFuelsList.get(pos).getId());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
