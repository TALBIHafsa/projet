package com.example.halalscan.App;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.halalscan.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddSuggestionBottomSheetFragment extends BottomSheetDialogFragment {

    private String scannedBarcode;

    public static AddSuggestionBottomSheetFragment newInstance(String scannedBarcode) {
        AddSuggestionBottomSheetFragment fragment = new AddSuggestionBottomSheetFragment();
        Bundle args = new Bundle();
        args.putString("scannedBarcode", scannedBarcode);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_suggestion2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            scannedBarcode = getArguments().getString("scannedBarcode");
        }

        view.findViewById(R.id.add).setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddProductActivity.class);
            intent.putExtra("scannedBarcode", scannedBarcode);
            startActivity(intent);
        });

    }
}