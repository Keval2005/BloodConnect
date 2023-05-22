package com.example.blooddonation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateCamp extends AppCompatActivity {
    ImageButton back;
    TextInputEditText idob,nameob,addressob,monoob,taglineob,pinob,useridob,st,et,location_str;
    Button lctbtnob;
    Button browse,submitob;

    TextView t1,t2,locationop;
    ImageView img;
    TextInputEditText datePickerob,ngoId,ngoName;
    Uri filepath;
    Bitmap bitmap;

    SharedPreferences tempCampCreateInfo;
    String edtdate;

    LinearLayout linearLayout;
    String date,time,map,id,name,address,mono,tag,pin,userid,a,b,c,uristr,wow;

    String ststr,etstr;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_camp);

        linearLayout = findViewById(R.id.createcampParentLayout);

        Snackbar.make(linearLayout, "Double Click on Date,Time and Helper Ngo Fields to Enter data in them", Snackbar.LENGTH_LONG).show();
        back = findViewById(R.id.create_camp_back_button);
        idob = findViewById(R.id.camp_ID);
        nameob = findViewById(R.id.camp_name);
        addressob = findViewById(R.id.camp_address);
        monoob = findViewById(R.id.camp_mono);
        taglineob = findViewById(R.id.camp_tag);
        pinob = findViewById(R.id.camp_pin);
        useridob = findViewById(R.id.camp_username);
        lctbtnob = findViewById(R.id.locate_map);
        submitob = findViewById(R.id.submit);
        st = findViewById(R.id.camp_st);
        et = findViewById(R.id.camp_et);
        location_str = findViewById(R.id.location_string);
        img = findViewById(R.id.camp_image);
        browse =  findViewById(R.id.img_upload);
        datePickerob = findViewById(R.id.camp_date);

        ngoId = findViewById(R.id.ngo_id);
        ngoName = findViewById(R.id.ngo_name);

        String ngoNamestr,ngoIDstr;
        Bundle bundle;
        Intent getNgoIntent = getIntent();
        ngoNamestr = getNgoIntent.getStringExtra("ngoName");
        ngoIDstr = getNgoIntent.getStringExtra("ngoID");
        bundle = getNgoIntent.getBundleExtra("campBundle");
        ngoId.setText(ngoIDstr);
        ngoName.setText(ngoNamestr);
        double latitude=0.0,longitude=0.0;
        if(bundle!=null) {
            datePickerob.setText(bundle.getString("datePickerob", ""));
            st.setText(bundle.getString("st", ""));
            et.setText(bundle.getString("et", ""));
            idob.setText(bundle.getString("id", ""));
            nameob.setText(bundle.getString("name", ""));
            addressob.setText(bundle.getString("address", ""));
            monoob.setText(bundle.getString("mono", ""));
            taglineob.setText(bundle.getString("tag", ""));
            pinob.setText(bundle.getString("pin", ""));
            location_str.setText(bundle.getString("location_str", ""));
        }
        else{
            latitude = getIntent().getDoubleExtra("a", 0);
            longitude = getIntent().getDoubleExtra("b", 0);

            idob.setText(getIntent().getStringExtra("id"));
            datePickerob.setText(getIntent().getStringExtra("datePickerob"));
            st.setText(getIntent().getStringExtra("st"));
            et.setText(getIntent().getStringExtra("et"));
            nameob.setText(getIntent().getStringExtra("name"));
            addressob.setText(getIntent().getStringExtra("address"));
            monoob.setText(getIntent().getStringExtra("mono"));
            taglineob.setText(getIntent().getStringExtra("tag"));
            pinob.setText(getIntent().getStringExtra("pin"));
//        SharedPreferences map = getSharedPreferences("map", MODE_PRIVATE);
//        latitude = map.getFloat("a", 0);
//        longitude = map.getFloat("b", 0);
            location_str.setText(latitude +","+ longitude);
        }
        SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
        wow = sharedPreferences.getString("value","sample_userid");
        useridob.setText(String.valueOf(wow));

        lctbtnob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapintent = new Intent(getApplicationContext(),MapActivity.class);

                mapintent.putExtra("id", idob.getText().toString().trim());
                mapintent.putExtra("name", nameob.getText().toString().trim());
                mapintent.putExtra("address", addressob.getText().toString().trim());
                mapintent.putExtra("pin", pinob.getText().toString().trim());
                mapintent.putExtra("tag", taglineob.getText().toString().trim());
                mapintent.putExtra("st", st.getText().toString().trim());
                mapintent.putExtra("et", et.getText().toString().trim());
                mapintent.putExtra("mono", monoob.getText().toString().trim());
                mapintent.putExtra("location_str", location_str.getText().toString().trim());
                mapintent.putExtra("datePickerob", datePickerob.getText().toString().trim());
                mapintent.putExtra("ngoID", ngoId.getText().toString().trim());
                mapintent.putExtra("ngoName", ngoName.getText().toString().trim());

                startActivity(mapintent);
            }
        });

        Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate();
            }
            private void updateDate() {
                String myFormat = "dd/MM/yy"; //put your date format in which you need to display
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

                edtdate =  sdf.format(myCalendar.getTime());
                datePickerob.setText(sdf.format(myCalendar.getTime()));
            }
        };

        datePickerob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateCamp.this,
                        date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(CreateCamp.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent=new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent,"Select Image File"),1);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog1 = new TimePickerDialog(CreateCamp.this, new TimePickerDialog.OnTimeSetListener() {
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
                TimePickerDialog dialog2 = new TimePickerDialog(CreateCamp.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        et.setText(hourOfDay +":"+ minute);
                    }
                }, 15, 00, true);
                dialog2.show();
            }
        });

        submitob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadtofirebase();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cc = new Intent(CreateCamp.this,CampActivity.class);
                startActivity(cc);
            }
        });
    }

    private void uploadtofirebase() {

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Info Uploader");
        dialog.setCancelable(false);
        dialog.show();

        idob = findViewById(R.id.camp_ID);
        nameob = findViewById(R.id.camp_name);
        addressob = findViewById(R.id.camp_address);
        monoob = findViewById(R.id.camp_mono);
        taglineob = findViewById(R.id.camp_tag);
        pinob = findViewById(R.id.camp_pin);
        useridob = findViewById(R.id.camp_username);
        lctbtnob = findViewById(R.id.locate_map);
        submitob = findViewById(R.id.submit);
        pinob = findViewById(R.id.camp_pin);
        st = findViewById(R.id.camp_st);
        et = findViewById(R.id.camp_et);
        location_str = findViewById(R.id.location_string);
        img = findViewById(R.id.camp_image);
        browse =  findViewById(R.id.img_upload);

        datePickerob = findViewById(R.id.camp_date);


        date = datePickerob.getText().toString();
        time = String.valueOf(String.valueOf(st.getText().toString()) + " To " + String.valueOf(et.getText().toString()));
        map = String.valueOf(location_str.getText());

        id = String.valueOf(idob.getText());
        name = String.valueOf(nameob.getText());
        address = String.valueOf(addressob.getText());
        mono = String.valueOf(monoob.getText());
        tag = String.valueOf(taglineob.getText());
        pin = String.valueOf(pinob.getText());
        userid = String.valueOf(useridob.getText());


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        storageReference= FirebaseStorage.getInstance().getReference();

        databaseReference.child("camp").child("not_approved").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(id)) {
                    Toast.makeText(getApplicationContext(), "Campaign Request is already registered",
                            Toast.LENGTH_SHORT).show();
                } else {

                    databaseReference.child("camp").child("not_approved").child(id).child("id").setValue(id);
                    databaseReference.child("camp").child("not_approved").child(id).child("name").setValue(name);
                    databaseReference.child("camp").child("not_approved").child(id).child("address").setValue(address);
                    databaseReference.child("camp").child("not_approved").child(id).child("mono").setValue(mono);
                    databaseReference.child("camp").child("not_approved").child(id).child("date").setValue(date);
                    databaseReference.child("camp").child("not_approved").child(id).child("map").setValue(map);
                    databaseReference.child("camp").child("not_approved").child(id).child("pin").setValue(pin);

                    databaseReference.child("camp").child("not_approved").child(id).child("tag").setValue(tag);
                    databaseReference.child("camp").child("not_approved").child(id).child("userid").setValue(userid);
                    databaseReference.child("camp").child("not_approved").child(id).child("time").setValue(time);
                    databaseReference.child("camp").child("not_approved").child(id).child("NGO_Id").setValue(ngoId.getText().toString().trim());

                    StorageReference reference = storageReference.child("camp_request_images/"+System.currentTimeMillis()+".jpeg");
                    reference.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //  uristr = String.valueOf(uri);
                                    uristr = uri.toString();
                                    databaseReference.child("camp").child("not_approved").child(id).child("image").setValue(uristr);
                                    dialog.dismiss();

                                    idob.setText("");
                                    nameob.setText("");
                                    addressob.setText("");
                                    pinob.setText("");
                                    monoob.setText("");
                                    taglineob.setText("");
                                    location_str.setText("");
                                    datePickerob.setText("");
                                    st.setText("");
                                    et.setText("");
                                    ngoId.setText("");
                                    ngoName.setText("");
                                    img.setImageBitmap(null);
                                    filepath =null;

                                    Snackbar.make(linearLayout
                                            , "Campaign Request Registered Successfully"
                                            ,Snackbar.LENGTH_LONG ).show();
//                                    Toast.makeText(CreateCamp.this,
//                                            "Campaign Request Registered Successfully",
//                                            Toast.LENGTH_LONG);
                                }
                            });
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            float percent=(100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                            dialog.setMessage("Uploaded: "+(int)percent+" %");
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if(requestCode==1  && resultCode==RESULT_OK)
        {
            filepath=data.getData();
            try{
                InputStream inputStream=getContentResolver().openInputStream(filepath);
                bitmap= BitmapFactory.decodeStream(inputStream);
                img.setImageBitmap(bitmap);
            }catch (Exception ex)
            {

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void selectNGO(View view) {
        
        Intent gotoNGOintent = new Intent(getApplicationContext(),ngoActivity.class);
        gotoNGOintent.putExtra("id", idob.getText().toString().trim());
        gotoNGOintent.putExtra("name", nameob.getText().toString().trim());
        gotoNGOintent.putExtra("address", addressob.getText().toString().trim());
        gotoNGOintent.putExtra("pin", pinob.getText().toString().trim());
        gotoNGOintent.putExtra("tag", taglineob.getText().toString().trim());
        gotoNGOintent.putExtra("st", st.getText().toString().trim());
        gotoNGOintent.putExtra("et", et.getText().toString().trim());
        gotoNGOintent.putExtra("mono", monoob.getText().toString().trim());
        gotoNGOintent.putExtra("location_str", location_str.getText().toString().trim());
        gotoNGOintent.putExtra("datePickerob", datePickerob.getText().toString().trim());
        startActivity(gotoNGOintent);

    }
}