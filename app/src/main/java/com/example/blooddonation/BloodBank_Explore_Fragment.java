package com.example.blooddonation;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.DialogPlusBuilder;
import com.orhanobut.dialogplus.ViewHolder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BloodBank_Explore_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BloodBank_Explore_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BloodBank_Explore_Fragment() {
        // Required empty public constructor
    }

    String address,email,map,mono,name,password,pin,time,userid;

    String abnve,abpnv,anve,apve,bnve,bpve,onve,opve;

    TextView o1,o2,o3,o4,o5,o6,o7,o8,o9,o10,o11,o12,o13,o14,o15,o16,o17,o18;

    public BloodBank_Explore_Fragment(String address,String email,String map,String mono,String name,String password,String pin,String time,String userid) {
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BloodBank_Explore_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BloodBank_Explore_Fragment newInstance(String param1, String param2) {
        BloodBank_Explore_Fragment fragment = new BloodBank_Explore_Fragment();
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

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_blood_bank__explore, container, false);

        ImageButton bloodbank_back_button = view.findViewById(R.id.bloodbank_back_button);
        ImageView bloodbank_img = view.findViewById(R.id.bloodbank_img);
        TextView bloodbank_title_text = view.findViewById(R.id.bloodbank_title_text);
        TextInputEditText blood_ID = view.findViewById(R.id.blood_ID);
        TextInputEditText blood_fullname = view.findViewById(R.id.blood_fullname);
        TextInputEditText blood_address = view.findViewById(R.id.blood_address);
        TextInputEditText blood_email = view.findViewById(R.id.blood_email);
        TextInputEditText blood_contact = view.findViewById(R.id.blood_contact);
        TextInputEditText blood_area = view.findViewById(R.id.blood_area);
        TextInputEditText blood_time = view.findViewById(R.id.blood_time);
        Button bloodbank_call = view.findViewById(R.id.bloodbank_call);
        Button bloodbank_locate = view.findViewById(R.id.bloodbank_locate);
        Button view_blood_data = view.findViewById(R.id.view_blood_data);

        view_blood_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPlus dialogPlus = DialogPlus.newDialog(getContext())
                        .setContentHolder(new ViewHolder(R.layout.blood_data))
                        .setExpanded(true,1025)
                        .create();
                dialogPlus.show();

                View myview = dialogPlus.getHolderView();

                LinearLayout linearLayout;
                linearLayout = myview.findViewById(R.id.blood_data_linearlayout);
                linearLayout.setPadding(32, 32, 32, 32);

                o1 = myview.findViewById(R.id.o1);
                o2 = myview.findViewById(R.id.o2);
                o3 = myview.findViewById(R.id.o3);
                o4 = myview.findViewById(R.id.o4);
                o5 = myview.findViewById(R.id.o5);
                o6 = myview.findViewById(R.id.o6);
                o7 = myview.findViewById(R.id.o7);
                o8 = myview.findViewById(R.id.o8);
                o9 = myview.findViewById(R.id.o9);
                o18 = myview.findViewById(R.id.o18);
                o10 = myview.findViewById(R.id.o10);
                o11 = myview.findViewById(R.id.o11);
                o12 = myview.findViewById(R.id.o12);
                o13 = myview.findViewById(R.id.o13);
                o14 = myview.findViewById(R.id.o14);
                o15 = myview.findViewById(R.id.o15);
                o16 = myview.findViewById(R.id.o16);
                o17 = myview.findViewById(R.id.o17);

                databaseReference.child("BloodBankData").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(userid)){

                            abnve = snapshot.child(userid).child("abnve").getValue(String.class);
                            abpnv = snapshot.child(userid).child("abpve").getValue(String.class);
                            anve = snapshot.child(userid).child("anve").getValue(String.class);
                            apve = snapshot.child(userid).child("apve").getValue(String.class);
                            bnve = snapshot.child(userid).child("bnve").getValue(String.class);
                            bpve = snapshot.child(userid).child("bpve").getValue(String.class);
                            onve = snapshot.child(userid).child("onve").getValue(String.class);
                            opve = snapshot.child(userid).child("opve").getValue(String.class);

                            o10.setText(abnve);
                            o11.setText(abpnv);
                            o12.setText(anve);
                            o13.setText(apve);
                            o14.setText(bnve);
                            o15.setText(bpve);
                            o16.setText(onve);
                            o17.setText(opve);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });




//        blood_data.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                FragmentTransaction fr = getFragmentManager().beginTransaction();
//                fr.replace(R.id.yo,new BloodBankDataFragment());
//                fr.addToBackStack(null);
//                fr.commit();
//            }
//        });

        bloodbank_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = String.valueOf(blood_contact.getText());
                Intent intent =new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+number));
                getActivity().startActivity(intent);
            }
        });

        bloodbank_locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("geo:0, 0?q="+map);

                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });

        bloodbank_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),BloodBankActivity.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
            }
        });
        blood_ID.setText(userid);
        blood_fullname.setText(name);
        blood_address.setText(address);
        blood_contact.setText(mono);
        blood_time.setText(time);
        blood_area.setText(pin);
        blood_email.setText(email);

//        Glide.with(getContext()).load(img_blood).into(bloodbank_img);

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