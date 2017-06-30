package br.com.mvbos.fillit.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Marcus Becker on 26/06/2017.
 */

public class FillModel implements Parcelable {

    private long id;
    private long gasStation;
    private long vehicle;
    private long fuel;
    private long date;
    private double price;
    private int liters;
    private double lat;
    private double lng;
    private long dataSync;

    public FillModel(long id) {
        this.id = id;
    }

    public FillModel(long id, long gasStation, long vehicle, long fuel, long date, double price, int liters, double lat, double lng, long dataSync) {
        this.id = id;
        this.gasStation = gasStation;
        this.vehicle = vehicle;
        this.fuel = fuel;
        this.date = date;
        this.price = price;
        this.liters = liters;
        this.lat = lat;
        this.lng = lng;
        this.dataSync = dataSync;
    }

    public FillModel(Parcel in) {
        this.id = in.readLong();
        this.gasStation = in.readLong();
        this.vehicle = in.readLong();
        this.fuel = in.readLong();
        this.date = in.readLong();
        this.price = in.readDouble();
        this.liters = in.readInt();
        this.lat = in.readDouble();
        this.lng = in.readDouble();
        this.dataSync = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.gasStation);
        dest.writeLong(this.vehicle);
        dest.writeLong(this.fuel);
        dest.writeLong(this.date);
        dest.writeDouble(this.price);
        dest.writeInt(this.liters);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
        dest.writeLong(this.dataSync);
    }

    public static final Parcelable.Creator<FillModel> CREATOR = new Parcelable.Creator<FillModel>() {
        public FillModel createFromParcel(Parcel in) {
            return new FillModel(in);
        }

        public FillModel[] newArray(int size) {
            return new FillModel[size];
        }
    };


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getGasStation() {
        return gasStation;
    }

    public void setGasStation(long gasStation) {
        this.gasStation = gasStation;
    }

    public long getVehicle() {
        return vehicle;
    }

    public void setVehicle(long vehicle) {
        this.vehicle = vehicle;
    }

    public long getFuel() {
        return fuel;
    }

    public void setFuel(long fuel) {
        this.fuel = fuel;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getLiters() {
        return liters;
    }

    public void setLiters(int liters) {
        this.liters = liters;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public long getDataSync() {
        return dataSync;
    }

    public void setDataSync(long dataSync) {
        this.dataSync = dataSync;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FillModel fillModel = (FillModel) o;

        return id == fillModel.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "FillModel{" +
                "id=" + id +
                ", gasStation=" + gasStation +
                ", vehicle=" + vehicle +
                ", fuel=" + fuel +
                ", date=" + date +
                ", price=" + price +
                ", liters=" + liters +
                ", lat=" + lat +
                ", lng=" + lng +
                ", dataSync=" + dataSync +
                '}';
    }
}