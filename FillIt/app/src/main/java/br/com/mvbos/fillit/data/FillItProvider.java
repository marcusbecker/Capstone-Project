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
    private static final int FUEL = 100;
    private static final int FUEL_WITH_ID = 101;
    private static final int FUEL_WITH_NAME = 102;
    private static final int FUEL_WITH_DATASYNC = 103;

    private static final int FLAG = 200;
    private static final int FLAG_WITH_ID = 201;
    private static final int FLAG_WITH_NAME = 202;
    private static final int FLAG_WITH_ICON = 203;
    private static final int FLAG_WITH_DATASYNC = 204;

    private static final int VEHICLE = 300;
    private static final int VEHICLE_WITH_ID = 301;
    private static final int VEHICLE_WITH_PHOTO = 302;
    private static final int VEHICLE_WITH_NAME = 303;
    private static final int VEHICLE_WITH_FUEL = 304;
    private static final int VEHICLE_WITH_DATASYNC = 305;
    private static final int VEHICLE_JOIN_FUEL = 306;

    private static final int FILL = 400;
    private static final int FILL_WITH_ID = 401;
    private static final int FILL_WITH_GASSTATION = 402;
    private static final int FILL_WITH_VEHICLE = 403;
    private static final int FILL_WITH_FUEL = 404;
    private static final int FILL_WITH_DATE = 405;
    private static final int FILL_WITH_PRICE = 406;
    private static final int FILL_WITH_LITERS = 407;
    private static final int FILL_WITH_LAT = 408;
    private static final int FILL_WITH_LNG = 409;
    private static final int FILL_WITH_DATASYNC = 410;
    private static final int FILL_JOIN_GASSTATION_JOIN_VEHICLE_JOIN_FUEL = 411;

    private static final int GASSTATION = 500;
    private static final int GASSTATION_WITH_ID = 501;
    private static final int GASSTATION_WITH_NAME = 502;
    private static final int GASSTATION_WITH_LAT = 503;
    private static final int GASSTATION_WITH_LNG = 504;
    private static final int GASSTATION_WITH_FLAG = 505;
    private static final int GASSTATION_WITH_ADDRESS = 506;
    private static final int GASSTATION_WITH_DATASYNC = 507;
    private static final int GASSTATION_JOIN_FLAG = 508;

    private static final String sFuelId = FillItContract.FuelEntry.TABLE_NAME + "." + FillItContract.FuelEntry._ID + " = ? ";

    private static final String sFlagId = FillItContract.FlagEntry.TABLE_NAME + "." + FillItContract.FlagEntry._ID + " = ? ";

    private static final String sVehicleId = FillItContract.VehicleEntry.TABLE_NAME + "." + FillItContract.VehicleEntry._ID + " = ? ";

    private static final String sFillId = FillItContract.FillEntry.TABLE_NAME + "." + FillItContract.FillEntry._ID + " = ? ";

    private static final String sGasStationId = FillItContract.GasStationEntry.TABLE_NAME + "." + FillItContract.GasStationEntry._ID + " = ? ";

    private static final SQLiteQueryBuilder sVehicleJoinFuel;
    private static final SQLiteQueryBuilder sFillJoinGasStationJoinVehicleJoinFuel;
    private static final SQLiteQueryBuilder sGasStationJoinFlag;

    static {
        sVehicleJoinFuel = new SQLiteQueryBuilder();
        sVehicleJoinFuel.setTables(
                FillItContract.VehicleEntry.TABLE_NAME + " LEFT JOIN " +
                        FillItContract.FuelEntry.TABLE_NAME + " ON " +
                        FillItContract.VehicleEntry.TABLE_NAME + "." + FillItContract.VehicleEntry.COLUMN_NAME_FUEL + " = " +
                        FillItContract.FuelEntry.TABLE_NAME + "." + FillItContract.FuelEntry._ID
        );

        sFillJoinGasStationJoinVehicleJoinFuel = new SQLiteQueryBuilder();
        sFillJoinGasStationJoinVehicleJoinFuel.setTables(
                FillItContract.FillEntry.TABLE_NAME + " LEFT JOIN " +
                        FillItContract.GasStationEntry.TABLE_NAME + " ON " +
                        FillItContract.FillEntry.TABLE_NAME + "." + FillItContract.FillEntry.COLUMN_NAME_GASSTATION + " = " +
                        FillItContract.GasStationEntry.TABLE_NAME + "." + FillItContract.GasStationEntry._ID
                        + " LEFT JOIN " +
                        FillItContract.VehicleEntry.TABLE_NAME + " ON " +
                        FillItContract.FillEntry.TABLE_NAME + "." + FillItContract.FillEntry.COLUMN_NAME_VEHICLE + " = " +
                        FillItContract.VehicleEntry.TABLE_NAME + "." + FillItContract.VehicleEntry._ID
                        + " LEFT JOIN " +
                        FillItContract.FuelEntry.TABLE_NAME + " ON " +
                        FillItContract.FillEntry.TABLE_NAME + "." + FillItContract.FillEntry.COLUMN_NAME_FUEL + " = " +
                        FillItContract.FuelEntry.TABLE_NAME + "." + FillItContract.FuelEntry._ID
        );

        sGasStationJoinFlag = new SQLiteQueryBuilder();
        sGasStationJoinFlag.setTables(
                FillItContract.GasStationEntry.TABLE_NAME + " LEFT JOIN " +
                        FillItContract.FlagEntry.TABLE_NAME + " ON " +
                        FillItContract.GasStationEntry.TABLE_NAME + "." + FillItContract.GasStationEntry.COLUMN_NAME_FLAG + " = " +
                        FillItContract.FlagEntry.TABLE_NAME + "." + FillItContract.FlagEntry._ID
        );

    }

    private Cursor getVehicleJoinFuel(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return sVehicleJoinFuel.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getFillJoinGasStationJoinVehicleJoinFuel(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return sFillJoinGasStationJoinVehicleJoinFuel.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getGasStationJoinFlag(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return sGasStationJoinFlag.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FillItContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, FillItContract.PATH_FUEL, FUEL);
        matcher.addURI(authority, FillItContract.PATH_FUEL + "/#", FUEL_WITH_ID);
        matcher.addURI(authority, FillItContract.PATH_FUEL + "/name/*", FUEL_WITH_NAME);
        matcher.addURI(authority, FillItContract.PATH_FUEL + "/datasync/#", FUEL_WITH_DATASYNC);

        matcher.addURI(authority, FillItContract.PATH_FLAG, FLAG);
        matcher.addURI(authority, FillItContract.PATH_FLAG + "/#", FLAG_WITH_ID);
        matcher.addURI(authority, FillItContract.PATH_FLAG + "/name/*", FLAG_WITH_NAME);
        matcher.addURI(authority, FillItContract.PATH_FLAG + "/icon/*", FLAG_WITH_ICON);
        matcher.addURI(authority, FillItContract.PATH_FLAG + "/datasync/#", FLAG_WITH_DATASYNC);

        matcher.addURI(authority, FillItContract.PATH_VEHICLE, VEHICLE);
        matcher.addURI(authority, FillItContract.PATH_VEHICLE + "/#", VEHICLE_WITH_ID);
        matcher.addURI(authority, FillItContract.PATH_VEHICLE + "/photo/*", VEHICLE_WITH_PHOTO);
        matcher.addURI(authority, FillItContract.PATH_VEHICLE + "/name/*", VEHICLE_WITH_NAME);
        matcher.addURI(authority, FillItContract.PATH_VEHICLE + "/fuel/#", VEHICLE_WITH_FUEL);
        matcher.addURI(authority, FillItContract.PATH_VEHICLE + "/datasync/#", VEHICLE_WITH_DATASYNC);
        matcher.addURI(authority, FillItContract.PATH_VEHICLE_JOIN_FUEL + "/", VEHICLE_JOIN_FUEL);

        matcher.addURI(authority, FillItContract.PATH_FILL, FILL);
        matcher.addURI(authority, FillItContract.PATH_FILL + "/#", FILL_WITH_ID);
        matcher.addURI(authority, FillItContract.PATH_FILL + "/gasstation/#", FILL_WITH_GASSTATION);
        matcher.addURI(authority, FillItContract.PATH_FILL + "/vehicle/#", FILL_WITH_VEHICLE);
        matcher.addURI(authority, FillItContract.PATH_FILL + "/fuel/#", FILL_WITH_FUEL);
        matcher.addURI(authority, FillItContract.PATH_FILL + "/date/#", FILL_WITH_DATE);
        matcher.addURI(authority, FillItContract.PATH_FILL + "/price/#", FILL_WITH_PRICE);
        matcher.addURI(authority, FillItContract.PATH_FILL + "/liters/#", FILL_WITH_LITERS);
        matcher.addURI(authority, FillItContract.PATH_FILL + "/lat/#", FILL_WITH_LAT);
        matcher.addURI(authority, FillItContract.PATH_FILL + "/lng/#", FILL_WITH_LNG);
        matcher.addURI(authority, FillItContract.PATH_FILL + "/datasync/#", FILL_WITH_DATASYNC);
        matcher.addURI(authority, FillItContract.PATH_FILL_JOIN_GASSTATION_JOIN_VEHICLE_JOIN_FUEL + "/", FILL_JOIN_GASSTATION_JOIN_VEHICLE_JOIN_FUEL);

        matcher.addURI(authority, FillItContract.PATH_GASSTATION, GASSTATION);
        matcher.addURI(authority, FillItContract.PATH_GASSTATION + "/#", GASSTATION_WITH_ID);
        matcher.addURI(authority, FillItContract.PATH_GASSTATION + "/name/*", GASSTATION_WITH_NAME);
        matcher.addURI(authority, FillItContract.PATH_GASSTATION + "/lat/#", GASSTATION_WITH_LAT);
        matcher.addURI(authority, FillItContract.PATH_GASSTATION + "/lng/#", GASSTATION_WITH_LNG);
        matcher.addURI(authority, FillItContract.PATH_GASSTATION + "/flag/#", GASSTATION_WITH_FLAG);
        matcher.addURI(authority, FillItContract.PATH_GASSTATION + "/address/*", GASSTATION_WITH_ADDRESS);
        matcher.addURI(authority, FillItContract.PATH_GASSTATION + "/datasync/#", GASSTATION_WITH_DATASYNC);
        matcher.addURI(authority, FillItContract.PATH_GASSTATION_JOIN_FLAG + "/", GASSTATION_JOIN_FLAG);

        return matcher;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            // Fuel
            case FUEL:
                return FillItContract.FuelEntry.CONTENT_TYPE;
            case FUEL_WITH_ID:
                return FillItContract.FuelEntry.CONTENT_ITEM_TYPE;
            case FUEL_WITH_NAME:
                return FillItContract.FuelEntry.CONTENT_ITEM_TYPE;
            case FUEL_WITH_DATASYNC:
                return FillItContract.FuelEntry.CONTENT_ITEM_TYPE;

            // FlagModel
            case FLAG:
                return FillItContract.FlagEntry.CONTENT_TYPE;
            case FLAG_WITH_ID:
                return FillItContract.FlagEntry.CONTENT_ITEM_TYPE;
            case FLAG_WITH_NAME:
                return FillItContract.FlagEntry.CONTENT_ITEM_TYPE;
            case FLAG_WITH_ICON:
                return FillItContract.FlagEntry.CONTENT_ITEM_TYPE;
            case FLAG_WITH_DATASYNC:
                return FillItContract.FlagEntry.CONTENT_ITEM_TYPE;

            // Vehicle
            case VEHICLE:
                return FillItContract.VehicleEntry.CONTENT_TYPE;
            case VEHICLE_WITH_ID:
                return FillItContract.VehicleEntry.CONTENT_ITEM_TYPE;
            case VEHICLE_WITH_PHOTO:
                return FillItContract.VehicleEntry.CONTENT_ITEM_TYPE;
            case VEHICLE_WITH_NAME:
                return FillItContract.VehicleEntry.CONTENT_ITEM_TYPE;
            case VEHICLE_WITH_FUEL:
                return FillItContract.VehicleEntry.CONTENT_ITEM_TYPE;
            case VEHICLE_WITH_DATASYNC:
                return FillItContract.VehicleEntry.CONTENT_ITEM_TYPE;
            case VEHICLE_JOIN_FUEL:
                return FillItContract.VehicleEntry.CONTENT_TYPE;

            // Fill
            case FILL:
                return FillItContract.FillEntry.CONTENT_TYPE;
            case FILL_WITH_ID:
                return FillItContract.FillEntry.CONTENT_ITEM_TYPE;
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
            case FILL_JOIN_GASSTATION_JOIN_VEHICLE_JOIN_FUEL:
                return FillItContract.FillEntry.CONTENT_TYPE;

            // GasStationModel
            case GASSTATION:
                return FillItContract.GasStationEntry.CONTENT_TYPE;
            case GASSTATION_WITH_ID:
                return FillItContract.GasStationEntry.CONTENT_ITEM_TYPE;
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
            case GASSTATION_JOIN_FLAG:
                return FillItContract.GasStationEntry.CONTENT_TYPE;

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

            case FUEL_WITH_ID: {
                String[] mSelectionArgs = {uri.getPathSegments().get(1)};
                retCursor = mOpenHelper.getReadableDatabase().query(FillItContract.FuelEntry.TABLE_NAME, projection, sFuelId, mSelectionArgs, null, null, null);
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

            case FLAG_WITH_ID: {
                String[] mSelectionArgs = {uri.getPathSegments().get(1)};
                retCursor = mOpenHelper.getReadableDatabase().query(FillItContract.FlagEntry.TABLE_NAME, projection, sFlagId, mSelectionArgs, null, null, null);
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

            case VEHICLE_WITH_ID: {
                String[] mSelectionArgs = {uri.getPathSegments().get(1)};
                retCursor = mOpenHelper.getReadableDatabase().query(FillItContract.VehicleEntry.TABLE_NAME, projection, sVehicleId, mSelectionArgs, null, null, null);
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

            case VEHICLE_JOIN_FUEL: {
                retCursor = getVehicleJoinFuel(
                        uri,
                        projection,
                        selection,
                        selectionArgs,
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

            case FILL_WITH_ID: {
                String[] mSelectionArgs = {uri.getPathSegments().get(1)};
                retCursor = mOpenHelper.getReadableDatabase().query(FillItContract.FillEntry.TABLE_NAME, projection, sFillId, mSelectionArgs, null, null, null);
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

            case FILL_JOIN_GASSTATION_JOIN_VEHICLE_JOIN_FUEL: {
                retCursor = getFillJoinGasStationJoinVehicleJoinFuel(
                        uri,
                        projection,
                        selection,
                        selectionArgs,
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

            case GASSTATION_WITH_ID: {
                String[] mSelectionArgs = {uri.getPathSegments().get(1)};
                retCursor = mOpenHelper.getReadableDatabase().query(FillItContract.GasStationEntry.TABLE_NAME, projection, sGasStationId, mSelectionArgs, null, null, null);
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

            case GASSTATION_JOIN_FLAG: {
                retCursor = getGasStationJoinFlag(
                        uri,
                        projection,
                        selection,
                        selectionArgs,
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
                db.execSQL("DELETE FROM " + FillItContract.FuelEntry.TABLE_NAME);
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

