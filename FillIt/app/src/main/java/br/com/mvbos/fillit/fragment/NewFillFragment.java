package br.com.mvbos.fillit.fragment;

import android.content.ContentResolver;
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
import android.widget.Toast;

import java.util.Calendar;

import br.com.mvbos.fillit.R;
import br.com.mvbos.fillit.data.FillItContract;
import br.com.mvbos.fillit.model.FillModel;
import br.com.mvbos.fillit.util.Converter;

public class NewFillFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";

    private FillModel mFill;
    private EditText mGasstation;
    private EditText mVehicle;
    private EditText mFuel;
    private EditText mDate;
    private EditText mPrice;
    private EditText mLiters;


    private OnFragmentInteractionListener mListener;

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

        mGasstation = (EditText) view.findViewById(R.id.etGasstation);
        mVehicle = (EditText) view.findViewById(R.id.etVehicle);
        mFuel = (EditText) view.findViewById(R.id.etFuel);
        mDate = (EditText) view.findViewById(R.id.etDate);
        mPrice = (EditText) view.findViewById(R.id.etPrice);
        mLiters = (EditText) view.findViewById(R.id.etLiters);


        view.findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean success;
                final ContentResolver resolver = view.getContext().getContentResolver();
                ContentValues mFillValues = new ContentValues();

                mFill.setGasstation(0);
                mFill.setVehicle(1);
                mFill.setFuel(1);
                mFill.setPrice(Converter.toFloat(mPrice, 0f));
                mFill.setLiters(Math.round(Converter.toFloat(mLiters, 0f)));

                mFillValues.put(FillItContract.FillEntry.COLUMN_NAME_GASSTATION, mFill.getGasstation());
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
}
