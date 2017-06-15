package br.com.mvbos.fillit.item;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.mvbos.fillit.R;
import br.com.mvbos.fillit.data.FillItContract;

/**
 * Created by Marcus Becker on 15/06/2017.
 */

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.ViewHolder> {

    private Cursor mCursor;
    private View.OnClickListener mOnClickItem;

    public VehicleAdapter(Cursor cursor, View.OnClickListener clickItem) {
        this.mCursor = cursor;
        this.mOnClickItem = clickItem;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextViewPhoto;
        public TextView mTextViewName;
        public TextView mTextViewFuel;

        //public TextView mTextViewDataSync;

        public ViewHolder(View view) {
            super(view);
            mTextViewPhoto = (TextView) view.findViewById(R.id.tvPhoto);
            mTextViewName = (TextView) view.findViewById(R.id.tvName);
            mTextViewFuel = (TextView) view.findViewById(R.id.tvFuel);
            //mTextViewDataSync = (TextView) view.findViewById(R.id.tvDataSync);
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
    public VehicleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_vehicle, parent, false);
        //v.setOnClickListener(mOnClickItem);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        String photo = mCursor.getString(mCursor.getColumnIndex(FillItContract.VehicleEntry.COLUMN_NAME_PHOTO));
        String name = mCursor.getString(mCursor.getColumnIndex(FillItContract.VehicleEntry.COLUMN_NAME_NAME));
        long fuel = mCursor.getLong(mCursor.getColumnIndex(FillItContract.VehicleEntry.COLUMN_NAME_FUEL));
        //Date dataSync = mCursor.getDate(mCursor.getColumnIndex(FillItContract.VehicleEntry.COLUMN_NAME_DATASYNC));

        holder.mTextViewPhoto.setText(photo);
        holder.mTextViewName.setText(name);
        //holder.mTextViewFuel.setText(fuel);
        //holder.mTextViewDataSync.setText(dataSync);

        holder.mTextViewName.setOnClickListener(mOnClickItem);
    }

    @Override
    public int getItemCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    public Cursor getCursor() {
        return mCursor;
    }
}