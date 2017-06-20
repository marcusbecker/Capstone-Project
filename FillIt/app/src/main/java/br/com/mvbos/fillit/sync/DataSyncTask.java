package br.com.mvbos.fillit.sync;

import android.content.Context;

/**
 * Created by Marcus Becker on 20/06/2017.
 */

public class DataSyncTask {

    public static final String ACTION_SYNC = "dataSync";

    public static void executeTask(Context context, String action) {

        if (ACTION_SYNC.equals(action)) {
            //do something
            System.out.println("it is time!");
        }
    }
}
