package com.example.wavezcellular.activities;

import static com.example.wavezcellular.utils.User.generateGuest;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wavezcellular.R;
import com.example.wavezcellular.utils.User;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserActivityUpgrade extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profile_IMG_back,profile_IMG_picture,profile_IMG_mail,profile_IMG_location,profile_IMG_favorite;
    private TextView profile_TXT_name,profile_TXT_mail,profile_TXT_location,profile_TXT_favorite;
    private MaterialButton profile_BTN_change,profile_BTN_signout;


    private ActivityResultLauncher<String> pickImageLauncher;
    //fireBase
    private FirebaseUser firebaseUser;
    private DatabaseReference myRef;
    private String userID;
    private User user;
    private Bundle bundle;
    private DatabaseReference userPhotoRef;

    private ValueEventListener photoValueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_upgrade);

        bundle = getIntent().getExtras();
        if (bundle == null) {
            bundle = new Bundle();
        }
        findViews();
        createListener();
        getCurrentUsersData();



    }

    private void createListener() {

        profile_IMG_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserActivityUpgrade.this, HomeActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

        profile_IMG_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto(v);
            }
        });

        profile_BTN_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(UserActivityUpgrade.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        profile_BTN_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setTitle("Change Your Name:");
                final EditText input = new EditText(view.getContext());
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        user.setName(input.getText().toString());
                        saveData();
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });
                alert.show();

            }
        });
    }

    public void getCurrentUsersData() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            String guest = generateGuest();
            user = new User(guest, "No Email Available");
            setUserInfo();
        } else {
            myRef = FirebaseDatabase.getInstance().getReference("Users");
            userID = firebaseUser.getUid();
            userPhotoRef = myRef.child(userID).child("Photo");
            pickImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    // The URI of the selected image is available here
                    savePhotoForUser(uri);
                }
            });
            // Register the ValueEventListener to retrieve the photo URL from the database
            photoValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String photoUrl = dataSnapshot.getValue(String.class);
                    if (photoUrl != null && URLUtil.isValidUrl(photoUrl)) {
                        loadImage(photoUrl);
                    } else {
                        // Set a default placeholder image if no photo is available
                        profile_IMG_picture.setImageResource(R.drawable.ic_user);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any database error during the retrieval
                }
            };
            myRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    user = dataSnapshot.getValue(User.class);
                    setUserInfo();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    public void saveData() {
        if (!user.getName().contains("guest")) {
            FirebaseDatabase.getInstance().getReference("Users")
                    .child(userID)
                    .setValue(user);
            setUserInfo();
        }
    }

    private void setUserInfo() {
        profile_TXT_name.setText(user.getName());
        profile_TXT_mail.setText(user.getEmail());
    }

    // Method to load the photo from the URL and display it in the ImageView
    private void loadImage(String photoUrl) {
        Glide.with(UserActivityUpgrade.this)
                .load(photoUrl)
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_error)
                .into(profile_IMG_picture);
    }

    private void displayUserPhoto() {
        userPhotoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String photoUrl = dataSnapshot.getValue(String.class);
                if (photoUrl != null) {
                    // Load the photo into the ImageView using a Glide library
                    Glide.with(UserActivityUpgrade.this)
                            .load(photoUrl)
                            .placeholder(R.drawable.ic_user)
                            .error(R.drawable.ic_error)
                            .into(profile_IMG_picture);
                } else {
                    // Set a default placeholder image if no photo is available
                    profile_IMG_picture.setImageResource(R.drawable.ic_user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any database error during the retrieval
            }
        });
    }



    private void savePhotoForUser(Uri imageUri) {
        userPhotoRef.setValue(imageUri.toString())
                .addOnSuccessListener(aVoid -> {
                    // Photo reference saved successfully in the database
                })
                .addOnFailureListener(e -> {
                    // Error occurred while saving photo reference in the database
                });
    }



    public void choosePhoto(View view) {
        pickImageLauncher.launch("image/*");
    }

    private void findViews() {
        profile_IMG_back = findViewById(R.id.profile_IMG_back);
        profile_IMG_picture = findViewById(R.id.profile_IMG_picture);
        profile_IMG_mail = findViewById(R.id.profile_IMG_mail);
        profile_IMG_location = findViewById(R.id.profile_IMG_location);
        profile_IMG_favorite = findViewById(R.id.profile_IMG_favorite);

        profile_TXT_name = findViewById(R.id.profile_TXT_name);
        profile_TXT_mail = findViewById(R.id.profile_TXT_mail);
        profile_TXT_location = findViewById(R.id.profile_TXT_location);
        profile_TXT_favorite = findViewById(R.id.profile_TXT_favorite);

        profile_BTN_change = findViewById(R.id.profile_BTN_change);
        profile_BTN_signout = findViewById(R.id.profile_BTN_signout);
    }

    // Override onResume() to start listening for changes in the photo URL when the activity is resumed
    @Override
    protected void onResume() {
        super.onResume();
        userPhotoRef.addValueEventListener(photoValueEventListener);
    }


    // Override onPause() to stop listening for changes in the photo URL when the activity is paused
    @Override
    protected void onPause() {
        super.onPause();
        userPhotoRef.removeEventListener(photoValueEventListener);
    }
}