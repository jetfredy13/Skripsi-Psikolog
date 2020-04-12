package com.example.psikologku_psikolog.Chat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.psikologku_psikolog.JadwalKonseling;
import com.example.psikologku_psikolog.MainActivity;
import com.example.psikologku_psikolog.MessageListAdapter;
import com.example.psikologku_psikolog.PsikologFragments.MainPsikologFragment;
import com.example.psikologku_psikolog.R;
import com.example.psikologku_psikolog.SesiKonseling;
import com.example.psikologku_psikolog.User;
import com.example.psikologku_psikolog.midtrans_demo;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UserKonseling extends AppCompatActivity {
    ImageButton btnSend,attach_file;
    private RecyclerView recView;
    private MessageListAdapter mlAdapter;
    ImageView user_profile;
    TextView nama_user;
    long t;
    User user;
    ArrayList<UserMessage> listMessage;
    EditText message;
    Date curTime;
    Intent intent;
    DatabaseReference ref;
    SharedPreferences sp;
    private static final int AUDIO_REQUEST = 1;
    JadwalKonseling jadwalKonseling;
    private Uri audioUrl;
    private StorageTask uploadTask;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    private SesiKonseling sesiKonseling;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_konseling);
        message = findViewById(R.id.text_send);
        curTime =  new Date();
        user = (User) getIntent().getSerializableExtra("User");
        nama_user = findViewById(R.id.nama_user);
        user_profile = findViewById(R.id.user_profile);
        attach_file = findViewById(R.id.attachment);
        intent = getIntent();
        sesiKonseling = (SesiKonseling) getIntent().getSerializableExtra("Sesi");
        Toolbar tool = findViewById(R.id.toolbar);
        setSupportActionBar(tool);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tool.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        recView = findViewById(R.id.rec_view_chat);
        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        final String id = sp.getString("id","");
        //currUser = sp.getString("username","");
        LinearLayoutManager lm = new LinearLayoutManager(UserKonseling.this);
        lm.setStackFromEnd(true);
        recView.setHasFixedSize(true);
        recView.setLayoutManager(lm);
        ref = FirebaseDatabase.getInstance().getReference("User").child(user.getId());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    listMessage.add(ds.child("Konseling").getValue(UserMessage.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        nama_user.setText(user.getNama());
        btnSend = findViewById(R.id.btn_send);
        attach_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadSound();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               sendChat(user.getId(),id,message.getText().toString(),"text");
            }
        });
        loadChat(user.getId(),id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //beri pengecekkan sudah muncul bila jam nya sudah melebihi kasih pengecekkan tanggal dan jam
        getMenuInflater().inflate(R.menu.menu_chat,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull final MenuItem item) {
        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        final String id = sp.getString("id","");
        final String key = user.getId()+":"+id;
        //ref = FirebaseDatabase.getInstance().getReference("Sesi Konseling").child(key);
        //getSesiKonseling(key);
        switch (item.getItemId()){
                case R.id.selesai_konsultasi:
                    FirebaseDatabase.getInstance().getReference("Sesi Konseling").child(key)
                            .child("Jadwal Sesi").child(sesiKonseling.getSesi_sekarang())
                            .child("status").setValue("Selesai");
                    /*
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            sesiKonseling = new SesiKonseling();
                            sesiKonseling = dataSnapshot.getValue(SesiKonseling.class);
                            String sesi_sekarang = sesiKonseling.getSesi_sekarang();
                            jadwalKonseling = dataSnapshot.child("Jadwal Sesi").child(sesi_sekarang).getValue(JadwalKonseling.class);
                            if(dataSnapshot.child("Jadwal Sesi").child(sesiKonseling.getSesi_sekarang()
                            ).child("status").getValue(String.class).equals("Belum"))
                            {
                                Calendar c = Calendar.getInstance();
                                String[] waktu = jadwalKonseling.getJam().split(":");
                                String[] tanggal = jadwalKonseling.getTanggal().split("-");
                                int jam  = Integer.parseInt(waktu[0]);
                                int menit = Integer.parseInt(waktu[1]);
                                int hari = Integer.parseInt(tanggal[0]);
                                int bulan = Integer.parseInt(tanggal[1]);
                                int tahun = Integer.parseInt(tanggal[2]);
                                System.out.println("Calendar Tanggal : "+ c.get(Calendar.DATE) +
                                        "Bulan : " + c.get(Calendar.MONTH+1)  +" Tahun : " +  c.get(Calendar.YEAR));
                                System.out.println("Database Tanggal : "+ hari + "Bulan :"+ bulan + "Tahun :"+tahun );
                               /* if(c.get(Calendar.DATE)== hari && c.get(Calendar.MONTH)+1 == bulan && c.get(Calendar.YEAR)== tahun  )
                                {

                                    if(c.get(Calendar.HOUR_OF_DAY) > jam || (c.get(Calendar.HOUR_OF_DAY) == jam && c.get(Calendar.MINUTE)>= menit ))
                                    {*/

                                    //}

                                //}

                            /*}

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });*/

                break;
            }

        return super.onOptionsItemSelected(item);
    }
    private void uploadSound()
    {
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,AUDIO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AUDIO_REQUEST && resultCode == RESULT_OK && data != null && data .getData() !=null){
            audioUrl = data.getData();
            if(uploadTask!=null && uploadTask.isInProgress())
            {
                Toast.makeText(UserKonseling.this,"Upload in progress",Toast.LENGTH_SHORT).show();
            }
            else{
                uploadVoiceMail();
            }
        }
    }
    private void uploadVoiceMail() {
        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        final String id = sp.getString("id","");
        final ProgressDialog pd = new ProgressDialog(UserKonseling.this);
        pd.setMessage("Uploading Audio ...");
        pd.show();
        if(audioUrl != null){
            final StorageReference stReference = FirebaseStorage.getInstance().getReference("Voice Mail").child(System.currentTimeMillis()+":"+id);
            /*stReference.putFile(audioUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    pd.dismiss();
                    Toast.makeText(UserKonseling.this,"Uploading Finished",Toast.LENGTH_SHORT).show();
                }
            });*/
            uploadTask = stReference.putFile(audioUrl);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task <UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw  task.getException();
                    }
                    return stReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();
                        sendChat(user.getId(),id,mUri,"audio");
                        //reference = FirebaseDatabase.getInstance().getReference("Psikolog").child(id);
                        //HashMap<String,Object> map = new HashMap<>();
                        //map.put("image_url",mUri);
                        //reference.updateChildren(map);
                        pd.dismiss();
                    }
                    else{
                        Toast.makeText(UserKonseling.this,"Failed !",Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UserKonseling.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } else{
            Toast.makeText(UserKonseling.this,"No Audio Selected",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onBackPressed() {
        UserKonseling.this.finish();
        super.onBackPressed();

    }

    /*
        private void getSesiKonseling(String key)
        {
            ref = FirebaseDatabase.getInstance().getReference("Sesi Konseling").child(key);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    sesiKonseling = new SesiKonseling();
                    sesiKonseling = dataSnapshot.getValue(SesiKonseling.class);
                    System.out.println("Isi Sesi Konseling "+ dataSnapshot);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }*/
    public void sendChat(String user,String currUser,String text,String type){
        t = curTime.getTime();
        ref = FirebaseDatabase.getInstance().getReference().child("Konseling");
        UserMessage msg = new UserMessage(currUser,text,user,t,type);
        ref.push().setValue(msg);
        message.setText("");
    }
    public void loadChat(final String currUser, final String psikolog){
        listMessage = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference("Konseling");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listMessage.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    UserMessage msg = ds.getValue(UserMessage.class);
                    if(msg.getCurentUser().equals(currUser) && msg.getSended().equals(psikolog) ||
                    msg.getCurentUser().equals(psikolog) && msg.getSended().equals(currUser)){
                        listMessage.add(msg);
                    }
                    mlAdapter = new MessageListAdapter(getApplicationContext(),listMessage,"");
                    recView.setAdapter(mlAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
