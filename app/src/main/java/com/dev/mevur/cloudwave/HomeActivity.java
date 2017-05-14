package com.dev.mevur.cloudwave;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
    public void btnFunctionClicked(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_navigation: {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btn_alarm: {
                Intent intent = new Intent(HomeActivity.this, AlarmActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btn_map: {
                Intent intent = new Intent(HomeActivity.this, MapActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}
