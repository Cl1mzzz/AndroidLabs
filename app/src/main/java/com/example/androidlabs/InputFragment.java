package com.example.androidlabs;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.FileOutputStream;
import java.io.IOException;

public class InputFragment extends Fragment {

    private EditText nameEditText;
    private RadioGroup colorGroup, priceGroup;
    private Button orderButton, viewHistoryButton;
    private OnDataPassListener dataPasser;

    public interface OnDataPassListener {
        void onDataPass(String data);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPassListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input, container, false);

        nameEditText = view.findViewById(R.id.editTextName);
        colorGroup = view.findViewById(R.id.radioGroupColor);
        priceGroup = view.findViewById(R.id.radioGroupPrice);
        orderButton = view.findViewById(R.id.buttonOrder);
        viewHistoryButton = view.findViewById(R.id.buttonViewHistory);

        orderButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            int colorId = colorGroup.getCheckedRadioButtonId();
            int priceId = priceGroup.getCheckedRadioButtonId();

            if (TextUtils.isEmpty(name) || colorId == -1 || priceId == -1) {
                Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
            } else {
                RadioButton selectedColor = view.findViewById(colorId);
                RadioButton selectedPrice = view.findViewById(priceId);
                String result = "Ім'я: " + name + "\nКолір: " + selectedColor.getText() + "\nЦіна: " + selectedPrice.getText();
                saveOrderToFile(result);
                dataPasser.onDataPass(result);
            }
        });

        viewHistoryButton.setOnClickListener(v -> {
            ((MainActivity) getActivity()).viewHistory();
        });

        return view;
    }

    private void saveOrderToFile(String orderData) {
        try (FileOutputStream fos = getContext().openFileOutput("orders.txt", Context.MODE_APPEND)) {
            fos.write((orderData + "\n\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Помилка збереження", Toast.LENGTH_SHORT).show();
        }
    }
}
