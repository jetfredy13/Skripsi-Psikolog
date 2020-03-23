package com.example.psikologku_psikolog.Artikel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.psikologku_psikolog.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class CreateArticle extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE1 = 1;
    Button btnUpload,btnPost;
    EditText etArtikel,etJudul;
    private Uri FilePath;
    Artikel artikel;
    FirebaseStorage storage;
    DatabaseReference ref;
    SharedPreferences sp;
    StorageReference storageReference;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_article);
        btnUpload = findViewById(R.id.btnUpload);
        btnPost = findViewById(R.id.btnPost);
        etArtikel = findViewById(R.id.etArtikel);
        etJudul = findViewById(R.id.etJudul);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("Foto Artikel");
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
        btnPost.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               uploadArtikel();
           }
       });
    }
    private void uploadArtikel(){
        if(!etArtikel.equals("") && !etJudul.equals("")){
            sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            String id = sp.getString("id","");
            ref = FirebaseDatabase.getInstance().getReference().child("Artikel");
            artikel = new Artikel();
            artikel.setJudul(etJudul.getText().toString());
            artikel.setIsi_artikel(etArtikel.getText().toString());
            artikel.setPenulis(id);
            artikel.setTanggal_artikel("2-28-2020");
            //id = ref.push().getKey();
            uploadImage();
            ref.push().setValue(artikel);
            //artikel
        }
    }
    public void chooseImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE1);
    }
    private void uploadImage(){

        if(FilePath != null)
        {
            final ProgressDialog pg = new ProgressDialog(this);
            pg.setTitle("Uploading ...");
            pg.show();
            StorageReference str = storageReference.child("Article/"+ id);
            str.putFile(FilePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    pg.dismiss();
                    Toast.makeText(CreateArticle.this,"Uploaded",Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pg.dismiss();
                    Toast.makeText(CreateArticle.this,"Failed"+ e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    pg.setMessage("Uploading "+(int)progress+"%");
                }
            });
        }
        else{
            Toast.makeText(CreateArticle.this,"Harus mengirimkan file-file yang diperlukan terlebih dahulu",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE1 && resultCode == RESULT_OK){
            if(data != null && data.getData() != null) {
                FilePath = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
           }
        }
    }
}
