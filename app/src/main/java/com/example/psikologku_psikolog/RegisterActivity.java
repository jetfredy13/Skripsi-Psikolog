package com.example.psikologku_psikolog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final int RESULT_LOAD_IMAGE1 = 1;
    EditText etName, etEmail, etPassword, etConfirmpass, etNik,etDate;
    Button btnRegister, btnUpload;
    TextView tvGotoLogin;
    Psikolog psikolog;
    DatabaseReference ref;
    Spinner spbidang;
    FirebaseStorage storage;
    StorageReference storageReference;
    private Uri FilePath;
    private List<String> fileNameList;
    private boolean multiple_image;
    private int total;
    private String bidang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etName = findViewById(R.id.etNama);
        etEmail = findViewById(R.id.etEmail);
        btnUpload = findViewById(R.id.btnUpload);
        spbidang = findViewById(R.id.spinner_bidang);
        etPassword = findViewById(R.id.etPassword);
        etConfirmpass = findViewById(R.id.etConfirm);
        etNik = findViewById(R.id.etNik);
        etDate = findViewById(R.id.etDate);
        fileNameList = new ArrayList<>();
        multiple_image = false;
        btnRegister = findViewById(R.id.btnRegister);
        tvGotoLogin = findViewById(R.id.tvLogin);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("File Psikolog");
        psikolog = new Psikolog();
        ArrayAdapter<CharSequence> spadapter = ArrayAdapter.createFromResource(this,R.array.bidang,android.R.layout.simple_spinner_item);
        spadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spbidang.setAdapter(spadapter);
        spbidang.setOnItemSelectedListener(this);
        ref = FirebaseDatabase.getInstance().getReference().child("Psikolog");
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pesan = "";
                if (etName.equals("") || etEmail.equals("") || etPassword.equals("") || etNik.equals("")) {
                    pesan = "Ada data yang masih kosong";
                } else if (!etPassword.getText().toString().equals(etConfirmpass.getText().toString())) {
                    pesan = "Password dan confirm password harus sama";
                } else {
                    if(multiple_image)
                    {
                        psikolog.setNama(etName.getText().toString());
                        psikolog.setEmail(etEmail.getText().toString());
                        psikolog.setPassword(etPassword.getText().toString());
                        psikolog.setNIK(etNik.getText().toString());
                        psikolog.setBidang(bidang);
                        psikolog.setTanggal_berakhir(etDate.getText().toString());
                        psikolog.setImage_url("default");
                        psikolog.setTipeUser(0);
                        ref.push().setValue(psikolog);
                        pesan = "Berhasil Register";
                    }
                    else{
                        if(UploadImage() && !bidang.equals(""))
                        {
                            psikolog.setNama(etName.getText().toString());
                            psikolog.setEmail(etEmail.getText().toString());
                            psikolog.setPassword(etPassword.getText().toString());
                            psikolog.setNIK(etNik.getText().toString());
                            psikolog.setBidang(bidang);
                            psikolog.setTanggal_berakhir(etDate.getText().toString());
                            psikolog.setTipeUser(0);
                            ref.push().setValue(psikolog);
                            pesan = "Berhasil Register";
                        }
                    }

                }
                if (!pesan.equals("")) {
                    Toast.makeText(RegisterActivity.this, pesan, Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseImage();
            }
        });
        tvGotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(RegisterActivity.this, MainActivity.class);
                finish();
                startActivity(loginIntent);
            }
        });
    }
    private void ChooseImage () {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE1);
    }

    private boolean UploadImage(){
        boolean sukses = false;
        if(FilePath != null)
        {
            final ProgressDialog pg = new ProgressDialog(this);
            pg.setTitle("Uploading ...");
            pg.show();
            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(FilePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    pg.dismiss();
                    Toast.makeText(RegisterActivity.this,"Uploaded",Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pg.dismiss();
                    Toast.makeText(RegisterActivity.this,"Failed"+ e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    pg.setMessage("Uploading "+(int)progress+"%");
                }
            });
            sukses = true;
        }
        else{
            Toast.makeText(RegisterActivity.this,"Harus mengirimkan file-file yang diperlukan terlebih dahulu",Toast.LENGTH_SHORT).show();
            sukses = false;
        }
        return sukses;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE1 && resultCode == RESULT_OK)
        {
            if(data != null && data.getClipData() != null)
            {
                multiple_image = true;
                final ProgressDialog pg = new ProgressDialog(this);
                pg.setTitle("Uploading ...");
                pg.show();
                total = data.getClipData().getItemCount();
                for(int i=0 ; i< total ; i++)
                {
                    Uri fileUri = data.getClipData().getItemAt(i).getUri();
                    String filename = getFileName(fileUri);
                    fileNameList.add(filename);
                    StorageReference fileToUpload = storageReference.child("Images").child(filename);
                    fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pg.dismiss();
                            Toast.makeText(RegisterActivity.this,"Upload Success",Toast.LENGTH_SHORT);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pg.dismiss();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            pg.setMessage("Uploading "+(int)progress+"%");
                        }
                    });
                }
            }
            else if(data != null && data.getData() != null) {
                multiple_image = false;
                FilePath = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePath);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public String getFileName(Uri uri)
    {
        String result = null;
        if(uri.getScheme().equals("content")){
            Cursor cursor = getContentResolver().query(uri,null,null,null,null,null);
            try {
                if(cursor != null && cursor.moveToFirst()){
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if(result == null){
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if(cut != -1){
                result = result.substring(cut+1);
            }
        }
        return result;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        bidang = parent.getItemAtPosition(i).toString();
        Toast.makeText(RegisterActivity.this,bidang,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}



