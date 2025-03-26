package com.example.androidlabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class HistoryFragment extends Fragment {

    private static final String FILE_NAME = "orders.txt";
    private TextView ordersHistoryList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        ordersHistoryList = view.findViewById(R.id.ordersHistoryList);
        Button deleteHistoryButton = view.findViewById(R.id.buttonDeleteHistory);
        Button backButton = view.findViewById(R.id.buttonBack);

        loadHistoryFromFile();

        deleteHistoryButton.setOnClickListener(v -> deleteHistoryFile());

        backButton.setOnClickListener(v -> getFragmentManager().popBackStack());

        return view;
    }

    private void loadHistoryFromFile() {
        StringBuilder stringBuilder = new StringBuilder();

        try (FileInputStream fis = getActivity().openFileInput(FILE_NAME);
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

        } catch (IOException e) {
            stringBuilder.append("Немає збережених записів.");
        }

        ordersHistoryList.setText(stringBuilder.toString());
    }

    private void deleteHistoryFile() {
        if (getActivity().deleteFile(FILE_NAME)) {
            ordersHistoryList.setText("Немає збережених записів.");
            Toast.makeText(getActivity(), "Історію видалено", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Не вдалося видалити історію", Toast.LENGTH_SHORT).show();
        }
    }
}

