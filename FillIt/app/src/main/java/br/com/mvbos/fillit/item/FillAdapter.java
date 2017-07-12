package br.com.mvbos.fillit.item;

import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.mvbos.fillit.R;
import br.com.mvbos.fillit.data.FillItContract;
import br.com.mvbos.fillit.util.FileUtil;

/**
 * Created by Marcus Becker on 15/06/2017.
 */

public class FillAdapter extends RecyclerView.Adapter<FillAdapter.ViewHolder> {

    private final Resources mResources;
    private Cursor mCursor;
    private File mPath;
    private final View.OnClickListener mOnClickItem;

    private final DateFormat dateFormat;
    private final NumberFormat numberFormat;

    public FillAdapter(Resources rec, Cursor cursor, View.OnClickListener clickItem) {
        this.mResources = rec;
        this.mCursor = cursor;
        this.mOnClickItem = clickItem;
        dateFormat = SimpleDateFormat.getDateInstance();
        numberFormat = NumberFormat.getCurrencyInstance();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mImageViewPhoto;

        public final TextView mGasStation;
        public final TextView mVehicle;
        public final TextView mDate;
        public final TextView mLiters;

        public ViewHolder(View view) {
            super(view);
            mImageViewPhoto = (ImageView) view.findViewById(R.id.ivPhoto);
            mGasStation = (TextView) view.findViewById(R.id.tvGasStation);
            mVehicle = (TextView) view.findViewById(R.id.tvVehicle);
            mDate = (TextView) view.findViewById(R.id.tvDate);
            mLiters = (TextView) view.findViewById(R.id.tvLiters);
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_fill, parent, false);
        v.setOnClickListener(mOnClickItem);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        String gasStation = mCursor.getString(mCursor.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_NAME));
        String photo = mCursor.getString(mCursor.getColumnIndex(FillItContract.VehicleEntry.COLUMN_NAME_PHOTO));
        String vehicle = mCursor.getString(mCursor.getColumnIndex(FillItContract.VehicleEntry.COLUMN_NAME_NAME));
        String fuel = mCursor.getString(mCursor.getColumnIndex(FillItContract.FuelEntry.COLUMN_NAME_NAME));

        long date = mCursor.getLong(mCursor.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_DATE));
        double price = mCursor.getDouble(mCursor.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_PRICE));
        int liters = mCursor.getInt(mCursor.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_LITERS));

        if (gasStation == null) {
            holder.mGasStation.setText(mResources.getString(R.string.no_gas_station));
        } else {
            holder.mGasStation.setText(gasStation);
        }

        String t = mResources.getString(R.string.format_litter_fuel_price);

        holder.mVehicle.setText(String.valueOf(vehicle));
        holder.mDate.setText(dateFormat.format(new Date(date)));
        holder.mLiters.setText(String.format(t, liters, fuel, numberFormat.format(price)));

        if (photo != null && !photo.isEmpty()) {
            final Bitmap imageBitmap = BitmapFactory.decodeFile(new File(mPath, photo).getAbsolutePath());
            holder.mImageViewPhoto.setImageBitmap(FileUtil.roundBitmap(imageBitmap));
        }

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