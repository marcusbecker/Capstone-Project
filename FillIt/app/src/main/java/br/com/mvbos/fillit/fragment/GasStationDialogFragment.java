package br.com.mvbos.fillit.fragment;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import br.com.mvbos.fillit.R;
import br.com.mvbos.fillit.data.FillItContract;
import br.com.mvbos.fillit.item.FlagSpinnerAdapter;
import br.com.mvbos.fillit.model.FillModel;
import br.com.mvbos.fillit.model.FlagModel;
import br.com.mvbos.fillit.model.GasStationModel;
import br.com.mvbos.fillit.util.ModelBuilder;

//https://guides.codepath.com/android/Using-DialogFragment#overview
public class GasStationDialogFragment extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String BUNDLE_FILL_MODEL = "fill_model";
    private static final int ID_LOADER = 9;

    private FlagModel[] mFlagsList;
    private Spinner mFlagSpinner;
    private FillModel mFill;
    private DialogEditListener mListener;

    public void setListener(DialogEditListener listener) {
        this.mListener = listener;
    }

    public interface DialogEditListener {
        void onFinishEditDialog(GasStationModel gas);
    }

    private EditText mEditText;

    public GasStationDialogFragment() {
    }

    public static GasStationDialogFragment newInstance(FillModel fillModel) {
        GasStationDialogFragment frag = new GasStationDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_FILL_MODEL, fillModel);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gasstation_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments().containsKey(BUNDLE_FILL_MODEL)) {
            mFill = getArguments().getParcelable(BUNDLE_FILL_MODEL);
        }

        getDialog().setTitle(getString(R.string.label_new_gas_station));

        mEditText = (EditText) view.findViewById(R.id.etName);
        mFlagSpinner = (Spinner) view.findViewById(R.id.spGasStation);

        mEditText.requestFocus();

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        view.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogEditListener listener = mListener;

                if (getActivity() instanceof DialogEditListener) {
                    listener = (DialogEditListener) getActivity();
                } else if (getTargetFragment() instanceof DialogEditListener) {
                    listener = (DialogEditListener) getTargetFragment();
                } else if (getParentFragment() instanceof DialogEditListener) {
                    listener = (DialogEditListener) getParentFragment();
                }

                if (listener != null) {
                    final FlagModel flagModel = mFlagsList[mFlagSpinner.getSelectedItemPosition()];

                    GasStationModel gas = new GasStationModel(0);
                    gas.setName(mEditText.getText().toString());
                    gas.setFlag((int) flagModel.getId());
                    gas.setFlagName(flagModel.getName());
                    gas.setFlagIcon(flagModel.getIcon());

                    listener.onFinishEditDialog(gas);
                }

                dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(ID_LOADER, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        final Uri uri = FillItContract.FlagEntry.CONTENT_URI;
        String[] projection = new String[]{
                FillItContract.FlagEntry._ID,
                FillItContract.FlagEntry.COLUMN_NAME_NAME,
                FillItContract.FlagEntry.COLUMN_NAME_ICON,
                FillItContract.FlagEntry.COLUMN_NAME_DATASYNC};

        String selectionClause = null;
        String[] selectionArgs = null;
        String sortOrder = null;

        return new CursorLoader(getActivity(),
                uri,
                projection,
                selectionClause,
                selectionArgs,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            mFlagsList = ModelBuilder.buildFlagList(data);

            SpinnerAdapter adapter = new FlagSpinnerAdapter(getContext(), mFlagsList);
            mFlagSpinner.setAdapter(adapter);
            mFlagSpinner.setSelection((int) mFill.getGasStation());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
