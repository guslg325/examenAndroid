package com.example.examenandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.examenandroid.dialogs.LoginErrorDialog;
import com.example.examenandroid.model.Login;
import com.example.examenandroid.model.ReqresCredentials;
import com.example.examenandroid.network.ApiClient;
import com.example.examenandroid.network.ApiReqres;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences sp;
    private String status;
    private EditText etEmail, etPassword;
    private ProgressBar progressBar;
    private Button btnLogin, btnRegister;
    private Login l;
    private ReqresCredentials c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Verify active session
        sp = getSharedPreferences("persistant_preferences", Context.MODE_PRIVATE);
        status = sp.getString("session_token","");
        if(!status.isEmpty()){
            startActivity(new Intent(MainActivity.this, MainMenuActivity.class));
            finish();
        }

        etEmail = findViewById(R.id.et_login_email);
        etPassword = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.btn_login_login);
        btnRegister = findViewById(R.id.btn_login_register);
        progressBar = findViewById(R.id.pb_main);
        progressBar.setVisibility(ProgressBar.GONE);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_login_login:
                progressBar.setVisibility(ProgressBar.VISIBLE);
                attemptLogin(etEmail.getText().toString(),etPassword.getText().toString());
                break;
            case R.id.btn_login_register:
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                break;
        }
    }

    void attemptLogin(String email, String password){
        c = new ReqresCredentials();
        c.setEmail(email);
        c.setPassword(password);

        Call<Login> call = ApiClient.getClient().create(ApiReqres.class).getLogin(c);
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                progressBar.setVisibility(ProgressBar.GONE);
                if(response.isSuccessful()){
                    l = response.body();

                    SharedPreferences.Editor spEdit = sp.edit();
                    spEdit.putString("session_token",l.getToken());
                    spEdit.putString("email",email);
                    spEdit.commit();

                    Intent itn = new Intent(MainActivity.this,MainMenuActivity.class);
                    itn.putExtra("email",email);
                    startActivity(itn);
                    finish();
                }else{
                    DialogFragment dialog = new LoginErrorDialog();
                    dialog.show(getSupportFragmentManager(),"login error");
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                progressBar.setVisibility(ProgressBar.GONE);
                DialogFragment dialog = new LoginErrorDialog();
                dialog.show(getSupportFragmentManager(),"login error");
            }
        });
    }
}