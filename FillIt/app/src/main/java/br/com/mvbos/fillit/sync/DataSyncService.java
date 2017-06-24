package br.com.mvbos.fillit.sync;


import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by Marcus Becker on 24/06/2017.
 */

public class DataSyncService extends IntentService {


    public DataSyncService() {
        super("DataSyncService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        DataSyncTask.executeTask(getBaseContext(), DataSyncTask.ACTION_SYNC);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Toast.makeText(this, "Sincronização finalizada.", Toast.LENGTH_SHORT).show();
    }
}
