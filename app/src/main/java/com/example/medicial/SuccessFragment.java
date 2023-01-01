package com.example.medicial;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class SuccessFragment extends Fragment {
    Handler handler = new Handler();
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        {Inflate the layout for this fragment}
        view = inflater.inflate(R.layout.fragment_success, container, false);

        Runnable runnable = this::ReturnToLogin;
        handler.postDelayed(runnable, 2 * 1000);
        return view;
    }

    public void ReturnToLogin() {
        Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
}