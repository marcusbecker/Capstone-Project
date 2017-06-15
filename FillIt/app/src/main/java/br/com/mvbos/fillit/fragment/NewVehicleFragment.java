package br.com.mvbos.fillit.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import br.com.mvbos.fillit.R;
import br.com.mvbos.fillit.data.FillItContract;

public class NewVehicleFragment extends Fragment {
    private static final String ARG_PARAM_ID = "param_id";

    private Long mId;

    private EditText mPhoto;
    private EditText mName;
    private EditText mFuel;

    private OnFragmentInteractionListener mListener;

    public NewVehicleFragment() {
    }

    public static NewVehicleFragment newInstance(long id) {
        NewVehicleFragment fragment = new NewVehicleFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM_ID, id);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId = getArguments().getLong(ARG_PARAM_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_new_vehicle, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPhoto = (EditText) view.findViewById(R.id.etPhoto);
        mName = (EditText) view.findViewById(R.id.etName);
        mFuel = (EditText) view.findViewById(R.id.etFuel);


        view.findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri mVehicleUri;
                ContentValues mVehicleValues = new ContentValues();

                //mVehicleValues.put(FillItContract.VehicleEntry.COLUMN_NAME_PHOTO, "");
                mVehicleValues.put(FillItContract.VehicleEntry.COLUMN_NAME_NAME, mName.getText().toString());
                //mVehicleValues.put(FillItContract.VehicleEntry.COLUMN_NAME_FUEL, "");

                mVehicleUri = view.getContext().getContentResolver().insert(
                        FillItContract.VehicleEntry.CONTENT_URI,
                        mVehicleValues
                );

                onButtonPressed(mVehicleUri);
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

}
