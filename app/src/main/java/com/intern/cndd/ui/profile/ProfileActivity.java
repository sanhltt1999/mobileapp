package com.intern.cndd.ui.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.intern.cndd.R;
import com.intern.cndd.model.Orders;
import com.intern.cndd.prevalent.Prevalent;
import com.intern.cndd.ui.shipping.ShippingActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMG = 1;

    private ImageView mAvatarImageView;
    private EditText mNameEditText;
    private EditText mAddressEditText;
    private EditText mPhoneEditText;
    private Button mUpdateButton;
    private ProgressDialog loadingBar;
    private String saveCurrentDate, saveCurrentTime, randomKey, downloadImageUrl;
    private StorageReference ProductImageRef;
    private DatabaseReference ProductRef;
    private Uri ImageUri;
    private Orders mOrders = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAvatarImageView = findViewById(R.id.avatarImageView);
        mNameEditText = findViewById(R.id.nameEditText);
        mAddressEditText = findViewById(R.id.addressEditText);
        mPhoneEditText = findViewById(R.id.phoneEditText);
        mUpdateButton = findViewById(R.id.updateButton);
        loadingBar = new ProgressDialog(this);

        ProductImageRef = FirebaseStorage.getInstance().getReference().child("Profile Image");
        ProductRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getId());


        Glide.with(this)
                .load(Prevalent.currentOnlineUser.getImage())
                .centerCrop()
                .into(mAvatarImageView);

        mNameEditText.setText(Prevalent.currentOnlineUser.getName());
        mAddressEditText.setText(Prevalent.currentOnlineUser.getAddress());
        mPhoneEditText.setText(Prevalent.currentOnlineUser.getPhone());

        mAvatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });

        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mNameEditText.getText().toString().trim();
                String address = mAddressEditText.getText().toString().trim();
                String phone = mPhoneEditText.getText().toString().trim();

                if (name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(ProfileActivity.this, "Please enter your information!", Toast.LENGTH_SHORT).show();
                } else {

                    try {
                        mOrders = (Orders) getIntent().getExtras().getSerializable(ShippingActivity.SHIP_KEY);
                        updateOrder();
                    } catch (Exception e) {
                        Log.d("Bug", e.toString());
                    }


                    if (ImageUri == null) {
                        downloadImageUrl = Prevalent.currentOnlineUser.getImage();
                        SaveInfoToDatabase();
                    } else {
                        StoreInformation();
                    }

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {

            if (requestCode == RESULT_LOAD_IMG) {
                ImageUri = data.getData();

                Glide.with(this)
                        .asBitmap()
                        .load(ImageUri)
                        .centerCrop()
                        .into(mAvatarImageView);
            }
        }
    }

    private void StoreInformation() {
        loadingBar.setTitle("Update");
        loadingBar.setMessage("Please wait!");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        randomKey = saveCurrentDate + saveCurrentTime;


        final StorageReference filePath = ProductImageRef.child(ImageUri.getLastPathSegment() + randomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(ProfileActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(ProfileActivity.this, "Profile Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(ProfileActivity.this, "got the Profile image Url Successfully...", Toast.LENGTH_SHORT).show();

                            SaveInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void updateOrder() {

        HashMap<String, Object> order = new HashMap<>();

        order.put("name", mNameEditText.getText().toString().trim());
        order.put("phone", mPhoneEditText.getText().toString().trim());
        order.put("address", mAddressEditText.getText().toString().trim());

        final DatabaseReference shipRef =  FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(Prevalent.currentOnlineUser.getId());

        shipRef.updateChildren(order).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(ProfileActivity.this, "Complete", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void SaveInfoToDatabase() {
        HashMap<String, Object> user = new HashMap<>();
        user.put("id", Prevalent.currentOnlineUser.getId());
        user.put("phone", mPhoneEditText.getText().toString().trim());
        user.put("password", Prevalent.currentOnlineUser.getPassword());
        user.put("name", mNameEditText.getText().toString().trim());
        user.put("address", mAddressEditText.getText().toString().trim());
        user.put("image", downloadImageUrl);

        ProductRef.updateChildren(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            loadingBar.dismiss();
                            Toast.makeText(ProfileActivity.this, "Update Successfully...", Toast.LENGTH_SHORT).show();
                            Prevalent.currentOnlineUser.setPhone(mPhoneEditText.getText().toString().trim());
                            Prevalent.currentOnlineUser.setName(mNameEditText.getText().toString().trim());
                            Prevalent.currentOnlineUser.setAddress(mAddressEditText.getText().toString().trim());
                            Prevalent.currentOnlineUser.setImage(downloadImageUrl);

                        } else {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(ProfileActivity.this, "Error " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


}