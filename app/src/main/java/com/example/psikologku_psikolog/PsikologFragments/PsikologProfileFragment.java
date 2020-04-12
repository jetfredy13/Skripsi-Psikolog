package com.example.psikologku_psikolog.PsikologFragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.psikologku_psikolog.Konsultasi.KonsultasiHarian;
import com.example.psikologku_psikolog.Konsultasi.KonsultasiHarianAdapter;
import com.example.psikologku_psikolog.Psikolog;
import com.example.psikologku_psikolog.R;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class PsikologProfileFragment extends Fragment {

    ImageView image_profile;
    TextView nama_user;
    Button btnJadwal;
    DatabaseReference reference;
    SharedPreferences sp;
    EditText dateTime,jam_awal,jam_akhir;
    Spinner spinner_hari;
    StorageReference storageReference;
    private RecyclerView rec_view_jadwal_harian;
    private KonsultasiHarianAdapter konsultasiAdapter;
    private List<KonsultasiHarian> ListkonsultasiHarian;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUrl;
    private String id;
    private StorageTask uploadTask;
    public PsikologProfileFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_psikolog_profile,container,false);
        image_profile = view.findViewById(R.id.profile_psikolog);
        nama_user = view.findViewById(R.id.nama_user);
        dateTime = view.findViewById(R.id.tanggal_jadwal);
        jam_awal = view.findViewById(R.id.jam_awal_konsultasi);
        jam_akhir = view.findViewById(R.id.jam_akhir_konsultasi);
        spinner_hari = view.findViewById(R.id.spinner_hari);
        rec_view_jadwal_harian = view.findViewById(R.id.jadwal_harian);
        btnJadwal = view.findViewById(R.id.set_jadwal_konseling);
        ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(this.getActivity(),R.array.hari,android.R.layout.simple_spinner_item);
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_hari.setAdapter(spAdapter);
        rec_view_jadwal_harian.setHasFixedSize(true);
        rec_view_jadwal_harian.setLayoutManager(new LinearLayoutManager(getActivity()));
        storageReference = FirebaseStorage.getInstance().getReference("Profile Psikolog");
        sp = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        id = sp.getString("id","");
        //Toast.makeText(getContext(),id,Toast.LENGTH_SHORT).show();
        getJadwalHarian();
        reference = FirebaseDatabase.getInstance().getReference("Psikolog").child(id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Psikolog psikolog = dataSnapshot.getValue(Psikolog.class);
                nama_user.setText(psikolog.getNama());
                if(psikolog.getImage_url().equals("default")){
                    image_profile.setImageResource(R.mipmap.ic_launcher);
                } else{
                    Glide.with(getContext()).load(psikolog.getImage_url()).into(image_profile);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btnJadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(!jam_awal.getText().toString().equals("") && !jam_akhir.getText().toString().equals(""))
                {
                    reference = FirebaseDatabase.getInstance().getReference("Psikolog").child(id).child("KonsultasiHarian");
                    KonsultasiHarian konsultasiHarian = new KonsultasiHarian();
                    konsultasiHarian.setHari(spinner_hari.getSelectedItem().toString());
                    konsultasiHarian.setJam_awal(jam_awal.getText().toString());
                    konsultasiHarian.setJam_akhir(jam_akhir.getText().toString());
                    reference.child(spinner_hari.getSelectedItemPosition()+1+"").setValue(konsultasiHarian);
                }

            }
        });
        image_profile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                openImage();
            }
        });
        dateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDateTime();
            }
        });
        jam_awal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTimeAwal();
            }
        });
        jam_akhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTimeAkhir();
            }
        });


        return view;
    }
    private void openImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }
    private void setTimeAwal(){
        Calendar c = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                String waktu_mulai ="";
                if(minute<=9)
                {
                    waktu_mulai = hour+":0"+minute;
                }
                else{
                    waktu_mulai = hour+":"+minute;
                }
                jam_awal.setText(waktu_mulai);
            }
        },c.get(Calendar.HOUR),c.get(Calendar.MINUTE),true);
        timePickerDialog.show();
    }

    private void setTimeAkhir(){
        Calendar c = Calendar.getInstance();
        final TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                String[] waktu_awal = jam_awal.getText().toString().split(":");
                String waktu_selesai = "";
                if(jam_awal.getText().toString().equals(""))
                {
                    Toast.makeText(getContext(),"Tolong isi jam awal terlebih dahulu",Toast.LENGTH_SHORT).show();
                }
                else if(Integer.parseInt(waktu_awal[0])>=hour){
                    Toast.makeText(getContext(),"Jam berakhir tidak boleh sebelum jam awal",Toast.LENGTH_SHORT).show();
                }
                else{
                        if(minute<=9)
                        {
                            waktu_selesai = hour+":0"+minute;
                        }
                        else{
                            waktu_selesai = hour+":"+minute;
                        }
                        jam_akhir.setText(waktu_selesai);
                }

            }
        },c.get(Calendar.HOUR),c.get(Calendar.MINUTE),true);
        timePickerDialog.show();
    }
    private void setDateTime(){
        Calendar c = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                datePicker.setMinDate(System.currentTimeMillis()-1000);
            }
        }
        ,c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DATE));
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        datePickerDialog.show();
    }
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void getJadwalHarian()
    {
        ListkonsultasiHarian = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Psikolog").child(id).child("KonsultasiHarian");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    KonsultasiHarian konsultasiHarian = snapshot.getValue(KonsultasiHarian.class);
                    ListkonsultasiHarian.add(konsultasiHarian);
                }
                konsultasiAdapter = new KonsultasiHarianAdapter(ListkonsultasiHarian,getContext());
                rec_view_jadwal_harian.setAdapter(konsultasiAdapter);
                konsultasiAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void uploadImage()
    {
        sp = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        final String id = sp.getString("id","");
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Uploading");
        pd.show();
        if(imageUrl != null){
            final StorageReference stReference = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageUrl));
            uploadTask = stReference.putFile(imageUrl);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
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
                        reference = FirebaseDatabase.getInstance().getReference("Psikolog").child(id);
                        HashMap<String,Object> map = new HashMap<>();
                        map.put("image_url",mUri);
                        reference.updateChildren(map);
                        pd.dismiss();
                    }
                    else{
                        Toast.makeText(getContext(),"Failed !",Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } else{
            Toast.makeText(getContext(),"No Image Selected",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data !=null && data.getData() !=null){
            imageUrl = data.getData();
            if(uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(getContext(),"Upload in progress",Toast.LENGTH_SHORT).show();
            }else{
                uploadImage();
            }
        }
    }
}
