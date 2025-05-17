package com.example.lab4.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lab4.R;

public class InternetFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_url_view, container, false);

        LinearLayout urlContainer = view.findViewById(R.id.url_input_container);
        EditText urlEntry = view.findViewById(R.id.url_input_field);
        Button urlButton = view.findViewById(R.id.play_url_button);

        urlContainer.setVisibility(View.VISIBLE);

        return view;
    }

}
