package br.com.mvbos.fillit.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import br.com.mvbos.fillit.R;
import br.com.mvbos.fillit.data.FillItContract;
import br.com.mvbos.fillit.model.VehicleModel;
import br.com.mvbos.fillit.util.FileUtil;

public class NewVehicleFragment extends Fragment {
    private static final String ARG_PARAM_ID = "param_id";
    private static final String ARG_PARAM_PATH = "param_path";
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int PERMISSIONS_REQUEST_CAPTURE_IMAGE = 2;

    private VehicleModel mVehicle;

    private ImageButton mPhoto;
    private EditText mName;
    private EditText mFuel;

    private OnFragmentInteractionListener mListener;
    private String mCurrentPhotoPath;

    private File mStorageDir;


    public NewVehicleFragment() {
    }

    public static NewVehicleFragment newInstance(VehicleModel v) {
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
                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
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

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                FileUtil.setPic(mPhoto, imageBitmap, mCurrentPhotoPath);
                //mPhoto.setImageBitmap(imageBitmap);
                mVehicle.setPhoto(new File(mCurrentPhotoPath).getName());
            }
        } else {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                FileUtil.setPic(mPhoto, mCurrentPhotoPath);
                mVehicle.setPhoto(new File(mCurrentPhotoPath).getName());
            }
        }

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

        String fuel = mFuel.getText().toString();

        if (!fuel.isEmpty()) {
            mVehicle.setFuel(Long.valueOf(mFuel.getText().toString()));
        } else {
            mVehicle.setFuel(0);
        }

        mVehicle.setName(mName.getText().toString());
        //outState.putParcelable(ARG_PARAM_ID, mVehicle);
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

        mPhoto = (ImageButton) view.findViewById(R.id.ibPhoto);
        mName = (EditText) view.findViewById(R.id.etName);
        mFuel = (EditText) view.findViewById(R.id.etFuel);

        mName.setText(mVehicle.getName());
        mFuel.setText(String.valueOf(mVehicle.getFuel()));

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
                        Toast.makeText(getContext(), "Permissão negada", Toast.LENGTH_SHORT).show();
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
                Uri mVehicleUri;
                ContentValues mVehicleValues = new ContentValues();

                mVehicle.setFuel(0);
                mVehicle.setName(mName.getText().toString());

                mVehicleValues.put(FillItContract.VehicleEntry.COLUMN_NAME_PHOTO, mVehicle.getPhoto());
                mVehicleValues.put(FillItContract.VehicleEntry.COLUMN_NAME_NAME, mVehicle.getName());
                mVehicleValues.put(FillItContract.VehicleEntry.COLUMN_NAME_FUEL, mVehicle.getFuel());

                if (mVehicle.getId() > 0) {
                    view.getContext().getContentResolver().update(
                            FillItContract.VehicleEntry.CONTENT_URI,
                            mVehicleValues,
                            FillItContract.VehicleEntry._ID + "=?",
                            new String[]{String.valueOf(mVehicle.getId())}
                    );

                    mVehicleUri = new ContentUris().withAppendedId(FillItContract.VehicleEntry.CONTENT_URI, mVehicle.getId());

                } else {
                    mVehicleUri = view.getContext().getContentResolver().insert(
                            FillItContract.VehicleEntry.CONTENT_URI,
                            mVehicleValues
                    );
                }

                onButtonPressed(mVehicleUri);
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

                return;
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

}
