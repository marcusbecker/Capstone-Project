package br.com.mvbos.fillit.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Marcus Becker on 30/06/2017.
 */

public class GasStationModel implements Parcelable {
    private long id;
    private String name;
    private double lat;
    private double lng;
    private int flag;
    private String address;
    private long dataSync;

    public GasStationModel(long id, String name, double lat, double lng, int flag, String address, long dataSync) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.flag = flag;
        this.address = address;
        this.dataSync = dataSync;
    }

    public GasStationModel(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.lat = in.readDouble();
        this.lng = in.readDouble();
        this.flag = in.readInt();
        this.address = in.readString();
        this.dataSync = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
        dest.writeInt(this.flag);
        dest.writeString(this.address);
        dest.writeLong(this.dataSync);
    }

    public static final Parcelable.Creator<GasStationModel> CREATOR = new Parcelable.Creator<GasStationModel>() {
        public GasStationModel createFromParcel(Parcel in) {
            return new GasStationModel(in);
        }

        public GasStationModel[] newArray(int size) {
            return new GasStationModel[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

        GasStationModel that = (GasStationModel) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "GasStationModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", flag=" + flag +
                ", address='" + address + '\'' +
                ", dataSync=" + dataSync +
                '}';
    }

    public boolean hasLatLng() {
        return lat != 0 && lng != 0;
    }
}