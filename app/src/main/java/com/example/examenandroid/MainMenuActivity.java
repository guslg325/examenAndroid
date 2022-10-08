package com.example.examenandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.examenandroid.adapter.UserAdapter;
import com.example.examenandroid.model.UserList;
import com.example.examenandroid.network.ApiClient;
import com.example.examenandroid.network.ApiReqres;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainMenuActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {
    private RecyclerView rvUsers;
    private UserAdapter userAdapter;
    private List<UserList.User> userListData;
    private Toolbar mToolbar;
    private ProgressBar progressBar;
    private FragmentDrawer drawerFragment;
    private SharedPreferences sp;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        sp = getSharedPreferences("persistant_preferences",MODE_PRIVATE);

        email = sp.getString("email","");

        progressBar = findViewById(R.id.pb_main_menu);

        mToolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout),mToolbar,email);
        drawerFragment.setDrawerListener(this);

        rvUsers = findViewById(R.id.rv_users);

        showUsers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        SearchView searchView = findViewById(R.id.sv_search);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                userAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    public void showUsers(){
        Call<UserList> call = ApiClient.getClient().create(ApiReqres.class).getUsers("1");
        call.enqueue(new Callback<UserList>() {
            @Override
            public void onResponse(Call<UserList> call, Response<UserList> response) {
                progressBar.setVisibility(ProgressBar.GONE);
                if (response.isSuccessful()){
                    UserList userList = response.body();
                    userListData = userList.data;

                    userAdapter = new UserAdapter(userListData,getApplicationContext());
                    rvUsers.setAdapter(userAdapter);
                    rvUsers.addOnItemTouchListener(new FragmentDrawer.RecyclerTouchListener(MainMenuActivity.this,
                            rvUsers, new FragmentDrawer.ClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                            Intent itn = new Intent(MainMenuActivity.this,DetailsActivity.class);
                            itn.putExtra("id",position);
                            startActivity(itn);
                        }

                        @Override
                        public void onLongClick(View view, int position) {

                        }
                    }));
                }else{
                    //Error getting users
                    Toast.makeText(MainMenuActivity.this,R.string.dialog_error_title,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserList> call, Throwable t) {
                progressBar.setVisibility(ProgressBar.GONE);
                Toast.makeText(MainMenuActivity.this,R.string.dialog_error_title,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        SharedPreferences.Editor spEditor = sp.edit();
        spEditor.putString("session_token","");
        spEditor.putString("email","");
        spEditor.commit();

        Intent itn = new Intent(MainMenuActivity.this,MainActivity.class);
        startActivity(itn);
        finish();
    }
}