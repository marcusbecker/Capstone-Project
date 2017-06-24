package br.com.mvbos.fillit.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.widget.Toast;

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

        if (ACTION_SYNC.equals(action)) {
            final String url = context.getString(R.string.sync_url);
            final ContentResolver resolver = context.getContentResolver();

            try {
                final String resp = post(url, "");
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

            System.out.println("it is time!");
        }
    }

}
