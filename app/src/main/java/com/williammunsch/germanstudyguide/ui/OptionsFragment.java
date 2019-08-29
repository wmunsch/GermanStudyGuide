package com.williammunsch.germanstudyguide.ui;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

public class OptionsFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static OptionsFragment newInstance(int index) {
        OptionsFragment fragment = new OptionsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
