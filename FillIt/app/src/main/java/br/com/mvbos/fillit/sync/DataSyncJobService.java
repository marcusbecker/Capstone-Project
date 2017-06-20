package br.com.mvbos.fillit.sync;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;

/**
 * Created by Marcus Becker on 20/06/2017.
 */

public class DataSyncJobService extends com.firebase.jobdispatcher.JobService {

    private AsyncTask mTask;

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        mTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Context context = DataSyncJobService.this;
                DataSyncTask.executeTask(context, DataSyncTask.ACTION_SYNC);

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                boolean needToBeRescheduled = false;
                jobFinished(jobParameters, needToBeRescheduled);
            }
        };

        mTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (mTask != null) {
            mTask.cancel(true);
        }

        boolean jobShouldBeRetriedAgain = true;
        return jobShouldBeRetriedAgain;
    }
}
