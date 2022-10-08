package com.example.examenandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.examenandroid.dialogs.RegisterErrorDialog;
import com.example.examenandroid.model.Register;
import com.example.examenandroid.model.ReqresCredentials;
import com.example.examenandroid.network.ApiClient;
import com.example.examenandroid.network.ApiReqres;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText etEmail,etPassword;
    private Button btnRegister;
    private ProgressBar progressBar;
    private Register r;
    private ReqresCredentials c;
    private Toolbar jtb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        jtb = findViewById(R.id.toolbar_register);
        jtb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        etEmail = findViewById(R.id.et_register_email);
        etPassword = findViewById(R.id.et_register_password);
        btnRegister = findViewById(R.id.btn_register_register);
        progressBar = findViewById(R.id.pb_register);

        progressBar.setVisibility(ProgressBar.GONE);

        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        attemptRegister(etEmail.getText().toString(),etPassword.getText().toString());
    }

    void attemptRegister(String email,String password){
        progressBar.setVisibility(ProgressBar.VISIBLE);
        c = new ReqresCredentials();
        c.setEmail(email);
        c.setPassword(password);

        Call<Register> call = ApiClient.getClient().create(ApiReqres.class).getRegister(c);
        call.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                progressBar.setVisibility(ProgressBar.GONE);
                if(response.isSuccessful()){
                    r = response.body();
                    finish();
                }else{
                    DialogFragment dialog = new RegisterErrorDialog();
                    dialog.show(getSupportFragmentManager(),"register error");
                }
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                progressBar.setVisibility(ProgressBar.GONE);
                DialogFragment dialog = new RegisterErrorDialog();
                dialog.show(getSupportFragmentManager(),"register error");
            }
        });
    }
}