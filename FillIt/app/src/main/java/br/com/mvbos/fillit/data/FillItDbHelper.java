package br.com.mvbos.fillit.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Marcus Becker on 11/06/2017.
 */

class FillItDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "FillIt.db";

    public FillItDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FillItContract.SQL_CREATE_FUEL);
        db.execSQL(FillItContract.SQL_CREATE_FLAG);
        db.execSQL(FillItContract.SQL_CREATE_VEHICLE);
        db.execSQL(FillItContract.SQL_CREATE_FILL);
        db.execSQL(FillItContract.SQL_CREATE_GASSTATION);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(FillItContract.SQL_DELETE_FUEL);
        db.execSQL(FillItContract.SQL_DELETE_FLAG);
        db.execSQL(FillItContract.SQL_DELETE_VEHICLE);
        db.execSQL(FillItContract.SQL_DELETE_FILL);
        db.execSQL(FillItContract.SQL_DELETE_GASSTATION);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}



