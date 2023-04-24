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
import com.google.android.material.textview.MaterialTextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Camp_Explore_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Camp_Explore_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Camp_Explore_Fragment() {
        // Required empty public constructor
    }
    String address,date,id,image,map,mono,name,pin,tag,time,userid;

    public Camp_Explore_Fragment(String address, String date, String id, String image, String map, String mono, String name, String pin, String tag, String time, String userid) {
        this.address = address;
        this.date = date;
        this.id = id;
        this.image = image;
        this.map = map;
        this.mono = mono;
        this.name = name;
        this.pin = pin;
        this.tag = tag;
        this.time = time;
        this.userid = userid;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Camp_Explore_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Camp_Explore_Fragment newInstance(String param1, String param2) {
        Camp_Explore_Fragment fragment = new Camp_Explore_Fragment();
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
        View view = inflater.inflate(R.layout.fragment_camp_explore, container, false);



        ImageButton camp_back_button = view.findViewById(R.id.camp_back_button);
        ImageView camp_img = view.findViewById(R.id.camp_img);
        TextView camp_title_text = view.findViewById(R.id.camp_title_text);
        TextInputEditText camp_ID = view.findViewById(R.id.camp_ID);
        TextInputEditText camp_fullname = view.findViewById(R.id.camp_fullname);
        TextInputEditText camp_stime = view.findViewById(R.id.camp_stime);
        TextInputEditText camp_address = view.findViewById(R.id.camp_address);
        MaterialTextView camp_username = view.findViewById(R.id.camp_username);
        MaterialTextView camp_location = view.findViewById(R.id.camp_location);
        Button camp_update = view.findViewById(R.id.camp_update);
        Button camp_delete = view.findViewById(R.id.camp_delete);
        Button camp_map = view.findViewById(R.id.camp_map);

        camp_ID.setText(id);
        camp_fullname.setText(name);
        camp_stime.setText(time);
        camp_address.setText(address);
        camp_username.setText(userid);
        camp_location.setText(map);
        Glide.with(getContext()).load(image).into(camp_img);

        camp_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap();
            }
        });

        camp_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        camp_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),CampActivity.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
            }
        });

//        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                if(isEnabled()){
//                    Intent intent = new Intent(getActivity(),Campaign1.class);
//                    startActivity(intent);
//                    setEnabled(false);
//                    requireActivity().onBackPressed();
//                }
//            }
//        });


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
    private void openMap() {
        Uri uri = Uri.parse("geo:0, 0?q="+map);

        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);

    }
}