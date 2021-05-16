package com.workmanager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.workmanager.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static com.workmanager.MyWorker.TASK_DESC;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Data data;
    private Data data1;
    private Constraints constraints;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        data = new Data.Builder()
                .putString(TASK_DESC, "data passed from MainActivity")
                .build();

        data1 = new Data.Builder()
                .putString(TASK_DESC, "data passed from MainActivity2")
                .build();

        //constraints = new Constraints.Builder().setRequiresCharging(false).build();

        //initializeOneTimeRequest();
        //initializeOnePeriodicRequest();
        initializeChainedRequest();
    }

    private void initializeOneTimeRequest() {

        final OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
                .setInputData(data)
                .build();


        binding.btEnqueueWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkManager.getInstance().enqueue(oneTimeWorkRequest);
            }
        });

        WorkManager.getInstance().getWorkInfoByIdLiveData(oneTimeWorkRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        binding.tvStatus.setText(workInfo.getOutputData().getString(TASK_DESC));
                    }
                });


    }


    private void initializeChainedRequest() {

        final OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
                .setInputData(data)
                .build();

        final OneTimeWorkRequest oneTimeWorkRequest1 = new OneTimeWorkRequest.Builder(MyWorker.class)
                .setInputData(data1)
                .build();


        binding.btEnqueueWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<OneTimeWorkRequest> requests = new ArrayList<>();
                requests.add(oneTimeWorkRequest);
                requests.add(oneTimeWorkRequest1);

                WorkManager.getInstance().enqueue(requests);
            }
        });

        WorkManager.getInstance().getWorkInfoByIdLiveData(oneTimeWorkRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        binding.tvStatus.setText(workInfo.getOutputData().getString(TASK_DESC));
                    }
                });

        WorkManager.getInstance().getWorkInfoByIdLiveData(oneTimeWorkRequest1.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        binding.tvStatus2.setText(workInfo.getOutputData().getString(TASK_DESC));
                    }
                });

    }

    private void initializeOnePeriodicRequest() {

        final PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest
                .Builder(MyWorker.class, 15, TimeUnit.MINUTES)
                .setInputData(data)
                .build();

        binding.btEnqueueWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkManager.getInstance().enqueue(periodicWorkRequest);
            }
        });

    }

}
