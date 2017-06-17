package br.com.mvbos.fillit.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Marcus Becker on 17/06/2017.
 */

public class VehicleModel implements Parcelable {
    private long id;
    private String photo;
    private String name;
    private long fuel;
    private long dataSync;

    public VehicleModel(long id) {
        this.id = id;
    }

    public VehicleModel(long id, String photo, String name, long fuel, long dataSync) {
        this.id = id;
        this.photo = photo;
        this.name = name;
        this.fuel = fuel;
        this.dataSync = dataSync;
    }

    public VehicleModel(Parcel in) {
        this.id = in.readLong();
        this.photo = in.readString();
        this.name = in.readString();
        this.fuel = in.readLong();
        this.dataSync = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.photo);
        dest.writeString(this.name);
        dest.writeLong(this.fuel);
        dest.writeLong(this.dataSync);
    }

    public static final Parcelable.Creator<VehicleModel> CREATOR = new Parcelable.Creator<VehicleModel>() {
        public VehicleModel createFromParcel(Parcel in) {
            return new VehicleModel(in);
        }

        public VehicleModel[] newArray(int size) {
            return new VehicleModel[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getFuel() {
        return fuel;
    }

    public void setFuel(long fuel) {
        this.fuel = fuel;
    }

    public long getDataSync() {
        return dataSync;
    }

    public void setDataSync(long dataSync) {
        this.dataSync = dataSync;
    }

    @Override
    public String toString() {
        return "VehicleModel{" +
                "id=" + id +
                ", photo='" + photo + '\'' +
                ", name='" + name + '\'' +
                ", fuel=" + fuel +
                ", dataSync=" + dataSync +
                '}';
    }

    public boolean hasPhoto() {
        return photo != null && !photo.isEmpty();
    }
}
