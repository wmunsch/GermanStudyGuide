package com.williammunsch.germanstudyguide.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.williammunsch.germanstudyguide.R;


/**
 * Fragment that is displayed after a failed login attempt.
 */
public class LoginDialogFragment extends DialogFragment {
    public LoginDialogFragment(){
        //Required empty constructor
    }

    public static LoginDialogFragment newInstance(String title, String info){
        LoginDialogFragment frag = new LoginDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("info", info);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.logindialog_layout, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get fields from view
        TextView textView1 = view.findViewById(R.id.textView_login1);
        TextView textView2 = view.findViewById(R.id.textView_login2);
        Button button = view.findViewById(R.id.button_login1);

        textView1.setText(getArguments().getString("title"));
        textView2.setText(getArguments().getString("info"));

        button.setOnClickListener(v->dismiss());

    }


}
