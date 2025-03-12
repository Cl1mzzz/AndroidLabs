package com.example.androidlabs;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements InputFragment.OnDataPassListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new InputFragment())
                    .commit();
        }
    }

    @Override
    public void onDataPass(String data) {
        ResultFragment resultFragment = ResultFragment.newInstance(data);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, resultFragment)
                .addToBackStack(null)
                .commit();
    }

    public void clearForm() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new InputFragment())
                .commit();
    }
}