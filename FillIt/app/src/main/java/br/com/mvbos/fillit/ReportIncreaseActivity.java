package br.com.mvbos.fillit;


import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Set;

import br.com.mvbos.fillit.data.FillItContract;

//http://www.android-graphview.org/static-labels/
public class ReportIncreaseActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ID_LOADER = 90;

    private final String[] mProjection = {
            FillItContract.FillEntry._ID,
            FillItContract.FillEntry.COLUMN_NAME_DATE,
            FillItContract.FillEntry.COLUMN_NAME_PRICE,
            FillItContract.FillEntry.COLUMN_NAME_LITERS
    };


    private GraphView graph;

    final DateFormat df = new SimpleDateFormat("dd");

    private final String mSortOrder = FillItContract.FillEntry.COLUMN_NAME_DATE + " ASC";

    private final String mSelectionClause = FillItContract.FillEntry.COLUMN_NAME_DATE + " BETWEEN ? AND ?";

    private final LinkedHashMap<String, Double> cache = new LinkedHashMap<>(10);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_increase);

        graph = (GraphView) findViewById(R.id.graph);
        getLoaderManager().initLoader(ID_LOADER, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Calendar firstDay = Calendar.getInstance();
        firstDay.set(Calendar.DAY_OF_MONTH, 1);

        Calendar lastDay = Calendar.getInstance();
        lastDay.set(Calendar.DAY_OF_MONTH, lastDay.getActualMaximum(Calendar.DAY_OF_MONTH));

        final String[] mSelectionArgs = {
                String.valueOf(firstDay.getTimeInMillis()),
                String.valueOf(lastDay.getTimeInMillis())};

        return new CursorLoader(
                getBaseContext(),
                FillItContract.FillEntry.CONTENT_URI,
                mProjection,
                mSelectionClause,
                mSelectionArgs,
                mSortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        final int dateIndex = cursor.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_DATE);
        final int priceIndex = cursor.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_PRICE);
        final int litersIndex = cursor.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_LITERS);

        while (cursor.moveToNext()) {
            long date = cursor.getLong(dateIndex);
            String strDate = df.format(new Date(date));
            double price = cursor.getDouble(priceIndex);
            int liters = cursor.getInt(litersIndex);

            double old = 0;
            if (cache.containsKey(strDate)) {
                old = cache.get(strDate);
            }

            cache.put(strDate, (price * liters) + old);
        }


        updateGraphic();

    }

    private void updateGraphic() {
        final NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);

        final Set<String> key = cache.keySet();
        int total = key.size();

        if (total == 0) {

        } else {

            if (total < 2) {
                total = 2;
            }

            final String[] hLabel = new String[total];
            final String[] vLabel = new String[total];
            final DataPoint[] points = new DataPoint[total];

            int i = 0;
            for (String s : key) {
                hLabel[i] = s;
                vLabel[i] = numberFormat.format(cache.get(s));
                points[i] = new DataPoint(i, cache.get(s));
                i++;
            }


            if (key.size() == 1) {
                Calendar lastDay = Calendar.getInstance();

                hLabel[i] = String.valueOf(lastDay.getActualMaximum(Calendar.DAY_OF_MONTH));
                vLabel[i] = vLabel[0];
                points[i] = new DataPoint(i, points[0].getY());
            }


            staticLabelsFormatter.setHorizontalLabels(hLabel);
            staticLabelsFormatter.setVerticalLabels(vLabel);

            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
            graph.addSeries(new LineGraphSeries<>(points));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        updateGraphic();
    }
}
