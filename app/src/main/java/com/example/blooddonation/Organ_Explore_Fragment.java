package com.example.blooddonation;

import android.content.Intent;
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
 * Use the {@link Organ_Explore_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Organ_Explore_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Organ_Explore_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Organ_Explore_Fragment newInstance(String param1, String param2) {
        Organ_Explore_Fragment fragment = new Organ_Explore_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Organ_Explore_Fragment(){
        // Required empty public constructor
    }
    String address,email,map,mono,name,password,pin,time,userid;
    public Organ_Explore_Fragment(String address,String email,String map,String mono,String name,String password,String pin,String time,String userid) {
        // Required empty public constructor

        this.address = address;
        this.email = email;
        this.map = map;
        this.mono = mono;
        this.name = name;
        this.password = password;
        this.pin = pin;
        this.time = time;
        this.userid = userid;

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
        View view =  inflater.inflate(R.layout.fragment_organ_explore, container, false);

        ImageButton hospital_back_button = view.findViewById(R.id.hospital_back_button);
        ImageView hospital_img = view.findViewById(R.id.hospital_img);
        TextView hospital_title_text = view.findViewById(R.id.hospital_title_text);
        TextInputEditText hospital_ID = view.findViewById(R.id.hospital_ID);
        TextInputEditText hospital_fullname = view.findViewById(R.id.hospital_fullname);
        TextInputEditText hospital_address = view.findViewById(R.id.hospital_address);
        TextInputEditText hospital_email = view.findViewById(R.id.hospital_email);
        TextInputEditText hospital_contact = view.findViewById(R.id.hospital_contact);
        TextInputEditText hospital_area = view.findViewById(R.id.hospital_area);
        TextInputEditText hospital_time = view.findViewById(R.id.hospital_time);
        Button hospital_call = view.findViewById(R.id.hospital_call);
        Button hospital_locate = view.findViewById(R.id.hospital_locate);

        hospital_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = String.valueOf(hospital_contact.getText());
                Intent intent =new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+number));
                getActivity().startActivity(intent);
            }
        });

        hospital_locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("geo:0, 0?q="+map);

                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });

        hospital_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrganActivity.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
            }
        });
        hospital_ID.setText(userid);
        hospital_fullname.setText(name);
        hospital_address.setText(address);
        hospital_contact.setText(mono);
        hospital_time.setText(time);
        hospital_area.setText(pin);
        hospital_email.setText(email);

//        Glide.with(getContext()).load(img_hospital).into(hospital_img);
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