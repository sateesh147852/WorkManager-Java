package com.workmanager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends Worker {

    public static final String TAG = MyWorker.class.getSimpleName();
    public static final String TASK_DESC = "task_desc";


    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        displayNotification("My Worker", "Hey I finished my work");
        String task = getInputData().getString(TASK_DESC);
        showToastMessage(task);

        Data data = new Data.Builder()
                .putString(TASK_DESC,task)
                .build();

        return Result.success(data);
    }

    private void showToastMessage(String message){

        Log.i(TAG, "showToastMessage: "+message + "  "+ Thread.currentThread().getName());
    }

    private void displayNotification(String title, String task) {

        NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel("workManager","workManager",NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notification = new  NotificationCompat.Builder(getApplicationContext(),"workManager")
                .setContentTitle(title)
                .setContentText(task)
                .setSmallIcon(R.mipmap.ic_launcher);

        notificationManager.notify(1,notification.build());
    }
}
