package br.com.mvbos.fillit.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.io.File;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.mvbos.fillit.R;
import br.com.mvbos.fillit.data.FillItContract;
import br.com.mvbos.fillit.model.FillModel;
import br.com.mvbos.fillit.util.FileUtil;
import br.com.mvbos.fillit.util.ModelBuilder;

/**
 * Created by Marcus Becker on 11/07/2017.
 */

public class WidgetSpentRemoteViewsService extends RemoteViewsService {

    private static final String TAG = "WidgetSpent";

    private class LoremViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        private FillModel[] fillModels;
        private Context context = null;
        private DateFormat dateFormat;
        private NumberFormat numberFormat;
        final File mPath;

        public LoremViewsFactory(Context context) {
            this.context = context;
            mPath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        }

        @Override
        public void onCreate() {

            dateFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT);
            numberFormat = NumberFormat.getCurrencyInstance();

            final String[] mProjection = {
                    FillItContract.FillEntry.TABLE_NAME + "." + FillItContract.FillEntry._ID,
                    FillItContract.FillEntry.COLUMN_NAME_GASSTATION,
                    FillItContract.FillEntry.COLUMN_NAME_VEHICLE,
                    FillItContract.FillEntry.COLUMN_NAME_FUEL,
                    FillItContract.FillEntry.COLUMN_NAME_DATE,
                    FillItContract.FillEntry.COLUMN_NAME_PRICE,
                    FillItContract.FillEntry.COLUMN_NAME_LITERS,
                    FillItContract.FillEntry.COLUMN_NAME_LAT,
                    FillItContract.FillEntry.COLUMN_NAME_LNG,
                    FillItContract.GasStationEntry.COLUMN_NAME_NAME,
                    FillItContract.GasStationEntry.COLUMN_NAME_LAT,
                    FillItContract.GasStationEntry.COLUMN_NAME_LNG,
                    FillItContract.GasStationEntry.COLUMN_NAME_FLAG,
                    FillItContract.VehicleEntry.COLUMN_NAME_PHOTO,
                    FillItContract.VehicleEntry.COLUMN_NAME_NAME,
                    FillItContract.FuelEntry.COLUMN_NAME_NAME
            };

            final Calendar c = Calendar.getInstance();
            c.roll(Calendar.DAY_OF_MONTH, -5);

            final String mSelectionClause = FillItContract.FillEntry.COLUMN_NAME_DATE + " > ?";
            final String[] mSelectionArgs = {String.valueOf(c.getTimeInMillis())};
            final String mSortOrder = null;

            final Uri uri = FillItContract.BASE_CONTENT_URI
                    .buildUpon()
                    .appendPath(FillItContract.PATH_FILL_JOIN_GASSTATION_JOIN_VEHICLE_JOIN_FUEL)
                    .build();

            final Cursor query = getContentResolver().query(uri, mProjection, mSelectionClause, mSelectionArgs, mSortOrder);
            fillModels = ModelBuilder.buildFillList(query);
        }

        @Override
        public void onDestroy() {
            fillModels = null;
        }

        @Override
        public int getCount() {
            return fillModels != null ? fillModels.length : 0;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews row = new RemoteViews(
                    context.getPackageName(),
                    R.layout.item_widget_spent);

            final FillModel f = fillModels[position];
            final String strSummary = context.getString(R.string.format_litter_price);

            row.setTextViewText(R.id.tvWidgetTitle, f.getVehicleName());

            row.setTextViewText(R.id.tvWidgetSubTitle, f.getFuelName());

            row.setTextViewText(R.id.tvWidgetSumm, String.format(strSummary,
                    f.getLiters(),
                    numberFormat.format(f.getPrice()),
                    dateFormat.format(new Date(f.getDate()))));

            if (f.getVehicleIcon() != null && !f.getVehicleIcon().isEmpty()) {
                final Bitmap imageBitmap = BitmapFactory.decodeFile(new File(mPath, f.getVehicleIcon()).getAbsolutePath());
                row.setImageViewBitmap(R.id.ivWidgetPhoto, FileUtil.roundBitmap(imageBitmap));
            }

            Intent i = new Intent();
            Bundle extras = new Bundle();
            extras.putLong(SpentWidgetProvider.EXTRA_WIDGET_ID, fillModels[position].getId());
            i.putExtras(extras);
            row.setOnClickFillInIntent(R.id.ivWidgetPhoto, i);

            return (row);
        }

        @Override
        public RemoteViews getLoadingView() {
            return (null);
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public void onDataSetChanged() {
            Log.d(TAG, "data change");
        }
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new LoremViewsFactory(this.getApplicationContext());
    }
}