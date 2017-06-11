package br.com.mvbos.fillit.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by Marcus Becker on 11/06/2017.
 */

public class FillItProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FillItDbHelper mOpenHelper;
    static final int FUEL = 100;
    static final int FUEL_WITH_NAME = 101;
    static final int FUEL_WITH_DATASYNC = 102;

    static final int FLAG = 200;
    static final int FLAG_WITH_NAME = 201;
    static final int FLAG_WITH_ICON = 202;
    static final int FLAG_WITH_DATASYNC = 203;

    static final int VEHICLE = 300;
    static final int VEHICLE_WITH_PHOTO = 301;
    static final int VEHICLE_WITH_NAME = 302;
    static final int VEHICLE_WITH_FUEL = 303;
    static final int VEHICLE_WITH_DATASYNC = 304;

    static final int FILL = 400;
    static final int FILL_WITH_GASSTATION = 401;
    static final int FILL_WITH_VEHICLE = 402;
    static final int FILL_WITH_FUEL = 403;
    static final int FILL_WITH_DATE = 404;
    static final int FILL_WITH_PRICE = 405;
    static final int FILL_WITH_LITERS = 406;
    static final int FILL_WITH_LAT = 407;
    static final int FILL_WITH_LNG = 408;
    static final int FILL_WITH_DATASYNC = 409;

    static final int GASSTATION = 500;
    static final int GASSTATION_WITH_NAME = 501;
    static final int GASSTATION_WITH_LAT = 502;
    static final int GASSTATION_WITH_LNG = 503;
    static final int GASSTATION_WITH_FLAG = 504;
    static final int GASSTATION_WITH_ADDRESS = 505;
    static final int GASSTATION_WITH_DATASYNC = 506;

    private static final String sFuelId = FillItContract.FuelEntry.TABLE_NAME + "." + FillItContract.FuelEntry._ID + " = ? ";
    private static final String sFuelName = FillItContract.FuelEntry.TABLE_NAME + "." + FillItContract.FuelEntry.COLUMN_NAME_NAME + " = ? ";
    private static final String sFuelDataSync = FillItContract.FuelEntry.TABLE_NAME + "." + FillItContract.FuelEntry.COLUMN_NAME_DATASYNC + " = ? ";

    private static final String sFlagId = FillItContract.FlagEntry.TABLE_NAME + "." + FillItContract.FlagEntry._ID + " = ? ";
    private static final String sFlagName = FillItContract.FlagEntry.TABLE_NAME + "." + FillItContract.FlagEntry.COLUMN_NAME_NAME + " = ? ";
    private static final String sFlagIcon = FillItContract.FlagEntry.TABLE_NAME + "." + FillItContract.FlagEntry.COLUMN_NAME_ICON + " = ? ";
    private static final String sFlagDataSync = FillItContract.FlagEntry.TABLE_NAME + "." + FillItContract.FlagEntry.COLUMN_NAME_DATASYNC + " = ? ";

    private static final String sVehicleId = FillItContract.VehicleEntry.TABLE_NAME + "." + FillItContract.VehicleEntry._ID + " = ? ";
    private static final String sVehiclePhoto = FillItContract.VehicleEntry.TABLE_NAME + "." + FillItContract.VehicleEntry.COLUMN_NAME_PHOTO + " = ? ";
    private static final String sVehicleName = FillItContract.VehicleEntry.TABLE_NAME + "." + FillItContract.VehicleEntry.COLUMN_NAME_NAME + " = ? ";
    private static final String sVehicleFuel = FillItContract.VehicleEntry.TABLE_NAME + "." + FillItContract.VehicleEntry.COLUMN_NAME_FUEL + " = ? ";
    private static final String sVehicleDataSync = FillItContract.VehicleEntry.TABLE_NAME + "." + FillItContract.VehicleEntry.COLUMN_NAME_DATASYNC + " = ? ";

    private static final String sFillId = FillItContract.FillEntry.TABLE_NAME + "." + FillItContract.FillEntry._ID + " = ? ";
    private static final String sFillGasstation = FillItContract.FillEntry.TABLE_NAME + "." + FillItContract.FillEntry.COLUMN_NAME_GASSTATION + " = ? ";
    private static final String sFillVehicle = FillItContract.FillEntry.TABLE_NAME + "." + FillItContract.FillEntry.COLUMN_NAME_VEHICLE + " = ? ";
    private static final String sFillFuel = FillItContract.FillEntry.TABLE_NAME + "." + FillItContract.FillEntry.COLUMN_NAME_FUEL + " = ? ";
    private static final String sFillDate = FillItContract.FillEntry.TABLE_NAME + "." + FillItContract.FillEntry.COLUMN_NAME_DATE + " = ? ";
    private static final String sFillPrice = FillItContract.FillEntry.TABLE_NAME + "." + FillItContract.FillEntry.COLUMN_NAME_PRICE + " = ? ";
    private static final String sFillLiters = FillItContract.FillEntry.TABLE_NAME + "." + FillItContract.FillEntry.COLUMN_NAME_LITERS + " = ? ";
    private static final String sFillLat = FillItContract.FillEntry.TABLE_NAME + "." + FillItContract.FillEntry.COLUMN_NAME_LAT + " = ? ";
    private static final String sFillLng = FillItContract.FillEntry.TABLE_NAME + "." + FillItContract.FillEntry.COLUMN_NAME_LNG + " = ? ";
    private static final String sFillDataSync = FillItContract.FillEntry.TABLE_NAME + "." + FillItContract.FillEntry.COLUMN_NAME_DATASYNC + " = ? ";

    private static final String sGasStationId = FillItContract.GasStationEntry.TABLE_NAME + "." + FillItContract.GasStationEntry._ID + " = ? ";
    private static final String sGasStationName = FillItContract.GasStationEntry.TABLE_NAME + "." + FillItContract.GasStationEntry.COLUMN_NAME_NAME + " = ? ";
    private static final String sGasStationLat = FillItContract.GasStationEntry.TABLE_NAME + "." + FillItContract.GasStationEntry.COLUMN_NAME_LAT + " = ? ";
    private static final String sGasStationLng = FillItContract.GasStationEntry.TABLE_NAME + "." + FillItContract.GasStationEntry.COLUMN_NAME_LNG + " = ? ";
    private static final String sGasStationFlag = FillItContract.GasStationEntry.TABLE_NAME + "." + FillItContract.GasStationEntry.COLUMN_NAME_FLAG + " = ? ";
    private static final String sGasStationAddress = FillItContract.GasStationEntry.TABLE_NAME + "." + FillItContract.GasStationEntry.COLUMN_NAME_ADDRESS + " = ? ";
    private static final String sGasStationDataSync = FillItContract.GasStationEntry.TABLE_NAME + "." + FillItContract.GasStationEntry.COLUMN_NAME_DATASYNC + " = ? ";

    private static final SQLiteQueryBuilder sFuelByVehicleByFill;
    private static final SQLiteQueryBuilder sFlagByGasStation;
    private static final SQLiteQueryBuilder sVehicleByFill;
    private static final SQLiteQueryBuilder sGasStationByFill;

    static {
        sFuelByVehicleByFill = new SQLiteQueryBuilder();
        sFuelByVehicleByFill.setTables(
                FillItContract.VehicleEntry.TABLE_NAME + " INNER JOIN " +
                        FillItContract.FuelEntry.TABLE_NAME + " ON " +
                        FillItContract.VehicleEntry.TABLE_NAME + "." + FillItContract.VehicleEntry.COLUMN_NAME_FUEL + " = " +
                        FillItContract.FuelEntry.TABLE_NAME + FillItContract.FuelEntry._ID
                        + ", " +
                        FillItContract.FillEntry.TABLE_NAME + " INNER JOIN " +
                        FillItContract.FuelEntry.TABLE_NAME + " ON " +
                        FillItContract.FillEntry.TABLE_NAME + "." + FillItContract.FillEntry.COLUMN_NAME_FUEL + " = " +
                        FillItContract.FuelEntry.TABLE_NAME + FillItContract.FuelEntry._ID
        );

        sFlagByGasStation = new SQLiteQueryBuilder();
        sFlagByGasStation.setTables(
                FillItContract.GasStationEntry.TABLE_NAME + " INNER JOIN " +
                        FillItContract.FlagEntry.TABLE_NAME + " ON " +
                        FillItContract.GasStationEntry.TABLE_NAME + "." + FillItContract.GasStationEntry.COLUMN_NAME_FLAG + " = " +
                        FillItContract.FlagEntry.TABLE_NAME + FillItContract.FlagEntry._ID
        );

        sVehicleByFill = new SQLiteQueryBuilder();
        sVehicleByFill.setTables(
                FillItContract.FillEntry.TABLE_NAME + " INNER JOIN " +
                        FillItContract.VehicleEntry.TABLE_NAME + " ON " +
                        FillItContract.FillEntry.TABLE_NAME + "." + FillItContract.FillEntry.COLUMN_NAME_VEHICLE + " = " +
                        FillItContract.VehicleEntry.TABLE_NAME + FillItContract.VehicleEntry._ID
        );

        sGasStationByFill = new SQLiteQueryBuilder();
        sGasStationByFill.setTables(
                FillItContract.FillEntry.TABLE_NAME + " INNER JOIN " +
                        FillItContract.GasStationEntry.TABLE_NAME + " ON " +
                        FillItContract.FillEntry.TABLE_NAME + "." + FillItContract.FillEntry.COLUMN_NAME_GASSTATION + " = " +
                        FillItContract.GasStationEntry.TABLE_NAME + FillItContract.GasStationEntry._ID
        );

    }

    private Cursor getFuelByVehicleByFill(Uri uri, String[] projection, String sortOrder) {
        String param = uri.getPathSegments().get(1);
        String selection = sFuelId;
        String[] selectionArgs = new String[]{param};
        return sFuelByVehicleByFill.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getFlagByGasStation(Uri uri, String[] projection, String sortOrder) {
        String param = uri.getPathSegments().get(1);
        String selection = sFlagId;
        String[] selectionArgs = new String[]{param};
        return sFlagByGasStation.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getVehicleByFill(Uri uri, String[] projection, String sortOrder) {
        String param = uri.getPathSegments().get(1);
        String selection = sVehicleId;
        String[] selectionArgs = new String[]{param};
        return sVehicleByFill.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getGasStationByFill(Uri uri, String[] projection, String sortOrder) {
        String param = uri.getPathSegments().get(1);
        String selection = sGasStationId;
        String[] selectionArgs = new String[]{param};
        return sGasStationByFill.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FillItContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, FillItContract.PATH_FUEL, FUEL);
        matcher.addURI(authority, FillItContract.PATH_FUEL + "/*", FUEL_WITH_NAME);
        matcher.addURI(authority, FillItContract.PATH_FUEL + "/#", FUEL_WITH_DATASYNC);

        matcher.addURI(authority, FillItContract.PATH_FLAG, FLAG);
        matcher.addURI(authority, FillItContract.PATH_FLAG + "/*", FLAG_WITH_NAME);
        matcher.addURI(authority, FillItContract.PATH_FLAG + "/*", FLAG_WITH_ICON);
        matcher.addURI(authority, FillItContract.PATH_FLAG + "/#", FLAG_WITH_DATASYNC);

        matcher.addURI(authority, FillItContract.PATH_VEHICLE, VEHICLE);
        matcher.addURI(authority, FillItContract.PATH_VEHICLE + "/*", VEHICLE_WITH_PHOTO);
        matcher.addURI(authority, FillItContract.PATH_VEHICLE + "/*", VEHICLE_WITH_NAME);
        matcher.addURI(authority, FillItContract.PATH_VEHICLE + "/#", VEHICLE_WITH_FUEL);
        matcher.addURI(authority, FillItContract.PATH_VEHICLE + "/#", VEHICLE_WITH_DATASYNC);

        matcher.addURI(authority, FillItContract.PATH_FILL, FILL);
        matcher.addURI(authority, FillItContract.PATH_FILL + "/#", FILL_WITH_GASSTATION);
        matcher.addURI(authority, FillItContract.PATH_FILL + "/#", FILL_WITH_VEHICLE);
        matcher.addURI(authority, FillItContract.PATH_FILL + "/#", FILL_WITH_FUEL);
        matcher.addURI(authority, FillItContract.PATH_FILL + "/#", FILL_WITH_DATE);
        matcher.addURI(authority, FillItContract.PATH_FILL + "/#", FILL_WITH_PRICE);
        matcher.addURI(authority, FillItContract.PATH_FILL + "/#", FILL_WITH_LITERS);
        matcher.addURI(authority, FillItContract.PATH_FILL + "/#", FILL_WITH_LAT);
        matcher.addURI(authority, FillItContract.PATH_FILL + "/#", FILL_WITH_LNG);
        matcher.addURI(authority, FillItContract.PATH_FILL + "/#", FILL_WITH_DATASYNC);

        matcher.addURI(authority, FillItContract.PATH_GASSTATION, GASSTATION);
        matcher.addURI(authority, FillItContract.PATH_GASSTATION + "/*", GASSTATION_WITH_NAME);
        matcher.addURI(authority, FillItContract.PATH_GASSTATION + "/#", GASSTATION_WITH_LAT);
        matcher.addURI(authority, FillItContract.PATH_GASSTATION + "/#", GASSTATION_WITH_LNG);
        matcher.addURI(authority, FillItContract.PATH_GASSTATION + "/#", GASSTATION_WITH_FLAG);
        matcher.addURI(authority, FillItContract.PATH_GASSTATION + "/*", GASSTATION_WITH_ADDRESS);
        matcher.addURI(authority, FillItContract.PATH_GASSTATION + "/#", GASSTATION_WITH_DATASYNC);

        return matcher;
    }


    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            // Fuel
            case FUEL_WITH_NAME:
                return FillItContract.FuelEntry.CONTENT_ITEM_TYPE;
            case FUEL_WITH_DATASYNC:
                return FillItContract.FuelEntry.CONTENT_ITEM_TYPE;
            // Flag
            case FLAG_WITH_NAME:
                return FillItContract.FlagEntry.CONTENT_ITEM_TYPE;
            case FLAG_WITH_ICON:
                return FillItContract.FlagEntry.CONTENT_ITEM_TYPE;
            case FLAG_WITH_DATASYNC:
                return FillItContract.FlagEntry.CONTENT_ITEM_TYPE;
            // Vehicle
            case VEHICLE_WITH_PHOTO:
                return FillItContract.VehicleEntry.CONTENT_ITEM_TYPE;
            case VEHICLE_WITH_NAME:
                return FillItContract.VehicleEntry.CONTENT_ITEM_TYPE;
            case VEHICLE_WITH_FUEL:
                return FillItContract.VehicleEntry.CONTENT_ITEM_TYPE;
            case VEHICLE_WITH_DATASYNC:
                return FillItContract.VehicleEntry.CONTENT_ITEM_TYPE;
            // Fill
            case FILL_WITH_GASSTATION:
                return FillItContract.FillEntry.CONTENT_ITEM_TYPE;
            case FILL_WITH_VEHICLE:
                return FillItContract.FillEntry.CONTENT_ITEM_TYPE;
            case FILL_WITH_FUEL:
                return FillItContract.FillEntry.CONTENT_ITEM_TYPE;
            case FILL_WITH_DATE:
                return FillItContract.FillEntry.CONTENT_ITEM_TYPE;
            case FILL_WITH_PRICE:
                return FillItContract.FillEntry.CONTENT_ITEM_TYPE;
            case FILL_WITH_LITERS:
                return FillItContract.FillEntry.CONTENT_ITEM_TYPE;
            case FILL_WITH_LAT:
                return FillItContract.FillEntry.CONTENT_ITEM_TYPE;
            case FILL_WITH_LNG:
                return FillItContract.FillEntry.CONTENT_ITEM_TYPE;
            case FILL_WITH_DATASYNC:
                return FillItContract.FillEntry.CONTENT_ITEM_TYPE;
            // GasStation
            case GASSTATION_WITH_NAME:
                return FillItContract.GasStationEntry.CONTENT_ITEM_TYPE;
            case GASSTATION_WITH_LAT:
                return FillItContract.GasStationEntry.CONTENT_ITEM_TYPE;
            case GASSTATION_WITH_LNG:
                return FillItContract.GasStationEntry.CONTENT_ITEM_TYPE;
            case GASSTATION_WITH_FLAG:
                return FillItContract.GasStationEntry.CONTENT_ITEM_TYPE;
            case GASSTATION_WITH_ADDRESS:
                return FillItContract.GasStationEntry.CONTENT_ITEM_TYPE;
            case GASSTATION_WITH_DATASYNC:
                return FillItContract.GasStationEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case FUEL: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FillItContract.FuelEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case FUEL_WITH_NAME: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FillItContract.FuelEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case FUEL_WITH_DATASYNC: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FillItContract.FuelEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case FLAG: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FillItContract.FlagEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case FLAG_WITH_NAME: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FillItContract.FlagEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case FLAG_WITH_ICON: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FillItContract.FlagEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case FLAG_WITH_DATASYNC: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FillItContract.FlagEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case VEHICLE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FillItContract.VehicleEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case VEHICLE_WITH_PHOTO: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FillItContract.VehicleEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case VEHICLE_WITH_NAME: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FillItContract.VehicleEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case VEHICLE_WITH_FUEL: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FillItContract.VehicleEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case VEHICLE_WITH_DATASYNC: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FillItContract.VehicleEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case FILL: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FillItContract.FillEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case FILL_WITH_GASSTATION: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FillItContract.FillEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case FILL_WITH_VEHICLE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FillItContract.FillEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case FILL_WITH_FUEL: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FillItContract.FillEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case FILL_WITH_DATE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FillItContract.FillEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case FILL_WITH_PRICE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FillItContract.FillEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case FILL_WITH_LITERS: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FillItContract.FillEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case FILL_WITH_LAT: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FillItContract.FillEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case FILL_WITH_LNG: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FillItContract.FillEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case FILL_WITH_DATASYNC: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FillItContract.FillEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case GASSTATION: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FillItContract.GasStationEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case GASSTATION_WITH_NAME: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FillItContract.GasStationEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case GASSTATION_WITH_LAT: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FillItContract.GasStationEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case GASSTATION_WITH_LNG: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FillItContract.GasStationEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case GASSTATION_WITH_FLAG: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FillItContract.GasStationEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case GASSTATION_WITH_ADDRESS: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FillItContract.GasStationEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case GASSTATION_WITH_DATASYNC: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FillItContract.GasStationEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case FUEL: {
                long _id = db.insert(FillItContract.FuelEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = FillItContract.FuelEntry.buildFuelUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case FLAG: {
                long _id = db.insert(FillItContract.FlagEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = FillItContract.FlagEntry.buildFlagUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case VEHICLE: {
                long _id = db.insert(FillItContract.VehicleEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = FillItContract.VehicleEntry.buildVehicleUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case FILL: {
                long _id = db.insert(FillItContract.FillEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = FillItContract.FillEntry.buildFillUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case GASSTATION: {
                long _id = db.insert(FillItContract.GasStationEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = FillItContract.GasStationEntry.buildGasStationUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        if (null == selection) selection = "1";
        switch (match) {
            case FUEL:
                rowsDeleted = db.delete(
                        FillItContract.FuelEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FLAG:
                rowsDeleted = db.delete(
                        FillItContract.FlagEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case VEHICLE:
                rowsDeleted = db.delete(
                        FillItContract.VehicleEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FILL:
                rowsDeleted = db.delete(
                        FillItContract.FillEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case GASSTATION:
                rowsDeleted = db.delete(
                        FillItContract.GasStationEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;
        switch (match) {
            case FUEL:
                rowsUpdated = db.update(FillItContract.FuelEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case FLAG:
                rowsUpdated = db.update(FillItContract.FlagEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case VEHICLE:
                rowsUpdated = db.update(FillItContract.VehicleEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case FILL:
                rowsUpdated = db.update(FillItContract.FillEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case GASSTATION:
                rowsUpdated = db.update(FillItContract.GasStationEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        int returnCount = 0;

        switch (match) {
            case FUEL:
                db.beginTransaction();

                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(FillItContract.FuelEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case FLAG:
                db.beginTransaction();

                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(FillItContract.FlagEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case VEHICLE:
                db.beginTransaction();

                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(FillItContract.VehicleEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case FILL:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(FillItContract.FillEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case GASSTATION:
                db.beginTransaction();

                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(FillItContract.GasStationEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new FillItDbHelper(getContext());
        return true;
    }

}





