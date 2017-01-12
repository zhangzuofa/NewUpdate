package com.example.administrator.newupdate;

import android.support.annotation.BinderThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity {
   @BindView(R.id.start_btn)
    Button startBtn;
    @BindView(R.id.pause_btn)
    Button pauseBtn;
    @BindView(R.id.stop_btn)
    Button stopBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }


}
