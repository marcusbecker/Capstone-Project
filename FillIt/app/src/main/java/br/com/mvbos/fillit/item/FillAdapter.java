package br.com.mvbos.fillit.item;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import br.com.mvbos.fillit.R;
import br.com.mvbos.fillit.data.FillItContract;

/**
 * Created by Marcus Becker on 15/06/2017.
 */

public class FillAdapter extends RecyclerView.Adapter<FillAdapter.ViewHolder> {

    private Cursor mCursor;
    private File mPath;
    private View.OnClickListener mOnClickItem;

    public FillAdapter(Cursor cursor, View.OnClickListener clickItem) {
        this.mCursor = cursor;
        this.mOnClickItem = clickItem;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageViewPhoto;
        public TextView mTextViewName;
        public TextView mTextViewFuel;

        public TextView mGasStation;
        public TextView mVehicle;
        public TextView mFuel;
        public TextView mDate;
        public TextView mPrice;
        public TextView mLiters;
        public TextView mLat;
        public TextView mLng;

        public ViewHolder(View view) {
            super(view);
            mImageViewPhoto = (ImageView) view.findViewById(R.id.ivPhoto);
            mTextViewName = (TextView) view.findViewById(R.id.tvName);
            mTextViewFuel = (TextView) view.findViewById(R.id.tvFuel);

            mGasStation = (TextView) view.findViewById(R.id.tvGasstation);
            mVehicle = (TextView) view.findViewById(R.id.tvVehicle);
            mFuel = (TextView) view.findViewById(R.id.tvFuel);
            mDate = (TextView) view.findViewById(R.id.tvDate);
            mPrice = (TextView) view.findViewById(R.id.tvPrice);
            mLiters = (TextView) view.findViewById(R.id.tvLiters);
            mLat = (TextView) view.findViewById(R.id.tvLat);
            mLng = (TextView) view.findViewById(R.id.tvLng);
        }
    }

    public Cursor swapCursor(Cursor cursor) {
        if (mCursor == cursor) {
            return null;
        }

        Cursor oldCursor = mCursor;
        this.mCursor = cursor;

        if (this.mCursor != null) {
            this.notifyDataSetChanged();
        }

        return oldCursor;
    }

    @Override
    public FillAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_gas_station, parent, false);
        v.setOnClickListener(mOnClickItem);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        long gasStation = mCursor.getLong(mCursor.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_GASSTATION));
        long vehicle = mCursor.getLong(mCursor.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_VEHICLE));
        long fuel = mCursor.getLong(mCursor.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_FUEL));
        long date = mCursor.getLong(mCursor.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_DATE));
        double price = mCursor.getDouble(mCursor.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_PRICE));
        int liters = mCursor.getInt(mCursor.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_LITERS));
        double lat = mCursor.getDouble(mCursor.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_LAT));
        double lng = mCursor.getDouble(mCursor.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_LNG));

        holder.mGasStation.setText(String.valueOf(gasStation));
        holder.mVehicle.setText(String.valueOf(vehicle));
        holder.mTextViewFuel.setText(String.valueOf(fuel));
        holder.mDate.setText(String.valueOf(date));
        holder.mPrice.setText(String.valueOf(price));
        holder.mLiters.setText(String.valueOf(liters));
        holder.mLat.setText(String.valueOf(lat));
        holder.mLng.setText(String.valueOf(lng));

    }

    public File getPath() {
        return mPath;
    }

    public void setPath(File path) {
        this.mPath = path;
    }

    @Override
    public int getItemCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    public Cursor getCursor() {
        return mCursor;
    }
}