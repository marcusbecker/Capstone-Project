package br.com.mvbos.fillit.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Marcus Becker on 11/06/2017.
 */

public class FillItContract {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";

    public static final String CONTENT_AUTHORITY = "br.com.mvbos.fillit";
    public static final Uri BASE_CONTENTURI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FUEL = "fuel";
    public static final String PATH_FLAG = "flag";
    public static final String PATH_VEHICLE = "vehicle";
    public static final String PATH_FILL = "fill";
    public static final String PATH_GASSTATION = "gasstation";

    protected static final String SQL_CREATE_FUEL =
            "CREATE TABLE " + FillItContract.FuelEntry.TABLE_NAME + " (" + FillItContract.FuelEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP + FillItContract.FuelEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP + FillItContract.FuelEntry.COLUMN_NAME_DATASYNC + INT_TYPE + " )";

    protected static final String SQL_DELETE_FUEL =
            "DROP TABLE IF EXISTS " + FillItContract.FuelEntry.TABLE_NAME;


    protected static final String SQL_CREATE_FLAG =
            "CREATE TABLE " + FillItContract.FlagEntry.TABLE_NAME + " (" + FillItContract.FlagEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP + FillItContract.FlagEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP + FillItContract.FlagEntry.COLUMN_NAME_ICON + TEXT_TYPE + COMMA_SEP + FillItContract.FlagEntry.COLUMN_NAME_DATASYNC + INT_TYPE + " )";

    protected static final String SQL_DELETE_FLAG =
            "DROP TABLE IF EXISTS " + FillItContract.FlagEntry.TABLE_NAME;


    protected static final String SQL_CREATE_VEHICLE =
            "CREATE TABLE " + FillItContract.VehicleEntry.TABLE_NAME + " (" + FillItContract.VehicleEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP + FillItContract.VehicleEntry.COLUMN_NAME_PHOTO + TEXT_TYPE + COMMA_SEP + FillItContract.VehicleEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP + FillItContract.VehicleEntry.COLUMN_NAME_FUEL + INT_TYPE + COMMA_SEP + FillItContract.VehicleEntry.COLUMN_NAME_DATASYNC + INT_TYPE + " )";

    protected static final String SQL_DELETE_VEHICLE =
            "DROP TABLE IF EXISTS " + FillItContract.VehicleEntry.TABLE_NAME;


    protected static final String SQL_CREATE_FILL =
            "CREATE TABLE " + FillItContract.FillEntry.TABLE_NAME + " (" + FillItContract.FillEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP + FillItContract.FillEntry.COLUMN_NAME_GASSTATION + INT_TYPE + COMMA_SEP + FillItContract.FillEntry.COLUMN_NAME_VEHICLE + INT_TYPE + COMMA_SEP + FillItContract.FillEntry.COLUMN_NAME_FUEL + INT_TYPE + COMMA_SEP + FillItContract.FillEntry.COLUMN_NAME_DATE + INT_TYPE + COMMA_SEP + FillItContract.FillEntry.COLUMN_NAME_PRICE + REAL_TYPE + COMMA_SEP + FillItContract.FillEntry.COLUMN_NAME_LITERS + INT_TYPE + COMMA_SEP + FillItContract.FillEntry.COLUMN_NAME_LAT + REAL_TYPE + COMMA_SEP + FillItContract.FillEntry.COLUMN_NAME_LNG + REAL_TYPE + COMMA_SEP + FillItContract.FillEntry.COLUMN_NAME_DATASYNC + INT_TYPE + " )";

    protected static final String SQL_DELETE_FILL =
            "DROP TABLE IF EXISTS " + FillItContract.FillEntry.TABLE_NAME;


    protected static final String SQL_CREATE_GASSTATION =
            "CREATE TABLE " + FillItContract.GasStationEntry.TABLE_NAME + " (" + FillItContract.GasStationEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP + FillItContract.GasStationEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP + FillItContract.GasStationEntry.COLUMN_NAME_LAT + REAL_TYPE + COMMA_SEP + FillItContract.GasStationEntry.COLUMN_NAME_LNG + REAL_TYPE + COMMA_SEP + FillItContract.GasStationEntry.COLUMN_NAME_FLAG + INT_TYPE + COMMA_SEP + FillItContract.GasStationEntry.COLUMN_NAME_ADDRESS + TEXT_TYPE + COMMA_SEP + FillItContract.GasStationEntry.COLUMN_NAME_DATASYNC + INT_TYPE + " )";

    protected static final String SQL_DELETE_GASSTATION =
            "DROP TABLE IF EXISTS " + FillItContract.GasStationEntry.TABLE_NAME;


    public static class FuelEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENTURI.buildUpon().appendPath(PATH_FUEL).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FUEL;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FUEL;

        public static final String TABLE_NAME = "fuel_entry";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DATASYNC = "datasync";

        public static Uri buildFuelUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static class FlagEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENTURI.buildUpon().appendPath(PATH_FLAG).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FLAG;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FLAG;

        public static final String TABLE_NAME = "flag_entry";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_ICON = "icon";
        public static final String COLUMN_NAME_DATASYNC = "datasync";

        public static Uri buildFlagUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static class VehicleEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENTURI.buildUpon().appendPath(PATH_VEHICLE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VEHICLE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VEHICLE;

        public static final String TABLE_NAME = "vehicle_entry";
        public static final String COLUMN_NAME_PHOTO = "photo";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_FUEL = "fuel";
        public static final String COLUMN_NAME_DATASYNC = "datasync";

        public static Uri buildVehicleUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static class FillEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENTURI.buildUpon().appendPath(PATH_FILL).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FILL;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FILL;

        public static final String TABLE_NAME = "fill_entry";
        public static final String COLUMN_NAME_GASSTATION = "gasstation";
        public static final String COLUMN_NAME_VEHICLE = "vehicle";
        public static final String COLUMN_NAME_FUEL = "fuel";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_PRICE = "price";
        public static final String COLUMN_NAME_LITERS = "liters";
        public static final String COLUMN_NAME_LAT = "lat";
        public static final String COLUMN_NAME_LNG = "lng";
        public static final String COLUMN_NAME_DATASYNC = "datasync";

        public static Uri buildFillUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static class GasStationEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENTURI.buildUpon().appendPath(PATH_GASSTATION).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GASSTATION;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GASSTATION;

        public static final String TABLE_NAME = "gasstation_entry";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_LAT = "lat";
        public static final String COLUMN_NAME_LNG = "lng";
        public static final String COLUMN_NAME_FLAG = "flag";
        public static final String COLUMN_NAME_ADDRESS = "address";
        public static final String COLUMN_NAME_DATASYNC = "datasync";

        public static Uri buildGasStationUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}



