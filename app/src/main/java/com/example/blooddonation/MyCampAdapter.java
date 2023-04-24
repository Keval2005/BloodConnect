package com.example.blooddonation;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MyCampAdapter extends FirebaseRecyclerAdapter<ModelCamp,MyCampAdapter.myViewHolder3> {
    String[] values;
    public MyCampAdapter(@NonNull FirebaseRecyclerOptions<ModelCamp> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyCampAdapter.myViewHolder3 holder, int position, @NonNull ModelCamp model) {
        values = null;
        holder.address.setText(model.getAddress());
        holder.date.setText(model.getDate());
        holder.id.setText(model.getId());
        holder.map.setText(model.getMap());
        holder.mono.setText(model.getMono());
        holder.name.setText(model.getName());
        holder.pin.setText(model.getPin());
        holder.tag.setText(model.getTag());
        holder.time.setText(model.getTime());
        holder.userid.setText(model.getUserid());
        Glide.with(holder.image.getContext()).load(model.getImage()).into(holder.image);

        holder.editbtn.setVisibility(View.INVISIBLE);
        holder.delbtn.setVisibility(View.INVISIBLE);

        SharedPreferences sharedPreferences = holder.image.getContext().getSharedPreferences("myKey", MODE_PRIVATE);
        String wow1 = sharedPreferences.getString("value","");

        if(wow1.equals(model.getUserid())){
            holder.editbtn.setVisibility(View.VISIBLE);
            holder.editbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final DialogPlus dialogPlus = DialogPlus.newDialog(holder.image.getContext())
                            .setContentHolder(new ViewHolder(R.layout.dialogcontent))
                            .setExpanded(true,1800)
                            .create();

                    final Context dialogluscontext = dialogPlus.getHolderView().getContext();

                    final TextInputEditText nameob,monoob,taglineob,st,et;
                    final Button submitob;

                    View myview = dialogPlus.getHolderView();

                    LinearLayout linearLayout;
                    linearLayout = myview.findViewById(R.id.linearlayout1);
                    linearLayout.setPadding(25, 25, 25, 25);
                    nameob = myview.findViewById(R.id.updatecamp_name);
                    monoob = myview.findViewById(R.id.updatecamp_mono);
                    taglineob = myview.findViewById(R.id.updatecamp_tag);
                    submitob = myview.findViewById(R.id.updatesubmit);
                    st = myview.findViewById(R.id.updatecamp_st);
                    et =  myview.findViewById(R.id.updatecamp_et);

                    Calendar myCalendar = Calendar.getInstance();

                    st.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TimePickerDialog dialog1 = new TimePickerDialog(dialogluscontext, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    st.setText(hourOfDay +":"+ minute);
                                }
                            }, 15, 00, true);
                            dialog1.show();
                        }
                    });

                    et.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TimePickerDialog dialog2 = new TimePickerDialog(dialogluscontext, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    et.setText(hourOfDay +":"+ minute);
                                }
                            }, 15, 00, true);
                            dialog2.show();
                        }
                    });


                    String timestr = model.getTime();
                    values = timestr.split("To");
                    final String before = values[0].trim();
                    final String after = values[1].trim();

                    monoob.setText(model.getMono());
                    nameob.setText(model.getName());
                    taglineob.setText(model.getTag());
                    st.setText(before);
                    et.setText(after);

                    dialogPlus.show();


                    submitob.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            final String timestr1 = String.valueOf(String.valueOf(st.getText().toString()) + " To " + String.valueOf(et.getText().toString()));

                            Map<String,Object> map = new HashMap<>();
                            map.put("mono",monoob.getText().toString());
                            map.put("name",nameob.getText().toString());
                            map.put("tag",taglineob.getText().toString());
                            map.put("time",timestr1);

                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                            databaseReference.child("camp").child("approved")
                                    .child(getRef(holder.getAdapterPosition()).getKey()).updateChildren(map)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            dialogPlus.dismiss();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            dialogPlus.dismiss();
                                        }
                                    });

                        }
                    });

                }
            });
        }else {
            holder.editbtn.setVisibility(View.INVISIBLE);
        }
        if(wow1.equals(model.getUserid())){
            holder.delbtn.setVisibility(View.VISIBLE);
            holder.delbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(holder.image.getContext());
                    builder.setTitle("Delete Your Camp");
                    builder.setMessage("Are you sure to delete Your Camp this time?.. ");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                            databaseReference.child("camp").child("approved")
                                    .child(getRef(holder.getAdapterPosition()).getKey()).removeValue();

                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    builder.show();

                }
            });
        }else {
            holder.delbtn.setVisibility(View.INVISIBLE);
        }






//        holder.Explore1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentManager manager;
//                manager = ((AppCompatActivity)v.getContext()).getSupportFragmentManager().beginTransaction().replace
//                                (R.id.main_menu,new descfragment(model.getCampid(),model.getCampname(),model.getStime(),model.getEtime(),model.getTagline(),model.getArea(),model.getPurl(),model.getUsername(),model.getLocation())).addToBackStack(null).commit();
//            }
//        });

        holder.Explore1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity)v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace
                        (R.id.framelayout_id,new Camp_Explore_Fragment(model.getAddress(),model.getDate(),model.getId(),model.getImage(),model.getMap(),model.getMono(),model.getName(),model.getPin(),model.getTag(),model.getTime(),model.getUserid())).addToBackStack(null).commit();
            }
        });
    }


    @NonNull
    @Override
    public MyCampAdapter.myViewHolder3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowdesign_camp,parent,false);
        return new MyCampAdapter.myViewHolder3(view);
    }

    public class myViewHolder3 extends RecyclerView.ViewHolder {
        ImageView image;
        TextView address,date,map,mono,name,pin,tag,time,userid,id;
        Button Explore1;
        View rootView;
        ImageButton delbtn,editbtn;
        public myViewHolder3(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;

//            recyclerView = (RecyclerView)itemView.findViewById(R.id.recview);
            image = (ImageView)itemView.findViewById(R.id.image);
            address = (TextView)itemView.findViewById(R.id.address);
            date = (TextView)itemView.findViewById(R.id.date);
            map = (TextView)itemView.findViewById(R.id.map);
            mono = (TextView)itemView.findViewById(R.id.mono);
            name = (TextView)itemView.findViewById(R.id.name);
            pin = (TextView)itemView.findViewById(R.id.pin);
            Explore1 = (Button)itemView.findViewById(R.id.Explore1);
            tag = (TextView)itemView.findViewById(R.id.tag);
            time = (TextView)itemView.findViewById(R.id.time);
            userid = (TextView)itemView.findViewById(R.id.userid);
            id = (TextView)itemView.findViewById(R.id.id);
            delbtn = (ImageButton) itemView.findViewById(R.id.delbtn);
            editbtn = (ImageButton) itemView.findViewById(R.id.editbtn);
        }
    }
}
