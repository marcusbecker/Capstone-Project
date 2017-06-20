package br.com.mvbos.fillit.sync;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

/**
 * Created by Marcus Becker on 20/06/2017.
 */

public class DataSyncStarter {

    private static final String DATA_SYNC_TAG = "dtSynTag";
    private static final int INTERVAL_RUNNER = (int) TimeUnit.HOURS.toMillis(3);
    private static final int INTERVAL_SECONDS = (int) TimeUnit.MINUTES.toMillis(1);
    private static boolean initializes;


    synchronized public static void scheduleTask(@NonNull final Context context) {
        if (initializes)
            return;

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job.Builder job = dispatcher.newJobBuilder();

        job.setService(DataSyncJobService.class)
                .setTag(DATA_SYNC_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(INTERVAL_RUNNER, INTERVAL_RUNNER + INTERVAL_SECONDS))
                .setReplaceCurrent(true);

        dispatcher.schedule(job.build());

        initializes = true;
    }

}
