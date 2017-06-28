package br.com.mvbos.fillit.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Marcus Becker on 28/06/2017.
 */

public class FlagModel implements Parcelable {
    private long id;
    private String name;
    private String icon;
    private long dataSync;

    public FlagModel(long id) {
        this.id = id;
    }

    public FlagModel(long id, String name, String icon, long dataSync) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.dataSync = dataSync;
    }

    public FlagModel(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.icon = in.readString();
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
        dest.writeString(this.icon);
        dest.writeLong(this.dataSync);
    }

    public static final Parcelable.Creator<FlagModel> CREATOR = new Parcelable.Creator<FlagModel>() {
        public FlagModel createFromParcel(Parcel in) {
            return new FlagModel(in);
        }

        public FlagModel[] newArray(int size) {
            return new FlagModel[size];
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

        FlagModel flag = (FlagModel) o;

        return id == flag.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "FlagModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", dataSync=" + dataSync +
                '}';
    }
}