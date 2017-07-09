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
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FUEL = "fuel";
    public static final String PATH_FLAG = "flag";
    public static final String PATH_VEHICLE = "vehicle";
    public static final String PATH_VEHICLE_JOIN_FUEL = "vehicle_join_fuel";
    public static final String PATH_FILL = "fill";
    public static final String PATH_FILL_JOIN_GASSTATION_JOIN_VEHICLE_JOIN_FUEL = "fill_join_gasstation_join_vehicle_join_fuel";
    public static final String PATH_GASSTATION = "gasstation";
    public static final String PATH_GASSTATION_JOIN_FLAG = "gasstation_join_flag";

    static final String SQL_CREATE_FUEL =
            "CREATE TABLE " + FillItContract.FuelEntry.TABLE_NAME + " (" + FillItContract.FuelEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP + FillItContract.FuelEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP + FillItContract.FuelEntry.COLUMN_NAME_DATASYNC + INT_TYPE + " )";

    static final String SQL_DELETE_FUEL =
            "DROP TABLE IF EXISTS " + FillItContract.FuelEntry.TABLE_NAME;


    static final String SQL_CREATE_FLAG =
            "CREATE TABLE " + FillItContract.FlagEntry.TABLE_NAME + " (" + FillItContract.FlagEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP + FillItContract.FlagEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP + FillItContract.FlagEntry.COLUMN_NAME_ICON + TEXT_TYPE + COMMA_SEP + FillItContract.FlagEntry.COLUMN_NAME_DATASYNC + INT_TYPE + " )";

    static final String SQL_DELETE_FLAG =
            "DROP TABLE IF EXISTS " + FillItContract.FlagEntry.TABLE_NAME;


    static final String SQL_CREATE_VEHICLE =
            "CREATE TABLE " + FillItContract.VehicleEntry.TABLE_NAME + " (" + FillItContract.VehicleEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP + FillItContract.VehicleEntry.COLUMN_NAME_PHOTO + TEXT_TYPE + COMMA_SEP + FillItContract.VehicleEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP + FillItContract.VehicleEntry.COLUMN_NAME_FUEL + INT_TYPE + COMMA_SEP + FillItContract.VehicleEntry.COLUMN_NAME_DATASYNC + INT_TYPE + " )";

    static final String SQL_DELETE_VEHICLE =
            "DROP TABLE IF EXISTS " + FillItContract.VehicleEntry.TABLE_NAME;


    static final String SQL_CREATE_FILL =
            "CREATE TABLE " + FillItContract.FillEntry.TABLE_NAME + " (" + FillItContract.FillEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP + FillItContract.FillEntry.COLUMN_NAME_GASSTATION + INT_TYPE + COMMA_SEP + FillItContract.FillEntry.COLUMN_NAME_VEHICLE + INT_TYPE + COMMA_SEP + FillItContract.FillEntry.COLUMN_NAME_FUEL + INT_TYPE + COMMA_SEP + FillItContract.FillEntry.COLUMN_NAME_DATE + INT_TYPE + COMMA_SEP + FillItContract.FillEntry.COLUMN_NAME_PRICE + REAL_TYPE + COMMA_SEP + FillItContract.FillEntry.COLUMN_NAME_LITERS + INT_TYPE + COMMA_SEP + FillItContract.FillEntry.COLUMN_NAME_LAT + REAL_TYPE + COMMA_SEP + FillItContract.FillEntry.COLUMN_NAME_LNG + REAL_TYPE + COMMA_SEP + FillItContract.FillEntry.COLUMN_NAME_DATASYNC + INT_TYPE + " )";

    static final String SQL_DELETE_FILL =
            "DROP TABLE IF EXISTS " + FillItContract.FillEntry.TABLE_NAME;


    static final String SQL_CREATE_GASSTATION =
            "CREATE TABLE " + FillItContract.GasStationEntry.TABLE_NAME + " (" + FillItContract.GasStationEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP + FillItContract.GasStationEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP + FillItContract.GasStationEntry.COLUMN_NAME_LAT + REAL_TYPE + COMMA_SEP + FillItContract.GasStationEntry.COLUMN_NAME_LNG + REAL_TYPE + COMMA_SEP + FillItContract.GasStationEntry.COLUMN_NAME_FLAG + INT_TYPE + COMMA_SEP + FillItContract.GasStationEntry.COLUMN_NAME_ADDRESS + TEXT_TYPE + COMMA_SEP + FillItContract.GasStationEntry.COLUMN_NAME_DATASYNC + INT_TYPE + " )";

    static final String SQL_DELETE_GASSTATION =
            "DROP TABLE IF EXISTS " + FillItContract.GasStationEntry.TABLE_NAME;


    public static class FuelEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FUEL).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FUEL;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FUEL;

        public static final String TABLE_NAME = "fuel_entry";
        public static final String COLUMN_NAME_NAME = "fuel_name";
        public static final String COLUMN_NAME_DATASYNC = "fuel_datasync";

        public static Uri buildFuelUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static class FlagEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FLAG).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FLAG;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FLAG;

        public static final String TABLE_NAME = "flag_entry";
        public static final String COLUMN_NAME_NAME = "flag_name";
        public static final String COLUMN_NAME_ICON = "flag_icon";
        public static final String COLUMN_NAME_DATASYNC = "flag_datasync";

        public static Uri buildFlagUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static class VehicleEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_VEHICLE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VEHICLE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VEHICLE;

        public static final String TABLE_NAME = "vehicle_entry";
        public static final String COLUMN_NAME_PHOTO = "vehicle_photo";
        public static final String COLUMN_NAME_NAME = "vehicle_name";
        public static final String COLUMN_NAME_FUEL = "vehicle_fuel";
        public static final String COLUMN_NAME_DATASYNC = "vehicle_datasync";

        public static Uri buildVehicleUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static class FillEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FILL).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FILL;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FILL;

        public static final String TABLE_NAME = "fill_entry";
        public static final String COLUMN_NAME_GASSTATION = "fill_gasstation";
        public static final String COLUMN_NAME_VEHICLE = "fill_vehicle";
        public static final String COLUMN_NAME_FUEL = "fill_fuel";
        public static final String COLUMN_NAME_DATE = "fill_date";
        public static final String COLUMN_NAME_PRICE = "fill_price";
        public static final String COLUMN_NAME_LITERS = "fill_liters";
        public static final String COLUMN_NAME_LAT = "fill_lat";
        public static final String COLUMN_NAME_LNG = "fill_lng";
        public static final String COLUMN_NAME_DATASYNC = "fill_datasync";

        public static Uri buildFillUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static class GasStationEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_GASSTATION).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GASSTATION;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GASSTATION;

        public static final String TABLE_NAME = "gasstation_entry";
        public static final String COLUMN_NAME_NAME = "gasstation_name";
        public static final String COLUMN_NAME_LAT = "gasstation_lat";
        public static final String COLUMN_NAME_LNG = "gasstation_lng";
        public static final String COLUMN_NAME_FLAG = "gasstation_flag";
        public static final String COLUMN_NAME_ADDRESS = "gasstation_address";
        public static final String COLUMN_NAME_DATASYNC = "gasstation_datasync";

        public static Uri buildGasStationUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}

