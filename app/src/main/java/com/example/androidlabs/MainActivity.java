package com.example.androidlabs;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText nameEditText;
    private RadioGroup colorGroup, priceGroup;
    private Button orderButton;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        nameEditText = findViewById(R.id.editTextName);
        colorGroup = findViewById(R.id.radioGroupColor);
        priceGroup = findViewById(R.id.radioGroupPrice);
        orderButton = findViewById(R.id.buttonOrder);
        resultTextView = findViewById(R.id.textViewResult);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                int colorId = colorGroup.getCheckedRadioButtonId();
                int priceId = priceGroup.getCheckedRadioButtonId();

                if (TextUtils.isEmpty(name) || colorId == -1 || priceId == -1) {
                    Toast.makeText(MainActivity.this,R.string.error, Toast.LENGTH_SHORT).show();
                } else {
                    RadioButton selectedColor = findViewById(colorId);
                    RadioButton selectedPrice = findViewById(priceId);
                    String result = "Ім'я: " + name + "\nКолір: " + selectedColor.getText() + "\nЦіна: " + selectedPrice.getText();
                    resultTextView.setText(result);
                }
            }
        });
    }
}