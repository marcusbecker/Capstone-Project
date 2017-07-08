package br.com.mvbos.fillit.sync;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.Settings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import br.com.mvbos.fillit.R;
import br.com.mvbos.fillit.data.FillItContract;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Marcus Becker on 20/06/2017.
 */


public class DataSyncTask {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static final String ACTION_SYNC = "dataSync";

    private static OkHttpClient client = new OkHttpClient();


    private static String get(String url, String json) throws IOException {
        RequestBody body = FormBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    private static String post(String url, String json) throws IOException {

        RequestBody formBody = new FormBody.Builder()
                .add("data", json)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static void executeTask(Context context, String action) {
        short load = 2;

        final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        JSONObject content = new JSONObject();
        try {
            content.put("id", androidId).toString();
            content.put("locale", Locale.getDefault().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (ACTION_SYNC.equals(action)) {
            if (load == 0) {
                final String url = context.getString(R.string.url_sync);
                final ContentResolver resolver = context.getContentResolver();

                try {
                    final String resp = get(url, content.toString());
                    final JSONObject jObject = new JSONObject(resp);
                    final JSONArray fuel = jObject.getJSONArray("Fuel");
                    final long dataSync = Calendar.getInstance().getTimeInMillis();

                    ContentValues[] values = new ContentValues[fuel.length()];

                    for (int i = 0; i < fuel.length(); i++) {
                        values[i] = new ContentValues();
                        final String name = fuel.getJSONObject(i).getString("name");

                        values[i].put(FillItContract.FuelEntry._ID, i + 1);
                        values[i].put(FillItContract.FuelEntry.COLUMN_NAME_NAME, name);
                        values[i].put(FillItContract.FuelEntry.COLUMN_NAME_DATASYNC, dataSync);
                    }

                    resolver.bulkInsert(FillItContract.FuelEntry.CONTENT_URI, values);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            if (load == 1) {
                final String url = context.getString(R.string.url_flags);
                final ContentResolver resolver = context.getContentResolver();

                try {
                    final String resp = get(url, content.toString());
                    final JSONObject jObject = new JSONObject(resp);
                    final JSONArray flag = jObject.getJSONArray("Flag");
                    final long dataSync = Calendar.getInstance().getTimeInMillis();

                    ContentValues[] values = new ContentValues[flag.length()];

                    for (int i = 0; i < flag.length(); i++) {
                        values[i] = new ContentValues();
                        final int id = flag.getJSONObject(i).getInt("id");
                        final String icon = flag.getJSONObject(i).getString("icon");
                        final String name = flag.getJSONObject(i).getString("name");

                        values[i].put(FillItContract.FlagEntry._ID, id);
                        values[i].put(FillItContract.FlagEntry.COLUMN_NAME_NAME, name);
                        values[i].put(FillItContract.FlagEntry.COLUMN_NAME_ICON, icon);
                        values[i].put(FillItContract.FlagEntry.COLUMN_NAME_DATASYNC, dataSync);
                    }

                    resolver.bulkInsert(FillItContract.FlagEntry.CONTENT_URI, values);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (load == 2) {

                try {
                    final String url = context.getString(R.string.url_sync_prices);
                    final JSONObject jsonPrices = createJsonPrices(context);
                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put(content);
                    jsonArray.put(jsonPrices);

                    final String resp = post(url, jsonArray.toString());

                    System.out.println(resp);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static JSONObject createJsonPrices(Context context) {
        final String[] projection = {
                FillItContract.FillEntry.TABLE_NAME + "." + FillItContract.FillEntry._ID,
                FillItContract.FillEntry.COLUMN_NAME_GASSTATION,
                FillItContract.FillEntry.COLUMN_NAME_FUEL,
                FillItContract.FillEntry.COLUMN_NAME_DATE,
                FillItContract.FillEntry.COLUMN_NAME_PRICE,
                FillItContract.FillEntry.COLUMN_NAME_LITERS,
                FillItContract.FillEntry.COLUMN_NAME_LAT,
                FillItContract.FillEntry.COLUMN_NAME_LNG,
                FillItContract.GasStationEntry.COLUMN_NAME_LAT,
                FillItContract.GasStationEntry.COLUMN_NAME_LNG,
                FillItContract.GasStationEntry.COLUMN_NAME_FLAG,
        };

        final String selectionClause = FillItContract.FillEntry.COLUMN_NAME_DATASYNC + " IS NULL";
        final String[] selectionArgs = {};
        final String sortOrder = null;

        final Uri uri = FillItContract.BASE_CONTENT_URI
                .buildUpon()
                .appendPath(FillItContract.PATH_FILL_JOIN_GASSTATION_JOIN_VEHICLE_JOIN_FUEL)
                .build();

        final Cursor cursor = context.getContentResolver().query(
                uri,
                projection,
                selectionClause,
                selectionArgs,
                sortOrder);

        final JSONObject jObject = new JSONObject();

        short limit = 30;
        long longDate = new Date().getTime();
        ArrayList<ContentProviderOperation> ops = new ArrayList<>(cursor.getCount());

        try {
            JSONArray arr = new JSONArray();

            while (cursor.moveToNext()) {
                if (limit == 0) {
                    break;
                }

                limit--;

                final long id = cursor.getLong(cursor.getColumnIndex(FillItContract.FillEntry._ID));
                final String idSel = FillItContract.FillEntry.TABLE_NAME + "." + FillItContract.FillEntry._ID + "=?";

                ops.add(
                        ContentProviderOperation.newUpdate(FillItContract.FillEntry.CONTENT_URI)
                                .withValue(FillItContract.FillEntry.COLUMN_NAME_DATASYNC, String.valueOf(longDate))
                                .withSelection(idSel, new String[]{String.valueOf(id)})
                                .build());

                JSONObject json = new JSONObject();

                json.put(FillItContract.FillEntry.COLUMN_NAME_GASSTATION, cursor.getLong(cursor.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_GASSTATION)));
                json.put(FillItContract.FillEntry.COLUMN_NAME_FUEL, cursor.getLong(cursor.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_FUEL)));
                json.put(FillItContract.FillEntry.COLUMN_NAME_DATE, cursor.getLong(cursor.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_DATE)));
                json.put(FillItContract.FillEntry.COLUMN_NAME_PRICE, cursor.getDouble(cursor.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_PRICE)));
                json.put(FillItContract.FillEntry.COLUMN_NAME_LITERS, cursor.getDouble(cursor.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_LITERS)));
                json.put(FillItContract.FillEntry.COLUMN_NAME_LAT, cursor.getDouble(cursor.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_LAT)));
                json.put(FillItContract.FillEntry.COLUMN_NAME_LNG, cursor.getDouble(cursor.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_LNG)));
                json.put(FillItContract.GasStationEntry.COLUMN_NAME_FLAG, cursor.getInt(cursor.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_FLAG)));
                json.put(FillItContract.GasStationEntry.COLUMN_NAME_LAT, cursor.getDouble(cursor.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_LAT)));
                json.put(FillItContract.GasStationEntry.COLUMN_NAME_LNG, cursor.getDouble(cursor.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_LNG)));
                json.put(FillItContract.GasStationEntry.COLUMN_NAME_FLAG, cursor.getLong(cursor.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_FLAG)));

                arr.put(json);
            }

            jObject.put("data", arr);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        cursor.close();

        if (!ops.isEmpty()) {
            try {
                context.getContentResolver().applyBatch(FillItContract.CONTENT_AUTHORITY, ops);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (OperationApplicationException e) {
                e.printStackTrace();
            }
        }

        return jObject;
    }
}
