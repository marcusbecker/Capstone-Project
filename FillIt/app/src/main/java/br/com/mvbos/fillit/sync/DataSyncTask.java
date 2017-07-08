package br.com.mvbos.fillit.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Settings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import br.com.mvbos.fillit.R;
import br.com.mvbos.fillit.data.FillItContract;
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

    private static String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static void executeTask(Context context, String action) {
        short load = 1;

        final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        String content = "{\"id\":\"" + androidId + "\"}";

        if (ACTION_SYNC.equals(action)) {
            if (load >= 0) {
                final String url = context.getString(R.string.url_sync);
                final ContentResolver resolver = context.getContentResolver();

                try {
                    final String resp = post(url, content);
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
                    final String resp = post(url, content);
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
                    final String jsonPrices = createJsonPrices(context);
                    final String resp = post(url, content);

                    System.out.println(resp);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String createJsonPrices(Context context) {
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
                FillItContract.FuelEntry.COLUMN_NAME_NAME
        };

        final String selectionClause = FillItContract.FillEntry.COLUMN_NAME_DATASYNC + "=?";
        final String[] selectionArgs = {String.valueOf(0)};
        final String sortOrder = " LIMIT 30";

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

        if (cursor.moveToFirst()) {
            JSONArray arr = new JSONArray();

            try {
                while (cursor.moveToNext()) {
                    JSONObject json = new JSONObject();

                    json.put(FillItContract.FillEntry.COLUMN_NAME_GASSTATION, cursor.getLong(cursor.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_FLAG)));
                    json.put(FillItContract.FillEntry.COLUMN_NAME_FUEL, cursor.getLong(cursor.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_FLAG)));
                    json.put(FillItContract.FillEntry.COLUMN_NAME_DATE, cursor.getLong(cursor.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_FLAG)));
                    json.put(FillItContract.FillEntry.COLUMN_NAME_PRICE, cursor.getDouble(cursor.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_FLAG)));
                    json.put(FillItContract.FillEntry.COLUMN_NAME_LITERS, cursor.getDouble(cursor.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_FLAG)));
                    json.put(FillItContract.FillEntry.COLUMN_NAME_LAT, cursor.getDouble(cursor.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_FLAG)));
                    json.put(FillItContract.FillEntry.COLUMN_NAME_LNG, cursor.getDouble(cursor.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_FLAG)));
                    json.put(FillItContract.GasStationEntry.COLUMN_NAME_FLAG, cursor.getInt(cursor.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_FLAG)));
                    json.put(FillItContract.GasStationEntry.COLUMN_NAME_LAT, cursor.getDouble(cursor.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_LAT)));
                    json.put(FillItContract.GasStationEntry.COLUMN_NAME_LNG, cursor.getDouble(cursor.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_LNG)));
                    json.put(FillItContract.GasStationEntry.COLUMN_NAME_FLAG, cursor.getLong(cursor.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_FLAG)));
                    json.put(FillItContract.FuelEntry.COLUMN_NAME_NAME, cursor.getLong(cursor.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_FLAG)));

                    arr.put(json);
                }

                jObject.put("data", arr);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return jObject.toString();
    }
}
