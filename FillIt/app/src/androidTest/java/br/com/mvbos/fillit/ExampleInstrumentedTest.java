package br.com.mvbos.fillit;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import br.com.mvbos.fillit.data.FillItContract;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private Context appContext;

    @Test
    public void useAppContext() throws Exception {
        appContext = InstrumentationRegistry.getTargetContext();

        long mGasstation = 1;
        long mVehicle = 1;
        long mFuel = 1;
        long mDate = new Date().getTime();
        double mPrice = 3.75;
        int mLiters = 5;
        double mLat = 01001;
        double mLng = 10110;
        long mDataSync = new Date().getTime();

        Uri uri = testInsert(mGasstation, mVehicle, mFuel, mDate, mPrice, mLiters, mLat, mLng, mDataSync);

        assertNotEquals(uri, null);

        Log.e("br.com.mvbos.fillit", "URI " + uri);

        testQuery(mGasstation, mVehicle, mFuel, mDate, mPrice, mLiters, mLat, mLng, mDataSync);

        int ru = testUpdate(mGasstation, mVehicle, mFuel, mDate, mPrice, mLiters, mLat, mLng, mDataSync);

        assertEquals(ru, 1);

        int rd = testDelete();

        assertEquals(rd, 1);

        assertEquals("br.com.mvbos.fillit", appContext.getPackageName());
    }

    private int testDelete() {
        String mSelectionClause = FillItContract.FillEntry._ID + "= ?";
        String[] mSelectionArgs = {"1"};

        int mRowsDeleted;

        mRowsDeleted = getContentResolver().delete(
                FillItContract.FillEntry.CONTENT_URI,
                mSelectionClause,
                mSelectionArgs
        );

        return mRowsDeleted;
    }

    @NonNull
    private int testUpdate(float mGasstation, float mVeichle, float mFuel, float mDate, double mPrice, int mLiters, double mLat, double mLng, float mDataSync) {
        ContentValues mUpdateValues = new ContentValues();
        mUpdateValues.put(FillItContract.FillEntry.COLUMN_NAME_GASSTATION, mGasstation);
        mUpdateValues.put(FillItContract.FillEntry.COLUMN_NAME_VEHICLE, mVeichle);
        mUpdateValues.put(FillItContract.FillEntry.COLUMN_NAME_FUEL, mFuel);
        mUpdateValues.put(FillItContract.FillEntry.COLUMN_NAME_DATE, mDate);
        mUpdateValues.put(FillItContract.FillEntry.COLUMN_NAME_PRICE, mPrice);
        mUpdateValues.put(FillItContract.FillEntry.COLUMN_NAME_LITERS, mLiters);
        mUpdateValues.put(FillItContract.FillEntry.COLUMN_NAME_LAT, mLat);
        mUpdateValues.put(FillItContract.FillEntry.COLUMN_NAME_LNG, mLng);
        mUpdateValues.put(FillItContract.FillEntry.COLUMN_NAME_DATASYNC, mDataSync);

        int mRowsUpdated;

        String[] mSelectionArgs = {"1"};

        String mSelectionClause = FillItContract.FillEntry._ID + " = ?";

        mRowsUpdated = getContentResolver().update(
                FillItContract.FillEntry.CONTENT_URI,
                mUpdateValues,
                mSelectionClause,
                mSelectionArgs
        );

        return mRowsUpdated;
    }

    private Uri testInsert(float mGasstation, float mVeichle, float mFuel, float mDate, double mPrice, int mLiters, double mLat, double mLng, float mDataSync) {
        Uri mFillUri;
        ContentValues mFillValues = new ContentValues();

        mFillValues.put(FillItContract.FillEntry.COLUMN_NAME_GASSTATION, mGasstation);
        mFillValues.put(FillItContract.FillEntry.COLUMN_NAME_VEHICLE, mVeichle);
        mFillValues.put(FillItContract.FillEntry.COLUMN_NAME_FUEL, mFuel);
        mFillValues.put(FillItContract.FillEntry.COLUMN_NAME_DATE, mDate);
        mFillValues.put(FillItContract.FillEntry.COLUMN_NAME_PRICE, mPrice);
        mFillValues.put(FillItContract.FillEntry.COLUMN_NAME_LITERS, mLiters);
        mFillValues.put(FillItContract.FillEntry.COLUMN_NAME_LAT, mLat);
        mFillValues.put(FillItContract.FillEntry.COLUMN_NAME_LNG, mLng);
        mFillValues.put(FillItContract.FillEntry.COLUMN_NAME_DATASYNC, mDataSync);

        mFillUri = getContentResolver().insert(FillItContract.FillEntry.CONTENT_URI, mFillValues);

        return mFillUri;
    }

    @NonNull
    private void testQuery(float mGasstation, float mVeichle, float mFuel, float mDate, double mPrice, int mLiters, double mLat, double mLng, float mDataSync) {
        Cursor mCursor;
        String mSortOrder = null;

        String[] mProjection = {
                FillItContract.FillEntry._ID,
                FillItContract.FillEntry.COLUMN_NAME_GASSTATION,
                FillItContract.FillEntry.COLUMN_NAME_VEHICLE,
                FillItContract.FillEntry.COLUMN_NAME_FUEL,
                FillItContract.FillEntry.COLUMN_NAME_DATE,
                FillItContract.FillEntry.COLUMN_NAME_PRICE,
                FillItContract.FillEntry.COLUMN_NAME_LITERS,
                FillItContract.FillEntry.COLUMN_NAME_LAT,
                FillItContract.FillEntry.COLUMN_NAME_LNG,
                FillItContract.FillEntry.COLUMN_NAME_DATASYNC
        };
        String mSelectionClause = ""; //FillItContract.FillEntry._ID + " = ?"
        String[] mSelectionArgs = {};

        mCursor = getContentResolver().query(
                FillItContract.FillEntry.CONTENT_URI,
                mProjection,
                mSelectionClause,
                mSelectionArgs,
                mSortOrder);

        if (mCursor != null) {
            int gasstationIndex = mCursor.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_GASSTATION);
            int veichleIndex = mCursor.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_VEHICLE);
            int fuelIndex = mCursor.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_FUEL);
            int dateIndex = mCursor.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_DATE);
            int priceIndex = mCursor.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_PRICE);
            int litersIndex = mCursor.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_LITERS);
            int latIndex = mCursor.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_LAT);
            int lngIndex = mCursor.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_LNG);
            int dataSyncIndex = mCursor.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_DATASYNC);

            while (mCursor.moveToNext()) {
                long gasstation = mCursor.getLong(gasstationIndex);
                long vehicle = mCursor.getLong(veichleIndex);
                float fuel = mCursor.getLong(fuelIndex);
                long date = mCursor.getLong(dateIndex);
                double price = mCursor.getDouble(priceIndex);
                int liters = mCursor.getInt(litersIndex);
                double lat = mCursor.getDouble(latIndex);
                double lng = mCursor.getDouble(lngIndex);
                long dataSync = mCursor.getLong(dataSyncIndex);

                assertEquals(mGasstation, gasstation, 0.002);
                assertEquals(mVeichle, vehicle, 0.002);
                assertEquals(mFuel, fuel, 0.002);
                assertEquals(mDate, date, 0.002);
                assertEquals(mPrice, price, 0.002);
                assertEquals(mLiters, liters, 0.002);
                assertEquals(mLat, lat, 0.002);
                assertEquals(mLng, lng, 0.002);
                assertEquals(mDataSync, dataSync, 0.002);
            }
        } else {
        }
    }

    public ContentResolver getContentResolver() {
        return appContext.getContentResolver();
    }
}
