package br.com.mvbos.fillit.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Marcus Becker on 18/06/2017.
 */

public class FuelModel implements Parcelable {
    private long id;
    private String name;
    private long dataSync;

    public FuelModel() {
    }

    public FuelModel(long id, String name, long dataSync) {
        this.id = id;
        this.name = name;
        this.dataSync = dataSync;
    }

    public FuelModel(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
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
        dest.writeLong(this.dataSync);
    }

    public static final Parcelable.Creator<FuelModel> CREATOR = new Parcelable.Creator<FuelModel>() {
        public FuelModel createFromParcel(Parcel in) {
            return new FuelModel(in);
        }

        public FuelModel[] newArray(int size) {
            return new FuelModel[size];
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

    public long getDataSync() {
        return dataSync;
    }

    public void setDataSync(long dataSync) {
        this.dataSync = dataSync;
    }

    @Override
    public String toString() {
        return "FuelModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dataSync=" + dataSync +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FuelModel fuelModel = (FuelModel) o;

        return id == fuelModel.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}