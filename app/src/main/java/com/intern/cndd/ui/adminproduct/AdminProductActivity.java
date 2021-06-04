package com.intern.cndd.ui.adminproduct;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.intern.cndd.ui.adminhome.AdminHomeActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminProductActivity extends AppCompatActivity {

    private String mCategoryName, mDescription, mPrice, mName, saveCurrentDate, saveCurrentTime;
    private Button mAddNewProductButton;
    private ImageView mInputProductImage;
    private EditText mInputProductName;
    private EditText mInputProductDescription;
    private EditText mInputProductPrice;
    private ProgressDialog loadingBar;

    private static final int GalleryPick = 1;
    private Uri mImageUri;
    private String productRandomKey, downloadImageUrl;
    private StorageReference mProductImageRef;
    private DatabaseReference mProductRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product);

        mCategoryName = getIntent().getExtras().get(AdminHomeActivity.CATEGORY_KEY).toString();
        mProductImageRef = FirebaseStorage.getInstance().getReference().child("Product Image");
        mProductRef = FirebaseDatabase.getInstance().getReference().child("Products");

        mAddNewProductButton = findViewById(R.id.add_new_product);
        mInputProductName = findViewById(R.id.product_name);
        mInputProductImage = findViewById(R.id.select_product_image);
        mInputProductDescription = findViewById(R.id.product_description);
        mInputProductPrice = findViewById(R.id.product_price);
        loadingBar = new ProgressDialog(this);

        mInputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        mAddNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });


    }

    private void ValidateProductData() {
        mDescription = mInputProductDescription.getText().toString();
        mPrice = mInputProductPrice.getText().toString();
        mName = mInputProductName.getText().toString();

        if (mImageUri == null) {
            Toast.makeText(this, "Product image is mandatory...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(mDescription)) {
            Toast.makeText(this, "Please write product description", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(mPrice)) {
            Toast.makeText(this, "Please write product price", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(mName)) {
            Toast.makeText(this, "Please write product name", Toast.LENGTH_SHORT).show();
        } else {
            StoreProductInformation();
        }
    }


    private void StoreProductInformation() {
        loadingBar.setTitle("Add New Product");
        loadingBar.setMessage("Dear Admin, please wait while we are adding the new product.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;


        final StorageReference filePath = mProductImageRef.child(mImageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(mImageUri);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminProductActivity.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

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

                            Toast.makeText(AdminProductActivity.this, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }


    private void SaveProductInfoToDatabase() {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("id", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", mDescription);
        productMap.put("category", mCategoryName);
        productMap.put("price", mPrice);
        productMap.put("name", mName);
        productMap.put("image", downloadImageUrl);
        productMap.put("total", "1");
        productMap.put("start", "5");


        mProductRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(AdminProductActivity.this, AdminHomeActivity.class);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(AdminProductActivity.this, "Product is added successfully...", Toast.LENGTH_SHORT).show();
                        } else {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminProductActivity.this, "Error " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            mImageUri = data.getData();
            mInputProductImage.setImageURI(mImageUri);
        }
    }

}