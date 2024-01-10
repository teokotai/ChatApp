package com.example.hellohuman9.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.hellohuman9.model.UserModel;

public class AndroidUtil {

    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void passUserModelAsIntent(Intent intent, UserModel model){
        intent.putExtra("username",model.getUsername());
        intent.putExtra("phone",model.getPhone());
        intent.putExtra("userId",model.getUserId());
        intent.putExtra("fcmToken",model.getFcmToken());
        intent.putExtra("checkBoxPetOwner",model.isCheckBoxPetOwner());
        intent.putExtra("checkBoxCaretaker",model.isCheckBoxCaretaker());
        intent.putExtra("gender",model.getGender());
        intent.putExtra("experience",model.getExperience());
        intent.putExtra("details",model.getDescription());
        intent.putExtra("location",model.getLocation());
        intent.putExtra("ownedPets",model.getOwnedPets());
        intent.putExtra("age",model.getAge());
        intent.putExtra("position",model.getPosition());
    }

    public static UserModel getUserModelFromIntent(Intent intent){
        UserModel userModel = new UserModel();
        userModel.setUsername(intent.getStringExtra("username"));
        userModel.setPhone(intent.getStringExtra("phone"));
        userModel.setUserId(intent.getStringExtra("userId"));
        userModel.setFcmToken(intent.getStringExtra("fcmToken"));
        userModel.setGender(intent.getStringExtra("gender"));
        userModel.setExperience(intent.getStringExtra("experience"));
        userModel.setDescription(intent.getStringExtra("details"));
        userModel.setLocation(intent.getStringExtra("location"));
        userModel.setOwnedPets(intent.getStringExtra("ownedPets"));
        userModel.setAge(intent.getIntExtra("age",0));
        userModel.setPosition(intent.getStringExtra("position"));
//        userModel.setCheckBoxPetOwner(intent.getBooleanExtra("checkBoxPetOwner", true));
//        userModel.setCheckBoxCaretaker(intent.getBooleanExtra("checkBoxCaretaker", true));
        return userModel;
    }

    //tut18
    public static void setProfilePic(Context context, Uri imageUri, ImageView imageView){
        Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform()).into(imageView);
    }
}
