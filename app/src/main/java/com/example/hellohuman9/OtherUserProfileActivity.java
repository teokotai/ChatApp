package com.example.hellohuman9;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hellohuman9.adapter.ChatRecyclerAdapter;
import com.example.hellohuman9.model.ChatroomModel;
import com.example.hellohuman9.model.UserModel;
import com.example.hellohuman9.utils.AndroidUtil;
import com.example.hellohuman9.utils.FirebaseUtil;

public class OtherUserProfileActivity extends AppCompatActivity {
    UserModel otherUser;
    ImageView imageView;
    String chatroomId;
    TextView usernameInput;
    TextView phoneInput;
    ImageButton backBtn;
    TextView genderInput;
    TextView ownedPetsInput;
    TextView experienceInput;
    TextView ageInput;
    TextView locationInput;
    TextView descriptionInput;
    TextView positionInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);

        otherUser = AndroidUtil.getUserModelFromIntent(getIntent());
        chatroomId = FirebaseUtil.getChatroomId(FirebaseUtil.currentUserId(),otherUser.getUserId());

        usernameInput = findViewById(R.id.other_username);
        phoneInput = findViewById(R.id.profile_phone);
        genderInput = findViewById(R.id.profile_gender);
        ownedPetsInput = findViewById(R.id.profile_owned_pets);
        experienceInput = findViewById(R.id.profile_experience);
        ageInput = findViewById(R.id.profile_age);
        locationInput = findViewById(R.id.profile_location);
        descriptionInput = findViewById(R.id.profile_description);
        positionInput = findViewById(R.id.profile_position);
        imageView = findViewById(R.id.other_profile_image_view);


        backBtn = findViewById(R.id.back_btn);

        backBtn.setOnClickListener((v) ->{
            onBackPressed();
        });


        usernameInput.setText(otherUser.getUsername());
        phoneInput.setText(otherUser.getPhone());
        genderInput.setText(otherUser.getGender());
        ownedPetsInput.setText(otherUser.getOwnedPets());
        experienceInput.setText(otherUser.getExperience());
        ageInput.setText(String.valueOf(otherUser.getAge()));
        locationInput.setText(otherUser.getLocation());
        descriptionInput.setText(otherUser.getDescription());
        positionInput.setText(otherUser.getPosition());


        FirebaseUtil.getOtherProfilePicStorageRef(otherUser.getUserId()).getDownloadUrl()
                .addOnCompleteListener(t -> {
                    if (t.isSuccessful()){
                        Uri uri = t.getResult();
                        AndroidUtil.setProfilePic(this,uri,imageView);
                    }
                });


        Log.d("GENDER","TOT GENDER: " + otherUser.getGender());

        Log.d("AICI TRANS", getIntent().getExtras().get("name") + "");

    }


}




