package com.example.mywordle;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;


public class FragmentProfile extends Fragment {

@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup contaner, Bundle savedInstanceState){
    View view = inflater.inflate(R.layout.fragment_profile,contaner,false);
    return view;
}


}