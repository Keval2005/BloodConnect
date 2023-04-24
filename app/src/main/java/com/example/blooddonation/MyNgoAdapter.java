package com.example.blooddonation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class MyNgoAdapter extends FirebaseRecyclerAdapter<ModelNGO,MyNgoAdapter.myViewHolder4> {

    public MyNgoAdapter(@NonNull FirebaseRecyclerOptions<ModelNGO> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyNgoAdapter.myViewHolder4 holder, int position, @NonNull ModelNGO model1) {
        holder.address.setText(model1.getAddress());
//        holder.answer.setText(model1.getAnswer());
        holder.email.setText(model1.getEmail());
        holder.mono.setText(model1.getMono());
        holder.name.setText(model1.getName());
//        holder.password.setText(model1.getPassword());
        holder.pin.setText(model1.getPin());
//        holder.question.setText(model1.getQuestion());
//        holder.time.setText(model1.getTime());
        holder.userid.setText(model1.getId());
        Glide.with(holder.img_blood.getContext()).load(model1.getImg_ngo()).into(holder.img_blood);
        holder.Explore1_blood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity)v.getContext();
                String id = ((AppCompatActivity) v.getContext()).getIntent().getStringExtra("id");
                String name = ((AppCompatActivity) v.getContext()).getIntent().getStringExtra("name");
                String address = ((AppCompatActivity) v.getContext()).getIntent().getStringExtra("address");
                String pin = ((AppCompatActivity) v.getContext()).getIntent().getStringExtra("pin");
                String tag = ((AppCompatActivity) v.getContext()).getIntent().getStringExtra("tag");
                String st = ((AppCompatActivity) v.getContext()).getIntent().getStringExtra("st");
                String et = ((AppCompatActivity) v.getContext()).getIntent().getStringExtra("et");
                String mono = ((AppCompatActivity) v.getContext()).getIntent().getStringExtra("mono");
                String location_str = ((AppCompatActivity) v.getContext()).getIntent().getStringExtra("location_str");
                String datePickerob = ((AppCompatActivity) v.getContext()).getIntent().getStringExtra("datePickerob");

                Bundle b = new Bundle();
                b.putString("id",id);
                b.putString("name", name);
                b.putString("address", address);
                b.putString("pin", pin);
                b.putString("tag", tag);
                b.putString("st",st);
                b.putString("et", et);
                b.putString("mono", mono);
                b.putString("location_str", location_str);
                b.putString("datePickerob", datePickerob);

                Ngo_Explore_Fragment fragment = new Ngo_Explore_Fragment(model1.getAddress(), model1.getEmail(), model1.getId(), model1.getImg_ngo(), model1.getMono(), model1.getName(), model1.getPin());
                fragment.setArguments(b);
                activity.getSupportFragmentManager().beginTransaction().replace
                        (R.id.framelayout_id,fragment).addToBackStack(null).commit();
            }
        });
    }

    @NonNull
    @Override
    public MyNgoAdapter.myViewHolder4 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowdesign_ngo,parent,false);
        return new MyNgoAdapter.myViewHolder4(view);
    }

    public class myViewHolder4 extends RecyclerView.ViewHolder {

        ImageView img_blood;

        TextView address,answer,email,mono,name,password,pin,question,time,userid;
        Button Explore1_blood;
        View rootView;

        public myViewHolder4(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;

            img_blood = (ImageView)itemView.findViewById(R.id.img_blood);
            address = (TextView)itemView.findViewById(R.id.address);
            email = (TextView)itemView.findViewById(R.id.email);
            Explore1_blood = (Button)itemView.findViewById(R.id.Explore1_blood);
            mono = (TextView)itemView.findViewById(R.id.mono);
            name = (TextView)itemView.findViewById(R.id.name);
            pin = (TextView)itemView.findViewById(R.id.pin);
            userid = itemView.findViewById(R.id.userid);
        }
    }
}
