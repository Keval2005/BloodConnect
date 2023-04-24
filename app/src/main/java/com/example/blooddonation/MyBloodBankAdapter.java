package com.example.blooddonation;

import android.media.Image;
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

public class MyBloodBankAdapter extends FirebaseRecyclerAdapter<ModelBloodBank,MyBloodBankAdapter.myViewHolder1> {


    public MyBloodBankAdapter(@NonNull FirebaseRecyclerOptions<ModelBloodBank> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyBloodBankAdapter.myViewHolder1 holder, int position, @NonNull ModelBloodBank model1) {

        holder.address.setText(model1.getAddress());
//        holder.answer.setText(model1.getAnswer());
        holder.email.setText(model1.getEmail());
        holder.mono.setText(model1.getMono());
        holder.name.setText(model1.getName());
        holder.password.setText(model1.getPassword());
        holder.pin.setText(model1.getPin());
//        holder.question.setText(model1.getQuestion());
        holder.time.setText(model1.getTime());
        holder.userid.setText(model1.getUserid());
//        Glide.with(holder.img_blood.getContext()).load(model1.getImg_blood()).into(holder.img_blood);

        holder.Explore1_blood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity)v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace
                        (R.id.framelayout_id,new BloodBank_Explore_Fragment(model1.getAddress(),model1.getEmail(),model1.getMap(),model1.getMono(),model1.getName(),model1.getPassword(),model1.getPin(),model1.getTime(),model1.getUserid())).addToBackStack(null).commit();
            }
        });
    }

    @NonNull
    @Override
    public MyBloodBankAdapter.myViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowdesign_bloodbank,parent,false);
        return new MyBloodBankAdapter.myViewHolder1(view);
    }

    public class myViewHolder1 extends RecyclerView.ViewHolder{

        ImageView img_blood;

        TextView address,answer,email,mono,name,password,pin,question,time,userid;
        Button Explore1_blood;
        View rootView;

        public myViewHolder1(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
//
//            img_blood = (ImageView)itemView.findViewById(R.id.img_blood);
            address = (TextView)itemView.findViewById(R.id.address);
            answer = (TextView)itemView.findViewById(R.id.answer);
            email = (TextView)itemView.findViewById(R.id.email);
            Explore1_blood = (Button)itemView.findViewById(R.id.Explore1_blood);
            mono = (TextView)itemView.findViewById(R.id.mono);
            name = (TextView)itemView.findViewById(R.id.name);
            password = (TextView)itemView.findViewById(R.id.password);
            pin = (TextView)itemView.findViewById(R.id.pin);
            question = (TextView)itemView.findViewById(R.id.question);
            time = (TextView)itemView.findViewById(R.id.time);
            userid = (TextView)itemView.findViewById(R.id.userid);
        }
    }
}
