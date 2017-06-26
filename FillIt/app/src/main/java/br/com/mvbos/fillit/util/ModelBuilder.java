package br.com.mvbos.fillit.util;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.com.mvbos.fillit.data.FillItContract;
import br.com.mvbos.fillit.model.FillModel;
import br.com.mvbos.fillit.model.FuelModel;
import br.com.mvbos.fillit.model.VehicleModel;

/**
 * Created by Marcus Becker on 18/06/2017.
 */

public class ModelBuilder {
    /*
    public static FuelModel buildFuel(Cursor query) {
        FuelModel v = new FuelModel(0);
        if (query.moveToFirst()) {
            v.setId(query.getLong(query.getColumnIndex(FillItContract.FuelEntry._ID)));
            v.setName(query.getString(query.getColumnIndex(FillItContract.FuelEntry.COLUMN_NAME_NAME)));
            v.setDataSync(query.getDate(query.getColumnIndex(FillItContract.FuelEntry.COLUMN_NAME_DATASYNC)));

            query.close();
            return v;
        }

        return null;
    }

    public static FlagModel buildFlag(Cursor query) {
        FlagModel v = new FlagModel(0);
        if (query.moveToFirst()) {
            v.setId(query.getLong(query.getColumnIndex(FillItContract.FlagEntry._ID)));
            v.setName(query.getString(query.getColumnIndex(FillItContract.FlagEntry.COLUMN_NAME_NAME)));
            v.setIcon(query.getString(query.getColumnIndex(FillItContract.FlagEntry.COLUMN_NAME_ICON)));
            v.setDataSync(query.getDate(query.getColumnIndex(FillItContract.FlagEntry.COLUMN_NAME_DATASYNC)));

            query.close();
            return v;
        }

        return null;
    }
*/
    public static VehicleModel buildVehicle(Cursor query) {
        VehicleModel v = null;
        if (query.moveToFirst()) {
            v = new VehicleModel(0);
            v.setId(query.getLong(query.getColumnIndex(FillItContract.VehicleEntry._ID)));
            v.setPhoto(query.getString(query.getColumnIndex(FillItContract.VehicleEntry.COLUMN_NAME_PHOTO)));
            v.setName(query.getString(query.getColumnIndex(FillItContract.VehicleEntry.COLUMN_NAME_NAME)));
            v.setFuel(query.getLong(query.getColumnIndex(FillItContract.VehicleEntry.COLUMN_NAME_FUEL)));
            v.setDataSync(query.getLong(query.getColumnIndex(FillItContract.VehicleEntry.COLUMN_NAME_DATASYNC)));

            query.close();
        }

        return v;
    }

    public static List<FuelModel> buildFuelList(Cursor cursor) {
        List<FuelModel> list = new ArrayList<>(cursor.getColumnCount());
        while (cursor.moveToNext()) {
            FuelModel v = new FuelModel();
            v.setId(cursor.getLong(cursor.getColumnIndex(FillItContract.FuelEntry._ID)));
            v.setName(cursor.getString(cursor.getColumnIndex(FillItContract.FuelEntry.COLUMN_NAME_NAME)));
            v.setDataSync(cursor.getLong(cursor.getColumnIndex(FillItContract.FuelEntry.COLUMN_NAME_DATASYNC)));

            list.add(v);
        }

        cursor.close();
        return list;
    }

    public static FillModel buildFill(Cursor query) {
        FillModel v = new FillModel(0);
        if (query.moveToFirst()) {
            v.setId(query.getLong(query.getColumnIndex(FillItContract.FillEntry._ID)));
            v.setGasstation(query.getLong(query.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_GASSTATION)));
            v.setVehicle(query.getLong(query.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_VEHICLE)));
            v.setFuel(query.getLong(query.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_FUEL)));
            v.setDate(query.getLong(query.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_DATE)));
            v.setPrice(query.getDouble(query.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_PRICE)));
            v.setLiters(query.getInt(query.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_LITERS)));
            v.setLat(query.getDouble(query.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_LAT)));
            v.setLng(query.getDouble(query.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_LNG)));
            v.setDataSync(query.getLong(query.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_DATASYNC)));

            query.close();
            return v;
        }

        return null;
    }

    /*
    public static GasStationModel buildGasStation(Cursor query) {
        GasStationModel v = new GasStationModel(0);
        if (query.moveToFirst()) {
            v.setId(query.getLong(query.getColumnIndex(FillItContract.GasStationEntry._ID)));
            v.setName(query.getString(query.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_NAME)));
            v.setLat(query.getDouble(query.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_LAT)));
            v.setLng(query.getDouble(query.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_LNG)));
            v.setFlag(query.getInt(query.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_FLAG)));
            v.setAddress(query.getString(query.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_ADDRESS)));
            v.setDataSync(query.getDate(query.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_DATASYNC)));

            query.close();
            return v;
        }

        return null;
    }
*/
}



