package com.pkh.sopt_19th_6.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pkh.sopt_19th_6.R;
import com.pkh.sopt_19th_6.application.ApplicationController;
import com.pkh.sopt_19th_6.main.MainResult;
import com.pkh.sopt_19th_6.network.NetworkService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {


    TextView titleTextview;
    TextView contentTextview;
    ImageView imgView;

    Button closeBtn;
    NetworkService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        titleTextview = (TextView)findViewById(R.id.titleTextview);
        contentTextview = (TextView)findViewById(R.id.contentTextview);
        imgView = (ImageView)findViewById(R.id.imgView);
        closeBtn=(Button)findViewById(R.id.closeBtn);


        Intent intent = getIntent();
        String id = intent.getExtras().getString("id");


        service = ApplicationController.getInstance().getNetworkService();

        // TODO: 2016. 11. 21. 요청
        Call<MainResult> requestDetail = service.getDetailData(id);
        requestDetail.enqueue(new Callback<MainResult>() {
            @Override
            public void onResponse(Call<MainResult> call, Response<MainResult> response) {
                if(response.isSuccessful()){
                    titleTextview.setText(response.body().result.get(0).subject);
                    contentTextview.setText(response.body().result.get(0).contents);

                    if(response.body().result.get(0).image_url != ""){
                        Glide.with(getApplicationContext())
                                .load(response.body().result.get(0).image_url)
                                .into(imgView);
                    }
                }
            }

            @Override
            public void onFailure(Call<MainResult> call, Throwable t) {

            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
