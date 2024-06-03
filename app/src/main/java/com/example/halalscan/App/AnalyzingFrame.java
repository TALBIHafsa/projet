package com.example.halalscan.App;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.halalscan.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AnalyzingFrame extends BottomSheetDialogFragment {


    public static AnalyzingFrame newInstance(String scannedBarcode) {
        AnalyzingFrame fragment = new AnalyzingFrame();
        Bundle args = new Bundle();
        args.putString("scannedBarcode", scannedBarcode);
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_analyzing, container, false);
    }
}