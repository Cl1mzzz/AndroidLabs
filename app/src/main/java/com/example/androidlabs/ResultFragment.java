package com.example.androidlabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ResultFragment extends Fragment {

    private static final String ARG_RESULT = "result";

    public static ResultFragment newInstance(String result) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_RESULT, result);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);

        TextView resultTextView = view.findViewById(R.id.textViewResult);
        Button cancelButton = view.findViewById(R.id.buttonCancel);

        if (getArguments() != null) {
            String result = getArguments().getString(ARG_RESULT);
            resultTextView.setText(result);
        }

        cancelButton.setOnClickListener(v -> {
            ((MainActivity) getActivity()).clearForm();
        });

        return view;
    }
}
