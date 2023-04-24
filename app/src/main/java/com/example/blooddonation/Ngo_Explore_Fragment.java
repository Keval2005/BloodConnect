package com.example.blooddonation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Ngo_Explore_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Ngo_Explore_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String address,email,id,img_ngo,mono,name,pin;

    public Ngo_Explore_Fragment() {
        // Required empty public constructor
    }

    public Ngo_Explore_Fragment(String address,String email,String id,String img_ngo,String mono,String name,String pin){
        this.address = address;
        this.email = email;
        this.id = id;
        this.img_ngo = img_ngo;
        this.mono = mono;
        this.name = name;
        this.pin = pin;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Ngo_Explore_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Ngo_Explore_Fragment newInstance(String param1, String param2) {
        Ngo_Explore_Fragment fragment = new Ngo_Explore_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_ngo_explore, container, false);

        ImageButton back =  view.findViewById(R.id.ngo_back_button);
        ImageView ngo_img = view.findViewById(R.id.ngo_img);
        TextInputEditText ngo_id = view.findViewById(R.id.ngo_ID);
        TextInputEditText ngo_name = view.findViewById(R.id.ngo_fullname);
        TextInputEditText ngo_address = view.findViewById(R.id.ngo_address);
        TextInputEditText ngo_email = view.findViewById(R.id.ngo_email);
        Button ngo_call = view.findViewById(R.id.ngo_call);
        Button ngo_select = view.findViewById(R.id.ngo_select);

        ngo_id.setText(id);
        ngo_name.setText(name);
        ngo_address.setText(address);
        ngo_email.setText(email);
        Glide.with(getContext()).load(img_ngo).into(ngo_img);

        Bundle b = getArguments();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(getActivity(),ngoActivity.class);
                getActivity().startActivity(back);
                getActivity().overridePendingTransition(0, 0);
            }
        });

        ngo_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = String.valueOf(mono);
                Intent intent =new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+number));
                getActivity().startActivity(intent);
            }
        });

        ngo_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selectedNgo = new Intent(getActivity(),CreateCamp.class);
//                SharedPreferences sp = getActivity().getSharedPreferences("ngo", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sp.edit();
//                editor.putString("ngoID", id);
//                editor.putString("ngoName", name);
//                editor.commit();
                selectedNgo.putExtra("campBundle", b);
                selectedNgo.putExtra("ngoID", id);
                selectedNgo.putExtra("ngoName", name);
                getActivity().startActivity(selectedNgo);
            }
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar_id);
        toolbar.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar_id);
        toolbar.setVisibility(View.VISIBLE);
    }
}