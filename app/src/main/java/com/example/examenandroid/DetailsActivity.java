package com.example.examenandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.examenandroid.model.SingleUser;
import com.example.examenandroid.network.ApiClient;
import com.example.examenandroid.network.ApiReqres;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView tvId,tvEmail,tvFirstName,tvLastName;
    private Toolbar jtb;
    private ProgressBar progressBar;
    private SingleUser.User userData;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        id = getIntent().getIntExtra("id",0);

        imageView = findViewById(R.id.img_details_avatar);
        tvId = findViewById(R.id.tv_details_id);
        tvEmail = findViewById(R.id.tv_details_email);
        tvFirstName = findViewById(R.id.tv_details_first_name);
        tvLastName = findViewById(R.id.tv_details_last_name);
        progressBar = findViewById(R.id.pb_details);

        jtb = findViewById(R.id.toolbar_details);
        jtb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        showData();
    }

    private void showData(){
        Call<SingleUser> call = ApiClient.getClient().create(ApiReqres.class).getUser("users/"+(id+1));
        call.enqueue(new Callback<SingleUser>() {
            @Override
            public void onResponse(Call<SingleUser> call, Response<SingleUser> response) {
                progressBar.setVisibility(ProgressBar.GONE);
                if(response.isSuccessful()){
                    SingleUser user = response.body();
                    userData = user.data;

                    tvId.setText(userData.id.toString());
                    tvEmail.setText(userData.email);
                    tvFirstName.setText(userData.first_name);
                    tvLastName.setText(userData.last_name);
                    Glide.with(DetailsActivity.this).load(userData.avatar).into(imageView);
                }else{
                    Toast.makeText(DetailsActivity.this,response.toString(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SingleUser> call, Throwable t) {
                progressBar.setVisibility(ProgressBar.GONE);
                Toast.makeText(DetailsActivity.this,"Error",Toast.LENGTH_SHORT).show();
            }
        });
    }
}