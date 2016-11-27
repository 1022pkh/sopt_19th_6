package com.pkh.sopt_19th_6.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.pkh.sopt_19th_6.R;
import com.pkh.sopt_19th_6.application.ApplicationController;
import com.pkh.sopt_19th_6.detail.DetailActivity;
import com.pkh.sopt_19th_6.network.NetworkService;
import com.pkh.sopt_19th_6.register.RegisterActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<MainListData> mDatas;
    LinearLayoutManager mLayoutManager;
    Adapter adapter;
    ImageView addBtn;

    NetworkService service;


    //Back 키 두번 클릭 여부 확인
    private final long FINSH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addBtn = (ImageView)findViewById(R.id.addBtn);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        // layoutManager 설정
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);



        // TODO: 2016. 11. 21. 데이터 받아오기

        mDatas = new ArrayList<>();
        service = ApplicationController.getInstance().getNetworkService();


        Call<MainResult> requestMainData =  service.getMainData();
        requestMainData.enqueue(new Callback<MainResult>() {
            @Override
            public void onResponse(Call<MainResult> call, Response<MainResult> response) {

                if(response.isSuccessful()){
                    Log.i("myTag", String.valueOf(response.body().result.size()));
                    adapter.setAdapter(response.body().result);
                    mDatas = response.body().result;
                }
            }

            @Override
            public void onFailure(Call<MainResult> call, Throwable t) {

            }
        });


        adapter = new Adapter(mDatas,clickEvent);
        recyclerView.setAdapter(adapter);


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart
                ();
        // TODO: 2016. 11. 21. 새로 리스트 받아오기.

        Call<MainResult> requestMainData =  service.getMainData();
        requestMainData.enqueue(new Callback<MainResult>() {
            @Override
            public void onResponse(Call<MainResult> call, Response<MainResult> response) {

                if(response.isSuccessful()){

                    Log.i("myTag", String.valueOf(response.body().result.size()));
                    mDatas = response.body().result;
                    adapter.setAdapter(response.body().result);

                }
            }

            @Override
            public void onFailure(Call<MainResult> call, Throwable t) {

            }
        });

    }

    public View.OnClickListener clickEvent = new View.OnClickListener() {
        public void onClick(View v) {
            int itemPosition = recyclerView.getChildPosition(v);
            int tempId = mDatas.get(itemPosition).id;
//             Toast.makeText(getApplicationContext(),temp,Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
            intent.putExtra("id",String.valueOf(tempId));
            startActivity(intent);

        }
    };


    @Override
    public void onBackPressed() {
        long tempTime        = System.currentTimeMillis();
        long intervalTime    = tempTime - backPressedTime;

//            super.onBackPressed();
        /**
         * Back키 두번 연속 클릭 시 앱 종료
         */
        if ( 0 <= intervalTime && FINSH_INTERVAL_TIME >= intervalTime ) {
            super.onBackPressed();
        }
        else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(),"뒤로 가기 키을 한번 더 누르시면 종료됩니다.",Toast.LENGTH_SHORT).show();
        }

    }
}
