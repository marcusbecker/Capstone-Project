package br.com.mvbos.fillit.util;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.com.mvbos.fillit.data.FillItContract;
import br.com.mvbos.fillit.model.FillModel;
import br.com.mvbos.fillit.model.FlagModel;
import br.com.mvbos.fillit.model.FuelModel;
import br.com.mvbos.fillit.model.GasStationModel;
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
*/
    public static FlagModel buildFlag(Cursor query) {
        FlagModel v = new FlagModel(0);
        if (query.moveToFirst()) {
            v.setId(query.getLong(query.getColumnIndex(FillItContract.FlagEntry._ID)));
            v.setName(query.getString(query.getColumnIndex(FillItContract.FlagEntry.COLUMN_NAME_NAME)));
            v.setIcon(query.getString(query.getColumnIndex(FillItContract.FlagEntry.COLUMN_NAME_ICON)));
            v.setDataSync(query.getLong(query.getColumnIndex(FillItContract.FlagEntry.COLUMN_NAME_DATASYNC)));

            query.close();
            return v;
        }

        return null;
    }

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

    public static FillModel[] buildFillList(Cursor query) {
        if (query == null || !query.moveToFirst()) {
            return new FillModel[0];
        }

        FillModel[] list = new FillModel[query.getCount()];

        int i = 0;
        do {
            FillModel model = new FillModel(0);

            model.setId(query.getLong(query.getColumnIndex(FillItContract.FillEntry._ID)));
            model.setGasStation(query.getLong(query.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_GASSTATION)));
            model.setVehicle(query.getLong(query.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_VEHICLE)));
            model.setFuel(query.getLong(query.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_FUEL)));
            model.setDate(query.getLong(query.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_DATE)));
            model.setPrice(query.getDouble(query.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_PRICE)));
            model.setLiters(query.getInt(query.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_LITERS)));
            model.setLat(query.getDouble(query.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_LAT)));
            model.setLng(query.getDouble(query.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_LNG)));

            final int dataSyncIndex = query.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_DATASYNC);
            if (dataSyncIndex > -1) {
                model.setDataSync(query.getLong(dataSyncIndex));
            }

            final int gasStationIndex = query.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_NAME);
            final int photoIndex = query.getColumnIndex(FillItContract.VehicleEntry.COLUMN_NAME_PHOTO);
            final int vehicleIndex = query.getColumnIndex(FillItContract.VehicleEntry.COLUMN_NAME_NAME);
            final int fuelIndex = query.getColumnIndex(FillItContract.FuelEntry.COLUMN_NAME_NAME);

            if (gasStationIndex > -1) {
                final String gasStation = query.getString(gasStationIndex);
                model.setGasStationName(gasStation);
            }

            if (photoIndex > -1) {
                final String photo = query.getString(photoIndex);
                model.setVehicleIcon(photo);
            }

            if (vehicleIndex > -1) {
                final String vehicle = query.getString(vehicleIndex);
                model.setVehicleName(vehicle);
            }

            if (fuelIndex > -1) {
                final String fuel = query.getString(fuelIndex);
                model.setFuelName(fuel);
            }

            list[i] = model;
            i++;

        } while (query.moveToNext());

        query.close();
        return list;
    }

    public static VehicleModel[] buildVehicleList(Cursor cursor) {
        if (cursor == null || !cursor.moveToFirst()) {
            return new VehicleModel[0];
        }

        VehicleModel[] list = new VehicleModel[cursor.getCount()];

        short i = 0;
        do {
            VehicleModel v = new VehicleModel(0);
            v.setId(cursor.getLong(cursor.getColumnIndex(FillItContract.VehicleEntry._ID)));
            v.setPhoto(cursor.getString(cursor.getColumnIndex(FillItContract.VehicleEntry.COLUMN_NAME_PHOTO)));
            v.setName(cursor.getString(cursor.getColumnIndex(FillItContract.VehicleEntry.COLUMN_NAME_NAME)));
            v.setFuel(cursor.getLong(cursor.getColumnIndex(FillItContract.VehicleEntry.COLUMN_NAME_FUEL)));
            v.setDataSync(cursor.getLong(cursor.getColumnIndex(FillItContract.VehicleEntry.COLUMN_NAME_DATASYNC)));

            list[i++] = v;
        } while (cursor.moveToNext());

        cursor.close();
        return list;
    }

    public static FlagModel[] buildFlagList(Cursor cursor) {
        if (cursor == null || !cursor.moveToFirst()) {
            return new FlagModel[0];
        }

        FlagModel[] list = new FlagModel[cursor.getCount()];

        short i = 0;
        do {
            FlagModel f = new FlagModel(0);
            f.setId(cursor.getLong(cursor.getColumnIndex(FillItContract.FlagEntry._ID)));
            f.setName(cursor.getString(cursor.getColumnIndex(FillItContract.FlagEntry.COLUMN_NAME_NAME)));
            f.setIcon(cursor.getString(cursor.getColumnIndex(FillItContract.FlagEntry.COLUMN_NAME_ICON)));
            f.setDataSync(cursor.getLong(cursor.getColumnIndex(FillItContract.FlagEntry.COLUMN_NAME_DATASYNC)));

            list[i++] = f;
        } while (cursor.moveToNext());

        cursor.close();
        return list;
    }

    public static FuelModel[] buildFuelList(Cursor cursor) {
        if (cursor == null || !cursor.moveToFirst()) {
            return new FuelModel[0];
        }

        FuelModel[] list = new FuelModel[cursor.getCount()];

        short i = 0;
        do {
            FuelModel v = new FuelModel();
            v.setId(cursor.getLong(cursor.getColumnIndex(FillItContract.FuelEntry._ID)));
            v.setName(cursor.getString(cursor.getColumnIndex(FillItContract.FuelEntry.COLUMN_NAME_NAME)));
            v.setDataSync(cursor.getLong(cursor.getColumnIndex(FillItContract.FuelEntry.COLUMN_NAME_DATASYNC)));

            list[i++] = v;
        } while (cursor.moveToNext());

        cursor.close();
        return list;
    }

    public static FillModel buildFill(Cursor query) {
        FillModel v = new FillModel(0);
        if (query.moveToFirst()) {
            v.setId(query.getLong(query.getColumnIndex(FillItContract.FillEntry._ID)));
            v.setGasStation(query.getLong(query.getColumnIndex(FillItContract.FillEntry.COLUMN_NAME_GASSTATION)));
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


    public static GasStationModel[] buildGasStationArray(Cursor cursor) {
        if (cursor == null || !cursor.moveToFirst()) {
            return new GasStationModel[0];
        }

        GasStationModel[] list = new GasStationModel[cursor.getCount()];

        short i = 0;
        int flagNameIndex = cursor.getColumnIndex(FillItContract.FlagEntry.COLUMN_NAME_NAME);
        int flagIconIndex = cursor.getColumnIndex(FillItContract.FlagEntry.COLUMN_NAME_ICON);

        do {
            GasStationModel g = new GasStationModel(0);
            g.setId(cursor.getLong(cursor.getColumnIndex(FillItContract.GasStationEntry._ID)));
            g.setName(cursor.getString(cursor.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_NAME)));
            g.setLat(cursor.getDouble(cursor.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_LAT)));
            g.setLng(cursor.getDouble(cursor.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_LNG)));
            g.setFlag(cursor.getInt(cursor.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_FLAG)));
            g.setAddress(cursor.getString(cursor.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_ADDRESS)));
            g.setDataSync(cursor.getLong(cursor.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_DATASYNC)));

            if (flagNameIndex > -1) {
                g.setFlagName(cursor.getString(cursor.getColumnIndex(FillItContract.FlagEntry.COLUMN_NAME_NAME)));
            }

            if (flagIconIndex > -1) {
                g.setFlagIcon(cursor.getString(cursor.getColumnIndex(FillItContract.FlagEntry.COLUMN_NAME_ICON)));
            }

            list[i++] = g;
        } while (cursor.moveToNext());

        cursor.close();
        return list;
    }

    public static List<GasStationModel> buildGasStationList(Cursor cursor) {
        if (cursor == null || !cursor.moveToFirst()) {
            return new ArrayList<>(5);
        }

        List<GasStationModel> list = new ArrayList<>(cursor.getCount() + 5);

        int flagNameIndex = cursor.getColumnIndex(FillItContract.FlagEntry.COLUMN_NAME_NAME);
        int flagIconIndex = cursor.getColumnIndex(FillItContract.FlagEntry.COLUMN_NAME_ICON);

        do {
            GasStationModel g = new GasStationModel(0);
            g.setId(cursor.getLong(cursor.getColumnIndex(FillItContract.GasStationEntry._ID)));
            g.setName(cursor.getString(cursor.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_NAME)));
            g.setLat(cursor.getDouble(cursor.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_LAT)));
            g.setLng(cursor.getDouble(cursor.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_LNG)));
            g.setFlag(cursor.getInt(cursor.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_FLAG)));
            g.setAddress(cursor.getString(cursor.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_ADDRESS)));
            g.setDataSync(cursor.getLong(cursor.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_DATASYNC)));

            if (flagNameIndex > -1) {
                g.setFlagName(cursor.getString(cursor.getColumnIndex(FillItContract.FlagEntry.COLUMN_NAME_NAME)));
            }

            if (flagIconIndex > -1) {
                g.setFlagIcon(cursor.getString(cursor.getColumnIndex(FillItContract.FlagEntry.COLUMN_NAME_ICON)));
            }

            list.add(g);
        } while (cursor.moveToNext());

        cursor.close();
        return list;
    }

    public static GasStationModel buildGasStation(Cursor query) {
        GasStationModel v = new GasStationModel(0);

        if (query.moveToFirst()) {
            v.setId(query.getLong(query.getColumnIndex(FillItContract.GasStationEntry._ID)));
            v.setName(query.getString(query.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_NAME)));
            v.setLat(query.getDouble(query.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_LAT)));
            v.setLng(query.getDouble(query.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_LNG)));
            v.setFlag(query.getInt(query.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_FLAG)));
            v.setAddress(query.getString(query.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_ADDRESS)));
            v.setDataSync(query.getLong(query.getColumnIndex(FillItContract.GasStationEntry.COLUMN_NAME_DATASYNC)));

            int temp = query.getColumnIndex(FillItContract.FlagEntry.COLUMN_NAME_NAME);
            if (temp > -1) {
                v.setFlagName(query.getString(query.getColumnIndex(FillItContract.FlagEntry.COLUMN_NAME_NAME)));
            }

            temp = query.getColumnIndex(FillItContract.FlagEntry.COLUMN_NAME_ICON);
            if (temp > -1) {
                v.setFlagIcon(query.getString(query.getColumnIndex(FillItContract.FlagEntry.COLUMN_NAME_ICON)));
            }

            query.close();
            return v;
        }

        return null;
    }

}



